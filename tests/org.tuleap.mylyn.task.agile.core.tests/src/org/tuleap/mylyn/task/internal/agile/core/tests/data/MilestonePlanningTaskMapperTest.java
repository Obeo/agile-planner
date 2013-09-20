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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
	 * Tests the basic manipulation of a MilestonePlanningWrapper to ensure the TaskAttribute structure is
	 * correct.
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
	 * Tests the basic manipulation of a MilestonePlanningWrapper for writing and reading.
	 */
	@Test
	public void testReadAndWrite() {
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
		assertEquals(11f, subMilestone.getDuration().floatValue(), 0f);
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
	 * Tests the basic manipulation of a MilestonePlanningWrapper without optional fields.
	 */
	@Test
	public void testMilestoneWithoutOptionalFields() {
		MilestonePlanningTaskMapper mapper = new MilestonePlanningTaskMapper(taskData);
		MilestonePlanningWrapper wrapper = mapper.getMilestonePlanningWrapper();
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone();
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem();
		// System.out.println(taskData.getRoot());

		mapper = new MilestonePlanningTaskMapper(taskData);
		wrapper = mapper.getMilestonePlanningWrapper();

		assertEquals(1, wrapper.submilestonesCount());
		Iterator<SubMilestoneWrapper> subMilestones = wrapper.getSubMilestones().iterator();
		subMilestone = subMilestones.next();
		assertEquals(-1, subMilestone.getId());
		assertNull(subMilestone.getLabel());
		assertNull(subMilestone.getDuration());
		assertNull(subMilestone.getCapacity());
		assertNull(subMilestone.getStartDate());

		assertFalse(subMilestones.hasNext());

		assertEquals(1, wrapper.backlogItemsCount());
		Iterator<BacklogItemWrapper> backlogItems = wrapper.getBacklogItems().iterator();
		backlogItem = backlogItems.next();
		assertEquals(-1, backlogItem.getId());
		assertNull(backlogItem.getLabel());
		assertNull(backlogItem.getInitialEffort());
		assertNull(backlogItem.getAssignedMilestoneId());

		assertFalse(backlogItems.hasNext());
	}

	/**
	 * Check that the mechanism to assign/unassign a backlog item to a milestone works right.
	 */
	@Test
	public void testBacklogItemAssignedIdRemoval() {
		MilestonePlanningTaskMapper mapper = new MilestonePlanningTaskMapper(taskData);
		MilestonePlanningWrapper wrapper = mapper.getMilestonePlanningWrapper();
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone();
		subMilestone.setId(123);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem();
		backlogItem.setId(333);
		backlogItem.setAssignedMilestoneId(123);
		assertEquals(123, backlogItem.getAssignedMilestoneId().intValue());
		backlogItem.removeAssignedMilestoneId();
		assertNull(backlogItem.getAssignedMilestoneId());
	}

	/**
	 * Check that the mechanism to set values works if invoked several times.
	 */
	@Test
	public void testMultipleAssignments() {
		MilestonePlanningTaskMapper mapper = new MilestonePlanningTaskMapper(taskData);
		MilestonePlanningWrapper wrapper = mapper.getMilestonePlanningWrapper();
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone();
		subMilestone.setId(123);
		assertEquals(123, subMilestone.getId());
		subMilestone.setId(128);
		assertEquals(128, subMilestone.getId());

		subMilestone.setCapacity(10f);
		assertEquals(10f, subMilestone.getCapacity().floatValue(), 0f);
		subMilestone.setCapacity(12f);
		assertEquals(12f, subMilestone.getCapacity().floatValue(), 0f);

		subMilestone.setDuration(20f);
		assertEquals(20f, subMilestone.getDuration().floatValue(), 0f);
		subMilestone.setDuration(22f);
		assertEquals(22f, subMilestone.getDuration().floatValue(), 0f);

		String label = "Label 1"; //$NON-NLS-1$
		String label2 = "Other"; //$NON-NLS-1$
		subMilestone.setLabel(label);
		assertEquals(label, subMilestone.getLabel());
		subMilestone.setLabel(label2);
		assertEquals(label2, subMilestone.getLabel());

		Date testDate = new Date();
		Date testDate2 = new Date();
		testDate2.setTime(testDate.getTime() - 120000);
		subMilestone.setStartDate(testDate);
		assertEquals(testDate.getTime(), subMilestone.getStartDate().getTime());
		subMilestone.setStartDate(testDate2);
		assertEquals(testDate2.getTime(), subMilestone.getStartDate().getTime());

		BacklogItemWrapper backlogItem = wrapper.addBacklogItem();
		backlogItem.setId(333);
		assertEquals(333, backlogItem.getId());
		backlogItem.setId(321);
		assertEquals(321, backlogItem.getId());

		backlogItem.setAssignedMilestoneId(123);
		assertEquals(123, backlogItem.getAssignedMilestoneId().intValue());
		backlogItem.setAssignedMilestoneId(128);
		assertEquals(128, backlogItem.getAssignedMilestoneId().intValue());

		backlogItem.setInitialEffort(20f);
		assertEquals(20f, backlogItem.getInitialEffort().floatValue(), 0f);
		backlogItem.setInitialEffort(15f);
		assertEquals(15f, backlogItem.getInitialEffort().floatValue(), 0f);

		backlogItem.setLabel(label);
		assertEquals(label, backlogItem.getLabel());
		backlogItem.setLabel(label2);
		assertEquals(label2, backlogItem.getLabel());
	}

	/**
	 * Check that method getXxxWrapper() always returns the same instance on a given instance.
	 */
	@Test
	public void testGetWrapperAlwaysReturnsTheSameInstance() {
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

		MilestonePlanningWrapper wrapper2 = mapper.getMilestonePlanningWrapper();
		assertSame(wrapper, wrapper2);
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
