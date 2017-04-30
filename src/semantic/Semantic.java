package semantic;
/* **************************** IMPORTS *****************************/

/**
 * Classe que implementa l'analitzador semàntic del compilador de Babel2017.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 30/04/2017
 */
public class Semantic {

    /* ************************** ATTRIBUTES ***************************/
    private static Semantic instance;

    /* ************************* CONSTRUCTORS ***************************/
    private Semantic() {

        if (instance == null) {

            instance = this;
        }
    }

    public static Semantic getInstance() {

        return instance;
    }

    /* ************************* PUBLIC METHODS **************************/

    /* ************************ PRIVATE METHODS *************************/

}
