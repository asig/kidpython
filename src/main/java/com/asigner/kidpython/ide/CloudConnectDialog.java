package com.asigner.kidpython.ide;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Label;

public class CloudConnectDialog extends Dialog {

	protected Object result;
	protected Shell shlCloudSync;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CloudConnectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
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


		CLabel lblNewLabel = new CLabel(grpServices, SWT.NONE);
		lblNewLabel.setText("Dropbox");

		Button btnCheckButton = new Button(grpServices, SWT.CHECK);
		btnCheckButton.setText("Connected");

		Button btnNewButton = new Button(grpServices, SWT.NONE);
		btnNewButton.setText("New Button");
		new Label(grpServices, SWT.NONE);
		new Label(grpServices, SWT.NONE);

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

}
