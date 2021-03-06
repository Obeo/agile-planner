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

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.internal.tasks.core.data.TaskDataState;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.TaskAttributes;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
	 * The task repository for tests.
	 */
	private TaskRepository taskRepository;

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
		assertEquals("123", swimlane.getId());
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
			card.setComplete(false);
			for (int j = 0; j < 3; j++) {
				card.addAllowedColumn(Integer.toString(10 + i + j));
			}
		}

		swimlane = wrapper.getSwimlanes().get(0);

		List<ColumnWrapper> columns = wrapper.getColumns();
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(10 + i), columns.get(i).getId());
			assertEquals("Column" + i, columns.get(i).getLabel());
			// assertEquals()
		}
		List<SwimlaneWrapper> swimlanes = wrapper.getSwimlanes();
		assertEquals(1, swimlanes.size());
		swimlane = swimlanes.get(0);

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(200 + i), cards.get(i).getId());
			assertEquals("Label " + (200 + i), cards.get(i).getLabel());
			assertEquals(Integer.toString(10 + i), cards.get(i).getColumnId());
			assertEquals("Value 100" + i, cards.get(i).getFieldValue("100"));
			assertFalse(cards.get(i).isComplete());
			List<String> allowedColumns = cards.get(i).getAllowedColumnIds();
			for (int j = 0; j < allowedColumns.size(); j++) {
				assertEquals(Integer.toString(10 + i + j), allowedColumns.get(j));
			}

		}
	}

	/**
	 * Tests swimlane.getCards() return the cards of the right swimlane.
	 */
	@Test
	public void testGetCardsOfSwimlane() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
			card.setComplete(false);
			for (int j = 0; j < 3; j++) {
				card.addAllowedColumn(Integer.toString(10 + i + j));
			}
		}
		swimlane = wrapper.addSwimlane("456");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(300 + i));
			card.setLabel("Label " + (300 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
			card.setComplete(false);
			for (int j = 0; j < 3; j++) {
				card.addAllowedColumn(Integer.toString(10 + i + j));
			}
		}

		swimlane = wrapper.getSwimlanes().get(0);

		List<ColumnWrapper> columns = wrapper.getColumns();
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(10 + i), columns.get(i).getId());
			assertEquals("Column" + i, columns.get(i).getLabel());
			// assertEquals()
		}
		List<SwimlaneWrapper> swimlanes = wrapper.getSwimlanes();
		assertEquals(2, swimlanes.size());
		swimlane = swimlanes.get(0);
		assertEquals("123", swimlane.getId());

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(200 + i), cards.get(i).getId());
			assertEquals("Label " + (200 + i), cards.get(i).getLabel());
			assertEquals(Integer.toString(10 + i), cards.get(i).getColumnId());
			assertEquals("Value 100" + i, cards.get(i).getFieldValue("100"));
			assertFalse(cards.get(i).isComplete());
			List<String> allowedColumns = cards.get(i).getAllowedColumnIds();
			for (int j = 0; j < allowedColumns.size(); j++) {
				assertEquals(Integer.toString(10 + i + j), allowedColumns.get(j));
			}
		}

		swimlane = swimlanes.get(1);
		assertEquals("456", swimlane.getId());
		cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(Integer.toString(300 + i), cards.get(i).getId());
			assertEquals("Label " + (300 + i), cards.get(i).getLabel());
			assertEquals(Integer.toString(10 + i), cards.get(i).getColumnId());
			assertEquals("Value 100" + i, cards.get(i).getFieldValue("100"));
			assertFalse(cards.get(i).isComplete());
			List<String> allowedColumns = cards.get(i).getAllowedColumnIds();
			for (int j = 0; j < allowedColumns.size(); j++) {
				assertEquals(Integer.toString(10 + i + j), allowedColumns.get(j));
			}
		}
	}

	/**
	 * Tests basic card wall modifications.
	 */
	@Test
	public void testCardwallModification() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		assertEquals("123", swimlane.getId());

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
			card.setComplete(false);
			for (int j = 0; j < 3; j++) {
				card.addAllowedColumn(Integer.toString(10 + i + j));
			}
		}

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.getCard(Integer.toString(200 + i));
			card.setLabel("Other " + (200 + i));
			card.setColumnId(Integer.toString(13 - i));
			card.setFieldValue("100", "Other 100" + i);
			card.setComplete(true);
			card.clearAllowedColumnIds();
			for (int j = 0; j < 3; j++) {
				card.addAllowedColumn(Integer.toString(13 - i + j));
			}
		}

		List<CardWrapper> cards = swimlane.getCards();
		assertEquals(4, cards.size());
		for (int i = 0; i < 4; i++) {
			CardWrapper card = cards.get(i);
			assertEquals(Integer.toString(200 + i), card.getId());
			assertEquals("Other " + (200 + i), card.getLabel());
			assertEquals(Integer.toString(13 - i), card.getColumnId());
			assertEquals("Other 100" + i, card.getFieldValue("100"));
			assertTrue(cards.get(i).isComplete());
			List<String> allowedColumns = cards.get(i).getAllowedColumnIds();
			for (int j = 0; j < allowedColumns.size(); j++) {
				assertEquals(Integer.toString(13 - i + j), allowedColumns.get(j));
			}

		}
	}

	/**
	 * Tests basic card wall modifications.
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
		assertEquals("123", swimlane.getId());

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
		}

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			String id = Integer.toString(200 + i);
			String cardPrefix = SwimlaneWrapper.PREFIX_SWIMLANE + "123-c-" + id;
			CardWrapper card = swimlane.getCard(id);

			String labelKey = cardPrefix + '-' + SwimlaneWrapper.SUFFIX_LABEL;
			assertEquals(1, listener.getInvocationsCount(labelKey).intValue());
			card.setLabel("Label " + (200 + i)); // Should not notify
			assertEquals(1, listener.getInvocationsCount(labelKey).intValue());
			card.setLabel("Other " + (200 + i)); // Should notify
			assertEquals(2, listener.getInvocationsCount(labelKey).intValue());

			String fieldKey = cardPrefix + '-' + CardWrapper.FIELD_SEPARATOR + "100";
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
		assertEquals("123", swimlane.getId());
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setFieldValue("100", "Value 100" + i);
		}

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		for (int i = 0; i < 4; i++) {
			String id = Integer.toString(200 + i);
			String cardPrefix = SwimlaneWrapper.PREFIX_SWIMLANE + "123" + "-c-" + id;
			CardWrapper card = swimlane.getCard(id);

			assertEquals("Value 100" + i, card.getFieldValue("100"));
			String fieldKey = cardPrefix + '-' + CardWrapper.FIELD_SEPARATOR + "100";
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
		assertEquals("123", swimlane.getId());
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
		assertEquals("123", swimlane.getId());
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
		taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}

	/**
	 * Tests card wall setFieldValues() method.
	 */
	@Test
	public void testSetFieldValues() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("456");
		card.setFieldValues("100", Lists.newArrayList("1", "2", "3"));

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		card = swimlane.getCard("456");

		assertEquals("1", card.getFieldValue("100"));
		List<String> fieldValues = card.getFieldValues("100");
		assertEquals(3, fieldValues.size());
		assertEquals("1", fieldValues.get(0));
		assertEquals("2", fieldValues.get(1));
		assertEquals("3", fieldValues.get(2));
	}

	/**
	 * Tests card wall setFieldAttributes() method.
	 */
	@Test
	public void testGetFieldAttributes() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("456");
		card.setFieldValues("100", Lists.newArrayList("1", "2", "3"));

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		card = swimlane.getCard("456");

		TaskAttribute fieldAttribute = card.getFieldAttribute("100");
		assertNotNull(fieldAttribute);
		assertSame(taskData.getRoot(), fieldAttribute.getParentAttribute());
		assertEquals(3, fieldAttribute.getValues().size());

		List<TaskAttribute> fieldAttributes = card.getFieldAttributes();
		assertEquals(1, fieldAttributes.size());
	}

	/**
	 * Tests card wall addField method.
	 */
	@Test
	public void testAddField() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("456");
		card.addField("100", "Field 100", TaskAttribute.TYPE_SHORT_RICH_TEXT);

		// Retrieval of swimlane item from the cardwall to modify it
		swimlane = wrapper.getSwimlanes().get(0);

		// Modification of the cards created previously
		card = swimlane.getCard("456");

		TaskAttribute fieldAttribute = card.getFieldAttribute("100");
		assertNotNull(fieldAttribute);
		assertSame(taskData.getRoot(), fieldAttribute.getParentAttribute());
		assertEquals(0, fieldAttribute.getValues().size());
		assertEquals("Field 100", card.getFieldLabel("100"));
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, fieldAttribute.getMetaData().getType());

		// Check that setting the value after having created the field works right
		card.setFieldValue("100", "Some value");
		fieldAttribute = card.getFieldAttribute("100");
		assertNotNull(fieldAttribute);
		assertEquals(1, fieldAttribute.getValues().size());
		assertEquals("Some value", fieldAttribute.getValue());
		assertEquals("Field 100", card.getFieldLabel("100"));
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, fieldAttribute.getMetaData().getType());
	}

	/**
	 * Test the behaviour fo set & get Allowed column ids.
	 */
	@Test
	public void testAllowedColumnIds() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("456");

		assertEquals(0, card.getAllowedColumnIds().size());

		card.addAllowedColumn("10");
		assertEquals(1, card.getAllowedColumnIds().size());
		assertEquals("10", card.getAllowedColumnIds().get(0));

		card.addAllowedColumn("11");
		assertEquals(2, card.getAllowedColumnIds().size());
		assertEquals("10", card.getAllowedColumnIds().get(0));
		assertEquals("11", card.getAllowedColumnIds().get(1));
	}

	/**
	 * Tests the manipulation of the card artifact id.
	 */
	@Test
	public void testCardArtifactId() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		CardWrapper card = swimlane.addCard("456");

		card.setArtifactId("ArtId");
		assertEquals("ArtId", card.getArtifactId());

		TaskAttribute att = taskData.getRoot().getAttribute("mta_swi-123-c-456-art_id");
		assertNotNull(att);
		assertEquals(1, att.getValues().size());
		assertEquals("ArtId", att.getValue());
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for an empty swimlane.
	 */
	@Test
	public void testSwimlaneNumberOfCardsEmpty() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		assertEquals(0, swimlane.getNumberOfCards());
		assertEquals(0, swimlane.getNumberOfAssignedCards());
		assertEquals(0, swimlane.getNumberOfCards(true));
		assertEquals(0, swimlane.getNumberOfCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with assigned incomplete
	 * cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsAssignedIncompleteCards() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.setComplete(false);
		}
		assertEquals(4, swimlane.getNumberOfCards());
		assertEquals(4, swimlane.getNumberOfAssignedCards());
		assertEquals(0, swimlane.getNumberOfCards(true));
		assertEquals(4, swimlane.getNumberOfCards(false));
		assertEquals(0, swimlane.getNumberOfAssignedCards(true));
		assertEquals(4, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with assigned incomplete
	 * cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsAssignedCompleteCards() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.setComplete(true);
		}
		assertEquals(4, swimlane.getNumberOfCards());
		assertEquals(4, swimlane.getNumberOfAssignedCards());
		assertEquals(4, swimlane.getNumberOfCards(true));
		assertEquals(0, swimlane.getNumberOfCards(false));
		assertEquals(4, swimlane.getNumberOfAssignedCards(true));
		assertEquals(0, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with unassigned incomplete
	 * cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsUnassignedIncompleteCards() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			// card.setColumnId(Integer.toString(10 + i));
			card.setComplete(false);
		}
		assertEquals(4, swimlane.getNumberOfCards());
		assertEquals(0, swimlane.getNumberOfAssignedCards());
		assertEquals(0, swimlane.getNumberOfCards(true));
		assertEquals(4, swimlane.getNumberOfCards(false));
		assertEquals(0, swimlane.getNumberOfAssignedCards(true));
		assertEquals(0, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with unassigned and
	 * implicitely incomplete cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsUnassignedImplicitelyIncompleteCards() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			// card.setColumnId(Integer.toString(10 + i));
			// if complete is not set on a card, it should be like setting false
			// card.setComplete(false);
		}
		assertEquals(4, swimlane.getNumberOfCards());
		assertEquals(0, swimlane.getNumberOfAssignedCards());
		assertEquals(0, swimlane.getNumberOfCards(true));
		assertEquals(4, swimlane.getNumberOfCards(false));
		assertEquals(0, swimlane.getNumberOfAssignedCards(true));
		assertEquals(0, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with unassigned complete
	 * cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsUnassignedCompleteCards() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");
		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			// card.setColumnId(Integer.toString(10 + i));
			card.setComplete(true);
		}
		assertEquals(4, swimlane.getNumberOfCards());
		assertEquals(0, swimlane.getNumberOfAssignedCards());
		assertEquals(4, swimlane.getNumberOfCards(true));
		assertEquals(0, swimlane.getNumberOfCards(false));
		assertEquals(0, swimlane.getNumberOfAssignedCards(true));
		assertEquals(0, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests the getNumberOfCards() method of {@link SwimlaneWrapper} for swimlane with different cards.
	 */
	@Test
	public void testSwimlaneNumberOfCardsMix() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");

		// One card assigned true
		int i = 0;
		CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setColumnId(Integer.toString(10 + i));
		card.setComplete(true);
		i++;

		// One card assigned false
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setColumnId(Integer.toString(10 + i));
		card.setComplete(false);
		i++;

		// One card unassigned true
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setComplete(true);
		i++;

		// One card unassigned false
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setComplete(false);
		i++;

		// One card assigned false implicitely
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setColumnId(Integer.toString(10));

		assertEquals(5, swimlane.getNumberOfCards());
		assertEquals(3, swimlane.getNumberOfAssignedCards());
		assertEquals(2, swimlane.getNumberOfCards(true));
		assertEquals(3, swimlane.getNumberOfCards(false));
		assertEquals(1, swimlane.getNumberOfAssignedCards(true));
		assertEquals(2, swimlane.getNumberOfAssignedCards(false));
	}

	/**
	 * Tests of the mechanism used to mark actual modifications of the TaskData.
	 */
	@Test
	public void testMarkChangedAttributes() {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			wrapper.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = wrapper.addSwimlane("123");

		// One card assigned true
		int i = 0;
		CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setColumnId(Integer.toString(10 + i));
		card.setComplete(true);
		i++;

		// One card assigned false
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setColumnId(Integer.toString(10 + i));
		card.setComplete(false);
		i++;

		// One card unassigned true
		card = swimlane.addCard(Integer.toString(200 + i));
		card.setLabel("Label " + (200 + i));
		card.setComplete(true);
		i++;

		TaskDataState state = new TaskDataState("kind", "repository", "id");
		state.setRepositoryData(TaskDataState.createCopy(taskData));
		state.revert();
		final TaskDataModel model = new TaskDataModel(taskRepository,
				new TaskTask("kind", "repository", "id"), state);
		TaskData taskDataToUse = model.getTaskData();
		wrapper = new CardwallWrapper(taskDataToUse.getRoot());
		wrapper.addListener(new ITaskAttributeChangeListener() {
			@Override
			public void attributeChanged(TaskAttribute attribute) {
				model.attributeChanged(attribute);
			}
		});
		wrapper.markChanges(model, TaskAttributes.prefixedBy(SwimlaneWrapper.PREFIX_SWIMLANE));
		// Nothing should have changed
		List<CardWrapper> cards = wrapper.getSwimlanes().get(0).getCards();

		for (CardWrapper aCard : cards) {
			// Check that there is no CHANGED sub-attribute
			assertTrue(aCard.getColumnIdTaskAttribute() == null
					|| aCard.getColumnIdTaskAttribute().getAttributes().isEmpty());
		}

		cards.get(0).setColumnId("10"); // Unchanged
		cards.get(1).setColumnId("10"); // Changed from 11 to 10
		cards.get(2).setColumnId("10"); // Changed from null to 10
		wrapper.markChanges(model, TaskAttributes.prefixedBy(SwimlaneWrapper.PREFIX_SWIMLANE));
		assertTrue(cards.get(0).getColumnIdTaskAttribute().getAttributes().isEmpty()); // Not changed
		assertNotNull(cards.get(1).getColumnIdTaskAttribute().getAttribute(AbstractNotifyingWrapper.CHANGED));
		assertNotNull(cards.get(1).getColumnIdTaskAttribute().getAttribute(AbstractNotifyingWrapper.CHANGED));
	}
}
