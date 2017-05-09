package utils;/* **************************** IMPORTS *****************************/
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
            "[ERR_SIN_11] %d, Error,la especificació del retorn de la funció conté errors",
            "[ERR_SIN_12] %d, Construcció de funció incorrecte",
            "[ERR_SIN_13] %d, S'esperaven el token [%s] però ha aparegut en l'entrada el token [%s]",
            "[ERR_SIN_14] %d, Error, el tipus de vector ha de ser SENCER o LOGIC",
            "[ERR_SIN_15] %d, Error en la declaració de la instrucció",
            "[ERR_SIN_16] %d, Error, la declaració del vector no és correcta",
            "[ERR_SIN_17] %d, L'operand de la operació conté errors",
            "[ERR_SIN_18] %d, ERROR falta declarar el tipus",
            //ERRORS SEMANTICS - COMENÇA CASELLA 20
            "[ERR_SEM_1] %d, Constant %s doblement definida",
            "[ERR_SEM_2] %d, Variable %s doblement definida",
            "[ERR_SEM_3] %d, Funció %s doblement definida",
            "[ERR_SEM_4] %d, Paràmetre %s doblement definit",
            "[ERR_SEM_5] %d, Límits decreixents en vector",
            "[ERR_SEM_6] %d, El tipus de l'expressió no és SENCER",
            "[ERR_SEM_7] %d, El tipus de l'expressió no és LOGIC",
            "[ERR_SEM_8] %d, La condició no és de tipus LOGIC",
            "[ERR_SEM_9] %d, L'identificador %s no ha estat declarat",
            "[ERR_SEM_10] %d, L'identificador %s en la instrucció LLEGIR no és una variable de tipus simple",
            "[ERR_SEM_11] %d, L'identificador %s en part esquerra d'assignació no és una variable",
            "[ERR_SEM_12] %d, La variable i l'expressió d'assignació tenen tipus diferents. El tipus de la variable és %s i el de l'expressió és %s",
            "[ERR_SEM_13] %d, El tipus de l'índex d'accés del vector no és SENCER",
            "[ERR_SEM_14] %d, El tipus de la expressió ESCRIURE no és simple o no és una constant cadena",
            "[ERR_SEM_15] %d, La funció en declaració té %d paràmetres mentre que en ús en té %d",
            "[ERR_SEM_16] %d, El tipus del paràmetre número %d de la funció no coincideix amb el tipus en la seva declaració %s",
            "[ERR_SEM_17] %d, El paràmetre número %d de la funció no es pot passar per referència",
            "[ERR_SEM_18] %d, La funció %s ha de ser del tipus %s però en la expressió del seu valor és del tipus %s",
            "[ERR_SEM_19] %d, Retornar fora de funció",
            "[ERR_SEM_20] %d, L'expressió no és estàtica",
            "[ERR_SEM_21] %d, L'identificador %sno ha estat declarat com a funció",
            "[ERR_SEM_22] %d, L'identificador %s no ha estat declarat com a variable o constant",
            "[ERR_SEM_23] %d, La variable %s en part esquerra d'assignació no és de tipus simple",
            "[ERR_SEM_24] %d, La expressió assignada a la constant %s no és de tipus simple o de tipus cadena",
            "[ERR_SEM_25] %d, L'identificador %s no ha estat declarat com a vector",
            "[ERR_SEM_26] %d, El valor d'accés al vector %d excedeix els límits declarats",
            "[ERR_SEM_27] %d, Els dos operands no són del mateix tipus",
            "[ERR_SEM_28] %d, Una de les parts de l'assignació no és de tipus simple",
            "[ERR_SEM_29] %d, La funció %s no conté un retornar",
            "[ERR_SEM_30] %d, La expressió del retornar és de tipus indefinit"
    };


    private static final String[] WARNINGS = {
            "[WAR_LEX_1] %d, Identificador %s té més de 32 caràcters, l'identificador es tallarà per %s"
    };

    private static final String[] FATAL_ERRORS = {
            "Error Sintàctic! El compilador aborta la seva execució.",
            "Error, Fitxer corrupte! El compilador aborta la seva execució."
    };

    /* ************************** ATTRIBUTES ***************************/
    private PrintWriter fileWritter; //Eina per escriure en el fitxer d'error.

    private static Error instance; //Instancia del singleton.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor de la classe utils.Error.
     * @param fileName String amb el nom del programa que s'està compilant.
     */
    public Error (String fileName) {

        if (instance == null) {
            fileName += FILE_ERROR_EXTENSION;

            try {
                fileWritter = new PrintWriter(fileName, "UTF-8");
            } catch (IOException e) {
                System.out.println("utils.Error! El fitxer " + fileName + " no s'ha pogut crear.");
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

    public void writeFatalError(int errorCode) {

        System.out.println(FATAL_ERRORS[errorCode]);
        exit(-1);
    }
}
