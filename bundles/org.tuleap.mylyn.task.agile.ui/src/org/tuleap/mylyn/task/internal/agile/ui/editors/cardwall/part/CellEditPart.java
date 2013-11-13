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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.FoldableCellFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ICardwallProperties;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CellContentEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * The edit part for the cells containing cards.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellEditPart extends AbstractGraphicalEditPart {

	/**
	 * The folding listener.
	 */
	private PropertyChangeListener foldingListener;

	/**
	 * The mouse listener used to capture events of folding checkbox and updating the model.
	 */
	private ChangeListener foldingChangeListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new FoldableCellFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CellContentEditPolicy());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<CardWrapper> getModelChildren() {
		SwimlaneCell cell = (SwimlaneCell)getModel();
		return cell.getCards();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return ((FoldableCellFigure)getFigure()).getCardsContainer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		IFigure contentPane = getContentPane();
		SwimlaneCell cell = (SwimlaneCell)getModel();
		if (cell.isFolded() || cell.getColumn().isFolded()) {
			StackLayout sl = new StackLayout();
			contentPane.setLayoutManager(sl);
		} else {
			ToolbarLayout tl = new ToolbarLayout(false);
			tl.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
			tl.setStretchMinorAxis(true);
			tl.setSpacing(IMylynAgileUIConstants.MARGIN);
			contentPane.setLayoutManager(tl);
		}
		contentPane.revalidate();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		// Listen to the model
		SwimlaneCell cell = (SwimlaneCell)getModel();
		foldingListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt != null && ICardwallProperties.FOLDED.equals(evt.getPropertyName())) {
					refreshVisuals();
				}
			}
		};
		cell.addPropertyChangeListener(foldingListener);
		cell.getColumn().addPropertyChangeListener(foldingListener);
		// And listen to the figures
		FoldableCellFigure f = (FoldableCellFigure)getFigure();
		foldingChangeListener = new ChangeListener() {
			@Override
			public void handleStateChanged(ChangeEvent event) {
				SwimlaneCell c = (SwimlaneCell)getModel();
				FoldableCellFigure cellFigure = (FoldableCellFigure)getFigure();
				c.setFolded(cellFigure.isFolded());
			}
		};
		f.addFoldingListener(foldingChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		SwimlaneCell cell = (SwimlaneCell)getModel();
		cell.removePropertyChangeListener(foldingListener);
		cell.getColumn().removePropertyChangeListener(foldingListener);
		foldingListener = null;
		FoldableCellFigure f = (FoldableCellFigure)getFigure();
		f.removeFoldingListener(foldingChangeListener);
		foldingChangeListener = null;
		super.deactivate();
	}
}
