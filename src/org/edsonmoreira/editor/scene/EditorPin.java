/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.scene;

/**
 *
 * @author edson
 */
class EditorPin {

    private final String inter;
    private final InterfaceType type;

    public EditorPin(String inter, InterfaceType type) {
        assert(inter != null && type != null);
        this.inter = inter;
        this.type = type;
    }

    public String getInter() {
        return inter;
    }

    public InterfaceType getType() {
        return type;
    }

    public enum InterfaceType 
    {
        Provided,
        Required;
    }
}