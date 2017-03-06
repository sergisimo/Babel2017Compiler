/* **************************** IMPORTS *****************************/
import java.io.IOException;
import java.io.PrintWriter;

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

    private static final String LINE_NUMBER_REPLACER = "$LN";
    private static final String INFO_REPLACER = "$INFO";
    private static final String INFO_1_REPLACER = "$1INFO";

    static final String ERR_LEX_1_CODE = "ERR_LEX1";
    private static final String ERR_LEX_1 = "[ERR_LEX_1] $LN, Caràcter [$INFO] desconegut";

    static final String WAR_LEX_1_CODE = "WAR_LEX1";
    private static final String WAR_LEX_1 = "[WAR_LEX_1] $LN, Identificador [$INFO] té més de 20 caràcters, l'identificador es tallarà per [$1INFO]";

    /* ************************** ATTRIBUTES ***************************/
    PrintWriter fileWritter; //Eina per escriure en el fitxer d'error.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor de la classe Error.
     * @param fileName String amb el nom del programa que s'està compilant.
     */
    public Error (String fileName) {

        fileName += FILE_ERROR_EXTENSION;

        try {
            fileWritter = new PrintWriter(fileName, "UTF-8");
        } catch (IOException e) {
            //Control d'errors
        }
    }

    /* ************************* PUBLIC METHODS **************************/

    /**
     * Procediment que permet tancar el fitxer d'errors.
     */
    public void closeFileWriter() {

        fileWritter.close();
    }

    /**
     * Procediment que permet escriure un error en el fitxer d'error.
     * @param code Codi del error que es vol escriure.
     * @param line Linia en la qual es troba l'error.
     * @param extraInformation Informacio extra per complementar l'error. (El seu tipus depen del error).
     */
    public void writeError (String code, int line, Object extraInformation) {

        switch (code) {

            case ERR_LEX_1_CODE:
                String aux = ERR_LEX_1.replace(LINE_NUMBER_REPLACER, Integer.toString(line));
                aux = aux.replace(INFO_REPLACER, Character.toString((char)extraInformation));
                fileWritter.println(aux);
                break;
        }
    }

    /**
     * Procediment que permet escriure un warning en el fitxer d'error.
     * @param code Codi del warning que es vol escriure.
     * @param line Linia en la qual es troba el warning.
     * @param extraInformation Informacio extra per complementar el warning. (El seu tipus depen del warning).
     */
    public void writeWarning (String code, int line, Object extraInformation) {

        switch (code) {

            case WAR_LEX_1_CODE:
                String aux = WAR_LEX_1.replace(LINE_NUMBER_REPLACER, Integer.toString(line));
                String id = (String) extraInformation;
                String cuttedId = id.substring(0, 20);

                aux = aux.replace(INFO_REPLACER, id);
                aux = aux.replace(INFO_1_REPLACER, cuttedId);
                fileWritter.println(aux);
                break;
        }
    }
}
