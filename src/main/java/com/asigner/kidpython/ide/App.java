package com.asigner.kidpython.ide;

import com.asigner.kidpython.compiler.CodeGenerator;
import com.asigner.kidpython.compiler.Error;
import com.asigner.kidpython.compiler.Parser;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.compiler.runtime.Instruction;
import com.asigner.kidpython.compiler.runtime.NativeFunctions;
import com.asigner.kidpython.compiler.runtime.VirtualMachine;
import com.asigner.kidpython.ide.console.ConsoleComposite;
import com.asigner.kidpython.ide.console.ConsoleInputStream;
import com.asigner.kidpython.ide.console.ConsoleOutputStream;
import com.asigner.kidpython.ide.turtle.TurtleCanvas;
import com.asigner.kidpython.ide.util.SWTResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class App {

    protected Shell shell;

    private TurtleCanvas turtleCanvas;
    private SourceCodeComposite sourceCodeComposite;
    private ConsoleComposite consoleComposite;

    private InputStream consoleInputStream;
    private OutputStream consoleOutputStream;
    private PrintWriter consoleOut;
    private VirtualMachine virtualMachine;
    private NativeFunctions nativeFunctions;

    // VirtualMachine toolbar
    ToolItem vmStart;
    ToolItem vmPause;
    ToolItem vmResume;
    ToolItem vmStop;
    ToolItem vmStepInto;
    ToolItem vmStepOver;

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
    }

    /**
     * Open the window.
     */
    public void open() {
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

    private  int l = 0;
    /**
     * Create contents of the window.
     */
    protected void createContents() {
        Display display = Display.getDefault();
        shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
        shell.setLayout(new GridLayout(1, false));
        Image icon = new Image(display, App.class.getResourceAsStream("icons/icon.png"));
        shell.setImage(icon);

        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");
        shell.setMenuBar(menuBar);

        exitItem.addListener(SWT.Selection, event -> {
            shell.getDisplay().dispose();
            System.exit(0);
        });

        createVmToolbar();
//        addToolbarItem(toolBar, "PRINT", event -> {
//            consoleComposite.write("Hello " + AnsiEscapeCodes.IMAGE_NEGATIVE + "Hello" + AnsiEscapeCodes.IMAGE_POSITIVE + "\u001B[0m, \u001B[1m\u001B[3m\u001b[41m\u001b[33mWorld!\u001b[0m " + l + "\n");
//            l = l+1;
//        });
//        ToolItem separator = new ToolItem(toolBar, SWT.SEPARATOR);
//        addToolbarItem(toolBar, "Turtle", event -> {
//            turtleCanvas.setSlowMotion(true);
//            turtleCanvas.setPen(new RGB(255,0,255), 10);
//            turtleCanvas.move(100);
//            turtleCanvas.turn(47);
//        });

        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Upper part of toplevel sash
        SashForm sashForm2 = new SashForm(sashForm, SWT.HORIZONTAL);
        sashForm2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sourceCodeComposite = new SourceCodeComposite(sashForm2, SWT.NONE);
        turtleCanvas = new TurtleCanvas(sashForm2, SWT.NONE);

        // Lower part of toplevel sash
        consoleComposite = new ConsoleComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        sashForm.setWeights(new int[]{3, 1});

        shell.setText("Simple menu");
        shell.setSize(578, 390);
        shell.layout();
        shell.setMaximized(true);
        shell.open();

        consoleInputStream = new ConsoleInputStream(consoleComposite);
        consoleOutputStream = new ConsoleOutputStream(consoleComposite);
        consoleOut = new PrintWriter(consoleOutputStream, true);
        nativeFunctions = new NativeFunctions(consoleInputStream, consoleOutputStream, turtleCanvas);
        virtualMachine = new VirtualMachine(consoleOutputStream, consoleInputStream, nativeFunctions);

        updateVmButtons();

        virtualMachine.addListener(new VirtualMachine.EventListener() {
            @Override
            public void vmStateChanged() {
                updateVmButtons();
            }

            @Override
            public void newStatementReached(Stmt stmt) {

            }

            @Override
            public void programSet() {
                updateVmButtons();
            }

            @Override
            public void reset() {
                updateVmButtons();
            }
        });
    }

    private void updateVmButtons() {
        shell.getDisplay().syncExec(() -> {
            VirtualMachine.State state = virtualMachine.getState();
            switch (state) {
                case RUNNING:
                    vmStart.setEnabled(false);
                    vmPause.setEnabled(true);
                    vmResume.setEnabled(false);
                    vmStop.setEnabled(true);
                    vmStepInto.setEnabled(false);
                    vmStepOver.setEnabled(false);
                    break;
                case STOPPED:
                    vmStart.setEnabled(true);
                    vmPause.setEnabled(false);
                    vmResume.setEnabled(false);
                    vmStop.setEnabled(false);
                    vmStepInto.setEnabled(true);
                    vmStepOver.setEnabled(true);
                    break;
                case PAUSED:
                    vmStart.setEnabled(false);
                    vmPause.setEnabled(false);
                    vmResume.setEnabled(true);
                    vmStop.setEnabled(true);
                    vmStepInto.setEnabled(true);
                    vmStepOver.setEnabled(true);
                    break;
            }
        });

    }
    private void createVmToolbar() {
        ToolBar vmToolbar = new ToolBar(shell, SWT.BORDER);
        vmToolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        vmStart = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/nav_go@2x.png"), event -> {
            runCode(sourceCodeComposite.getText());
        });
        vmPause = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/suspend_co@2x.png"), event -> {
            virtualMachine.pause();
        });
        vmResume = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/resume_co@2x.png"), event -> {
            virtualMachine.pause();
        });
        vmStop = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stop@2x.png"), event -> {
            stopVm();
        });
        vmStepOver = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stepover_co@2x.png"), event -> {
            stepInto();
        });
        vmStepInto = addToolbarItem(vmToolbar, SWTResources.getImage("/com/asigner/kidpython/ide/toolbar/stepinto_co@2x.png"), event -> {
            stepOver();
        });

        vmStart.setEnabled(true);
        vmStop.setEnabled(false);
        vmToolbar.pack();
    }

    private ToolItem addToolbarItem(ToolBar toolbar, Image image, Listener handler) {
        ToolItem item = new ToolItem(toolbar, SWT.PUSH);
        item.setImage(image);
        item.addListener(SWT.Selection, handler);
        return item;
    }

    private void runCode(String source) {
        Parser p = new Parser(source);
        Stmt stmt = p.parse();
        if (stmt == null) {
            for (Error e : p.getErrors()) {
                consoleOut.println(e);
            }
            return;
        }

        CodeGenerator codeGen = new CodeGenerator(stmt);
        List<Instruction> program = codeGen.generate();
        int i = 0;
        for (Instruction instr : program) {
            System.out.println(String.format("%04d: %s", i++, instr));
        }
        System.out.flush();

        virtualMachine.setProgram(program);
        virtualMachine.start();
    }

    private void stopVm() {
        virtualMachine.stop();
    }

    private void stepInto() {
        // Implement me
    }

    private void stepOver() {
        // Implement me
    }
}
