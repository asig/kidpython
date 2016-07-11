package com.asigner.kidpython.ide;

import com.asigner.kidpython.compiler.CodeGenerator;
import com.asigner.kidpython.compiler.Error;
import com.asigner.kidpython.compiler.Parser;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.compiler.runtime.NativeFunctions;
import com.asigner.kidpython.compiler.runtime.VirtualMachine;
import com.asigner.kidpython.compiler.runtime.Instruction;
import com.asigner.kidpython.ide.console.ConsoleComposite;
import com.asigner.kidpython.ide.console.ConsoleInputStream;
import com.asigner.kidpython.ide.console.ConsoleOutputStream;
import com.asigner.kidpython.ide.turtle.TurtleCanvas;
import com.asigner.kidpython.ide.util.AnsiEscapeCodes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
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

        exitItem.addListener(SWT.Selection, event-> {
            shell.getDisplay().dispose();
            System.exit(0);
        });

        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        addToolbarItem(toolBar, "RUN", event -> {
            runCode(sourceCodeComposite.getText());
        });
        addToolbarItem(toolBar, "PRINT", event -> {
            consoleComposite.write("Hello " + AnsiEscapeCodes.IMAGE_NEGATIVE + "Hello" + AnsiEscapeCodes.IMAGE_POSITIVE + "\u001B[0m, \u001B[1m\u001B[3m\u001b[41m\u001b[33mWorld!\u001b[0m " + l + "\n");
            l = l+1;
        });
        ToolItem separator = new ToolItem(toolBar, SWT.SEPARATOR);
        addToolbarItem(toolBar, "Turtle", event -> {
            turtleCanvas.setPen(new RGB(255,0,255), 10);
            turtleCanvas.move(100);
            turtleCanvas.turn(47);
        });

        toolBar.pack();

        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        SashForm sashForm2 = new SashForm(sashForm, SWT.HORIZONTAL);
        sashForm2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sourceCodeComposite = new SourceCodeComposite(sashForm2, SWT.NONE);
        turtleCanvas = new TurtleCanvas(sashForm2, SWT.NONE);

        consoleComposite = new ConsoleComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        sashForm.setWeights(new int[] {1, 1});

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
    }

    private void addToolbarItem(ToolBar toolbar, String text, Listener handler) {
        ToolItem item = new ToolItem(toolbar, SWT.PUSH);
        item.setText(text);
        item.addListener(SWT.Selection, handler);

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
        virtualMachine.run();
    }
}
