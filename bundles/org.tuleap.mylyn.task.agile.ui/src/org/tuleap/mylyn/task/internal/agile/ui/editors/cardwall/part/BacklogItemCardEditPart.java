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

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneItemWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.BacklogItemCardFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardDetailsPanel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure;

/**
 * The edit part for the cards.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class BacklogItemCardEditPart extends CardEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new BacklogItemCardFigure();
	}

	/**
	 * Get the figure of the card.
	 * 
	 * @return The card figure.
	 */
	@Override
	public BacklogItemCardFigure getCardFigure() {
		return (BacklogItemCardFigure)getFigure();
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
		SwimlaneItemWrapper wrapper = (SwimlaneItemWrapper)getModel();

		fig.setTitle(wrapper.getLabel());
		fig.setUrl(wrapper.getDisplayId());

		CardDetailsPanel panel = getDetailsPanel();
		if (panel.isFolded()) {
			panel.setTitle("> details");
		} else {
			panel.setTitle("v details");
		}
		panel.invalidateTree();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getChildren()
	 */
	@Override
	public List<?> getModelChildren() {
		SwimlaneItemWrapper wrapper = (SwimlaneItemWrapper)getModel();
		// TODO return wrapper.getFieldAttributes();
		return Collections.emptyList();
	}
}
