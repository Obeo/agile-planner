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
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.layout.SwimlaneLayout;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link HeaderEditPart}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class HeaderEditPartTest extends AbstractCardwallTest {

	private HeaderEditPart editPart;

	private CardwallModel cardwallModel;

	private HeaderModel headerModel;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest#setUp()
	 */
	@Override
	@Before
	public void setUp() {
		super.setUp();
		cardwall.addColumn("1", "To do");
		cardwall.addColumn("2", "Done");
		editPart = new HeaderEditPart();
		cardwallModel = new CardwallModel(cardwall);
		headerModel = new HeaderModel(cardwallModel);
		editPart.setModel(headerModel);
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.HeaderEditPart#createFigure()}.
	 */
	@Test
	public void testCreateFigure() {
		IFigure figure = editPart.createFigure();
		assertTrue(figure instanceof Panel);
		assertTrue(figure.getLayoutManager() instanceof SwimlaneLayout);
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.HeaderEditPart#getModelChildren()}
	 * .
	 */
	@Test
	public void testGetModelChildren() {
		List<Object> children = editPart.getModelChildren();
		assertEquals(3, children.size());
		assertEquals("Backlog", children.get(0));
		assertTrue(children.get(1) instanceof ColumnModel);
		assertEquals("To do", ((ColumnModel)children.get(1)).getLabel());
		assertTrue(children.get(2) instanceof ColumnModel);
		assertEquals("Done", ((ColumnModel)children.get(2)).getLabel());
	}
}
