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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de component complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="component">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="position" type="{hfd.edsonmoreira.org}position"/>
 *         &lt;element name="dimension" type="{hfd.edsonmoreira.org}dimension"/>
 *         &lt;element name="providedInterfaces" type="{hfd.edsonmoreira.org}interfacePack"/>
 *         &lt;element name="requiredInterfaces" type="{hfd.edsonmoreira.org}interfacePack"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "component", propOrder = {
    "name",
    "id",
    "position",
    "dimension",
    "providedInterfaces",
    "requiredInterfaces"
})
public class Component implements HFDElement{

    @XmlElement(required = true)
    protected String name;
    @XmlSchemaType(name = "unsignedInt")
    protected long id;
    @XmlElement(required = true)
    protected Position position;
    @XmlElement(required = true)
    protected Dimension dimension;
    @XmlElement(required = true)
    protected InterfacePack providedInterfaces;
    @XmlElement(required = true)
    protected InterfacePack requiredInterfaces;

    /**
     * Obtém o valor da propriedade name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
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
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Define o valor da propriedade position.
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * Obtém o valor da propriedade dimension.
     * 
     * @return
     *     possible object is
     *     {@link Dimension }
     *     
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Define o valor da propriedade dimension.
     * 
     * @param value
     *     allowed object is
     *     {@link Dimension }
     *     
     */
    public void setDimension(Dimension value) {
        this.dimension = value;
    }

    /**
     * Obtém o valor da propriedade providedInterfaces.
     * 
     * @return
     *     possible object is
     *     {@link InterfacePack }
     *     
     */
    public InterfacePack getProvidedInterfaces() {
        return providedInterfaces;
    }

    /**
     * Define o valor da propriedade providedInterfaces.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfacePack }
     *     
     */
    public void setProvidedInterfaces(InterfacePack value) {
        this.providedInterfaces = value;
    }

    /**
     * Obtém o valor da propriedade requiredInterfaces.
     * 
     * @return
     *     possible object is
     *     {@link InterfacePack }
     *     
     */
    public InterfacePack getRequiredInterfaces() {
        return requiredInterfaces;
    }

    /**
     * Define o valor da propriedade requiredInterfaces.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfacePack }
     *     
     */
    public void setRequiredInterfaces(InterfacePack value) {
        this.requiredInterfaces = value;
    }

    @Override
    public boolean isComponent() {
        return true;
    }

    @Override
    public boolean isSubarchitecture() {
        return false;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public SubArchitecture getSubArchitecture() {
        throw new UnsupportedOperationException("This HFDElement is not a SubArchitecture."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Component other = (Component) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (this.position != other.position && (this.position == null || !this.position.equals(other.position))) {
            return false;
        }
        if (this.dimension != other.dimension && (this.dimension == null || !this.dimension.equals(other.dimension))) {
            return false;
        }
        if (this.providedInterfaces != other.providedInterfaces && (this.providedInterfaces == null || !this.providedInterfaces.equals(other.providedInterfaces))) {
            return false;
        }
        if (this.requiredInterfaces != other.requiredInterfaces && (this.requiredInterfaces == null || !this.requiredInterfaces.equals(other.requiredInterfaces))) {
            return false;
        }
        return true;
    }
    
    
    
}
