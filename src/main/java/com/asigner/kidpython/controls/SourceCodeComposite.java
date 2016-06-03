package com.asigner.kidpython.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SourceCodeComposite extends Composite {

    private static final int BUTTONS = 10;

    private PythonEditor.State[] editorStates = new PythonEditor.State[BUTTONS];
    private Button buttons[] = new Button[BUTTONS];
    private PythonEditor editor;

    private boolean selectingSource = false;
    private int selectedSource = 0;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SourceCodeComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout(BUTTONS, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        for (int i = 0; i < BUTTONS; i++) {
            final int sourceIdx = i;
            buttons[i] = new Button(composite, SWT.TOGGLE);
            buttons[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
            buttons[i].setBounds(0, 0, 93, 29);
            buttons[i].setText(Integer.toString(i + 1));
            buttons[i].addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectSource(sourceIdx);
                }
            });
        }

        editor = new PythonEditor(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        selectSource(0);
    }


    private void selectSource(int idx) {
        if (selectingSource) {
            return;
        }
        selectingSource = true;

        buttons[selectedSource].setSelection(false);
        editorStates[selectedSource] = editor.saveState();
        selectedSource = idx;
        buttons[selectedSource].setSelection(true);
        editor.restoreState(editorStates[selectedSource]);
        editor.setFocus();

        selectingSource = false;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }


}
