package codeGeneration;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lexicographical.Lexicographical;
import semantic.Semantic;
import semantic.SemanticContainer;
import taulasimbols.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static java.lang.System.exit;

/**
 * Classe que implementa el generador de codi.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 7/7/17
 */
public class CodeGenerator {

    /* ************************* CONSTANTS ***************************/
    //CODE
    private static final String INITIAL_CODE = "\t.data\nerror:\n\t.asciiz \"[ERR_GC_1] Índex de vector fora de límits\"\necert:\n\t.asciiz \"CERT\"\nefals:\n\t.asciiz \"FALS\"\n";
    private static final String MAIN_CODE = ".text\nrerror:\n\tli $v0, 4\n\tla $a0, error\n\tsyscall\n\tli $v0, 10\n\tsyscall\nmain:";

    //IDENTIFIERS
    public static final String GLOBAL_POINTER = "$gp";
    private static final String ERROR_LABEL = "rerror";
    private static final String TRUE_LABEL = "ecert";
    private static final String FALSE_LABEL = "efals";
    private static final String CODE_LABEL = ".text";
    private static final String DATA_LABEL = ".data";

    //INSTRUCTIONS
    private static final String LOAD_WORD = "lw $%d, %d(%s)";
    private static final String STORE_WORD = "sw $%d, %d(%s)";
    private static final String LOAD_IMMEDIAT = "li $%d, %d";
    private static final String LOAD_ADRESS = "la $%d, %s";
    private static final String MOVE = "move $%d, $%d";

    private static final String ADD = "add $%d, $%d, $%d";
    private static final String MULTIPLICATION = "mul $%d, $%d, $%d";
    private static final String SUBSTRACT = "sub $%d, $%d, $%d";
    private static final String DIV = "div $%d, $%d, $%d";
    private static final String NEGAT_NUMBER = "neg $%d, $%d";

    private static final String NOT = "not $%d, $%d";
    private static final String AND = "and $%d, $%d, $%d";
    private static final String OR = "or $%d, $%d, $%d";

    private static final String EQUAL = "seq $%d, $%d, $%d";
    private static final String GREATER_THAN = "sgt $%d, $%d, $%d";
    private static final String LOWER_THAN = "slt $%d, $%d, $%d";
    private static final String GREATER_EQUAL_THAN = "sge $%d, $%d, $%d";
    private static final String LOWER_EQUAL_THAN = "sle $%d, $%d, $%d";
    private static final String DIFFERENT = "sne $%d, $%d, $%d";

    private static final String BRANCH = "b %s";
    private static final String BRANCH_LOWER_THAN = "blt $%d, $%d, %s";
    private static final String BRANCH_GREATER_THAN = "bgt $%d, $%d, %s";
    private static final String BRANCH_EQUAL_ZERO= "beq $%d, 0, %s";

    private static final String CADENA = ".asciiz \"%s\"";
    private static final String SYSTEM_CALL = "syscall";

    /* ************************* ATTRIBUTES ***************************/
    private PrintWriter fileWritter;

    private Registers registers;

    private int globalOffset;

    private int etiqueta;

    private static CodeGenerator instance;

    /* ************************* CONSTRUCTORS ***************************/
    public CodeGenerator(String fileName) {

        if (instance == null) {

            fileName += ".asm";
            globalOffset = 0;
            etiqueta = 0;
            registers = new Registers();

            try {
                fileWritter = new PrintWriter(fileName, "UTF-8");
                fileWritter.print(INITIAL_CODE);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                System.out.println("Error! El fitxer " + fileName + " no s'ha pogut crear.");
                exit(-1);
            }

            instance = this;
        }
    }

    public static CodeGenerator getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/
    public void closeFileWritter () {

        fileWritter.close();
    }

    public void writeLabel(String label) {

        fileWritter.println(label + ":");
    }

    public int getGlobalOffset(ITipus tipus) {

        int returnValue = globalOffset;

        if (tipus.getClass() == TipusSimple.class) globalOffset += 4;
        else if(tipus.getClass() == TipusArray.class) {
            TipusArray array = (TipusArray) tipus;
            globalOffset += (((int)array.obtenirDimensio(0).getLimitSuperior() - (int)array.obtenirDimensio(0).getLimitInferior() + 1) * 4);
        }

        return returnValue;
    }

    public int loadNumber(int value) {

        int register = registers.askForRegister();

        generateCode(String.format(LOAD_IMMEDIAT, register, value));
        return register;
    }

    public int loadBoolean(boolean value) {

        int register = registers.askForRegister();

        if (value) generateCode(String.format(LOAD_IMMEDIAT, register, 1));
        else generateCode(String.format(LOAD_IMMEDIAT, register, 0));

        return register;
    }

    public int oneNumberOperation(SemanticContainer number, String operation) {

        int register = -1;

        switch (operation) {

            case Lexicographical.MES:
                register = loadNumberOnRegister(number, GLOBAL_POINTER);
                break;

            case Lexicographical.MENYS:
                register = loadNumberOnRegister(number, GLOBAL_POINTER);
                generateCode(String.format(NEGAT_NUMBER, register, register));
                break;
        }

        return register;
    }

    public int twoNumberOperation(SemanticContainer number1, SemanticContainer number2, String operation) {

        int register, register2;

        register = loadNumberOnRegister(number1, GLOBAL_POINTER);
        register2 = loadNumberOnRegister(number2, GLOBAL_POINTER);

        switch (operation) {
            case Lexicographical.MES:
                generateCode(String.format(ADD, register, register, register2));
                break;

            case Lexicographical.MENYS:
                generateCode(String.format(SUBSTRACT, register, register, register2));
                break;

            case Lexicographical.MULTIPLICAR:
                generateCode(String.format(MULTIPLICATION, register, register, register2));
                break;

            case Lexicographical.DIVIDIR:
                generateCode(String.format(DIV, register, register, register2));
                break;
        }

        registers.freeRegister(register2);
        return register;
    }

    public int notOperation(SemanticContainer bool) {

        int register = loadBooleanOnRegister(bool, GLOBAL_POINTER);
        int register2 = registers.askForRegister();

        generateCode(String.format(NOT, register, register));
        generateCode(String.format(LOAD_IMMEDIAT, register2, 0x1));
        generateCode(String.format(AND, register, register, register2));

        return register;
    }

    public int booleanOperation(SemanticContainer bool1, SemanticContainer bool2, String operation) {

        int register = loadBooleanOnRegister(bool1, GLOBAL_POINTER);
        int register2 = loadBooleanOnRegister(bool2, GLOBAL_POINTER);

        switch (operation) {

            case Lexicographical.AND:
                generateCode(String.format(AND, register, register, register2));
                break;

            case Lexicographical.OR:
                generateCode(String.format(OR, register, register, register2));
                break;
        }

        registers.freeRegister(register2);
        return register;
    }

    public int relationalOperation(SemanticContainer exp1, SemanticContainer exp2, String operation) {

        int register, register2;

        if (((ITipus)exp1.getValue(SemanticContainer.TIPUS)).getNom().equals(Lexicographical.LOGIC)) register = loadBooleanOnRegister(exp1, GLOBAL_POINTER);
        else register = loadNumberOnRegister(exp1, GLOBAL_POINTER);

        if (((ITipus)exp2.getValue(SemanticContainer.TIPUS)).getNom().equals(Lexicographical.LOGIC)) register2 = loadBooleanOnRegister(exp2, GLOBAL_POINTER);
        else register2 = loadNumberOnRegister(exp2, GLOBAL_POINTER);

        switch (operation) {

            case "==":
                generateCode(String.format(EQUAL, register, register, register2));
                break;

            case "<":
                generateCode(String.format(LOWER_THAN, register, register, register2));
                break;

            case ">":
                generateCode(String.format(GREATER_THAN, register, register, register2));
                break;

            case "<=":
                generateCode(String.format(LOWER_EQUAL_THAN, register, register, register2));
                break;

            case ">=":
                generateCode(String.format(GREATER_EQUAL_THAN, register, register, register2));
                break;

            case "<>":
                generateCode(String.format(DIFFERENT, register, register, register2));
                break;
        }

        registers.freeRegister(register2);
        return register;
    }

    public void assignation(SemanticContainer var, int registre) {

        StringBuilder pointer = new StringBuilder(GLOBAL_POINTER);
        int desplacament = calculaDesplacament(var, pointer);

        generateCode(String.format(STORE_WORD, registre, desplacament, pointer));

        registers.freeRegister(registre);
    }

    public void escriure(SemanticContainer escriure) {

        ITipus tipus = (ITipus) escriure.getValue(SemanticContainer.TIPUS);
        int registre;

        switch (tipus.getNom()) {

            case Lexicographical.CADENA:
                generateCode(String.format(LOAD_IMMEDIAT, 2, 4));
                if (escriure.getValue(SemanticContainer.REG).getClass() == String.class) generateCode(String.format(LOAD_ADRESS, 4, (String) escriure.getValue(SemanticContainer.REG)));
                else generateCode(String.format(LOAD_ADRESS, 4, ((Constant)escriure.getValue(SemanticContainer.LINEA)).getNom()));
                generateCode(SYSTEM_CALL);
                break;

            case Lexicographical.SENCER:
                registre = loadNumberOnRegister(escriure, GLOBAL_POINTER);
                generateCode(String.format(LOAD_IMMEDIAT, 2, 1));
                generateCode(String.format(MOVE, 4, registre));
                generateCode(SYSTEM_CALL);
                registers.freeRegister(registre);
                break;

            case Lexicographical.LOGIC:
                registre = loadBooleanOnRegister(escriure, GLOBAL_POINTER);
                String eti1 = askForLabel();
                String eti2 = askForLabel();
                generateCode(String.format(LOAD_IMMEDIAT, 2, 4));
                generateCode(String.format(BRANCH_EQUAL_ZERO, registre, eti1));
                generateCode(String.format(LOAD_ADRESS, 4, TRUE_LABEL));
                generateCode(String.format(BRANCH, eti2));
                writeLabel(eti1);
                generateCode(String.format(LOAD_ADRESS, 4, FALSE_LABEL));
                writeLabel(eti2);
                generateCode(SYSTEM_CALL);
                registers.freeRegister(registre);
                break;
        }
    }

    public void llegir(SemanticContainer llegir) {

        StringBuilder stringBuilder = new StringBuilder(GLOBAL_POINTER);
        int desplacament = calculaDesplacament(llegir, stringBuilder);

        generateCode(String.format(LOAD_IMMEDIAT, 2, 5));
        generateCode(SYSTEM_CALL);
        generateCode(String.format(STORE_WORD, 2, desplacament, stringBuilder));
    }

    public String generateString(String cadena) {

        String eti = askForLabel();
        generateCode(DATA_LABEL);
        writeLabel(eti);
        generateCode(String.format(CADENA, cadena));
        generateCode(CODE_LABEL);

        return eti;
    }

    public void generateStringConstant(String id, String valor) {

        generateCode(DATA_LABEL);
        writeLabel(id);
        generateCode(String.format(CADENA, valor));
        generateCode(CODE_LABEL);
    }

    public void writeMain() {

        generateCode(MAIN_CODE);
    }

    public void generateBranchCode(SemanticContainer exp, String label) {

        generateCode(String.format(BRANCH_EQUAL_ZERO, exp.getValue(SemanticContainer.REG), label));
    }

    public void genereteInconditonalBranch(String label) {

        generateCode(String.format(BRANCH, label));
    }

    /* ************************* PRIVATE METHODS **************************/
    public int loadNumberOnRegister(SemanticContainer aux, String pointer) {

        int register, desplacament;
        StringBuilder auxPointer = new StringBuilder(pointer);

        if ((int) aux.getValue(SemanticContainer.REG) != -1) {
            register = (int) aux.getValue(SemanticContainer.REG);
        } else if ((boolean) aux.getValue(SemanticContainer.ESTATIC)) {
            register = registers.askForRegister();
            generateCode(String.format(LOAD_IMMEDIAT, register, (int)aux.getValue(SemanticContainer.VALOR)));
        } else {
            register = registers.askForRegister();
            desplacament = calculaDesplacament(aux, auxPointer);
            generateCode(String.format(LOAD_WORD, register, desplacament, auxPointer));
        }

        return register;
    }

    public int loadBooleanOnRegister(SemanticContainer aux, String pointer) {

        int register, desplacament;
        StringBuilder auxPointer = new StringBuilder(pointer);

        if ((int) aux.getValue(SemanticContainer.REG) != -1) {
            register = (int) aux.getValue(SemanticContainer.REG);
        } else if ((boolean) aux.getValue(SemanticContainer.ESTATIC)) {
            register = registers.askForRegister();
            if ((boolean) aux.getValue(SemanticContainer.VALOR)) generateCode(String.format(LOAD_IMMEDIAT, register, 1));
            else generateCode(String.format(LOAD_IMMEDIAT, register, 0));
        } else {
            register = registers.askForRegister();
            desplacament = calculaDesplacament(aux, auxPointer);
            generateCode(String.format(LOAD_WORD, register, desplacament, auxPointer));
        }

        return register;
    }

    private void generateCode(String code) {

        fileWritter.println("\t" + code);
    }

    private int calculaDesplacament(SemanticContainer aux, StringBuilder stringBuilder) {

        SemanticContainer casella = (SemanticContainer) aux.getValue(SemanticContainer.COLUMNA);
        Variable variable = (Variable) aux.getValue(SemanticContainer.LINEA);
        ITipus tipus = variable.getTipus();
        int register, register2 = -1;

        if (tipus.getClass() == TipusSimple.class) return -variable.getDesplacament();
        else if ((boolean) casella.getValue(SemanticContainer.ESTATIC)) {

            TipusArray array = (TipusArray) tipus;
            return - (variable.getDesplacament() + ((int)casella.getValue(SemanticContainer.VALOR) - (int) array.obtenirDimensio(0).getLimitInferior()) * 4);
        } else {

            TipusArray array = (TipusArray) tipus;
            register = registers.askForRegister();
            generateCode(String.format(LOAD_IMMEDIAT, register, (int) array.obtenirDimensio(0).getLimitInferior()));
            if ((int) casella.getValue(SemanticContainer.REG) == -1) register2 = loadNumberOnRegister(casella, GLOBAL_POINTER);
            else register2 = (int) casella.getValue(SemanticContainer.REG);
            generateCode(String.format(BRANCH_LOWER_THAN, register2, register, ERROR_LABEL));
            generateCode(String.format(LOAD_IMMEDIAT, register, (int) array.obtenirDimensio(0).getLimitSuperior()));
            generateCode(String.format(BRANCH_GREATER_THAN, register2, register, ERROR_LABEL));
            generateCode(String.format(LOAD_IMMEDIAT, register, (int) array.obtenirDimensio(0).getLimitInferior()));
            generateCode(String.format(SUBSTRACT, register2, register2, register));
            generateCode(String.format(LOAD_IMMEDIAT, register, 4));
            generateCode(String.format(MULTIPLICATION, register2, register2, register));
            generateCode(String.format(LOAD_IMMEDIAT, register, variable.getDesplacament()));
            generateCode(String.format(ADD, register2, register2, register));
            generateCode(String.format(SUBSTRACT, register2, 28, register2));
            stringBuilder.replace(0, stringBuilder.length(), "$" + register2);

            registers.freeRegister(register);
            return 0;
        }
    }

    public String askForLabel() {

        etiqueta++;
        return "eti" + (etiqueta - 1);
    }
}
