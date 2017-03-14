/* **************************** IMPORTS *****************************/
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.exit;

/**
 * Classe que implementa tots els mecanismes per la correcta creació i escritura del fitxer de errors.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 05/03/2017
 */
public class Error {

    /* ************************** CONSTANTS ***************************/
    private static final String FILE_ERROR_EXTENSION = ".err";

    private static final String[] ERRORS = {
            "[ERR_LEX_1] %d, Caràcter %c desconegut",
            "[ERR_LEX_2] %d, Els delimitadors de la constant cadena [\"] no estan tancats"
    };


    private static final String[] WARNINGS = {
            "[WAR_LEX_1] %d, Identificador %s té més de 32 caràcters, l'identificador es tallarà per %s"
    };

    /* ************************** ATTRIBUTES ***************************/
    PrintWriter fileWritter; //Eina per escriure en el fitxer d'error.

    private static Error instance; //Instancia del singleton.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor de la classe Error.
     * @param fileName String amb el nom del programa que s'està compilant.
     */
    public Error (String fileName) {

        if (instance == null) {
            fileName += FILE_ERROR_EXTENSION;

            try {
                fileWritter = new PrintWriter(fileName, "UTF-8");
            } catch (IOException e) {
                System.out.println("Error! El fitxer " + fileName + " no s'ha pogut crear.");
                exit(-1);
            }

            instance = this;
        }
    }

    public static Error getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/

    /**
     * Procediment que permet tancar el fitxer d'errors.
     */
    public void closeFileWriter() {

        fileWritter.close();
    }

    public void writeError(int errorCode, Object ... args) {

        fileWritter.println(String.format(ERRORS[errorCode], args));
    }

    public void writeWarning(int warningCode, Object ... args) {

        fileWritter.println(String.format(WARNINGS[warningCode], args));
    }
}
