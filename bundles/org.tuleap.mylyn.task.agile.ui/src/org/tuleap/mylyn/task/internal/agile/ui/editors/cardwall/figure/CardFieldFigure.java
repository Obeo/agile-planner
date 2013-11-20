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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * Figure of a configurable card field.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldFigure extends Figure {

	/**
	 * The panel.
	 */
	private final Panel panel;

	/**
	 * The field label.
	 */
	private final Label label;

	/**
	 * The field label that displays the field value.
	 */
	private final Label valueLabel;

	/**
	 * The actual field values.
	 */
	private List<String> values;

	/**
	 * Constructor.
	 */
	public CardFieldFigure() {
		panel = new Panel();
		ToolbarLayout l = new ToolbarLayout(true);
		l.setStretchMinorAxis(true);
		panel.setLayoutManager(l);
		add(panel);

		// panel.setBackgroundColor(ColorConstants.lightGreen);
		label = new Label();
		panel.add(label);
		panel.add(new Label(": ")); //$NON-NLS-1$
		valueLabel = new Label();
		panel.add(valueLabel);

		GridLayout layout = new GridLayout();
		setLayoutManager(layout);
		layout.setConstraint(this, new GridData(SWT.FILL, SWT.TOP, true, false));
	}

	/**
	 * Set the field data.
	 * 
	 * @param lbl
	 *            The label
	 * @param someValues
	 *            The values
	 */
	public void setField(String lbl, List<String> someValues) {
		this.label.setText(lbl);
		setValues(someValues);
	}

	/**
	 * Sets the values of this field.
	 * 
	 * @param values
	 *            The values to set.
	 */
	public void setValues(List<String> values) {
		this.values = Lists.newArrayList(values);
		StringBuilder b = new StringBuilder();
		if (!values.isEmpty()) {
			Iterator<String> it = values.iterator();
			b.append(it.next());
			while (it.hasNext()) {
				b.append(", ").append(it.next()); //$NON-NLS-1$
			}
		}
		this.valueLabel.setText(b.toString());
	}

	/**
	 * Provides access to the value label, for direct editing.
	 * 
	 * @return The value label.
	 */
	public Label getValueLabel() {
		return valueLabel;
	}

	/**
	 * Provides the actual values displayed by the field. Modifying the retruned list has no effect on this
	 * figure.
	 * 
	 * @return a List, never <code>null</code> but possibly empty, which is a copy of this figure's list of
	 *         values.
	 */
	public List<String> getValues() {
		if (values == null) {
			return Collections.emptyList();
		}
		return Lists.newArrayList(values);
	}
}
