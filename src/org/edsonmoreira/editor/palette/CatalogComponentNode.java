/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.edsonmoreira.hfd.Component;
import org.edsonmoreira.hfd.Dimension;
import org.edsonmoreira.hfd.HFDElement;
import org.edsonmoreira.hfd.InterfacePack;
import org.edsonmoreira.hfd.Position;
import org.edsonmoreira.hfd.catalog.CatalogComponent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author edson
 */
public class CatalogComponentNode extends AbstractNode {

    CatalogComponent catalogComponent;

    public CatalogComponentNode(CatalogComponent component) {
        super(Children.LEAF);
        this.catalogComponent = component;
        this.setDisplayName(this.getLabel());
        this.setIconBaseWithExtension(Bundle.COMPONENT_ICON_BASENAME());
    }
    
    
    
    private HFDElement getHFDElement() {
        Component hfdElement;
        hfdElement = new Component();
        hfdElement.setName(catalogComponent.getName());
        InterfacePack pInt = new InterfacePack();
        pInt.getInterface().addAll(catalogComponent.getProvidedInterfaces().getInterface());
        hfdElement.setProvidedInterfaces(pInt);
        InterfacePack rInt = new InterfacePack();
        rInt.getInterface().addAll(catalogComponent.getRequiredInterfaces().getInterface());
        hfdElement.setRequiredInterfaces(rInt);
        Dimension d = new Dimension();
        d.setWidth(50);
        d.setHeight(50);
        hfdElement.setDimension(d);
        Position p = new Position();
        p.setX(0);
        p.setY(0);
        hfdElement.setPosition(p);
        return hfdElement;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        Transferable answer = new ExTransferable.Single(HFDElementFlavor.HFDELEMENT_FLAVOR) {

            @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return getHFDElement();
            }
        };
        return answer;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<b>" + catalogComponent.getName() + "</b>";
    }

    @Override
    public String getShortDescription() {
        return this.getDescriptionLabel();
    }
    
    private String getLabel(){
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        result.append("<table cellspacing=\"0\" cellpadding=\"1\">");
        result.append("<tr>");
        result.append("<td><b>").append(catalogComponent.getName()).append("</b></td>");
        result.append("</tr>");
        result.append("<tr>");
        result.append("<td>").append(catalogComponent.getShortDescription()).append("</td>");
        result.append("</tr>");
        result.append("</table>");
        result.append("</html>");
        return result.toString();
    }
    
    private String getDescriptionLabel() {
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        result.append("<table cellspacing=\"0\" cellpadding=\"1\">");
        result.append("<tr>");
        result.append("<td><b>Name</b></td>");
        result.append("<td>").append(catalogComponent.getName()).append("</td>");
        result.append("</tr>");
        result.append("<tr>");
        result.append("<td><b>Description</b></td>");
        result.append("<td>").append(catalogComponent.getShortDescription()).append("</td>");
        result.append("</tr>");
        result.append("<tr>");
        result.append("<td><b>Provided Interfaces</b></td>");

        List<String> pInterfaces = catalogComponent.getProvidedInterfaces().getInterface();
        Collections.sort(pInterfaces);
        if (!pInterfaces.isEmpty()) {
            boolean first = true;
            for (String e : pInterfaces) {
                if (first) {
                    result.append("<td>").append(e).append("</td></tr>");
                    first = false;
                } else {
                    result.append("<tr><td></td><td>");
                    result.append(e);
                    result.append("</td></tr>");
                    first = false;
                }
            }
        } else {
            result.append("<td></td></tr>");
        }
        result.append("<tr><td><b>Required Interfaces</b></td>");
        List<String> rInterfaces = catalogComponent.getRequiredInterfaces().getInterface();
        Collections.sort(rInterfaces);
        if (!rInterfaces.isEmpty()) {
            boolean first = true;
            for (String e : rInterfaces) {
                if (first) {
                    result.append("<td>").append(e).append("</td></tr>");
                    first = false;
                } else {
                    result.append("<tr><td></td><td>");
                    result.append(e);
                    result.append("</td></tr>");
                    first = false;
                }
            }
        } else {
            result.append("<td></td></tr>");
        }
        result.append("</table>");
        result.append("</html>");
        return result.toString();
    }

}
