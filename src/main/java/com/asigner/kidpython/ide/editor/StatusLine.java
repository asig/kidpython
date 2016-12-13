package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.ide.util.SWTUtils;
import com.asigner.kidpython.util.Messages;
import com.google.common.collect.Lists;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import java.util.List;

public class StatusLine extends Composite {

    private final List<Label> labels = Lists.newArrayList();
    private final Font statusFont;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public StatusLine(Composite parent, int style) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);

        statusFont = new Font(Display.getDefault(), "Roboto Mono", SWTUtils.scaleFont(8), SWT.NONE);

        Label label = makeLabel("");

        label = makeLabel("");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

        label = makeLabel("");
        label.setAlignment(SWT.RIGHT);
    }

    private Label makeLabel(String text) {
        Label label = new Label(this, SWT.NONE);
        label.setText(text);
        label.setFont(statusFont);
        labels.add(label);
        return label;
    }

    public void setStylesheet(Stylesheet stylesheet) {
        this.setBackground(stylesheet.getDefaultBackground());
        this.labels.forEach(l -> l.setForeground(stylesheet.getStyle(Stylesheet.Entity.LINE_NUMBER_FG).getFg()));
        this.getDisplay().asyncExec(this::redraw);
    }

    public void setPosition(int line, int col) {
        String s = String.format(Messages.get(Messages.Key.StatusLine_Position), line, col);
        labels.get(0).setText(s);
        this.pack();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
