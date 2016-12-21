package com.asigner.kidpython.ide.settings;

import com.asigner.kidpython.ide.Settings;
import com.asigner.kidpython.ide.editor.CodeEditor;
import com.asigner.kidpython.ide.editor.Stylesheet;
import com.asigner.kidpython.util.Messages;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import java.util.Set;

import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme;
import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme_AvailableColorSchemes;
import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme_Preview;

public class ColorSchemePrefPage extends PreferencePage {

    private final Set<String> wellKnownWords;
    private final java.util.List<Stylesheet> availableStylesheets;

    private Stylesheet selectedStylesheet;

    public ColorSchemePrefPage(java.util.List<Stylesheet> availableStylesheets, Set<String> wellKnownWords) {
        super(Messages.get(Preferences_ColorScheme));
        this.wellKnownWords = wellKnownWords;
        this.availableStylesheets = availableStylesheets;
        noDefaultAndApplyButton();
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

        Label lblTitle = new Label(composite, SWT.NONE);
        lblTitle.setText(Messages.get(Preferences_ColorScheme_AvailableColorSchemes));

        Group grpPreview = new Group(composite, SWT.NONE);
        grpPreview.setLayout(new FillLayout(SWT.HORIZONTAL));
        grpPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
        grpPreview.setText(Messages.get(Preferences_ColorScheme_Preview));

        CodeEditor codeEditor = new CodeEditor(grpPreview, SWT.NONE);
        codeEditor.setWellKnownWords(wellKnownWords);
        codeEditor.restoreState(new CodeEditor.State(
                "// Draw a bunch of boxes \n" +
                        "// ---------------------\n" +
                        "ANGLE=15\n" +
                        "COLORSTEP = 255/(360/ANGLE)\n" +
                        "\n" +
                        "function box()\n" +
                        "  for i in 0 .. 3 do\n" +
                        "    turtle.move(100)\n" +
                        "    turtle.turn(90)\n" +
                        "  end\n" +
                        "end\n" +
                        "\n" +
                        "for i in 1 .. 360/ANGLE do\n" +
                        "  turtle.penColor(255-i*COLORSTEP,0, i*COLORSTEP)\n" +
                        "  box()\n" +
                        "  turtle.turn(ANGLE)\n" +
                        "end\n" +
                        "println(\"All boxes drawn!\")"
        ));

        List list = new List(composite, SWT.BORDER);
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        for (Stylesheet s : availableStylesheets) {
            list.add(s.getName());
        }
        int idx = Settings.getInstance().getSelectedStylesheetIndex();
        selectedStylesheet = availableStylesheets.get(idx);
        list.select(idx);
        codeEditor.setStylesheet(selectedStylesheet);
        list.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int idx = list.getSelectionIndex();
                if (idx > -1) {
                    selectedStylesheet = availableStylesheets.get(idx);
                    codeEditor.setStylesheet(selectedStylesheet);
                }
            }
        });

        return composite;
    }

    public Stylesheet getSelectedStylesheet() {
        return selectedStylesheet;
    }
}
