/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.palette;

import java.util.ArrayList;
import java.util.List;
import org.edsonmoreira.hfd.catalog.Catalog;
import org.edsonmoreira.hfd.catalog.CatalogComponent;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;


/**
 *
 * @author edson
 */
class DiagramElementNodeFactory extends ChildFactory<Catalog> {

    Catalog catalog;
    String sort;

    public DiagramElementNodeFactory(String sort, Catalog catalog) {
        this.catalog = catalog;
        this.sort = sort;
    }

    @Override
    protected boolean createKeys(List<Catalog> list) {
        list.add(catalog);
        return true;
    }

    @Override
    protected Node[] createNodesForKey(Catalog key) {
        if (this.sort.equals(Bundle.SUBARCHITECTURE_IDENTIFIER())) {
            return new Node[]{
                new SubArchitectureNode()
            };
        } else if (this.sort.equals(Bundle.COMPONENTS_IDENTIFIER())) {
            return createNodesFromCatalog();
        } else {
            return new Node[]{};
        }
    }

    private Node[] createNodesFromCatalog() {
        List<CatalogComponent> list = catalog.getComponent();
        List<CatalogComponentNode> result = new ArrayList<CatalogComponentNode>();
        for (CatalogComponent e : list) {
            result.add(new CatalogComponentNode(e));
        }
        Node[] nodes = new Node[result.size()];
        for(int i =0; i< nodes.length; i++){
            nodes[i] = result.get(i);
        }
        return nodes;
    }
}
