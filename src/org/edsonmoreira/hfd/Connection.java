//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.03.09 às 10:24:39 PM GMT-03:00 
//
package org.edsonmoreira.hfd;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
 * Classe Java de connection complex type.
 *
 * <p>
 * O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro
 * desta classe.
 *
 * <pre>
 * &lt;complexType name="connection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="protocol" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interface" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="providerComponentId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="requirerComponentId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connection", propOrder = {
    "protocol",
    "_interface",
    "providerComponentId",
    "requirerComponentId"
})
public class Connection {

    @XmlElement(required = true)
    protected String protocol;
    @XmlElement(name = "interface", required = true)
    protected String _interface;
    @XmlSchemaType(name = "unsignedInt")
    protected long providerComponentId;
    @XmlSchemaType(name = "unsignedInt")
    protected long requirerComponentId;

    /**
     * Obtém o valor da propriedade protocol.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Define o valor da propriedade protocol.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setProtocol(String value) {
        this.protocol = value;
        this.fireChangeHappenned();
    }

    /**
     * Obtém o valor da propriedade interface.
     *
     * @return possible object is {@link String }
     *
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Define o valor da propriedade interface.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    /**
     * Obtém o valor da propriedade providerComponentId.
     *
     */
    public long getProviderComponentId() {
        return providerComponentId;
    }

    /**
     * Define o valor da propriedade providerComponentId.
     *
     */
    public void setProviderComponentId(long value) {
        this.providerComponentId = value;
    }

    /**
     * Obtém o valor da propriedade requirerComponentId.
     *
     */
    public long getRequirerComponentId() {
        return requirerComponentId;
    }

    /**
     * Define o valor da propriedade requirerComponentId.
     *
     */
    public void setRequirerComponentId(long value) {
        this.requirerComponentId = value;
    }

    
    public Node getNode() {
        return node;
    }

    @XmlTransient
    private final Connection conn = this;

    @XmlTransient
    private final Node node = new AbstractNode(Children.LEAF) {

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            //set.setName("Connection Properies");
            try {
                Node.Property protocolProp = new PropertySupport.Reflection(conn, String.class, "getProtocol", "setProtocol");
                protocolProp.setName("Protocolo");
                set.put(protocolProp);
            } catch (NoSuchMethodException ex) {
                ErrorManager.getDefault();
            }
            sheet.put(set);
            return sheet;
        }
    };
    
    
    @XmlTransient
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>();
    
    
    public void RegisterChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }
    
    
    public void fireChangeHappenned() {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
