package taulasimbols;

/**
 * Interficie base per a tots els tipus de dades que pot contenir el llenguatge
 * Babel.
 */
public abstract class ITipus {

    /**<p>Nom del tipus</p>*/
    protected String nom;

    /**<p>Tamany que ocupa el tipus en mem�ria</p>*/
    protected int tamany;

    /**
     * <p>Obt� el nom del tipus</p>
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /**
     * <p>Estableix el nom de tipus</p>
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * <p>Obt� el tamany que ocupa el tipus en mem�ria</p>
     * @return int
     */
    public int getTamany() {
        return tamany;
    }

    /**
     * <p>Estableix el tamany que ocupa el tipus en mem�ria</p>
     * @param tamany
     */
    public void setTamany(int tamany) {
        this.tamany = tamany;
    }

    /**
     * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
     * retorna cert si s�n iguals.</p>
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj instanceof ITipus) {
            ITipus itipus = (ITipus) obj;

            if (nom != null) {
                return nom.equals(itipus.getNom())
                        && tamany == itipus.getTamany();
            } else {
                return itipus.getNom() == null && tamany == itipus.getTamany();
            }
        } else {
            return false;
        }
    }

    /**
     * <p>Obt� tota la informaci� del objecte en format XML</p>
     * @return String
     */
    public abstract String toXml();
}

