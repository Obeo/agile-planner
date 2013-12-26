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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.swt.widgets.Composite;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util.CardMultiSelectionFieldEditor;

/**
 * {@link DirectEditManager} for Multi-selction Field direct editing.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardMultiSelectionFieldDirectEditManager extends DirectEditManager {

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
	 *            The attribute
	 */
	public CardMultiSelectionFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
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
		CardMultiSelectionFieldEditor editor = (CardMultiSelectionFieldEditor)getCellEditor();
		List<String> values = attribute.getValues();
		editor.setValue(values);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.DirectEditManager#createCellEditorOn(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CellEditor createCellEditorOn(Composite composite) {
		Map<String, String> options = attribute.getOptions();
		List<String> values = attribute.getValues();
		String[] items = new String[options.size()];
		int i = 0;
		for (Entry<String, String> entry : options.entrySet()) {
			items[i++] = entry.getValue();
		}
		CardMultiSelectionFieldEditor editor = new CardMultiSelectionFieldEditor(composite, options, values);
		return editor;
	}

}
