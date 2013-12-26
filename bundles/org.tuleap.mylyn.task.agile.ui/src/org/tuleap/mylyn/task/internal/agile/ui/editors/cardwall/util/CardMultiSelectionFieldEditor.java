/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * The card cell editor that correctly notifies to allow direct editing multiple selection on cards.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardMultiSelectionFieldEditor extends CellEditor {

	/**
	 * The table widget.
	 */
	private Table table;

	/**
	 * The table Items.
	 */
	private Map<String, String> options;

	/**
	 * The table initial selected items.
	 */
	private List<String> initialValues;

	/**
	 * The table selected items.
	 */
	private final List<String> checkedItems = new ArrayList<String>();

	/**
	 * The constructor.
	 * 
	 * @param parent
	 *            The parent
	 * @param options
	 *            The options
	 * @param initialValues
	 *            The initial values
	 */
	public CardMultiSelectionFieldEditor(Composite parent, Map<String, String> options,
			List<String> initialValues) {
		this.options = options;
		this.initialValues = Lists.newArrayList(initialValues);
		create(parent);

	}

	@Override
	protected Control createControl(Composite composite) {

		table = new Table(composite, SWT.CHECK);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);

		for (Entry<String, String> entry : options.entrySet()) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData(entry.getKey(), entry.getValue());
			tableItem.setText(entry.getValue());
			if (initialValues.contains(entry.getKey())) {
				tableItem.setChecked(true);
				checkedItems.add(entry.getKey());
			}
		}

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.CHECK) {
					TableItem item = (TableItem)e.item;
					String key = getKeyByValue(options, item.getText());
					if (item.getChecked()) {
						checkedItems.add(key);
					} else {
						checkedItems.remove(key);
					}
				}
			}
		});

		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				applyEditorValueAndDeactivate();
			}
		});
		return table;

	}

	@Override
	protected Object doGetValue() {
		return checkedItems;
	}

	@Override
	protected void doSetValue(Object theValue) {
		fireEditorValueChanged(true, true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		table.setFocus();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	@Override
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	/**
	 * Apply the set value and stop displaying the widget.
	 */
	private void applyEditorValueAndDeactivate() {
		Object newValue = doGetValue();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);
		fireApplyEditorValue();
		deactivate();
	}

	/**
	 * Get the key of a value in a {@link Map}.
	 * 
	 * @param map
	 *            The map.
	 * @param value
	 *            The value to search.
	 * @param <E>
	 *            The value type
	 * @param <T>
	 *            The key type
	 * @return The corresponding key.
	 */
	private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
