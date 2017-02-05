/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.ide.preferences;

import com.programmablefun.util.Messages;
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

import com.programmablefun.ide.CodeRepository;
import com.programmablefun.ide.sync.LocalPersistenceStrategy;
import com.programmablefun.ide.sync.SyncService;

import static com.programmablefun.util.Messages.Key.Preferences_CodeRepository;

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
