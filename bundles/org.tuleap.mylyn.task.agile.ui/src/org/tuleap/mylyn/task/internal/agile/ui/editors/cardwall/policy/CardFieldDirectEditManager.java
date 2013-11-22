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
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * {@link DirectEditManager} for String Field direct edting.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldDirectEditManager extends DirectEditManager {

	/**
	 * The label.
	 */
	private final Label label;

	/**
	 * The validator to use for the cell editor.
	 */
	private ICellEditorValidator validator;

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            The source
	 * @param editorType
	 *            The editor type
	 * @param locator
	 *            The locator
	 * @param label
	 *            The label
	 */
	public CardFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator, Label label) {
		super(source, editorType, locator);
		this.label = label;
	}

	/**
	 * Constructor with a validator.
	 * 
	 * @param source
	 *            The source
	 * @param editorType
	 *            The editor type
	 * @param locator
	 *            The locator
	 * @param label
	 *            The label
	 * @param validator
	 *            The validator to use for the cell editor, if <code>null</code> no validatin will be
	 *            performed.
	 */
	public CardFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator, Label label, ICellEditorValidator validator) {
		this(source, editorType, locator, label);
		this.validator = validator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	@Override
	protected void initCellEditor() {
		String initialLabelText = label.getText();
		getCellEditor().setValue(initialLabelText);
		getCellEditor().setValidator(validator);
	}

}
