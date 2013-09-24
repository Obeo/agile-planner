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

import java.util.List;

import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;

/**
 * Model used by the planning editor to manipulate the backlog wrapped in a MilestonePlanningWrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestoneBacklogModel implements IBacklog {

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
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklog#getBacklogItems()
	 */
	@Override
	public List<BacklogItemWrapper> getBacklogItems() {
		return wrapper.getOrderedUnassignedBacklogItems();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklog#getMilestoneId()
	 */
	@Override
	public Integer getMilestoneId() {
		return Integer.valueOf(wrapper.getId());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklog#getMilestonePlanning()
	 */
	@Override
	public MilestonePlanningWrapper getMilestonePlanning() {
		return wrapper;
	}
}
