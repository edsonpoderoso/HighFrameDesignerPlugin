//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.02.10 às 08:20:23 PM GMT-03:00 
//


package org.edsonmoreira.hfd.catalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de catalogComponent complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="catalogComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shortDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="longDescreption" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="providedInterfaces" type="{catalog.hfd.edsonmoreira.org}interfacePack"/>
 *         &lt;element name="requiredInterfaces" type="{catalog.hfd.edsonmoreira.org}interfacePack"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "catalogComponent", propOrder = {
    "name",
    "shortDescription",
    "longDescreption",
    "providedInterfaces",
    "requiredInterfaces"
})
public class CatalogComponent {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String shortDescription;
    @XmlElement(required = true)
    protected String longDescreption;
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
     * Obtém o valor da propriedade shortDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Define o valor da propriedade shortDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Obtém o valor da propriedade longDescreption.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongDescreption() {
        return longDescreption;
    }

    /**
     * Define o valor da propriedade longDescreption.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongDescreption(String value) {
        this.longDescreption = value;
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

}
