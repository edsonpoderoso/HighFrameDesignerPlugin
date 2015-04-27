//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.03.09 às 08:00:57 PM GMT-03:00 
//


package org.edsonmoreira.hfd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de dimension complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="dimension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dimension", propOrder = {
    "width",
    "height"
})
public class Dimension {

    @XmlSchemaType(name = "unsignedInt")
    protected long width;
    @XmlSchemaType(name = "unsignedInt")
    protected long height;

    /**
     * Obtém o valor da propriedade width.
     * 
     */
    public long getWidth() {
        return width;
    }

    /**
     * Define o valor da propriedade width.
     * 
     */
    public void setWidth(long value) {
        this.width = value;
    }

    /**
     * Obtém o valor da propriedade height.
     * 
     */
    public long getHeight() {
        return height;
    }

    /**
     * Define o valor da propriedade height.
     * 
     */
    public void setHeight(long value) {
        this.height = value;
    }

}
