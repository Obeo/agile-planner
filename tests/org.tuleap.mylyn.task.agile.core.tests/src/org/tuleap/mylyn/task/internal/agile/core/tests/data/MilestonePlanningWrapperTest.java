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
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the milestone planning task mapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestonePlanningWrapperTest {

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
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(200);
		subMilestone.setCapacity(20);
		subMilestone.setDuration(11);
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(testDate);

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);

		// Milestones
		TaskAttribute milestoneListAtt = planningAtt.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		TaskAttribute milestone0 = milestoneListAtt.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0"); //$NON-NLS-1$
		assertTrue(milestone0.getMetaData().isReadOnly());

		TaskAttribute capacity = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_CAPACITY);
		assertEquals(Float.toString(20f), capacity.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, capacity.getMetaData().getType());

		TaskAttribute duration = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_DURATION);
		assertEquals(Float.toString(11f), duration.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, duration.getMetaData().getType());

		TaskAttribute id = milestone0.getAttribute(milestone0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_ID);
		assertEquals("200", id.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, id.getMetaData().getType());

		TaskAttribute label = milestone0.getAttribute(milestone0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertEquals("Milestone 1", label.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, label.getMetaData().getType());

		TaskAttribute start = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_START_DATE);
		assertEquals(Long.toString(testDate.getTime()), start.getValue());
		assertEquals(TaskAttribute.TYPE_DATETIME, start.getMetaData().getType());

		String label1 = "label of backlog item 300"; //$NON-NLS-1$

		// Backlog
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(300);
		backlogItem.setLabel(label1);
		backlogItem.setInitialEffort(5);
		backlogItem.setAssignedMilestoneId(200);

		TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		TaskAttribute bi0 = backlogAtt.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0"); //$NON-NLS-1$
		assertTrue(bi0.getMetaData().isReadOnly());

		TaskAttribute pointsAtt = bi0.getAttribute(bi0.getId() + '-'
				+ BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
		assertEquals(Float.toString(5f), pointsAtt.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, pointsAtt.getMetaData().getType());

		TaskAttribute biIdAtt = bi0.getAttribute(bi0.getId() + '-' + AbstractTaskAttributeWrapper.SUFFIX_ID);
		assertEquals("300", biIdAtt.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, biIdAtt.getMetaData().getType());

		TaskAttribute biLabelAtt = bi0.getAttribute(bi0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertEquals(label1, biLabelAtt.getValue());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, biLabelAtt.getMetaData().getType());

		TaskAttribute assignedIdAtt = bi0.getAttribute(bi0.getId() + '-'
				+ BacklogItemWrapper.SUFFIX_ASSIGNED_MILESTONE_ID);
		assertEquals("200", assignedIdAtt.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, assignedIdAtt.getMetaData().getType());
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper for writing and reading.
	 */
	@Test
	public void testReadAndWrite() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(200);
		subMilestone.setCapacity(20);
		subMilestone.setDuration(11);
		String label0 = "Milestone 1"; //$NON-NLS-1$
		subMilestone.setLabel(label0);
		subMilestone.setStartDate(testDate);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(300);
		String label1 = "label of backlog item 300"; //$NON-NLS-1$
		backlogItem.setLabel(label1);
		backlogItem.setInitialEffort(5);
		backlogItem.setAssignedMilestoneId(200);
		// System.out.println(taskData.getRoot());

		wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		Iterator<SubMilestoneWrapper> subMilestones = wrapper.getSubMilestones().iterator();
		subMilestone = subMilestones.next();
		assertEquals(200, subMilestone.getId());
		assertEquals(label0, subMilestone.getLabel());
		assertEquals(11f, subMilestone.getDuration().floatValue(), 0f);
		assertEquals(20f, subMilestone.getCapacity().floatValue(), 0f);
		assertEquals(testDate, subMilestone.getStartDate());

		assertFalse(subMilestones.hasNext());

		Iterator<BacklogItemWrapper> backlogItems = wrapper.getAllBacklogItems().iterator();
		backlogItem = backlogItems.next();
		assertEquals(300, backlogItem.getId());
		assertEquals(label1, backlogItem.getLabel());
		assertEquals(5f, backlogItem.getInitialEffort().floatValue(), 0f);
		assertEquals(200, backlogItem.getAssignedMilestoneId().intValue());

		assertFalse(backlogItems.hasNext());
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper without optional fields.
	 */
	@Test
	public void testMilestoneWithoutOptionalFields() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(-1);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(-1);
		// System.out.println(taskData.getRoot());

		wrapper = new MilestonePlanningWrapper(taskData.getRoot());

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
		Iterator<BacklogItemWrapper> backlogItems = wrapper.getAllBacklogItems().iterator();
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
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		// Date testDate = new Date();
		wrapper.addSubMilestone(123);
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(333);
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
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(123);
		assertEquals(123, subMilestone.getId());

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

		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(333);

		assertEquals(333, backlogItem.getId());

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
	 * Tests the retrieval of a milestone by its id.
	 */
	@Test
	public void testGetMilestoneById() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addSubMilestone(123);
		assertNull(wrapper.getSubMilestone(122));
		SubMilestoneWrapper other = wrapper.getSubMilestone(123);
		assertNotNull(other);
	}

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}

	/**
	 * Test the notification mechanism with one listener.
	 */
	@Test
	public void testNotificationsWithOneListener() {
		TestChangeListener listener = new TestChangeListener();
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addListener(listener);

		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(200);
		subMilestone.setCapacity(12F);

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		TaskAttribute milestoneListAtt = planningAtt.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		TaskAttribute milestone0 = milestoneListAtt.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0"); //$NON-NLS-1$
		TaskAttribute capacity = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_CAPACITY);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(capacity));
		subMilestone.setCapacity(12F); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(capacity));
		subMilestone.setCapacity(13F); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(capacity));

		subMilestone.setDuration(21.5F);
		TaskAttribute duration = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_DURATION);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(duration));
		subMilestone.setDuration(21.5F); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(duration));
		subMilestone.setDuration(20F); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(duration));

		subMilestone.setLabel("Label"); //$NON-NLS-1$
		TaskAttribute label = milestone0.getAttribute(milestone0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(label));
		subMilestone.setLabel("Label"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(label));
		subMilestone.setLabel("Other"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(label));

		Date startDate = new Date();
		subMilestone.setStartDate(startDate);

		TaskAttribute start = milestone0.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_START_DATE);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(start));
		subMilestone.setStartDate(startDate); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(start));
		subMilestone.setStartDate(new Date(startDate.getTime() + 1)); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(start));

		// Backlog items
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(200);

		TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		TaskAttribute bi0 = backlogAtt.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0"); //$NON-NLS-1$

		backlogItem.setInitialEffort(5);
		TaskAttribute pointsAtt = bi0.getAttribute(bi0.getId() + '-'
				+ BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(pointsAtt));
		backlogItem.setInitialEffort(5);
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(pointsAtt));
		backlogItem.setInitialEffort(6);
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(pointsAtt));

		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		TaskAttribute biLabelAtt = bi0.getAttribute(bi0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(biLabelAtt));
		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(biLabelAtt));
		backlogItem.setLabel("Other"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(biLabelAtt));

		backlogItem.setAssignedMilestoneId(200);
		TaskAttribute assignedIdAtt = bi0.getAttribute(bi0.getId() + '-'
				+ BacklogItemWrapper.SUFFIX_ASSIGNED_MILESTONE_ID);

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(assignedIdAtt));
		backlogItem.setAssignedMilestoneId(200);
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(assignedIdAtt));
		backlogItem.setAssignedMilestoneId(201);
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(assignedIdAtt));
	}

}
