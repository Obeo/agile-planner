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

import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CellContentEditPart;

/**
 * The command to move a card inside a same cell.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class MoveCardInsideCellCommand extends AbstractMoveCardCommand {

	/**
	 * Constructor.
	 * 
	 * @param cellContentEditPart
	 *            The edit part of the cell containing the card being moved.
	 * @param moved
	 *            The edit part of the card being moved.
	 * @param after
	 *            The edit part of the card which will be located after the one being moved or null if it does
	 *            not exist.
	 */
	public MoveCardInsideCellCommand(CellContentEditPart cellContentEditPart, CardEditPart moved,
			CardEditPart after) {
		super(cellContentEditPart, moved, after);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.command.AbstractMoveCardCommand#execute()
	 */
	@Override
	public void execute() {
		moveArtifactWithinItem(targetSwimlane, movedCard, targetFollowingCard);

		super.execute();
	}

}
