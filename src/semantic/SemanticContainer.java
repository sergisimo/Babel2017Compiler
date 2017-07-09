/**
 * This type was created in VisualAge.
 */
package semantic;
import java.util.Hashtable;

public class SemanticContainer {

    /* ************************** CONSTANTS ***************************/
    public static final String VALOR = "VALOR";
    public static final String ESTATIC = "ESTATIC";
    public static final String TIPUS = "TIPUS";
    public static final String OPERADOR = "OPERADOR";
    public static final String COMPARACIO = "COMPARACIO";
    public static final String LINEA = "LINEA";
    public static final String COLUMNA = "COLUMNA";
    public static final String REG = "REG";
    public static final String TOKEN = "TOKEN";

    private Hashtable attributes = new Hashtable();
    /**
     * SemanticContainer constructor comment.
     */
    public SemanticContainer() {
        super();
        this.setValue(REG, -1);
    }
    /**
     * This method was created in VisualAge.
     * @return java.util.Hashtable
     */
    public java.util.Hashtable getAttributes() {
        return attributes;
    }
    /**
     * This method was created in VisualAge.
     * @return java.lang.Object
     * @param attributeID java.lang.Object
     */
    public Object getValue(java.lang.Object attributeID) {
        return getAttributes().get(attributeID);
    }
    /**
     * This method was created in VisualAge.
     */
    public void removeAll() {
        getAttributes().clear();
    }
    /**
     * This method was created in VisualAge.
     * @param attributeID java.lang.Object
     */
    public void removeAttribute(java.lang.Object attributeID) {
        getAttributes().remove(attributeID);
    }
    /**
     * This method was created in VisualAge.
     * @param newValue java.util.Hashtable
     */
    public void setAttributes(java.util.Hashtable newValue) {
        this.attributes = newValue;
    }
    /**
     * This method was created in VisualAge.
     * @param attributeID java.lang.Object
     * @param attributeValue java.lang.Object
     */
    public void setValue(java.lang.Object attributeID, java.lang.Object attributeValue) {
        getAttributes().put(attributeID,attributeValue);
    }


    public void copy(SemanticContainer exp)
    {

        if (exp.getAttributes().containsKey(VALOR))
            setValue(VALOR,exp.getValue(VALOR));
        if (exp.getAttributes().containsKey(ESTATIC))
            setValue(ESTATIC,exp.getValue(ESTATIC));
        if (exp.getAttributes().containsKey(TIPUS))
            setValue(TIPUS,exp.getValue(TIPUS));
        if (exp.getAttributes().containsKey(OPERADOR))
            setValue(OPERADOR,exp.getValue(OPERADOR));
        if (exp.getAttributes().containsKey(COMPARACIO))
            setValue(COMPARACIO,exp.getValue(COMPARACIO));
        if (exp.getAttributes().containsKey(LINEA))
            setValue(LINEA,exp.getValue(LINEA));
        if (exp.getAttributes().containsKey(COLUMNA))
            setValue(COLUMNA,exp.getValue(COLUMNA));
        if (exp.getAttributes().containsKey(REG))
            setValue(REG,exp.getValue(REG));
        if (exp.getAttributes().containsKey(TOKEN))
            setValue(TOKEN,exp.getValue(TOKEN));

    }

    public String toString() {

        return "Valor: "+getValue(VALOR)+
                "; Estatic: "+getValue(ESTATIC)+
                "; Tipus: "+getValue(TIPUS)+
                "; Oper: "+getValue(OPERADOR)+
                "; Comp: "+getValue(COMPARACIO)+
                "; Linea: "+getValue(LINEA)+
                "; Columna: "+getValue(COLUMNA)+
                "; Registre: "+getValue(REG);
    }

}
