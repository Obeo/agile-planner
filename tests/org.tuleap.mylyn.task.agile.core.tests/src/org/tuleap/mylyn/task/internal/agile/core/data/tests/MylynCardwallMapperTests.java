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
package org.tuleap.mylyn.task.internal.agile.core.data.tests;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallTaskMapper;

import static org.junit.Assert.fail;

/**
 * Unit tests of the Mylyn cardwall mapper.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynCardwallMapperTests {
	/**
	 * The task data used for the unit tests.
	 */
	private TaskData taskData;

	/**
	 * The kind of the connector.
	 */
	private final String connectorKind = "agile"; //$NON-NLS-1$

	/**
	 * The URL of the repository.
	 */
	private final String repositoryUrl = "http://www.example.com"; //$NON-NLS-1$

	/**
	 * The identifier of the task.
	 */
	private final String taskId = "TaskId#1234"; //$NON-NLS-1$

	/**
	 * Sets the condition for all the unit tests.
	 */
	@Before
	public void setUp() {
		TaskRepository repository = new TaskRepository(this.connectorKind, this.repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(repository);
		this.taskData = new TaskData(mapper, this.connectorKind, this.repositoryUrl, this.taskId);
	}

	/**
	 * Test the initialization of the empty task data.
	 * <p>
	 * In this test, we will start with an empty task data and see if it is properly initialized with all its
	 * task attribute by the mapper. Those task attributes should have no values.
	 * </p>
	 */
	@Test
	public void testInitializeEmptyTaskData() {
		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);
		mylynCardwallMapper.initializeEmptyTaskData();

		fail();
	}

	/**
	 * Test the retrieval of the kind of the backlog items.
	 */
	@Test
	public void testGetBacklogItemsKind() {
		fail();
	}

	/**
	 * Test the retrieval of all the cardwall states.
	 */
	@Test
	public void testGetAllCardwallStates() {
		fail();
	}

	/**
	 * Test the retrieval of all the cardwall backlog items.
	 */
	@Test
	public void testGetAllCardwallBacklogItems() {
		fail();
	}

	/**
	 * Test the retrieval of all the cardwall artifacts of a specific cardwall backlog item.
	 */
	@Test
	public void testGetAllCardwallArtifacts() {
		fail();
	}

	/**
	 * Test the change of the state of a cardwall artifact.
	 */
	@Test
	public void testGetCardwallState() {
		fail();
	}

	/**
	 * Test the retrieval of all the cardwall artifacts with a given state.
	 */
	@Test
	public void testGetCardwallArtifacts() {
		fail();
	}

	/**
	 * Test the change in state of a cardwall artifact.
	 */
	@Test
	public void testChangeState() {
		fail();
	}

	/**
	 * Test the addition of a new cardwall state.
	 */
	@Test
	public void testAddCardwallState() {
		fail();
	}

	/**
	 * Test the addition of a new backlog item to the cardwall.
	 */
	@Test
	public void testAddCardwallBacklogItem() {
		fail();
	}

	/**
	 * Test the addition of an artifact to a backlog item of the cardwall.
	 */
	@Test
	public void testAddCardwallArtifact() {
		fail();
	}
}
