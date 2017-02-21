import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexicographical {

    private int actualLine;
    private int actualState;
    private int endToken;

    private char actualChar;

    private boolean eof;

    private Token actualToken;

    private FileInputStream inputStream;

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

    private void getNextChar() {

        try {
            int aux = inputStream.read();

            if (aux == -1) eof = true;
            else actualChar = (char) aux;
        } catch (IOException e) {
            //control d'errors
        }
    }
}
