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
package org.tuleap.mylyn.task.internal.agile.core.tests.merge;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.internal.tasks.core.data.TaskDataState;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.PlanningDataModelMerger;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests of {@link PlanningDataModelMerger}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningDataModelMergerTest {

	private static final String CONNECTOR_KIND = "test";

	private static final String REPO_URL = "https://test";

	private static final String TASK_ID = "123";

	private TaskRepository taskRepository;

	private ITask task;

	private TaskDataState taskDataState;

	private TaskData remote;

	private TaskData lastRead;

	private TaskDataModel model;

	@Before
	public void setUp() {
		taskRepository = new TaskRepository(CONNECTOR_KIND, REPO_URL);
		task = new TaskTask(CONNECTOR_KIND, REPO_URL, TASK_ID);
		taskDataState = new TaskDataState(CONNECTOR_KIND, REPO_URL, TASK_ID);
	}

	/**
	 * @param map
	 * @param backlog
	 */
	private TaskData createTestData(LinkedHashMap<String, List<String>> map, List<String> backlog) {
		TaskData td = new TaskData(new TaskAttributeMapper(taskRepository), CONNECTOR_KIND, REPO_URL, TASK_ID);
		createMilestone(td, backlog, map);
		return td;
	}

	/**
	 * Creates a milestone for tests with the given IDs. A LinkedHashMap is used to guarantee the order of the
	 * created milestones.
	 */
	private void createMilestone(TaskData taskData, List<String> backlogIds,
			LinkedHashMap<String, List<String>> milestoneIds) {
		TaskAttribute root = taskData.getRoot();
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(root);
		wrapper.setLabel("Milestone Test");
		wrapper.setDisplayId("123");
		wrapper.setHasCardwall(false);
		for (String id : backlogIds) {
			BacklogItemWrapper bi = wrapper.addBacklogItem(id);
			bi.setLabel("Item " + id);
			bi.setDisplayId("#" + id);
			bi.setInitialEffort("5");
		}
		for (Entry<String, List<String>> entry : milestoneIds.entrySet()) {
			String id = entry.getKey();
			SubMilestoneWrapper sprint = wrapper.addSubMilestone(id);
			sprint.setLabel("Sprint " + id);
			sprint.setCapacity("60");
			sprint.setDisplayId("sprint" + id);
			for (String bId : entry.getValue()) {
				BacklogItemWrapper bi = sprint.addBacklogItem(bId);
				bi.setLabel("Item " + bId);
				bi.setDisplayId("#" + bId);
				bi.setInitialEffort("5");
			}
		}
	}

	/**
	 * Checks that model is unchanged when merging no changes.
	 */
	@Test
	public void testNoChangeNoLastRead() {
		LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		List<String> backlog = Arrays.asList("0", "1", "2", "3", "4");
		map.put("m0", Arrays.asList("10", "11", "12", "13"));
		map.put("m1", Arrays.asList("20", "21", "22", "23"));
		map.put("m2", Arrays.asList("30", "31", "32", "33"));
		remote = createTestData(map, backlog);
		taskDataState.setRepositoryData(remote);
		taskDataState.revert();
		model = new TaskDataModel(taskRepository, task, taskDataState);
		taskDataState.setRepositoryData(remote);

		PlanningDataModelMerger merger = new PlanningDataModelMerger(model);
		merger.merge();
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(model.getTaskData().getRoot());
		assertEquals("Milestone Test", wrapper.getLabel());
		assertEquals("123", wrapper.getDisplayId());
		assertFalse(wrapper.getHasCardwall());
		List<BacklogItemWrapper> items = wrapper.getOrderedUnassignedBacklogItems();
		assertEquals(5, items.size());
		for (int i = 0; i < 5; i++) {
			BacklogItemWrapper bi = items.get(i);
			assertEquals("" + i, bi.getId());
			assertEquals("Item " + i, bi.getLabel());
			assertEquals("#" + i, bi.getDisplayId());
			assertEquals("5", bi.getInitialEffort());
		}
		List<SubMilestoneWrapper> milestones = wrapper.getSubMilestones();
		assertEquals(3, milestones.size());
		for (int j = 0; j < 3; j++) {
			SubMilestoneWrapper sprint = milestones.get(j);
			assertEquals("m" + j, sprint.getId());
			assertEquals("Sprint m" + j, sprint.getLabel());
			assertEquals("60", sprint.getCapacity());
			assertEquals("sprintm" + j, sprint.getDisplayId());
			List<BacklogItemWrapper> sprintItems = sprint.getOrderedBacklogItems();
			assertEquals(4, sprintItems.size());
			for (int i = 0; i < 4; i++) {
				BacklogItemWrapper bi = sprintItems.get(i);
				int id = 10 * j + 10 + i;
				assertEquals("" + id, bi.getId());
				assertEquals("Item " + id, bi.getLabel());
				assertEquals("#" + id, bi.getDisplayId());
				assertEquals("5", bi.getInitialEffort());
			}
		}
	}

	/**
	 * Checks that model is unchanged when merging no changes.
	 */
	@Test
	public void testNoChangeWithLastReadUnchanged() {
		LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		List<String> backlog = Arrays.asList("0", "1", "2", "3", "4");
		map.put("m0", Arrays.asList("10", "11", "12", "13"));
		map.put("m1", Arrays.asList("20", "21", "22", "23"));
		map.put("m2", Arrays.asList("30", "31", "32", "33"));
		remote = createTestData(map, backlog);
		lastRead = createTestData(map, backlog);
		taskDataState.setRepositoryData(remote);
		taskDataState.revert();
		model = new TaskDataModel(taskRepository, task, taskDataState);
		taskDataState.setRepositoryData(remote);
		taskDataState.setLastReadData(lastRead);

		PlanningDataModelMerger merger = new PlanningDataModelMerger(model);
		merger.merge();
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(model.getTaskData().getRoot());
		assertEquals("Milestone Test", wrapper.getLabel());
		assertEquals("123", wrapper.getDisplayId());
		assertFalse(wrapper.getHasCardwall());
		List<BacklogItemWrapper> items = wrapper.getOrderedUnassignedBacklogItems();
		assertEquals(5, items.size());
		for (int i = 0; i < 5; i++) {
			BacklogItemWrapper bi = items.get(i);
			assertEquals("" + i, bi.getId());
			assertEquals("Item " + i, bi.getLabel());
			assertEquals("#" + i, bi.getDisplayId());
			assertEquals("5", bi.getInitialEffort());
		}
		List<SubMilestoneWrapper> milestones = wrapper.getSubMilestones();
		assertEquals(3, milestones.size());
		for (int j = 0; j < 3; j++) {
			SubMilestoneWrapper sprint = milestones.get(j);
			assertEquals("m" + j, sprint.getId());
			assertEquals("Sprint m" + j, sprint.getLabel());
			assertEquals("60", sprint.getCapacity());
			assertEquals("sprintm" + j, sprint.getDisplayId());
			List<BacklogItemWrapper> sprintItems = sprint.getOrderedBacklogItems();
			assertEquals(4, sprintItems.size());
			for (int i = 0; i < 4; i++) {
				BacklogItemWrapper bi = sprintItems.get(i);
				int id = 10 * j + 10 + i;
				assertEquals("" + id, bi.getId());
				assertEquals("Item " + id, bi.getLabel());
				assertEquals("#" + id, bi.getDisplayId());
				assertEquals("5", bi.getInitialEffort());
			}
		}
	}
}
