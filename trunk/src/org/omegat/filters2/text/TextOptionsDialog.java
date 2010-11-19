/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2009 Didier Briel
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
**************************************************************************/

package org.omegat.filters2.text;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.omegat.util.OStrings;

/**
 * Modal dialog to edit Text filter options.
 *
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 */
public class TextOptionsDialog extends javax.swing.JDialog
{
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    private Map<String,String> options;
    
    /**
     * Creates new form TextOptionsDialog 
     */
    public TextOptionsDialog(Dialog parent, Map<String,String> options)
    {
        super(parent, true);
        this.options = new TreeMap<String, String>(options);
        initComponents();

        String segmentOn = options.get(TextFilter.OPTION_SEGMENT_ON);
        if (TextFilter.SEGMENT_BREAKS.equals(segmentOn)) {
            breaksRB.setSelected(true);
        } else if (TextFilter.SEGMENT_NEVER.equals(segmentOn)) {
            neverRB.setSelected(true);
        } else {
            emptyLinesRB.setSelected(true);
        }
        
        //  Handle escape key to close the window
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(escape, "ESCAPE");                                                  
        getRootPane().getActionMap().put("ESCAPE", escapeAction);               
    }
    
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus()
    {
        return returnStatus;
    }
    
    /** Returns updated options. */
    public Map<String,String> getOptions()
    {
        return options;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        breaksRB = new javax.swing.JRadioButton();
        emptyLinesRB = new javax.swing.JRadioButton();
        neverRB = new javax.swing.JRadioButton();

        setTitle(OStrings.getString("TEXTFILTER_OPTIONS_DIALOG_TITLE"));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK"));
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(okButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL"));
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.GridLayout(0, 1, 0, 3));

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 3, 3, 3)));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, OStrings.getString("TEXTFILTER_OPTIONS_DIALOG_DESCRIPTION"));
        jPanel1.add(jLabel1);

        buttonGroup1.add(breaksRB);
        org.openide.awt.Mnemonics.setLocalizedText(breaksRB, OStrings.getString("TEXTFILTER_OPTION_SEGMENT_ON_LINE_BREAKS"));
        jPanel1.add(breaksRB);

        buttonGroup1.add(emptyLinesRB);
        emptyLinesRB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(emptyLinesRB, OStrings.getString("TEXTFILTER_OPTION_SEGMENT_ON_EMPTY_LINES"));
        jPanel1.add(emptyLinesRB);

        buttonGroup1.add(neverRB);
        org.openide.awt.Mnemonics.setLocalizedText(neverRB, OStrings.getString("TEXTFILTER_OPTION_NEVER_SEGMENT"));
        jPanel1.add(neverRB);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {
        String segmentOn;
        if (breaksRB.isSelected()) {
            segmentOn = TextFilter.SEGMENT_BREAKS;
        } else if (neverRB.isSelected()) {
            segmentOn = TextFilter.SEGMENT_NEVER;
        } else {
            segmentOn = TextFilter.SEGMENT_EMPTYLINES;
        }

        options.put(TextFilter.OPTION_SEGMENT_ON, segmentOn);

        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    
    private void doClose(int retStatus)
    {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton breaksRB;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton emptyLinesRB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton neverRB;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
    private int returnStatus = RET_CANCEL;
}
