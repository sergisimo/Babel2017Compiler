import java.io.IOException;
import java.io.PrintWriter;

public class Error {

    /* ************************** CONSTANTS ***************************/
    private static final String FILE_ERROR_EXTENSION = ".err";

    private static final String LINE_NUMBER_REPLACER = "$LN";
    private static final String INFO_REPLACER = "$INFO";
    private static final String INFO_1_REPLACER = "$1INFO";

    static final String ERR_LEX_1_CODE = "ERR_LEX1";
    static final String ERR_LEX_1 = "[ERR_LEX_1] $LN, Caràcter [$INFO] desconegut";

    static final String WAR_LEX_1_CODE = "WAR_LEX1";
    static final String WAR_LEX_1_DESCRIPTION = "[WAR_LEX_1] $LN, Identificador [$INFO] té més de 20 caràcters, l'identificador es tallarà per [$1INFO]";

    /* ************************** ATTRIBUTES ***************************/
    PrintWriter fileWritter;

    /* ************************* CONSTRUCTORS ***************************/
    public Error (String fileName) {

        fileName += FILE_ERROR_EXTENSION;

        try {
            fileWritter = new PrintWriter(fileName, "UTF-8");
        } catch (IOException e) {
            //Control d'errors
        }
    }

    /* ************************* PUBLIC METHODS **************************/
    public void closeFileWriter() {

        fileWritter.close();
    }

    public void writeError (String code, int line, Object extraInformation) {

        switch (code) {

            case ERR_LEX_1_CODE:
                String aux = ERR_LEX_1.replace(LINE_NUMBER_REPLACER, Integer.toString(line));
                aux = aux.replace(INFO_REPLACER, Character.toString((char)extraInformation));
                fileWritter.println(aux);
                break;
        }
    }

    public void writeWarning (String code, int line, Object extraInformation) {

        switch (code) {

            case WAR_LEX_1_CODE:
                String aux = WAR_LEX_1_DESCRIPTION.replace(LINE_NUMBER_REPLACER, Integer.toString(line));
                String id = (String) extraInformation;
                String cuttedId = id.substring(0, 20);

                aux = aux.replace(INFO_REPLACER, id);
                aux = aux.replace(INFO_1_REPLACER, cuttedId);
                fileWritter.println(aux);
                break;
        }
    }


}
