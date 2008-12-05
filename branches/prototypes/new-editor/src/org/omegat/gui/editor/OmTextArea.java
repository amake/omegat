/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2008 Alex Buloichik
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

package org.omegat.gui.editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.undo.UndoManager;

import org.omegat.core.Core;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * New implementation of EditorPane. Only mouse handling required.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
class OmTextArea extends JEditorPane {

    /** Undo Manager to store edits */
    protected final UndoManager undoManager = new UndoManager();

    protected final EditorController controller;

    public OmTextArea(EditorController controller) {
        this.controller = controller;
        setEditorKit(new OmEditorKit());

        addMouseListener(mouseListener);
    }

    /** Orders to cancel all Undoable edits. */
    public void cancelUndo() {
        undoManager.die();
    }

    /**
     * Return OmDocument instaed just a Document. If editor was not initialized
     * with OmDocument, it will contains other Document implementation. In this
     * case we don't need it.
     */
    public OmDocument getOmDocument() {
        try {
            return (OmDocument) getDocument();
        } catch (ClassCastException ex) {
            return null;
        }
    }

    protected MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                controller.goToSegmentAtLocation(getCaretPosition());
            }
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                // any spell checking to be done?
                if (createSpellCheckerPopUp(e.getPoint()))
                    return;

                // fall back to go to segment
                if (createGoToSegmentPopUp(e.getPoint()))
                    return;
            }
        }
    };

    /**
     * Redefine some keys behavior. We can't use key listeners, because we have
     * to make something AFTER standard keys processing.
     */
    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() != KeyEvent.KEY_PRESSED) {
            // key released
            super.processKeyEvent(e);
            return;
        }

        boolean processed = false;
if (e.getKeyCode()==37 || e.getKeyCode()==39) {
    System.out.println(e.getKeyCode());
    
}
        // non-standard processing
        if (isKey(e, KeyEvent.VK_TAB, 0)) {
            // press TAB when 'Use TAB to advance'
            if (controller.settings.isUseTabForAdvance()) {
                controller.nextEntry();
                processed = true;
            }
        } else if (isKey(e, KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK)) {
            // press Shift+TAB when 'Use TAB to advance'
            if (controller.settings.isUseTabForAdvance()) {
                controller.prevEntry();
                processed = true;
            }
        } else if (isKey(e, KeyEvent.VK_ENTER, 0)) {
            // press ENTER
            if (!controller.settings.isUseTabForAdvance()) {
                controller.nextEntry();
                processed = true;
            }
        } else if (isKey(e, KeyEvent.VK_ENTER, KeyEvent.CTRL_MASK)) {
            // press Ctrl+ENTER
            if (!controller.settings.isUseTabForAdvance()) {
                controller.prevEntry();
                processed = true;
            }
        } else if (isKey(e, KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK)) {
            // convert Shift+Enter event to straight enter key
            KeyEvent ke = new KeyEvent(e.getComponent(), e.getID(),
                    e.getWhen(), 0, KeyEvent.VK_ENTER, '\n');
            super.processKeyEvent(ke);
            processed = true;
        } else if (isKey(e, KeyEvent.VK_A, KeyEvent.CTRL_MASK)) {
            // handling Ctrl+A manually
            setSelectionStart(controller.getTranslationStart());
            setSelectionEnd(controller.getTranslationEnd());
            processed = true;
        } else if (isKey(e, KeyEvent.VK_O, KeyEvent.CTRL_MASK
                | KeyEvent.SHIFT_MASK)) {
            // handle Ctrl+Shift+O - toggle orientation LTR-RTL
            controller.toggleOrientation();
        }

        // leave standard processing if need
        if (processed) {
            e.consume();
        } else {
            super.processKeyEvent(e);
        }

        // some after-processing catches
        if (!processed) {
            if (e.getKeyCode() == KeyEvent.VK_HOME) {
                // press HOME
                checkAndFixCaret();
            } else if (e.getKeyCode() == KeyEvent.VK_END) {
                // press END
                checkAndFixCaret();
            }
        }
    }

    /**
     * Checks whether the selection & caret is inside editable text, and changes
     * their positions accordingly if not.
     */
    private void checkAndFixCaret() {
        // int pos = m_editor.getCaretPosition();
        int spos = getSelectionStart();
        int epos = getSelectionEnd();
        /*
         * int start = m_segmentStartOffset + m_sourceDisplayLength +
         * OConsts.segmentStartStringFull.length();
         */
        int start = getOmDocument().activeTranslationBegin.getOffset();
        // -1 for space before tag, -2 for newlines
        /*
         * int end = editor.getTextLength() - m_segmentEndInset -
         * OConsts.segmentEndStringFull.length();
         */
        int end = getOmDocument().activeTranslationEnd.getOffset();

        if (spos != epos) {
            // dealing with a selection here - make sure it's w/in bounds
            if (spos < start) {
                setSelectionStart(start);
            } else if (spos > end) {
                setSelectionStart(end);
            }
            if (epos > end) {
                setSelectionEnd(end);
            } else if (epos < start) {
                setSelectionStart(start);
            }
        } else {
            // non selected text
            if (spos < start) {
                setCaretPosition(start);
            } else if (spos > end) {
                setCaretPosition(end);
            }
        }
    }

    private static boolean isKey(KeyEvent e, int code, int modifiers) {
        return e.getKeyCode() == code && e.getModifiers() == modifiers;
    }

    /**
     * creates a popup menu for inactive segments - with an item allowing to go
     * to the given segment.
     */
    private boolean createGoToSegmentPopUp(Point point) {
        final int mousepos = this.viewToModel(point);

        if (mousepos >= controller.getTranslationStart()
                - OConsts.segmentStartStringFull.length()
                && mousepos <= controller.getTranslationEnd()
                        + OConsts.segmentStartStringFull.length())
            return false;

        JPopupMenu popup = new JPopupMenu();

        JMenuItem item = popup
                .add(OStrings.getString("MW_PROMPT_SEG_NR_TITLE"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCaretPosition(mousepos);
                controller.goToSegmentAtLocation(getCaretPosition());
            }
        });

        popup.show(this, (int) point.getX(), (int) point.getY());

        return true;
    }

    /**
     * create the spell checker popup menu - suggestions for a wrong word, add
     * and ignore. Works only for the active segment, for the translation
     * 
     * @param point
     *            : where should the popup be shown
     */
    protected boolean createSpellCheckerPopUp(final Point point) {
        if (!controller.getSettings().isAutoSpellChecking())
            return false;

        // where is the mouse
        int mousepos = viewToModel(point);

        if (mousepos < controller.getTranslationStart()
                || mousepos > controller.getTranslationEnd())
            return false;

        try {
            // find the word boundaries
            final int wordStart = Utilities.getWordStart(this, mousepos);
            final int wordEnd = Utilities.getWordEnd(this, mousepos);

            final String word = getText(wordStart, wordEnd - wordStart);

            final AbstractDocument xlDoc = (AbstractDocument) getDocument();

            if (!Core.getSpellChecker().isCorrect(word)) {
                // get the suggestions and create a menu
                List<String> suggestions = Core.getSpellChecker().suggest(word);

                // create the menu
                JPopupMenu popup = new JPopupMenu();

                // the suggestions
                for (final String replacement : suggestions) {
                    JMenuItem item = popup.add(replacement);
                    item.addActionListener(new ActionListener() {
                        // the action: replace the word with the selected
                        // suggestion
                        public void actionPerformed(ActionEvent e) {
                            try {
                                int pos = getCaretPosition();
                                xlDoc.replace(wordStart, word.length(),
                                        replacement, controller.getSettings()
                                                .getTranslatedAttributeSet());
                                setCaretPosition(pos);
                            } catch (BadLocationException exc) {
                                Log.log(exc);
                            }
                        }
                    });
                }

                // what if no action is done?
                if (suggestions.size() == 0) {
                    JMenuItem item = popup.add(OStrings
                            .getString("SC_NO_SUGGESTIONS"));
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // just hide the menu
                        }
                    });
                }

                popup.add(new JSeparator());

                // let us ignore it
                JMenuItem item = popup.add(OStrings.getString("SC_IGNORE_ALL"));
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addIgnoreWord(word, wordStart, false);
                    }
                });

                // or add it to the dictionary
                item = popup.add(OStrings.getString("SC_ADD_TO_DICTIONARY"));
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addIgnoreWord(word, wordStart, true);
                    }
                });

                popup.show(this, (int) point.getX(), (int) point.getY());
            }
        } catch (BadLocationException ex) {
            Log.log(ex);
        }
        return true;
    }

    /**
     * add a new word to the spell checker or ignore a word
     * 
     * @param word
     *            : the word in question
     * @param offset
     *            : the offset of the word in the editor
     * @param add
     *            : true for add, false for ignore
     */
    protected void addIgnoreWord(final String word, final int offset,
            final boolean add) {
        UIThreadsUtil.mustBeSwingThread();

        if (add) {
            Core.getSpellChecker().learnWord(word);
        } else {
            Core.getSpellChecker().ignoreWord(word);
        }

        controller.spellCheckerThread.resetCache();

        // redraw segment
        getOmDocument().rebuildElementsForSegment(
                controller.displayedEntryIndex);
    }
}
