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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ICardwallProperties;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link CardwallModel}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallModelTest extends AbstractCardwallTest {

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
	 * Test of {@link CardwallModel#getFilter()}.
	 */
	@Test
	public void testCardwallModelGetFilterNull() {
		CardwallModel m = new CardwallModel(cardwall);
		assertNull(m.getFilter());
	}

	/**
	 * Test of {@link CardwallModel#getFilter()} and {@link CardwallModel#setFilter(String)}.
	 */
	@Test
	public void testCardwallModelGetFilterEmptyString() {
		CardwallModel m = new CardwallModel(cardwall);
		m.setFilter("");
		assertEquals("", m.getFilter());
		m.setFilter(null);
		assertNull(m.getFilter());
		m.setFilter("hello");
		assertEquals("hello", m.getFilter());
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
		assertEquals(0, listener.getEvents().size());

		cell.setFolded(true); // should notify
		assertEquals(1, listener.getEvents().size());
		PropertyChangeEvent event = listener.getEvents().get(0);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.FALSE, event.getOldValue());
		assertEquals(Boolean.TRUE, event.getNewValue());
		assertSame(cell, event.getSource());
		cell.setFolded(true); // should not notify
		assertEquals(1, listener.getEvents().size());

		cell.setFolded(false); // should notify
		assertEquals(2, listener.getEvents().size());
		event = listener.getEvents().get(1);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.TRUE, event.getOldValue());
		assertEquals(Boolean.FALSE, event.getNewValue());
		assertSame(cell, event.getSource());

		cell.removePropertyChangeListener(listener);
		cell.setFolded(true); // should notify, but not the removed listener
		assertEquals(2, listener.getEvents().size());
	}

	/**
	 * Test of {@link SwimlaneModel#getHeaderCards()}
	 */
	@Test
	public void testSwimlaneModelGetHeaderCardsEmpty() {
		CardwallModel m = new CardwallModel(cardwall);
		SwimlaneModel swimlaneModel = m.getSwimlanes().get(0);
		assertTrue(swimlaneModel.getHeaderCards().isEmpty());
	}

	/**
	 * Test of {@link SwimlaneModel#getHeaderCards()}
	 */
	@Test
	public void testSwimlaneModelGetHeaderCardsNotEmpty() {
		CardwallModel m = new CardwallModel(cardwall);
		SwimlaneModel swimlaneModel = m.getSwimlanes().get(0);
		swimlaneModel.getWrapper().addCard("new card 0");
		assertEquals(1, swimlaneModel.getHeaderCards().size());
		assertEquals("new card 0", swimlaneModel.getHeaderCards().get(0).getId());

		swimlaneModel.getWrapper().addCard("new card 1");
		assertEquals(2, swimlaneModel.getHeaderCards().size());
		assertEquals("new card 0", swimlaneModel.getHeaderCards().get(0).getId());
		assertEquals("new card 1", swimlaneModel.getHeaderCards().get(1).getId());
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
		assertEquals(0, listener.getEvents().size());

		column.setFolded(true); // should notify
		assertEquals(1, listener.getEvents().size());
		PropertyChangeEvent event = listener.getEvents().get(0);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.FALSE, event.getOldValue());
		assertEquals(Boolean.TRUE, event.getNewValue());
		assertSame(column, event.getSource());
		column.setFolded(true); // should not notify
		assertEquals(1, listener.getEvents().size());

		column.setFolded(false); // should notify
		assertEquals(2, listener.getEvents().size());
		event = listener.getEvents().get(1);
		assertEquals(ICardwallProperties.FOLDED, event.getPropertyName());
		assertEquals(Boolean.TRUE, event.getOldValue());
		assertEquals(Boolean.FALSE, event.getNewValue());
		assertSame(column, event.getSource());

		column.removePropertyChangeListener(listener);
		column.setFolded(true); // should notify, but not the removed listener
		assertEquals(2, listener.getEvents().size());
	}

	/**
	 * Tests {@link ColumnModel#getLabel()}.
	 */
	@Test
	public void testColumnModelGetLabel() {
		CardwallModel m = new CardwallModel(cardwall);
		List<ColumnModel> columns = m.getColumns();
		for (int i = 0; i < 4; i++) {
			assertEquals("Column" + i, columns.get(i).getLabel());
		}
	}

	/**
	 * Tests {@link ColumnModel#getCardwall()}.
	 */
	@Test
	public void testColumnModelGetCardwall() {
		CardwallModel m = new CardwallModel(cardwall);
		ColumnModel column = m.getColumns().get(0);
		assertSame(m, column.getCardwall());
	}

	/**
	 * Test of {@link CardModel}.
	 */
	@Test
	public void testCardModel() {
		CardModel card = new CardModel(cardwall.getSwimlanes().get(0).getCard("200"));
		assertFalse(card.isFolded());
		card.setFolded(true);
		assertTrue(card.isFolded());
		card.setFolded(false);
		assertFalse(card.isFolded());
	}

	/**
	 * Test of {@link HeaderModel}.
	 */
	@Test
	public void testHeaderModel() {
		CardwallModel m = new CardwallModel(cardwall);
		HeaderModel header = new HeaderModel(m);
		assertEquals("Backlog", header.getBacklogLabel());
		List<ColumnModel> columns = header.getColumns();
		assertEquals(4, columns.size());
		for (int i = 0; i < 4; i++) {
			assertEquals("Column" + i, columns.get(i).getLabel());
		}
	}

	/**
	 * Check {@link CardModel#getWrapper()}.
	 */
	@Test
	public void testCardModelGetWrapper() {
		CardWrapper cardWrapper = cardwall.getSwimlanes().get(0).getCard("200");
		CardModel card = new CardModel(cardWrapper);
		CardWrapper wrapper = card.getWrapper();
		assertSame(cardWrapper, wrapper);
	}

	/**
	 * Checks the notifications of {@link CardModel}.
	 */
	@Test
	public void testCardModelNotification() {
		CardModel card = new CardModel(cardwall.getSwimlanes().get(0).getCard("200"));

		TestModelListener listener = new TestModelListener();
		card.addPropertyChangeListener(listener);

		card.setFolded(false);
		assertEquals(0, listener.getEvents().size());

		card.setFolded(true);
		assertEquals(1, listener.getEvents().size());
		PropertyChangeEvent event = listener.getEvents().get(0);
		assertEquals("folded", event.getPropertyName());
		assertEquals(Boolean.FALSE, event.getOldValue());
		assertEquals(Boolean.TRUE, event.getNewValue());

		card.setFolded(true);
		assertEquals(1, listener.getEvents().size());

		card.setFolded(false);
		assertEquals(2, listener.getEvents().size());
		event = listener.getEvents().get(1);
		assertEquals("folded", event.getPropertyName());
		assertEquals(Boolean.TRUE, event.getOldValue());
		assertEquals(Boolean.FALSE, event.getNewValue());
	}

	/**
	 * tests set up.
	 */
	@Override
	@Before
	public void setUp() {
		super.setUp();
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
