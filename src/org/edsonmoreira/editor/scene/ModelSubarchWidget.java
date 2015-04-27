/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.scene;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.edsonmoreira.editor.Util;
import org.netbeans.api.visual.border.BorderFactory;
import org.edsonmoreira.hfd.SubArchitecture;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author edson
 */
public class ModelSubarchWidget extends Widget {

    private final int BASE_SIZE = 300;
    private final int INTERFACES_IDENT = 5;
    private final int CELL_PADDING = 3;
    private final int BORDER_THICKNESS = 8;
    private final Color backgroudColor = this.getColor(240, 240, 200);
    private final Color foregroundColor = Color.BLACK;
    private final SubArchitecture subArchitecture;

    public ModelSubarchWidget(Scene scene, SubArchitecture subArchitecture) {
        super(scene);
        this.subArchitecture = subArchitecture;
        setPreferredBounds(Util.dimensionToRectangle(subArchitecture.getDimension()));
        setPreferredSize(Util.dimensionToRectangle(subArchitecture.getDimension()).getSize());
    }

    private int getMinimumWidth(Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(this.getFont());
        double width = 2 * CELL_PADDING + metrics.stringWidth("Nome: " + subArchitecture.getName());
        width = Math.max(width, 2 * CELL_PADDING + metrics.stringWidth("Modelo: " + subArchitecture.getModel()));
        width = Math.max(width, 2 * CELL_PADDING + metrics.stringWidth("IP: " + subArchitecture.getIpAdress()));
        return (int) width;
    }

    protected int getCellHeight(Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(this.getFont());
        return (int) (2 * CELL_PADDING + metrics.getHeight());
    }

    @Override
    protected Rectangle calculateClientArea() {
        return getPreferredBounds();
    }

    @Override
    protected void paintWidget() {
        paintBackground();
        Graphics2D gc = this.getGraphics();
        int ascent = gc.getFontMetrics().getAscent();
        int fontSize = gc.getFontMetrics().getHeight();
        gc.setColor(foregroundColor);
        gc.drawRect(0, 0, (int) this.getPreferredBounds().getWidth(), (int) this.getPreferredBounds().getHeight());
        paintBorder();
        gc.drawString("Nome: " + subArchitecture.getName(), CELL_PADDING, CELL_PADDING + ascent);
        gc.drawString("Modelo: " + subArchitecture.getModel(), CELL_PADDING, getCellHeight(gc) + CELL_PADDING + ascent);
        gc.drawString("IP: " + subArchitecture.getIpAdress(), CELL_PADDING, 2 * getCellHeight(gc) + CELL_PADDING + ascent);
    }

    private Color getColor(int r, int g, int b) {
        return Color.decode("0x" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b));
    }

    @Override
    protected void paintBackground() {
        Graphics2D gc = this.getGraphics();
        gc.setColor(backgroudColor);
        gc.fill(getPreferredBounds());

    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state); //To change body of generated methods, choose Tools | Templates.
        if (state.isSelected()) {
            this.setBorder(BorderFactory.createResizeBorder(BORDER_THICKNESS, Color.MAGENTA, true));
        } else {
            this.setBorder(BorderFactory.createEmptyBorder());
        }
    }

}
