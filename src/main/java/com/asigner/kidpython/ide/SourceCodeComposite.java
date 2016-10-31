package com.asigner.kidpython.ide;

import com.asigner.kidpython.ide.editor.CodeEditor;
import com.asigner.kidpython.ide.editor.Stylesheet;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class SourceCodeComposite extends Composite {

    private static final String KEY_SELECTEDSOURCE = "SourceCodeComposite.selectedSource";

    private CodeRepository codeRepository;
    private Settings settings;

    private CodeEditor.State[] editorStates;
    private Button[] buttons;
    private CodeEditor editor;

    private boolean selectingSource = false;
    private int selectedSource = -1;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SourceCodeComposite(Composite parent, int style, CodeRepository codeRepository) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        this.codeRepository = codeRepository;
        this.settings = Settings.getInstance();
        codeRepository.addListener(newStrategy -> init());

        this.addDisposeListener(disposeEvent -> {
            codeRepository.getSource(selectedSource).setCode(editor.getText());
            codeRepository.save();
        });
        int nofSources = codeRepository.getNofSources();

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout(nofSources, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        editorStates = new CodeEditor.State[nofSources];
        buttons = new Button[nofSources];
        for (int i = 0; i < nofSources; i++) {
            final int sourceIdx = i;
            editorStates[i] = new CodeEditor.State(codeRepository.getSource(i).getCode());
            buttons[i] = new Button(composite, SWT.TOGGLE);
            buttons[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
            buttons[i].setBounds(0, 0, 93, 29);
            buttons[i].setText(codeRepository.getSource(i).getName());
            buttons[i].addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectSource(sourceIdx);
                }
            });
            final int finalI = i;
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDoubleClick(MouseEvent mouseEvent) {
                    changeName(finalI);
                }
            });
        }

        editor = new CodeEditor(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        editor.addModifyListener(event -> {
            codeRepository.getSource(selectedSource).setCode(editor.getText());
            codeRepository.save();
        });

        selectSource(settings.getInt(KEY_SELECTEDSOURCE, 0));
    }

    private void changeName(int idx) {
        Button b = buttons[idx];
        Text text = new Text(b.getParent(), SWT.SINGLE);
        Rectangle r = b.getBounds();
        r.x += 5;
        r.y += 5;
        r.width -= 10;
        r.height -= 10;
        text.setBounds(r);
        text.setText(b.getText());
        text.setVisible(true);
        text.moveAbove(b);
        text.setSelection(0, b.getText().length());
        text.setFocus();
        Runnable acceptName = () -> {
            String newName = text.getText();
            codeRepository.getSource(idx).setName(newName);
            buttons[idx].setText(newName);
        };
        text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                acceptName.run();
                text.dispose();
            }
        });
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.character) {
                    case '\r':
                        acceptName.run();
                        // intentional fall-through
                    case 0x1b:
                        text.dispose();
                        break;
                    default:
                        super.keyPressed(e);
                }
            }
        });
    }

    private void init() {
        for (int i = 0; i < buttons.length; i++) {
            editorStates[i] = new CodeEditor.State(codeRepository.getSource(i).getCode());
            buttons[i].setText(codeRepository.getSource(i).getName());
        }
        if (selectedSource != -1) {
            editor.restoreState(editorStates[selectedSource]);
        } else {
            selectSource(0);
        }
    }

    public String getText() {
        return editor.getText();
    }

    public CodeEditor getEditor() {
        return editor;
    }

    public void setStylesheet(Stylesheet stylesheet) {
        editor.setStylesheet(stylesheet);
    }

    private void selectSource(int idx) {
        if (selectingSource) {
            return;
        }
        selectingSource = true;

        if (selectedSource > -1) {
            buttons[selectedSource].setSelection(false);
            editorStates[selectedSource] = editor.saveState();
            codeRepository.getSource(selectedSource).setCode(editor.getText());
            codeRepository.save();
        }
        selectedSource = idx;
        settings.set(KEY_SELECTEDSOURCE, selectedSource);
        settings.save();
        if (selectedSource > -1) {
            buttons[selectedSource].setSelection(true);
            editor.restoreState(editorStates[selectedSource]);
        }
        editor.setFocus();

        selectingSource = false;
    }

    public void setErrors(List<com.asigner.kidpython.compiler.Error> errors) {
        editor.setErrors(errors);
    }

    public void clearErrors() {
        setErrors(Lists.newArrayList());
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
