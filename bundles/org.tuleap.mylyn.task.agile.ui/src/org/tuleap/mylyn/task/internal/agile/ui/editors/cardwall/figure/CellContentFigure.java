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
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;

/**
 * Figure representing a cell containing cards in the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellContentFigure extends AbstractCellFigure {

	/**
	 * The margin between the cards and the border of the cell, and between the cards of the cell.
	 */
	private static final int MARGIN = 10;

	/**
	 * The panel which contains cards.
	 */
	private Panel contentPanel;

	/**
	 * Constructor.
	 */
	public CellContentFigure() {
		super();

		Panel marginsPanel = new Panel();
		marginsPanel.setOpaque(false);
		add(marginsPanel);
		setConstraint(marginsPanel, new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		marginsPanel.setLayoutManager(marginsLayout);

		// Add the panel which will contain the artifact figures
		contentPanel = new Panel();
		contentPanel.setOpaque(false);
		marginsPanel.add(contentPanel);
		marginsPanel.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));
		ToolbarLayout contentLayout = new ToolbarLayout(false);
		contentLayout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		contentLayout.setStretchMinorAxis(true);
		contentLayout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(contentLayout);
	}

	/**
	 * Get the panel which contains the cards.
	 * 
	 * @return The panel.
	 */
	public Panel getArtifactContainer() {
		return contentPanel;
	}

}
