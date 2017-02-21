public class Token {

    private String tokenType;
    private String lexeme;

    public Token (String tokenType, String lexeme) {

        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public String getLexeme() {

        return lexeme;
    }

    public String getTokenType() {

        return tokenType;
    }

    public void setLexeme(String lexeme) {

        this.lexeme = lexeme;
    }

    public void setTokenType(String tokenType) {

        this.tokenType = tokenType;
    }
}
