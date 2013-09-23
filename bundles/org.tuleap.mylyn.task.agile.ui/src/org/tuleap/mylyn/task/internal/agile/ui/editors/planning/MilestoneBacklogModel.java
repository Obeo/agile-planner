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
package org.tuleap.mylyn.task.internal.agile.ui.editors.planning;

import com.google.common.collect.Iterables;

import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;

/**
 * Model used by the planning editor to manipulate the backlog wrapped in a MilestonePlanningWrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestoneBacklogModel implements IBacklogItemContainer {

	/**
	 * The wrapper.
	 */
	private final MilestonePlanningWrapper wrapper;

	/**
	 * Constructor.
	 * 
	 * @param wrapper
	 *            The wrapper
	 */
	public MilestoneBacklogModel(MilestonePlanningWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklogItemContainer#getBacklogItems()
	 */
	@Override
	public Iterable<BacklogItemWrapper> getBacklogItems() {
		return Iterables.filter(wrapper.getBacklogItems(), PlanningFilters.unassigned());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklogItemContainer#getMilestonePlanning()
	 */
	@Override
	public MilestonePlanningWrapper getMilestonePlanning() {
		return wrapper;
	}
}
