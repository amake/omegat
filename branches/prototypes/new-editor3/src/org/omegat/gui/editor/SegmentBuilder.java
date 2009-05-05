/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
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

import java.text.DecimalFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.omegat.core.data.SourceTextEntry;
import org.omegat.util.OConsts;
import org.omegat.util.Preferences;
import org.omegat.util.gui.Styles;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * Class for store information about displayed segment, and for show segment in
 * editor.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class SegmentBuilder {

    /** Attributes for show text. */
    protected static final AttributeSet ATTR_SOURCE = Styles.GREEN;
    protected static final AttributeSet ATTR_SEGMENT_MARK = Styles.BOLD;
    protected static final AttributeSet ATTR_TRANS_TRANSLATED = Styles.TRANSLATED;
    protected static final AttributeSet ATTR_TRANS_UNTRANSLATED = Styles.UNTRANSLATED;
    protected static final AttributeSet ATTR_ACTIVE = new SimpleAttributeSet();
    public static final String SEGMENT_MARK_ATTRIBUTE = "SEGMENT_MARK_ATTRIBUTE";
    public static final String SEGMENT_SPELL_CHECK = "SEGMENT_SPELL_CHECK";
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0000");

    final SourceTextEntry ste;
    final int segmentNumberInProject;

    private final Document3 doc;
    private final EditorController3 controller;
    private final EditorSettings settings;

    protected int activeTranslationBeginOffset, activeTranslationEndOffset;

    protected Position beginPosP1, endPosM1;

    /**
     * true if beginSpellCheck/endSpellCheck is P/M mode, i.e. smaller of real
     * text - used for inactive text
     * 
     * false if M/P mode, i.e. bigger of real text - used for active text
     */
    protected boolean spellPM;
    protected Position beginSpellCheckPM1, endSpellCheckPM1;

    protected int offset;

    public SegmentBuilder(final EditorController3 controller,
            final Document3 doc, final EditorSettings settings,
            final SourceTextEntry ste, final int segmentNumberInProject) {
        this.controller = controller;
        this.doc = doc;
        this.settings = settings;
        this.ste = ste;
        this.segmentNumberInProject = segmentNumberInProject;
    }

    /**
     * Create element for one segment.
     * 
     * @param doc
     *            document
     * @return OmElementSegment
     */
    public void createSegmentElement(final boolean isActive) {
        UIThreadsUtil.mustBeSwingThread();

        beginSpellCheckPM1 = null;
        endSpellCheckPM1 = null;

        doc.trustedChangesInProgress = true;
        try {
            try {
                if (beginPosP1 != null && endPosM1 != null) {
                    // remove old segment
                    int beginOffset = beginPosP1.getOffset() - 1;
                    int endOffset = endPosM1.getOffset() + 1;
                    doc.remove(beginOffset, endOffset - beginOffset);
                    offset = beginOffset;
                } else {
                    // there is no segment in document yet - need to add
                    offset = doc.getLength();
                }

                boolean translationExists = ste.getTranslation() != null
                        && ste.getTranslation().length() > 0;

                int beginOffset = offset;
                if (isActive) {
                    createActiveSegmentElement(translationExists);
                } else {
                    createInactiveSegmentElement(translationExists);
                }
                int endOffset = offset;

                beginPosP1 = doc.createPosition(beginOffset + 1);
                endPosM1 = doc.createPosition(endOffset - 1);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            doc.trustedChangesInProgress = false;
        }
    }

    /**
     * Add separator between segments - one empty line.
     * 
     * @param doc
     */
    public static void addSegmentSeparator(final Document3 doc) {
        doc.trustedChangesInProgress = true;
        try {
            try {
                doc.insertString(doc.getLength(), "\n", null);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            doc.trustedChangesInProgress = false;
        }
    }

    /**
     * Create method for active segment.
     */
    private void createActiveSegmentElement(boolean translationExists)
            throws BadLocationException {

        addInactiveSegPart(true, ste.getSrcText(), ATTR_SOURCE);

        boolean needToCheckSpelling = false;

        String activeText;
        if (translationExists) {
            // translation exist
            activeText = ste.getTranslation();
            if (settings.isAutoSpellChecking()) {
                // spell it
                needToCheckSpelling = true;
                doc.controller.spellCheckerThread.addForCheck(ste
                        .getTranslation());
            }
        } else if (!Preferences
                .isPreference(Preferences.DONT_INSERT_SOURCE_TEXT)) {
            // need to insert source text on empty translation
            activeText = ste.getSrcText();
            if (settings.isAutoSpellChecking()) {
                // spell it
                needToCheckSpelling = true;
                doc.controller.spellCheckerThread.addForCheck(ste.getSrcText());
            }
        } else {
            // empty text on non-exist translation
            activeText = "";
        }

        addActiveSegPart(activeText, ATTR_ACTIVE);

        if (needToCheckSpelling) {
            beginSpellCheckPM1 = doc
                    .createPosition(activeTranslationBeginOffset - 1);
            endSpellCheckPM1 = doc
                    .createPosition(activeTranslationEndOffset + 1);
            spellPM = false;
        }

        doc.activeTranslationBeginM1 = doc
                .createPosition(activeTranslationBeginOffset - 1);
        doc.activeTranslationEndP1 = doc
                .createPosition(activeTranslationEndOffset + 1);
    }

    /**
     * Create method for inactive segment.
     */
    private void createInactiveSegmentElement(boolean translationExists)
            throws BadLocationException {
        if (settings.isDisplaySegmentSources()) {
            addInactiveSegPart(true, ste.getSrcText(), ATTR_SOURCE);
        }

        boolean needToCheckSpelling = false;
        if (translationExists) {
            // translation exist
            if (settings.isAutoSpellChecking()) {
                // spell it
                needToCheckSpelling = true;
                doc.controller.spellCheckerThread.addForCheck(ste
                        .getTranslation());
            }
            int prevOffset = offset;
            addInactiveSegPart(false, ste.getTranslation(), settings
                    .getTranslatedAttributeSet());

            if (needToCheckSpelling) {
                // remember about u202{a,b,c} chars !
                beginSpellCheckPM1 = doc.createPosition(prevOffset + 2);
                endSpellCheckPM1 = doc.createPosition(offset - 2);
                spellPM = true;
            }
        } else if (!settings.isDisplaySegmentSources()) {
            // translation not exist, and source part doesn't displayed yet
            addInactiveSegPart(true, ste.getSrcText(), settings
                    .getUntranslatedAttributeSet());
        }
    }

    /**
     * Get segment's start position.
     * 
     * @return start position
     */
    public int getStartPosition() {
        return beginPosP1.getOffset() - 1;
    }

    /**
     * Get segment's end position.
     * 
     * @return end position
     */
    public int getEndPosition() {
        return endPosM1.getOffset() + 1;
    }

    /**
     * Set attributes for created paragraphs for better RTL support.
     * 
     * @param begin
     *            paragraphs begin
     * @param end
     *            paragraphs end
     * @param isSource
     *            is source segment part
     */
    private void setAttributes(int begin, int end, boolean isSource) {
        boolean rtl = false;
        switch (controller.currentOrientation) {
        case LTR:
            rtl = false;
            break;
        case RTL:
            rtl = true;
            break;
        case DIFFER:
            rtl = isSource ? controller.sourceLangIsRTL
                    : controller.targetLangIsRTL;
            break;
        }
        doc.setAlignment(begin, end, rtl);
    }

    private boolean isRtlAlignment(boolean isSource) {
        switch (controller.currentOrientation) {
        case LTR:
            return false;
        case RTL:
            return true;
        case DIFFER:
            return isSource ? controller.sourceLangIsRTL
                    : controller.targetLangIsRTL;
        default:
            return false;
        }
    }

    public boolean isInsideSegment(int location) {
        return beginPosP1.getOffset() - 1 <= location
                && location < endPosM1.getOffset() + 1;
    }

    public boolean isInsideSpellCheckable(int location) {
        if (beginSpellCheckPM1 == null || endSpellCheckPM1 == null) {
            return false;
        }
        int b = beginSpellCheckPM1.getOffset();
        int e = endSpellCheckPM1.getOffset();
        if (spellPM) {
            b--;
            e++;
        } else {
            b++;
            e--;
        }
        return b <= location && location < e;
    }

    /**
     * Add inactive segment part, without segment begin/end marks.
     * 
     * @param parent
     *            parent element
     * @param text
     *            segment part text
     * @param attrs
     *            attributes
     * @param langIsRTL
     * @return true if language is RTL
     */
    private void addInactiveSegPart(boolean isSource, String text,
            AttributeSet attrs) throws BadLocationException {
        boolean rtlAlign = isRtlAlignment(isSource);
        int prevOffset = offset;
        boolean rtl = isSource ? controller.sourceLangIsRTL
                : controller.targetLangIsRTL;
        insert(rtl ? "\u202b" : "\u202a", null, rtlAlign); // LTR- or RTL-
        // embedding
        insert(text, attrs, rtlAlign);
        insert("\u202c", null, rtlAlign); // end of embedding
        insert("\n", null, rtlAlign);
        setAttributes(prevOffset, offset, isSource);
    }

    /**
     * Add active segment part, with segment begin/end marks.
     * 
     * @param parent
     *            parent element
     * @param text
     *            segment part text
     * @param attrs
     *            attributes
     * @param markBeg
     *            text of begin mark
     * @param markEnd
     *            text of end mark
     * @param langIsRTL
     *            true if language is RTL
     * @return segment part element
     */
    private void addActiveSegPart(String text, AttributeSet attrs)
            throws BadLocationException {
        int prevOffset = offset;
        boolean rtl = controller.targetLangIsRTL;
        boolean rtlAlign = isRtlAlignment(false);

        String smTextB = OConsts.segmentStartString.trim().replace("0000",
                NUMBER_FORMAT.format(segmentNumberInProject));
        insert(smTextB, ATTR_SEGMENT_MARK, rtlAlign);
        insert(" ", null, rtlAlign);

        insert(rtl ? "\u202b" : "\u202a", null, rtlAlign); // LTR- or RTL-
        // embedding

        activeTranslationBeginOffset = offset;
        insert(text, attrs, rtlAlign);
        activeTranslationEndOffset = offset;

        insert("\u202c", null, rtlAlign); // end of embedding

        insert(" ", null, rtlAlign);
        String smTextE = OConsts.segmentEndString.trim();
        insert(smTextE, ATTR_SEGMENT_MARK, rtlAlign);
        insert("\n", null, rtlAlign);

        setAttributes(prevOffset, offset, false);
    }

    private void insert(String text, AttributeSet attrs, boolean rtlAlign)
            throws BadLocationException {
        SimpleAttributeSet a = new SimpleAttributeSet();
        if (attrs != null) {
            a.addAttributes(attrs);
        }
        if (rtlAlign) {
            a
                    .addAttribute(StyleConstants.Alignment,
                            StyleConstants.ALIGN_RIGHT);
        } else {
            a.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
        }
        doc.insertString(offset, text, attrs);
        offset += text.length();
    }
}
