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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;

import static org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants.MARGIN;

/**
 * Figure representing a cell containing cards in the card wall.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CellFigure extends Panel {

	/**
	 * The panel which contains cards.
	 */
	private Panel contentPanel;

	/**
	 * Constructor.
	 */
	public CellFigure() {
		LineBorder lineBorder = new LineBorder();
		lineBorder.setColor(ColorConstants.lightGray);
		setBorder(lineBorder);
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		setLayoutManager(marginsLayout);

		// Add the panel which will contain the cards
		contentPanel = new Panel();
		add(contentPanel);
		setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, false));
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(layout);
	}

	/**
	 * Get the panel which contains the cards.
	 * 
	 * @return The panel.
	 */
	public Panel getCardsContainer() {
		return contentPanel;
	}

}
