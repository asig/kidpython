package com.asigner.kidpython.ide.sync;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DropboxConnectDialog extends Dialog {

    private boolean result;
    protected Shell shell;
    private Text codeCtrl;

    private String authorizeUrl;
    private String code;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public DropboxConnectDialog(Shell parent, int style) {
        super(parent, style);
        setText("Connect to Dropbox");
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getCode() {
        return code;
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public boolean open() {
        result = false;
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(800, 400);
        shell.setText(getText());
        shell.setLayout(new GridLayout(1, false));

        Group grpGoTo = new Group(shell, SWT.NONE);
        grpGoTo.setText("1. Go to Dropbox");
        grpGoTo.setLayout(new GridLayout(1, false));
        grpGoTo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        CLabel lblNewLabel = new CLabel(grpGoTo, SWT.NONE);
        lblNewLabel.setText("A browser window should have opened that shows the Dropbox connect screen.");

        CLabel lblNewLabel_1 = new CLabel(grpGoTo, SWT.NONE);
        lblNewLabel_1.setText("If not, please visit this URL:");

        Text link = new Text(grpGoTo, SWT.NONE);
        link.setText(authorizeUrl);

        Group grpLogIn = new Group(shell, SWT.NONE);
        grpLogIn.setText("2. Log in and grant access");
        grpLogIn.setLayout(new GridLayout(1, false));
        grpLogIn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        CLabel lblNewLabel_2 = new CLabel(grpLogIn, SWT.NONE);
        lblNewLabel_2.setText("In the browser window, grant Dropbox access to \"Programmable Fun\".");

        Group grpPastAccess = new Group(shell, SWT.NONE);
        grpPastAccess.setText("3. Paste Access code");
        grpPastAccess.setLayout(new GridLayout(1, false));
        grpPastAccess.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        CLabel lblNewLabel_3 = new CLabel(grpPastAccess, SWT.NONE);
        lblNewLabel_3.setText("Please enter the Code that you got from Dropbox:");

        codeCtrl = new Text(grpPastAccess, SWT.BORDER);
        codeCtrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button btnConnect = new Button(shell, SWT.NONE);
        btnConnect.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        btnConnect.setText("Connect");
        btnConnect.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                result = true;
                code = codeCtrl.getText();
                shell.dispose();
            }
        });

    }
}
