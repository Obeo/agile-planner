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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallArtifact;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallBacklogItem;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallState;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallStateMapping;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallStateValue;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallTaskMapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests of the Mylyn cardwall mapper.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class CardwallMapperTests {
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

		TaskAttribute attribute = this.taskData.getRoot();

		Collection<TaskAttribute> taskAttributesList = attribute.getAttributes().values();

		Iterator<TaskAttribute> iterator = taskAttributesList.iterator();

		TaskAttribute taskAttribute1 = iterator.next();

		TaskAttribute taskAttribute2 = iterator.next();

		TaskAttribute taskAttribute3 = iterator.next();

		assertEquals(3, taskAttributesList.size());

		assertEquals(TaskAttribute.STATUS, taskAttribute1.getId());

		assertEquals(TaskAttribute.TASK_KIND, taskAttribute2.getId());

		assertEquals(IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, taskAttribute3.getId());

	}

	/**
	 * Test the retrieval of the kind of the backlog items.
	 */
	@Test
	public void testGetBacklogItemsKind() {

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		mylynCardwallMapper.setBacklogItemsKind("My new Backlog Items kind"); //$NON-NLS-1$

		assertEquals("My new Backlog Items kind", mylynCardwallMapper.getBacklogItemsKind()); //$NON-NLS-1$

	}

	/**
	 * Test the retrieval of all the cardwall states.
	 */
	@Test
	public void testGetAllCardwallStates() {

		// Create the first state

		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(11));
		list.add(new Integer(12));
		list.add(new Integer(13));

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(new Integer(21));
		list2.add(new Integer(22));
		list2.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping1 = new CardwallStateMapping(1, list);

		CardwallStateMapping cardwallStateMapping2 = new CardwallStateMapping(2, list2);

		List<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping1);

		mappingsList.add(cardwallStateMapping2);

		// Create the second state

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(new Integer(11));
		list3.add(new Integer(12));
		list3.add(new Integer(13));

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(new Integer(21));
		list4.add(new Integer(22));
		list4.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping3 = new CardwallStateMapping(1, list3);

		CardwallStateMapping cardwallStateMapping4 = new CardwallStateMapping(2, list4);

		List<CardwallStateMapping> mappingsList2 = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping3);

		mappingsList.add(cardwallStateMapping4);

		CardwallState cardwallState = new CardwallState(100, "First State", mappingsList); //$NON-NLS-1$

		CardwallState cardwallState2 = new CardwallState(200, "Second State", mappingsList2); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		// add the states to the cardwall

		mylynCardwallMapper.addCardwallState(cardwallState);

		mylynCardwallMapper.addCardwallState(cardwallState2);

		Collection<CardwallState> cardwallStates = mylynCardwallMapper.getAllCardwallStates();

		// get the Cardwall states

		Iterator<CardwallState> iterator = cardwallStates.iterator();

		CardwallState cardwallState3 = iterator.next();

		CardwallState cardwallState4 = iterator.next();

		assertEquals(100, cardwallState3.getId());

		assertEquals(200, cardwallState4.getId());

	}

	/**
	 * Test the retrieval of all the cardwall backlog items.
	 */
	@Test
	public void testGetAllCardwallBacklogItems() {

		// Create the cardwall state values

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(1, 20, "The first state value"); //$NON-NLS-1$

		CardwallStateValue cardwallStateValue2 = new CardwallStateValue(2, 20, "The second state value"); //$NON-NLS-1$

		// Create the cardwall artifacts

		CardwallArtifact artifact1 = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 15, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact2 = new CardwallArtifact(20,
				"The second artifact", "The second artifact", 16, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact3 = new CardwallArtifact(30,
				"The third artifact", "The third artifact kind", 17, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact4 = new CardwallArtifact(40,
				"The fourth artifact", "The fourth artifact", 18, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		// Create the cardwall backlog items

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallBacklogItem cardwallBacklogItem2 = new CardwallBacklogItem(2000, "Second backlog item", //$NON-NLS-1$
				"Second backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem1);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem2);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact1);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact2);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem2, artifact3);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem2, artifact4);

		Collection<CardwallBacklogItem> cardwallBacklogItems = mylynCardwallMapper
				.getAllCardwallBacklogItems();

		// get the Cardwall Backlog items

		Iterator<CardwallBacklogItem> iterator = cardwallBacklogItems.iterator();

		CardwallBacklogItem cardwallBacklogItem4 = iterator.next();

		CardwallBacklogItem cardwallBacklogItem5 = iterator.next();

		assertEquals(1000, cardwallBacklogItem4.getId());

		assertEquals(2000, cardwallBacklogItem5.getId());

	}

	/**
	 * Test the retrieval of all the cardwall artifacts of a specific cardwall backlog item.
	 */
	@Test
	public void testGetAllCardwallArtifacts() {

		// Create the cardwall state values

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(1, 20, "The first state value"); //$NON-NLS-1$

		CardwallStateValue cardwallStateValue2 = new CardwallStateValue(2, 20, "The second state value"); //$NON-NLS-1$

		// Create the cardwall artifacts

		CardwallArtifact artifact1 = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 15, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact2 = new CardwallArtifact(20,
				"The second artifact", "The second artifact", 16, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact3 = new CardwallArtifact(30,
				"The third artifact", "The third artifact kind", 17, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact4 = new CardwallArtifact(40,
				"The fourth artifact", "The fourth artifact", 18, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		// Create the cardwall backlog items

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallBacklogItem cardwallBacklogItem2 = new CardwallBacklogItem(2000, "Second backlog item", //$NON-NLS-1$
				"Second backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact1);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact2);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem2, artifact3);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem2, artifact4);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem1);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem2);

		Collection<CardwallArtifact> cardwallArtifacts = mylynCardwallMapper
				.getAllCardwallArtifacts(cardwallBacklogItem2);

		// get the Cardwall artifacts

		Iterator<CardwallArtifact> iterator = cardwallArtifacts.iterator();

		CardwallArtifact cardwallArtifact5 = iterator.next();
		assertEquals(30, cardwallArtifact5.getId());

		CardwallArtifact cardwallArtifact6 = iterator.next();
		assertEquals(40, cardwallArtifact6.getId());

		Collection<CardwallArtifact> cardwallArtifacts2 = mylynCardwallMapper
				.getAllCardwallArtifacts(cardwallBacklogItem1);

		Iterator<CardwallArtifact> iterator2 = cardwallArtifacts2.iterator();

		CardwallArtifact cardwallArtifact7 = iterator2.next();
		assertEquals(10, cardwallArtifact7.getId());

		CardwallArtifact cardwallArtifact8 = iterator2.next();
		assertEquals(20, cardwallArtifact8.getId());

	}

	/**
	 * Test getting the state of a cardwall artifact.
	 */
	@Test
	public void testGetCardwallState() {

		// Create the first state

		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(11));
		list.add(new Integer(12));
		list.add(new Integer(13));

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(new Integer(21));
		list2.add(new Integer(22));
		list2.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping1 = new CardwallStateMapping(1, list);

		CardwallStateMapping cardwallStateMapping2 = new CardwallStateMapping(2, list2);

		List<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping1);

		mappingsList.add(cardwallStateMapping2);

		// Create the second state

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(new Integer(31));
		list3.add(new Integer(32));
		list3.add(new Integer(33));

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(new Integer(41));
		list4.add(new Integer(42));
		list4.add(new Integer(43));

		CardwallStateMapping cardwallStateMapping3 = new CardwallStateMapping(3, list3);

		CardwallStateMapping cardwallStateMapping4 = new CardwallStateMapping(4, list4);

		List<CardwallStateMapping> mappingsList2 = new ArrayList<CardwallStateMapping>();

		mappingsList2.add(cardwallStateMapping3);

		mappingsList2.add(cardwallStateMapping4);

		CardwallState cardwallState = new CardwallState(100, "First State", mappingsList); //$NON-NLS-1$

		CardwallState cardwallState2 = new CardwallState(200, "Second State", mappingsList2); //$NON-NLS-1$

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(22, 20, "The first state value"); //$NON-NLS-1$

		CardwallArtifact artifact = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 2, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact);

		mylynCardwallMapper.addCardwallState(cardwallState);

		mylynCardwallMapper.addCardwallState(cardwallState2);

		CardwallState resultCardwallState = mylynCardwallMapper.getCardwallState(artifact);

		assertEquals(100, resultCardwallState.getId());

	}

	/**
	 * Test the retrieval of all the cardwall artifacts with a given state.
	 */
	@Test
	public void testGetCardwallArtifacts() {

		// Create the first state

		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(11));
		list.add(new Integer(12));
		list.add(new Integer(13));

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(new Integer(21));
		list2.add(new Integer(12));
		list2.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping1 = new CardwallStateMapping(1, list);

		CardwallStateMapping cardwallStateMapping2 = new CardwallStateMapping(2, list2);

		List<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping1);

		mappingsList.add(cardwallStateMapping2);

		// Create the second state

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(new Integer(11));
		list3.add(new Integer(12));
		list3.add(new Integer(13));

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(new Integer(21));
		list4.add(new Integer(22));
		list4.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping3 = new CardwallStateMapping(3, list3);

		CardwallStateMapping cardwallStateMapping4 = new CardwallStateMapping(4, list4);

		mappingsList.add(cardwallStateMapping3);

		mappingsList.add(cardwallStateMapping4);

		CardwallState cardwallState = new CardwallState(100, "First State", mappingsList); //$NON-NLS-1$

		// Create the cardwall state values

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(12, 20, "The first state value"); //$NON-NLS-1$

		CardwallStateValue cardwallStateValue2 = new CardwallStateValue(21, 20, "The second state value"); //$NON-NLS-1$

		// Create the cardwall artifacts

		CardwallArtifact artifact1 = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 15, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact2 = new CardwallArtifact(20,
				"The second artifact", "The second artifact", 2, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact3 = new CardwallArtifact(30,
				"The third artifact", "The third artifact kind", 17, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact4 = new CardwallArtifact(40,
				"The fourth artifact", "The fourth artifact", 18, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		// Create the cardwall backlog item

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact1);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact2);

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact3);
		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact4);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem1);

		Collection<CardwallArtifact> resultArtifactCollection = mylynCardwallMapper.getCardwallArtifacts(
				cardwallBacklogItem1, cardwallState);

		Iterator<CardwallArtifact> iterator = resultArtifactCollection.iterator();

		CardwallArtifact firstCardwallartifact = iterator.next();

		assertEquals(20, firstCardwallartifact.getId());

		assertEquals(1, resultArtifactCollection.size());

	}

	/**
	 * Test the change in state of a cardwall artifact.
	 */
	@Test
	public void testChangeState() {

		// Create the state

		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(11));
		list.add(new Integer(12));
		list.add(new Integer(13));

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(new Integer(21));
		list2.add(new Integer(22));
		list2.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping1 = new CardwallStateMapping(1, list);

		CardwallStateMapping cardwallStateMapping2 = new CardwallStateMapping(2, list2);

		List<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping1);

		mappingsList.add(cardwallStateMapping2);

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(new Integer(11));
		list3.add(new Integer(12));
		list3.add(new Integer(13));

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(new Integer(21));
		list4.add(new Integer(22));
		list4.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping3 = new CardwallStateMapping(3, list3);

		CardwallStateMapping cardwallStateMapping4 = new CardwallStateMapping(4, list4);

		mappingsList.add(cardwallStateMapping3);

		mappingsList.add(cardwallStateMapping4);

		CardwallState cardwallState = new CardwallState(100, "First State", mappingsList); //$NON-NLS-1$

		// the artifact

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(1, 20, "The first state value"); //$NON-NLS-1$

		CardwallArtifact artifact = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 1, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		// Before changing the artifact state

		assertEquals(1, artifact.getCardwallStateValue().getFieldId());

		mylynCardwallMapper.changeState(artifact, cardwallState);

		// After changing the artifact state

		assertEquals(11, artifact.getCardwallStateValue().getFieldId());

	}

	/**
	 * Test the addition of a new cardwall state.
	 */
	@Test
	public void testAddCardwallState() {

		// Create the first state

		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(11));
		list.add(new Integer(12));
		list.add(new Integer(13));

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(new Integer(21));
		list2.add(new Integer(22));
		list2.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping1 = new CardwallStateMapping(1, list);

		CardwallStateMapping cardwallStateMapping2 = new CardwallStateMapping(2, list2);

		List<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping1);

		mappingsList.add(cardwallStateMapping2);

		// Create the second state

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(new Integer(11));
		list3.add(new Integer(12));
		list3.add(new Integer(13));

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(new Integer(21));
		list4.add(new Integer(22));
		list4.add(new Integer(23));

		CardwallStateMapping cardwallStateMapping3 = new CardwallStateMapping(3, list3);

		CardwallStateMapping cardwallStateMapping4 = new CardwallStateMapping(4, list4);

		List<CardwallStateMapping> mappingsList2 = new ArrayList<CardwallStateMapping>();

		mappingsList.add(cardwallStateMapping3);

		mappingsList.add(cardwallStateMapping4);

		CardwallState cardwallState = new CardwallState(100, "First State", mappingsList); //$NON-NLS-1$

		CardwallState cardwallState2 = new CardwallState(200, "Second State", mappingsList2); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		// add the states to the cardwall

		CardwallState.getObjectsList().removeAll(mylynCardwallMapper.getAllCardwallStates());

		mylynCardwallMapper.addCardwallState(cardwallState);

		mylynCardwallMapper.addCardwallState(cardwallState2);

		Collection<CardwallState> cardwallStates = mylynCardwallMapper.getAllCardwallStates();

		// get the Cardwall states

		Iterator<CardwallState> iterator = cardwallStates.iterator();

		CardwallState cardwallState3 = iterator.next();

		CardwallState cardwallState4 = iterator.next();

		assertEquals(100, cardwallState3.getId());

		assertEquals(200, cardwallState4.getId());
	}

	/**
	 * Test the addition of a new backlog item to the cardwall.
	 */
	@Test
	public void testAddCardwallBacklogItem() {

		// Create the cardwall backlog items

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallBacklogItem cardwallBacklogItem2 = new CardwallBacklogItem(2000, "Second backlog item", //$NON-NLS-1$
				"Second backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		CardwallBacklogItem.getObjectsList().removeAll(mylynCardwallMapper.getAllCardwallBacklogItems());

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem1);

		mylynCardwallMapper.addCardwallBacklogItem(cardwallBacklogItem2);

		Collection<CardwallBacklogItem> cardwallBacklogItems = mylynCardwallMapper
				.getAllCardwallBacklogItems();

		// get the Cardwall backlog items

		Iterator<CardwallBacklogItem> iterator = cardwallBacklogItems.iterator();

		CardwallBacklogItem cardwallBacklogItem3 = iterator.next();

		CardwallBacklogItem cardwallBacklogItem4 = iterator.next();

		assertEquals(1000, cardwallBacklogItem3.getId());

		assertEquals(2000, cardwallBacklogItem4.getId());

	}

	/**
	 * Test the addition of an artifact to a backlog item of the cardwall.
	 */
	@Test
	public void testAddCardwallArtifact() {
		// Create the cardwall state values

		CardwallStateValue cardwallStateValue1 = new CardwallStateValue(12, 20, "The first state value"); //$NON-NLS-1$

		CardwallStateValue cardwallStateValue2 = new CardwallStateValue(21, 20, "The second state value"); //$NON-NLS-1$

		CardwallArtifact.getObjectsList().clear();

		CardwallArtifact artifact1 = new CardwallArtifact(10,
				"The first artifact", "The first artifact kind", 15, cardwallStateValue1); //$NON-NLS-1$ //$NON-NLS-2$

		CardwallArtifact artifact2 = new CardwallArtifact(20,
				"The second artifact", "The second artifact", 2, cardwallStateValue2); //$NON-NLS-1$ //$NON-NLS-2$

		// Create the cardwall backlog item

		CardwallBacklogItem cardwallBacklogItem1 = new CardwallBacklogItem(1000, "First backlog item", //$NON-NLS-1$
				"First backlog item kind", null); //$NON-NLS-1$

		CardwallTaskMapper mylynCardwallMapper = new CardwallTaskMapper(taskData, true);

		// test adding the first artifact

		assertEquals(0, mylynCardwallMapper.getAllCardwallArtifacts(cardwallBacklogItem1).size());

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact1);

		Collection<CardwallArtifact> firstartifactsCollection = mylynCardwallMapper
				.getAllCardwallArtifacts(cardwallBacklogItem1);

		Iterator<CardwallArtifact> iterator = firstartifactsCollection.iterator();

		CardwallArtifact firstCardwallArtifact = iterator.next();

		assertEquals(10, firstCardwallArtifact.getId());

		assertEquals(1, firstartifactsCollection.size());

		// test adding the second artifact

		mylynCardwallMapper.addCardwallBacklogArtifact(cardwallBacklogItem1, artifact2);

		Collection<CardwallArtifact> secondartifactsCollection = mylynCardwallMapper
				.getAllCardwallArtifacts(cardwallBacklogItem1);

		Iterator<CardwallArtifact> iterator2 = secondartifactsCollection.iterator();

		@SuppressWarnings("unused")
		CardwallArtifact intermediateCardwallArtifact = iterator2.next();

		CardwallArtifact secondCardwallArtifact = iterator2.next();

		assertEquals(20, secondCardwallArtifact.getId());

		assertEquals(2, secondartifactsCollection.size());

	}
}
