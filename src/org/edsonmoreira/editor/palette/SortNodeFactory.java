/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import java.util.List;
import org.edsonmoreira.hfd.catalog.Catalog;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author edson
 */

public class SortNodeFactory extends ChildFactory<Catalog>{
    private Catalog catalog;
    
    public SortNodeFactory(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    protected boolean createKeys(List<Catalog> list) {
        list.add(catalog);
        return true;
    }
    
    @Override
    protected Node[] createNodesForKey(Catalog key) {
        return new Node[]{
            new SortNode(Bundle.SUBARCHITECTURE_IDENTIFIER(), catalog),
            new SortNode(Bundle.COMPONENTS_IDENTIFIER(), catalog),
        };
    }
    
    
}
