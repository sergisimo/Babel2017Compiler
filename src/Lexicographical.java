/* **************************** IMPORTS *****************************/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe que implementa l'analitzador lexicogràfic del compilador de Babel2017.
 */
public class Lexicographical {

    /* ************************** ATTRIBUTES ***************************/
    private int actualLine; //Control de la línia del fitxer en que l'analitzador es troba.
    private int actualState; //Estat en el que es troba el motor de l'analitzador.
    private int endToken; //Permet indicar quan s'ha trobat un token.

    private char actualChar; //Buffer per gurdar el caràcter següent que analitzarà el motor.

    private boolean eof; //Permet indicar quan s'ha arribat a final de fitxer.

    private Token actualToken; //Conté informació sobre el token que s'està tractant.

    private FileInputStream inputStream; //Permet llegir caràcters del fitxer.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor del analitzador, que incialitze les variables necessaries i obre el fitxer.
     * @param fileName String que ha de contenir el nom del fitxer on es troba el programa que s'ha de compilar.
     */
    public Lexicographical(String fileName) {

        actualLine = 0;
        actualState = 0;
        eof = false;

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


    /* *** MAIN DE PROVA ******/
    public static void main (String args[]) {}
}
