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
package org.tuleap.mylyn.task.internal.agile.ui.tests.backlog;

import java.util.Date;

import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.MilestoneBacklogModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.SubMilestoneBacklogModel;

import static org.junit.Assert.assertEquals;

/**
 * Tests of the Backlog model classes.
 *
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class BacklogModelTest extends AbstractBacklogTest {

	/**
	 * test SubMilestoneBacklogModel.
	 */
	@Test
	public void testMilestoneBacklogModel() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("200"); //$NON-NLS-1$
		subMilestone.setCapacity("20");
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(new Date());
		subMilestone.setStartDate(new Date(System.currentTimeMillis() + 11L * 1000 * 3600 * 24));

		BacklogItemWrapper backlogItem = subMilestone.addBacklogItem("300"); //$NON-NLS-1$
		backlogItem.setInitialEffort("5");
		backlogItem.setLabel("backlog item 1"); //$NON-NLS-1$

		BacklogItemWrapper backlogItem2 = wrapper.addBacklogItem("301"); //$NON-NLS-1$
		backlogItem2.setInitialEffort("10");
		backlogItem2.setLabel("backlog item 2"); //$NON-NLS-1$

		MilestoneBacklogModel backlogModel = new MilestoneBacklogModel(wrapper);

		// CHECKING
		assertEquals(backlogModel.getMilestoneId(), null);
		assertEquals(backlogModel.getBacklogItems().size(), 1);
		assertEquals(backlogModel.getBacklogItems().get(0).getId(), backlogItem2.getId());
		assertEquals(backlogModel.getMilestonePlanning(), wrapper);
	}

	/**
	 * test SubMilestoneBacklogModel.
	 */
	@Test
	public void testSubMilestoneBacklogModel() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("200"); //$NON-NLS-1$
		subMilestone.setCapacity("20");
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(new Date());
		subMilestone.setStartDate(new Date(System.currentTimeMillis() + 11L * 1000 * 3600 * 24));
		SubMilestoneBacklogModel backlogModel = new SubMilestoneBacklogModel(wrapper, subMilestone);

		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("300"); //$NON-NLS-1$
		backlogItem.setInitialEffort("5");
		backlogItem.setLabel("backlog item 1"); //$NON-NLS-1$

		BacklogItemWrapper backlogItem2 = subMilestone.addBacklogItem("301"); //$NON-NLS-1$
		backlogItem2.setInitialEffort("5");
		backlogItem2.setLabel("backlog item 2"); //$NON-NLS-1$

		assertEquals(backlogModel.getMilestoneId(), subMilestone.getId());
		assertEquals(backlogModel.getSubMilestone(), subMilestone);
		assertEquals(backlogModel.getBacklogItems().size(), 1);
		assertEquals(backlogModel.getBacklogItems().get(0).getId(), backlogItem2.getId());
		assertEquals(backlogModel.getMilestonePlanning(), wrapper);
	}

}
