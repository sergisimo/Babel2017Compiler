package syntactic;
/* **************************** IMPORTS *****************************/
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import lexicographical.Lexicographical;
import lexicographical.Token;
import semantic.Semantic;
import semantic.SemanticContainer;
import taulasimbols.Funcio;
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
        System.out.println(Semantic.getInstance().getTaulaSimbols().toXml());
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

        SemanticContainer vhExp;
        String id;

        this.accept(Token.TokenType.CONST);
        id = lookAhead.getLexeme();
        this.accept(Token.TokenType.ID);
        this.accept(Token.TokenType.IGUAL);
        vhExp = this.Exp();
        this.accept(Token.TokenType.PUNT_COMA);

        Semantic.getInstance().addConstant(id, vhExp);
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

        Funcio auxFuncio;
        String id, tipusRetorn;

        switch (lookAhead.getTokenType()) {

            case FUNCIO:
                Semantic.getInstance().addBloc(false);
                this.accept(Token.TokenType.FUNCIO);
                id = lookAhead.getLexeme();
                this.accept(Token.TokenType.ID);
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                auxFuncio = this.Llista_Param();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                this.accept(Token.TokenType.DOS_PUNTS);
                tipusRetorn = lookAhead.getLexeme();
                this.accept(Token.TokenType.TIPUS_SIMPLE);
                this.accept(Token.TokenType.PUNT_COMA);
                Semantic.getInstance().addFuncio(id, auxFuncio, tipusRetorn);
                Semantic.getInstance().setFuncioAnalitzada(auxFuncio);
                Semantic.getInstance().setReturnDone(false);
                this.Decl_Cte_Var();
                this.accept(Token.TokenType.FUNC);
                this.Llista_Inst();
                this.accept(Token.TokenType.FIFUNC);
                Semantic.getInstance().removeBloc();
                this.accept(Token.TokenType.PUNT_COMA);
                Semantic.getInstance().checkReturnDone();
                this.Dec_Fun();
                break;

            default:
                break;
        }
    }

    private Funcio Llista_Param() {

        Funcio auxFuncio = new Funcio();

        switch (lookAhead.getTokenType()) {

            case PERREF:
            case PERVAL:
                auxFuncio = this.Llista_Param1(auxFuncio);
                break;

            default:
                break;
        }

        return auxFuncio;
    }

    private Funcio Llista_Param1(Funcio auxFuncio) {

        String tipusRetorn = this.PasValor();
        String id = lookAhead.getLexeme();
        this.accept(Token.TokenType.ID);
        this.accept(Token.TokenType.DOS_PUNTS);
        ITipus tipus = this.Tipus();
        auxFuncio = Semantic.getInstance().addParametre(auxFuncio, id, tipusRetorn, tipus);
        auxFuncio = this.Llista_Param2(auxFuncio);

        return auxFuncio;
    }

    private Funcio Llista_Param2(Funcio auxFuncio) {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                auxFuncio = this.Llista_Param1(auxFuncio);
                break;

            default:
                break;
        }

        return auxFuncio;
    }

    private String PasValor() {

        String tipusRetorn = new String();

        switch (lookAhead.getTokenType()) {

            case PERREF:
                tipusRetorn = lookAhead.getLexeme();
                this.accept(Token.TokenType.PERREF);
                break;

            case PERVAL:
                tipusRetorn = lookAhead.getLexeme();
                this.accept(Token.TokenType.PERVAL);
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
        }

        return tipusRetorn;
    }

    private ITipus Tipus() {

        ITipus tipus = this.Tipus_Abr();
        tipus = Semantic.getInstance().generateTipus(lookAhead.getLexeme(), tipus);
        this.accept(Token.TokenType.TIPUS_SIMPLE);
        return tipus;
    }

    private ITipus Tipus_Abr() {

        SemanticContainer limitInferior, limitSuperior;

        switch (lookAhead.getTokenType()) {

            case VECTOR:
                this.accept(Token.TokenType.VECTOR);
                this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                limitInferior = this.Exp();
                this.accept(Token.TokenType.PUNT_PUNT);
                limitSuperior = this.Exp();
                this.accept(Token.TokenType.CLAUDATOR_DARRERE);
                this.accept(Token.TokenType.DE);

                return Semantic.getInstance().generateArray(limitInferior, limitSuperior);

            default:
                break;
        }

        return null;
    }

    private SemanticContainer Exp() {

        SemanticContainer vsExpSimple;

        vsExpSimple = this.Exp_Simple();
        return this.Exp1(vsExpSimple);
    }

    private SemanticContainer Exp1(SemanticContainer vhExp1) {

        SemanticContainer vsExp1, vsExp_Simple;
        String operador;

        switch (lookAhead.getTokenType()) {

            case OPER_REL:
                operador = lookAhead.getLexeme();
                this.accept(Token.TokenType.OPER_REL);
                vsExp_Simple = this.Exp_Simple();
                vsExp1 = Semantic.getInstance().doRelationalOperation(vhExp1, operador, vsExp_Simple);
                break;

            default:
                vsExp1 = vhExp1;
                break;
        }

        return vsExp1;
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

        SemanticContainer vsExp = this.Exp();
        Semantic.getInstance().checkParameter(vsExp);
        this.Exp4();
    }

    private void Exp4() {

        switch (lookAhead.getTokenType()) {

            case COMA:
                this.accept(Token.TokenType.COMA);
                this.Exp3();
                break;

            default:
                Semantic.getInstance().checkNumberOfParameters();
                break;
        }
    }

    private SemanticContainer Exp_Simple() {

        SemanticContainer vsExpSimple1, vsTerme;

        vsExpSimple1 = this.Exp_Simple1();
        vsTerme = this.Terme();
        if (vsTerme != null) vsTerme = Semantic.getInstance().doArithmeticOperation(null, vsExpSimple1, vsTerme);
        return this.Terme1(vsTerme);
    }

    private SemanticContainer Exp_Simple1() {

        SemanticContainer vsExpSimple1 = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case MES:
                vsExpSimple1.setValue(SemanticContainer.OPERADOR, Lexicographical.MES);
                this.accept(Token.TokenType.MES);
                break;

            case MENYS:
                vsExpSimple1.setValue(SemanticContainer.OPERADOR, Lexicographical.MENYS);
                this.accept(Token.TokenType.MENYS);
                break;

            case NOT:
                vsExpSimple1.setValue(SemanticContainer.OPERADOR, Lexicographical.NOT);
                this.accept(Token.TokenType.NOT);
                break;

            default:
                vsExpSimple1.setValue(SemanticContainer.OPERADOR, Semantic.UNDEFINED_VALUE);
                break;
        }

        return vsExpSimple1;
    }

    private SemanticContainer Terme() {

        SemanticContainer vsFactor;

        vsFactor = this.Factor();
        return this.Factor1(vsFactor);
    }

    private SemanticContainer Terme1(SemanticContainer vhTerme1) {

        SemanticContainer vsTerme1, vsTerme2, vsTerme;

        switch (lookAhead.getTokenType()) {

            case MES:
            case MENYS:
            case OR:
                vsTerme2 = this.Terme2();
                vsTerme = this.Terme();
                if (vhTerme1 != null && vsTerme != null) vsTerme = Semantic.getInstance().doArithmeticOperation(vhTerme1, vsTerme2, vsTerme);
                vsTerme1 = this.Terme1(vsTerme);
                break;

            default:
                vsTerme1 = vhTerme1;
                break;
        }

        return vsTerme1;
    }

    private SemanticContainer Terme2() {

        SemanticContainer vsTerme2 = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case MES:
                vsTerme2.setValue(SemanticContainer.OPERADOR, Lexicographical.MES);
                this.accept(Token.TokenType.MES);
                break;

            case MENYS:
                vsTerme2.setValue(SemanticContainer.OPERADOR, Lexicographical.MENYS);
                this.accept(Token.TokenType.MENYS);
                break;

            case OR:
                vsTerme2.setValue(SemanticContainer.OPERADOR, Lexicographical.OR);
                this.accept(Token.TokenType.OR);
                break;
        }

        return vsTerme2;
    }

    private SemanticContainer Factor() {

        SemanticContainer vsFactor = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case CTE_ENTERA:
                vsFactor.setValue(SemanticContainer.ESTATIC, true);
                vsFactor.setValue(SemanticContainer.VALOR, Integer.parseInt(lookAhead.getLexeme()));
                vsFactor.setValue(SemanticContainer.TIPUS, Semantic.getInstance().generateTipusSimple(Lexicographical.SENCER));
                this.accept(Token.TokenType.CTE_ENTERA);
                break;

            case CTE_LOGICA:
                vsFactor.setValue(SemanticContainer.ESTATIC, true);
                if (lookAhead.getLexeme().equals(Lexicographical.CERT)) vsFactor.setValue(SemanticContainer.VALOR, true);
                else vsFactor.setValue(SemanticContainer.VALOR, false);
                vsFactor.setValue(SemanticContainer.TIPUS, Semantic.getInstance().generateTipusSimple(Lexicographical.LOGIC));
                this.accept(Token.TokenType.CTE_LOGICA);
                break;

            case CTE_CADENA:
                vsFactor.setValue(SemanticContainer.ESTATIC, true);
                vsFactor.setValue(SemanticContainer.VALOR, lookAhead.getLexeme());
                vsFactor.setValue(SemanticContainer.TIPUS, Semantic.getInstance().generateTipusCadena(Lexicographical.CADENA, lookAhead.getLexeme().length()));
                this.accept(Token.TokenType.CTE_CADENA);
                break;

            case PARENTESI_DAVANT:
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                vsFactor = this.Exp();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                break;

            case ID:
                vsFactor = Semantic.getInstance().searchIDForUtilitzation(lookAhead.getLexeme());
                this.accept(Token.TokenType.ID);
                vsFactor = this.Variable5(vsFactor);
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
        }

        return vsFactor;
    }

    private SemanticContainer Factor1(SemanticContainer vhFactor1) {

        SemanticContainer vsFactor1, vsFactor2, vsFactor;

        switch (lookAhead.getTokenType()) {

            case MULTIPLICAR:
            case DIVIDIR:
            case AND:
                vsFactor2 = this.Factor2();
                vsFactor = this.Factor();
                if (vhFactor1 != null && vsFactor != null) vsFactor = Semantic.getInstance().doArithmeticOperation(vhFactor1, vsFactor2, vsFactor);
                vsFactor1 = this.Factor1(vsFactor);
                break;

            default:
                vsFactor1 = vhFactor1;
                break;
        }

        return vsFactor1;
    }

    private SemanticContainer Factor2() {

        SemanticContainer vsFactor2 = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case MULTIPLICAR:
                vsFactor2.setValue(SemanticContainer.OPERADOR, Lexicographical.MULTIPLICAR);
                this.accept(Token.TokenType.MULTIPLICAR);
                break;

            case DIVIDIR:
                vsFactor2.setValue(SemanticContainer.OPERADOR, Lexicographical.DIVIDIR);
                this.accept(Token.TokenType.DIVIDIR);
                break;

            case AND:
                vsFactor2.setValue(SemanticContainer.OPERADOR, Lexicographical.AND);
                this.accept(Token.TokenType.AND);
                break;
        }

        return vsFactor2;
    }

    private SemanticContainer Variable() {

        SemanticContainer vhVariable1 = Semantic.getInstance().searchIDForUtilitzation(lookAhead.getLexeme());
        SemanticContainer vsVariable1;

        this.accept(Token.TokenType.ID);
        vsVariable1 = this.Variable1(vhVariable1);
        if (vsVariable1 != null) Semantic.getInstance().checkIsVariable(vhVariable1, vsVariable1);
        return vsVariable1;
    }

    private SemanticContainer Variable1(SemanticContainer vhVariable1) {

        SemanticContainer vsVariable1 = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case CLAUDATOR_DAVANT:
                this.accept(Token.TokenType.CLAUDATOR_DAVANT);
                SemanticContainer vsExp = this.Exp();
                this.accept(Token.TokenType.CLAUDATOR_DARRERE);
                if (vhVariable1 != null) vsVariable1 = Semantic.getInstance().checkArray(vhVariable1, vsExp);
                break;

            default:
                if (vhVariable1 != null) vsVariable1 = Semantic.getInstance().checkConstantOrVariable(vhVariable1);
                break;
        }

        return vsVariable1;
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

    private SemanticContainer Variable5(SemanticContainer vhVaraible5) {

        SemanticContainer vsVariable5 = new SemanticContainer();

        switch (lookAhead.getTokenType()) {

            case PARENTESI_DAVANT:
                if (vhVaraible5 != null) Semantic.getInstance().checkFunctionID(vhVaraible5);
                this.accept(Token.TokenType.PARENTESI_DAVANT);
                this.Exp2();
                this.accept(Token.TokenType.PARENTESI_DARRERE);
                if (vhVaraible5 != null) vsVariable5 = Semantic.getInstance().getTypeFromFunction(vhVaraible5);
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
                vsVariable5 = this.Variable1(vhVaraible5);
                break;

            default: Error.getInstance().writeFatalError(0);
                break;
        }

        return vsVariable5;
    }

    private void Llista_Inst() {

        this.Inst();
        this.accept(Token.TokenType.PUNT_COMA);
        System.out.println(Lexicographical.getInstance().getActualLine());
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

        SemanticContainer vsExp, vsVariable;

        switch (lookAhead.getTokenType()) {

            case ID:
                vsVariable = this.Variable();
                this.accept(Token.TokenType.IGUAL);
                vsExp = this.Exp();
                if (vsVariable != null && vsExp != null) Semantic.getInstance().checkAssignation(vsVariable, vsExp);
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
                vsExp = this.Exp();
                if (vsExp != null) Semantic.getInstance().checkIsLogic(vsExp);
                break;

            case MENTRE:
                this.accept(Token.TokenType.MENTRE);
                vsExp = this.Exp();
                if (vsExp != null) Semantic.getInstance().checkIsLogic(vsExp);
                this.accept(Token.TokenType.FER);
                this.Llista_Inst();
                this.accept(Token.TokenType.FIMENTRE);
                break;

            case SI:
                this.accept(Token.TokenType.SI);
                vsExp = this.Exp();
                if (vsExp != null) Semantic.getInstance().checkIsLogic(vsExp);
                this.accept(Token.TokenType.LLAVORS);
                this.Llista_Inst();
                this.Inst1();
                this.accept(Token.TokenType.FISI);
                break;

            case RETORNAR:
                this.accept(Token.TokenType.RETORNAR);
                vsExp = this.Exp();
                Semantic.getInstance().checkReturn(vsExp);
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

        SemanticContainer vsExp = this.Exp();
        Semantic.getInstance().checkExpEscriure(vsExp);
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
