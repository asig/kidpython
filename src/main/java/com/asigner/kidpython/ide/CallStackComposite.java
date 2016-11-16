// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.runtime.FuncValue;
import com.asigner.kidpython.runtime.VirtualMachine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.stream.Collectors;

public class CallStackComposite extends Composite {

    private VirtualMachine virtualMachine;
    private VirtualMachine.EventListener listener;

    private final Table table;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public CallStackComposite(Composite parent, int style) {
        super(parent, style);

        this.listener = new VirtualMachine.EventListener() {
            @Override
            public void vmStateChanged() {
                if (virtualMachine.getState() == VirtualMachine.State.PAUSED) {
                    fillVariables();
                } else {
                    clearVariables();
                }
            }

            @Override
            public void newStatementReached(Node stmt) { }

            @Override
            public void programSet() {
                clearVariables();
            }

            @Override
            public void reset() {
                clearVariables();
            }

            @Override
            public void enteringFunction(FuncValue func) { }

            @Override
            public void leavingFunction() { }
        };

        setLayout(new FillLayout());

        table = new Table(this, SWT.NONE);
        for (String title : new String[] { "Name", "Value"}) {
            TableColumn col = new TableColumn(table, SWT.NONE);
            col.setText(title);
            col.setWidth(50);
        }
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        if (virtualMachine == this.virtualMachine) {
            return;
        }
        if (this.virtualMachine != null) {
            this.virtualMachine.removeListener(listener);
        }
        this.virtualMachine = virtualMachine;
        this.virtualMachine.addListener(listener);
    }

    private void clearVariables() {
        getDisplay().asyncExec(table::removeAll);
    }

    private void fillVariables() {
        final VirtualMachine.Frame frame = virtualMachine.getCurrentFrame();
        getDisplay().asyncExec(() -> {
            table.removeAll();
            VirtualMachine.Frame curFrame = frame;
            while (curFrame != null) {
                for (String varName : curFrame.getVarNames().stream().sorted().collect(Collectors.toList())) {
                    new TableItem(table, SWT.NONE)
                            .setText(new String[]{varName, curFrame.getVar(varName).toString()});
                }
                curFrame = curFrame.getParent();
                if (curFrame != null) {
                    new TableItem(table, SWT.NONE)
                            .setText(new String[]{"------------------"});
                }
            }
        });
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
