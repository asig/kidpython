// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.runtime.FuncValue;
import com.asigner.kidpython.runtime.VarType;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.google.common.collect.Sets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.Set;
import java.util.stream.Collectors;

public class CallStackComposite extends Composite {

    private static final Object SEPARATOR = new Object();
    private static final Set<VarType> VAR_TYPES = Sets.newHashSet(VarType.REGULAR, VarType.PARAMETER);

    private final Table table;

    private VirtualMachine virtualMachine;
    private VirtualMachine.EventListener listener;

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
            col.setWidth(100);
        }
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        // Set up necessary listeners for custom paint items
        table.addListener(SWT.EraseItem, event -> {
            if (event.item.getData() == SEPARATOR) {
                // Don't do the default foreground painting for separators
                event.detail &= ~SWT.FOREGROUND;
            }
        });
        table.addListener(SWT.PaintItem, event -> {
            if (event.item.getData() == SEPARATOR) {
                Rectangle clientRect = table.getClientArea();
                GC gc = event.gc;
                int y = event.y + event.height/2;
                gc.drawLine(clientRect.x, y, clientRect.x + clientRect.width, y);
            }
        });

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
                for (String varName : curFrame.getVarNames(VAR_TYPES).stream().sorted().collect(Collectors.toList())) {
                    new TableItem(table, SWT.NONE)
                            .setText(new String[]{varName, curFrame.getVar(varName).asLiteral()});
                }
                curFrame = curFrame.getParent();
                if (curFrame != null) {
                    new TableItem(table, SWT.NONE).setData(SEPARATOR);
                }
            }
        });
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
