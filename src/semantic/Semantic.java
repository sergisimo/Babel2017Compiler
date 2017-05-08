package semantic;

/* **************************** IMPORTS *****************************/
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.dc.pr.PRError;
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
    private static final String UNDEFINED_TYPE_IDENTIFIER = "_undefined";
    public static final String UNDEFINED_VALUE = "UNDEFINED";

    /* ************************** ATTRIBUTES ***************************/
    private TaulaSimbols taulaSimbols;

    private boolean returnDone;

    private Funcio funcioAnalitzada;

    private Funcio funcioCridada;

    private int comptador;

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

    public void setReturnDone(boolean returnDone) {

        this.returnDone = returnDone;
    }

    public void setFuncioAnalitzada(Funcio funcioAnalitzada) {

        this.funcioAnalitzada = funcioAnalitzada;
    }

    public void setFuncioCridada(Funcio funcioCridada) {

        this.funcioCridada = funcioCridada;
        this.comptador = 0;
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

        if (searchID(lexeme, true) != null) {
            Error.getInstance().writeError(21, Lexicographical.getInstance().getActualLine(), lexeme);
            tipus = new TipusIndefinit();
            lexeme += UNDEFINED_TYPE_IDENTIFIER;
        }

        aux.setNom(lexeme);
        aux.setTipus(tipus);

        taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).inserirVariable(aux);
    }

    public void addConstant (String id, SemanticContainer exp) {

        Constant aux = new Constant();
        ITipus tipus;
        Object valor;

        if (searchID(id, true) != null) {
            Error.getInstance().writeError(20, Lexicographical.getInstance().getActualLine(), id);
            tipus = new TipusIndefinit();
            id += UNDEFINED_TYPE_IDENTIFIER;
            valor = null;
        } else {

            if (!(boolean)exp.getValue(SemanticContainer.ESTATIC)) {
                Error.getInstance().writeError(40, Lexicographical.getInstance().getActualLine());
                tipus = new TipusIndefinit();
                valor = null;
            } else {
                tipus = (ITipus) exp.getValue(SemanticContainer.TIPUS);
                if (tipus.getClass() != TipusSimple.class && tipus.getClass() != TipusCadena.class) {
                    Error.getInstance().writeError(43, Lexicographical.getInstance().getActualLine(), id);
                    tipus = new TipusIndefinit();
                    valor = null;
                } else valor = exp.getValue(SemanticContainer.VALOR);
            }
        }

        aux.setNom(id);
        aux.setTipus(tipus);
        aux.setValor(valor);

        taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).inserirConstant(aux);
    }

    public void addFuncio(String id, Funcio funcio, String retorn) {

        if (searchID(id, true) != null) {
            Error.getInstance().writeError(22, Lexicographical.getInstance().getActualLine(), id);
            id += UNDEFINED_TYPE_IDENTIFIER;
        }

        funcio.setNom(id);
        funcio.setTipus(generateTipusSimple(retorn));

        taulaSimbols.obtenirBloc(0).inserirProcediment(funcio);
    }

    public Funcio addParametre(Funcio funcio, String id, String pasParametre, ITipus tipus) {

        Parametre parametre = new Parametre();

        if (checkRepeatedParameter(funcio, id)) {
            Error.getInstance().writeError(23, Lexicographical.getInstance().getActualLine(), id);
            id += UNDEFINED_TYPE_IDENTIFIER;
            tipus = new TipusIndefinit();
            parametre.setTipusPasParametre(TipusPasParametre.VALOR);
        } else {

            if (pasParametre.equals(Lexicographical.PERREF)) parametre.setTipusPasParametre(TipusPasParametre.REFERENCIA);
            else parametre.setTipusPasParametre(TipusPasParametre.VALOR);
        }

        parametre.setTipus(tipus);
        parametre.setNom(id);

        funcio.inserirParametre(parametre);
        taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).inserirVariable(parametre);

        return funcio;
    }

    public ITipus generateTipus (String tipus, ITipus tipusArray) {

        ITipus aux;

        if (tipusArray == null) {

            aux = generateTipusSimple(tipus);
        } else {

            TipusArray auxArray = (TipusArray) tipusArray;
            auxArray.setTipusElements(generateTipusSimple(tipus));
            auxArray.setNom("V_" + auxArray.obtenirDimensio(0).getLimitInferior() + "_" + auxArray.obtenirDimensio(0).getLimitSuperior() + "_" + auxArray.getTipusElements().getNom());
            aux = auxArray;
        }

        return aux;
    }

    public ITipus generateArray(SemanticContainer limitInferior, SemanticContainer limitSuperior) {

        TipusSimple tipusLimitInferior, tipusLimitSuperior;

        TipusArray auxTipus = new TipusArray();
        DimensioArray auxDimensio = new DimensioArray();
        ITipus auxSencer = generateTipusSimple(Lexicographical.SENCER);

        if (limitInferior.getValue(SemanticContainer.TIPUS).getClass() != TipusSimple.class) Error.getInstance().writeError(25, Lexicographical.getInstance().getActualLine());
        else {

            tipusLimitInferior = (TipusSimple) limitInferior.getValue(SemanticContainer.TIPUS);
            if (limitSuperior.getValue(SemanticContainer.TIPUS).getClass() != TipusSimple.class) Error.getInstance().writeError(25, Lexicographical.getInstance().getActualLine());
            else  {

                tipusLimitSuperior = (TipusSimple) limitSuperior.getValue(SemanticContainer.TIPUS);

                if (!tipusLimitInferior.getNom().equals(Lexicographical.SENCER) || !tipusLimitSuperior.getNom().equals(Lexicographical.SENCER)) Error.getInstance().writeError(25, Lexicographical.getInstance().getActualLine());
                else {

                    if (!(boolean)limitInferior.getValue(SemanticContainer.ESTATIC) || !(boolean)limitSuperior.getValue(SemanticContainer.ESTATIC)) Error.getInstance().writeError(39, Lexicographical.getInstance().getActualLine());
                    else {

                        int numLimitInferior, numLimitSuperior;

                        numLimitInferior = (int) limitInferior.getValue(SemanticContainer.VALOR);
                        numLimitSuperior = (int) limitSuperior.getValue(SemanticContainer.VALOR);

                        if (numLimitInferior > numLimitSuperior) Error.getInstance().writeError(24, Lexicographical.getInstance().getActualLine());
                        else {
                            auxDimensio.setTipusLimit(auxSencer);
                            auxDimensio.setLimitInferior(numLimitInferior);
                            auxDimensio.setLimitSuperior(numLimitSuperior);

                            auxTipus.inserirDimensio(auxDimensio);

                            return auxTipus;
                        }
                    }
                }
            }
        }

        auxDimensio.setTipusLimit(auxSencer);
        auxDimensio.setLimitInferior(0);
        auxDimensio.setLimitSuperior(0);
        auxTipus.inserirDimensio(auxDimensio);

        return auxTipus;
    }

    public ITipus generateTipusSimple(String tipus) {

        TipusSimple aux = new TipusSimple();
        aux.setNom(tipus);

        switch (tipus) {

            case Lexicographical.LOGIC:
                //PREGUNTAR COM OMPLIR EL TIPUS PER GENERACIÓ DE CODI
                break;

            case Lexicographical.SENCER:
                //PREGUNTAR COM OMPLIR EL TIPUS PER GENERACIÓ DE CODI
                break;
        }

        return aux;
    }

    public ITipus generateTipusCadena(String tipus, int length) {

        TipusCadena aux = new TipusCadena();

        aux.setNom(tipus);
        aux.setLongitud(length);

        return aux;
    }

    public SemanticContainer searchID(String id, boolean declaration) {

        SemanticContainer aux = new SemanticContainer();

        Variable auxVariable = taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).obtenirVariable(id);
        Constant auxCostant = taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).obtenirConstant(id);
        Procediment auxProcediment = taulaSimbols.obtenirBloc(0).obtenirProcediment(id);

        aux.setValue(SemanticContainer.TOKEN, id);

        if (auxVariable == null) auxVariable = taulaSimbols.obtenirBloc(0).obtenirVariable(id);
        else {
            aux.setValue(SemanticContainer.VALOR, auxVariable);
            return aux;
        }

        if (auxCostant == null) auxCostant = taulaSimbols.obtenirBloc(0).obtenirConstant(id);
        else {
            aux.setValue(SemanticContainer.VALOR, auxCostant);
            return aux;
        }

        if (auxProcediment != null) {
            aux.setValue(SemanticContainer.VALOR, auxProcediment);
            return aux;
        }

        if (auxVariable != null) {
            aux.setValue(SemanticContainer.VALOR, auxVariable);
            return aux;
        }

        if (auxCostant != null) {
            aux.setValue(SemanticContainer.VALOR, auxCostant);
            return aux;
        }

        if (!declaration) Error.getInstance().writeError(28, Lexicographical.getInstance().getActualLine(), id);
        aux.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
        return null;
    }

    public SemanticContainer getTypeFromFunction(SemanticContainer function) {

        Procediment aux = (Procediment) function.getValue(SemanticContainer.VALOR);
        SemanticContainer returnContainer = new SemanticContainer();

        returnContainer.setValue(SemanticContainer.ESTATIC, false);
        returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);

        if (aux.getClass() != Funcio.class) returnContainer.setValue(SemanticContainer.TIPUS, new TipusIndefinit());
        else {
            Funcio auxFuncio = (Funcio) aux;
            returnContainer.setValue(SemanticContainer.TIPUS, auxFuncio.getTipus());
        }

        return returnContainer;
    }

    public void checkFunctionID(String id) {

        Funcio auxProcediment = (Funcio) taulaSimbols.obtenirBloc(0).obtenirProcediment(id);

        setFuncioCridada(auxProcediment);

        if (auxProcediment == null) Error.getInstance().writeError(40, Lexicographical.getInstance().getActualLine(), id);
    }

    public SemanticContainer checkConstantOrVariable(SemanticContainer container) {

        String id = (String) container.getValue(SemanticContainer.TOKEN);
        SemanticContainer returnContainer = new SemanticContainer();

        if (isVariableID(id)) {

            Variable auxVariable = (Variable) container.getValue(SemanticContainer.VALOR);
            returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
            returnContainer.setValue(SemanticContainer.TIPUS, auxVariable.getTipus());
            returnContainer.setValue(SemanticContainer.ESTATIC, false);
        } else if (isConstantID(id)) {

            Constant auxConstant = (Constant) container.getValue(SemanticContainer.VALOR);
            returnContainer.setValue(SemanticContainer.VALOR, auxConstant.getValor());
            returnContainer.setValue(SemanticContainer.TIPUS, auxConstant.getTipus());
            returnContainer.setValue(SemanticContainer.ESTATIC, true);
        } else {

            Error.getInstance().writeError(41, Lexicographical.getInstance().getActualLine(), id);
            returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
            returnContainer.setValue(SemanticContainer.TIPUS, new TipusIndefinit());
            returnContainer.setValue(SemanticContainer.ESTATIC, false);
        }

        return returnContainer;
    }

    public SemanticContainer checkArray(SemanticContainer idContainer, SemanticContainer expContainer) {

        String id = (String) idContainer.getValue(SemanticContainer.TOKEN);
        SemanticContainer returnContainer = new SemanticContainer();

        if (isVariableID(id)) {
            Variable variableAux = (Variable) idContainer.getValue(SemanticContainer.VALOR);

            if (variableAux.getTipus().getClass() != TipusArray.class) Error.getInstance().writeError(44, Lexicographical.getInstance().getActualLine(), id);
            else {
                ITipus tipusExp = (ITipus) expContainer.getValue(SemanticContainer.TIPUS);
                TipusArray tipusVector = (TipusArray) variableAux.getTipus();
                if (!tipusExp.getNom().equals(Lexicographical.SENCER)) Error.getInstance().writeError(32, Lexicographical.getInstance().getActualLine());
                else {

                    if ((boolean)expContainer.getValue(SemanticContainer.ESTATIC)) {

                        int valor = (int) expContainer.getValue(SemanticContainer.VALOR);
                        if ((int)tipusVector.obtenirDimensio(0).getLimitInferior() > valor ||  (int)tipusVector.obtenirDimensio(0).getLimitSuperior() < valor) Error.getInstance().writeError(45, Lexicographical.getInstance().getActualLine(), id);
                    }

                    returnContainer.setValue(SemanticContainer.TIPUS, tipusVector.getTipusElements());
                    returnContainer.setValue(SemanticContainer.ESTATIC, false);
                    returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);

                    return returnContainer;
                }
            }
        } else {
            Error.getInstance().writeError(30, Lexicographical.getInstance().getActualLine(), id);
        }

        returnContainer.setValue(SemanticContainer.TIPUS, new TipusIndefinit());
        returnContainer.setValue(SemanticContainer.ESTATIC, false);
        returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);

        return returnContainer;
    }

    public void checkIsVariable(SemanticContainer containerID, SemanticContainer containerType) {

        String id = (String) containerID.getValue(SemanticContainer.TOKEN);
        ITipus tipus = (ITipus) containerType.getValue(SemanticContainer.TIPUS);

        if (!isVariableID(id)) Error.getInstance().writeError(30, Lexicographical.getInstance().getActualLine(), id);
        if (tipus.getClass() != TipusSimple.class) Error.getInstance().writeError(42, Lexicographical.getInstance().getActualLine(), id);
    }

    public SemanticContainer doArithmeticOperation (SemanticContainer exp1, SemanticContainer operator, SemanticContainer exp2) {

        String operatorAux = (String) operator.getValue(SemanticContainer.OPERADOR);

        switch (operatorAux) {

            case Lexicographical.MES:
            case Lexicographical.MENYS:
            case Lexicographical.MULTIPLICAR:
            case Lexicographical.DIVIDIR:
                return integerOperation(exp1, operatorAux, exp2);

            case Lexicographical.NOT:
            case Lexicographical.AND:
            case Lexicographical.OR:
                return logicOperation(exp1, operatorAux, exp2);

            case UNDEFINED_VALUE:
                return exp2;
        }

        return exp2;
    }

    public SemanticContainer doRelationalOperation (SemanticContainer exp1, String operador, SemanticContainer exp2) {

        if (areInteger(exp1, exp2)) return integerRelationalOperation(exp1, operador, exp2);
        else if (areBoolean(exp1, exp2)) return booleanRelationalOperation(exp1, operador, exp2);
        else {

            Error.getInstance().writeError(46, Lexicographical.getInstance().getActualLine());

            SemanticContainer container = new SemanticContainer();
            container.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
            container.setValue(SemanticContainer.TIPUS, new TipusIndefinit());
            container.setValue(SemanticContainer.ESTATIC, false);

            return container;
        }
    }

    public void checkAssignation(SemanticContainer variable, SemanticContainer exp) {

        ITipus tipusVariable = (ITipus) variable.getValue(SemanticContainer.TIPUS);
        ITipus tipusExp = (ITipus) exp.getValue(SemanticContainer.TIPUS);

        if (tipusExp == null || tipusVariable == null || tipusExp.getClass() != TipusSimple.class || tipusVariable.getClass() != TipusSimple.class) Error.getInstance().writeError(47, Lexicographical.getInstance().getActualLine());
        else if (!tipusVariable.getNom().equals(tipusExp.getNom())) Error.getInstance().writeError(31, Lexicographical.getInstance().getActualLine(), tipusVariable.getNom(), tipusExp.getNom());
    }

    public void checkIsLogic(SemanticContainer exp) {

        ITipus tipusExp = (ITipus) exp.getValue(SemanticContainer.TIPUS);

        if (tipusExp == null || !tipusExp.getNom().equals(Lexicographical.LOGIC)) Error.getInstance().writeError(26, Lexicographical.getInstance().getActualLine());
    }

    public void checkExpEscriure (SemanticContainer expEscriure) {

        ITipus tipusExp = (ITipus) expEscriure.getValue(SemanticContainer.TIPUS);

        if (tipusExp == null || (tipusExp.getClass() != TipusSimple.class && tipusExp.getClass() != TipusCadena.class)) Error.getInstance().writeError(33, Lexicographical.getInstance().getActualLine());
    }

    public void checkReturnDone() {

        if (!returnDone) Error.getInstance().writeError(48, Lexicographical.getInstance().getActualLine(), funcioAnalitzada.getNom());
    }

    public void checkReturn(SemanticContainer exp) {

        ITipus tipusFuncio = funcioAnalitzada.getTipus();
        ITipus tipusExp = (ITipus) exp.getValue(SemanticContainer.TIPUS);

        returnDone = true;

        if (taulaSimbols.getBlocActual() == 0) Error.getInstance().writeError(38, Lexicographical.getInstance().getActualLine());
        if (tipusExp == null || tipusFuncio == null) Error.getInstance().writeError(49, Lexicographical.getInstance().getActualLine());
        else if(!tipusExp.getNom().equals(tipusFuncio.getNom())) Error.getInstance().writeError(37, Lexicographical.getInstance().getActualLine(), funcioAnalitzada.getNom(), tipusFuncio.getNom(), tipusExp.getNom());
    }

    public void checkParameter(SemanticContainer exp) {

        ITipus tipusParametre = null;

        if (comptador < funcioCridada.getNumeroParametres()) tipusParametre = funcioCridada.obtenirParametre(comptador).getTipus();
        ITipus tipusExp = (ITipus) exp.getValue(SemanticContainer.TIPUS);

        if (tipusParametre == null || tipusExp == null);
        else if ((!tipusParametre.getNom().equals(tipusExp.getNom()))) Error.getInstance().writeError(35, Lexicographical.getInstance().getActualLine(), comptador + 1, tipusParametre.getNom());
        else if (funcioCridada.obtenirParametre(comptador).getTipusPasParametre() == TipusPasParametre.REFERENCIA && (boolean) exp.getValue(SemanticContainer.ESTATIC)) Error.getInstance().writeError(36, Lexicographical.getInstance().getActualLine(), comptador + 1);

        comptador++;
    }

    public void checkNumberOfParameters() {

        if (comptador != funcioCridada.getNumeroParametres()) Error.getInstance().writeError(34, Lexicographical.getInstance().getActualLine(), funcioCridada.getNumeroParametres(), comptador);
    }

    /* ************************ PRIVATE METHODS *************************/
    private boolean isVariableID(String id) {

        Variable auxVariable = taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).obtenirVariable(id);

        if (auxVariable == null && taulaSimbols.getBlocActual() != 0) auxVariable = taulaSimbols.obtenirBloc(0).obtenirVariable(id);
        if (auxVariable == null) return false;

        return true;
    }

    private boolean isConstantID(String id) {

        Constant auxCostant = taulaSimbols.obtenirBloc(taulaSimbols.getBlocActual()).obtenirConstant(id);

        if (auxCostant == null && taulaSimbols.getBlocActual() != 0) auxCostant = taulaSimbols.obtenirBloc(0).obtenirConstant(id);
        if (auxCostant == null) return false;

        return true;
    }

    private boolean checkRepeatedParameter(Funcio funcio, String id) {

        for (int i = 0; i < funcio.getNumeroParametres(); i++) {

            if (funcio.obtenirParametre(i).getNom().equals(id)) return true;
        }

        return false;
    }

    private SemanticContainer integerOperation(SemanticContainer exp1, String operator, SemanticContainer exp2) {

        SemanticContainer vsAdd = new SemanticContainer();


        if (exp1 == null) {

            ITipus tipus = (ITipus) exp2.getValue(SemanticContainer.TIPUS);
            if (!tipus.getNom().equals(Lexicographical.SENCER)) Error.getInstance().writeError(25, Lexicographical.getInstance().getActualLine());
            else {
                vsAdd.setValue(SemanticContainer.TIPUS, tipus);
                if ((boolean) exp2.getValue(SemanticContainer.ESTATIC)) {
                    vsAdd.setValue(SemanticContainer.ESTATIC, true);
                    vsAdd.setValue(SemanticContainer.VALOR, oneNumberOperation((int)exp2.getValue(SemanticContainer.VALOR), operator));
                } else {
                    vsAdd.setValue(SemanticContainer.ESTATIC, false);
                    vsAdd.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
                }
            }
        } else {

            if (!areInteger(exp1, exp2)) Error.getInstance().writeError(25, Lexicographical.getInstance().getActualLine());
            else {
                vsAdd.setValue(SemanticContainer.TIPUS, generateTipusSimple(Lexicographical.SENCER));
                if ((boolean) exp1.getValue(SemanticContainer.ESTATIC) && (boolean) exp2.getValue(SemanticContainer.ESTATIC)) {
                    vsAdd.setValue(SemanticContainer.ESTATIC, true);
                    int valor1 = (int) exp1.getValue(SemanticContainer.VALOR);
                    int valor2 = (int) exp2.getValue(SemanticContainer.VALOR);
                    vsAdd.setValue(SemanticContainer.VALOR, twoNumberOperation(valor1, valor2, operator));
                } else {
                    vsAdd.setValue(SemanticContainer.ESTATIC, false);
                    vsAdd.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
                }
            }
        }

        return vsAdd;
    }

    private SemanticContainer logicOperation(SemanticContainer exp1, String operator, SemanticContainer exp2) {

        SemanticContainer vsLogic = new SemanticContainer();


        if (exp1 == null) {

            ITipus tipus = (ITipus) exp2.getValue(SemanticContainer.TIPUS);
            if (!tipus.getNom().equals(Lexicographical.LOGIC)) Error.getInstance().writeError(26, Lexicographical.getInstance().getActualLine());
            else {
                vsLogic.setValue(SemanticContainer.TIPUS, tipus);
                if ((boolean) exp2.getValue(SemanticContainer.ESTATIC)) {
                    vsLogic.setValue(SemanticContainer.ESTATIC, true);
                    vsLogic.setValue(SemanticContainer.VALOR, !(boolean)exp2.getValue(SemanticContainer.VALOR));
                } else {
                    vsLogic.setValue(SemanticContainer.ESTATIC, false);
                    vsLogic.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
                }
            }

        } else {
            if (!areBoolean(exp1, exp2)) Error.getInstance().writeError(26, Lexicographical.getInstance().getActualLine());
            else {
                vsLogic.setValue(SemanticContainer.TIPUS, generateTipusSimple(Lexicographical.LOGIC));
                if ((boolean) exp1.getValue(SemanticContainer.ESTATIC) && (boolean) exp2.getValue(SemanticContainer.ESTATIC)) {
                    vsLogic.setValue(SemanticContainer.ESTATIC, true);
                    boolean valor1 = (boolean) exp1.getValue(SemanticContainer.VALOR);
                    boolean valor2 = (boolean) exp2.getValue(SemanticContainer.VALOR);
                    vsLogic.setValue(SemanticContainer.VALOR, booleanOperation(valor1, valor2, operator));
                } else {
                    vsLogic.setValue(SemanticContainer.ESTATIC, false);
                    vsLogic.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
                }
            }
        }

        return vsLogic;
    }

    private SemanticContainer integerRelationalOperation(SemanticContainer exp1, String operator, SemanticContainer exp2) {

        SemanticContainer returnContainer = new SemanticContainer();

        returnContainer.setValue(SemanticContainer.TIPUS, generateTipusSimple(Lexicographical.LOGIC));

        if ((boolean) exp1.getValue(SemanticContainer.ESTATIC) && (boolean) exp2.getValue(SemanticContainer.ESTATIC)) {

            int value1 = (int) exp1.getValue(SemanticContainer.VALOR);
            int value2 = (int) exp2.getValue(SemanticContainer.VALOR);

            returnContainer.setValue(SemanticContainer.ESTATIC, true);
            switch (operator) {

                case "==":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 == value2);
                    break;

                case "<":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 < value2);
                    break;

                case ">":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 > value2);
                    break;

                case "<=":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 <= value2);
                    break;

                case ">=":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 >= value2);
                    break;

                case "<>":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 != value2);
                    break;
            }

        } else {

            returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
            returnContainer.setValue(SemanticContainer.ESTATIC, false);
        }

        return returnContainer;
    }

    private SemanticContainer booleanRelationalOperation(SemanticContainer exp1, String operator, SemanticContainer exp2) {

        SemanticContainer returnContainer = new SemanticContainer();
        int value1Aux = 0, value2Aux = 0;

        returnContainer.setValue(SemanticContainer.TIPUS, generateTipusSimple(Lexicographical.LOGIC));

        if ((boolean) exp1.getValue(SemanticContainer.ESTATIC) && (boolean) exp2.getValue(SemanticContainer.ESTATIC)) {

            boolean value1 = (boolean) exp1.getValue(SemanticContainer.VALOR);
            boolean value2 = (boolean) exp2.getValue(SemanticContainer.VALOR);
            returnContainer.setValue(SemanticContainer.ESTATIC, true);

            if (value1) value1Aux = 1;
            if (value2) value2Aux = 1;

            switch (operator) {

                case "==":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 == value2);
                    break;

                case "<":
                    returnContainer.setValue(SemanticContainer.VALOR, value1Aux < value2Aux);
                    break;

                case ">":
                    returnContainer.setValue(SemanticContainer.VALOR, value1Aux > value2Aux);
                    break;

                case "<=":
                    returnContainer.setValue(SemanticContainer.VALOR, value1Aux <= value2Aux);
                    break;

                case ">=":
                    returnContainer.setValue(SemanticContainer.VALOR, value1Aux >= value2Aux);
                    break;

                case "<>":
                    returnContainer.setValue(SemanticContainer.VALOR, value1 != value2);
                    break;
            }

        } else {

            returnContainer.setValue(SemanticContainer.VALOR, UNDEFINED_VALUE);
            returnContainer.setValue(SemanticContainer.ESTATIC, false);
        }

        return returnContainer;
    }

    private int oneNumberOperation(int number, String operation) {

        switch (operation) {
            case Lexicographical.MES:
                return number;

            case Lexicographical.MENYS:
                return -number;
        }

        return number;
    }

    private int twoNumberOperation(int number1, int number2, String operation) {

        switch (operation) {
            case Lexicographical.MES:
                return number1 + number2;

            case Lexicographical.MENYS:
                return number1 - number2;

            case Lexicographical.MULTIPLICAR:
                return number1 * number2;

            case Lexicographical.DIVIDIR:
                return number1 / number2;
        }

        return number1;
    }

    private boolean booleanOperation(boolean bool1, boolean bool2, String operation) {

        switch (operation) {

            case Lexicographical.AND:
                return bool1 & bool2;

            case Lexicographical.OR:
                return bool1 | bool2;
        }

        return bool1;
    }

    private boolean areInteger(SemanticContainer exp1, SemanticContainer exp2) {

        ITipus tipus1 = (ITipus) exp1.getValue(SemanticContainer.TIPUS);
        ITipus tipus2 = (ITipus) exp2.getValue(SemanticContainer.TIPUS);

        if (tipus1 != null && tipus2 != null) return tipus1.getNom().equals(Lexicographical.SENCER) && tipus2.getNom().equals(Lexicographical.SENCER);
        else return false;
    }

    private boolean areBoolean(SemanticContainer exp1, SemanticContainer exp2) {

        ITipus tipus1 = (ITipus) exp1.getValue(SemanticContainer.TIPUS);
        ITipus tipus2 = (ITipus) exp2.getValue(SemanticContainer.TIPUS);

        if (tipus1 != null && tipus2 != null) return tipus1.getNom().equals(Lexicographical.LOGIC) && tipus2.getNom().equals(Lexicographical.LOGIC);
        else return false;
    }
}
