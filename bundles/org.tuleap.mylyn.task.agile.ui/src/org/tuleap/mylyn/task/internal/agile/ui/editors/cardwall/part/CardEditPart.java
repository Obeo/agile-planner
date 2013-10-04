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
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure;

/**
 * The edit part for the cards.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardEditPart extends AbstractGraphicalEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new CardFigure();
	}

	/**
	 * Get the figure of the card.
	 * 
	 * @return The card figure.
	 */
	public CardFigure getCardFigure() {
		return (CardFigure)getFigure();
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
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		CardFigure fig = getCardFigure();
		CardWrapper card = (CardWrapper)getModel();

		fig.setTitle(card.getLabel());

		// TODO: Iterate on all the managed configurable fields.
		addFieldValue(CardWrapper.CARD_VALUE_FIELD_ID);
		addFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID);
		addFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID);
	}

	/**
	 * Add a new field to the card.
	 * 
	 * @param id
	 *            The id of the configurable field to add.
	 */
	private void addFieldValue(String id) {
		String value = ((CardWrapper)getModel()).getFieldValue(id);
		if (value != null) {
			String label = ((CardWrapper)getModel()).getFieldLabel(id);
			getCardFigure().setField(id, label, value);
		}
	}
}
