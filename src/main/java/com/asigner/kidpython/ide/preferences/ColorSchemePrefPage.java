package com.asigner.kidpython.ide.preferences;

import com.asigner.kidpython.ide.Settings;
import com.asigner.kidpython.ide.editor.CodeEditor;
import com.asigner.kidpython.ide.editor.Stylesheet;
import com.asigner.kidpython.ide.editor.StylesheetRepository;
import com.asigner.kidpython.util.Messages;
import com.google.common.collect.Sets;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import java.io.File;
import java.util.Set;

import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme;
import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme_AddColorSchemes;
import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme_AvailableColorSchemes;
import static com.asigner.kidpython.util.Messages.Key.Preferences_ColorScheme_Preview;

public class ColorSchemePrefPage extends PreferencePage {

    private final StylesheetRepository original;
    private final StylesheetRepository workingCopy;
    private final Set<String> filesToCopy = Sets.newHashSet();

    private final Set<String> wellKnownWords;

    private boolean initialized;
    private List list;

    private Stylesheet selectedStylesheet;

    public ColorSchemePrefPage(StylesheetRepository repository, Set<String> wellKnownWords) {
        super(Messages.get(Preferences_ColorScheme));
        this.wellKnownWords = wellKnownWords;
        this.original = repository;
        this.workingCopy = new StylesheetRepository().replaceWith(this.original);
        noDefaultAndApplyButton();
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        Group group = new Group(composite, SWT.NONE);
        group.setText(Messages.get(Preferences_ColorScheme_AvailableColorSchemes));
        group.setLayout(new GridLayout(1, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        list = new List(group, SWT.BORDER);
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Button btnNewButton = new Button(group, SWT.NONE);
        btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        btnNewButton.setText(Messages.get(Preferences_ColorScheme_AddColorSchemes));

        Group group_1 = new Group(composite, SWT.NONE);
        group_1.setText(Messages.get(Preferences_ColorScheme_Preview));
        group_1.setLayout(new FillLayout(SWT.HORIZONTAL));
        group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        CodeEditor codeEditor = new CodeEditor(group_1, SWT.NONE);
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

        for (Stylesheet s : workingCopy.getAll()) {
            list.add(s.getName());
        }
        list.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (list.getSelectionCount() > 0) {
                    String selected = list.getSelection()[0];
                    selectedStylesheet = workingCopy.get(selected);
                    codeEditor.setStylesheet(selectedStylesheet);
                }
            }
        });

        String selected =  Settings.getInstance().getSelectedStylesheet();
        if (selected == null) {
            selected = list.getItem(0);
        }
        list.setSelection(new String[] { selected });
        // Changing the selection does not result in an event... so let's set it manually
        selectedStylesheet = workingCopy.get(selected);
        codeEditor.setStylesheet(selectedStylesheet);

        initialized = true;

        return composite;
    }

    @Override
    public boolean performOk() {
        if (initialized) {
            // TODO: Copy files
            original.replaceWith(workingCopy);
            Settings.getInstance().setSelectedstylesheet(selectedStylesheet.getName());
        }
        return true;
    }

    private void copyTheme(File file) {


    }
}
