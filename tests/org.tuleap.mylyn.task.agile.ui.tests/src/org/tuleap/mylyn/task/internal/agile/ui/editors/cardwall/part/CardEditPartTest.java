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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
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
public class CardEditPartTest extends AbstractCardwallTest {

	private CardEditPart part;

	private CardwallModel cardwallModel;

	private SwimlaneWrapper swimlane;

	private CardWrapper unassignedCard;

	private CardWrapper assignedCard;

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
		unassignedCard = swimlane.addCard("123");
		assignedCard = swimlane.addCard("456");
		assignedCard.setColumnId("1");
		swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		AbstractTaskEditorPart taskEditorPart = new AbstractTaskEditorPart() {
			@Override
			public void createControl(Composite parent, FormToolkit toolkit) {
				// Do nothing
				fail("Method createControl should not be called.");
			}
		};
		part = new CardEditPart(taskEditorPart);
	}

	@Test
	public void testCreateFigureOfAssignedCard() {
		part.setModel(new CardModel(assignedCard));
		IFigure figure = part.createFigure();
		assertTrue(figure instanceof CardFigure);
		CardFigure cardFigure = (CardFigure)figure;
		Object child = cardFigure.getChildren().get(0);
		assertTrue(child instanceof RoundedRectangle);
		assertEquals(cardFigure.getDefaultBackgroundColor(), ((RoundedRectangle)child).getBackgroundColor());
	}

	@Test
	public void testCreateFigureOfUnassignedCard() {
		part.setModel(new CardModel(unassignedCard));
		IFigure figure = part.createFigure();
		assertTrue(figure instanceof CardFigure);
		CardFigure cardFigure = (CardFigure)figure;
		Object child = cardFigure.getChildren().get(0);
		assertTrue(child instanceof RoundedRectangle);
		assertEquals(cardFigure.getHeaderBackgroundColor(), ((RoundedRectangle)child).getBackgroundColor());
	}

	@Test
	public void testGetCardFigureOfAssignedCard() {
		part.setModel(new CardModel(assignedCard));
		CardFigure cardFigure = part.getCardFigure();
		Object child = cardFigure.getChildren().get(0);
		assertTrue(child instanceof RoundedRectangle);
		assertEquals(cardFigure.getDefaultBackgroundColor(), ((RoundedRectangle)child).getBackgroundColor());
	}

	@Test
	public void testGetCardFigureOfUnassignedCard() {
		part.setModel(new CardModel(unassignedCard));
		CardFigure cardFigure = part.getCardFigure();
		Object child = cardFigure.getChildren().get(0);
		assertTrue(child instanceof RoundedRectangle);
		assertEquals(cardFigure.getHeaderBackgroundColor(), ((RoundedRectangle)child).getBackgroundColor());
	}

	@Test
	public void testGetModelChildrenEmpty() {
		part.setModel(new CardModel(assignedCard));
		assertTrue(part.getModelChildren().isEmpty());
	}

	@Test
	public void testGetModelChildren() {
		assignedCard.addField("f0", "Assigned to", "msb");
		assignedCard.addField("f1", "Remaining effort", "double");
		part.setModel(new CardModel(assignedCard));
		assertEquals(2, part.getModelChildren().size());
		TaskAttribute att0 = (TaskAttribute)part.getModelChildren().get(0);
		assertEquals(assignedCard.getFieldAttribute("f0"), att0);
		TaskAttribute att1 = (TaskAttribute)part.getModelChildren().get(1);
		assertEquals(assignedCard.getFieldAttribute("f1"), att1);
	}
}
