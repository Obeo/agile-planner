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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.command;

import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests of {@link SetFieldValuesCommand}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SetFieldValuesCommandTest extends AbstractCardwallTest {

	private SwimlaneWrapper swimlane;

	private CardWrapper card;

	private TaskAttribute fieldAtt;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		cardwall.addColumn("1", "To do");
		cardwall.addColumn("2", "Done");
		swimlane = cardwall.addSwimlane("swi1");
		card = swimlane.addCard("123");
		card.setColumnId("1");
		fieldAtt = card.addField("f0", "Assigned to", "msb");
		fieldAtt.putOption("1", "Darth Vador");
		fieldAtt.putOption("2", "Luke Skywalker");
		fieldAtt.putOption("3", "Obiwan Kenobi");
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#execute()}.
	 */
	@Test
	public void testExecute() {
		List<String> values = Arrays.asList("1", "2");
		SetFieldValuesCommand command = new SetFieldValuesCommand(card, fieldAtt, values);
		assertTrue(fieldAtt.getValues().isEmpty());
		command.execute();
		assertEquals(Arrays.asList("1", "2"), fieldAtt.getValues());
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#execute()}.
	 */
	@Test
	public void testExecuteCausesNotification() {
		List<String> values = Arrays.asList("1", "2");
		SetFieldValuesCommand command = new SetFieldValuesCommand(card, fieldAtt, values);
		final boolean[] flag = {false };
		card.addListener(new ITaskAttributeChangeListener() {
			@Override
			public void attributeChanged(TaskAttribute attribute) {
				if (attribute == fieldAtt) {
					flag[0] = true;
				} else {
					fail("Unexpected attribute change notification occurred");
				}
			}
		});

		assertTrue(fieldAtt.getValues().isEmpty());
		assertFalse(flag[0]);
		command.execute();
		assertTrue(flag[0]);
		assertEquals(Arrays.asList("1", "2"), fieldAtt.getValues());
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#execute()}.
	 */
	@Test
	public void testExecuteWithoutChangeDoesNotNotify() {
		List<String> values = Arrays.asList("1", "2");
		fieldAtt.setValues(values);
		SetFieldValuesCommand command = new SetFieldValuesCommand(card, fieldAtt, values);
		card.addListener(new ITaskAttributeChangeListener() {
			@Override
			public void attributeChanged(TaskAttribute attribute) {
				fail("Unexpected attribute change notification occurred");
			}
		});
		assertEquals(Arrays.asList("1", "2"), fieldAtt.getValues());
		command.execute();
		assertEquals(Arrays.asList("1", "2"), fieldAtt.getValues());
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#undo()}.
	 */
	@Test
	public void testUndo() {
		List<String> values = Arrays.asList("1", "2");
		SetFieldValuesCommand command = new SetFieldValuesCommand(card, fieldAtt, values);
		assertTrue(fieldAtt.getValues().isEmpty());
		command.execute();
		command.undo();
		assertTrue(fieldAtt.getValues().isEmpty());
	}

}
