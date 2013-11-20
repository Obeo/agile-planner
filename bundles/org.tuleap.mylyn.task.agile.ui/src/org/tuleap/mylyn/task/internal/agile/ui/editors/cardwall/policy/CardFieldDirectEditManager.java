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

/**
 * {@link DirectEditManager} for String Field direct edting.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldDirectEditManager extends DirectEditManager {

	/**
	 * The label.
	 */
	private Label label;

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
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	@Override
	protected void initCellEditor() {
		String initialLabelText = label.getText();
		getCellEditor().setValue(initialLabelText);
	}

}
