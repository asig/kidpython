package com.asigner.kidpython.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SourceCodeComposite extends Composite {

    private static final int BUTTONS = 10;

    private String[] sources = new String[BUTTONS];
    private Button buttons[] = new Button[BUTTONS];
    private StyledText styledText;

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
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));


        for (int i = 0; i < BUTTONS; i++) {
            final int sourceIdx = i;
            buttons[i] = new Button(composite, SWT.TOGGLE);
            buttons[i].setBounds(0, 0, 93, 29);
            buttons[i].setText(Integer.toString(i + 1));
            buttons[i].addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectSource(sourceIdx);
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub

                }
            });

            sources[i] = "";
        }

        styledText = new StyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        selectSource(0);
    }

    private void selectSource(int idx) {
        if (selectingSource) {
            return;
        }
        selectingSource = true;

        buttons[selectedSource].setSelection(false);
        sources[selectedSource] = styledText.getText();
        selectedSource = idx;
        buttons[selectedSource].setSelection(true);
        styledText.setText(sources[selectedSource]);

        selectingSource = false;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
