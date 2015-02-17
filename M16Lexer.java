/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micro16;

import micro16.M16Optimizer;

/**
 *
 * @author Alexander Poschenreithner <alexander.poschenreithner@gmail.com>
 */
public class M16Lexer {

    /**
     * Pattern for Register Names: R0-r8
     */
    protected static final String patternRegister = "[rR][01234567]";

    /**
     * Assign patterns: <, =, <-
     */
    protected static final String patternAssigner = "(\\<(-)*|\\=)";

    /**
     * Pattern for "R1<", "R3<-" or "R7="
     */
    protected static final String patternAssign = patternRegister + patternAssigner;

    /**
     * Pattern for "R1<R3", "R3<-R9" or "R7=r4"
     */
    protected static final String patternAssignWithRegister = patternAssign + patternRegister;

    /**
     * Pattern for R1=R3+R5
     */
    protected static final String patternAdd = patternAssignWithRegister + "\\+" + patternRegister;

    /**
     * Pattern for R1=R3+R5
     */
    protected static final String patternSubstract = patternAssignWithRegister + "\\-" + patternRegister;


    //protected static final String patternMultipylByTwo = patternAssign + "(" + patternRegister + "\\*2|2\\*" + patternRegister + ")";

    protected static final String patternMultiplyByNumber = patternAssign + "(" + patternRegister + "\\*[0-9]+|[0-9]+\\*" + patternRegister + ")";

    /**
     * Pattern for R1=R3*R5
     */
    protected static final String patternMultiplyByRegister = patternAssignWithRegister + "\\*" + patternRegister;

    /**
     * Pattern for R1=R3:R5
     */
    protected static final String patternDivideByRegister = patternAssignWithRegister + "\\:" + patternRegister;


    public static void lexer() {

    }

    /**
     *
     * @param line
     * @return
     */
    public String syntax(String line) {

        M16Optimizer opt = new M16Optimizer();

        line = line.replaceAll("\\#.*", ""); //remove all comments
        line = line.replaceAll("\\s+", ""); //remove all whitespaces

        if (line.matches(patternMultiplyByNumber)) {
            line = opt.multiplicationByNumber(line);
        }

        if (line.matches(patternMultiplyByRegister)) {
            line = opt.multiplicationByReg(line);
        }

        if (line.matches(patternSubstract)) {
            line = opt.substraction(line);
        }

        if (line.matches(patternDivideByRegister)) {
            line = opt.divideByRegister(line);
        }

        if (line.matches(patternAdd)) {
        }

        if (line.matches(patternAssignWithRegister)) {
        }

        if (line.matches(patternRegister)) {
            System.out.println("Only a Register: " + line);
        }

        return new String();
    }

}
