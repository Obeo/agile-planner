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
import org.eclipse.draw2d.Panel;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.layout.SwimlaneLayout;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the {@link SwimlaneEditPart} class. This class is in the same package as the class under test to
 * unit-test protected methods.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneEditPartTest extends AbstractCardwallTest {

	private SwimlaneEditPart part;

	private CardwallModel cardwallModel;

	private SwimlaneWrapper swimlane;

	private SwimlaneModel swimlaneModel;

	/**
	 * Configure the data for the tests.
	 */
	@Override
	@Before
	public void setUp() {
		super.setUp();
		cardwallModel = new CardwallModel(cardwall);
		swimlane = cardwall.addSwimlane("swi1");
		swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		part = new SwimlaneEditPart();
	}

	/**
	 * Test the basic behavior of SwimlaneEditPart.
	 */
	@Test
	public void testGetModelChildrenWithoutCard() {
		part.setModel(swimlaneModel);
		assertSame(swimlaneModel, part.getModel());
		List<?> children = part.getModelChildren();
		assertEquals(1, children.size());
		assertSame(swimlane, children.get(0));
	}

	/**
	 * Test the basic behavior of SwimlaneEditPart.
	 */
	@Test
	public void testGetModelChildrenWithCards() {
		part.setModel(swimlaneModel);
		assertSame(swimlaneModel, part.getModel());
		List<?> children = part.getModelChildren();
		assertEquals(1, children.size());
		assertSame(swimlane, children.get(0));
	}

	@Test
	public void testCreateFigure() {
		part.setModel(swimlaneModel);
		IFigure figure = part.createFigure();
		assertTrue(figure instanceof Panel);
		assertTrue(figure.getLayoutManager() instanceof SwimlaneLayout);
		SwimlaneLayout layout = (SwimlaneLayout)figure.getLayoutManager();
		assertEquals(cardwall.getColumns().size() + 1, layout.getNbColumnsVisible());
	}
}
