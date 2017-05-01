package semantic;

/* **************************** IMPORTS *****************************/
import taulasimbols.*;

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


}
