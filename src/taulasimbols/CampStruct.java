package taulasimbols;

/**
 * <p>Aquesta clase representa un camp d'un tipus struct</p>
 */
public class CampStruct {

    /**<p>nom del camp</p>*/
    private String nom;

    /**<p>tipus del camp</p>*/
    private ITipus tipus;

    /**
     * <p>Creador de la clase Constant</p>
     * @param nom
     */
    public CampStruct(String nom) {
        this.nom = nom;
    }
    /**
     * <p>Creador de la clase Constant</p>
     * @param nom
     * @param  tipus
     */
    public CampStruct(String nom, ITipus tipus) {
        this.nom = nom;
        this.tipus = tipus;
    }

    /**
     * <p>Obt� el nom del tipus</p>
     * @return nom del camp
     */
    public String getNom() {
        return nom;
    }

    /**
     * <p>Estaleix el nom del camp</p>
     * @param  nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * <p>Obt� el tipus del camp</p>
     * @return tipus del camp
     */
    public ITipus getTipus() {
        return tipus;
    }

    /**
     * <p>Estableix el tipus del camp</p>
     * @param tipus
     */
    public void setTipus(ITipus tipus) {
        this.tipus = tipus;
    }

    /**
     * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
     * retorna cert si s�n iguals.</p>
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj instanceof CampStruct) {
            CampStruct camp = (CampStruct) obj;

            boolean equals = true;
            if (nom == null)
                equals = camp.nom == null;
            else
                equals = nom.equals(camp.nom);

            if (tipus == null)
                equals = camp.tipus == null;
            else
                equals = tipus.equals(camp.tipus);

            return equals;
        } else {
            return false;
        }
    }

    /**
     * <p>Obt� tota la informaci� del objecte en format XML</p>
     * @return String
     */
    public String toXml() {
        String result = "<CampStruct Nom=\"" + nom + "\">";
        if (tipus != null)
            result += tipus.toXml();

        result += "</CampStruct>";
        return result;
    }
}

