/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import java.awt.datatransfer.DataFlavor;
import org.edsonmoreira.hfd.HFDElement;

/**
 *
 * @author edson
 */
public class HFDElementFlavor extends DataFlavor {
    
    public static final DataFlavor HFDELEMENT_FLAVOR = new HFDElementFlavor();

    public HFDElementFlavor() {
        super(HFDElement.class, "HFDElement");
    }
    
}
