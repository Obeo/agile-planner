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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CellEditPart;

/**
 * The command to move a card from a cell to another.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ChangeCardStatusCommand extends Command {

	/**
	 * The cell content edit part where the card is moved to.
	 */
	protected CellEditPart targetCell;

	/**
	 * The swimlane where the card is moved.
	 */
	protected SwimlaneWrapper targetSwimlane;

	/**
	 * The column where the card is moved to.
	 */
	protected ColumnWrapper targetColumn;

	/**
	 * The card wrapper which is moved.
	 */
	protected CardWrapper movedCard;

	/**
	 * The former card status id.
	 */
	protected String oldStatusId;

	/**
	 * Constructor.
	 *
	 * @param targetCellEditPart
	 *            The edit part of the cell <b>where the card is being moved to</b>.
	 * @param moved
	 *            The edit part of the card being moved.
	 */
	public ChangeCardStatusCommand(CellEditPart targetCellEditPart, CardEditPart moved) {
		this.targetCell = targetCellEditPart;

		SwimlaneCell cell = (SwimlaneCell)targetCellEditPart.getModel();
		targetSwimlane = cell.getSwimlane().getWrapper();
		targetColumn = cell.getColumn().getWrapper();

		movedCard = ((CardModel)moved.getModel()).getWrapper();
		oldStatusId = movedCard.getColumnId();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		movedCard.setColumnId(targetColumn.getId());
		EditPart parent = targetCell.getParent();
		for (Object child : parent.getChildren()) {
			((EditPart)child).refresh();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		movedCard.setColumnId(oldStatusId);
		EditPart parent = targetCell.getParent();
		for (Object child : parent.getChildren()) {
			((EditPart)child).refresh();
		}
	}

}
