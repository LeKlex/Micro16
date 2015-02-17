/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micro16;

import java.security.SecureRandom;
import java.math.BigInteger;

/**
 *
 * @author Alexander Poschenreithner <alexander.poschenreithner@gmail.com>
 */
public class M16Optimizer {

    private static SecureRandom random = new SecureRandom();

    public M16Optimizer() {

    }

    public static String getSecureLabel() {
        return new BigInteger(130, random).toString(32);
    }


    /**
     *
     * @param cmd
     * @return
     */
    protected static String[] splitCommand(String cmd) {
        String[] splitted = new String[3];
        String[] firstReg = cmd.split(M16Lexer.patternAssigner);
        String[] operands = firstReg[1].split("[\\-\\+\\*\\:]");

        splitted[0] = firstReg[0];
        splitted[1] = operands[0];
        splitted[2] = operands[1];

        return splitted;
    }

    public static String substraction(String cmd) {
        String[] splitted = splitCommand(cmd);
        cmd = splitted[0] + "<-"; // Rx<-
        if (splitted[1].equalsIgnoreCase(splitted[2])) {
            cmd += "0"; //Rx = R1-R1, so just set to 0
        } else {
            //z = x+(-y)
            /*
             R2<-~R1 # R1 negiert nach R2 gespeichert
             R2<-R2+1 # R2 enthaelt nun -y
             R2<-R2+R0 # berechne x+(-y)
             */
            cmd += "~" + splitted[2] + "\n"; //negate second Register, Rx = ~Ry
            cmd += splitted[0] + "<-" + splitted[0] + "+1\n"; //now negated register Rx = Rx+1
            cmd += splitted[0] + "<-" + splitted[0] + "+" + splitted[1]; //now Rx=Rx+(-Ry)
        }
        System.out.println(cmd);
        return cmd;
    }

    public static String addition(String cmd) {
        return cmd;
    }

    protected static String multiplicationByReg(String cmd) {
        String[] splitted = splitCommand(cmd);
        String id = getSecureLabel();

        /*
         R7<-0
         :loop
         (R6&1);if Z goto .zero
         R7<-R7+R5
         R6<-rsh(R6) :zero
         (R6); if Z goto .end
         R5<-lsh(R5)
         goto .loop
         :end
         */
        cmd = splitted[0] + "<-0\n" //Set target reg to 0
                + ":loop" + id + "\n" //begin of loop
                + "(" + splitted[2] + "&1);if Z goto .zero" + id + "\n"
                + splitted[0] + "<-" + splitted[0] + "+" + splitted[1] + "\n"
                + splitted[2] + "<-rsh(" + splitted[2] + ") :zero" + id + "\n"
                + "(" + splitted[2] + "); if Z goto .end" + id + "\n"
                + splitted[1] + "<-lsh(" + splitted[1] + ")" + "\n"
                + "goto .loop" + id + "\n"
                + ":end" + id + "\n";

        System.out.println(cmd);

        return cmd;
    }

    /**
     * Check if a string is a number (integer)
     *
     * @param s
     * @return
     */
    protected static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    protected static String multiplicationByNumber(String cmd) {
        //X = Y*Z
        String[] splitted = splitCommand(cmd);
        Integer multiplier = 0;
        String multiplicand = new String();

        if (isInteger(splitted[1])) {
            multiplier = Integer.parseInt(splitted[1]);
            multiplicand = splitted[2];
        } else {
            multiplier = Integer.parseInt(splitted[2]);
            multiplicand = splitted[1];
        }

        //At this time only power of 2 numbers
        if (isPowerOfTwo(multiplier)) {
            int num = (int) (Math.log(multiplier) / Math.log(2)); //calc 2^x = multiplier
            cmd = "";

            for (int i = 0; i < num - 1; i++) {
                if (i % 2 == 0) {
                    cmd += splitted[0] + "<-lsh(" + multiplicand + "+" + multiplicand + ")\n";
                }
            }
            if (num % 2 == 1) {
                cmd += splitted[0] + "<-(" + multiplicand + "+" + multiplicand + ")\n";
            }

        } else {
            cmd = "#Sorry only power of 2 implemented yet!";
        }

        System.out.println("#Multiplikator: " + multiplier + ", Multiplikant: " + multiplicand);
        System.out.println(cmd);

        return cmd;
    }

    private static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number: " + number);
        }
        if ((number & -number) == number) {
            return true;
        }
        return false;
    }

    public static String divideByRegister(String cmd) {
        String[] splitted = splitCommand(cmd);
        String id = getSecureLabel();

        cmd = splitted[0] + "<-0\n"; //Set result register to 0
        cmd += "AC<-~" + splitted[2] + "\n"; //We use AC to store the negated divisor, to keep all as it is
        cmd += "AC<-AC+1\n"; //Add 1 for two's complement
        cmd += "R10<-" + splitted[1] + "\n"; //we use R10
        cmd += "R10<-R10+AC :divide" + id +"\n"; //Substract divisor
        cmd += splitted[0] + "<-"+ splitted[0] +"+1\n"; //add 1
        cmd += "(~R10); if N goto .divide"+id+"\n"; //repeate until R10 becomes negative
        cmd += splitted[0] + "<-" + splitted[0] + "+ -1\n"; //substract one form result since we iterate one time too often

        System.out.println(cmd);

        return cmd;
    }

}
