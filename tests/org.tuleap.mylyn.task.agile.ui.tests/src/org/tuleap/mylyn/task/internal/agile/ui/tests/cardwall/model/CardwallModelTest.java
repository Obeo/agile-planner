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
package org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ICardwallProperties;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link CardwallModel}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallModelTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * The cardwall wrapper.
	 */
	private CardwallWrapper cardwall;

	/**
	 * Tests the basic behavior of a cardwall model.
	 */
	@Test
	public void testCardwallModel() {
		CardwallModel m = new CardwallModel(cardwall);
		assertSame(cardwall, m.getWrapper());
		List<ColumnModel> columns = m.getColumns();
		assertEquals(4, columns.size());

		List<SwimlaneModel> swimlanes = m.getSwimlanes();
		assertEquals(1, swimlanes.size());
		SwimlaneModel swimlane = swimlanes.get(0);
		assertSame(m, swimlane.getCardwall());

		List<SwimlaneCell> cells = swimlane.getCells();
		assertEquals(4, cells.size());
		for (SwimlaneCell cell : cells) {
			assertEquals(1, cell.getCards().size());
		}
	}

	/**
	 * Test {@link SwimlaneCell}.
	 */
	@Test
	public void testSwimlaneCell() {
		CardwallModel m = new CardwallModel(cardwall);
		SwimlaneWrapper swimlaneWrapper = cardwall.getSwimlanes().get(0);
		SwimlaneModel swimlane = new SwimlaneModel(m, swimlaneWrapper);
		ColumnModel column = m.getColumns().get(0);
		SwimlaneCell cell = new SwimlaneCell(swimlane, column);
		assertSame(column, cell.getColumn());
		assertSame(swimlane, cell.getSwimlane());
		assertFalse(cell.isFolded());
		cell.setFolded(true);
		assertTrue(cell.isFolded());
		cell.setFolded(false);
		assertFalse(cell.isFolded());
	}

	/**
	 * Test {@link SwimlaneCell} notifications.
	 */
	@Test
	public void testSwimlaneCellNotification() {
		CardwallModel m = new CardwallModel(cardwall);
		SwimlaneWrapper swimlaneWrapper = cardwall.getSwimlanes().get(0);
		SwimlaneModel swimlane = new SwimlaneModel(m, swimlaneWrapper);
		ColumnModel column = m.getColumns().get(0);
		SwimlaneCell cell = new SwimlaneCell(swimlane, column);

		TestModelListener listener = new TestModelListener();
		cell.addPropertyChangeListener(listener);

		// cell is already not folded
		cell.setFolded(false); // should not notify
		assertEquals(0, listener.eventsReceived.size());

		cell.setFolded(true); // should notify
		assertEquals(1, listener.eventsReceived.size());
		PropertyChangeEvent event = listener.eventsReceived.get(0);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.FALSE, event.getOldValue());
		assertEquals(Boolean.TRUE, event.getNewValue());
		assertSame(cell, event.getSource());
		cell.setFolded(true); // should not notify
		assertEquals(1, listener.eventsReceived.size());

		cell.setFolded(false); // should notify
		assertEquals(2, listener.eventsReceived.size());
		event = listener.eventsReceived.get(1);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.TRUE, event.getOldValue());
		assertEquals(Boolean.FALSE, event.getNewValue());
		assertSame(cell, event.getSource());

		cell.removePropertyChangeListener(listener);
		cell.setFolded(true); // should notify, but not the removed listener
		assertEquals(2, listener.eventsReceived.size());
	}

	/**
	 * Tests {@link ColumnModel}.
	 */
	@Test
	public void testColumnModel() {
		CardwallModel m = new CardwallModel(cardwall);
		ColumnModel column = m.getColumns().get(0);
		assertFalse(column.isFolded());
		column.setFolded(true);
		assertTrue(column.isFolded());
		column.setFolded(false);
		assertFalse(column.isFolded());
	}

	/**
	 * Tests {@link ColumnModel} notification.
	 */
	@Test
	public void testColumnModelNotification() {
		CardwallModel m = new CardwallModel(cardwall);
		ColumnModel column = m.getColumns().get(0);

		TestModelListener listener = new TestModelListener();
		column.addPropertyChangeListener(listener);

		// cell is already not folded
		column.setFolded(false); // should not notify
		assertEquals(0, listener.eventsReceived.size());

		column.setFolded(true); // should notify
		assertEquals(1, listener.eventsReceived.size());
		PropertyChangeEvent event = listener.eventsReceived.get(0);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.FALSE, event.getOldValue());
		assertEquals(Boolean.TRUE, event.getNewValue());
		assertSame(column, event.getSource());
		column.setFolded(true); // should not notify
		assertEquals(1, listener.eventsReceived.size());

		column.setFolded(false); // should notify
		assertEquals(2, listener.eventsReceived.size());
		event = listener.eventsReceived.get(1);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.TRUE, event.getOldValue());
		assertEquals(Boolean.FALSE, event.getNewValue());
		assertSame(column, event.getSource());

		column.removePropertyChangeListener(listener);
		column.setFolded(true); // should notify, but not the removed listener
		assertEquals(2, listener.eventsReceived.size());
	}

	/**
	 * Model listener used for tests.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private class TestModelListener implements PropertyChangeListener {
		/**
		 * Cache of received events.
		 */
		private final List<PropertyChangeEvent> eventsReceived = new ArrayList<PropertyChangeEvent>();

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			eventsReceived.add(evt);
		}
	}

	/**
	 * tests set up.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository";
		String connectorKind = "kind";
		String taskId = "id";
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		cardwall = new CardwallWrapper(taskData.getRoot());
		for (int i = 0; i < 4; i++) {
			cardwall.addColumn(Integer.toString(10 + i), "Column" + i);
		}
		SwimlaneWrapper swimlane = cardwall.addSwimlane("123");

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i));
			card.setColumnId(Integer.toString(10 + i));
			card.addFieldValue("100", "Value 100" + i);
		}
	}
}
