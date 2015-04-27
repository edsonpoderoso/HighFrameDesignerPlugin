/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author edson
 */
public class ProvidedInterfacePinWidget extends Widget {

    public ProvidedInterfacePinWidget(Scene scene) {
        super(scene);
    }
    
    @Override
    protected Rectangle calculateClientArea() {
        Graphics2D gc = this.getGraphics();
        ModelComponentWidget parent = (ModelComponentWidget) this.getParentWidget();
        int baseSize = parent.getCellHeight();
        return new Rectangle(baseSize, baseSize);
    }

    @Override
    protected void paintWidget() {
        Graphics2D gc = this.getGraphics();
        ModelComponentWidget parent = (ModelComponentWidget) this.getParentWidget();
        int baseSize = parent.getCellHeight();
        int spacerSize = (int) (baseSize * .3);
        int circleSize = baseSize - 2 * spacerSize;
        gc.setColor(Color.BLACK);
        gc.drawOval(spacerSize + 1 , spacerSize + 1, circleSize, circleSize);
        gc.drawLine(0, baseSize / 2, spacerSize, baseSize / 2);
    }
    
}
