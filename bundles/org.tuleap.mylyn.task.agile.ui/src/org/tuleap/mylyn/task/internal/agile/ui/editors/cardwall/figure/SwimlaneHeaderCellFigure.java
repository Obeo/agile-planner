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

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.Panel;
import org.eclipse.swt.SWT;

/**
 * Figure representing a cell containing cards in the card wall, that can be folded thanks to a checkbox in
 * the upper right corner.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneHeaderCellFigure extends CellFigure {

	/**
	 * The figure used to display progress in this swimlane.
	 */
	private final ProgressFigure progressFigure;

	/**
	 * Constructor.
	 */
	public SwimlaneHeaderCellFigure() {
		progressFigure = new ProgressFigure();
		Panel headerPanel = getHeaderPanel();
		headerPanel.add(progressFigure);
		headerPanel.setConstraint(progressFigure, new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Progress figure getter.
	 * 
	 * @return The figure used to report progress in this swimlane.
	 */
	public ProgressFigure getProgressFigure() {
		return progressFigure;
	}
}
