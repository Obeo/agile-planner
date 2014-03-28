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
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.SwimlaneHeaderCellFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link SwimlaneHeaderEditPart}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneHeaderEditPartTest extends AbstractCardwallTest {

	private SwimlaneHeaderEditPart editPart;

	private SwimlaneWrapper swimlane;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		cardwall.addColumn("1", "To do");
		cardwall.addColumn("2", "Done");
		swimlane = cardwall.addSwimlane("swi1");
		editPart = new SwimlaneHeaderEditPart();
		editPart.setModel(swimlane);
	}

	@Test
	public void testCreateFigure() {
		IFigure figure = editPart.createFigure();
		assertTrue(figure instanceof SwimlaneHeaderCellFigure);
	}

	@Test
	public void testGetModelChildrenOnEmptyHeader() {
		List<CardModel> children = editPart.getModelChildren();
		assertTrue(children.isEmpty());
	}

	@Test
	public void testGetModelChildren() {
		swimlane.addCard("1");
		swimlane.addCard("2");
		List<CardModel> children = editPart.getModelChildren();
		assertEquals(2, children.size());
		assertEquals("1", children.get(0).getWrapper().getId());
		assertEquals("2", children.get(1).getWrapper().getId());
	}
}
