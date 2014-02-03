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
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.TaskAttributes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TaskAttributesTest {

	/**
	 * Test of {@link TaskAttributes#prefixedBy(String...)};
	 */
	@Test
	public void testPrefixedBy() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		TaskAttribute att = taskData.getRoot().createAttribute("toto");
		assertTrue(TaskAttributes.prefixedBy("toto").apply(att));
		assertTrue(TaskAttributes.prefixedBy("to").apply(att));
		assertTrue(TaskAttributes.prefixedBy("t").apply(att));
		assertTrue(TaskAttributes.prefixedBy("").apply(att));
		assertFalse(TaskAttributes.prefixedBy("titi").apply(att));
		assertFalse(TaskAttributes.prefixedBy("totoa").apply(att));
	}

	/**
	 * Test of {@link TaskAttributes#prefixedBy(String...)};
	 */
	@Test
	public void testPrefixedByMulti() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		TaskAttribute att = taskData.getRoot().createAttribute("toto");
		assertTrue(TaskAttributes.prefixedBy("titi", "toto").apply(att));
		assertTrue(TaskAttributes.prefixedBy("titi", "to").apply(att));
		assertTrue(TaskAttributes.prefixedBy("titi", "t").apply(att));
		assertTrue(TaskAttributes.prefixedBy("titi", "").apply(att));
		assertFalse(TaskAttributes.prefixedBy("titi", null).apply(att));
		assertFalse(TaskAttributes.prefixedBy("titi", "totoa").apply(att));
	}

	/**
	 * Test of {@link TaskAttributes#prefixedBy(String...)};
	 */
	@Test
	public void testIdentifiedBy() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		TaskAttribute att = taskData.getRoot().createAttribute("toto");
		assertTrue(TaskAttributes.identifiedBy("toto").apply(att));
		assertFalse(TaskAttributes.identifiedBy("to").apply(att));
		assertFalse(TaskAttributes.identifiedBy("totoa").apply(att));
	}

	/**
	 * Test of {@link TaskAttributes#prefixedBy(String...)};
	 */
	@Test
	public void testIdentifiedByMulti() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		TaskAttribute att = taskData.getRoot().createAttribute("toto");
		assertTrue(TaskAttributes.identifiedBy("titi", "toto").apply(att));
		assertFalse(TaskAttributes.identifiedBy("titi", "to").apply(att));
		assertFalse(TaskAttributes.identifiedBy("titi", "totoa").apply(att));
	}

}
