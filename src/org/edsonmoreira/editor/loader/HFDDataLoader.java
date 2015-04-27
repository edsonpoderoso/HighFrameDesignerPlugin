/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor.loader;

import java.io.IOException;
import org.edsonmoreira.editor.HFDDataObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.FileEntry;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.NbBundle;

/**
 *
 * @author edson
 */
@NbBundle.Messages({
    "LBL_HFD_LOADER=Files of HFD"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_HFD_LOADER",
        mimeType = "text/hfd+xml",
        extension = {"hfd", "hfdcat"}
)
@DataObject.Registration(
        mimeType = "text/hfd+xml",
        //iconBase = "org/edsonmoreira/editor/loader/HFD.png",
        displayName = "#LBL_HFD_LOADER",
        position = 300
)

public class HFDDataLoader extends MultiFileLoader {

    public HFDDataLoader() {
        super("org.edsonmoreira.editor.HFDDataObject");
        this.setDisplayName("HFDLoader");
    }

    @Override
    protected FileObject findPrimaryFile(FileObject fo) {
        if (fo.getExt().equalsIgnoreCase("hfd")) {
            return fo;
        } else if (fo.getExt().equalsIgnoreCase("hfdcat")) {
            FileObject fb = FileUtil.findBrother(fo, "hfd");
            return fb;
        } else {
            return null;
        }
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject fo) throws DataObjectExistsException, IOException {
        return new HFDDataObject(fo, this);
    }

    @Override
    protected MultiDataObject.Entry createPrimaryEntry(MultiDataObject mdo, FileObject fo
    ) {
        FileEntry fe = new FileEntry(mdo, fo);
        return fe;
    }

    @Override
    protected MultiDataObject.Entry createSecondaryEntry(MultiDataObject mdo, FileObject fo) {
        FileEntry fe = new FileEntry(mdo, fo);
        return fe;
    }

    @Override
    protected String actionsContext() {
        return "Loaders/text/hfd+xml/Actions";
    }

}
