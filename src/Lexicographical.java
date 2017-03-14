/* **************************** IMPORTS *****************************/
import java.io.*;
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

    private static final int MAX_ID_LENGTH = 32;


    /* ************************** ATTRIBUTES ***************************/
    private int actualLine; //Control de la línia del fitxer en que l'analitzador es troba.
    private int actualState; //Estat en el que es troba el motor de l'analitzador.
    private int endToken; //Permet indicar quan s'ha trobat un token.

    private char actualChar; //Buffer per gurdar el caràcter següent que analitzarà el motor.

    private boolean eof; //Permet indicar quan s'ha arribat a final de fitxer.

    private Token actualToken; //Conté informació sobre el token que s'està tractant.

    private FileInputStream inputStream; //Permet llegir caràcters del fitxer.

    private LinkedList<String> keyWords; //Llista amb totes les paraules claus.

    private PrintWriter fileWritter; //Objecte per poder escriure el fitxer de lexemes.

    private static Lexicographical instance; //Instancia del singleton.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor del analitzador, que incialitze les variables necessaries i obre el fitxer.
     * @param fileName String que ha de contenir el nom del fitxer on es troba el programa que s'ha de compilar.
     */
    public Lexicographical(String fileName) {


        if (instance == null) {
            String fileNameLex = fileName + ".lex";
            fileName += ".bab";

            actualLine = 1;
            actualState = 0;
            eof = false;
            this.initializeKeyWords();

            try {
                inputStream = new FileInputStream(fileName);
                fileWritter = new PrintWriter(fileNameLex, "UTF-8");

            } catch (FileNotFoundException e) {
                System.out.println("Error! El fitxer " + fileName + " no existeix.");
                exit(-1);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Error! El fitxer " + fileNameLex + " no s'ha pogut crear.");
                exit(-1);
            }

            instance = this;
        }
    }

    public static Lexicographical getInstance() {

        return instance;
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

        fileWritter.println("[ " + actualToken.getTokenType() + ", " + actualToken.getLexeme() + " ]");
        if (eof) fileWritter.close();

        return actualToken;
    }

    /**
     * Procediment que tanca el fitxer d'entrada del programa que s'esta compilant.
     */
    public void closeInputSteram () {

        try {
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error! No s'ha tancat correctament el fitxer .bab.");
            exit(-1);
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
        } else if (actualChar == ' ' || actualChar == '\r' || actualChar == '\t'){
            return 0;
        }else if (actualChar == '\n'){
            actualLine++;
            return 0;
        } else {
            Error.getInstance().writeError(0, this.actualLine, this.actualChar);
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
        } else if (eof) {
            actualState = 0;
            return 3;
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
                this.actualToken.setLexeme(this.actualToken.getLexeme().toLowerCase());
                actualState = 0;
                return 2;
            } else {
                if (this.actualToken.getLexeme().length() > MAX_ID_LENGTH) {
                    Error.getInstance().writeWarning(0, this.actualLine, this.actualToken.getLexeme(), this.actualToken.getLexeme().substring(0, MAX_ID_LENGTH));
                    this.actualToken.setLexeme(this.actualToken.getLexeme().substring(0, MAX_ID_LENGTH));
                }
                this.actualToken.setTokenType(Token.TokenType.ID);
                this.actualToken.setLexeme(this.actualToken.getLexeme().toLowerCase());
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

        if (eof) {
            Error.getInstance().writeError(1, this.actualLine);
            actualState = 0;
            return 1;
        } else if (actualChar != '"' && actualChar != '\n' && actualChar != '\r') {
            this.actualToken.setLexeme(this.actualToken.getLexeme() + actualChar);
            return 0;
        } else {
            if (actualChar == '\n' || actualChar == '\r') {
                Error.getInstance().writeError(1, this.actualLine);
                if (actualChar == '\n') this.actualLine++;
            }
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
            Error.getInstance().writeError(0, this.actualLine, '.');
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

    /*public static void main(String args[]) {

        String[] auxFileName = args[0].split(".bab");

        Lexicographical lex = new Lexicographical(auxFileName[0]);
        Error error = new Error(auxFileName[0]);
        Token aux;

        aux = lex.getToken();
        while (aux.getTokenType() != Token.TokenType.EOF) {
            aux = lex.getToken();
        }

        error.closeFileWriter();
        lex.closeInputSteram();
    }*/
}
