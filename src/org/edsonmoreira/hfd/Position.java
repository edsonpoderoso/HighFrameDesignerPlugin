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
 * <p>Classe Java de position complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="position">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "position", propOrder = {
    "x",
    "y"
})
public class Position {

    @XmlSchemaType(name = "unsignedInt")
    protected long x;
    @XmlSchemaType(name = "unsignedInt")
    protected long y;

    /**
     * Obtém o valor da propriedade x.
     * 
     */
    public long getX() {
        return x;
    }

    /**
     * Define o valor da propriedade x.
     * 
     */
    public void setX(long value) {
        this.x = value;
    }

    /**
     * Obtém o valor da propriedade y.
     * 
     */
    public long getY() {
        return y;
    }

    /**
     * Define o valor da propriedade y.
     * 
     */
    public void setY(long value) {
        this.y = value;
    }

}
