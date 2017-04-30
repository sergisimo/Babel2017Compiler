package taulasimbols;

/**
 * <p>Clase que representa un tipus simple del llenguatge Babel, com per exemple un
 * sencer, caracter, real, ...</p>
 */
public class TipusSimple extends ITipus {

    /**<p>Valor m�nim que pot assolir el tipus</p>*/
    private Object minim;

    /**<p>Valor m�xim que pot assolir el tipus</p>*/
    private Object maxim;

    /**<p>Constructor de TipusSimple</p>*/
    public TipusSimple() {
    }

    /**
     * <p>Constructor de TipusSimple</p>
     * @param nom del tipus
     * @param tamany que ocupa el tipus
     */
    public TipusSimple(String nom, int tamany) {
        this.nom = nom;
        this.tamany = tamany;
    }

    /**
     * <p>Constructor de TipusSimple</p>
     * @param maxim valor m�nim que pot assolir aquest tipus
     * @param minim valor m�xim que pot assolir aquest tipus
     */
    public TipusSimple(Object minim, Object maxim) {
        this.minim = minim;
        this.maxim = maxim;
    }
    /**
     * <p>Constructor de TipusSimple</p>
     * @param nom del tipus
     * @param tamany que ocupa el tipus
     * @param minim valor m�nim que pot assolir aquest tipus
     * @param maxim valor m�xim que pot assolir aquest tipus
     */
    public TipusSimple(String nom, int tamany, Object minim, Object maxim) {
        this.nom = nom;
        this.tamany = tamany;
        this.minim = minim;
        this.maxim = maxim;
    }

    /**
     * <p>Obt� el valor m�nim que pot assolir el tipus simple</p>
     * @return Object
     */
    public Object getMinim() {
        return minim;
    }

    /**
     * <p>Estableix el valor m�nim que pot assolir el tipus simple</p>
     * @param minim
     */
    public void setMinim(Object minim) {
        this.minim = minim;
    }

    /**
     * <p>Obt� el valor m�xim que pot assolir el tipus simple</p>
     * @return Object
     */
    public Object getMaxim() {
        return maxim;
    }

    /**
     * <p>Estableix el valor m�xim que pot assolir el tipus simple</p>
     * @param maxim
     */
    public void setMaxim(Object maxim) {
        this.maxim = maxim;
    }

    /**
     * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
     * retorna cert si s�n iguals.</p>
     * @param obj
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj instanceof TipusSimple) {
            TipusSimple tipus = (TipusSimple) obj;
            boolean equals = super.equals(tipus);
            if (maxim == null)
                equals &= tipus.maxim == null;
            else
                equals &= maxim.equals(tipus.maxim);

            if (minim == null)
                equals &= tipus.minim == null;
            else
                equals &= minim.equals(tipus.minim);

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
        String str = "<TipusSimple Nom=\"" + nom + "\"" +
                " Tamany=\"" + tamany + "\"";
        if (minim != null)
            str += " M�nim=\"" + minim.toString() + "\"";
        else
            str += " M�nim=\"null\"";

        if (maxim != null)
            str += " M�xim=\"" + maxim.toString() + "\">";
        else
            str += " M�xim=\"null\">";

        str += "</TipusSimple>";
        return str;
    }
}

