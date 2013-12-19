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

import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.ColumnHeaderFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.FoldableColumnHeaderFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ColumnModel;

/**
 * The edit part for the cells used as heading for columns, swimlanes and the whole card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ColumnHeaderEditPart extends AbstractGraphicalEditPart {

	/**
	 * Listener for folding the card's details.
	 */
	private ChangeListener foldingChangeListener;

	/**
	 * The task editor taskEditorPart displaying the cardwall.
	 */
	private AbstractTaskEditorPart taskEditorPart;

	/**
	 * Constructor.
	 * 
	 * @param taskEditorPart
	 *            THe task editor taskEditorPart that displays the cardwall.
	 */
	public ColumnHeaderEditPart(AbstractTaskEditorPart taskEditorPart) {
		this.taskEditorPart = taskEditorPart;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		if (getModel() instanceof ColumnModel) {
			return new FoldableColumnHeaderFigure();
		}
		return new ColumnHeaderFigure();
	}

	/**
	 * Returns the figure as a {@link ColumnHeaderFigure}.
	 * 
	 * @return the figure as a {@link ColumnHeaderFigure}.
	 */
	public ColumnHeaderFigure getHeaderFigure() {
		return (ColumnHeaderFigure)getFigure();
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
		Object model = getModel();

		ColumnHeaderFigure cell = getHeaderFigure();
		cell.setLabel(getText(model));
		if (model instanceof ColumnModel) {
			((FoldableColumnHeaderFigure)cell).setFolded(((ColumnModel)model).isFolded());
			cell.setHeaderBandColor(getHeaderBandColor(model));
		}
	}

	/**
	 * Get the text to display in the cell.
	 * 
	 * @param model
	 *            The model object to manage.
	 * @return The text to display.
	 */
	private String getText(Object model) {
		String result = ""; //$NON-NLS-1$
		if (model instanceof ColumnModel) {
			result = ((ColumnModel)model).getLabel();
		} else if (model instanceof String) {
			result = (String)model;
		}
		return result;
	}

	/**
	 * Get the color to use.
	 * 
	 * @param model
	 *            The model object to manage.
	 * @return The color to use, or null.
	 */
	private String getHeaderBandColor(Object model) {
		String result = null;
		if (model instanceof ColumnModel) {
			result = ((ColumnModel)model).getWrapper().getColor();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		// Register listener for cell folding checkbox
		Object model = getModel();
		if (model instanceof ColumnModel) {
			FoldableColumnHeaderFigure f = (FoldableColumnHeaderFigure)getFigure();
			ColumnModel c = (ColumnModel)getModel();
			if (c.getCardwall().getColumns().get(c.getCardwall().getColumns().size() - 1) == c) {
				c.setFolded(true);
				f.setFolded(true);
			}
			foldingChangeListener = new ChangeListener() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.draw2d.ChangeListener#handleStateChanged(org.eclipse.draw2d.ChangeEvent)
				 */
				@Override
				public void handleStateChanged(ChangeEvent event) {
					ColumnModel column = (ColumnModel)getModel();
					FoldableColumnHeaderFigure fig = (FoldableColumnHeaderFigure)getFigure();
					column.setFolded(fig.isFolded());
					// The wrapping page needs to reflow so that scrollabrs don't appear where they shouldn't
					taskEditorPart.getTaskEditorPage().reflow();
				}
			};
			f.addFoldingListener(foldingChangeListener);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		// Deregister listener for cell folding checkbox
		Object model = getModel();
		if (model instanceof ColumnModel) {
			FoldableColumnHeaderFigure f = (FoldableColumnHeaderFigure)getFigure();
			f.removeFoldingListener(foldingChangeListener);
		}
		foldingChangeListener = null;
		super.deactivate();
	}
}
