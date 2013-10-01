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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CellHeadFigure;

/**
 * The edit part for the cells used as heading for columns, swimlanes and the whole card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellHeadEditPart extends AbstractCellEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new CellHeadFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.AbstractCellEditPart#getCellFigure()
	 */
	@Override
	public CellHeadFigure getCellFigure() {
		return (CellHeadFigure)getFigure();
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

		CellHeadFigure cell = getCellFigure();
		cell.getLabel().setText(getText(model));

		if (model instanceof String) {
			cell.setBackgroundColor(ColorConstants.lightGray);
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
		if (model instanceof ColumnWrapper) {
			result = ((ColumnWrapper)model).getLabel();
		} else if (model instanceof SwimlaneWrapper) {
			result = ((SwimlaneWrapper)model).getSwimlaneItem().getLabel();
		} else if (model instanceof String) {
			result = (String)model;
		}
		return result;
	}

}
