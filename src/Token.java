/**
 * Classe que permet guardar la informació referent a un Token.
 */
public class Token {

    public enum TokenType {
        PROG,
        FI_PROG,
        CONST,
        ID,
        IGUAL,
        PUNT_COMA,
        VAR,
        FUNC,
        FI_FUNC,
        PARENTESI_DAVANT,
        PARENTESI_DARRERE,
        TIPUS_SIMPLE,
        PER_REF,
        PER_VAL,
        PUNT_PUNT,
        COMA,
        MES,
        MENYS,
        NOT,
        OR,
        MULTIPLICAR,
        DIVIDIR,
        AND,
        CTE_ENTERA,
        CTE_LOGICA,
        CTE_CADENA,
        CLAUDATOR_DAVANT,
        CLAUDATOR_DARRERE,
        INTERROGANT,
        ESCRIURE,
        LLEGIR,
        CICLE,
        FINS,
        MENTRE,
        FER,
        FIMENTRE,
        SI,
        LLAVORS,
        SINO,
        FI_SI,
        RETORNAR,
        PERCADA,
        EN,
        FIPER,
        OPER_REL
    }

    /* ************************** ATTRIBUTES ***************************/
    private TokenType tokenType; //Guarda quin tipus de token es tracta.
    private String lexeme; //Guarda el lexema del token.



    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Constructor de la classe Token.
     * @param tokenType String que contingui el tipus de token del nou Token.
     * @param lexeme String que contingui el lexema del nou Token.
     */
    public Token (TokenType tokenType, String lexeme) {

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
    public TokenType getTokenType() {

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
    public void setTokenType(TokenType tokenType) {

        this.tokenType = tokenType;
    }
}
