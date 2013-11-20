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

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * {@link ComboBoxCellEditor} that correctly notifies to allow direct edit.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @see <a
 *      href="http://www.eclipse.org/forums/index.php/t/69000/">http://www.eclipse.org/forums/index.php/t/69000/</a>
 * @see <a
 *      href="https://bugs.eclipse.org/bugs/show_bug.cgi?format=multiple&id=85936">https://bugs.eclipse.org/bugs/show_bug.cgi?format=multiple&id=85936</a>
 */
public class CardwallComboBoxCellEditor extends ComboBoxCellEditor {

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent
	 * @param editorValues
	 *            The values of the combo.
	 */
	public CardwallComboBoxCellEditor(Composite parent, String[] editorValues) {
		super(parent, editorValues);
	}

	/**
	 * Overridden to avoid a bug in ComboBoxCellEditor.
	 * 
	 * @param value
	 *            The value to set.
	 */
	@Override
	protected void doSetValue(Object value) {
		super.doSetValue(value);
		fireEditorValueChanged(true, true);
	}
}
