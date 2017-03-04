/* **************************** IMPORTS *****************************/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Classe que implementa l'analitzador lexicogràfic del compilador de Babel2017.
 */
public class Lexicographical {

    /* ************************** CONSTANTS ***************************/
    private static final String VAR = "var";
    private static final String CONST = "const";
    private static final String PROG = "prog";
    private static final String FIPROG = "fiprog";
    private static final String FUNC = "func";
    private static final String FIFUNC = "fifunc";
    private static final String PERREF = "perref";
    private static final String PERVAL = "perval";
    private static final String LLEGIR = "llegir";
    private static final String ESCRIURE = "escriure";
    private static final String CICLE = "cicle";
    private static final String FINS = "fins";
    private static final String MENTRE = "mentre";
    private static final String FER = "fer";
    private static final String FIMENTRE = "fimentre";
    private static final String SI = "si";
    private static final String LLAVORS = "llavors";
    private static final String SINO = "sino";
    private static final String FISI = "fisi";
    private static final String PERCADA = "percada";
    private static final String EN = "en";
    private static final String FIPER = "fiper";
    private static final String RETORNAR = "retornar";

    /* ************************** ATTRIBUTES ***************************/
    private int actualLine; //Control de la línia del fitxer en que l'analitzador es troba.
    private int actualState; //Estat en el que es troba el motor de l'analitzador.
    private int endToken; //Permet indicar quan s'ha trobat un token.

    private char actualChar; //Buffer per gurdar el caràcter següent que analitzarà el motor.

    private boolean eof; //Permet indicar quan s'ha arribat a final de fitxer.

    private Token actualToken; //Conté informació sobre el token que s'està tractant.

    private FileInputStream inputStream; //Permet llegir caràcters del fitxer.

    private LinkedList<String> keyWords; //Llista amb totes les paraules claus.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor del analitzador, que incialitze les variables necessaries i obre el fitxer.
     * @param fileName String que ha de contenir el nom del fitxer on es troba el programa que s'ha de compilar.
     */
    public Lexicographical(String fileName) {

        actualLine = 0;
        actualState = 0;
        eof = false;
        this.initializeKeyWords();

        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            //Control de errors
        }
    }

    /* ************************* PUBLIC METHODS **************************/
    /**
     * Funció encarregade de executar el motor de l'analitzador fins que aquest extregui el següent token del document.
     * @return El següent Token trobat en el programa que s'està compilant.
     */
    public Token getToken() {

        boolean readChar = true;

        if (endToken == 2) {
            readChar = false;
            endToken = 0;
        }

        while (endToken == 0) {

            if (readChar) getNextChar();
            else readChar = true;

            //endtoken = motor();
        }

        return actualToken;
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
     * Procediment que permet llegir el següent caràcter del fitxer, controlant el final de fitxer.
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
    }

    /*
     * Procediment que retorna cert si la String que es passa per paràmetre és una paraula clau
     */
    private boolean containsKeyWord(String keyWord) {

        for (String aux : keyWords) if (aux.equalsIgnoreCase(keyWord)) return true;
        return false;
    }

    /* *** MAIN DE PROVA ******/
    public static void main (String args[]) {}
}
