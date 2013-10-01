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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.StackLayout;

/**
 * Figure representing a cell used as heading for columns, swimlanes and the whole card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellHeadFigure extends AbstractCellFigure {

	/**
	 * The label of the cell.
	 */
	private Label label;

	/**
	 * Constructor.
	 */
	public CellHeadFigure() {
		setBorder(new LineBorder());
		setOpaque(true);
		setLayoutManager(new StackLayout());

		label = new Label();
		label.setOpaque(true); // has to be true to change the background color of this.
		add(label);
	}

	/**
	 * Getter of the label.
	 * 
	 * @return The label.
	 */
	public Label getLabel() {
		return label;
	}

}
