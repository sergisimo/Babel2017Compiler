package syntactic;

/* **************************** IMPORTS *****************************/
import lexicographical.Token;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Classe que implementa l'estructura on es guardaran tots els símbols directors.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 13/03/2017
 */
public class SynchronizationSets {

    /* ************************** CONSTANTS ***************************/
                            // FIRST
    private static Token.TokenType[] FIRST_P = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FIRST_DECL = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FIRST_DECL_CTE_VAR = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FIRST_DECL_CV = {Token.TokenType.CONST, Token.TokenType.VAR};
    private static Token.TokenType[] FIRST_DEC_CTE = {Token.TokenType.CONST};
    private static Token.TokenType[] FIRST_DEC_VAR = {Token.TokenType.VAR};
    private static Token.TokenType[] FIRST_DEC_FUN = {Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FIRST_LLISTA_PARAM = {Token.TokenType.PERREF, Token.TokenType.PERVAL, Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FIRST_LLISTA_PARAM1 = {Token.TokenType.PERREF, Token.TokenType.PERVAL};
    private static Token.TokenType[] FIRST_PAS_VALOR = {Token.TokenType.PERREF, Token.TokenType.PERVAL};
    private static Token.TokenType[] FIRST_LLISTA_PARAM2 = {Token.TokenType.COMA, Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FIRST_TIPUS = {Token.TokenType.VECTOR, Token.TokenType.TIPUS_SIMPLE};
    private static Token.TokenType[] FIRST_TIPUS_ABR = {Token.TokenType.VECTOR, Token.TokenType.TIPUS_SIMPLE};
    private static Token.TokenType[] FIRST_EXP = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP1 = {Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.DOS_PUNTS};
    private static Token.TokenType[] FIRST_EXP_SIMPLE = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP_SIMPLE1 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_TERME1 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.DOS_PUNTS};
    private static Token.TokenType[] FIRST_TERME2 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR};
    private static Token.TokenType[] FIRST_TERME = {Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_FACTOR1 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FIRST_FACTOR2 = {Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FIRST_FACTOR = {Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP2 = {Token.TokenType.PARENTESI_DARRERE, Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP3 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP4 = {Token.TokenType.PARENTESI_DARRERE, Token.TokenType.COMA};
    private static Token.TokenType[] FIRST_VARIABLE5 = {Token.TokenType.PARENTESI_DAVANT, Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND, Token.TokenType.CLAUDATOR_DAVANT};
    private static Token.TokenType[] FIRST_VARIABLE = {Token.TokenType.ID};
    private static Token.TokenType[] FIRST_VARIABLE1 = {Token.TokenType.CLAUDATOR_DAVANT, Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND, Token.TokenType.IGUAL};
    private static Token.TokenType[] FIRST_LLISTA_INST = {Token.TokenType.ID, Token.TokenType.ESCRIURE, Token.TokenType.LLEGIR, Token.TokenType.CICLE, Token.TokenType.MENTRE, Token.TokenType.SI, Token.TokenType.RETORNAR, Token.TokenType.PERCADA};
    private static Token.TokenType[] FIRST_LLISTA_INST1 = {Token.TokenType.FIPROG, Token.TokenType.FIFUNC, Token.TokenType.FINS, Token.TokenType.FIMENTRE, Token.TokenType.FIPER, Token.TokenType.FISI, Token.TokenType.SINO, Token.TokenType.ID, Token.TokenType.ESCRIURE, Token.TokenType.LLEGIR, Token.TokenType.CICLE, Token.TokenType.MENTRE, Token.TokenType.SI, Token.TokenType.RETORNAR, Token.TokenType.PERCADA};
    private static Token.TokenType[] FIRST_VARIABLE2 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID, Token.TokenType.SI};
    private static Token.TokenType[] FIRST_VARIABLE2_ABR = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID, Token.TokenType.SI};
    private static Token.TokenType[] FIRST_VARIABLE3 = {Token.TokenType.ID};
    private static Token.TokenType[] FIRST_VARIABLE4 = {Token.TokenType.PARENTESI_DARRERE, Token.TokenType.COMA};
    private static Token.TokenType[] FIRST_INST = {Token.TokenType.ID, Token.TokenType.ESCRIURE, Token.TokenType.LLEGIR, Token.TokenType.CICLE, Token.TokenType.MENTRE, Token.TokenType.SI, Token.TokenType.RETORNAR, Token.TokenType.PERCADA};
    private static Token.TokenType[] FIRST_INST1 = {Token.TokenType.FISI, Token.TokenType.SINO};
    private static Token.TokenType[] FIRST_EXP_ESCRIURE = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FIRST_EXP_ESCRIURE1 = {Token.TokenType.PARENTESI_DARRERE, Token.TokenType.COMA};

                            // FOLLOWS
    private static Token.TokenType[] FOLLOWS_P = {};
    private static Token.TokenType[] FOLLOWS_DECL = {Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_DECL_CTE_VAR = {Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_DECL_CV = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_DEC_CTE = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_DEC_VAR = {Token.TokenType.CONST, Token.TokenType.VAR, Token.TokenType.FUNCIO, Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_DEC_FUN = {Token.TokenType.PROG};
    private static Token.TokenType[] FOLLOWS_LLISTA_PARAM = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_LLISTA_PARAM1 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_PAS_VALOR = {Token.TokenType.ID};
    private static Token.TokenType[] FOLLOWS_LLISTA_PARAM2 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_TIPUS = {Token.TokenType.PUNT_COMA, Token.TokenType.COMA, Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_TIPUS_ABR = {Token.TokenType.TIPUS_SIMPLE};
    private static Token.TokenType[] FOLLOWS_EXP = {Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.DOS_PUNTS};
    private static Token.TokenType[] FOLLOWS_EXP1 = {Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.DOS_PUNTS};
    private static Token.TokenType[] FOLLOWS_EXP_SIMPLE = {Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.OPER_REL, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.COMA};
    private static Token.TokenType[] FOLLOWS_EXP_SIMPLE1 = {Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FOLLOWS_TERME1 = {Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.OPER_REL, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.COMA};
    private static Token.TokenType[] FOLLOWS_TERME2 = {Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FOLLOWS_TERME = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FOLLOWS_FACTOR1 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FOLLOWS_FACTOR2 = {Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FOLLOWS_FACTOR = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FOLLOWS_EXP2 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_EXP3 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_EXP4 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_VARIABLE5 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND};
    private static Token.TokenType[] FOLLOWS_VARIABLE = {Token.TokenType.IGUAL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.COMA};
    private static Token.TokenType[] FOLLOWS_VARIABLE1 = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.OR, Token.TokenType.OPER_REL, Token.TokenType.PARENTESI_DARRERE, Token.TokenType.CLAUDATOR_DARRERE, Token.TokenType.DOS_PUNTS, Token.TokenType.FER, Token.TokenType.LLAVORS, Token.TokenType.PUNT_COMA, Token.TokenType.PUNT_PUNT, Token.TokenType.COMA, Token.TokenType.MULTIPLICAR, Token.TokenType.DIVIDIR, Token.TokenType.AND, Token.TokenType.IGUAL};
    private static Token.TokenType[] FOLLOWS_LLISTA_INST = {Token.TokenType.FIPROG, Token.TokenType.FIFUNC, Token.TokenType.FINS, Token.TokenType.FIMENTRE, Token.TokenType.FIPER, Token.TokenType.FISI, Token.TokenType.SINO};
    private static Token.TokenType[] FOLLOWS_LLISTA_INST1 = {Token.TokenType.FIPROG, Token.TokenType.FIFUNC, Token.TokenType.FINS, Token.TokenType.FIMENTRE, Token.TokenType.FIPER, Token.TokenType.FISI, Token.TokenType.SINO};
    private static Token.TokenType[] FOLLOWS_VARIABLE2 = {Token.TokenType.PUNT_COMA};
    private static Token.TokenType[] FOLLOWS_VARIABLE2_ABR = {Token.TokenType.MES, Token.TokenType.MENYS, Token.TokenType.NOT, Token.TokenType.CTE_ENTERA, Token.TokenType.CTE_LOGICA, Token.TokenType.CTE_CADENA, Token.TokenType.PARENTESI_DAVANT, Token.TokenType.ID};
    private static Token.TokenType[] FOLLOWS_VARIABLE3 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_VARIABLE4 = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_INST = {Token.TokenType.PUNT_COMA};
    private static Token.TokenType[] FOLLOWS_INST1 = {Token.TokenType.FISI};
    private static Token.TokenType[] FOLLOWS_EXP_ESCRIURE = {Token.TokenType.PARENTESI_DARRERE};
    private static Token.TokenType[] FOLLOWS_EXP_ESCRIURE1 = {Token.TokenType.PARENTESI_DARRERE};

    /* ************************** ATTRIBUTES ***************************/
    private LinkedList<Token.TokenType>[] synchronizationSets;

    private static SynchronizationSets instance;

    /* ************************* CONSTRUCTORS ***************************/
    public SynchronizationSets() {

        if (instance == null) {

            synchronizationSets = new LinkedList[37];
            this.initiateSynchronizationSets();

            instance = this;
        }
    }

    public static SynchronizationSets getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/
    public LinkedList<Token.TokenType>[] getSynchronizationSets() {

        return synchronizationSets;
    }

    /* ************************ PRIVATE METHODS *************************/
    private void initiateSynchronizationSets() {

        this.ssP();
        this.ssDecCte();
        this.ssDecVar();
        this.ssDecFun();
        this.ssDecFunA();
        this.ssDecFunB();
        this.ssLlistaParam1();
        this.ssLlistaParam2();
        this.ssTipus();
        this.ssTipusAbr();
        this.ssTerme();
        this.ssFactor1();
        this.ssFactor();
        this.ssFactorA();
        this.ssVariable5();
        this.ssVariable();
        this.ssVariable1();
        this.ssLlistaInst();
        this.ssLlistaInstA();
        this.ssVariable2Abr();
        this.ssInst();
        this.ssInstA();
        this.ssInstB();
        this.ssInstC();
        this.ssInstD();
        this.ssInstE();
        this.ssInstF();
        this.ssInstG();
        this.ssInstH();
        this.ssInstI();
        this.ssEOF();
        this.ssIniciCodi();
        this.ssIniciFun();
        this.ssRepetDec();
        this.ssEndLine();
        this.ssID();
        this.ssExpEscrirue();
    }


    private void ssP() {

        synchronizationSets[0] = new LinkedList<>();
        synchronizationSets[0].addAll(Arrays.asList(FOLLOWS_P));
        synchronizationSets[0].add(Token.TokenType.EOF);
    }

    private void ssDecCte() {

        synchronizationSets[1] = new LinkedList<>();
        synchronizationSets[1].addAll(Arrays.asList(FOLLOWS_DEC_CTE));
        synchronizationSets[1].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[1].add(Token.TokenType.FUNC);
        synchronizationSets[1].add(Token.TokenType.EOF);
    }

    private void ssDecVar() {

        synchronizationSets[2] = new LinkedList<>();
        synchronizationSets[2].addAll(Arrays.asList(FOLLOWS_DEC_VAR));
        synchronizationSets[2].add(Token.TokenType.FUNC);
        synchronizationSets[2].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[2].add(Token.TokenType.EOF);
    }

    private void ssDecFun() {

        synchronizationSets[3] = new LinkedList<>();
        synchronizationSets[3].add(Token.TokenType.DOS_PUNTS);
        synchronizationSets[3].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[3].add(Token.TokenType.EOF);
    }

    private void ssDecFunA() {

        synchronizationSets[4] = new LinkedList<>();
        synchronizationSets[4].addAll(Arrays.asList(FIRST_EXP));
        synchronizationSets[4].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[4].add(Token.TokenType.EOF);
    }

    private void ssDecFunB() {

        synchronizationSets[5] = new LinkedList<>();
        synchronizationSets[5].addAll(Arrays.asList(FIRST_DEC_FUN));
        synchronizationSets[5].addAll(Arrays.asList(FOLLOWS_DEC_FUN));
        synchronizationSets[5].add(Token.TokenType.EOF);
    }

    private void ssLlistaParam1() {

        synchronizationSets[6] = new LinkedList<>();
        synchronizationSets[6].addAll(Arrays.asList(FIRST_TIPUS));
        synchronizationSets[6].addAll(Arrays.asList(FOLLOWS_TIPUS));
        synchronizationSets[6].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[6].add(Token.TokenType.EOF);
    }

    private void ssLlistaParam2() {

        synchronizationSets[7] = new LinkedList<>();
        synchronizationSets[7].addAll(Arrays.asList(FOLLOWS_LLISTA_PARAM2));
        synchronizationSets[7].addAll(Arrays.asList(FIRST_LLISTA_PARAM));
        synchronizationSets[7].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[7].add(Token.TokenType.EOF);
    }

    private void ssTipus() {

        synchronizationSets[8] = new LinkedList<>();
        synchronizationSets[8].addAll(Arrays.asList(FOLLOWS_TIPUS));
        synchronizationSets[8].addAll(Arrays.asList(FOLLOWS_LLISTA_PARAM2));
        synchronizationSets[8].addAll(Arrays.asList(FIRST_DECL_CV));
        synchronizationSets[8].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[8].add(Token.TokenType.EOF);
    }

    private void ssTipusAbr() {

        synchronizationSets[9] = new LinkedList<>();
        synchronizationSets[9].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[9].add(Token.TokenType.FIFUNC);
        synchronizationSets[9].add(Token.TokenType.FIPROG);
        synchronizationSets[9].add(Token.TokenType.FIMENTRE);
        synchronizationSets[9].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[9].add(Token.TokenType.EOF);
    }

    private void ssTerme() {

        synchronizationSets[10] = new LinkedList<>();
        synchronizationSets[10].addAll(Arrays.asList(FIRST_FACTOR1));
        synchronizationSets[10].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[10].add(Token.TokenType.EOF);
    }

    private void ssFactor1() {

        synchronizationSets[11] = new LinkedList<>();
        synchronizationSets[11].addAll(Arrays.asList(FIRST_FACTOR1));
        synchronizationSets[11].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[11].add(Token.TokenType.EOF);
    }

    private void ssFactor() {

        synchronizationSets[12] = new LinkedList<>();
        synchronizationSets[12].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[12].add(Token.TokenType.TIPUS_SIMPLE);
        synchronizationSets[12].add(Token.TokenType.EOF);
    }

    private void ssFactorA() {

        synchronizationSets[13] = new LinkedList<>();
        synchronizationSets[13].addAll(Arrays.asList(FOLLOWS_FACTOR));
        synchronizationSets[13].addAll(Arrays.asList(FIRST_LLISTA_INST1));
        synchronizationSets[13].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[13].add(Token.TokenType.EOF);
    }

    private void ssVariable5() {

        synchronizationSets[14] = new LinkedList<>();
        synchronizationSets[14].addAll(Arrays.asList(FOLLOWS_VARIABLE5));
        synchronizationSets[14].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[14].add(Token.TokenType.EOF);
    }

    private void ssVariable() {

        synchronizationSets[15] = new LinkedList<>();
        synchronizationSets[15].addAll(Arrays.asList(FIRST_VARIABLE1));
        synchronizationSets[15].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[15].add(Token.TokenType.EOF);
    }

    private void ssVariable1() {

        synchronizationSets[16] = new LinkedList<>();
        synchronizationSets[16].addAll(Arrays.asList(FOLLOWS_VARIABLE1));
        synchronizationSets[16].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[16].add(Token.TokenType.EOF);
    }

    private void ssLlistaInst() {

        synchronizationSets[17] = new LinkedList<>();
        synchronizationSets[17].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[17].addAll(Arrays.asList(FIRST_LLISTA_INST1));
        synchronizationSets[17].add(Token.TokenType.EOF);
    }

    private void ssLlistaInstA() {

        synchronizationSets[18] = new LinkedList<>();
        synchronizationSets[18].addAll(Arrays.asList(FIRST_LLISTA_INST1));
        synchronizationSets[18].add(Token.TokenType.EOF);
    }

    private void ssVariable2Abr() {

        synchronizationSets[19] = new LinkedList<>();
        synchronizationSets[19].addAll(Arrays.asList(FOLLOWS_VARIABLE2));
        synchronizationSets[19].add(Token.TokenType.EOF);
    }

    private void ssInst() {

        synchronizationSets[20] = new LinkedList<>();
        synchronizationSets[20].addAll(Arrays.asList(FIRST_VARIABLE2));
        synchronizationSets[20].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[20].add(Token.TokenType.EOF);
    }

    private void ssInstA() {

        synchronizationSets[21] = new LinkedList<>();
        synchronizationSets[21].addAll(Arrays.asList(FOLLOWS_INST));
        synchronizationSets[21].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[21].add(Token.TokenType.EOF);
    }

    private void ssInstB() {

        synchronizationSets[22] = new LinkedList<>();
        synchronizationSets[22].addAll(Arrays.asList(FOLLOWS_INST));
        synchronizationSets[22].add(Token.TokenType.EOF);
    }

    private void ssInstC() {

        synchronizationSets[23] = new LinkedList<>();
        synchronizationSets[23].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[23].add(Token.TokenType.FUNC);
        synchronizationSets[23].add(Token.TokenType.EOF);
    }

    private void ssInstD() {


        synchronizationSets[24] = new LinkedList<>();
        synchronizationSets[24].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[24].add(Token.TokenType.DOS_PUNTS);
        synchronizationSets[24].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[24].add(Token.TokenType.FUNC);
        synchronizationSets[24].add(Token.TokenType.EOF);
    }

    private void ssInstE() {

        synchronizationSets[25] = new LinkedList<>();
        synchronizationSets[25].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[25].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[25].add(Token.TokenType.FUNC);
        synchronizationSets[25].add(Token.TokenType.EOF);
    }

    private void ssInstF() {

        synchronizationSets[26] = new LinkedList<>();
        synchronizationSets[26].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[26].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[26].add(Token.TokenType.FUNC);
        synchronizationSets[26].add(Token.TokenType.EOF);
    }

    private void ssInstG() {

        synchronizationSets[27] = new LinkedList<>();
        synchronizationSets[27].addAll(Arrays.asList(FOLLOWS_INST));
        synchronizationSets[27].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[27].add(Token.TokenType.EOF);
    }

    private void ssInstH() {

        synchronizationSets[28] = new LinkedList<>();
        synchronizationSets[28].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[28].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[28].add(Token.TokenType.EOF);
    }

    private void ssInstI() {

        synchronizationSets[29] = new LinkedList<>();
        synchronizationSets[29].addAll(Arrays.asList(FOLLOWS_INST));
        synchronizationSets[29].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[29].add(Token.TokenType.EOF);
    }

    private void ssEOF() {

        synchronizationSets[30] = new LinkedList<>();
        synchronizationSets[30].add(Token.TokenType.EOF);
    }

    private void ssIniciCodi() {

        synchronizationSets[31] = new LinkedList<>();
        synchronizationSets[31].add(Token.TokenType.VAR);
        synchronizationSets[31].add(Token.TokenType.CONST);
        synchronizationSets[31].add(Token.TokenType.FUNCIO);
        synchronizationSets[31].add(Token.TokenType.PROG);
        synchronizationSets[31].add(Token.TokenType.FUNC);
        synchronizationSets[31].add(Token.TokenType.EOF);
    }

    private void ssIniciFun() {

        synchronizationSets[32] = new LinkedList<>();
        synchronizationSets[32].add(Token.TokenType.FUNCIO);
        synchronizationSets[32].add(Token.TokenType.PROG);
        synchronizationSets[32].add(Token.TokenType.EOF);
    }

    private void ssRepetDec() {

        synchronizationSets[33] = new LinkedList<>();
        synchronizationSets[33].add(Token.TokenType.VAR);
        synchronizationSets[33].add(Token.TokenType.CONST);
        synchronizationSets[33].add(Token.TokenType.FUNC);
        synchronizationSets[33].add(Token.TokenType.PROG);
        synchronizationSets[33].add(Token.TokenType.EOF);
    }

    private void ssEndLine() {

        synchronizationSets[34] = new LinkedList<>();
        synchronizationSets[34].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[34].add(Token.TokenType.VAR);
        synchronizationSets[34].add(Token.TokenType.CONST);
        synchronizationSets[34].add(Token.TokenType.FUNC);
        synchronizationSets[34].add(Token.TokenType.EOF);
    }

    private void ssID() {

        synchronizationSets[35] = new LinkedList<>();
        synchronizationSets[35].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[35].add(Token.TokenType.PARENTESI_DAVANT);
        synchronizationSets[35].add(Token.TokenType.EOF);
    }

    private void ssExpEscrirue() {

        synchronizationSets[36] = new LinkedList<>();
        synchronizationSets[36].addAll(Arrays.asList(FIRST_LLISTA_INST));
        synchronizationSets[36].addAll(Arrays.asList(FIRST_EXP));
        synchronizationSets[36].add(Token.TokenType.FIFUNC);
        synchronizationSets[36].add(Token.TokenType.FIPROG);
        synchronizationSets[36].add(Token.TokenType.FIMENTRE);
        synchronizationSets[36].add(Token.TokenType.PUNT_COMA);
        synchronizationSets[36].add(Token.TokenType.EOF);
    }
}
