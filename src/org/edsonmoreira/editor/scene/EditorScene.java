/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.scene;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.edsonmoreira.editor.Util;
import org.edsonmoreira.editor.palette.HFDElementFlavor;
import org.edsonmoreira.hfd.Component;
import org.edsonmoreira.hfd.Connection;
import org.edsonmoreira.hfd.HFDElement;
import org.edsonmoreira.hfd.SubArchitecture;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorShapeFactory;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author edson
 */
public class EditorScene extends GraphPinScene<HFDElement, Connection, EditorPin> implements UndoRedo.Provider {

    private final int ARROW_DEGREES = 40;
    private final int ARROW_SIZE = 12;
    private final int PROVIDER_PIN_SIZE = 0;
    //Scene Layers 
    private final LayerWidget dropLayer = new LayerWidget(this);
    private final LayerWidget archLayer = new LayerWidget(this);
    private final LayerWidget componentsLayer = new LayerWidget(this);
    private final LayerWidget edgesLayer = new LayerWidget(this);
    private final LayerWidget sateliteLayer = new LayerWidget(this);
    private transient MultiViewElementCallback callback;

    //Lookup setup
    private final InstanceContent ic = new InstanceContent();
    private final Lookup dynamicLookup = new AbstractLookup(ic);

    //UndoRedo Setup
    private final transient UndoRedo.Manager undoRedo = new UndoRedo.Manager();
    private long componentIdTracker = 1000;
    private long subArchTracker = 1000;

    public void setCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    //Widget Action Providers
    private final ConnectProvider myConnectProvider = new ConnectProvider() {
        @Override
        public boolean isSourceWidget(Widget widget) {
            Collection<Connection> edges = EditorScene.this.getEdges();
            for (Connection edge : edges) {
                if (EditorScene.this.findWidget(EditorScene.this.getEdgeSource(edge)) == widget) {
                    return false;
                }
            }
            return widget instanceof RequiredInterfacePinWidget;
        }

        @Override
        public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
            if (targetWidget instanceof ProvidedInterfacePinWidget) {
                EditorPin sourcePin = (EditorPin) EditorScene.this.findObject(sourceWidget);
                EditorPin targetPin = (EditorPin) EditorScene.this.findObject(targetWidget);
                if (sourcePin.getInter().equals(targetPin.getInter())) {
                    return ConnectorState.ACCEPT;
                }
            }
            return ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomTargetWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveTargetWidget(Scene scene, Point point) {
            return null;
        }

        @Override
        public void createConnection(Widget sourceWidget, Widget targetWidget) {
            Connection connection = new Connection();
            long sourceId = ((Component) EditorScene.this.findObject(sourceWidget.getParentWidget())).getId();
            long targetId = ((Component) EditorScene.this.findObject(targetWidget.getParentWidget())).getId();
            String inter = ((EditorPin) EditorScene.this.findObject(sourceWidget)).getInter();
            connection.setProtocol("undetermined");
            connection.setRequirerComponentId(sourceId);
            connection.setProviderComponentId(targetId);
            connection.setInterface(inter);
            undoRedo.addEdit(new AddEdgeEdit(connection));
        }

    };

    private final MoveProvider myMultiMoveProvider = new MoveProvider() {
        private final HashMap<Widget, Point> originals = new HashMap<Widget, Point>();
        private Point original;

        //undoRedo Management
        @Override
        public void movementStarted(Widget widget) {
            Object object = findObject(widget);
            if (isNode(object)) {
                for (Object o : getSelectedObjects()) {
                    if (isNode(o)) {
                        Widget w = findWidget(o);
                        if (w != null) {
                            originals.put(w, w.getPreferredLocation());
                        }
                    }
                }
            } else {
                originals.put(widget, widget.getPreferredLocation());
            }
        }

        @Override
        public void movementFinished(Widget widget) {
            //undoRedo Management
            HashMap<Object, Point> done = new HashMap<Object, Point>();
            HashMap<Object, Point> undone = new HashMap<Object, Point>();
            for (Map.Entry<Widget, Point> entry : originals.entrySet()) {
                //sync with model
                EditorScene.this.syncElementPosition(entry.getKey());
                Object object = findObject(entry.getKey());
                undone.put(object, entry.getValue());
                done.put(object, entry.getKey().getLocation());
            }
            undoRedo.addEdit(new MoveWidgetEdit(undone, done));

            originals.clear();
            original = null;
        }

        @Override
        public Point getOriginalLocation(Widget widget) {
            original = widget.getPreferredLocation();
            return original;
        }

        @Override
        public void setNewLocation(Widget widget, Point location) {
            int dx = location.x - original.x;
            int dy = location.y - original.y;
            for (Map.Entry<Widget, Point> entry : originals.entrySet()) {
                Point point = entry.getValue();
                entry.getKey().setPreferredLocation(new Point(point.x + dx, point.y + dy));
            }

        }

    };

    private final SelectProvider mySelectProvider = new SelectProvider() {
        List<Node> nodes = new ArrayList<Node>();

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean invertSelection) {
            return false;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean invertSelection) {
            return findObject(widget) != null || widget == EditorScene.this;
        }

        @Override
        public void select(Widget widget, Point point, boolean invertSelection) {
            Set<?> oldSelection = EditorScene.this.getSelectedObjects();
            Set<?> newSelection;

            if (widget == EditorScene.this) {
                EditorScene.this.setSelectedObjects(Collections.EMPTY_SET);
            } else {
                Object object = findObject(widget);
                setFocusedObject(object);
                if (object != null) {
                    if (!invertSelection && getSelectedObjects().contains(object)) {
                        return;
                    }
                    userSelectionSuggested(Collections.singleton(object), invertSelection);
                } else {
                    userSelectionSuggested(Collections.emptySet(), invertSelection);
                }
            }
            selectionChanged();
        }

        private void selectionChanged() {
            Set<?> selected = EditorScene.this.getSelectedObjects();
            if (selected.isEmpty()) {
                for (Node n : nodes) {
                    ic.remove(n);
                }
                nodes.clear();//NODE TEST
            } else {
                for (Object o : selected) {
                    if (o instanceof SubArchitecture) {
                        SubArchitecture co = (SubArchitecture) o;
                        nodes.add(co.getNode());
                        ic.add(co.getNode());
                    } else if (o instanceof Connection) {
                        Connection co = (Connection) o;
                        nodes.add(co.getNode());
                        ic.add(co.getNode());
                    }
                }
            }
        }
    };

    private final AcceptProvider myAcceptProvider = new AcceptProvider() {

        @Override
        public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
            if (widget == EditorScene.this && t.isDataFlavorSupported(HFDElementFlavor.HFDELEMENT_FLAVOR)) {
                return ConnectorState.ACCEPT;
            } else {
                return ConnectorState.REJECT;
            }
        }

        @Override
        public void accept(Widget widget, Point point, Transferable t
        ) {
            try {
                HFDElement hfdElement = (HFDElement) t.getTransferData(HFDElementFlavor.HFDELEMENT_FLAVOR);
                hfdElement.setPosition(Util.pointToPosition(point));
                Set<HFDElement> set = new HashSet<HFDElement>();
                if (hfdElement.isComponent()) {
                    hfdElement.getComponent().setId(componentIdTracker++);
                } else {
                    hfdElement.getSubArchitecture().setId(subArchTracker++);
                }
                set.add(hfdElement);
                undoRedo.addEdit(new AddElementsEdit(set));
                callback.getTopComponent().requestActive();
            } catch (UnsupportedFlavorException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    };

    private final ResizeProvider myResizeProvider = new ResizeProvider() {
        Rectangle originalBounds;
        Point originalPosition;

        @Override
        public void resizingStarted(Widget widget) {
            assert widget != null;
            originalBounds = (Rectangle) widget.getBounds().clone();
            originalPosition = (Point) widget.getLocation().clone();
        }

        @Override
        public void resizingFinished(Widget widget) {
            Rectangle newBounds = widget.getBounds();
            assert newBounds  != null;
            Point nbPos = newBounds.getLocation();
            Point correctedPosition = new Point((int) (originalPosition.getX() + nbPos.getX()), (int) (originalPosition.getY() + nbPos.getY()));
            widget.setPreferredLocation(correctedPosition);
            Rectangle correctedBounds = new Rectangle(0, 0, (int) newBounds.getWidth(), (int) newBounds.getHeight());
            widget.setPreferredBounds(correctedBounds);
            SubArchitecture sa = (SubArchitecture) EditorScene.this.findObject(widget);
            sa.setDimension(Util.rectangleToDimension(correctedBounds));
            sa.setPosition(Util.pointToPosition(correctedPosition));
            undoRedo.addEdit(new ResizeEdit((HFDElement) EditorScene.this.findObject(widget), originalPosition, originalBounds, correctedPosition, correctedBounds));
        }
    };

    private final PopupMenuProvider myPopupMenuProvider = new PopupMenuProvider() {

        @Override
        public JPopupMenu getPopupMenu(Widget widget, Point point) {
            JPopupMenu menu = new JPopupMenu();
            menu.add(new AbstractAction("Delete selected") {

                @Override
                public boolean isEnabled() {
                    return super.isEnabled() && !EditorScene.this.getSelectedObjects().isEmpty();
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    undoRedo.addEdit(new DeleteEdit());
                }
            });
            return menu;
        }
    };

    private final RectangularSelectDecorator myRectangularSelectDecorator = new RectangularSelectDecorator() {

        @Override
        public Widget createSelectionWidget() {
            return new Widget(EditorScene.this) {

                @Override
                protected Rectangle calculateClientArea() {
                    return new Rectangle(1, 1);
                }

                @Override
                protected void paintWidget() {
                    Graphics2D gc = getGraphics();
                    gc.setColor(Color.BLUE);
                    Composite x = gc.getComposite();
                    gc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.1f));
                    gc.fillRect((int) getPreferredBounds().getX(), (int) getPreferredBounds().getY(), (int) getPreferredBounds().getWidth(), (int) getPreferredBounds().getHeight());
                    gc.setComposite(x);
                    gc.drawRect((int) getPreferredBounds().getX(), (int) getPreferredBounds().getY(), (int) getPreferredBounds().getWidth(), (int) getPreferredBounds().getHeight());
                    gc.dispose();
                }
            };
        }
    };

    //Label handler for connections (NOT THE BEST WAY TO DO IT BUT IT WORKS)
    private final Map<Connection, LabelWidget> ConToLabel = new HashMap<Connection, LabelWidget>();
    private final ChangeListener connectionChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            assert e.getSource() instanceof Connection;
            Connection con = (Connection) e.getSource();
            LabelWidget lw = ConToLabel.get(con);
            assert lw != null;
            lw.setLabel(con.getProtocol());

        }
    };
    //End of Label handler

    public EditorScene() {
        super();
        //Configures the layers on the scene
        this.addChild(archLayer);
        this.addChild(componentsLayer);
        this.addChild(edgesLayer);
        this.addChild(dropLayer);
        this.addChild(sateliteLayer);

        //Configures the zoom, pan, selection and Accept actions on the scene
        this.getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        this.getActions().addAction(ActionFactory.createPanAction());
        this.getActions().addAction(ActionFactory.createSelectAction(mySelectProvider));
        this.getActions().addAction(ActionFactory.createAcceptAction(myAcceptProvider));
        this.getActions().addAction(ActionFactory.createPopupMenuAction(myPopupMenuProvider));
        this.getActions().addAction(ActionFactory.createRectangularSelectAction(myRectangularSelectDecorator,
                sateliteLayer,
                ActionFactory.createObjectSceneRectangularSelectProvider(this)));
    }

    //This method adds a new node while creates its pins
    public void myAddNode(HFDElement element) {
        assert element != null;
        if (element.isComponent()) {
            Component component = element.getComponent();
            List<String> providedInterfaces = component.getProvidedInterfaces().getInterface();
            Set<EditorPin> Pins = new HashSet<EditorPin>();
            
            //creating pins
            for (String e : providedInterfaces) {
                Pins.add(new EditorPin(e, EditorPin.InterfaceType.Provided));
            }
            List<String> requiredInterfaces = component.getRequiredInterfaces().getInterface();
            for (String e : requiredInterfaces) {
                Pins.add(new EditorPin(e, EditorPin.InterfaceType.Required));
            }
            
            //adding node and pins
            Widget widget = addNode(element);
            assert widget != null;
            //element.setDimension(Util.rectangleToDimension(widget.getBounds()));
            for (EditorPin p : Pins) {
                addPin(element, p);
            }
        } else {
            addNode(element);
        }
    }

    //this method  adds a new edge while creating its label
    public void myAddEdge(Connection e) {
        assert e != null;
        ConnectionWidget w = (ConnectionWidget) this.addEdge(e);
        Component sourceComponent = this.getComponentById(e.getRequirerComponentId());
        Component targetComponent = this.getComponentById(e.getProviderComponentId());
        EditorPin sourcePin = this.getNodePinByInterface(sourceComponent, e.getInterface(), EditorPin.InterfaceType.Required);
        EditorPin targetPin = this.getNodePinByInterface(targetComponent, e.getInterface(), EditorPin.InterfaceType.Provided);
        this.setEdgeSource(e, sourcePin);
        this.setEdgeTarget(e, targetPin);

        //Label Making
        LabelWidget label1 = new LabelWidget(this, e.getProtocol());
        label1.setOpaque(false);
        w.addChild(label1);
        w.setConstraint(label1, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_CENTER, 0.5f);
        ConToLabel.put(e, label1);
        e.RegisterChangeListener(connectionChangeListener);

    }

    //this helper method syncs a node object position with its widget position
    private void syncElementPosition(Widget widget) {
        assert !(widget instanceof ConnectionWidget);
        HFDElement element = (HFDElement) this.findObject(widget);
        element.setPosition(Util.pointToPosition(widget.getPreferredLocation()));
    }

    //this method syncs a widget position with its node object position
    private void syncWidgetPosition(HFDElement element) {
        Widget widget = this.findWidget(element);
        assert widget != null;
        widget.setPreferredLocation(Util.positionToPoint(element.getPosition()));
    }

    @Override
    protected Widget attachNodeWidget(HFDElement n) {
        assert n != null;
        Widget widget;
        Point pos = Util.positionToPoint(n.getPosition());
        if (n.isComponent()) {
            Component component = n.getComponent();
            widget = new ModelComponentWidget(this, component);
            componentsLayer.addChild(widget);
        } else {
            widget = new ModelSubarchWidget(this, n.getSubArchitecture());
            archLayer.addChild(widget);
            widget.getActions().addAction(ActionFactory.createResizeAction(ActionFactory.createFreeResizeStategy(),
                    ActionFactory.createDefaultResizeControlPointResolver(),
                    myResizeProvider
            ));
        }
        
        widget.getActions().addAction(ActionFactory.createSelectAction(mySelectProvider));
        widget.getActions().addAction(ActionFactory.createMoveAction(null, myMultiMoveProvider));
        widget.setPreferredLocation(pos);

        this.validate();
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(Connection e) {
        ConnectionWidget widget = new ConnectionWidget(this);
        widget.setRouter(RouterFactory.createOrthogonalSearchRouter(this.componentsLayer));
        widget.setForeground(Color.BLACK);
        widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(ARROW_DEGREES, ARROW_SIZE));
        widget.setStroke(new BasicStroke(1.0f));
        widget.getActions().addAction(ActionFactory.createSelectAction(mySelectProvider));
        edgesLayer.addChild(widget);
        return widget;
    }

    //this helper method is used for finding sources and target objects for edges
    private Component getComponentById(long id) {
        Set<?> elements = this.getObjects();
        for (Object o : elements) {
            if (o instanceof Component
                    && ((Component) o).getId() == id) {
                return (Component) o;
            }
        }
        throw new IllegalArgumentException("The component with the given id is not present on the scene");
    }

    //this helper method find a model Pin by its parent Component and its interface
    private EditorPin getNodePinByInterface(Component n, String inter, EditorPin.InterfaceType type) {
        Collection<EditorPin> pins = this.getNodePins(n);
        for (EditorPin p : pins) {
            if (p.getType().equals(type) && p.getInter().equals(inter)) {
                return p;
            }
        }
        throw new IllegalArgumentException("The given arguments do not match a single Pin on the scene");
    }

    @Override
    protected Widget attachPinWidget(HFDElement n, EditorPin p) {
        Component component = n.getComponent();
        ModelComponentWidget widget = (ModelComponentWidget) this.findWidget(component);
        Point pos;
        Widget pin;
        if (p.getType() == EditorPin.InterfaceType.Provided) {
            pos = widget.getProvidedInterfacePinPosition(p.getInter());
            pin = new ProvidedInterfacePinWidget(this);
        } else {
            pos = widget.getRequiredInterfacePinPosition(p.getInter());
            pin = new RequiredInterfacePinWidget(this);
        }
        widget.addChild(pin);
        pin.setPreferredLocation(pos);
        pin.getActions().addAction(ActionFactory.createConnectAction(dropLayer, myConnectProvider));
        return pin;

    }

    @Override
    protected void attachEdgeSourceAnchor(Connection edge, EditorPin oldSourcePin, EditorPin sourcePin
    ) {
        Widget sourcePinWidget = findWidget(sourcePin);
        Anchor sourceAnchor = new EditorAnchor(sourcePinWidget, 0); //AnchorFactory.createCircularAnchor(sourcePinWidget, 0);
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        edgeWidget.setSourceAnchor(sourceAnchor);
    }

    @Override
    protected void attachEdgeTargetAnchor(Connection edge, EditorPin oldTargetPin, EditorPin targetPin
    ) {
        Widget targetPinWidget = findWidget(targetPin);
        Anchor targetAnchor = new EditorAnchor(targetPinWidget, PROVIDER_PIN_SIZE);//AnchorFactory.createCircularAnchor(targetPinWidget, PROVIDER_PIN_SIZE);//Utilizar um valor calculado posteriormente
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        edgeWidget.setTargetAnchor(targetAnchor);
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(dynamicLookup);
    }

    @Override
    public UndoRedo getUndoRedo() {
        return undoRedo;

    }

    public void ready() {
        Collection<HFDElement> nodes = this.getNodes();
        long maxC = 0;
        long maxS = 0;
        for(HFDElement node:nodes){
            if(node.isComponent()){
                maxC = max(node.getComponent().getId(), maxC);
            }
            if(node.isSubarchitecture()){
                maxS = max(node.getSubArchitecture().getId(), maxS);
            }
        }
        subArchTracker = maxS+1;
        componentIdTracker = maxC+1;
        //undoRedo.discardAllEdits();
    }

    //Undoable Edits
    private final class ResizeEdit extends AbstractUndoableEdit {

        Point undonePosition;
        Rectangle undoneBounds;
        Point donePosition;
        Rectangle doneBounds;
        HFDElement element;
        Widget widget;

        public ResizeEdit(HFDElement element, Point originalPosition, Rectangle originalBounds, Point correctedPosition, Rectangle correctedBounds) {
            this.element = element;
            this.undonePosition = (Point) originalPosition.clone();
            this.undoneBounds = (Rectangle) originalBounds.clone();
            this.donePosition = (Point) correctedPosition.clone();
            this.doneBounds = (Rectangle) correctedBounds.clone();
        }

        @Override
        public String getPresentationName() {
            return "resize SubArchitecture";
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo(); //To change body of generated methods, choose Tools | Templates.
            element.setDimension(Util.rectangleToDimension(undoneBounds));
            element.setPosition(Util.pointToPosition(undonePosition));
            widget = EditorScene.this.findWidget(element);
            widget.setPreferredBounds((Rectangle) undoneBounds.clone());
            widget.setPreferredLocation((Point) undonePosition.clone());
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo(); //To change body of generated methods, choose Tools | Templates.
            element.setDimension(Util.rectangleToDimension(doneBounds));
            element.setPosition(Util.pointToPosition(donePosition));
            widget = EditorScene.this.findWidget(element);
            widget.setPreferredBounds((Rectangle) doneBounds.clone());
            widget.setPreferredLocation((Point) donePosition.clone());
        }

    }

//    private final class SelectEdit extends AbstractUndoableEdit {
//
//        Set<?> oldSelection;
//        Set<?> newSelection;
//
//        public SelectEdit(Set<?> oldSelection, Set<?> newSelection) {
//            super();
//            this.oldSelection = oldSelection;
//            this.newSelection = newSelection;
//        }
//
//        @Override
//        public String getPresentationName() {
//            return "Selection change";
//        }
//
//        @Override
//        public void undo() throws CannotUndoException {
//            super.undo(); //To change body of generated methods, choose Tools | Templates.
//            EditorScene.this.setSelectedObjects(oldSelection);
//        }
//
//        @Override
//        public void redo() throws CannotRedoException {
//            super.redo(); //To change body of generated methods, choose Tools | Templates.
//            EditorScene.this.setSelectedObjects(newSelection);
//        }
//
//    }

    private final class AddEdgeEdit implements UndoableEdit {

        boolean alive = true;
        boolean done = false;
        Connection connection;

        public AddEdgeEdit(Connection connection) {
            assert (connection != null);
            this.connection = connection;
            redo();
        }

        @Override
        public void undo() throws CannotUndoException {
            if (canUndo()) {
                EditorScene.this.removeEdge(connection);
                done = false;
            } else {
                throw new CannotUndoException();
            }
        }

        @Override
        public boolean canUndo() {
            return done && alive;
        }

        @Override
        public void redo() throws CannotRedoException {
            if (canRedo()) {
                EditorScene.this.myAddEdge(connection);
                done = true;
            } else {
                throw new CannotRedoException();
            }
        }

        @Override
        public boolean canRedo() {
            return !done && alive;
        }

        @Override
        public void die() {
            alive = false;
        }

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean replaceEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean isSignificant() {
            return true;
        }

        @Override
        public String getPresentationName() {
            return "Create connection";
        }

        @Override
        public String getUndoPresentationName() {
            return getPresentationName();
        }

        @Override
        public String getRedoPresentationName() {
            return getPresentationName();
        }
    }

    private final class AddElementsEdit implements UndoableEdit {

        Set<HFDElement> elements;
        boolean alive = true;
        boolean done = false;

        public AddElementsEdit(Set<HFDElement> elements) {
            if (!elements.isEmpty()) {
                this.elements = elements;
                this.redo();
            } else {
                throw new IllegalArgumentException("An AddElementsEdit needs at least one element to add");
            }
        }

        @Override
        public void undo() throws CannotUndoException {
            if (canUndo()) {
                for (HFDElement element : elements) {
                    EditorScene.this.removeNode(element);
                }
                done = false;
            } else {
                throw new CannotUndoException();
            }
        }

        @Override
        public boolean canUndo() {
            return alive && done;
        }

        @Override
        public void redo() throws CannotRedoException {
            if (canRedo()) {
                for (HFDElement element : elements) {
                    EditorScene.this.myAddNode(element);
                }
                this.done = true;
            } else {
                throw new CannotRedoException();
            }

        }

        @Override
        public boolean canRedo() {
            return (!done) && alive;
        }

        @Override
        public void die() {
            alive = false;
        }

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean replaceEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean isSignificant() {
            return true;
        }

        @Override
        public String getPresentationName() {
            return "Add " + elements.size() + " element" + (elements.size() == 1 ? "" : "s");
        }

        @Override
        public String getUndoPresentationName() {
            return getPresentationName();
        }

        @Override
        public String getRedoPresentationName() {
            return getPresentationName();
        }

    }

    private final class MoveWidgetEdit extends AbstractUndoableEdit {

        Map<Object, Point> pre;
        Map<Object, Point> post;

        @Override
        public String getPresentationName() {
            return "Move element" + ((pre.size() == 1) ? "" : "s");
        }

        public MoveWidgetEdit(Map<Object, Point> pre, Map<Object, Point> post) {
            super();
            this.pre = pre;
            this.post = post;
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            for (Map.Entry<Object, Point> e : pre.entrySet()) {
                Widget widget = EditorScene.this.findWidget(e.getKey());
                widget.setPreferredLocation(e.getValue());
                EditorScene.this.syncElementPosition(widget);
            }
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo(); 
            for (Map.Entry<Object, Point> e : post.entrySet()) {
                Widget widget = EditorScene.this.findWidget(e.getKey());
                widget.setPreferredLocation(e.getValue());
                EditorScene.this.syncElementPosition(widget);
            }
        }

    }

    private final class DeleteEdit extends AbstractUndoableEdit {

        Set<Object> set = new HashSet<Object>();
        Set<Connection> connections = new HashSet<Connection>();
        Set<HFDElement> nodes = new HashSet<HFDElement>();

        public DeleteEdit() {
            Set<?> selected = EditorScene.this.getSelectedObjects();
            for (Object o : selected) {
                set.add(o);
            }
            for (Object e : set) {
                if (e instanceof Connection) {
                    connections.add((Connection) e);
                    EditorScene.this.removeEdge((Connection) e);
                } else if (e instanceof HFDElement) {
                    HFDElement element = (HFDElement) e;
                    nodes.add(element);
                }
            }
            //Performance wise nodes should be removed after connections, otherwise you have to check if the connection is still on the scene
            for (HFDElement n : nodes) {
                if (n.isComponent()) {
                    Collection<EditorPin> pins = EditorScene.this.getNodePins(n);
                    pins = (pins == null ? Collections.EMPTY_SET : pins);
                    for (EditorPin p : pins) {
                        Collection<Connection> edges;
                        edges = findPinEdges(p, true, true);
                        connections.addAll(edges);
                    }
                }
                EditorScene.this.removeNodeWithEdges(n);
            }

        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo(); //To change body of generated methods, choose Tools | Templates.
            for (HFDElement e : nodes) {
                myAddNode(e);
            }
            for (Connection c : connections) {
                myAddEdge(c);
            }
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            for (Connection c : connections) {
                EditorScene.this.removeEdge(c);
            }
            for (HFDElement e : nodes) {
                EditorScene.this.removeNodeWithEdges(e);
            }

        }

        @Override
        public String getPresentationName() {
            return "delete selected";
        }

    }

    //Anchor for connections
    private class EditorAnchor extends Anchor {
        int gap = 7;
        
        EditorAnchor(Widget widget, int gap){
            super(widget);
            this.gap = gap;
        }
        
        @Override
        public Result compute(Entry entry) {
            Widget widget = getRelatedWidget();
            Rectangle bounds = widget.convertLocalToScene(widget.getBounds());
            Point center = getCenter(bounds);
            return new Anchor.Result (new Point (center.x + gap, center.y), Direction.RIGHT);
        }
        

        private Point getCenter(Rectangle bounds) {
            double  x = bounds.getX() + (bounds.getMaxX() - bounds.getMinX())/2;
            double y = bounds.getY() + (bounds.getMaxY() - bounds.getMinY())/2;
            return new Point((int)x,(int)y);
        }

    }
    
    

}
