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

import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

import static org.junit.Assert.assertEquals;

/**
 * Tests of the cardwall wrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallWrapperTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Tests basic ward wall creation.
	 */
	@Test
	public void testCardwallCreation() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(10 + i, "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane(123);
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId(1234);

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(200 + i);
			card.setLabel("Label " + (200 + i));
			card.setStatusId(10 + i);
			card.addFieldValue(100, "Value 100" + i);
		}

		item = swimlane.getSwimlaneItem();
		assertEquals(123, item.getId());
		assertEquals("Label item", item.getLabel());
		assertEquals(12.5F, item.getInitialEffort(), 0F);
		assertEquals(1234, item.getAssignedMilestoneId().intValue());

		List<ColumnWrapper> columns = wrapper.getColumns();
		for (int i = 0; i < 4; i++) {
			assertEquals(10 + i, columns.get(i).getId());
			assertEquals("Column" + i, columns.get(i).getLabel());
			// assertEquals()
		}
		List<SwimlaneWrapper> swimlanes = wrapper.getSwimlanes();
		assertEquals(1, swimlanes.size());
		swimlane = swimlanes.get(0);
		assertEquals(123, swimlane.getSwimlaneItem().getId());
		assertEquals("Label item", swimlane.getSwimlaneItem().getLabel());
		assertEquals(12.5F, swimlane.getSwimlaneItem().getInitialEffort(), 0F);
		assertEquals(1234, swimlane.getSwimlaneItem().getAssignedMilestoneId().intValue());

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(200 + i, cards.get(i).getId());
			assertEquals("Label " + (200 + i), cards.get(i).getLabel());
			assertEquals(10 + i, cards.get(i).getStatusId().intValue());
			assertEquals("Value 100" + i, cards.get(i).getFieldValue(100));
		}
	}

	/**
	 * Tests basic ward wall modifications.
	 */
	@Test
	public void testCardwallModification() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(10 + i, "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane(123);
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId(1234);

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);
		item = swimlane.getSwimlaneItem();
		item.setLabel("Other label");
		item.setInitialEffort(55.2F);
		item.setAssignedMilestoneId(4321);

		assertEquals("Other label", item.getLabel());
		assertEquals(55.2F, item.getInitialEffort(), 0F);
		assertEquals(4321, item.getAssignedMilestoneId().intValue());

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(200 + i);
			card.setLabel("Label " + (200 + i));
			card.setStatusId(10 + i);
			card.addFieldValue(100, "Value 100" + i);
		}

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.getCard(200 + i);
			card.setLabel("Other " + (200 + i));
			card.setStatusId(13 - i);
			card.setFieldValue(100, "Other 100" + i);
		}

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			CardWrapper card = cards.get(i);
			assertEquals(200 + i, card.getId());
			assertEquals("Other " + (200 + i), card.getLabel());
			assertEquals(13 - i, card.getStatusId().intValue());
			assertEquals("Other 100" + i, card.getFieldValue(100));
		}
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
