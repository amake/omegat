/**************************************************************************
 OmegaT - Java based Computer Assisted Translation (CAT) tool
 Copyright (C) 2002-2005  Keith Godfrey et al
                          keithgodfrey@users.sourceforge.net
                          907.223.2039

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

package org.omegat.gui.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.omegat.core.StringData;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.Token;

/* Previously this file contained a reference to Raymond Martin authorship by mistake.
 * The code he provided was not used because of low quality. */

/**
 * This is a combined Match + Glossary pane, that displays
 * fuzzy matches and glossary entries.
 * <p>
 * Separated into a component for future single-window design.
 *
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 */
public class MatchGlossaryPane extends javax.swing.JPanel implements java.beans.PropertyChangeListener
{
    
    /** main window holder */
    MainWindow mainwindow;
    
    /** Sets the main window */
    public void setMainWindow(MainWindow mw)
    {
        mainwindow = mw;
    }
    
    /** Creates new form MatchGlossaryPane */
    public MatchGlossaryPane()
    {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        matchSplitPane = new javax.swing.JSplitPane();
        upPanel = new javax.swing.JPanel();
        fuzzyMatchesLabel = new javax.swing.JLabel();
        matchScrollPane = new javax.swing.JScrollPane();
        m_matchPane = new javax.swing.JTextPane();
        downPanel = new javax.swing.JPanel();
        glossaryLabel = new javax.swing.JLabel();
        glossaryScrollPane = new javax.swing.JScrollPane();
        m_glosPane = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        matchSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        matchSplitPane.setResizeWeight(0.5);
        matchSplitPane.setContinuousLayout(true);
        matchSplitPane.setOneTouchExpandable(true);
        matchSplitPane.addPropertyChangeListener(this);

        upPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(fuzzyMatchesLabel, OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_Fuzzy_Matches"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        upPanel.add(fuzzyMatchesLabel, gridBagConstraints);

        m_matchPane.setEditable(false);
        m_matchPane.setMinimumSize(new java.awt.Dimension(100, 50));
        matchScrollPane.setViewportView(m_matchPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        upPanel.add(matchScrollPane, gridBagConstraints);

        matchSplitPane.setLeftComponent(upPanel);

        downPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(glossaryLabel, OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_Glossary"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        downPanel.add(glossaryLabel, gridBagConstraints);

        m_glosPane.setEditable(false);
        m_glosPane.setMinimumSize(new java.awt.Dimension(100, 50));
        glossaryScrollPane.setViewportView(m_glosPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        downPanel.add(glossaryScrollPane, gridBagConstraints);

        matchSplitPane.setRightComponent(downPanel);

        add(matchSplitPane, java.awt.BorderLayout.CENTER);

    }

    // Code for dispatching events from components to event handlers.

    public void propertyChange(java.beans.PropertyChangeEvent evt)
    {
        if (evt.getSource() == matchSplitPane)
        {
            MatchGlossaryPane.this.matchSplitPanePropertyChange(evt);
        }
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void matchSplitPanePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_matchSplitPanePropertyChange
    {//GEN-HEADEREND:event_matchSplitPanePropertyChange
        if( mainwindow!=null && "dividerLocation".equals(evt.getPropertyName()) )  // NOI18N
            mainwindow.storeScreenLayout();
    }//GEN-LAST:event_matchSplitPanePropertyChange
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel downPanel;
    private javax.swing.JLabel fuzzyMatchesLabel;
    private javax.swing.JLabel glossaryLabel;
    private javax.swing.JScrollPane glossaryScrollPane;
    private javax.swing.JTextPane m_glosPane;
    private javax.swing.JTextPane m_matchPane;
    private javax.swing.JScrollPane matchScrollPane;
    private javax.swing.JSplitPane matchSplitPane;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * Sets the text of the glossary window to the found glossary entries
     */
    public void updateGlossaryText()
    {
        m_glosPane.setText(m_glosDisplay);
        m_glosDisplay = "";                                                     // NOI18N
    }
    
    /**
     * Format the currently selected Near String (fuzzy match)
     * according to the tokens and their attributes.
     *
     * @param tokenList - the list of tokens to highlight
     * @param attrList - the attributes to color tokens accordingly
     */
    public void formatNearText(List tokenList, byte[] attrList)
    {
        if (attrList==null)
            return;
        
        int start;
        int end;
        javax.swing.JTextPane pane = m_matchPane;
        
        // reset color of text to default value
        int numTokens = tokenList.size();
        for (int i=0; i<numTokens; i++)
        {
            start = m_hiliteStart + ((Token) tokenList.get(i)).getOffset() + 4;
            end = m_hiliteStart + ((Token) tokenList.get(i)).getOffset() + 4 +
                    ((Token) tokenList.get(i)).getLength();
            
            pane.select(start, end);
            SimpleAttributeSet mattr = new SimpleAttributeSet();
            if ((attrList[i] & StringData.UNIQ) != 0)
                StyleConstants.setForeground(mattr, Color.blue);
            else if ((attrList[i] & StringData.PAIR) != 0)
                StyleConstants.setForeground(mattr, Color.green);
            pane.setCharacterAttributes(mattr, false);
        }
        pane.select(0, 0);
        SimpleAttributeSet mattr = new SimpleAttributeSet();
        pane.setCharacterAttributes(mattr, false);
    }
    
    /**
     * Sets the text of match window to the list of found matches (near strings),
     * and select the currently selected match in bold.
     */
    public void updateMatchText()
    {
        // Cancelling all the attributes
        m_matchPane.setCharacterAttributes(new SimpleAttributeSet(), true);
        
        m_matchPane.setText(m_matchDisplay);
        
        if ( m_hiliteStart >= 0 && m_matchDisplay.length()>0 )
        {
            m_matchPane.select(m_hiliteStart, m_hiliteEnd);
            MutableAttributeSet mattr;
            mattr = new SimpleAttributeSet();
            StyleConstants.setBold(mattr, true);
            m_matchPane.setCharacterAttributes(mattr, false);
            
            m_matchPane.setCaretPosition(m_hiliteStart);
        }
        
        m_matchDisplay = "";                                                    // NOI18N
        m_matchCount = 0;
    }
    
    public void reset()
    {
        m_matchDisplay = "";                                                    // NOI18N
        m_glosDisplay = "";                                                     // NOI18N
        m_matchCount = 0;
        m_matchPane.setText("");                                                // NOI18N
        m_glosPane.setText("");                                                 // NOI18N
    }
    
    public void setFont(Font f)
    {
        super.setFont(f);
        if( m_matchPane!=null )
        {
            m_matchPane.setFont(f);
            m_glosPane.setFont(f);
        }
    }
    
    public void addGlosTerm(String src, String loc, String comments)
    {
        String glos = "'" + src + "'  =  '" + loc + "'\n";                      // NOI18N
        if (comments.length() > 0)
            glos += comments + "\n\n";                                          // NOI18N
        else
            glos += "\n";                                                       // NOI18N
        m_glosDisplay += glos;
    }
    
    // returns offset in display of the start of this term
    public int addMatchTerm(String src, String loc, int score, String proj)
    {
        String entry = ++m_matchCount + ")  " + src + "\n" + loc + "\n< " +     // NOI18N
                score + "% " + proj + " >\n\n";                                 // NOI18N
        int size = m_matchDisplay.length();
        m_matchDisplay += entry;
        return size;
    }
    
    public void hiliteRange(int start, int end)
    {
        int len = m_matchDisplay.length();
        if (start < 0 || start > len)
        {
            m_hiliteStart = -1;
            m_hiliteEnd = -1;
            return;
        }
        
        if (end < 0)
            end = len;
        
        m_hiliteStart = start;
        m_hiliteEnd = end;
    }
    
    public void storeScreenLayout()
    {
        Preferences.setPreference(Preferences.MATCHWINDOW_WIDTH, getWidth());
        Preferences.setPreference(Preferences.MATCHWINDOW_HEIGHT, getHeight());
        Preferences.setPreference(Preferences.MATCHWINDOW_X, getX());
        Preferences.setPreference(Preferences.MATCHWINDOW_Y, getY());
    }
    
    /**
     * Initializes the screen layout.
     * <p>
     * KBG - assume screen size is 800x600 if width less than 900, and
     * 1024x768 if larger.  assume task bar at bottom of screen.
     * if screen size saved, recover that and use instead
     * (18may04).
     */
    private void initScreenLayout()
    {
        boolean badSize = false;
        try
        {
            String dw = Preferences.getPreference(Preferences.MATCHWINDOW_WIDTH);
            String dh = Preferences.getPreference(Preferences.MATCHWINDOW_HEIGHT);
            String dx = Preferences.getPreference(Preferences.MATCHWINDOW_X);
            String dy = Preferences.getPreference(Preferences.MATCHWINDOW_Y);
            int x = Integer.parseInt(dx);
            int y = Integer.parseInt(dy);
            int w = Integer.parseInt(dw);
            int h = Integer.parseInt(dh);
            setSize(w, h);
            setLocation(x, y);
        }
        catch(Exception e)
        {
            // size info missing - put window in default position
            GraphicsEnvironment env =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle scrSize = env.getMaximumWindowBounds();
            if (scrSize.width < 900)
            {
                // assume 800x600
                setSize(200, 536);
                setLocation(590, 0);
            }
            else
            {
                // assume 1024x768 or larger
                setSize(320, 700);
                setLocation(680, 0);
            }
        }
    }
    
    private String m_matchDisplay = "";                                         // NOI18N
    private String m_glosDisplay = "";                                          // NOI18N
    private int    m_matchCount;
    private int    m_hiliteStart = -1;
    private int    m_hiliteEnd = -1;
    
    /** Returns divider location */
    public int getDividerLocation()
    {
        return matchSplitPane.getDividerLocation();
    }
    /** Sets divider location */
    public void setDividerLocation(int value)
    {
        matchSplitPane.setDividerLocation(value);
    }
}
