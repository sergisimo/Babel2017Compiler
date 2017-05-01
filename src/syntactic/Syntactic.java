package syntactic;
/* **************************** IMPORTS *****************************/
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import lexicographical.Lexicographical;
import lexicographical.Token;
import semantic.Semantic;
import taulasimbols.ITipus;
import utils.Error;

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
            //new SynchronizationSets();
            new Semantic();

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
        else Error.getInstance().writeFatalError(0);
    }

    /*private void consume(LinkedList<Token.TokenType> tokenList) {

        while (!tokenList.contains(lookAhead.getTokenType())) lookAhead = Lexicographical.getInstance().getToken();
    }

    private boolean consumeErr(LinkedList<Token.TokenType> tokenList) {

        boolean esta = false;
        while (!tokenList.contains(lookAhead.getTokenType())){
            if (!esta) {
                Error.getInstance().writeError(19, Lexicographical.getInstance().getActualLine());
                esta = true;
            }
            lookAhead = Lexicographical.getInstance().getToken();
        }
        return esta;
    }*/

    private void P() {

        Semantic.getInstance().addBloc(true);
        this.Decl();
        this.accept(Token.TokenType.PROG);
        this.Llista_Inst();
        this.accept(Token.TokenType.FIPROG);
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
        this.accept(Token.TokenType.ID);
        this.accept(Token.TokenType.IGUAL);
        this.Exp();
        this.accept(Token.TokenType.PUNT_COMA);
    }

    private void Dec_Var() {

        ITipus tipus;
        String id;

        this.accept(Token.TokenType.VAR);
        id = lookAhead.getLexeme();
        this.accept(Token.TokenType.ID);
        this.accept(Token.TokenType.DOS_PUNTS);
        tipus = this.Tipus();
        this.accept(Token.TokenType.PUNT_COMA);
        Semantic.getInstance().addVariable(id, tipus);
    }

    private void Dec_Fun() {

        switch (lookAhead.getTokenType()) {

            case FUNCIO:
                this.accept(Token.TokenType.FUNCIO);
                this.accept(Token.TokenType.ID);
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Llista_Param();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                this.accept(Token.TokenType.DOS_PUNTS);
                this.accept(Token.TokenType.TIPUS_SIMPLE);
                this.accept(Token.TokenType.PUNT_COMA);
                Semantic.getInstance().addBloc(false);
                this.Decl_Cte_Var();
                this.accept(Token.TokenType.FUNC);
                this.Llista_Inst();
                this.accept(Token.TokenType.FIFUNC);
                Semantic.getInstance().removeBloc();
                this.accept(Token.TokenType.PUNT_COMA);
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
        this.accept(Token.TokenType.ID);
        this.accept(Token.TokenType.DOS_PUNTS);
        this.Tipus();
        this.Llista_Param2();
    }

    private void Llista_Param2() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                this.Llista_Param1();

                break;

            default:
                break;
        }
    }

    private void PasValor() {

        switch (lookAhead.getTokenType()) {

            case PERREF:
                this.accept(Token.TokenType.PERREF);
                break;

            case PERVAL:
                this.accept(Token.TokenType.PERVAL);
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
        }
    }

    private ITipus Tipus() {

        ITipus tipus = this.Tipus_Abr();
        tipus = Semantic.getInstance().generateTipus(lookAhead.getLexeme(), tipus);
        this.accept(Token.TokenType.TIPUS_SIMPLE);
        return tipus;
    }

    private ITipus Tipus_Abr() {

        switch (lookAhead.getTokenType()) {

            case VECTOR:
                this.accept(Token.TokenType.VECTOR);
                this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                this.Exp();
                this.accept(Token.TokenType.PUNT_PUNT);
                this.Exp();
                this.accept(Token.TokenType.CLAUDATOR_DARRERE);
                this.accept(Token.TokenType.DE);
                break;

            default:
                break;
        }

        return null;
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

        this.Factor();
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

    private void Factor() {

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
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                break;

            case ID:
                this.accept(Token.TokenType.ID);
                this.Variable5();
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
        }
    }

    private void Factor1() {

        switch (lookAhead.getTokenType()) {

            case MULTIPLICAR:
            case DIVIDIR:
            case AND:
                this.Factor2();
                this.Factor();
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


        this.accept(Token.TokenType.ID);
        this.Variable1();
    }

    private void Variable1() {

        switch (lookAhead.getTokenType()) {

            case CLAUDATOR_DAVANT:
                this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                this.Exp();
                this.accept(Token.TokenType.CLAUDATOR_DARRERE);
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

    private void Variable5() {

        switch (lookAhead.getTokenType()) {

            case PARENTESI_DAVANT:
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Exp2();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
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

            default: Error.getInstance().writeFatalError(0);
                break;
        }
    }

    private void Llista_Inst() {

        this.Inst();
        this.accept(Token.TokenType.PUNT_COMA);
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

    private void Inst() {

        switch (lookAhead.getTokenType()) {

            case ID:
                this.Variable();
                this.accept(Token.TokenType.IGUAL);
                this.Exp();
                break;

            case ESCRIURE:
                this.accept(Token.TokenType.ESCRIURE);
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.ExpEscriure();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                break;

            case LLEGIR:
                this.accept(Token.TokenType.LLEGIR);
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Variable3();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                break;

            case CICLE:
                this.accept(Token.TokenType.CICLE);
                this.Llista_Inst();
                this.accept(Token.TokenType.FINS);
                this.Exp();
                break;

            case MENTRE:
                this.accept(Token.TokenType.MENTRE);
                this.Exp();
                this.accept(Token.TokenType.FER);
                this.Llista_Inst();
                this.accept(Token.TokenType.FIMENTRE);
                break;

            case SI:
                this.accept(Token.TokenType.SI);
                this.Exp();
                this.accept(Token.TokenType.LLAVORS);
                this.Llista_Inst();
                this.Inst1();
                this.accept(Token.TokenType.FISI);
                break;

            case RETORNAR:
                this.accept(Token.TokenType.RETORNAR);
                this.Exp();
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
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

   /*private void errorPrincipi() {

        boolean a = false;
        do {
            if (Token.TokenType.PUNT_COMA == lookAhead.getTokenType())
                lookAhead = Lexicographical.getInstance().getToken();
            a = this.consumeErr((SynchronizationSets.getInstance().getSynchronizationSets()[31]));
            if (Token.TokenType.PUNT_COMA == lookAhead.getTokenType())
                lookAhead = Lexicographical.getInstance().getToken();
        }while(a);
    }*/

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
