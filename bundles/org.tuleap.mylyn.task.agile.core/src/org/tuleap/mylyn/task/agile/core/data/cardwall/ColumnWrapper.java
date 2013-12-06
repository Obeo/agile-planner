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
package org.tuleap.mylyn.task.agile.core.data.cardwall;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

/**
 * Card wall wrapper, encapsulates all the logic of writing and reading {@link TaskAttribute}s in a TaskData
 * and offers a usable API.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ColumnWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Prefix to use for column {@link TaskAttribute}s.
	 */
	public static final String PREFIX_COLUMN = "mta_col-"; //$NON-NLS-1$

	/**
	 * The parent card wall.
	 */
	private final CardwallWrapper parent;

	/**
	 * Constructor to use while creating a {@link TaskAttribute}.
	 * 
	 * @param parent
	 *            The parent.
	 * @param id
	 *            The id of the column
	 * @param label
	 *            The label of the column
	 */
	protected ColumnWrapper(CardwallWrapper parent, String id, String label) {
		super(parent.getRoot(), PREFIX_COLUMN, id);
		this.parent = parent;
		setLabel(label);
	}

	/**
	 * Constructor to use while creating a {@link TaskAttribute}.
	 * 
	 * @param parent
	 *            The parent.
	 * @param att
	 *            The {@link TaskAttribute} that should represent a column.
	 */
	protected ColumnWrapper(CardwallWrapper parent, TaskAttribute att) {
		super(parent.getRoot(), PREFIX_COLUMN, att.getValue());
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		parent.fireAttributeChanged(att);
	}
}
