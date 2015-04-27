/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.hfd;

/**
 *
 * @author edson
 */
public interface HFDElement {
    public boolean isComponent();
    public boolean isSubarchitecture();
    public Component getComponent();
    public SubArchitecture getSubArchitecture();
    public Position getPosition();
    public void setPosition(Position p);
    public Dimension getDimension();
    public void setDimension(Dimension d);
    
}
