//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.03.09 às 08:00:57 PM GMT-03:00 
//
package org.edsonmoreira.hfd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 * <p>
 * Classe Java de subArchitecture complex type.
 *
 * <p>
 * O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro
 * desta classe.
 *
 * <pre>
 * &lt;complexType name="subArchitecture">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="position" type="{hfd.edsonmoreira.org}position"/>
 *         &lt;element name="dimension" type="{hfd.edsonmoreira.org}dimension"/>
 *         &lt;element name="model" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ipAdress" type="{hfd.edsonmoreira.org}IPv4Address"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subArchitecture", propOrder = {
    "name",
    "id",
    "position",
    "dimension",
    "model",
    "ipAdress"
})
public class SubArchitecture implements HFDElement {

    @XmlElement(required = true)
    protected String name;
    @XmlSchemaType(name = "unsignedInt")
    protected long id;
    @XmlElement(required = true)
    protected Position position;
    @XmlElement(required = true)
    protected Dimension dimension;
    @XmlElement(required = true)
    protected String model;
    @XmlElement(required = true)
    protected String ipAdress;

    /**
     * Obtém o valor da propriedade name.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtém o valor da propriedade id.
     *
     */
    public long getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     *
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade position.
     *
     * @return possible object is {@link Position }
     *
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Define o valor da propriedade position.
     *
     * @param value allowed object is {@link Position }
     *
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * Obtém o valor da propriedade dimension.
     *
     * @return possible object is {@link Dimension }
     *
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Define o valor da propriedade dimension.
     *
     * @param value allowed object is {@link Dimension }
     *
     */
    public void setDimension(Dimension value) {
        this.dimension = value;
    }

    /**
     * Obtém o valor da propriedade model.
     *
     * @return possible object is {@link String }
     *
     */
    public String getModel() {
        return model;
    }

    /**
     * Define o valor da propriedade model.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Obtém o valor da propriedade ipAdress.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIpAdress() {
        return ipAdress;
    }

    /**
     * Define o valor da propriedade ipAdress.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIpAdress(String value) {
        this.ipAdress = value;
    }

    @Override
    public boolean isComponent() {
        return false;
    }

    @Override
    public boolean isSubarchitecture() {
        return true;
    }

    @Override
    public Component getComponent() {
        throw new UnsupportedOperationException("This HFDElement is not a Component"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SubArchitecture getSubArchitecture() {
        return this;
    }
    
    @XmlTransient
    public Node getNode() {
        return node;
    }
    
    @XmlTransient
    private final SubArchitecture subArch = this;
    
    @XmlTransient
    private final Node node = new AbstractNode(Children.LEAF) {

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            set.setName("SubArchitecture Properties");
            try {
                Node.Property nameProp = new PropertySupport.Reflection(subArch, String.class, "getName", "setName");
                Node.Property modeloProp = new PropertySupport.Reflection(subArch, String.class, "getModel", "setModel");
                Node.Property ipProp = new PropertySupport.Reflection(subArch, String.class, "getIpAdress", "setIpAdress");
                nameProp.setName("Name SA");
                modeloProp.setName("Model SA");
                ipProp.setName("IP SA");
                set.put(nameProp);
                set.put(modeloProp);
                set.put(ipProp);
            } catch (NoSuchMethodException ex) {
                ErrorManager.getDefault();
            }
            sheet.put(set);
            return sheet;
        }

    };
}
