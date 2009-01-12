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

package org.omegat.core;

import org.omegat.core.data.IProject;
import org.omegat.core.data.NotLoadedProject;
import org.omegat.core.matching.ITokenizer;
import org.omegat.core.matching.Tokenizer;
import org.omegat.core.segmentation.SRX;
import org.omegat.core.spellchecker.ISpellChecker;
import org.omegat.core.spellchecker.SpellChecker;
import org.omegat.core.threads.CheckThread;
import org.omegat.core.threads.IAutoSave;
import org.omegat.core.threads.SaveThread;
import org.omegat.gui.editor.EditorController;
import org.omegat.gui.editor.IEditor;
import org.omegat.gui.glossary.GlossaryTextArea;
import org.omegat.gui.main.ConsoleWindow;
import org.omegat.gui.main.IMainWindow;
import org.omegat.gui.main.IMessageWindow;
import org.omegat.gui.main.MainWindow;
import org.omegat.gui.matches.IMatcher;
import org.omegat.gui.matches.MatchesTextArea;
import org.omegat.gui.tagvalidation.ITagValidation;
import org.omegat.gui.tagvalidation.TagValidationTool;
import org.omegat.util.Log;

/**
 * Class which contains all components instances.
 * 
 * Note about threads synchronization: each component must have only local
 * synchronization. It mustn't synchronize around other components or some other
 * objects.
 * 
 * Components which works in Swing UI thread can have other synchronization
 * idea: it can not be synchronized to access to some data which changed only in UI thread.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class Core {
    private static IProject currentProject;
    private static IMainWindow mainWindow;
    private static IMessageWindow messageWindow;
    private static IEditor editor;
    private static ITagValidation tagValidation;
    private static IMatcher matcher;
    private static ITokenizer tokenizer;
    private static ISpellChecker spellChecker;
    private static boolean consoleMode;
    private static String preferredConfigDir;
    
    private static CheckThread checkThread;
    private static IAutoSave saveThread;
    
    private static GlossaryTextArea glossary;
    
    /**
     * Toggle for quiet mode. In quiet mode, less info is printed to the screen.
     * Quiet mode was introduced for the console mode.
     */
    private static boolean quiet_mode;


    /** Get project instance. */
    public static IProject getProject() {
        return currentProject;
    }

    /** Set new current project. */
    public static void setProject(final IProject newCurrentProject) {
        currentProject = newCurrentProject;
    }


    /** Get main window instance. */
    public static IMainWindow getMainWindow() {
        return mainWindow;
    }
    
    /** Get message window instance. */
    public static IMessageWindow getMessageWindow() {
        if (getConsoleMode()) {
            return messageWindow;
        } else {
            return mainWindow;
        }
    }

    /** Get editor instance. */
    public static IEditor getEditor() {
        return editor;
    }

    /** Get tag validation component instance. */
    public static ITagValidation getTagValidation() {
        return tagValidation;
    }

    /** Get matcher component instance. */
    public static IMatcher getMatcher() {
        return matcher;
    }
    
    /** Get tokenizer component instance. */
    public static ITokenizer getTokenizer() {
        return tokenizer;
    }
    
    /** Get spell checker instance. */
    public static ISpellChecker getSpellChecker() {
        return spellChecker;
    }
    
    public static IAutoSave getAutoSave() {
        return saveThread;
    }
    
    /** sets the application in console (scripting) mode. In console mode, the 
     * GUI is not started, but the project loaded and translated and the program 
     * ends. All informative messages are printed to the standard and error 
     * outputs (the console) */
    public static void setConsoleMode(boolean mode)
    {
        consoleMode = mode;
    }

    /** gets the value of console mode */
    public static boolean getConsoleMode()
    {
        return consoleMode;
    }

    public static void setQuietMode(boolean enable) {
        quiet_mode = enable;
    }

    public static boolean getQuietMode() {
        return quiet_mode;
    }

    /** sets the preferred configuration directory to be used */
    public static void setPreferredConfigDir(String configDir)
    {
        System.out.println("Preferred config-dir set to:"+configDir);
        preferredConfigDir = configDir;
    }

    /** gets the preferred configuration directory */
    public static String getPreferredConfigDir()
    {
        return preferredConfigDir;
    }

    /**
     * Initialize application components.
     */
    public static void initialize(final String[] args) throws Exception {
        // 1. Initialize project
        currentProject = new NotLoadedProject();

        if (getConsoleMode()) {
            // 2. Initialize application window
            ConsoleWindow me = new ConsoleWindow();
            messageWindow = me;
            
            //3. Initialize other components. There are less in console (scripting) mode.
            //we don't need editors, tag validators, matchers, spell checker or glossary.
			tokenizer = createComponent(ITokenizer.class, new Tokenizer(), args);
        } else {
            // 2. Initialize application frame
            MainWindow me = new MainWindow();
            mainWindow = me;

            //3. Initialize other components
            editor = new EditorController(me);
            tagValidation = new TagValidationTool(me);
            matcher = new MatchesTextArea(me);
            glossary = new GlossaryTextArea();
			tokenizer = createComponent(ITokenizer.class, new Tokenizer(), args);
			spellChecker = new SpellChecker();
        }

        if (!getConsoleMode()) {
            checkThread = new CheckThread();
            checkThread.start();
        
            SaveThread th = new SaveThread();
            saveThread = th;
            th.start();
        }
        
        SRX.getSRX();
    }
    
    /**
     * Try to create component instance by class specified in command line.
     * 
     * @param <T>
     *                return type
     * @param interfaceClass
     *                component interface class
     * @param defaultImplementation
     *                default component implementation instance
     * @param args
     *                command line
     * @return component implementation
     */
    protected static <T> T createComponent(final Class<T> interfaceClass, final T defaultImplementation,
            final String[] args) {
        final String prefix = interfaceClass.getSimpleName() + "=";
        String implClassName = null;
        try {
            for (String arg : args) {
                if (arg.startsWith(prefix)) {
                    implClassName = arg.substring(prefix.length());
                    return (T) Class.forName(implClassName).newInstance();
                }
            }
        } catch (Exception ex) {
            Log.log(ex);
        }
        return defaultImplementation;
    }
    
    /**
     * Set main window instance for unit tests.
     * 
     * @param mainWindow
     */
    protected static void setMainWindow(IMainWindow mainWindow) {
        Core.mainWindow = mainWindow;
    }
    
    /**
     * Set project instance for unit tests.
     * 
     * @param currentProject
     */
    protected static void setCurrentProject(IProject currentProject) {
        Core.currentProject = currentProject;
    }

}
