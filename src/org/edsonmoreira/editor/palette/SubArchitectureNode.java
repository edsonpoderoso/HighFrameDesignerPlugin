/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.edsonmoreira.editor.Util;
import org.edsonmoreira.hfd.Dimension;
import org.edsonmoreira.hfd.HFDElement;
import org.edsonmoreira.hfd.SubArchitecture;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author edson
 */
public class SubArchitectureNode extends AbstractNode {

    SubArchitectureNode() {
        super(Children.LEAF);
        this.setDisplayName("Sub-Architecture");
        this.setIconBaseWithExtension(Bundle.SUBARCH_ICON_BASENAME());
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
                return getHfdElement();
            }
        };
        return answer;
    }
    
    private HFDElement getHfdElement(){
        SubArchitecture sub = new SubArchitecture();
        sub.setName("Untitled");
        sub.setModel("Undetermined");
        sub.setIpAdress("0.0.0.0");
        Dimension d = new Dimension();
        d.setHeight(100);
        d.setWidth(100);
        sub.setDimension(d);
        sub.setPosition(Util.pointToPosition(new Point(0, 0)));
        return sub;
    }
}
