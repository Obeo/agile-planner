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

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper;
import org.tuleap.mylyn.task.internal.agile.core.util.MylynAgileCoreMessages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TopPlanningMapperTest {

	/**
	 * The test connector kind.
	 */
	private String connectorKind = "test";

	/**
	 * The test connector URL.
	 */
	private String connectorUrl = "test/url";

	/**
	 * The tested mapper.
	 */
	private TopPlanningMapper mapper;

	/**
	 * The {@link TaskData} used for tests.
	 */
	private TaskData taskData;

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper#initializeEmptyTaskData()}.
	 */
	@Test
	public void testInitializeEmptyTaskData() {
		mapper.initializeEmptyTaskData();
		TaskAttribute attribute = taskData.getRoot().getAttribute(TaskAttribute.TASK_KEY);
		assertNotNull(attribute);
		assertTrue(attribute.getMetaData().isReadOnly());
		assertEquals(TaskAttribute.TYPE_SHORT_TEXT, attribute.getMetaData().getType());

		attribute = taskData.getRoot().getAttribute(TaskAttribute.TASK_KIND);
		assertNotNull(attribute);
		assertEquals(MylynAgileCoreMessages.getString("TopPlanningMapper.GeneralPlanningOf"), attribute
				.getValue());
		assertTrue(attribute.getMetaData().isReadOnly());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, attribute.getMetaData().getType());

		attribute = taskData.getRoot().getAttribute(TaskAttribute.SUMMARY);
		assertNotNull(attribute);
		assertEquals(MylynAgileCoreMessages.getString("TopPlanningMapper.GeneralPlanning"), attribute
				.getValue());
		assertTrue(attribute.getMetaData().isReadOnly());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, attribute.getMetaData().getType());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper#setTaskKey(java.lang.String)}.
	 */
	@Test
	public void testSetTaskKey() {
		mapper.initializeEmptyTaskData();
		mapper.setTaskKey("taskKey");
		TaskAttribute attribute = taskData.getRoot().getAttribute(TaskAttribute.TASK_KEY);
		assertNotNull(attribute);
		assertEquals("taskKey", attribute.getValue());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper#setTaskKey(java.lang.String)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetTaskKeyWhenNotInitialized() {
		mapper.setTaskKey("key");
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper#setTaskKind(java.lang.String)}.
	 */
	@Test
	public void testSetTaskKind() {
		mapper.initializeEmptyTaskData();
		mapper.setTaskKind("kind");
		TaskAttribute attribute = taskData.getRoot().getAttribute(TaskAttribute.TASK_KIND);
		assertNotNull(attribute);
		assertEquals("kind", attribute.getValue());
	}

	/**
	 * Tests set-up.
	 */
	@Before
	public void setUp() {
		TaskRepository taskRepository = new TaskRepository(connectorKind, connectorUrl);
		taskData = new TaskData(new TaskAttributeMapper(taskRepository), connectorKind, connectorUrl, "123");
		mapper = new TopPlanningMapper(taskData);
	}

}
