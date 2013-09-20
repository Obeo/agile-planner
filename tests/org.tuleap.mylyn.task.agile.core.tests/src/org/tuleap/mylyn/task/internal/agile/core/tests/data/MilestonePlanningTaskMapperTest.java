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
package org.tuleap.mylyn.task.internal.agile.core.tests.data;

import java.util.Date;
import java.util.Iterator;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningTaskMapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the milestone planning task mapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestonePlanningTaskMapperTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper.
	 */
	@Test
	public void testMilestoneCreation() {
		MilestonePlanningTaskMapper mapper = new MilestonePlanningTaskMapper(taskData);
		MilestonePlanningWrapper wrapper = mapper.getMilestonePlanningWrapper();
		Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone();
		subMilestone.setCapacity(20);
		subMilestone.setDuration(11);
		subMilestone.setId(200);
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(testDate);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem();
		backlogItem.setId(300);
		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		backlogItem.setInitialEffort(5);
		backlogItem.setAssignedMilestoneId(200);
		// System.out.println(taskData.getRoot());

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		assertNotNull(planningAtt);

		// Milestones
		TaskAttribute milestoneListAtt = planningAtt.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		assertNotNull(milestoneListAtt);
		TaskAttribute milestone0 = milestoneListAtt.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0"); //$NON-NLS-1$
		assertNotNull(milestone0);
		assertTrue(milestone0.getMetaData().isReadOnly());

		TaskAttribute capacity = milestone0.getAttribute(SubMilestoneWrapper.MILESTONE_CAPACITY);
		assertNotNull(capacity);
		assertEquals(Float.toString(20f), capacity.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, capacity.getMetaData().getType());

		TaskAttribute duration = milestone0.getAttribute(SubMilestoneWrapper.MILESTONE_DURATION);
		assertNotNull(duration);
		assertEquals(Float.toString(11f), duration.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, duration.getMetaData().getType());

		TaskAttribute id = milestone0.getAttribute(IMylynAgileCoreConstants.ID);
		assertNotNull(id);
		assertEquals("200", id.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, id.getMetaData().getType());

		TaskAttribute label = milestone0.getAttribute(IMylynAgileCoreConstants.LABEL);
		assertNotNull(label);
		assertEquals("Milestone 1", label.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, label.getMetaData().getType());

		TaskAttribute start = milestone0.getAttribute(SubMilestoneWrapper.START_DATE);
		assertNotNull(start);
		assertEquals(Long.toString(testDate.getTime()), start.getValue());
		assertEquals(TaskAttribute.TYPE_DATETIME, start.getMetaData().getType());

		// Backlog
		TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		assertNotNull(backlogAtt);
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper.
	 */
	@Test
	public void testWrappingAnExistingMilestone() {
		MilestonePlanningTaskMapper mapper = new MilestonePlanningTaskMapper(taskData);
		MilestonePlanningWrapper wrapper = mapper.getMilestonePlanningWrapper();
		Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone();
		subMilestone.setCapacity(20);
		subMilestone.setDuration(11);
		subMilestone.setId(200);
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(testDate);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem();
		backlogItem.setId(300);
		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		backlogItem.setInitialEffort(5);
		backlogItem.setAssignedMilestoneId(200);
		// System.out.println(taskData.getRoot());

		mapper = new MilestonePlanningTaskMapper(taskData);
		wrapper = mapper.getMilestonePlanningWrapper();

		Iterator<SubMilestoneWrapper> subMilestones = wrapper.getSubMilestones().iterator();
		subMilestone = subMilestones.next();
		assertEquals(200, subMilestone.getId());
		assertEquals("Milestone 1", subMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(20f, subMilestone.getCapacity().floatValue(), 0f);
		assertEquals(testDate, subMilestone.getStartDate());

		assertFalse(subMilestones.hasNext());

		Iterator<BacklogItemWrapper> backlogItems = wrapper.getBacklogItems().iterator();
		backlogItem = backlogItems.next();
		assertEquals(300, backlogItem.getId());
		assertEquals("label of backlog item 300", backlogItem.getLabel()); //$NON-NLS-1$
		assertEquals(5f, backlogItem.getInitialEffort().floatValue(), 0f);
		assertEquals(200, backlogItem.getAssignedMilestoneId().intValue());

		assertFalse(backlogItems.hasNext());
	}

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		TaskRepository taskRepository = new TaskRepository("kind", "repository"); //$NON-NLS-1$ //$NON-NLS-2$
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, "kind", "repository", "id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
