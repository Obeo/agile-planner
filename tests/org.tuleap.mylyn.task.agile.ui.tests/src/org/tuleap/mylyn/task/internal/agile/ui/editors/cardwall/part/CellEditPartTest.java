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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CellFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of the {@link CardEditPart} class. This class is in the same package as the class under test to
 * unit-test protected methods.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CellEditPartTest extends AbstractCardwallTest {

	private CellEditPart part;

	private CardwallModel cardwallModel;

	private SwimlaneWrapper swimlane;

	private CardWrapper card0;

	private CardWrapper card1;

	private SwimlaneModel swimlaneModel;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest#setUp()
	 */
	@Override
	@Before
	public void setUp() {
		super.setUp();
		cardwallModel = new CardwallModel(cardwall);
		cardwall.addColumn("1", "To do");
		cardwall.addColumn("2", "Done");
		swimlane = cardwall.addSwimlane("swi1");
		card0 = swimlane.addCard("123");
		card0.setColumnId("1");
		card1 = swimlane.addCard("456");
		card1.setColumnId("1");
		swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		AbstractTaskEditorPart taskEditorPart = new AbstractTaskEditorPart() {
			@Override
			public void createControl(Composite parent, FormToolkit toolkit) {
				// Do nothing
				fail("Method createControl should not be called.");
			}
		};
		part = new CellEditPart(taskEditorPart);
	}

	@Test
	public void testCreateFigure() {
		SwimlaneCell cell = new SwimlaneCell(swimlaneModel, new ColumnModel(cardwall.getColumns().get(0)));
		part.setModel(cell);
		IFigure figure = part.createFigure();
		assertTrue(figure instanceof CellFigure);
	}

	@Test
	public void testGetModelChildrenOnNonEmptyCellWithoutFilter() {
		SwimlaneCell cell = new SwimlaneCell(swimlaneModel, new ColumnModel(cardwall.getColumns().get(0)));
		part.setModel(cell);
		List<CardModel> children = part.getModelChildren();
		assertEquals(2, children.size());
		assertEquals("123", children.get(0).getWrapper().getId());
		assertEquals("456", children.get(1).getWrapper().getId());
	}

	@Test
	public void testGetModelChildrenEmptyCellWithoutFilter() {
		SwimlaneCell cell = new SwimlaneCell(swimlaneModel, new ColumnModel(cardwall.getColumns().get(1)));
		part.setModel(cell);
		assertTrue(part.getModelChildren().isEmpty());
	}

	@Test
	public void testGetModelChildrenOnNonEmptyCellWithFilter() {
		cardwallModel.setFilter("123");
		SwimlaneCell cell = new SwimlaneCell(swimlaneModel, new ColumnModel(cardwall.getColumns().get(0)));
		part.setModel(cell);
		List<CardModel> children = part.getModelChildren();
		assertEquals(1, children.size());
		assertEquals("123", children.get(0).getWrapper().getId());
	}
}
