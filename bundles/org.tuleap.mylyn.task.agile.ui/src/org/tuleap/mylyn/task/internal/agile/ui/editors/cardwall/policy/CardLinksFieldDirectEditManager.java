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

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardFieldEditPart;

/**
 * {@link DirectEditManager} for links Field direct editing.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardLinksFieldDirectEditManager extends DirectEditManager {

	/**
	 * The links values.
	 */
	private final List<String> values;

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
	 * @param values
	 *            The links values
	 */
	public CardLinksFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator, List<String> values) {
		super(source, editorType, locator);
		this.values = values;
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
	 * @param values
	 *            The links values
	 * @param validator
	 *            The validator to use for the cell editor, if <code>null</code> no validation will be
	 *            performed.
	 */
	public CardLinksFieldDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator, List<String> values, ICellEditorValidator validator) {
		this(source, editorType, locator, values);
		this.validator = validator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	@Override
	protected void initCellEditor() {
		StringBuilder b = new StringBuilder();
		if (!values.isEmpty()) {
			Iterator<String> it = values.iterator();
			b.append(it.next());
			while (it.hasNext()) {
				b.append(", ").append(it.next()); //$NON-NLS-1$
			}
		}
		getCellEditor().setValue(b.toString());
		getCellEditor().setValidator(validator);
	}

	/**
	 * Cleanup is done here. Any feedback is erased and listeners unhooked.
	 */
	@Override
	protected void bringDown() {
		eraseFeedback();
		unhookListeners();
		if (getCellEditor() != null) {
			getCellEditor().deactivate();
			getCellEditor().dispose();
			setCellEditor(null);
		}
		((CardFieldEditPart)getEditPart()).getParent().getParent().refresh();
	}
}
