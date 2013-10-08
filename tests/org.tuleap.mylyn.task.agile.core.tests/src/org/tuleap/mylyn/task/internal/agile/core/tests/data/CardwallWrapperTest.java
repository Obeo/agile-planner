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

import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId("1234");

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setStatusId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
		}

		// Retrieval of swimlane item from the cardwall to check that retrieved object has the right behavior
		swimlane = wrapper.getSwimlanes().get(0);

		item = swimlane.getSwimlaneItem();
		assertEquals("123", item.getId());
		assertEquals("Label item", item.getLabel());
		assertEquals(12.5F, item.getInitialEffort(), 0F);
		assertEquals("1234", item.getAssignedMilestoneId());

		List<ColumnWrapper> columns = wrapper.getColumns();
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(10 + i), columns.get(i).getId());
			assertEquals("Column" + i, columns.get(i).getLabel());
			// assertEquals()
		}
		List<SwimlaneWrapper> swimlanes = wrapper.getSwimlanes();
		assertEquals(1, swimlanes.size());
		swimlane = swimlanes.get(0);
		assertEquals("123", swimlane.getSwimlaneItem().getId());
		assertEquals("Label item", swimlane.getSwimlaneItem().getLabel());
		assertEquals(12.5F, swimlane.getSwimlaneItem().getInitialEffort(), 0F);
		assertEquals("1234", swimlane.getSwimlaneItem().getAssignedMilestoneId());

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(200 + i), cards.get(i).getId());
			assertEquals("Label " + (200 + i), cards.get(i).getLabel());
			assertEquals(Integer.toString(10 + i), cards.get(i).getStatusId());
			assertEquals("Value 100" + i, cards.get(i).getFieldValue("100"));
		}
	}

	/**
	 * Tests basic ward wall modifications.
	 */
	@Test
	public void testCardwallModification() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId("1234");

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);
		item = swimlane.getSwimlaneItem();
		item.setLabel("Other label");
		item.setInitialEffort(55.2F);
		item.setAssignedMilestoneId("4321");

		assertEquals("Other label", item.getLabel());
		assertEquals(55.2F, item.getInitialEffort(), 0F);
		assertEquals("4321", item.getAssignedMilestoneId());

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setStatusId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
		}

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.getCard(Integer.toString(200 + i));
			card.setLabel("Other " + (200 + i));
			card.setStatusId(Integer.toString(13 - i));
			card.setFieldValue("100", "Other 100" + i);
		}

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			CardWrapper card = cards.get(i);
			assertEquals(Integer.toString(200 + i), card.getId());
			assertEquals("Other " + (200 + i), card.getLabel());
			assertEquals(Integer.toString(13 - i), card.getStatusId());
			assertEquals("Other 100" + i, card.getFieldValue("100"));
		}
	}

	/**
	 * Tests basic ward wall modifications.
	 */
	@Test
	public void testNotifications() {
		TestChangeListener listener = new TestChangeListener();
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		wrapper.addListener(listener);
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId("1234");

		String itemLabelKey = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_SWIMLANE_ITEM
				+ '-' + AbstractTaskAttributeWrapper.SUFFIX_LABEL;
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(itemLabelKey));
		item.setLabel("Label item"); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(itemLabelKey));
		item.setLabel("Other label"); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(itemLabelKey));

		String itemPointsKey = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_SWIMLANE_ITEM
				+ '-' + SwimlaneItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS;
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(itemPointsKey));
		item.setInitialEffort(12.5F); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(itemPointsKey));
		item.setInitialEffort(10F); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(itemPointsKey));

		String assignedIdKey = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_SWIMLANE_ITEM
				+ '-' + SwimlaneItemWrapper.SUFFIX_ASSIGNED_MILESTONE_ID;
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(assignedIdKey));
		item.setAssignedMilestoneId("1234"); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(assignedIdKey));
		item.setAssignedMilestoneId("4321"); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(assignedIdKey));

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		assertEquals("Other label", item.getLabel());
		assertEquals(10F, item.getInitialEffort(), 0F);
		assertEquals("4321", item.getAssignedMilestoneId());

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setStatusId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
		}

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			String id = Integer.toString(200 + i);
			String cardPrefix = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_CARD_LIST
					+ '-' + id;
			CardWrapper card = swimlane.getCard(id);

			String labelKey = cardPrefix + '-' + SwimlaneWrapper.SUFFIX_LABEL;
			assertEquals(1, listener.getInvocationsCount(labelKey).intValue());
			card.setLabel("Label " + (200 + i)); // Should not notify
			assertEquals(1, listener.getInvocationsCount(labelKey).intValue());
			card.setLabel("Other " + (200 + i)); // Should notify
			assertEquals(2, listener.getInvocationsCount(labelKey).intValue());

			String statusKey = cardPrefix + '-' + CardWrapper.SUFFIX_STATUS_ID;
			assertEquals(1, listener.getInvocationsCount(statusKey).intValue());
			card.setStatusId(Integer.toString(10 + i));
			assertEquals(1, listener.getInvocationsCount(statusKey).intValue());
			card.setStatusId(Integer.toString(13 - i));
			assertEquals(2, listener.getInvocationsCount(statusKey).intValue());

			String fieldKey = cardPrefix + CardWrapper.FIELD_SEPARATOR + "100";
			assertEquals(1, listener.getInvocationsCount(fieldKey).intValue());
			card.setFieldValue("100", "Value 100" + i); // Should not notify
			assertEquals(1, listener.getInvocationsCount(fieldKey).intValue());
			card.setFieldValue("100", "Other 100" + i); // Should notify
			assertEquals(2, listener.getInvocationsCount(fieldKey).intValue());
			card.addFieldValue("100", "Other 100" + i); // Should notify
			assertEquals(3, listener.getInvocationsCount(fieldKey).intValue());
			card.clearFieldValues("100"); // Should notify
			assertEquals(4, listener.getInvocationsCount(fieldKey).intValue());
			assertEquals("", card.getFieldValue("100"));

			// Following should notify only once
			card.addFieldValues("100", Arrays.asList("Other 100" + i, "Some 100" + i, "And again 100" + i));
			assertEquals(5, listener.getInvocationsCount(fieldKey).intValue());
			List<String> fieldValues = card.getFieldValues("100");
			assertEquals(3, fieldValues.size());
			assertEquals("Other 100" + i, fieldValues.get(0));
			assertEquals("Some 100" + i, fieldValues.get(1));
			assertEquals("And again 100" + i, fieldValues.get(2));
		}
	}

	/**
	 * Tests the removal of a listener from the cardwall wrapper.
	 */
	@Test
	public void testRemoveListenerFromCardwallWrapper() {
		TestChangeListener listener = new TestChangeListener();
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		wrapper.addListener(listener);
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		SwimlaneItemWrapper item = swimlane.getSwimlaneItem();
		item.setLabel("Label item");
		item.setInitialEffort(12.5F);
		item.setAssignedMilestoneId("1234");

		String itemLabelKey = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_SWIMLANE_ITEM
				+ '-' + AbstractTaskAttributeWrapper.SUFFIX_LABEL;
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(itemLabelKey));
		item.setLabel("Other label"); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(itemLabelKey));

		wrapper.removeListener(listener);
		item.setLabel("Yet another label"); // Should notify
		// the listener call count must not change anymore
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(itemLabelKey));
	}

	/**
	 * Tests card wall setFieldValue() method.
	 */
	@Test
	public void testSetFieldValue() {
		TestChangeListener listener = new TestChangeListener();
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		wrapper.addListener(listener);
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setFieldValue("100", "Value 100" + i);
		}

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			String id = Integer.toString(200 + i);
			String cardPrefix = CardwallWrapper.SWIMLANE_LIST + "-0-" + SwimlaneWrapper.SUFFIX_CARD_LIST
					+ '-' + id;
			CardWrapper card = swimlane.getCard(id);

			assertEquals("Value 100" + i, card.getFieldValue("100"));
			String fieldKey = cardPrefix + CardWrapper.FIELD_SEPARATOR + "100";
			assertEquals(1, listener.getInvocationsCount(fieldKey).intValue());

			card.setFieldValue("100", "Value 100" + i); // Should not notify
			assertEquals(1, listener.getInvocationsCount(fieldKey).intValue());
			assertEquals("Value 100" + i, card.getFieldValue("100"));

			card.setFieldValue("100", null); // Should not notify
			assertEquals(1, listener.getInvocationsCount(fieldKey).intValue());
			assertEquals("Value 100" + i, card.getFieldValue("100")); // Value must not have changed

			card.setFieldValue("100", "Other 100" + i); // Should notify
			assertEquals(2, listener.getInvocationsCount(fieldKey).intValue());
			assertEquals("Other 100" + i, card.getFieldValue("100")); // Value must have changed
		}
	}

	/**
	 * Tests the behavior of getFieldLabel() and setFieldlabel().
	 */
	@Test
	public void testGetSetFieldLabel() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setFieldLabel("100", "Label 100" + i);
		}

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			String id = Integer.toString(200 + i);
			CardWrapper card = swimlane.getCard(id);

			assertEquals("Label 100" + i, card.getFieldLabel("100"));

			card.setFieldLabel("100", "Label 100" + i);
			assertEquals("Label 100" + i, card.getFieldLabel("100"));

			card.setFieldLabel("100", null);
			assertEquals("Label 100" + i, card.getFieldLabel("100")); // Value must not have changed

			card.setFieldLabel("100", "Other 100" + i);
			assertEquals("Other 100" + i, card.getFieldLabel("100")); // Value must have changed
		}
	}

	/**
	 * Tests the behavior of getFieldLabel() and setFieldlabel().
	 */
	@Test
	public void testGetFieldNeverSet() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("200");
		assertNull(card.getFieldLabel("123"));
		assertNull(card.getFieldValue("123"));
		assertEquals(0, card.getFieldValues("123").size());
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
