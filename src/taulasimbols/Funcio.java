package taulasimbols;

/**
 * <p>Classe que representa una funci� del llenguatge Babel</p>
 */
public class Funcio extends Procediment {

    /**<p>Tipus que retorna la funci�</p>*/
    private ITipus tipus;

    /**<p>Despla�ament d'on deixa la funci� el resultat</p>*/
    private int desplacament;

    /**<p>Constructor de la classe Funcio</p>*/
    public Funcio() {
    }

    /**
     * <p>Constructor de la classe Funcio</p>
     * @param nom
     */
    public Funcio (String nom) {
        super(nom);
    }

    /**
     * <p>Constructor de la classe Funcio</p>
     * @param nom
     * @param tipus
     */
    public Funcio (String nom, ITipus tipus) {
        super(nom);
        this.tipus = tipus;
    }

    /**
     * <p>Constructor de la classe Funcio</p>
     * @param nom
     * @param etiqueta
     */
    public Funcio (String nom, String etiqueta) {
        super(nom, etiqueta);
    }

    /**
     * <p>Constructor de la classe Funcio</p>
     * @param nom
     * @param tipus
     * @param etiqueta
     */
    public Funcio (String nom, ITipus tipus, String etiqueta) {
        super(nom, etiqueta);
        this.tipus = tipus;
    }

    /**
     * <p>Constructor de la classe Funcio</p>
     * @param nom
     * @param tipus
     * @param etiqueta
     * @param tamanyFrame
     */
    public Funcio (String nom, ITipus tipus, String etiqueta, int tamanyFrame) {
        super(nom, etiqueta, tamanyFrame);
        this.tipus = tipus;
    }

    /**
     * <p>Obt� el tipus de la funci�</p>
     * @return ITipus
     */
    public ITipus getTipus() {
        return tipus;
    }

    /**
     * <p>Estableix el tipus de la funci�</p>
     * @param tipus
     */
    public void setTipus(ITipus tipus) {
        this.tipus = tipus;
    }

    /**
     * <p>Obt� el despla�ament d'on deixa la funci� el resultat</p>
     * @return int
     */
    public int getDesplacament() {
        return desplacament;
    }

    /**
     * <p>Estableix el despla�ament d'on deixa la funci� el resultat</p>
     * @param desplacament
     */
    public void setDeplacament(int desplacament) {
        this.desplacament = desplacament;
    }

    /**
     * <p>Obt� tota la informaci� del objecte en format XML</p>
     * @return String
     */
    public String toXml() {
        String result = "<Funcio Nom=\"" + getNom() + "\">";
        result += "<Parametres>";
        for (int i=0; i<getNumeroParametres(); i++)
            result += obtenirParametre(i).toXml();
        result += "</Parametres>";
        result += tipus.toXml();
        result += "</Funcio>";
        return result;
    }
}

