package com.asigner.kidpython.ide.controls;

import com.asigner.kidpython.ide.Settings;
import com.asigner.kidpython.ide.controls.editor.CodeEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SourceCodeComposite extends Composite {

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
    public SourceCodeComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        settings = Settings.load();
        this.addDisposeListener(disposeEvent -> {
            settings.getSource(selectedSource).setCode(editor.getText());
            settings.save();
        });
        Thread saver = new Thread(() -> {
            for(;;) {
                try {
                    Thread.sleep(5000);
                    settings.save();
                } catch (InterruptedException ignored) {
                }
            }});
        saver.setDaemon(true);
        saver.start();
        int nofSources = settings.getNofSources();

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout(nofSources, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        editorStates = new CodeEditor.State[nofSources];
        buttons = new Button[nofSources];
        for (int i = 0; i < nofSources; i++) {
            final int sourceIdx = i;
            editorStates[i] = new CodeEditor.State(settings.getSource(i).getCode());
            buttons[i] = new Button(composite, SWT.TOGGLE);
            buttons[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
            buttons[i].setBounds(0, 0, 93, 29);
            buttons[i].setText(settings.getSource(i).getName());
            buttons[i].addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectSource(sourceIdx);
                }
            });
        }

        editor = new CodeEditor(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        selectSource(settings.getSelectedSource());
    }

    private void selectSource(int idx) {
        if (selectingSource) {
            return;
        }
        selectingSource = true;

        if (selectedSource > -1) {
            buttons[selectedSource].setSelection(false);
            editorStates[selectedSource] = editor.saveState();
            settings.getSource(selectedSource).setCode(editor.getText());
            settings.save();
        }
        selectedSource = idx;
        settings.setSelectedSource(selectedSource);
        if (selectedSource > -1) {
            buttons[selectedSource].setSelection(true);
            editor.restoreState(editorStates[selectedSource]);
        }
        editor.setFocus();

        selectingSource = false;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }


}
