package com.asigner.kidpython.ide.settings;

import com.asigner.kidpython.util.Messages;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.asigner.kidpython.ide.CodeRepository;
import com.asigner.kidpython.ide.sync.LocalPersistenceStrategy;
import com.asigner.kidpython.ide.sync.SyncService;

import static com.asigner.kidpython.util.Messages.Key.Preferences_CodeRepository;

public class RepositoryPrefPage extends PreferencePage {

	private final CodeRepository codeRepository;
	
	public RepositoryPrefPage(CodeRepository codeRepository) {
	    super(Messages.get(Preferences_CodeRepository));
		this.codeRepository = codeRepository;
		noDefaultAndApplyButton();
	}

    @Override
	protected Control createContents(Composite parent) {
        Composite services = new Composite(parent, SWT.NONE);
        services.setLayout(new GridLayout(5, false));
        services.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

        for (SyncService service : SyncService.ALL) {
            addService(services, service);
        }

		return services;
	}

    private void addService(Composite parent, SyncService service) {
        CLabel lblNewLabel = new CLabel(parent, SWT.NONE);
        lblNewLabel.setText(service.getName());

        Button btnCheckButton = new Button(parent, SWT.CHECK);
        btnCheckButton.setEnabled(false);
        btnCheckButton.setText("Connected");
        btnCheckButton.setSelection(service.isConnected());

        Button btn = new Button(parent, SWT.NONE);
        btn.setText(service.isConnected() ? "Disconnect" : "Connect");
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (service.isConnected()) {
                    service.disconnect();
                    codeRepository.switchStrategy(new LocalPersistenceStrategy());
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
