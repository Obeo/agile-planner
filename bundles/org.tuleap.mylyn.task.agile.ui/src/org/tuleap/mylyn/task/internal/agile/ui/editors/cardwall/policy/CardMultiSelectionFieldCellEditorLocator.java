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

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;

/**
 * {@link CellEditorLocator} for Multi-selection field direct edition.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardMultiSelectionFieldCellEditorLocator implements CellEditorLocator {

	/**
	 * The field label.
	 */
	private Label label;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            the field label
	 */
	public CardMultiSelectionFieldCellEditorLocator(Label label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	@Override
	public void relocate(CellEditor celleditor) {
		Table table = (Table)celleditor.getControl();
		Point pref = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = label.getTextBounds().getCopy();
		label.translateToAbsolute(rect);
		table.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
	}

}
