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

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardFilter;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the {@link CardFilter} class.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFilterTests {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * The task repository for tests.
	 */
	private TaskRepository taskRepository;

	/**
	 * The cardwall used for tests.
	 */
	private CardwallWrapper cardwall;

	/**
	 * The swimlane wrapper used for tests.
	 */
	private SwimlaneWrapper swimlane;

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
		cardwall = new CardwallWrapper(taskData.getRoot());
		swimlane = cardwall.addSwimlane("swi1");
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testNullCriterion() {
		new CardFilter(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyCriterion() {
		new CardFilter("");
	}

	@Test
	public void testCriterionMatchesEmptyCardWithoutDisplayId() {
		CardWrapper card = swimlane.addCard("1");
		// By default, displayId = Id
		assertTrue(new CardFilter("1").apply(card));
		assertFalse(new CardFilter("a").apply(card));
	}

	@Test
	public void testCriterionMatchesDisplayId() {
		CardWrapper card = swimlane.addCard("1");
		card.setDisplayId("a");
		assertTrue(new CardFilter("a").apply(card));
		// ID is not matched if display ID is set
		assertFalse(new CardFilter("1").apply(card));
		assertFalse(new CardFilter("b").apply(card));
	}

	@Test
	public void testCriterionMatchesLabel() {
		CardWrapper card = swimlane.addCard("1");
		card.setLabel("SoMe LaBeL");
		assertTrue(new CardFilter("SoMe LaBeL").apply(card));
		assertTrue(new CardFilter("SOME LABEL").apply(card));
		assertTrue(new CardFilter("some label").apply(card));
		assertTrue(new CardFilter("SoMe").apply(card));
		assertTrue(new CardFilter("some").apply(card));
		assertTrue(new CardFilter("SOME").apply(card));
		assertTrue(new CardFilter("label").apply(card));
		assertTrue(new CardFilter("LABEL").apply(card));
		assertTrue(new CardFilter(" ").apply(card));
		assertFalse(new CardFilter("Any label").apply(card));
	}

	@Test
	public void testCriterionMatchesOneOfOneSingleValue() {
		CardWrapper card = swimlane.addCard("1");
		card.addField("f1", "Field 1", "string");
		card.setFieldValue("f1", "Some Value");
		assertTrue(new CardFilter("SoMe Value").apply(card));
		assertTrue(new CardFilter("SOME VALUE").apply(card));
		assertTrue(new CardFilter("some value").apply(card));
		assertTrue(new CardFilter("SoMe").apply(card));
		assertTrue(new CardFilter("some").apply(card));
		assertTrue(new CardFilter("SOME").apply(card));
		assertTrue(new CardFilter("value").apply(card));
		assertTrue(new CardFilter("VALUE").apply(card));
		assertTrue(new CardFilter(" ").apply(card));
		assertFalse(new CardFilter("Any value").apply(card));
	}

	@Test
	public void testCriterionMatchesOneOfOneMultipleValues() {
		CardWrapper card = swimlane.addCard("1");
		card.addField("f1", "Field 1", "string");
		card.setFieldValues("f1", Arrays.asList("Some Value", "Other Value"));
		assertTrue(new CardFilter("SoMe Value").apply(card));
		assertTrue(new CardFilter("SOME VALUE").apply(card));
		assertTrue(new CardFilter("some value").apply(card));
		assertTrue(new CardFilter("SoMe").apply(card));
		assertTrue(new CardFilter("some").apply(card));
		assertTrue(new CardFilter("SOME").apply(card));
		assertTrue(new CardFilter("value").apply(card));
		assertTrue(new CardFilter("VALUE").apply(card));
		assertTrue(new CardFilter("Other Value").apply(card));
		assertTrue(new CardFilter("other value").apply(card));
		assertTrue(new CardFilter("OTHER VALUE").apply(card));
		assertTrue(new CardFilter("Other").apply(card));
		assertTrue(new CardFilter("other").apply(card));
		assertTrue(new CardFilter("OTHER").apply(card));
		assertTrue(new CardFilter(" ").apply(card));
		assertFalse(new CardFilter("Any value").apply(card));
	}

	@Test
	public void testCriterionMatchesOneOfOneSingleValueWithOptions() {
		CardWrapper card = swimlane.addCard("1");
		card.addField("f1", "Field 1", "sb");
		TaskAttribute att = card.getFieldAttribute("f1");
		att.putOption("1", "Label 1");
		att.putOption("3", "Label 2");
		card.setFieldValue("f1", "3"); // Label 2
		assertTrue(new CardFilter("Label 2").apply(card));
		assertTrue(new CardFilter("label 2").apply(card));
		assertTrue(new CardFilter("LABEL 2").apply(card));
		assertTrue(new CardFilter("2").apply(card));
		assertTrue(new CardFilter("1").apply(card)); // because of the card ID
		assertTrue(new CardFilter(" ").apply(card));
		assertFalse(new CardFilter("Label 1").apply(card));
		// Even though it's the ID of the value set, only the label must be used for matching
		assertFalse(new CardFilter("3").apply(card));
	}

	@Test
	public void testCriterionMatchesOneOfOneMultipleValuesWithOptions() {
		CardWrapper card = swimlane.addCard("1");
		card.addField("f1", "Field 1", "msb");
		TaskAttribute att = card.getFieldAttribute("f1");
		att.putOption("1", "Label 1");
		att.putOption("3", "Label 2");
		att.putOption("4", "Label 4");
		att.putOption("5", "Label 5");
		card.setFieldValues("f1", Arrays.asList("3", "4")); // Label 2, Label 4
		assertTrue(new CardFilter("Label 2").apply(card));
		assertTrue(new CardFilter("label 2").apply(card));
		assertTrue(new CardFilter("LABEL 2").apply(card));
		assertTrue(new CardFilter("Label 4").apply(card));
		assertTrue(new CardFilter("label 4").apply(card));
		assertTrue(new CardFilter("LABEL 4").apply(card));
		assertTrue(new CardFilter("2").apply(card)); // "Label 2" contains "2"
		assertTrue(new CardFilter("4").apply(card)); // "Label 4" contains "4", not because option 4 is set
		assertTrue(new CardFilter("1").apply(card)); // because of the card ID
		assertTrue(new CardFilter(" ").apply(card));
		assertFalse(new CardFilter("Label 1").apply(card));
		assertFalse(new CardFilter("Label 5").apply(card));
		assertFalse(new CardFilter("5").apply(card));
		// Even though it's the ID of the value set, only the label must be used for matching
		assertFalse(new CardFilter("3").apply(card));
	}
}
