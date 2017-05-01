package semantic;

/* **************************** IMPORTS *****************************/
import taulasimbols.*;
import lexicographical.Lexicographical;
import utils.Error;

/**
 * Classe que implementa l'analitzador semàntic del compilador de Babel2017.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 30/04/2017
 */
public class Semantic {

    /* ************************** CONSTANTS ***************************/
    private static final String LOGIC_LEXEME = "logic";
    private static final String SENCER_LEXEME = "sencer";

    /* ************************** ATTRIBUTES ***************************/
    private TaulaSimbols taulaSimbols;

    private static Semantic instance;

    /* ************************* CONSTRUCTORS ***************************/
    public Semantic() {

        if (instance == null) {

            taulaSimbols = new TaulaSimbols();
            instance = this;
        }
    }

    public static Semantic getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/
    public TaulaSimbols getTaulaSimbols() {

        return taulaSimbols;
    }

    public void addBloc(boolean zero) {

        taulaSimbols.inserirBloc(new Bloc());
        if(taulaSimbols.getBlocActual() == 0 && !zero) taulaSimbols.setBlocActual(1);
    }

    public void removeBloc() {

        taulaSimbols.esborrarBloc(1);
        taulaSimbols.setBlocActual(0);
    }

    public void addVariable (String lexeme, ITipus tipus) {

        Variable aux = new Variable();

        aux.setNom(lexeme);
        aux.setTipus(tipus);

        taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).inserirVariable(aux);
    }

    public ITipus generateTipus (String tipus, ITipus tipusArray) {

        ITipus aux;

        if (tipusArray == null) {

            aux = generateTipusSimple(tipus);
        } else {

            TipusArray auxArray = (TipusArray) tipusArray;
            auxArray.setTipusElements(generateTipusSimple(tipus));
            auxArray.setNom("V_" + auxArray.obtenirDimensio(0).getLimitInferior() + "_" + auxArray.obtenirDimensio(0).getLimitSuperior() + "_" + auxArray.getTipusElements());
            aux = auxArray;
        }

        return aux;
    }

    public ITipus generateArray(int limitSuperior, int limitInferior) {

        TipusArray auxTipus = new TipusArray();
        DimensioArray auxDimensio = new DimensioArray();
        ITipus auxSencer = generateTipusSimple(SENCER_LEXEME);

        auxDimensio.setTipusLimit(auxSencer);
        auxDimensio.setLimitInferior(limitInferior);
        auxDimensio.setLimitSuperior(limitSuperior);

        auxTipus.inserirDimensio(auxDimensio);

        return auxTipus;
    }

    public SemanticContainer convExp (SemanticContainer terme1, String operador, SemanticContainer terme2){

        SemanticContainer resultat =  new SemanticContainer();
        switch (operador){

            case Lexicographical.MES:
            case Lexicographical.MENYS:
            case Lexicographical.MULTIPLICAR:
            case Lexicographical.DIVIDIR:
                resultat.setValue(SemanticContainer.VALOR, sumaRestaMulDiv(terme1, operador, terme2));
                break;

            case Lexicographical.NOT:
                resultat.setValue(SemanticContainer.VALOR, notBool(terme1, operador, terme2));
                break;

            case Lexicographical.AND:
            case Lexicographical.OR:
                resultat.setValue(SemanticContainer.VALOR, andOrBool(terme1, operador, terme2));
                break;

            default:
        }

        return resultat;
    }

    /* ************************ PRIVATE METHODS *************************/
    private ITipus generateTipusSimple(String tipus) {

        ITipus aux = new TipusSimple();
        aux.setNom(tipus);

        switch (tipus) {

            case LOGIC_LEXEME:
                //PREGUNTAR COM OMPLIR EL TIPUS PER GENERACIÓ DE CODI
                break;

            case SENCER_LEXEME:
                //PREGUNTAR COM OMPLIR EL TIPUS PER GENERACIÓ DE CODI
                break;
        }

        return aux;
    }

    private int sumaRestaMulDiv (SemanticContainer terme1, String operador, SemanticContainer terme2){

        int num1, num2, resultat;
        ITipus tipus = (ITipus)terme1.getValue(SemanticContainer.TIPUS);
        ITipus tipus2 = (ITipus)terme2.getValue(SemanticContainer.TIPUS);
        resultat = 0;
        switch (tipus.getNom()){

            case Lexicographical.SENCER:

                if (tipus.getNom().equals(tipus2.getNom())){
                    num1 = (int)terme1.getValue(SemanticContainer.VALOR);
                    num2 = (int)terme2.getValue(SemanticContainer.VALOR);

                    if (operador.equals(Lexicographical.MES))resultat = num1 + num2;
                    if (operador.equals(Lexicographical.MENYS))resultat = num1 - num2;
                    if (operador.equals(Lexicographical.MULTIPLICAR))resultat = num1 * num2;
                    if (operador.equals(Lexicographical.DIVIDIR))resultat = num1 / num2;

                }else{
                    Error.getInstance().writeError(0,2);
                }
                break;

            case Lexicographical.UNDEFINED:

                if (tipus2.getNom().equals(Lexicographical.SENCER)){
                    resultat = Integer.parseInt((String)terme2.getValue(SemanticContainer.VALOR));
                }else{
                    Error.getInstance().writeError(0,2);
                }
                break;

            default:
                Error.getInstance().writeError(0,2);

        }
        return resultat;
    }

    private boolean notBool (SemanticContainer terme1, String operador, SemanticContainer terme2){

        boolean resultat;
        ITipus tipus = (ITipus)terme1.getValue(SemanticContainer.TIPUS);
        ITipus tipus2 = (ITipus)terme2.getValue(SemanticContainer.TIPUS);

        resultat = false;
        switch (tipus.getNom()){

            case Lexicographical.UNDEFINED:

                if (tipus2.getNom().equals(Lexicographical.LOGIC)){
                    boolean valor2 = (boolean)terme2.getValue(SemanticContainer.VALOR);
                    if (valor2)resultat = false;
                    else {
                        resultat = true;
                    }

                }else{
                    Error.getInstance().writeError(0,2);
                }
                break;

            default:
                Error.getInstance().writeError(0,2);

        }
        return resultat;
    }

    private boolean andOrBool (SemanticContainer terme1, String operador, SemanticContainer terme2){

        boolean resultat;
        ITipus tipus = (ITipus)terme1.getValue(SemanticContainer.TIPUS);
        ITipus tipus2 = (ITipus)terme2.getValue(SemanticContainer.TIPUS);
        resultat = false;
        switch (tipus.getNom()){

            case Lexicographical.LOGIC:

                if (tipus.getNom().equals(tipus2.getNom())){

                    boolean valor = (boolean)terme1.getValue(SemanticContainer.VALOR);
                    boolean valor2 = (boolean)terme2.getValue(SemanticContainer.VALOR);
                    if (tipus.getNom().equals(Lexicographical.AND) )resultat = valor & valor2;
                    else {
                        resultat = valor | valor2;
                    }

                }else{
                    Error.getInstance().writeError(0,2);
                }
                break;

            default:
                Error.getInstance().writeError(0,2);

        }
        return resultat;
    }


}
