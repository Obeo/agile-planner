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

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractBacklogItemWrapper;

/**
 * Wrapper of a TaskAttribute that represents a Backlog Item in a swimlane.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneItemWrapper extends AbstractBacklogItemWrapper {

	/**
	 * The parent planning.
	 */
	private final SwimlaneWrapper parent;

	/**
	 * Constructor to use to wrap an existing task attribute that is not yet filled with sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 * @param id
	 *            The backlog item's functional id
	 */
	protected SwimlaneItemWrapper(final SwimlaneWrapper parent, final TaskAttribute root, int id) {
		super(root, id);
		Assert.isNotNull(parent);
		this.parent = parent;
		parent.getWrappedAttribute().addValue(root.getId());
	}

	/**
	 * Constructor to use to wrap an existing instance that already has sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 */
	protected SwimlaneItemWrapper(final SwimlaneWrapper parent, final TaskAttribute root) {
		super(root);
		Assert.isNotNull(parent);
		this.parent = parent;
	}

	/**
	 * Return the parent planning as a wrapper.
	 * 
	 * @return The (never null) parent planning, as a wrapper.
	 */
	public SwimlaneWrapper getParent() {
		return parent;
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
