/**
 * Classe que permet guardar la informació referent a un Token.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 05/03/2017
 */
public class Token {

    /**
     * Enumeració que inclou tots els tipus de token.
     */
    public enum TokenType {
        PROG,
        FIPROG,
        CONST,
        ID,
        IGUAL,
        PUNT_COMA,
        VAR,
        FUNC,
        FUNCIO,
        FIFUNC,
        PARENTESI_DAVANT,
        PARENTESI_DARRERE,
        TIPUS_SIMPLE,
        PERREF,
        PERVAL,
        PUNT_PUNT,
        VECTOR,
        DE,
        DOS_PUNTS,
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
        FISI,
        RETORNAR,
        PERCADA,
        EN,
        FIPER,
        OPER_REL,
        EOF
    }

    /* ************************** ATTRIBUTES ***************************/
    private TokenType tokenType; //Guarda quin tipus de token es tracta.
    private String lexeme; //Guarda el lexema del token.

    /* ************************* CONSTRUCTORS ***************************/
    /**
     * Consturcor buit de la classe token.
     */
    public Token () {

        lexeme = new String();
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

    /**
     * Setter del tipus de token a partir del lexema.
     * @param lexeme String amb el lexeme del qual s'ha d'extreure el tipus de Token.
     */
    public void setTokenTypeFromLexeme(String lexeme) {

        if (lexeme.equalsIgnoreCase(Lexicographical.VAR)) this.tokenType = TokenType.VAR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.CONST)) this.tokenType = TokenType.CONST;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PROG)) this.tokenType = TokenType.PROG;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FIPROG)) this.tokenType = TokenType.FIPROG;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FUNC)) this.tokenType = TokenType.FUNC;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FUNCIO)) this.tokenType = TokenType.FUNCIO;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FIFUNC)) this.tokenType = TokenType.FIFUNC;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PERREF)) this.tokenType = TokenType.PERREF;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PERVAL)) this.tokenType = TokenType.PERVAL;
        else if (lexeme.equalsIgnoreCase(Lexicographical.LLEGIR)) this.tokenType = TokenType.LLEGIR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.ESCRIURE)) this.tokenType = TokenType.ESCRIURE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.CICLE)) this.tokenType = TokenType.CICLE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FINS)) this.tokenType = TokenType.FINS;
        else if (lexeme.equalsIgnoreCase(Lexicographical.MENTRE)) this.tokenType = TokenType.MENTRE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FER)) this.tokenType = TokenType.FER;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FIMENTRE)) this.tokenType = TokenType.FIMENTRE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.SI)) this.tokenType = TokenType.SI;
        else if (lexeme.equalsIgnoreCase(Lexicographical.LLAVORS)) this.tokenType = TokenType.LLAVORS;
        else if (lexeme.equalsIgnoreCase(Lexicographical.SINO)) this.tokenType = TokenType.SINO;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FISI)) this.tokenType = TokenType.FISI;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PERCADA)) this.tokenType = TokenType.PERCADA;
        else if (lexeme.equalsIgnoreCase(Lexicographical.EN)) this.tokenType = TokenType.EN;
        else if (lexeme.equalsIgnoreCase(Lexicographical.FIPER)) this.tokenType = TokenType.FIPER;
        else if (lexeme.equalsIgnoreCase(Lexicographical.RETORNAR)) this.tokenType = TokenType.RETORNAR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.INTERROGANT)) this.tokenType = TokenType.INTERROGANT;
        else if (lexeme.equalsIgnoreCase(Lexicographical.COMA)) this.tokenType = TokenType.COMA;
        else if (lexeme.equalsIgnoreCase(Lexicographical.MES)) this.tokenType = TokenType.MES;
        else if (lexeme.equalsIgnoreCase(Lexicographical.MENYS)) this.tokenType = TokenType.MENYS;
        else if (lexeme.equalsIgnoreCase(Lexicographical.MULTIPLICAR)) this.tokenType = TokenType.MULTIPLICAR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PARENTESI_DAVANT)) this.tokenType = TokenType.PARENTESI_DAVANT;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PARENTESI_DARRERE)) this.tokenType = TokenType.PARENTESI_DARRERE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.CLAUDATOR_DAVANT)) this.tokenType = TokenType.CLAUDATOR_DAVANT;
        else if (lexeme.equalsIgnoreCase(Lexicographical.CLAUDATOR_DARRERE)) this.tokenType = TokenType.CLAUDATOR_DARRERE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.PUNT_COMA)) this.tokenType = TokenType.PUNT_COMA;
        else if (lexeme.equalsIgnoreCase(Lexicographical.DOS_PUNTS)) this.tokenType = TokenType.DOS_PUNTS;
        else if (lexeme.equalsIgnoreCase(Lexicographical.VECTOR)) this.tokenType = TokenType.VECTOR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.DE)) this.tokenType = TokenType.DE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.SENCER) || lexeme.equalsIgnoreCase(Lexicographical.LOGIC)) this.tokenType = TokenType.TIPUS_SIMPLE;
        else if (lexeme.equalsIgnoreCase(Lexicographical.NOT)) this.tokenType = TokenType.NOT;
        else if (lexeme.equalsIgnoreCase(Lexicographical.OR)) this.tokenType = TokenType.OR;
        else if (lexeme.equalsIgnoreCase(Lexicographical.AND)) this.tokenType = TokenType.AND;
        else if (lexeme.equalsIgnoreCase(Lexicographical.CERT) || lexeme.equalsIgnoreCase(Lexicographical.FALS)) this.tokenType = TokenType.CTE_LOGICA;
    }
}
