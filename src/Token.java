/**
 * Classe que permet guardar la informació referent a un Token.
 */
public class Token {

    /* ************************** ATTRIBUTES ***************************/
    private String tokenType; //Guarda quin tipus de token es tracta.
    private String lexeme; //Guarda el lexema del token.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor de la classe Token.
     * @param tokenType String que contingui el tipus de token del nou Token.
     * @param lexeme String que contingui el lexema del nou Token.
     */
    public Token (String tokenType, String lexeme) {

        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    /* *********************** GETTERS & SETTERS ************************/
    /**
     * Getter del lexema.
     * @return String que conté el lexema del Token.
     */
    public String getLexeme() {

        return lexeme;
    }

    /**
     * Getter del tipus de Token.
     * @return Stirng amb el tipus de token del Token.
     */
    public String getTokenType() {

        return tokenType;
    }

    /**
     * Setter del lexema.
     * @param lexeme String que conté el lexema que es vol assignar al Token.
     */
    public void setLexeme(String lexeme) {

        this.lexeme = lexeme;
    }

    /**
     * Setter del tipus de token.
     * @param tokenType Stirng amb el tipus de token que es vol assignar al Token.
     */
    public void setTokenType(String tokenType) {

        this.tokenType = tokenType;
    }
}
