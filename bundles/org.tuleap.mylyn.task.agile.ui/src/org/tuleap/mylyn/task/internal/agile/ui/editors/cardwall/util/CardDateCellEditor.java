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

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.DateWidget;

/**
 * The card cell editor that correctly notifies to allow direct editing dates on cards.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardDateCellEditor extends CellEditor {

	/**
	 * The date widget.
	 */
	private DateWidget dateTime;

	/**
	 * The constructor.
	 *
	 * @param parent
	 *            The parent
	 */
	public CardDateCellEditor(final Composite parent) {
		this(parent, SWT.DROP_DOWN);
	}

	/**
	 * The constructor.
	 *
	 * @param parent
	 *            The parent
	 * @param style
	 *            The style
	 */
	public CardDateCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Control createControl(Composite composite) {

		dateTime = new DateWidget(composite, SWT.FLAT, "Date", false, 1); //$NON-NLS-1$
		updateDate();
		dateTime.addWidgetSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar cal = dateTime.getDate();
				if (cal != null) {
					Date value = cal.getTime();
					if (!value.equals(doGetValue())) {
						doSetValue(value);
					}
				} else {
					if (doGetValue() != null) {
						doSetValue(null);
					}
					dateTime.setDate(null);
				}
				applyEditorValueAndDeactivate();
			}
		});

		return dateTime;

	}

	@Override
	protected Object doGetValue() {
		if (dateTime != null && dateTime.getDate() != null) {
			return dateTime.getDate().getTime();
		}
		return null;
	}

	@Override
	protected void doSetValue(Object theValue) {
		if (dateTime != null && theValue instanceof Date) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date)theValue);
			dateTime.setDate(c);
		}
		fireEditorValueChanged(true, true);

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		dateTime.setFocus();
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
	 * Update the date.
	 */
	private void updateDate() {
		if (doGetValue() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date)doGetValue());
			dateTime.setDate(cal);
		}
	}

}
