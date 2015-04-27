/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor;

import java.awt.event.ActionEvent;
import org.edsonmoreira.editor.scene.EditorScene;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.edsonmoreira.editor.palette.SortNodeFactory;
import org.edsonmoreira.hfd.Architecture;
import org.edsonmoreira.hfd.Component;
import org.edsonmoreira.hfd.Connection;
import org.edsonmoreira.hfd.SubArchitecture;
import org.edsonmoreira.hfd.catalog.Catalog;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.netbeans.spi.actions.AbstractSavable;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.MultiDataObject.Entry;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.IOProvider;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(
        displayName = "#LBL_HFD_VISUAL",
        iconBase = "org/edsonmoreira/editor/HFD.png",
        mimeType = "text/hfd+xml",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "HFDVisual",
        position = 2000
)
@Messages({"LBL_HFD_VISUAL=Model", "CATALOG_CONTEXT=org.edsonmoreira.hfd.catalog", "DOCUMENT_CONTEXT=org.edsonmoreira.hfd"})

public final class HFDVisualElement extends JPanel implements MultiViewElement, Lookup.Provider {

    private final HFDDataObject obj;
    private final JToolBar toolbar = new JToolBar();

//Scene
    private final EditorScene sc = new EditorScene();

    //JAXB contexts
    private JAXBContext jaxbArchitechtureContext;
    private JAXBContext jaxbCatalogContext;
    private Architecture document;
    private Catalog catalog;
    private Node paletteNodesRoot;
    private JComponent sceneView;
    private transient MultiViewElementCallback callback;

    //Lookup setup
    private final InstanceContent ic = new InstanceContent();
    private final Lookup dynamicLookup = new AbstractLookup(ic);
    private final UndoRedo undoRedo = sc.getUndoRedo();
    private final ChangeListener cl = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            sc.validate();
            sceneView.repaint();
            if (getLookup().lookup(MySavable.class) == null) {
                ic.add(new MySavable());
            }
        }
    };

    public HFDVisualElement(Lookup lkp) {
        obj = lkp.lookup(HFDDataObject.class);
        assert obj != null;
        this.setDoubleBuffered(true);
        initComponents();
        readDocument();
        if (document != null) {
            readCatalog();
        }
        initPalette();
        undoRedo.addChangeListener(cl);
        initEngineComponents();

    }

    //Initializes the document by reading the document file
    private void readDocument() {
        Unmarshaller unmarshaller;
        FileObject file = obj.getPrimaryFile();
        InputStream is = null;
        try {
            is = obj.getPrimaryEntry().getFile().getInputStream();
            jaxbArchitechtureContext = JAXBContext.newInstance(Bundle.DOCUMENT_CONTEXT());
            unmarshaller = jaxbArchitechtureContext.createUnmarshaller();
            document = (Architecture) unmarshaller.unmarshal(is);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    //Initializes the catalog by reading the catalog file
    private void readCatalog() {
        Unmarshaller unmarshaller;
        Set<Entry> secondary = obj.secondaryEntries();
        if (!secondary.isEmpty()) {
            assert secondary.size() == 1;
            for (Entry e : secondary) {
                InputStream is = null;
                try {
                    is = e.getFile().getInputStream();
                    jaxbCatalogContext = JAXBContext.newInstance(Bundle.CATALOG_CONTEXT());
                    unmarshaller = jaxbCatalogContext.createUnmarshaller();
                    catalog = (Catalog) unmarshaller.unmarshal(e.getFile().getInputStream());
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

            }
        }
    }

    private void initEngineComponents() {
        sceneView = sc.createView();
        jScrollPane1.setViewportView(sceneView);
    }

    @Override
    public String getName() {
        return "HFDVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setPreferredSize(new java.awt.Dimension(800, 600));

        jScrollPane1.setName("scrollpane"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return callback.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(new Lookup[]{
            dynamicLookup,
            sc.getLookup(),});
    }

    @Override
    public void componentOpened() {
        setDocumentOnScene();
    }

    @Override
    public void componentClosed() {
        MySavable s = dynamicLookup.lookup(MySavable.class);
        if (s != null) {
            ic.remove(s);
            s.disable();
        }
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        sc.setCallback(callback);
    }

    @Override
    public CloseOperationState canCloseElement() {
        if (dynamicLookup.lookup(MySavable.class) != null) {
            return MultiViewFactory.createUnsafeCloseState("", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dynamicLookup.lookup(Savable.class).save();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }, null);
        } else {
            return CloseOperationState.STATE_OK;
        }
    }

    private void initPalette() {
        paletteNodesRoot = new AbstractNode(Children.create(new SortNodeFactory(catalog), true));
        PaletteController palette = PaletteFactory.createPalette(paletteNodesRoot, new PaletteActions() {

            @Override
            public Action[] getImportActions() {
                return null;
            }

            @Override
            public Action[] getCustomPaletteActions() {
                return null;
            }

            @Override
            public Action[] getCustomCategoryActions(Lookup lkp) {
                return null;
            }

            @Override
            public Action[] getCustomItemActions(Lookup lkp) {
                return null;
            }

            @Override
            public Action getPreferredAction(Lookup lkp) {
                return null;
            }
        });
        ic.add(palette);
    }

    public void setDocumentOnScene() {
        List subArchs = document.getSubarchitecture();
        List components = document.getComponent();
        List connections = document.getConnection();
        
        for (Object s : subArchs) {
            sc.myAddNode((SubArchitecture) s);
        }
        for (Object c : components) {
            sc.myAddNode((Component) c);
        }
        for (Object cn : connections) {
            sc.myAddEdge((Connection) cn);
        }
        sc.ready();
    }

    private class MySavable extends AbstractSavable {

        MySavable() {
            super();
            register();
        }

        @Override
        protected String findDisplayName() {
            return obj.getName() +"."+ obj.getPrimaryFile().getExt();
        }

        @Override
        protected void handleSave() throws IOException {
            Set<?> elements = sc.getObjects();
            Set<SubArchitecture> subArchs = new HashSet<SubArchitecture>();
            Set<Component> components = new HashSet<Component>();
            Set<Connection> connections = new HashSet<Connection>();
            for (Object e : elements) {
                if (e instanceof SubArchitecture) {
                    subArchs.add((SubArchitecture) e);
                    continue;
                }
                if (e instanceof Component) {
                    components.add((Component) e);
                    continue;
                }
                if (e instanceof Connection) {
                    connections.add((Connection) e);
                }
            }
            document.getComponent().clear();
            document.getSubarchitecture().clear();
            document.getConnection().clear();
            
            document.getComponent().addAll(components);
            document.getSubarchitecture().addAll(subArchs);
            document.getConnection().addAll(connections);

            FileObject file = obj.getPrimaryFile();
            FileLock lock = null;
            OutputStream os = null;
            try {
                lock = file.lock();
                os = obj.getPrimaryFile().getOutputStream(lock);
                Marshaller m = jaxbArchitechtureContext.createMarshaller();
                m.marshal(document, os);
                IOProvider.getDefault().getStdOut().println("Salvou!");
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                if (os != null) {
                    os.close();
                }
                if (lock != null) {
                    lock.releaseLock();
                }
            }
            HFDVisualElement.this.ic.remove(this);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MySavable) {
                return ((MySavable) o).mve() == this.mve();
            }
            return false;
        }

        private HFDVisualElement mve() {
            return HFDVisualElement.this;
        }

        @Override
        public int hashCode() {
            return mve().hashCode();
        }

        private void disable() {
            unregister();
        }
    }
}
