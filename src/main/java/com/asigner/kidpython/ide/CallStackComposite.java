// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CallStackComposite extends Composite {

    private final Table table;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public CallStackComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());

        this.table = new Table(this, SWT.NONE);
        TableColumn col1 = new TableColumn(this.table, SWT.NONE);
        col1.setText("Variable");
        col1.setWidth(50);

        TableItem it1 = new TableItem(table,SWT.NONE);
        it1.setText(new String[]{"aaa"});
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
