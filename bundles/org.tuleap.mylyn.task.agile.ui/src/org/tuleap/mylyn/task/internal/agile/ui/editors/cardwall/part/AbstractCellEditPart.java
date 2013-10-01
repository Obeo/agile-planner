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

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.AbstractCellFigure;

/**
 * Edit part for the cells.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractCellEditPart extends AbstractGraphicalEditPart {

	/**
	 * Get the figure of the cell.
	 * 
	 * @return The cell figure.
	 */
	public AbstractCellFigure getCellFigure() {
		return (AbstractCellFigure)getFigure();
	}

}
