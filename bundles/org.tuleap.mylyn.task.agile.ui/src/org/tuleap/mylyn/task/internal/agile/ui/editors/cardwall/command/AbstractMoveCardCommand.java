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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CellContentEditPart;

/**
 * The command to move a card inside or between cells.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractMoveCardCommand extends Command {

	/**
	 * The cell content edit part where the card is moved to.
	 */
	protected CellContentEditPart targetCellContentEditPart;

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
	 * The card wrapper which will be located after the moved card.
	 */
	protected CardWrapper targetFollowingCard;

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
	public AbstractMoveCardCommand(CellContentEditPart cellContentEditPart, CardEditPart moved,
			CardEditPart after) {
		this.targetCellContentEditPart = cellContentEditPart;

		List<Object> cell = (List<Object>)cellContentEditPart.getModel();
		targetSwimlane = (SwimlaneWrapper)cell.get(0);
		targetColumn = (ColumnWrapper)cell.get(1);

		movedCard = (CardWrapper)moved.getModel();

		if (after != null) {
			targetFollowingCard = (CardWrapper)after.getModel();
		}
	}

	/**
	 * Moves the given <code>cardToMove</code> before the given <code>followingCard</code> within the same
	 * cell.
	 * 
	 * @param container
	 *            The swimlane wrapper containing the cell where the card is moved.
	 * @param cardToMove
	 *            The card being moved.
	 * @param followingCard
	 *            The card which will be located after the one being moved or null if it does not exist.
	 */
	protected void moveArtifactWithinItem(SwimlaneWrapper container, CardWrapper cardToMove,
			CardWrapper followingCard) {

		// Non applicable to wrappers / task attributes

		// List<CardWrapper> cardWrappers = container.getCards();
		//
		// List<TaskAttribute> cardsAttributes = getTaskAttributes(cardWrappers);
		// TaskAttribute cardAttributeToMove = cardToMove.getWrappedAttribute();
		//
		// int currentIndex = cardsAttributes.indexOf(cardAttributeToMove);
		//
		// int nextIndex;
		// if (followingCard == null) {
		// nextIndex = cardsAttributes.size() - 1;
		// } else {
		// TaskAttribute followingCardAttributeToMove = followingCard.getWrappedAttribute();
		// nextIndex = cardsAttributes.indexOf(followingCardAttributeToMove);
		// }
		// if (nextIndex < 0) {
		// nextIndex = 0;
		// }
		//
		// if (nextIndex < currentIndex) {
		// cardWrappers.remove(currentIndex);
		// cardWrappers.add(nextIndex, cardToMove);
		// } else {
		// cardWrappers.add(nextIndex, cardToMove);
		// cardWrappers.remove(currentIndex);
		// }

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		// FIXME: How to retrieve only the two implied cell edit part ?
		for (EditPart part : (List<EditPart>)targetCellContentEditPart.getParent().getChildren()) {
			part.refresh();
		}
	}

	// /**
	// * Get a list of task attributes from a list of wrappers.
	// *
	// * @param wrappers
	// * The wrappers.
	// * @return The task attributes of the wrappers.
	// */
	// private List<TaskAttribute> getTaskAttributes(List<? extends AbstractTaskAttributeWrapper> wrappers) {
	// return Lists.transform(wrappers, new Function<AbstractTaskAttributeWrapper, TaskAttribute>() {
	// /**
	// * {@inheritDoc}
	// *
	// * @see com.google.common.base.Function#apply(java.lang.Object)
	// */
	// @Override
	// public TaskAttribute apply(AbstractTaskAttributeWrapper input) {
	// return input.getWrappedAttribute();
	// }
	// });
	// }

}
