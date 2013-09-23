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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

/**
 * Model used by the planning editor to manipulate the backlog wrapped in a SubMilestoneWrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SubMilestoneBacklogModel implements IBacklogItemContainer {

	/**
	 * The milestone planning wrapper.
	 */
	private final MilestonePlanningWrapper planningWrapper;

	/**
	 * The sub-milestone wrapper.
	 */
	private final SubMilestoneWrapper subMilestoneWrapper;

	/**
	 * The sub-milestone wrapper.
	 */
	private final Predicate<BacklogItemWrapper> filter;

	/**
	 * Constructor.
	 * 
	 * @param planningWrapper
	 *            The milestone planning wrapper
	 * @param subMilestoneWrapper
	 *            The sub-milestone wrapper
	 */
	public SubMilestoneBacklogModel(MilestonePlanningWrapper planningWrapper,
			SubMilestoneWrapper subMilestoneWrapper) {
		this.planningWrapper = planningWrapper;
		this.subMilestoneWrapper = subMilestoneWrapper;
		this.filter = PlanningFilters.assignedTo(subMilestoneWrapper.getId());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklogItemContainer#getBacklogItems()
	 */
	@Override
	public Iterable<BacklogItemWrapper> getBacklogItems() {
		return Iterables.filter(planningWrapper.getBacklogItems(), filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklogItemContainer#getMilestonePlanning()
	 */
	@Override
	public MilestonePlanningWrapper getMilestonePlanning() {
		return planningWrapper;
	}

	/**
	 * Provides the sub-milestone.
	 * 
	 * @return The sub-milestone.
	 */
	public SubMilestoneWrapper getSubMilestone() {
		return subMilestoneWrapper;
	}
}
