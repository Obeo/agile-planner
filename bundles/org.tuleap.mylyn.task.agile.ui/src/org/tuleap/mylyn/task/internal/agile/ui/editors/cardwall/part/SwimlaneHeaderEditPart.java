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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.ProgressFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.SwimlaneHeaderCellFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;

/**
 * The edit part for a swimlane header (left cell).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneHeaderEditPart extends AbstractGraphicalEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		SwimlaneHeaderCellFigure cellFigure = new SwimlaneHeaderCellFigure();
		return cellFigure;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		ProgressFigure progressFigure = getCellFigure().getProgressFigure();
		SwimlaneWrapper swimlane = (SwimlaneWrapper)getModel();
		progressFigure.setTotal(swimlane.getNumberOfAssignedCards());
		progressFigure.setProgress(swimlane.getNumberOfCards(true));
		progressFigure.repaint();
	}

	/**
	 * Returns the figure as a {@link SwimlaneHeaderCellFigure}.
	 * 
	 * @return the figure as a {@link SwimlaneHeaderCellFigure}.
	 */
	public SwimlaneHeaderCellFigure getCellFigure() {
		return (SwimlaneHeaderCellFigure)getFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<CardModel> getModelChildren() {
		List<CardModel> res = new ArrayList<CardModel>();
		for (CardWrapper card : ((SwimlaneWrapper)getModel()).getCards()) {
			if (card.getColumnId() == null) {
				res.add(new CardModel(card));
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return getCellFigure().getCardsContainer();
	}

}
