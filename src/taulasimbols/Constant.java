package taulasimbols;

/**
 * <p>Clase que representa una constant del llenguatge Babel</p>
 */
public class Constant {

    /**<p>nom de la constant</p>*/
    private String nom;

    /**<p>valor de la constant</p>*/
    private Object valor;

    /**<p>tipus de la constant</p>*/
    private ITipus tipus;

    /** <p>Creador de la clase Constant</p>*/
    public Constant() {
    }

    /**
     * <p>Creador de la clase Constant</p>
     * @param nom
     * @param tipus
     * @param valor
     */
    public Constant(String nom, ITipus tipus, Object valor) {
        this.nom = nom;
        this.tipus = tipus;
        this.valor = valor;
    }

    /**
     * <p>Obt� el nom de la constant</p>
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /**
     * <p>Estableix el nom de la constant</p>
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * <p>Obt� el valor de la constant</p>
     * @return Object
     */
    public Object getValor() {
        return valor;
    }

    /**
     * <p>Estableix el valor de la constant</p>
     * @param valor
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }

    /**
     * <p>Obt� el tipus de la constant</p>
     * @return ITipus
     */
    public ITipus getTipus() {
        return tipus;
    }

    /**
     * <p>Estableix el tipus de la constant</p>
     * @param tipus
     */
    public void setTipus(ITipus tipus) {
        this.tipus = tipus;
    }

    /**
     * <p>Obt� tota la informaci� del objecte en format XML</p>
     * @return String
     */
    public String toXml() {
        String result = "<Constant Nom=\"" + nom + "\"";
        if (valor != null)
            result += " Valor=\"" + valor.toString() + "\">";
        else
            result += " Valor=\"null\">";

        result += tipus.toXml();
        result += "</Constant>";
        return result;
    }
}

