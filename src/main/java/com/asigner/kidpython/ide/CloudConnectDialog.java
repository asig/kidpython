package com.asigner.kidpython.ide;

import com.asigner.kidpython.ide.sync.SyncService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class CloudConnectDialog extends Dialog {

    private Object result;
    private Shell shlCloudSync;
    private CodeRepository codeRepository;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public CloudConnectDialog(Shell parent, int style) {
        super(parent, style);
        setText("Preferences");
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
        createContents();
        shlCloudSync.open();
        shlCloudSync.layout();
        Display display = getParent().getDisplay();
        while (!shlCloudSync.isDisposed()) {
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
        shlCloudSync = new Shell(getParent(), getStyle());
        shlCloudSync.setSize(450, 300);
        shlCloudSync.setText("Cloud Sync");
        shlCloudSync.setLayout(new GridLayout(1, false));

        Group grpServices = new Group(shlCloudSync, SWT.NONE);
        grpServices.setLayout(new GridLayout(5, false));
        grpServices.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
        grpServices.setText("Services");

        for (SyncService service : SyncService.ALL) {
            addService(grpServices, service);
        }

        Composite composite = new Composite(shlCloudSync, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        composite.setBounds(0, 0, 64, 64);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));

        Button btnClose = new Button(composite, SWT.NONE);
        btnClose.setText("Close");
        btnClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shlCloudSync.dispose();
            }
        });

    }

    private void addService(Composite parent, SyncService service) {
        CLabel lblNewLabel = new CLabel(parent, SWT.NONE);
        lblNewLabel.setText(service.getName());

        Button btnCheckButton = new Button(parent, SWT.CHECK);
        btnCheckButton.setText("Connected");
        btnCheckButton.setSelection(service.isConnected());

        Button btn = new Button(parent, SWT.NONE);
        btn.setText(service.isConnected() ? "Disconnect" : "Connect");
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (service.isConnected()) {
                    service.disconnect();
                } else {
                    service.connect();
                    if (service.isConnected()) {
                        // Disconnect all other services
                        for (SyncService s : SyncService.ALL) {
                            if (s != service) {
                                s.disconnect();
                            }
                        }
                        // set the new one
                        codeRepository.switchStrategy(service.getPersistenceStrategy());
                    }
                }
                btnCheckButton.setSelection(service.isConnected());
                btn.setText(service.isConnected() ? "Disconnect" : "Connect");
            }
        });
    }
}
