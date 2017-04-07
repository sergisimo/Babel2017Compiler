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
            "[ERR_LEX_2] %d, Els delimitadors de la constant cadena [\"] no estan tancats",
            "[ERR_SIN_1] %d, ERROR en el retornar de la funció",
            "[ERR_SIN_2] %d, Error en la capçalera del SI",
            "[ERR_SIN_3] %d, Error, la declaració de la constant no és correcta",
            "[ERR_SIN_4] %d, Error, la declaració de la variable no és correcta",
            "[ERR_SIN_5] %d, Error en la capçalera del MENTRE",
            "[ERR_SIN_6] %d, Hi ha codi després de la fi del programa",
            "[ERR_SIN_7] %d, Construcció d'instrucció [%s] incorrecta",
            "[ERR_SIN_8] %d, Error en el pas de parametres de la funció",
            "[ERR_SIN_9] %d, Error al declarar el nom de la funció",
            "[ERR_SIN_10] %d, El procediment principal conté errors",
            "[ERR_SIN_11] %d, Error en el retorn de la capçalera de la funció",
            "[ERR_SIN_12] %d, Construcció de funció incorrecte",
            "[ERR_SIN_13] %d, S'esperaven el token [%s] però ha aparegut en l'entrada el token [%s]",
            "[ERR_SIN_14] %d, Error, el tipus de vector ha de ser SENCER o LOGIC",
            "[ERR_SIN_15] %d, Error en la declaració de la instrucció",
            "[ERR_SIN_16] %d, Error, la declaració del vector no és correcta",
            "[ERR_SIN_17] %d, L'operand de la operació conté errors",
            "[ERR_SIN_18] %d, ERROR falta declarar el tipus",
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
