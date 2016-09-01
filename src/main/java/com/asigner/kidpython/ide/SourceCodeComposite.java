package com.asigner.kidpython.ide;

import com.asigner.kidpython.ide.editor.CodeEditor;
import com.asigner.kidpython.ide.editor.Stylesheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.util.List;

public class SourceCodeComposite extends Composite {

    private CodeRepository codeRepository;

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
        }

        editor = new CodeEditor(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        editor.addModifyListener(event -> {
            codeRepository.getSource(selectedSource).setCode(editor.getText());
            codeRepository.save();
        });

        selectSource(codeRepository.getSelectedSource());
    }

    private void init() {
        for (int i = 0; i < buttons.length; i++) {
            editorStates[i] = new CodeEditor.State(codeRepository.getSource(i).getCode());
            buttons[i].setText(codeRepository.getSource(i).getName());
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
        codeRepository.setSelectedSource(selectedSource);
        codeRepository.save();
        if (selectedSource > -1) {
            buttons[selectedSource].setSelection(true);
            editor.restoreState(editorStates[selectedSource]);
        }
        editor.setFocus();

        selectingSource = false;
    }

    public void setErrors(List< com.asigner.kidpython.compiler.Error> errors) {

    }

    public void clearErrors() {

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
