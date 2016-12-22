package com.asigner.kidpython.ide;

import com.asigner.kidpython.compiler.CodeGenerator;
import com.asigner.kidpython.compiler.Error;
import com.asigner.kidpython.compiler.Parser;
import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.ide.console.ConsoleComposite;
import com.asigner.kidpython.ide.editor.Stylesheet;
import com.asigner.kidpython.ide.platform.CocoaUiEnhancer;
import com.asigner.kidpython.ide.settings.ColorSchemePrefPage;
import com.asigner.kidpython.ide.settings.RepositoryPrefPage;
import com.asigner.kidpython.ide.sync.LocalPersistenceStrategy;
import com.asigner.kidpython.ide.sync.PersistenceStrategy;
import com.asigner.kidpython.ide.sync.SyncService;
import com.asigner.kidpython.ide.turtle.TurtleCanvas;
import com.asigner.kidpython.ide.util.AnsiEscapeCodes;
import com.asigner.kidpython.ide.util.SWTResources;
import com.asigner.kidpython.runtime.FuncValue;
import com.asigner.kidpython.runtime.Instruction;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.asigner.kidpython.runtime.nativecode.Export;
import com.asigner.kidpython.runtime.nativecode.MathWrapper;
import com.asigner.kidpython.runtime.nativecode.TurtleWrapper;
import com.asigner.kidpython.runtime.nativecode.UtilsWrapper;
import com.asigner.kidpython.util.Messages;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.asigner.kidpython.util.Messages.Key.Action_About;
import static com.asigner.kidpython.util.Messages.Key.Action_Help;
import static com.asigner.kidpython.util.Messages.Key.Action_Pause;
import static com.asigner.kidpython.util.Messages.Key.Action_Preferences;
import static com.asigner.kidpython.util.Messages.Key.Action_Resume;
import static com.asigner.kidpython.util.Messages.Key.Action_Run;
import static com.asigner.kidpython.util.Messages.Key.Action_Step_Into;
import static com.asigner.kidpython.util.Messages.Key.Action_Step_Over;
import static com.asigner.kidpython.util.Messages.Key.Action_Stop;
import static com.asigner.kidpython.util.Messages.Key.MenuItem_Exit;
import static com.asigner.kidpython.util.Messages.Key.Menu_File;
import static com.asigner.kidpython.util.Messages.Key.Toolbar_ColorScheme;
import static com.asigner.kidpython.util.Messages.Key.Toolbar_FollowCodeExecution;
import static com.asigner.kidpython.util.Messages.Key.VM_Error_While_Compiling;

public class App {

    private static final String APP_NAME = "Programmable Fun";

    protected Shell shell;

    private CodeRepository codeRepository;
    private Set<String> wellKnownWords;

    private TurtleCanvas turtleCanvas;
    private SourceCodeComposite sourceCodeComposite;
    private CallStackComposite callStackComposite;
    private ConsoleComposite consoleComposite;

    private PrintWriter consoleOut;
    private VirtualMachine virtualMachine;

    private BaseAction vmStartAction;
    private BaseAction vmPauseAction;
    private BaseAction vmResumeAction;
    private BaseAction vmStopAction;
    private BaseAction vmStepIntoAction;
    private BaseAction vmStepOverAction;
    private BaseAction helpAction;
    private IAction preferencesAction;
    private IAction aboutAction;

    private CoolBarManager coolBarManager;

    private Settings settings;

    private boolean highlightLines = false;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            App window = new App();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public App() {
        settings = Settings.getInstance();
        PersistenceStrategy persistenceStrategy = new LocalPersistenceStrategy();
        for (SyncService syncService : SyncService.ALL) {
            if (syncService.isConnected()) {
                persistenceStrategy = syncService.getPersistenceStrategy();
                break;
            }
        }
        codeRepository = new CodeRepository(persistenceStrategy);
        codeRepository.load();
    }

    /**
     * Open the window.
     */
    public void open() {
        Display.setAppName(APP_NAME);
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        Display display = Display.getDefault();
        shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
        shell.setLayout(new GridLayout(1, false));
        Image icon = new Image(display, App.class.getResourceAsStream("icons/icon.png"));
        shell.setImage(icon);

        createActions();
        createMenu(display);
        createToolbar();

        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Upper part of toplevel sash
        SashForm sashForm2 = new SashForm(sashForm, SWT.HORIZONTAL);
        sashForm2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sourceCodeComposite = new SourceCodeComposite(sashForm2, SWT.NONE, codeRepository);
        sourceCodeComposite.setStylesheet(Stylesheet.ALL.get(settings.getSelectedStylesheetIndex()));
        callStackComposite = new CallStackComposite(sashForm2, SWT.NONE);
        turtleCanvas = new TurtleCanvas(sashForm2, SWT.NONE);
        sashForm2.setWeights(new int[]{10,2,8});

        // Lower part of toplevel sash
        consoleComposite = new ConsoleComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        List<Object> nativeCodeWrappers = Lists.newArrayList(
                new TurtleWrapper(turtleCanvas), new MathWrapper(), new UtilsWrapper(consoleComposite)
        );
        wellKnownWords = Sets.newHashSet();
        for (Object wrapper : nativeCodeWrappers) {
            Export export = wrapper.getClass().getAnnotation(Export.class);
            if (export != null) {
                String name = export.name();
                if (Strings.isNullOrEmpty(name)) {
                    name = wrapper.getClass().getName();
                }
                wellKnownWords.add(name);
            }
            for (Method m : wrapper.getClass().getMethods()) {
                export = m.getAnnotation(Export.class);
                if (export != null) {
                    String name = export.name();
                    if (Strings.isNullOrEmpty(name)) {
                        name = m.getName();
                    }
                    wellKnownWords.add(name);
                }
            }
        }
        sourceCodeComposite.setWellKnownWords(wellKnownWords);

        sashForm.setWeights(new int[]{3, 1});

        shell.setText(APP_NAME);
        shell.setMaximized(true);
        shell.layout();
        shell.open();

        consoleOut = new PrintWriter(consoleComposite.getOutputStream(), true);
        virtualMachine = new VirtualMachine(consoleComposite.getOutputStream(), consoleComposite.getInputStream(), nativeCodeWrappers);

        callStackComposite.setVirtualMachine(virtualMachine);

        updateVmButtons();

        virtualMachine.addListener(new VirtualMachine.EventListener() {
            @Override
            public void vmStateChanged() {
                showVmStateMessage();
                updateVmButtons();
                updateSourceCodeSelectionButtons();
                switch (virtualMachine.getState()) {
                    case STOPPED:
                        highlightLine(-1);
                        break;
                    case RUNNING:
                        if (!highlightLines) {
                            highlightLine(-1);
                        }
                }
            }

            @Override
            public void newStatementReached(Node stmt) {
                if (highlightLines) {
                    highlightLine(stmt.getPos().getLine());
                }
            }

            @Override
            public void programSet() {
                updateVmButtons();
                highlightLine(-1);
            }

            @Override
            public void reset() {
                updateVmButtons();
                highlightLine(-1);
            }

            @Override
            public void enteringFunction(FuncValue func) {
            }

            @Override
            public void leavingFunction() {
            }
        });
    }

    private void updateSourceCodeSelectionButtons() {
        boolean enabled = virtualMachine.getState() == VirtualMachine.State.STOPPED;
        shell.getDisplay().asyncExec(() -> sourceCodeComposite.setSourceCodeSelectionEnabled(enabled));
    }

    private void updateVmButtons() {
        shell.getDisplay().asyncExec(() -> {
            VirtualMachine.State state = virtualMachine.getState();
            switch (state) {
                case RUNNING:
                    vmStartAction.setEnabled(false);
                    vmPauseAction.setEnabled(true);
                    vmResumeAction.setEnabled(false);
                    vmStopAction.setEnabled(true);
                    vmStepIntoAction.setEnabled(false);
                    vmStepOverAction.setEnabled(false);
                    break;
                case STOPPED:
                    vmStartAction.setEnabled(true);
                    vmPauseAction.setEnabled(false);
                    vmResumeAction.setEnabled(false);
                    vmStopAction.setEnabled(false);
                    vmStepIntoAction.setEnabled(true);
                    vmStepOverAction.setEnabled(true);
                    break;
                case PAUSED:
                    vmStartAction.setEnabled(false);
                    vmPauseAction.setEnabled(false);
                    vmResumeAction.setEnabled(true);
                    vmStopAction.setEnabled(true);
                    vmStepIntoAction.setEnabled(true);
                    vmStepOverAction.setEnabled(true);
                    break;
            }
        });
    }

    private void showVmStateMessage() {
        VirtualMachine.State state = virtualMachine.getState();
        switch (state) {
            case RUNNING:
                status(Messages.get(Messages.Key.VM_Execution_Started));
                break;
            case STOPPED:
                status(Messages.get(Messages.Key.VM_Execution_Stopped));
                break;
            case PAUSED:
                status(Messages.get(Messages.Key.VM_Execution_Paused));
                break;
        }
    }

    private void status(String s) {
        consoleOut.println(AnsiEscapeCodes.FG_BLUE + s + AnsiEscapeCodes.FG_BLACK);
    }

    private void createActions() {
        vmStartAction = new BaseAction(Messages.get(Action_Run), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/nav_go@2x.png"), this::runCode);
        vmPauseAction = new BaseAction(Messages.get(Action_Pause), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/suspend_co@2x.png"), () -> virtualMachine.pause() );
        vmResumeAction = new BaseAction(Messages.get(Action_Resume), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/resume_co@2x.png"), () -> virtualMachine.start() );
        vmStopAction = new BaseAction(Messages.get(Action_Stop), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stop@2x.png"), () -> virtualMachine.stop());
        vmStepIntoAction = new BaseAction(Messages.get(Action_Step_Into), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stepinto_co@2x.png"), this::stepInto);
        vmStepOverAction = new BaseAction(Messages.get(Action_Step_Over), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stepover_co@2x.png"), this::stepOver);
        aboutAction = new BaseAction(Messages.get(Action_About), this::showAbout);
        preferencesAction = new BaseAction(Messages.get(Action_Preferences), this::showPreferences);
        helpAction = new BaseAction(Messages.get(Action_Help), SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/help@2x.png"), this::showHelp);
    }

    private void createMenu(Display display) {
        Listener quitListener = event -> {
            display.dispose();
            System.exit(0);
        };

        boolean isMac = System.getProperty( "os.name" ).equals( "Mac OS X" );
        if (isMac) {
            CocoaUiEnhancer enhancer = new CocoaUiEnhancer(APP_NAME);
            enhancer.hookApplicationMenu( display, quitListener, aboutAction, preferencesAction);
        }

        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText(Messages.get(Menu_File));

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText(Messages.get(MenuItem_Exit));
        shell.setMenuBar(menuBar);
        exitItem.addListener(SWT.Selection, quitListener);

        if (!isMac) {
            MenuItem preferencesItem = new MenuItem(fileMenu, SWT.PUSH);
            preferencesItem.setText(preferencesAction.getText());
            preferencesItem.addListener(SWT.Selection, event -> preferencesAction.run());
        }
    }

    private void createToolbar() {
        coolBarManager = new CoolBarManager(SWT.FLAT);
        final ToolBarManager vmToolBarManager = new ToolBarManager(SWT.FLAT | SWT.NO_FOCUS);
        coolBarManager.add(vmToolBarManager);
        vmToolBarManager.add(vmStartAction);
        vmToolBarManager.add(vmPauseAction);
        vmToolBarManager.add(vmResumeAction);
        vmToolBarManager.add(vmStopAction);
        vmToolBarManager.add(vmStepIntoAction);
        vmToolBarManager.add(vmStepOverAction);

        final ToolBarManager helpToolBarManager = new ToolBarManager(SWT.FLAT | SWT.NO_FOCUS);
        coolBarManager.add(helpToolBarManager);
        helpToolBarManager.add(new ControlContribution("stylesheet") {
            @Override
            protected Control createControl(Composite parent) {
                final Composite composite = new Composite(parent, SWT.NULL);
                composite.setLayout(GridLayoutFactory.fillDefaults().margins(0, 0).numColumns(2).create());

                //START >>  label1
                final Label label1 = new Label(composite, SWT.NONE);
                label1.setText(Messages.get(Toolbar_ColorScheme));
                label1.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).grab(false, true).create());
                //END <<  label1

                Combo combo = new Combo(composite, SWT.READ_ONLY);
                for (Stylesheet sheet : Stylesheet.ALL) {
                    combo.add(sheet.getName());
                }
                combo.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent selectionEvent) {
                        int selected = combo.getSelectionIndex();
                        sourceCodeComposite.setStylesheet(Stylesheet.ALL.get(selected));
                        settings.setKeySelectedstylesheetIndex(selected);
                        settings.save();
                    }
                });
                combo.select(settings.getSelectedStylesheetIndex());

                return composite;
            }
        });
        helpToolBarManager.add(new ControlContribution("trace-code") {
            @Override
            protected Control createControl(Composite parent) {
                Button checkbox = new Button(parent, SWT.CHECK);
                checkbox.setText(Messages.get(Toolbar_FollowCodeExecution));
                checkbox.setSelection(highlightLines);
                checkbox.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        highlightLines = checkbox.getSelection();
                    }
                });
                return checkbox;
            }
        });
        helpToolBarManager.add(helpAction);

        coolBarManager.createControl(shell);
    }

    private void highlightCurrentInstructionLine() {
        Instruction instr = virtualMachine.getCurrentInstruction();
        int line = instr != null ? instr.getSourceNode().getPos().getLine() : -1;
        highlightLine(line);
    }

    private void highlightLine(int line) {
        sourceCodeComposite.setActiveLine(line);
    }

    private boolean loadCode() {
        sourceCodeComposite.clearErrors();
        Parser p = new Parser(sourceCodeComposite.getText());
        Stmt stmt = p.parse();
        if (stmt == null) {
            consoleOut.print(AnsiEscapeCodes.BOLD);
            consoleOut.print(AnsiEscapeCodes.FG_YELLOW);
            consoleOut.print(AnsiEscapeCodes.BG_RED);
            consoleOut.print(Messages.get(VM_Error_While_Compiling));
            consoleOut.println(AnsiEscapeCodes.RESET);
            for (Error e : p.getErrors()) {
                consoleOut.println(e);
            }
            sourceCodeComposite.setErrors(p.getErrors());
            return false;
        }

        CodeGenerator codeGen = new CodeGenerator(stmt);
        List<Instruction> program = codeGen.generate();
        int i = 0;
        for (Instruction instr : program) {
            System.out.println(String.format("%04d: %s", i++, instr));
        }
        System.out.flush();

        virtualMachine.setProgram(program);
        return true;
    }

    private void runCode() {
        if (loadCode()) {
            virtualMachine.start();
        }
    }

    private void stepInto() {
        if (virtualMachine.getState() != VirtualMachine.State.PAUSED) {
            if (!loadCode()) {
                return;
            }
        }

        int thisLine = virtualMachine.getCurrentInstruction().getSourceNode().getPos().getLine();

        virtualMachine.addListener(new VirtualMachine.EventListener() {
            @Override
            public void vmStateChanged() {
                if (virtualMachine.getState() == VirtualMachine.State.STOPPED) {
                    virtualMachine.removeListener(this);
                }
            }

            @Override
            public void newStatementReached(Node stmt) {
                int line = stmt.getPos().getLine();
                if (line != thisLine) {
                    virtualMachine.removeListener(this);
                    virtualMachine.pause();
                    highlightCurrentInstructionLine();
                }
            }

            @Override
            public void programSet() {
                virtualMachine.removeListener(this);
            }

            @Override
            public void reset() {
                virtualMachine.removeListener(this);
            }

            @Override
            public void enteringFunction(FuncValue func) { }

            @Override
            public void leavingFunction() { }
        });
        virtualMachine.start();
    }

    private void stepOver() {
        if (virtualMachine.getState() != VirtualMachine.State.PAUSED) {
            if (!loadCode()) {
                return;
            }
        }

        int thisLine = virtualMachine.getCurrentInstruction().getSourceNode().getPos().getLine();
        virtualMachine.addListener(new VirtualMachine.EventListener() {

            int callDepth = 0;

            @Override
            public void vmStateChanged() {
                if (virtualMachine.getState() == VirtualMachine.State.STOPPED) {
                    virtualMachine.removeListener(this);
                }
            }

            @Override
            public void newStatementReached(Node stmt) {
                int line = stmt.getPos().getLine();
                if (line != thisLine && callDepth <= 0) { // We might be in a function and "step over" leaves the function
                    virtualMachine.removeListener(this);
                    virtualMachine.pause();
                    highlightCurrentInstructionLine();
                }
            }

            @Override
            public void programSet() {
                virtualMachine.removeListener(this);
            }

            @Override
            public void reset() {
                virtualMachine.removeListener(this);
            }

            @Override
            public void enteringFunction(FuncValue func) {
                callDepth++;
            }

            @Override
            public void leavingFunction() {
                callDepth--;
            }
        });
        virtualMachine.start();
    }

    private void showAbout() {
        System.err.println("show about dialog");
    }

    private void showHelp() {
        System.err.println("Launch browser with help page");
    }

    private void showPreferences() {
        PreferencePage pages[] = new PreferencePage[] {
                new RepositoryPrefPage(codeRepository),
                new ColorSchemePrefPage(Stylesheet.ALL, wellKnownWords),
        };
        PreferenceManager mgr = new PreferenceManager();
        for(PreferencePage p : pages) {
            mgr.addToRoot(new PreferenceNode(p.getTitle(),p));
        }
        PreferenceDialog dlg = new PreferenceDialog(Display.getCurrent().getActiveShell(), mgr);
        dlg.open();
//        Settings.getInstance().save();^M
//
//
//        CloudConnectDialog dlg = new CloudConnectDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
//        dlg.open(codeRepository);
    }
}
