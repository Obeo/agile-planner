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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;

/**
 * {@link CellEditorLocator} for String Field direct edit.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardBoundFieldCellEditorLocator implements CellEditorLocator {

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
	public CardBoundFieldCellEditorLocator(Figure label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	@Override
	public void relocate(CellEditor celleditor) {
		CCombo combo = (CCombo)celleditor.getControl();
		Point pref = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = label.getBounds().getCopy();
		label.translateToAbsolute(rect);
		combo.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
	}

}
