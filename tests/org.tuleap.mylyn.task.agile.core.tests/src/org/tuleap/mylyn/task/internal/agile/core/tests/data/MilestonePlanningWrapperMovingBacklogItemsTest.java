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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

import static org.junit.Assert.assertEquals;

/**
 * Tests of the milestone planning task mapper.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class MilestonePlanningWrapperMovingBacklogItemsTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * the wrapper.
	 */
	private MilestonePlanningWrapper wrapper;

	/**
	 * The SubMilestone number 1.
	 */
	private SubMilestoneWrapper subMilestone1;

	/**
	 * The SubMilestone number 2.
	 */
	private SubMilestoneWrapper subMilestone2;

	/**
	 * The SubMilestone number 3.
	 */
	private SubMilestoneWrapper subMilestone3;

	/**
	 * The BacklogItem number 0.
	 */
	private BacklogItemWrapper backlogItem0;

	/**
	 * The BacklogItem number 1.
	 */
	private BacklogItemWrapper backlogItem1;

	/**
	 * The BacklogItem number 2.
	 */
	private BacklogItemWrapper backlogItem2;

	/**
	 * The BacklogItem number 3.
	 */
	private BacklogItemWrapper backlogItem3;

	/**
	 * The BacklogItem number 4.
	 */
	private BacklogItemWrapper backlogItem4;

	/**
	 * The BacklogItem number 5.
	 */
	private BacklogItemWrapper backlogItem5;

	/**
	 * The BacklogItem number 6.
	 */
	private BacklogItemWrapper backlogItem6;

	/**
	 * The BacklogItem number 7.
	 */
	private BacklogItemWrapper backlogItem7;

	/**
	 * The BacklogItem number 8.
	 */
	private BacklogItemWrapper backlogItem8;

	/**
	 * The BacklogItem number 9.
	 */
	private BacklogItemWrapper backlogItem9;

	/**
	 * The BacklogItem number 10.
	 */
	private BacklogItemWrapper backlogItem10;

	/**
	 * The BacklogItem number 11.
	 */
	private BacklogItemWrapper backlogItem11;

	/**
	 * The BacklogItem number 12.
	 */
	private BacklogItemWrapper backlogItem12;

	/**
	 * The BacklogItem number 13.
	 */
	private BacklogItemWrapper backlogItem13;

	/**
	 * The BacklogItem number 14.
	 */
	private BacklogItemWrapper backlogItem14;

	/**
	 * The BacklogItem number 15.
	 */
	private BacklogItemWrapper backlogItem15;

	/**
	 * The BacklogItem number 16.
	 */
	private BacklogItemWrapper backlogItem16;

	/**
	 * The BacklogItem number 17.
	 */
	private BacklogItemWrapper backlogItem17;

	/**
	 * The BacklogItem number 18.
	 */
	private BacklogItemWrapper backlogItem18;

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
		wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		// Date testDate = new Date();
		subMilestone1 = wrapper.addSubMilestone(100);
		subMilestone2 = wrapper.addSubMilestone(200);
		subMilestone3 = wrapper.addSubMilestone(300);

		backlogItem0 = wrapper.addBacklogItem(0);
		backlogItem1 = wrapper.addBacklogItem(1);
		backlogItem2 = wrapper.addBacklogItem(2);
		backlogItem3 = wrapper.addBacklogItem(3);
		backlogItem4 = wrapper.addBacklogItem(4);
		backlogItem5 = wrapper.addBacklogItem(5);
		backlogItem6 = wrapper.addBacklogItem(6);
		backlogItem7 = wrapper.addBacklogItem(7);
		backlogItem8 = wrapper.addBacklogItem(8);
		backlogItem9 = wrapper.addBacklogItem(9);
		backlogItem10 = wrapper.addBacklogItem(10);
		backlogItem11 = wrapper.addBacklogItem(11);
		backlogItem12 = wrapper.addBacklogItem(12);
		backlogItem13 = wrapper.addBacklogItem(13);
		backlogItem14 = wrapper.addBacklogItem(14);
		backlogItem15 = wrapper.addBacklogItem(15);
		backlogItem16 = wrapper.addBacklogItem(16);
		backlogItem17 = wrapper.addBacklogItem(17);
		backlogItem18 = wrapper.addBacklogItem(18);
	}

	/**
	 * Test moving backlogItems in the middle of a Milestone.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testmoveItemsToMilestone() {

		// testing the total number of BI
		Iterable<BacklogItemWrapper> backlogItemsList = wrapper.getBacklogItems();
		int j = 0;
		for (BacklogItemWrapper backlogItemWrapper : backlogItemsList) {
			j++;
		}
		assertEquals(19, j);

		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}

		List<BacklogItemWrapper> milestone1BIs = new ArrayList<BacklogItemWrapper>();
		milestone1BIs.add(backlogItem5);
		milestone1BIs.add(backlogItem6);
		milestone1BIs.add(backlogItem7);

		// test moving a list in the middle of a milestone
		wrapper.moveItemsToMilestone(milestone1BIs, backlogItem12, true, subMilestone1);

		Iterable<BacklogItemWrapper> backlogItems = wrapper
				.getMilestoneOrderedBacklogItemsList(subMilestone1);
		Iterator<BacklogItemWrapper> i = backlogItems.iterator();
		BacklogItemWrapper first = i.next();
		BacklogItemWrapper second = i.next();
		BacklogItemWrapper third = i.next();

		assertEquals(5, first.getId());
		assertEquals(6, second.getId());
		assertEquals(7, third.getId());

		assertEquals(100, first.getAssignedMilestoneId().intValue());
		assertEquals(100, second.getAssignedMilestoneId().intValue());
		assertEquals(100, third.getAssignedMilestoneId().intValue());
		assertEquals(null, backlogItem8.getAssignedMilestoneId());

		// the reverse way: test moving the list to the backlog
		wrapper.moveItemsToBacklog(milestone1BIs, backlogItem12, true);
		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}
	}

	/**
	 * Test moving backlogItems on the top of a Milestone.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testmoveItemsOnTopOfMilestone() {

		// testing the total number of BI
		Iterable<BacklogItemWrapper> backlogItemsList = wrapper.getBacklogItems();
		int j = 0;
		for (BacklogItemWrapper backlogItemWrapper : backlogItemsList) {
			j++;
		}
		assertEquals(19, j);

		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}

		List<BacklogItemWrapper> milestone2BIs = new ArrayList<BacklogItemWrapper>();
		milestone2BIs.add(backlogItem9);
		milestone2BIs.add(backlogItem10);
		milestone2BIs.add(backlogItem11);

		// test moving a list on the top of a milestone
		wrapper.moveItemsToMilestone(milestone2BIs, backlogItem0, true, subMilestone2);

		Iterable<BacklogItemWrapper> backlogItems = wrapper
				.getMilestoneOrderedBacklogItemsList(subMilestone2);
		Iterator<BacklogItemWrapper> i = backlogItems.iterator();
		BacklogItemWrapper first = i.next();
		BacklogItemWrapper second = i.next();
		BacklogItemWrapper third = i.next();

		assertEquals(9, first.getId());
		assertEquals(10, second.getId());
		assertEquals(11, third.getId());

		assertEquals(200, first.getAssignedMilestoneId().intValue());
		assertEquals(200, second.getAssignedMilestoneId().intValue());
		assertEquals(200, third.getAssignedMilestoneId().intValue());
		assertEquals(null, backlogItem12.getAssignedMilestoneId());

		// the reverse way: test moving the list to the backlog
		wrapper.moveItemsToBacklog(milestone2BIs, backlogItem0, true);
		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}
	}

	/**
	 * Test moving backlogItems on the bottom of a Milestone.
	 */
	@Test
	public void testmoveItemsOnTheBottomOfMilestone() {

		// testing the total number of BI
		Iterable<BacklogItemWrapper> backlogItemsList = wrapper.getBacklogItems();
		int j = 0;
		for (@SuppressWarnings("unused")
		BacklogItemWrapper backlogItemWrapper : backlogItemsList) {
			j++;
		}
		assertEquals(19, j);

		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}

		List<BacklogItemWrapper> milestone3BIs = new ArrayList<BacklogItemWrapper>();
		milestone3BIs.add(backlogItem15);
		milestone3BIs.add(backlogItem16);
		milestone3BIs.add(backlogItem17);

		// test moving a list on the bottom of a milestone
		wrapper.moveItemsToMilestone(milestone3BIs, backlogItem18, false, subMilestone3);

		Iterable<BacklogItemWrapper> backlogItems = wrapper
				.getMilestoneOrderedBacklogItemsList(subMilestone3);
		Iterator<BacklogItemWrapper> i = backlogItems.iterator();
		BacklogItemWrapper first = i.next();
		BacklogItemWrapper second = i.next();
		BacklogItemWrapper third = i.next();

		assertEquals(15, first.getId());
		assertEquals(16, second.getId());
		assertEquals(17, third.getId());

		assertEquals(300, first.getAssignedMilestoneId().intValue());
		assertEquals(300, second.getAssignedMilestoneId().intValue());
		assertEquals(300, third.getAssignedMilestoneId().intValue());
		assertEquals(null, backlogItem18.getAssignedMilestoneId());

		// the reverse way: test moving the list to the backlog
		wrapper.moveItemsToBacklog(milestone3BIs, backlogItem18, true);
		for (BacklogItemWrapper backlogItemWrapper : wrapper.getBacklogItems()) {
			assertEquals(null, backlogItemWrapper.getAssignedMilestoneId());
		}
	}
}
