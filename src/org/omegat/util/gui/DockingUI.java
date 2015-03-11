/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, Henry Pijffers, 
                         Benjamin Siband, and Kim Bruning
               2007 Zoltan Bartko
               2008 Andrzej Sawula, Alex Buloichik
               2009-2010 Alex Buloichik
               2014 Yu Tang
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.util.gui;

import java.awt.Color;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;

import org.omegat.core.Core;
import org.omegat.util.OStrings;
import org.omegat.util.Platform;

import com.vlsolutions.swing.docking.AutoHidePolicy;
import com.vlsolutions.swing.docking.AutoHidePolicy.ExpandMode;
import com.vlsolutions.swing.docking.ui.DockingUISettings;

import java.awt.Font;

/**
 * Docking UI support.
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers
 * @author Benjamin Siband
 * @author Kim Bruning
 * @author Zoltan Bartko
 * @author Andrzej Sawula
 * @author Alex Buloichik
 * @author Yu Tang
 * 
 */
public class DockingUI {

    /**
     * Initialize docking subsystem.
     */
    public static void initialize() {
        DockingUISettings.getInstance().installUI();
        AutoHidePolicy.getPolicy().setExpandMode(ExpandMode.EXPAND_ON_ROLLOVER);
        UIManager.put("DockViewTitleBar.minimizeButtonText", OStrings.getString("DOCKING_HINT_MINIMIZE"));
        UIManager.put("DockViewTitleBar.maximizeButtonText", OStrings.getString("DOCKING_HINT_MAXIMIZE"));
        UIManager.put("DockViewTitleBar.restoreButtonText", OStrings.getString("DOCKING_HINT_RESTORE"));
        UIManager.put("DockViewTitleBar.attachButtonText", OStrings.getString("DOCKING_HINT_DOCK"));
        UIManager.put("DockViewTitleBar.floatButtonText", OStrings.getString("DOCKING_HINT_UNDOCK"));
        UIManager.put("DockViewTitleBar.closeButtonText", new String());
        UIManager.put("DockTabbedPane.minimizeButtonText", OStrings.getString("DOCKING_HINT_MINIMIZE"));
        UIManager.put("DockTabbedPane.maximizeButtonText", OStrings.getString("DOCKING_HINT_MAXIMIZE"));
        UIManager.put("DockTabbedPane.restoreButtonText", OStrings.getString("DOCKING_HINT_RESTORE"));
        UIManager.put("DockTabbedPane.floatButtonText", OStrings.getString("DOCKING_HINT_UNDOCK"));
        UIManager.put("DockTabbedPane.closeButtonText", new String());

        Font defaultFont = UIManager.getFont("Label.font");
        UIManager.put("DockViewTitleBar.titleFont", defaultFont);
        UIManager.put("JTabbedPaneSmartIcon.font", defaultFont);
        UIManager.put("AutoHideButton.font", defaultFont);

        UIManager.put("DockViewTitleBar.isCloseButtonDisplayed", Boolean.FALSE);

        UIManager.put("DockViewTitleBar.hide", getIcon("minimize.gif"));
        UIManager.put("DockViewTitleBar.hide.rollover", getIcon("minimize.rollover.gif"));
        UIManager.put("DockViewTitleBar.hide.pressed", getIcon("minimize.pressed.gif"));
        UIManager.put("DockViewTitleBar.maximize", getIcon("maximize.gif"));
        UIManager.put("DockViewTitleBar.maximize.rollover", getIcon("maximize.rollover.gif"));
        UIManager.put("DockViewTitleBar.maximize.pressed", getIcon("maximize.pressed.gif"));
        UIManager.put("DockViewTitleBar.restore", getIcon("restore.gif"));
        UIManager.put("DockViewTitleBar.restore.rollover", getIcon("restore.rollover.gif"));
        UIManager.put("DockViewTitleBar.restore.pressed", getIcon("restore.pressed.gif"));
        UIManager.put("DockViewTitleBar.dock", getIcon("restore.gif"));
        UIManager.put("DockViewTitleBar.dock.rollover", getIcon("restore.rollover.gif"));
        UIManager.put("DockViewTitleBar.dock.pressed", getIcon("restore.pressed.gif"));
        UIManager.put("DockViewTitleBar.float", getIcon("undock.gif"));
        UIManager.put("DockViewTitleBar.float.rollover", getIcon("undock.rollover.gif"));
        UIManager.put("DockViewTitleBar.float.pressed", getIcon("undock.pressed.gif"));
        UIManager.put("DockViewTitleBar.attach", getIcon("dock.gif"));
        UIManager.put("DockViewTitleBar.attach.rollover", getIcon("dock.rollover.gif"));
        UIManager.put("DockViewTitleBar.attach.pressed", getIcon("dock.pressed.gif"));

        UIManager.put("DockViewTitleBar.menu.hide", getIcon("minimize.gif"));
        UIManager.put("DockViewTitleBar.menu.maximize", getIcon("maximize.gif"));
        UIManager.put("DockViewTitleBar.menu.restore", getIcon("restore.gif"));
        UIManager.put("DockViewTitleBar.menu.dock", getIcon("restore.gif"));
        UIManager.put("DockViewTitleBar.menu.float", getIcon("undock.gif"));
        UIManager.put("DockViewTitleBar.menu.attach", getIcon("dock.gif"));

        UIManager.put("DockViewTitleBar.menu.close", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.close", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.close.rollover", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.close.pressed", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.close", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.hide", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.maximize", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.float", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.closeAll", getIcon("empty.gif"));
        UIManager.put("DockTabbedPane.menu.closeAllOther", getIcon("empty.gif"));

        UIManager.put("DockingDesktop.closeActionAccelerator", null);
        UIManager.put("DockingDesktop.maximizeActionAccelerator", null);
        UIManager.put("DockingDesktop.dockActionAccelerator", null);
        UIManager.put("DockingDesktop.floatActionAccelerator", null);

        UIManager.put("DragControler.detachCursor", getIcon("undock.gif").getImage());

        // to ensure DockViewTitleBar title readability
        Color textColor = UIManager.getColor("InternalFrame.inactiveTitleForeground");
        Color backColor = UIManager.getColor("Panel.background");
        if (textColor!= null && backColor!=null) { // One of these could be null
            if (textColor.equals(backColor)) {
                float[] hsb = Color.RGBtoHSB(textColor.getRed(),
                        textColor.getGreen(), textColor.getBlue(), null);
                float brightness = hsb[2]; // darkest 0.0f <--> 1.0f brightest
                if (brightness >= 0.5f) {
                    brightness -= 0.5f;    // to darker
                } else {
                    brightness += 0.5f;    // to brighter
                }
                int rgb = Color.HSBtoRGB(hsb[0], hsb[1], brightness);
                ColorUIResource res = new ColorUIResource(rgb);
                UIManager.put("InternalFrame.inactiveTitleForeground", res);
            }
        }
        
        // AMK test
        Color borderColor = new Color(0x9B9B9B);
        UIManager.put("DockView.singleDockableBorder", new EmptyBorder(5, 5, 5, 5));
        // This color doesn't seem to be obtainable properly. These values are from visual inspection.
        if (Platform.isMacOSX()) {
            UIManager.put("DockView.tabbedDockableBorder", new MatteBorder(0, 5, 5, 5, new Color(0xE6E6E6)));
        } else {
            UIManager.put("DockView.tabbedDockableBorder", new MatteBorder(2, 5, 5, 5, Color.WHITE));
        }
        UIManager.put("DockViewTitleBar.border", new RoundedCornerBorder(8, borderColor, RoundedCornerBorder.SIDE_TOP));
        UIManager.put("AutoHideButton.expandBorderBottom", new RoundedCornerBorder(8, borderColor, RoundedCornerBorder.SIDE_BOTTOM));
        UIManager.put("OmegaTDockablePanel.border", new MatteBorder(1, 1, 1, 1, borderColor));
        UIManager.put("AutoHideButtonPanel.bottomBorder", new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, borderColor),
                new EmptyBorder(0, 10, 0, 10)));
        UIManager.put("AutoHideButtonPanel.background", new Color(0xDEDEDE));
        UIManager.put("AutoHideButtonPanelUI", "org.omegat.util.gui.AutoHideButtonPanelUI");
        UIManager.put("InternalFrame.activeTitleBackground", new Color(0xF6F6F7));
        UIManager.put("InternalFrame.inactiveTitleBackground", UIManager.getColor("Label.background"));
        UIManager.put("AutoHideButton.background", UIManager.getColor("Label.background"));
        UIManager.put("OmegaTStatusArea.border", new MatteBorder(1, 1, 1, 1, new Color(0x575757)));
        
        if (Platform.isMacOSX()) {
            Font niceFont = new Font("Helvetica Neue", defaultFont.getStyle(), defaultFont.getSize());
            UIManager.put("DockViewTitleBar.titleFont", niceFont);
            UIManager.put("JTabbedPaneSmartIcon.font", niceFont);
            UIManager.put("AutoHideButton.font", niceFont);
            UIManager.put("Panel.font", niceFont);
            UIManager.put("Label.font", niceFont);
            UIManager.put("Button.font", niceFont);
            UIManager.put("CheckBox.font", niceFont);
            UIManager.put("CheckBoxMenuItem.font", niceFont);
            UIManager.put("ColorChooser.font", niceFont);
            UIManager.put("ComboBox.font", niceFont);
            UIManager.put("List.font", niceFont);
            UIManager.put("RadioButton.font", niceFont);
            UIManager.put("RadioButtonMenuItem.font", niceFont);
            UIManager.put("Spinner.font", niceFont);
        }
        
        UIManager.put("DockViewTitleBar.maximize", getIcon("appbar.arrow.expand.inactive.png"));
        UIManager.put("DockViewTitleBar.maximize.rollover", getIcon("appbar.arrow.expand.png"));
        UIManager.put("DockViewTitleBar.maximize.pressed", getIcon("appbar.arrow.expand.pressed.png"));
        UIManager.put("DockViewTitleBar.restore", getIcon("appbar.arrow.collapsed.inactive.png"));
        UIManager.put("DockViewTitleBar.restore.rollover", getIcon("appbar.arrow.collapsed.png"));
        UIManager.put("DockViewTitleBar.restore.pressed", getIcon("appbar.arrow.collapsed.pressed.png"));
        UIManager.put("DockViewTitleBar.hide", getIcon("appbar.download.inactive.png"));
        UIManager.put("DockViewTitleBar.hide.rollover", getIcon("appbar.download.png"));
        UIManager.put("DockViewTitleBar.hide.pressed", getIcon("appbar.download.pressed.png"));
        UIManager.put("DockViewTitleBar.float", getIcon("appbar.new.window.inactive.png"));
        UIManager.put("DockViewTitleBar.float.rollover", getIcon("appbar.new.window.png"));
        UIManager.put("DockViewTitleBar.float.pressed", getIcon("appbar.new.window.pressed.png"));
        UIManager.put("DockViewTitleBar.dock", getIcon("appbar.upload.inactive.png"));
        UIManager.put("DockViewTitleBar.dock.rollover", getIcon("appbar.upload.png"));
        UIManager.put("DockViewTitleBar.dock.pressed", getIcon("appbar.upload.pressed.png"));
        UIManager.put("DockViewTitleBar.attach", getIcon("appbar.dock.window.inactive.png"));
        UIManager.put("DockViewTitleBar.attach.rollover", getIcon("appbar.dock.window.png"));
        UIManager.put("DockViewTitleBar.attach.pressed", getIcon("appbar.dock.window.pressed.png"));
        
        UIManager.put("DragControler.detachCursor", getIcon("appbar.upload.png").getImage());
        
        UIManager.put("DockViewTitleBar.disableCustomPaint", true);
    }

    /**
     * Load icon from classpath.
     * 
     * @param iconName
     *            icon file name
     * @return icon instance
     */
    private static ImageIcon getIcon(final String iconName) {
        return new ImageIcon(ResourcesUtil.getImage("/org/omegat/gui/resources/" + iconName));
    }

    /**
     * Move window to the center of main window.
     * 
     * @param window
     *            window
     */
    public static void displayCentered(final Window window) {
        window.setLocationRelativeTo(Core.getMainWindow().getApplicationFrame());
    }

    /**
     * Removes first, last and duplicate separators from menu.
     */
    public static void removeUnusedMenuSeparators(final JPopupMenu menu) {
        if (menu.getComponentCount() > 0 && menu.getComponent(0) instanceof JSeparator) {
            // remove first separator
            menu.remove(0);
        }
        if (menu.getComponentCount() > 0
                && menu.getComponent(menu.getComponentCount() - 1) instanceof JSeparator) {
            // remove last separator
            menu.remove(menu.getComponentCount() - 1);
        }
        for (int i = 0; i < menu.getComponentCount() - 1; i++) {
            if (menu.getComponent(i) instanceof JSeparator && menu.getComponent(i + 1) instanceof JSeparator) {
                // remove duplicate separators
                menu.remove(i);
            }
        }
    }
}
