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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.DateWidget;

/**
 * {@link CellEditorLocator} for String Field direct edit.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardDateFieldCellEditorLocator implements CellEditorLocator {

	/**
	 * The field label.
	 */
	private Figure label;

	/**
	 * Constructor.
	 *
	 * @param label
	 *            the field label
	 */
	public CardDateFieldCellEditorLocator(Figure label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	@Override
	public void relocate(CellEditor celleditor) {
		DateWidget dateWidget = (DateWidget)celleditor.getControl();
		Point pref = dateWidget.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = label.getBounds().getCopy();
		label.translateToAbsolute(rect);
		// To make sure the widget is accessible when it's on the right of the screen,
		// We locate its right at the current values right
		dateWidget.setBounds(rect.x - pref.x + rect.width, rect.y - 1, pref.x + 1, pref.y + 1);
	}
}
