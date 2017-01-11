package com.asigner.kidpython.ide;

import com.asigner.kidpython.ide.editor.CodeEditor;
import com.asigner.kidpython.ide.editor.Stylesheet;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import java.util.List;
import java.util.Set;

public class SourceCodeComposite extends Composite {

    public interface SourceSelectionChangedListener {
        void newSourceSelected(int idx);
    }

    private final List<SourceSelectionChangedListener> listeners = Lists.newArrayList();

    private CodeRepository codeRepository;
    private Settings settings;

    private boolean selectingSource = false;
    private int selectedSource = -1;

    private CTabFolder tabFolder;
    private CodeEditor.State[] editorStates;
    private CodeEditor[] editors;
    private CTabItem[] tabItems;

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
        codeRepository.addListener(newStrategy -> initEditorsFromRepo());

        this.addDisposeListener(disposeEvent -> {
            codeRepository.getSource(selectedSource).setCode(editors[selectedSource].getText());
            codeRepository.save();
        });
        int nofSources = codeRepository.getNofSources();

        setLayout(new FillLayout(SWT.HORIZONTAL));
        editors = new CodeEditor[nofSources];
        editorStates = new CodeEditor.State[nofSources];
        tabFolder = new CTabFolder(this, SWT.BORDER);
        tabFolder.setSimple(false);
        tabItems = new CTabItem[nofSources];
        for (int i = 0; i < nofSources; i++) {
            editorStates[i] = new CodeEditor.State(codeRepository.getSource(i).getCode());
            CodeEditor editor = new CodeEditor(tabFolder, SWT.NONE);
            editor.restoreState(editorStates[i]);
            CTabItem ti = new CTabItem(tabFolder, SWT.NONE);
            ti.setText(codeRepository.getSource(i).getName());
            ti.setControl(editor);
            ti.setData("index", i);

            int finalI = i;
            editor.addModifyListener(event -> {
                codeRepository.getSource(finalI).setCode(editor.getText());
                codeRepository.save();
            });

            tabItems[i] = ti;
            editors[i] = editor;
        }

        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectSource(tabFolder.getSelectionIndex());
            }
        });
        tabFolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                CTabFolder curFolder = (CTabFolder)arg0.widget;
                Point eventLocation = new Point(arg0.x, arg0.y);
                CTabItem item = curFolder.getItem(eventLocation);
                if (item != null) {
                    changeName((Integer)item.getData("index"));
                }
            }
        });

        selectSource(settings.getSelectedSourceIndex());
    }

    public void addListener(SourceSelectionChangedListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(SourceSelectionChangedListener listener) {
        this.listeners.remove(listener);
    }

    public void setActiveLine(int line) {
        editors[selectedSource].setActiveLine(line);
    }

    public void setSourceCodeSelectionEnabled(boolean enabled) {
        tabFolder.setEnabled(enabled);
    }

    private void changeName(int idx) {
        CTabItem tabItem = tabItems[idx];
        Text text = new Text(tabItem.getParent(), SWT.SINGLE);
        Rectangle r = tabItem.getBounds();
        text.setBounds(r);
        text.setText(tabItem.getText());
        text.setVisible(true);
        text.moveAbove(tabFolder);
        text.setSelection(0, tabItem.getText().length());
        text.setFocus();
        Runnable acceptName = () -> {
            String newName = text.getText() ;
            codeRepository.getSource(idx).setName(newName);
            tabItem.setText(newName);
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

    private void initEditorsFromRepo() {
        for (int i = 0; i < editors.length; i++) {
            tabItems[i].setText(codeRepository.getSource(i).getName());
            editors[i].restoreState(new CodeEditor.State(codeRepository.getSource(i).getCode()));
        }
        if (selectedSource < 0) {
            selectSource(0);
        }
    }

    public String getText() {
        return editors[selectedSource].getText();
    }

    public void setStylesheet(Stylesheet stylesheet) {
        for (CodeEditor editor : editors) {
            editor.setStylesheet(stylesheet);
        }
    }

    public void setWellKnownWords(Set<String> words) {
        for (CodeEditor editor : editors) {
            editor.setWellKnownWords(words);
        }
    }

    private void selectSource(int idx) {
        if (selectingSource) {
            return;
        }
        selectingSource = true;
        tabFolder.setSelection(idx);

        if (selectedSource > -1) {
            codeRepository.getSource(selectedSource).setCode(editors[selectedSource].getText());
            codeRepository.save();
        }
        selectedSource = idx;
        settings.setSelectedSourceIndex(selectedSource);
        settings.save();
        if (selectedSource > -1) {
            listeners.forEach(l -> l.newSourceSelected(idx));
        }
        editors[selectedSource].setFocus();

        selectingSource = false;
    }

    public void setErrors(List<com.asigner.kidpython.compiler.Error> errors) {
        editors[selectedSource].setErrors(errors);
    }

    public void clearErrors() {
        setErrors(Lists.newArrayList());
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
