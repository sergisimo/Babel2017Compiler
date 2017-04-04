/* **************************** IMPORTS *****************************/

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.util.LinkedList;

/**
 * Classe que implementa l'analitzador sintàctic del compilador de Babel2017.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 13/03/2017
 */
public class Syntactic {

    /* ************************** ATTRIBUTES ***************************/
    private Token lookAhead;

    private static Syntactic instance;

    /* ************************* CONSTRUCTORS ***************************/
    private Syntactic(String fileName) {

        if (instance == null) {
            new Error(fileName);
            new Lexicographical(fileName);
            new SynchronizationSets();

            instance = this;
        }
    }

    private static Syntactic getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/


    /* ************************ PRIVATE METHODS *************************/
    private void compile() {

        this.lookAhead = Lexicographical.getInstance().getToken();
        this.P();
    }

    private void accept (Token.TokenType tokenType) throws ParseException {

        if (tokenType == lookAhead.getTokenType()) lookAhead = Lexicographical.getInstance().getToken();
        else throw new ParseException(lookAhead.getLexeme(), 0);
    }

    private void consume(LinkedList<Token.TokenType> tokenList) {

        while (!tokenList.contains(lookAhead.getTokenType())) lookAhead = Lexicographical.getInstance().getToken();
    }

    private void P() {

        try {
            this.Decl();
            this.accept(Token.TokenType.PROG);
            this.Llista_Inst();
            this.accept(Token.TokenType.FIPROG);
        } catch (ParseException e) {
            Error.getInstance().writeError(10, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[0]);
        }

        try {
            this.accept(Token.TokenType.EOF);
        } catch (ParseException e) {
            Error.getInstance().writeError(7, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[30]);
        }
    }

    private void Decl() {

        this.Decl_Cte_Var();
        this.Dec_Fun();
    }

    private void Decl_Cte_Var() {
        switch (lookAhead.getTokenType()) {

            case CONST:
            case VAR:
                this.Decl_CV();
                this.Decl_Cte_Var();
                break;

            default:
                break;
        }
    }

    private void Decl_CV() {

        switch (lookAhead.getTokenType()) {

            case CONST:
                this.Dec_Cte();
                break;

            case VAR:
                this.Dec_Var();
                break;
        }
    }

    private void Dec_Cte() {

        this.accept(Token.TokenType.CONST);
        try {
            this.accept(Token.TokenType.ID);
            this.accept(Token.TokenType.IGUAL);
            this.Exp();
            this.accept(Token.TokenType.PUNT_COMA);
        } catch (ParseException e) {
            Error.getInstance().writeError(4, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[1]);
        }
    }

    private void Dec_Var() {

        this.accept(Token.TokenType.VAR);
        try {
            this.accept(Token.TokenType.ID);
            this.accept(Token.TokenType.DOS_PUNTS);
            this.Tipus();
            this.accept(Token.TokenType.PUNT_COMA);
        } catch (ParseException e) {
            Error.getInstance().writeError(5, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[2]);
        }
    }

    private void Dec_Fun() {

        switch (lookAhead.getTokenType()) {

            case FUNCIO:
                this.accept(Token.TokenType.FUNCIO);

                try {
                    this.accept(Token.TokenType.ID);
                    this.accept(Token.TokenType.PARENTESI_DAVANT);
                } catch (ParseException e) {
                    Error.getInstance().writeError(2, Lexicographical.getInstance().getActualLine(), "id", "(", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[3]);
                }

                this.Llista_Param();
                try {
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                    this.accept(Token.TokenType.DOS_PUNTS);
                    this.accept(Token.TokenType.TIPUS_SIMPLE);
                    this.accept(Token.TokenType.PUNT_COMA);
                } catch (ParseException e) {
                    Error.getInstance().writeError(12, Lexicographical.getInstance().getActualLine());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[4]);
                }

                this.Decl_Cte_Var();
                try {
                    this.accept(Token.TokenType.FUNC);
                    this.Llista_Inst();
                    this.accept(Token.TokenType.FIFUNC);
                    this.accept(Token.TokenType.PUNT_COMA);
                } catch (ParseException e) {
                    Error.getInstance().writeError(13, Lexicographical.getInstance().getActualLine());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[5]);
                }

                this.Dec_Fun();
                break;

            default:
                break;
        }
    }

    private void Llista_Param() {

        switch (lookAhead.getTokenType()) {

            case PERREF:
            case PERVAL:
                this.Llista_Param1();
                break;

            default:
                break;
        }
    }

    private void Llista_Param1() {

        this.PasValor();
        try {
            this.accept(Token.TokenType.ID);
            this.accept(Token.TokenType.DOS_PUNTS);
        } catch (ParseException e) {
            Error.getInstance().writeError(2, Lexicographical.getInstance().getActualLine(), "id", ":", lookAhead.getLexeme());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[6]);
        }
        this.Tipus();
        this.Llista_Param2();
    }

    private void Llista_Param2() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                try {
                    this.Llista_Param1();
                } catch (ParseException e) {
                    Error.getInstance().writeError(2, Lexicographical.getInstance().getActualLine(), "perref", "perval", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[7]);
                }
                break;

            default:
                break;
        }
    }

    private void PasValor() throws ParseException{

        switch (lookAhead.getTokenType()) {

            case PERREF:
                this.accept(Token.TokenType.PERREF);
                break;

            case PERVAL:
                this.accept(Token.TokenType.PERVAL);
                break;

            default:
                throw new ParseException(lookAhead.getLexeme(), 1);
        }
    }

    private void Tipus() {

        this.Tipus_Abr();
        try {
            this.accept(Token.TokenType.TIPUS_SIMPLE);
        } catch (ParseException e) {
            Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "tipus_simple", lookAhead.getLexeme());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[8]);
        }
    }

    private void Tipus_Abr() {

        switch (lookAhead.getTokenType()) {

            case VECTOR:
                this.accept(Token.TokenType.VECTOR);
                try {
                    this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                    this.Exp();
                    this.accept(Token.TokenType.PUNT_PUNT);
                    this.Exp();
                    this.accept(Token.TokenType.CLAUDATOR_DARRERE);
                    this.accept(Token.TokenType.DE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(15, Lexicographical.getInstance().getActualLine());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[9]);
                }
                break;

            default:
                break;
        }
    }

    private void Exp() {

        this.Exp_Simple();
        this.Exp1();
    }

    private void Exp1() {

        switch (lookAhead.getTokenType()) {

            case OPER_REL:
                this.accept(Token.TokenType.OPER_REL);
                this.Exp_Simple();
                break;

            default:
                break;
        }
    }

    private void Exp2() {

        switch (lookAhead.getTokenType()) {

            case MES:
            case MENYS:
            case NOT:
            case CTE_ENTERA:
            case CTE_LOGICA:
            case CTE_CADENA:
            case PARENTESI_DAVANT:
            case ID:
                this.Exp3();
                break;

            default:
                break;
        }
    }

    private void Exp3() {

        this.Exp();
        this.Exp4();
    }

    private void Exp4() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                this.Exp3();
                break;

            default:
                break;
        }
    }

    private void Exp_Simple() {

        this.Exp_Simple1();
        this.Terme();
        this.Terme1();
    }

    private void Exp_Simple1() {

        switch (lookAhead.getTokenType()) {

            case MES:
                this.accept(Token.TokenType.MES);
                break;

            case MENYS:
                this.accept(Token.TokenType.MENYS);
                break;

            case NOT:
                this.accept(Token.TokenType.NOT);
                break;

            default:
                break;
        }
    }

    private void Terme() {

        try {
            this.Factor();
        } catch (ParseException e) {
            Error.getInstance().writeError(18, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[10]);
        }
        this.Factor1();
    }

    private void Terme1() {

        switch (lookAhead.getTokenType()) {

            case MES:
            case MENYS:
            case OR:
                this.Terme2();
                this.Terme();
                this.Terme1();
                break;

            default:
                break;
        }
    }

    private void Terme2() {

        switch (lookAhead.getTokenType()) {

            case MES:
                this.accept(Token.TokenType.MES);
                break;

            case MENYS:
                this.accept(Token.TokenType.MENYS);
                break;

            case OR:
                this.accept(Token.TokenType.OR);
                break;
        }
    }

    private void Factor() throws ParseException {

        switch (lookAhead.getTokenType()) {

            case CTE_ENTERA:
                this.accept(Token.TokenType.CTE_ENTERA);
                break;

            case CTE_LOGICA:
                this.accept(Token.TokenType.CTE_LOGICA);
                break;

            case CTE_CADENA:
                this.accept(Token.TokenType.CTE_CADENA);
                break;

            case PARENTESI_DAVANT:
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Exp();
                try {
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), ")", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[12]);
                }
                break;

            case ID:
                this.accept(Token.TokenType.ID);
                try {
                    this.Variable5();
                } catch (ParseException e) {
                    Error.getInstance().writeError(16, Lexicographical.getInstance().getActualLine());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[13]);
                }
                break;

            default:
                throw new ParseException(lookAhead.getLexeme(), 1);
        }
    }

    private void Factor1() {

        switch (lookAhead.getTokenType()) {

            case MULTIPLICAR:
            case DIVIDIR:
            case AND:
                this.Factor2();
                try {
                    this.Factor();
                } catch (ParseException e) {
                    Error.getInstance().writeError(18, Lexicographical.getInstance().getActualLine());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[11]);
                }

                this.Factor1();
                break;

            default:
                break;
        }
    }

    private void Factor2() {

        switch (lookAhead.getTokenType()) {

            case MULTIPLICAR:
                this.accept(Token.TokenType.MULTIPLICAR);
                break;

            case DIVIDIR:
                this.accept(Token.TokenType.DIVIDIR);
                break;

            case AND:
                this.accept(Token.TokenType.AND);
                break;
        }
    }

    private void Variable() {

        try {
            this.accept(Token.TokenType.ID);
        } catch (ParseException e) {
            Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "id", lookAhead.getLexeme());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[15]);
        }
        this.Variable1();
    }

    private void Variable1() {

        switch (lookAhead.getTokenType()) {

            case CLAUDATOR_DAVANT:
                this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                this.Exp();
                try {
                    this.accept(Token.TokenType.CLAUDATOR_DARRERE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "]", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[16]);
                }
                break;

            default:
                break;
        }
    }

    private void Variable2() {

        this.Variable2_abr();
        this.Exp();
    }

    private void Variable2_abr() {

        switch (lookAhead.getTokenType()) {

            case SI:
                this.accept(Token.TokenType.SI);
                try {
                    this.accept(Token.TokenType.PARENTESI_DAVANT);
                    this.Exp();
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                    this.accept(Token.TokenType.INTERROGANT);
                    this.Exp();
                    this.accept(Token.TokenType.DOS_PUNTS);
                } catch (ParseException e) {
                    Error.getInstance().writeError(8, Lexicographical.getInstance().getActualLine(), "Condicional Ternari");
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[19]);
                }
                break;

            default:
                break;
        }
    }

    private void Variable3() {

        this.Variable();
        this.Variable4();
    }

    private void Variable4() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                this.Variable3();
                break;

            default:
                break;
        }
    }

    private void Variable5() throws ParseException {

        switch (lookAhead.getTokenType()) {

            case PARENTESI_DAVANT:
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Exp2();
                try {
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), ")", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[14]);
                }
                break;

            case CLAUDATOR_DAVANT:
            case IGUAL:
            case COMA:
            case MULTIPLICAR:
            case DIVIDIR:
            case AND:
            case PARENTESI_DARRERE:
            case MES:
            case MENYS:
            case OR:
            case OPER_REL:
            case PUNT_PUNT:
            case PUNT_COMA:
            case CLAUDATOR_DARRERE:
            case DOS_PUNTS:
            case FER:
            case LLAVORS:
                this.Variable1();
                break;

            default:
                throw new ParseException(lookAhead.getLexeme(), 1);
        }
    }

    private void Llista_Inst() {

        try {
            this.Inst();
        } catch (ParseException e) {
            Error.getInstance().writeError(17, Lexicographical.getInstance().getActualLine());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[17]);
        }

        try {
            this.accept(Token.TokenType.PUNT_COMA);
        } catch (ParseException e) {
            Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), ";", lookAhead.getLexeme());
            this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[18]);
        }

        this.Llista_Inst1();
    }

    private void Llista_Inst1() {

        switch (lookAhead.getTokenType()) {

            case ID:
            case ESCRIURE:
            case LLEGIR:
            case CICLE:
            case MENTRE:
            case SI:
            case RETORNAR:
            case PERCADA:
                this.Llista_Inst();
                break;

            default:
                break;
        }
    }

    private void Inst() throws ParseException {

        switch (lookAhead.getTokenType()) {

            case ID:
                this.Variable();
                try {
                    this.accept(Token.TokenType.IGUAL);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "=", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[20]);
                }
                this.Variable2();
                break;

            case ESCRIURE:
                this.accept(Token.TokenType.ESCRIURE);
                try {
                    this.accept(Token.TokenType.PARENTESI_DAVANT);
                    this.ExpEscriure();
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(8, Lexicographical.getInstance().getActualLine(), "escriure");
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[21]);
                }
                break;

            case LLEGIR:
                this.accept(Token.TokenType.LLEGIR);
                try {
                    this.accept(Token.TokenType.PARENTESI_DAVANT);
                    this.Variable3();
                    this.accept(Token.TokenType.PARENTESI_DARRERE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(8, Lexicographical.getInstance().getActualLine(), "llegir");
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[22]);
                }
                break;

            case CICLE:
                this.accept(Token.TokenType.CICLE);
                try {
                    this.Llista_Inst();
                    this.accept(Token.TokenType.FINS);
                    this.Exp();
                } catch (ParseException e) {
                    Error.getInstance().writeError(8, Lexicographical.getInstance().getActualLine(), "cicle");
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[23]);
                }
                break;

            case MENTRE:
                this.accept(Token.TokenType.MENTRE);
                this.Exp();
                try {
                    this.accept(Token.TokenType.FER);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "fer", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[24]);
                }

                this.Llista_Inst();
                try {
                    this.accept(Token.TokenType.FIMENTRE);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "fimentre", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[25]);
                }
                break;

            case SI:
                this.accept(Token.TokenType.SI);
                this.Exp();
                try {
                    this.accept(Token.TokenType.LLAVORS);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "llavors", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[26]);
                }

                this.Llista_Inst();
                this.Inst1();
                try {
                    this.accept(Token.TokenType.FISI);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "fisi", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[27]);
                }
                break;

            case RETORNAR:
                this.accept(Token.TokenType.RETORNAR);
                this.Exp();
                break;

            case PERCADA:
                this.accept(Token.TokenType.PERCADA);
                try {
                    this.accept(Token.TokenType.ID);
                    this.accept(Token.TokenType.EN);
                    this.accept(Token.TokenType.ID);
                    this.accept(Token.TokenType.FER);
                } catch (ParseException e) {
                    Error.getInstance().writeError(8, Lexicographical.getInstance().getActualLine(), "percada");
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[28]);
                }

                this.Llista_Inst();
                try {
                    this.accept(Token.TokenType.FIPER);
                } catch (ParseException e) {
                    Error.getInstance().writeError(14, Lexicographical.getInstance().getActualLine(), "fiper", lookAhead.getLexeme());
                    this.consume(SynchronizationSets.getInstance().getSynchronizationSets()[29]);
                }
                break;

            default:
                throw new ParseException(lookAhead.getLexeme(), 1);
        }
    }

    private void Inst1() {

        switch (lookAhead.getTokenType()) {

            case SINO:
                this.accept(Token.TokenType.SINO);
                this.Llista_Inst();
                break;

            default:
                break;
        }
    }

    private void ExpEscriure() {

        this.Exp();
        this.ExpEscriure1();
    }

    private void ExpEscriure1() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                this.ExpEscriure();
                break;

            default:
                break;
        }
    }

    /* *** MAIN PROVISIONAL ******/
    public static void main (String args[]) {

        if (args.length != 1) System.out.println("Error! Parametres introduits incorrectement. [EX] \"java -jar Babel2017Compiler.jar programa1.bab\"");
        else {
            String[] auxFileName = args[0].split(".bab");

            new Syntactic(auxFileName[0]);

            Syntactic.getInstance().compile();

            Error.getInstance().closeFileWriter();
            Lexicographical.getInstance().closeInputSteram();
        }
    }
}
