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
	 * The parent card wall.
	 */
	private final CardwallWrapper parent;

	/**
	 * Constructor to use to wrap an existing {@link TaskAttribute}.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that contains the columns
	 */
	protected ColumnWrapper(CardwallWrapper parent, TaskAttribute root) {
		super(root);
		this.parent = parent;
	}

	/**
	 * Constructor to use while creating a {@link TaskAttribute}.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that contains the columns
	 * @param id
	 *            The id of the column
	 * @param label
	 *            The label of the column
	 */
	protected ColumnWrapper(CardwallWrapper parent, TaskAttribute root, int id, String label) {
		super(root, id);
		this.parent = parent;
		setLabel(label);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		parent.fireAttributeChanged(attribute);
	}
}
