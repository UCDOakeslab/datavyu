package org.openshapa.views;

import org.openshapa.OpenSHAPA;
import org.openshapa.db.DBIndex;
import org.openshapa.db.Database;
import org.openshapa.db.FloatFormalArg;
import org.openshapa.db.FormalArgument;
import org.openshapa.db.IntFormalArg;
import org.openshapa.db.LogicErrorException;
import org.openshapa.db.MatrixVocabElement;
import org.openshapa.db.MatrixVocabElement.MatrixType;
import org.openshapa.db.NominalFormalArg;
import org.openshapa.db.PredicateVocabElement;
import org.openshapa.db.QuoteStringFormalArg;
import org.openshapa.db.SystemErrorException;
import org.openshapa.db.UnTypedFormalArg;
import org.openshapa.db.VocabElement;
import org.openshapa.views.discrete.datavalues.vocabelements.FormalArgumentV;
import org.openshapa.views.discrete.datavalues.vocabelements.MatrixVEV;
import org.openshapa.views.discrete.datavalues.vocabelements.PredicateVEV;
import org.openshapa.views.discrete.datavalues.vocabelements.VocabElementV;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author cfreeman
 */
public final class VocabEditorV extends OpenSHAPADialog {

    /** The database that this vocab editor is manipulating. */
    private Database db;

    /** The logger for the vocab editor. */
    private static Logger logger = Logger.getLogger(VocabEditorV.class);

    /** The currently selected vocab element. */
    private VocabElementV selectedVocabElement;

    /** The currently selected formal argument. */
    private FormalArgumentV selectedArgument;

    /** Index of the currently selected formal argument within the element. */
    private int selectedArgumentI;

    /** The collection of vocab element views in the current vocab listing. */
    private Vector<VocabElementV> veViews;

    /** Collection of vocab element views that are to be deleted completely. */
    private Vector<VocabElementV> veViewsToDeleteCompletely;

    /** Vertical frame for holding the current listing of Vocab elements. */
    private Box verticalFrame;

    /**
     * Constructor.
     *
     * @param parent The parent frame for the vocab editor.
     * @param modal Is this dialog to be modal or not?
     */
    public VocabEditorV(final Frame parent,
                        final boolean modal) {
        super(parent, modal);

        db = OpenSHAPA.getDatabase();
        initComponents();
        setName(this.getClass().getSimpleName());
        selectedVocabElement = null;
        selectedArgument = null;
        selectedArgumentI = -1;

        // Populate current vocab list with vocab data from the database.
        veViews = new Vector<VocabElementV>();
        veViewsToDeleteCompletely = new Vector<VocabElementV>();
        verticalFrame = Box.createVerticalBox();
        try {
            Vector<PredicateVocabElement> predVEs = db.getPredVEs();
            for (PredicateVocabElement pve : predVEs) {
                VocabElementV predicateV = new PredicateVEV(pve, this);
                verticalFrame.add(predicateV);
                veViews.add(predicateV);
            }

            Vector<MatrixVocabElement> matVEs = db.getMatrixVEs();
            for (MatrixVocabElement mve : matVEs) {
                VocabElementV matrixV = new MatrixVEV(mve, this);
                verticalFrame.add(matrixV);
                veViews.add(matrixV);
            }

        } catch (SystemErrorException e) {
            logger.error("Unable to populate current vocab list", e);
        }

        // Add a pad cell to fill out the bottom of the vertical frame.
        verticalFrame.add(new JPanel());
        jPanel1.add(verticalFrame, BorderLayout.NORTH);
        updateDialogState();
        this.getRootPane().setDefaultButton(okButton);
    }

    /**
     * The action to invoke when the user clicks on the add predicate button.
     */
    @Action
    public void addPredicate() {
        try {
            PredicateVocabElement pve = new PredicateVocabElement(db,
                                                                  "predicate");
            PredicateVEV pvev = new PredicateVEV(pve, this);
            pvev.setHasChanged(true);
            verticalFrame.add(pvev, (verticalFrame.getComponentCount() - 1));
            verticalFrame.validate();
            veViews.add(pvev);
            pvev.getNameComponent().requestFocus();

        } catch (SystemErrorException e) {
            logger.error("Unable to create predicate vocab element", e);
        }

        updateDialogState();
    }

    /**
     * The action to invoke when the user clicks on the add matrix button.
     */
    @Action
    public void addMatrix() {
        try {
            MatrixVocabElement mve = new MatrixVocabElement(db, "matrix");
            mve.setType(MatrixType.MATRIX);
            MatrixVEV mvev = new MatrixVEV(mve, this);
            mvev.setHasChanged(true);
            verticalFrame.add(mvev, (verticalFrame.getComponentCount() - 1));
            verticalFrame.validate();
            veViews.add(mvev);
            mvev.getNameComponent().requestFocus();

        } catch (SystemErrorException e) {
            logger.error("Unable to create predicate vocab element", e);
        }

        updateDialogState();
    }

    /**
     * The action to invoke when the user clicks on the move arg left button.
     */
    @Action
    public void moveArgumentLeft() {
        try {
            VocabElement ve = selectedVocabElement.getModel();
            ve.deleteFormalArg(selectedArgumentI);
            ve.insertFormalArg(selectedArgument.getModel(),
                               (selectedArgumentI - 1));
            selectedVocabElement.setHasChanged(true);
            selectedVocabElement.rebuildContents();

            updateDialogState();
        } catch (SystemErrorException e) {
            logger.error("Unable to move formal argument left", e);
        }
    }

    /**
     * The action to invoke when the user clicks on the move arg right button.
     */
    @Action
    public void moveArgumentRight() {
        try {
            VocabElement ve = selectedVocabElement.getModel();
            ve.deleteFormalArg(selectedArgumentI);
            ve.insertFormalArg(selectedArgument.getModel(),
                               (selectedArgumentI + 1));
            selectedVocabElement.setHasChanged(true);
            selectedVocabElement.rebuildContents();
        } catch (SystemErrorException e) {
            logger.error("Unable to move formal argument right", e);
        }
    }

    /**
     * The action to invoke when the user clicks on the add argument button.
     */
    @Action
    public void addArgument() {
        try {
            String type = (String) this.argTypeComboBox.getSelectedItem();
            FormalArgument fa;

            if (type.equals("Untyped")) {
                fa = new UnTypedFormalArg(db, "<untyped>");
            } else if (type.equals("Text")) {
                fa = new QuoteStringFormalArg(db, "<text>");
            } else if (type.equals("Nominal")) {
                fa = new NominalFormalArg(db, "<nominal>");
            } else if (type.equals("Integer")) {
                fa = new IntFormalArg(db, "<integer>");
            } else {
                fa = new FloatFormalArg(db, "<float>");
            }

            VocabElement ve = selectedVocabElement.getModel();
            ve.appendFormalArg(fa);

            // Store the selectedVocabElement in a temp variable - rebuilding
            // contents may alter the currently selected vocab element.
            VocabElementV temp = selectedVocabElement;
            temp.setHasChanged(true);
            temp.rebuildContents();

            // Select the contents of the newly created formal argument.
            FormalArgumentV faV = temp.getArgumentView(fa);
            faV.requestFocus();

            updateDialogState();

        } catch (SystemErrorException e) {
            logger.error("Unable to create formal argument.", e);
        }
    }

    /**
     * The action to invoke when the user toggles the varying argument state.
     */
    @Action
    public void setVaryingArgs() {
        if (selectedVocabElement != null) {
            try {
                selectedVocabElement.getModel()
                                    .setVarLen(varyArgCheckBox.isSelected());
                selectedVocabElement.rebuildContents();
            } catch (SystemErrorException e) {
                logger.error("Unable to set varying arguments.", e);
            }
        }
    }

    /**
     * The action to invoke when the user presses the delete button.
     */
    @Action
    public void delete() {
        // User has vocab element selected - mark it for deletion.
        if (selectedVocabElement != null && selectedArgument == null) {
            veViewsToDeleteCompletely.add(selectedVocabElement);
            selectedVocabElement.setDeleted(true);

        // User has argument selected - delete it from the vocab element.
        } else if (selectedArgument != null) {
            VocabElement ve = selectedVocabElement.getModel();
            try {
                ve.deleteFormalArg(selectedArgumentI);
                selectedVocabElement.setHasChanged(true);
                selectedVocabElement.rebuildContents();
            } catch (SystemErrorException e) {
                logger.error("Unable to selected argument", e);
            }
        }

        updateDialogState();
    }

    /**
     * The action to invoke when the user presses the revert button.
     */
    @Action
    public void revertChanges() {
        int tableSize = veViews.size();
        int curPos = 0;

        for (VocabElementV view : veViewsToDeleteCompletely) {
            view.setDeleted(false);
        }

        for (int i = 0; i < tableSize; i++) {
            if (veViews.get(curPos).hasChanged()) {
                verticalFrame.remove(veViews.get(curPos));
                veViews.remove(curPos);
            } else {
                curPos++;
            }
        }

        updateDialogState();
    }

    /**
     * The action to invoke when the user presses the apply button.
     */
    @Action
    public void applyChanges() {

        try {
            for (VocabElementV view : veViewsToDeleteCompletely) {
                db.removeVE(view.getModel().getID());
                verticalFrame.remove(view);
                veViews.remove(view);
            }

            for (VocabElementV vev : veViews) {
                if (vev.hasChanged()) {

                    VocabElement ve = vev.getModel();
                    if (ve.getID() == DBIndex.INVALID_ID) {
                        long id = db.addVocabElement(ve);
                        vev.setModel(db.getVocabElement(id));

                    } else {
                        db.replaceVocabElement(ve);
                    }
                    vev.setHasChanged(false);
                }
            }

            veViewsToDeleteCompletely.clear();
            updateDialogState();

        } catch (SystemErrorException e) {
            logger.error("Unable to apply vocab changes", e);
        } catch (LogicErrorException le) {
            OpenSHAPA.getApplication().showWarningDialog(le);
        }
    }

    /**
     * The action to invoke when the user presses the OK button.
     */
    @Action
    public void ok() {
        applyChanges();
        try {
            this.dispose();
            this.finalize();
        } catch (Throwable e) {
            logger.error("Unable to destroy vocab editor view.", e);
        }
    }

    /**
     * The action to invoke when the user presses the cancel button.
     */
    @Action
    public void closeWindow() {
        try {
            this.dispose();
            this.finalize();
        } catch (Throwable e) {
            logger.error("Unable to destroy vocab editor view.", e);
        }
    }

    /**
     * Method to update the visual state of the dialog to match the underlying
     * model.
     */
    public void updateDialogState() {
        ResourceMap rMap = Application.getInstance(OpenSHAPA.class)
                                      .getContext()
                                      .getResourceMap(VocabEditorV.class);

        boolean containsC = false;
        selectedVocabElement = null;
        selectedArgument = null;
        for (VocabElementV vev : veViews) {
            // A vocab element has focus - enable certain things.
            if (vev.hasFocus()) {
                selectedVocabElement = vev;
                selectedArgument = vev.getArgWithFocus();
            }

            // A vocab element contains a change - enable certain things.
            if (vev.hasChanged() || vev.isDeletable()) {
                containsC = true;
            }
        }

        if (containsC) {
            closeButton.setText(rMap.getString("closeButton.cancelText"));
            closeButton.setToolTipText(rMap.getString("closeButton.cancelTip"));

            revertButton.setEnabled(true);
            applyButton.setEnabled(true);
            okButton.setEnabled(true);
        } else {
            closeButton.setText(rMap.getString("closeButton.closeText"));
            closeButton.setToolTipText(rMap.getString("closeButton.closeTip"));

            revertButton.setEnabled(false);
            applyButton.setEnabled(false);
            okButton.setEnabled(false);
        }

        // If we have a selected vocab element - we can enable additional
        // functionality.
        if (selectedVocabElement != null) {
            addArgButton.setEnabled(true);
            argTypeComboBox.setEnabled(true);
            varyArgCheckBox.setEnabled(true);
            deleteButton.setEnabled(true);
            varyArgCheckBox.setSelected(selectedVocabElement.getModel()
                                                            .getVarLen());
        } else {
            addArgButton.setEnabled(false);
            argTypeComboBox.setEnabled(false);
            varyArgCheckBox.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        if (selectedArgument != null) {
            FormalArgument fa = selectedArgument.getModel();

            if (fa.getClass().equals(IntFormalArg.class)) {
                this.argTypeComboBox.setSelectedItem("Integer");
            } else if (fa.getClass().equals(FloatFormalArg.class)) {
                this.argTypeComboBox.setSelectedItem("Float");
            } else if (fa.getClass().equals(NominalFormalArg.class)) {
                this.argTypeComboBox.setSelectedItem("Nominal");
            } else if (fa.getClass().equals(QuoteStringFormalArg.class)) {
                this.argTypeComboBox.setSelectedItem("Text");
            } else {
                this.argTypeComboBox.setSelectedItem("Untyped");
            }

            // W00t - argument is selected - populate the index so that the user
            // can shift the argument around.
            selectedArgumentI = selectedVocabElement.getModel()
                               .findFormalArgIndex(selectedArgument.getModel());

            if (selectedArgumentI > 0) {
                this.moveArgLeftButton.setEnabled(true);
            } else {
                this.moveArgLeftButton.setEnabled(false);
            }

            try {
                if (selectedArgumentI < (selectedVocabElement.getModel()
                                                     .getNumFormalArgs() - 1)) {
                    this.moveArgRightButton.setEnabled(true);
                } else {
                    this.moveArgRightButton.setEnabled(false);
                }
            } catch (SystemErrorException e) {
                logger.error("Unable to get num of formal args", e);
            }
        } else {
            this.moveArgLeftButton.setEnabled(false);
            this.moveArgRightButton.setEnabled(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        addPredicateButton = new javax.swing.JButton();
        addMatrixButton = new javax.swing.JButton();
        moveArgLeftButton = new javax.swing.JButton();
        moveArgRightButton = new javax.swing.JButton();
        addArgButton = new javax.swing.JButton();
        argTypeComboBox = new javax.swing.JComboBox();
        varyArgCheckBox = new javax.swing.JCheckBox();
        deleteButton = new javax.swing.JButton();
        currentVocabList = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        revertButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/openshapa/views/resources/VocabEditorV"); // NOI18N
        setTitle(bundle.getString("window.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.openshapa.OpenSHAPA.class).getContext().getActionMap(VocabEditorV.class, this);
        addPredicateButton.setAction(actionMap.get("addPredicate")); // NOI18N
        addPredicateButton.setText(bundle.getString("addPredicateButton.text")); // NOI18N
        addPredicateButton.setToolTipText(bundle.getString("addPredicateButton.tip")); // NOI18N
        addPredicateButton.setName("addPredicateButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        getContentPane().add(addPredicateButton, gridBagConstraints);

        addMatrixButton.setAction(actionMap.get("addMatrix")); // NOI18N
        addMatrixButton.setText(bundle.getString("addMatrixButton.text")); // NOI18N
        addMatrixButton.setToolTipText(bundle.getString("addMatrixButton.tip")); // NOI18N
        addMatrixButton.setName("addMatrixButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(addMatrixButton, gridBagConstraints);

        moveArgLeftButton.setAction(actionMap.get("moveArgumentLeft")); // NOI18N
        moveArgLeftButton.setText(bundle.getString("moveArgLeftButton.text")); // NOI18N
        moveArgLeftButton.setToolTipText(bundle.getString("moveArgLeftButton.tip")); // NOI18N
        moveArgLeftButton.setName("moveArgLeftButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(moveArgLeftButton, gridBagConstraints);

        moveArgRightButton.setAction(actionMap.get("moveArgumentRight")); // NOI18N
        moveArgRightButton.setText(bundle.getString("moveArgRightButton.text")); // NOI18N
        moveArgRightButton.setToolTipText(bundle.getString("moveArgRightButton.tip")); // NOI18N
        moveArgRightButton.setName("moveArgRightButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        getContentPane().add(moveArgRightButton, gridBagConstraints);

        addArgButton.setAction(actionMap.get("addArgument")); // NOI18N
        addArgButton.setText(bundle.getString("addArgButton.text")); // NOI18N
        addArgButton.setToolTipText(bundle.getString("addArgButton.tip")); // NOI18N
        addArgButton.setName("addArgButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(addArgButton, gridBagConstraints);

        argTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Untyped", "Text", "Nominal", "Integer", "Float" }));
        argTypeComboBox.setToolTipText(bundle.getString("argTypeComboBox.tip")); // NOI18N
        argTypeComboBox.setEnabled(false);
        argTypeComboBox.setName("argTypeComboBox"); // NOI18N
        argTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                argTypeComboBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(argTypeComboBox, gridBagConstraints);

        varyArgCheckBox.setAction(actionMap.get("setVaryingArgs")); // NOI18N
        varyArgCheckBox.setText(bundle.getString("varyArgCheckBox.text")); // NOI18N
        varyArgCheckBox.setToolTipText(bundle.getString("varyArgCheckBox.tip")); // NOI18N
        varyArgCheckBox.setName("varyArgCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(varyArgCheckBox, gridBagConstraints);

        deleteButton.setAction(actionMap.get("delete")); // NOI18N
        deleteButton.setText(bundle.getString("deleteButton.text")); // NOI18N
        deleteButton.setToolTipText(bundle.getString("deleteButton.tip")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        getContentPane().add(deleteButton, gridBagConstraints);

        currentVocabList.setName("currentVocabList"); // NOI18N
        currentVocabList.setPreferredSize(new java.awt.Dimension(4, 400));

        jPanel1.setToolTipText(bundle.getString("vocablist.tooltip")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(1, 1));
        currentVocabList.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(currentVocabList, gridBagConstraints);

        revertButton.setAction(actionMap.get("revertChanges")); // NOI18N
        revertButton.setText(bundle.getString("revertButton.text")); // NOI18N
        revertButton.setToolTipText(bundle.getString("revertButton.tip")); // NOI18N
        revertButton.setName("revertButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(revertButton, gridBagConstraints);

        applyButton.setAction(actionMap.get("applyChanges")); // NOI18N
        applyButton.setText(bundle.getString("applyButton.text")); // NOI18N
        applyButton.setToolTipText(bundle.getString("applyButton.tip")); // NOI18N
        applyButton.setName("applyButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(applyButton, gridBagConstraints);

        okButton.setAction(actionMap.get("ok")); // NOI18N
        okButton.setText(bundle.getString("okButton.text")); // NOI18N
        okButton.setToolTipText(bundle.getString("okButton.tip")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(okButton, gridBagConstraints);

        closeButton.setAction(actionMap.get("closeWindow")); // NOI18N
        closeButton.setText(bundle.getString("closeButton.closeText")); // NOI18N
        closeButton.setToolTipText(bundle.getString("closeButton.closeTip")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(closeButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * The action to invoke when the user changes the formal argument dropdown.
     *
     * @param evt The event that triggered this action.
     */
    private void argTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_argTypeComboBoxItemStateChanged
        if (selectedVocabElement != null
            && selectedArgument != null
            && evt.getStateChange() == ItemEvent.SELECTED) {

            // Need to change the type of the selected argument.
            FormalArgument oldArg = selectedArgument.getModel();
            FormalArgument newArg = null;

            try {
                if (evt.getItem().equals("Untyped")) {
                    newArg = new UnTypedFormalArg(db, oldArg.getFargName());
                } else if (evt.getItem().equals("Text")) {
                    newArg = new QuoteStringFormalArg(db, oldArg.getFargName());
                } else if (evt.getItem().equals("Nominal")) {
                    newArg = new NominalFormalArg(db, oldArg.getFargName());
                } else if (evt.getItem().equals("Integer")) {
                    newArg = new IntFormalArg(db, oldArg.getFargName());
                } else {
                    newArg = new FloatFormalArg(db, oldArg.getFargName());
                }

                //VocabElement ve = selectedVocabElement.getVocabElement();
                selectedVocabElement.getModel()
                                    .replaceFormalArg(newArg,
                                                  selectedArgument.getArgPos());
                selectedVocabElement.setHasChanged(true);

                // Store the selectedVocabElement in a temp variable -
                // rebuilding contents may alter the currently selected vocab
                // element.
                VocabElementV temp = selectedVocabElement;
                temp.rebuildContents();

                // Select the contents of the newly created formal argument.
                FormalArgumentV faV = temp.getArgumentView(newArg);
                faV.requestFocus();

                updateDialogState();

            } catch (SystemErrorException se) {
                logger.error("Unable to alter selected argument.", se);
            }
        }
    }//GEN-LAST:event_argTypeComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addArgButton;
    private javax.swing.JButton addMatrixButton;
    private javax.swing.JButton addPredicateButton;
    private javax.swing.JButton applyButton;
    private javax.swing.JComboBox argTypeComboBox;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane currentVocabList;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton moveArgLeftButton;
    private javax.swing.JButton moveArgRightButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton revertButton;
    private javax.swing.JCheckBox varyArgCheckBox;
    // End of variables declaration//GEN-END:variables
}