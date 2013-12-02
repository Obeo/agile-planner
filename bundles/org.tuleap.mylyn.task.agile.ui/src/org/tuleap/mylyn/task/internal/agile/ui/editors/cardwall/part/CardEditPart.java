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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardDetailsPanel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure;

/**
 * The edit part for the cards.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardEditPart extends AbstractGraphicalEditPart {

	/**
	 * Listener for folding the card's details.
	 */
	private MouseListener mouseFoldingListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		CardWrapper wrapper = (CardWrapper)this.getModel();
		if (wrapper.getColumnId() == null) {
			return new CardFigure(false);
		}
		return new CardFigure(true);
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
		fig.setUrl(card.getDisplayId());

		CardDetailsPanel panel = getDetailsPanel();
		if (panel.isFolded()) {
			panel.setTitle("> details");
		} else {
			panel.setTitle("v details");
		}
		panel.invalidateTree();
	}

	/**
	 * Provides the detail panel.
	 * 
	 * @return The details panel.
	 */
	public CardDetailsPanel getDetailsPanel() {
		return getCardFigure().getDetailsPanel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return getCardFigure().getDetailsPanel().getFieldsPanel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getChildren()
	 */
	@Override
	public List<?> getModelChildren() {
		CardWrapper card = (CardWrapper)getModel();
		return card.getFieldAttributes();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		mouseFoldingListener = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent me) {
				//
			}

			@Override
			public void mousePressed(MouseEvent me) {
				getDetailsPanel().toggleDetails();
				getDetailsPanel().invalidateTree();
				refreshVisuals();
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				//
			}
		};
		getCardFigure().getDetailsPanel().addFoldingListener(mouseFoldingListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		getCardFigure().getDetailsPanel().removeFoldingListener(mouseFoldingListener);
		mouseFoldingListener = null;
		super.deactivate();
	}
}
