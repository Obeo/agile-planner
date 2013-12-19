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

import java.util.Date;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * {@link DirectEditManager} for date direct editing.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardDateFieldDirectEditManager extends DirectEditManager {

	/**
	 * The label.
	 */
	private final TaskAttribute attribute;

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            The source
	 * @param editorType
	 *            The editor type
	 * @param locator
	 *            The locator
	 * @param attribute
	 *            the attribute
	 */
	public CardDateFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator, TaskAttribute attribute) {
		super(source, editorType, locator);
		this.attribute = attribute;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	@Override
	protected void initCellEditor() {
		CellEditor editor = getCellEditor();
		String selectedValue = attribute.getValue();

		Long longDate = Long.valueOf(selectedValue);
		Date date = new Date(longDate.longValue());
		editor.setValue(date);
	}
}
