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
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link AgileTaskKindUtil}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AgileTaksKindUtilTests {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Test method for {@link AgileTaskKindUtil#setAgileTaskKind(TaskData, String)}.
	 */
	@Test
	public void testSetAgileTaskKind() {
		AgileTaskKindUtil.setAgileTaskKind(taskData, "Something");
		assertEquals("Something", AgileTaskKindUtil.getAgileTaskKind(taskData));
	}

	/**
	 * Test method for {@link AgileTaskKindUtil#getAgileTaskKind(TaskData)}.
	 */
	@Test
	public void testGetAgileTaskKind() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(AgileTaskKindUtil.TASK_KIND_KEY);
		attribute.setValue("Test");
		assertEquals("Test", AgileTaskKindUtil.getAgileTaskKind(taskData));
	}

	/**
	 * Builds the tested taskData.
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

}
