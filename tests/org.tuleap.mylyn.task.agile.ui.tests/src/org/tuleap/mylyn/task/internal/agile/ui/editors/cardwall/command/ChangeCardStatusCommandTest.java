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

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardwallEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CellEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.ColumnHeaderEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.HeaderEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.SwimlaneEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.SwimlaneHeaderEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.AbstractCardwallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests of {@link SetFieldValuesCommand}.
 * <p>
 * Because {@link AbstractTaskEditorPart} statically uses a font, this test must run as eclipse plug-in test.
 * </p>
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ChangeCardStatusCommandTest extends AbstractCardwallTest {

	private SwimlaneWrapper swimlane;

	private CardWrapper card;

	private CardwallEditPart cardwallEditPart;

	private HeaderEditPart headerEditPart;

	private ColumnHeaderEditPart headerToDoEditPart;

	private ColumnHeaderEditPart headerDoneEditPart;

	private SwimlaneEditPart swimlaneEditPart;

	private SwimlaneHeaderEditPart swimlaneHeaderEditPart;

	private CellEditPart cellToDoEditPart;

	private CellEditPart cellDoneEditPart;

	private CardEditPart cardEditPart;

	private ColumnWrapper colToDo;

	private ColumnWrapper colDone;

	private AbstractTaskEditorPart taskEditorPart;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		colToDo = cardwall.addColumn("1", "To do");
		colDone = cardwall.addColumn("2", "Done");
		swimlane = cardwall.addSwimlane("swi1");
		card = swimlane.addCard("123");
		card.setColumnId("1"); // Card is in column "To do"
		taskEditorPart = new AbstractTaskEditorPart() {
			@Override
			public void createControl(Composite parent, FormToolkit toolkit) {
				// Do nothing
				fail("Method createControl should not be called.");
			}

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#getTaskEditorPage()
			 */
			@Override
			public AbstractTaskEditorPage getTaskEditorPage() {
				return new AbstractTaskEditorPage(null, "test") {
					// Nothing
				};
			}
		};

		CardwallModel cardwallModel = new CardwallModel(cardwall);
		cardwallEditPart = new CardwallEditPart();
		cardwallEditPart.setModel(cardwallModel);

		// TODO Setting up the environment makes the test far less unitary.
		// A viewer needs to be properly set-up, and consequently a TaskEditor and so on...
		// Maybe the code does not separate concerns enough...
		// EditPartViewer viewer = new AbstractEditPartViewer() {
		// @Override
		// public EditPart findObjectAtExcluding(Point location, Collection exclusionSet,
		// Conditional conditional) {
		// fail("findObjectAtExcluding should not be called.");
		// return null;
		// }
		//
		// @Override
		// public Control createControl(Composite parent) {
		// fail("createControl should not be called.");
		// return null;
		// }
		// };
		// viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
		// viewer.setEditPartFactory(new CardwallEditPartFactory(taskEditorPart));
		// viewer.setContents(cardwallEditPart);
		// cardwallEditPart.refresh();

		HeaderModel headerModel = new HeaderModel(cardwallModel);
		headerEditPart = new HeaderEditPart();
		headerEditPart.setModel(headerModel);
		headerEditPart.setParent(cardwallEditPart);

		SwimlaneModel swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		swimlaneEditPart = new SwimlaneEditPart();
		swimlaneEditPart.setModel(swimlaneModel);
		swimlaneEditPart.setParent(cardwallEditPart);

		swimlaneHeaderEditPart = new SwimlaneHeaderEditPart();
		swimlaneHeaderEditPart.setModel(swimlane);
		swimlaneHeaderEditPart.setParent(swimlaneEditPart);

		ColumnModel colDoneModel = new ColumnModel(colDone);

		cellToDoEditPart = new CellEditPart(taskEditorPart);
		cellToDoEditPart.setModel(new SwimlaneCell(swimlaneModel, colDoneModel));
		cellToDoEditPart.setParent(swimlaneEditPart);

		cellDoneEditPart = new CellEditPart(taskEditorPart);
		cellDoneEditPart.setModel(new SwimlaneCell(swimlaneModel, colDoneModel));
		cellDoneEditPart.setParent(swimlaneEditPart);

		cardEditPart = new CardEditPart(taskEditorPart);
		cardEditPart.setModel(new CardModel(card));
		cardEditPart.setParent(cellToDoEditPart);
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#execute()}.
	 */
	@Test
	public void testExecute() {
		ChangeCardStatusCommand command = new ChangeCardStatusCommand(cellDoneEditPart, cardEditPart);
		assertEquals("1", card.getColumnId());
		command.execute();
		assertEquals("2", card.getColumnId());
	}

	/**
	 * Test method for {@link SetFieldValuesCommand#execute()}.
	 */
	@Test
	public void testUndo() {
		ChangeCardStatusCommand command = new ChangeCardStatusCommand(cellDoneEditPart, cardEditPart);
		assertEquals("1", card.getColumnId());
		command.execute();
		assertEquals("2", card.getColumnId());
		command.undo();
		assertEquals("1", card.getColumnId());
	}

}
