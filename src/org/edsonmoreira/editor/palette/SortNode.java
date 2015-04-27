/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import org.edsonmoreira.hfd.catalog.Catalog;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author edson
 */
class SortNode extends AbstractNode{


    SortNode(String sort, Catalog catalog) {
        super(Children.create(new DiagramElementNodeFactory(sort, catalog), true));
        this.setDisplayName(sort);
    }
    
}
