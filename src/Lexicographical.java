/* **************************** IMPORTS *****************************/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import static java.lang.System.exit;

/**
 * Classe que implementa l'analitzador lexicogràfic del compilador de Babel2017.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 05/03/2017
 */
public class Lexicographical {

    /* ************************** CONSTANTS ***************************/
    static final String VAR = "var";
    static final String CONST = "const";
    static final String PROG = "prog";
    static final String FIPROG = "fiprog";
    static final String FUNC = "func";
    static final String FUNCIO = "funcio";
    static final String FIFUNC = "fifunc";
    static final String PERREF = "perref";
    static final String PERVAL = "perval";
    static final String LLEGIR = "llegir";
    static final String ESCRIURE = "escriure";
    static final String CICLE = "cicle";
    static final String FINS = "fins";
    static final String MENTRE = "mentre";
    static final String FER = "fer";
    static final String FIMENTRE = "fimentre";
    static final String SI = "si";
    static final String LLAVORS = "llavors";
    static final String SINO = "sino";
    static final String FISI = "fisi";
    static final String PERCADA = "percada";
    static final String EN = "en";
    static final String FIPER = "fiper";
    static final String RETORNAR = "retornar";
    static final String VECTOR = "vector";
    static final String DE = "de";
    static final String SENCER = "sencer";
    static final String LOGIC = "logic";
    static final String NOT = "not";
    static final String OR = "or";
    static final String AND = "and";
    static final String CERT = "cert";
    static final String FALS = "fals";
    static final String INTERROGANT = "?";
    static final String COMA = ",";
    static final String MES = "+";
    static final String MENYS = "-";
    static final String MULTIPLICAR = "*";
    static final String PARENTESI_DAVANT = "(";
    static final String PARENTESI_DARRERE = ")";
    static final String CLAUDATOR_DAVANT = "[";
    static final String CLAUDATOR_DARRERE = "]";
    static final String PUNT_COMA = ";";
    static final String DOS_PUNTS = ":";
    private static final String EOF = "EOF";
    private static final String DIVIDIR = "/";


    /* ************************** ATTRIBUTES ***************************/
    private int actualLine; //Control de la línia del fitxer en que l'analitzador es troba.
    private int actualState; //Estat en el que es troba el motor de l'analitzador.
    private int endToken; //Permet indicar quan s'ha trobat un token.

    private char actualChar; //Buffer per gurdar el caràcter següent que analitzarà el motor.

    private boolean eof; //Permet indicar quan s'ha arribat a final de fitxer.

    private Token actualToken; //Conté informació sobre el token que s'està tractant.

    private FileInputStream inputStream; //Permet llegir caràcters del fitxer.

    private LinkedList<String> keyWords; //Llista amb totes les paraules claus.

    private Error error; //Objecte que permet escriure errors.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor del analitzador, que incialitze les variables necessaries i obre el fitxer.
     * @param fileName String que ha de contenir el nom del fitxer on es troba el programa que s'ha de compilar.
     * @param error Objecte error que ha de permetre escriure errors en el fitxer d'error.
     */
    public Lexicographical(String fileName, Error error) {

        actualLine = 1;
        actualState = 0;
        eof = false;
        this.initializeKeyWords();
        this.error = error;

        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error! El fitxer " + fileName + " no existeix.");
            exit(-1);
        }
    }

    /* ************************* PUBLIC METHODS **************************/
    /**
     * Funció encarregade de executar el motor de l'analitzador fins que aquest extregui el següent token del document.
     * @return El següent Token trobat en el programa que s'està compilant.
     */
    public Token getToken() {

        boolean readChar = true;
        this.actualToken = new Token();

        if (endToken == 2) readChar = false;

        endToken = 0;

        while (endToken == 0 || endToken == 3) {

            if (endToken == 3) readChar = false;

            if (readChar) this.getNextChar();
            else readChar = true;

            endToken = this.lexicographicalEngine();
        }

        return actualToken;
    }

    /**
     * Procediment que tanca el fitxer d'entrada del programa que s'esta compilant.
     */
    public void closeInputSteram () {

        try {
            inputStream.close();
        } catch (IOException e) {
                //CONTROL ERRORS
        }
    }

    /* *********************** GETTERS & SETTERS ************************/
    /**
     * Getter de la línia actual.
     * @return El número de línia en la qual es troba l'analitzador.
     */
    public int getActualLine() {

        return actualLine;
    }

    /* ************************ PRIVATE METHODS *************************/

    /*
     * Funció que implementa el motor del lexicogràfic.
     */
    private int lexicographicalEngine() {

        switch (actualState) {

            case 0: return this.state0();

            case 1: return this.state1();

            case 2: return this.state2();

            case 3: return this.state3();

            case 4: return this.state4();

            case 5: return this.state5();

            case 6: return this.state6();

            case 7: return this.state7();

            case 8: return this.state8();

            case 9: return this.state9();

            default: return 0;
        }
    }

    /*
     * Funció que implementa l'estat 0 del motor del lexicogràfic.
     */
    private int state0 () {

        if (eof) {
            this.actualToken.setTokenType(Token.TokenType.EOF);
            this.actualToken.setLexeme(EOF);
            return 1;
        } else if (actualChar == '/') {
            actualState = 1;
            return 0;
        } else if ((actualChar >= 'a' && actualChar <= 'z') || (actualChar >= 'A' && actualChar <= 'Z')){
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            actualState = 3;
            return 0;
        } else if (actualChar >= '0' && actualChar <= '9') {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            actualState = 4;
            return 0;
        } else if (actualChar == '"') {
            actualState = 5;
            return 0;
        } else if (actualChar == ':' || actualChar == '?' || actualChar == ',' || actualChar == '+' || actualChar == '-' || actualChar == '*' || actualChar == '(' || actualChar == ')' || actualChar == '[' || actualChar == ']' || actualChar == ';') {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            this.actualToken.setTokenTypeFromLexeme(this.actualToken.getLexeme());
            return 1;
        } else if (actualChar == '.') {
            actualState = 6;
            return 0;
        } else if (actualChar == '<') {
            actualState = 7;
            return 0;
        } else if (actualChar == '>') {
            actualState = 8;
            return 0;
        } else if (actualChar == '=') {
            actualState = 9;
            return 0;
        } else if (actualChar == ' ' || actualChar == '\r'){
            return 0;
        }else if (actualChar == '\n'){
            actualLine++;
            return 0;
        } else {
            error.writeError(Error.ERR_LEX_1_CODE, this.actualLine, this.actualChar);
            return 0;
        }
    }

    /*
     * Funció que implementa l'estat 1 del motor del lexicogràfic.
     */
    private int state1 () {

        if (actualChar == '/') {
            actualState = 2;
            return 0;
        } else {
            this.actualToken.setTokenType(Token.TokenType.DIVIDIR);
            this.actualToken.setLexeme(DIVIDIR);
            actualState = 0;
            return 2;
        }
    }

    /*
     * Funció que implementa l'estat 2 del motor del lexicogràfic.
     */
    private int state2 () {

        if (actualChar == '\n') {
            actualLine++;
            actualState = 0;
        }
        return 0;
    }

    /*
     * Funció que implementa l'estat 3 del motor del lexicogràfic.
     */
    private int state3 () {

        if ((actualChar >= 'a' && actualChar <= 'z') || (actualChar >= 'A' && actualChar <= 'Z') || (actualChar >= '0' && actualChar <= '9') || (actualChar == '_') ) {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            return 0;
        } else {
            if (this.containsKeyWord(this.actualToken.getLexeme())) {
                this.actualToken.setTokenTypeFromLexeme(this.actualToken.getLexeme());
                actualState = 0;
                return 2;
            } else {
                if (this.actualToken.getLexeme().length() > 20) {
                    error.writeWarning(Error.WAR_LEX_1_CODE, this.actualLine, this.actualToken.getLexeme());
                    this.actualToken.setLexeme(this.actualToken.getLexeme().substring(0, 20));
                }
                this.actualToken.setTokenType(Token.TokenType.ID);
                actualState = 0;
                return 2;
            }
        }
    }

    /*
     * Funció que implementa l'estat 4 del motor del lexicogràfic.
     */
    private int state4() {

        if (actualChar >= '0' && actualChar <= '9') {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            return 0;
        } else {
            this.actualToken.setTokenType(Token.TokenType.CTE_ENTERA);
            actualState = 0;
            return 2;
        }
    }

    /*
     * Funció que implementa l'estat 5 del motor del lexicogràfic.
     */
    private int state5() {

        if (actualChar != '"') {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            return 0;
        } else {
            this.actualToken.setTokenType(Token.TokenType.CTE_CADENA);
            actualState = 0;
            return 1;
        }
    }

    /*
     * Funció que implementa l'estat 6 del motor del lexicogràfic.
     */
    private int state6() {

        if (actualChar == '.') {
            this.actualToken.setLexeme("..");
            this.actualToken.setTokenType(Token.TokenType.PUNT_PUNT);
            actualState = 0;
            return 1;
        } else {
            error.writeError(Error.ERR_LEX_1_CODE, this.actualLine, '.');
            actualState = 0;
            return  3;
        }
    }

    /*
     * Funció que implementa l'estat 7 del motor del lexicogràfic.
     */
    private int state7() {

        actualState = 0;
        this.actualToken.setTokenType(Token.TokenType.OPER_REL);
        if (actualChar != '=' && actualChar != '>') {
            this.actualToken.setLexeme("<");
            return 2;
        } else if (actualChar == '=') {
            this.actualToken.setLexeme("<=");
            return 1;
        } else {
            this.actualToken.setLexeme("<>");
            return 1;
        }
    }

    /*
     * Funció que implementa l'estat 8 del motor del lexicogràfic.
     */
    private int state8 () {

        actualState = 0;
        this.actualToken.setTokenType(Token.TokenType.OPER_REL);
        if (actualChar != '=') {
            this.actualToken.setLexeme(">");
            return 2;
        } else {
            this.actualToken.setLexeme(">=");
            return 1;
        }
    }

    /*
     * Funció que implementa l'estat 9 del motor del lexicogràfic.
     */
    private int state9 () {

        actualState = 0;
        if (actualChar == '=') {
            this.actualToken.setTokenType(Token.TokenType.OPER_REL);
            this.actualToken.setLexeme("==");
            return 1;
        } else {
            this.actualToken.setTokenType(Token.TokenType.IGUAL);
            this.actualToken.setLexeme("=");
            return 2;
        }
    }

    /*
     * Funció que permet llegir el següent caràcter del fitxer, controlant el final de fitxer.
     */
    private void getNextChar() {

        try {
            int aux = inputStream.read();

            if (aux == -1) eof = true;
            else actualChar = (char) aux;
        } catch (IOException e) {
            //control d'errors
        }
    }

    /*
     * Procediment que inicialitza la llista que conté les paraules claus
     */
    private void initializeKeyWords() {

        keyWords = new LinkedList<>();

        keyWords.add(VAR);
        keyWords.add(CONST);
        keyWords.add(PROG);
        keyWords.add(FIPROG);
        keyWords.add(FUNC);
        keyWords.add(FUNCIO);
        keyWords.add(FIFUNC);
        keyWords.add(PERREF);
        keyWords.add(PERVAL);
        keyWords.add(LLEGIR);
        keyWords.add(ESCRIURE);
        keyWords.add(CICLE);
        keyWords.add(FINS);
        keyWords.add(MENTRE);
        keyWords.add(FER);
        keyWords.add(FIMENTRE);
        keyWords.add(SI);
        keyWords.add(LLAVORS);
        keyWords.add(SINO);
        keyWords.add(FISI);
        keyWords.add(PERCADA);
        keyWords.add(EN);
        keyWords.add(FIPER);
        keyWords.add(RETORNAR);
        keyWords.add(SENCER);
        keyWords.add(LOGIC);
        keyWords.add(AND);
        keyWords.add(OR);
        keyWords.add(NOT);
        keyWords.add(CERT);
        keyWords.add(FALS);
        keyWords.add(VECTOR);
        keyWords.add(DE);
    }

    /*
     * Funció que retorna cert si la String que es passa per paràmetre és una paraula clau
     */
    private boolean containsKeyWord(String keyWord) {

        for (String aux : keyWords) if (aux.equalsIgnoreCase(keyWord)) return true;
        return false;
    }

    /* *** MAIN PROVISIONAL ******/
    public static void main (String args[]) {

        if (args.length != 1) {
            System.out.println("Error! Parametres introduits incorrectement. [EX] \"java -jar Babel2017Compiler.jar programa1.bab\"");
        } else {
            String[] auxFileName = args[0].split(".bab");
            Error error = new Error(auxFileName[0]);
            Lexicographical lexicographical = new Lexicographical(args[0], error);
            Token aux;
            String fileName = auxFileName[0] + ".lex";

            try {
                PrintWriter fileWritter = new PrintWriter(fileName, "UTF-8");

                do {
                    aux = lexicographical.getToken();
                    fileWritter.println("[ " + aux.getTokenType() + ", " + aux.getLexeme() + " ]");
                } while (aux.getTokenType() != Token.TokenType.EOF);

                fileWritter.close();
            } catch (IOException e) {
                System.out.println("Error de I/O en la execució!");
                exit(-1);
            }

            error.closeFileWriter();
            lexicographical.closeInputSteram();
        }
    }
}
