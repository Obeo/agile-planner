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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the {@link CardEditPart} class. This class is in the same package as the class under test to
 * unit-test protected methods.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallEditPartTest extends AbstractCardwallTest {

	private CardwallEditPart part;

	private CardwallModel cardwallModel;

	private SwimlaneWrapper swimlane;

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
		part = new CardwallEditPart();
	}

	@Test
	public void testCreateFigure() {
		part.setModel(cardwallModel);
		IFigure figure = part.createFigure();
		assertTrue(figure instanceof FreeformLayer);
		assertTrue(figure.getLayoutManager() instanceof ToolbarLayout);
	}

	@Test
	public void testGetModelChildren() {
		part.setModel(cardwallModel);
		List<Object> modelChildren = part.getModelChildren();
		assertEquals(2, modelChildren.size());
		HeaderModel header = (HeaderModel)modelChildren.get(0);
		assertEquals(2, header.getColumns().size());
		SwimlaneModel swimChild = (SwimlaneModel)modelChildren.get(1);
		assertEquals("swi1", swimChild.getWrapper().getId());
		assertSame(cardwallModel, swimChild.getCardwall());
	}
}
