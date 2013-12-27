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
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import static org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants.MARGIN;

/**
 * Figure representing a cell containing cards in the card wall.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CellFigure extends Panel {

	/**
	 * The preferred cell header hegith.
	 */
	public static final int CELL_HEADER_HEIGHT = 24;

	/**
	 * The panel that contains the cards.
	 */
	private final Panel contentPanel;

	/**
	 * The panel that contains the cell's header widgets.
	 */
	private final Panel headerPanel;

	/**
	 * Constructor.
	 */
	public CellFigure() {
		LineBorder lineBorder = new LineBorder();
		lineBorder.setColor(ColorConstants.lightGray);
		setBorder(lineBorder);
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = 0;
		marginsLayout.marginWidth = 0;
		marginsLayout.horizontalSpacing = 0;
		marginsLayout.verticalSpacing = 0;
		setLayoutManager(marginsLayout);

		headerPanel = new Panel();
		headerPanel.setPreferredSize(-1, CELL_HEADER_HEIGHT);
		add(headerPanel);
		setConstraint(headerPanel, new GridData(SWT.FILL, SWT.FILL, true, false));
		headerPanel.setBorder(new MarginBorder(MARGIN));
		GridLayout headerLayout = new GridLayout(1, false);
		headerLayout.marginHeight = 0;
		headerLayout.marginWidth = 0;
		headerLayout.horizontalSpacing = MARGIN;
		headerLayout.verticalSpacing = 0;
		headerPanel.setLayoutManager(headerLayout);
		Font defaultFont = JFaceResources.getDefaultFont();
		FontData[] defaultFontData = defaultFont.getFontData();
		FontData newFontData = new FontData(defaultFontData[0].getName(), defaultFontData[0].getHeight() - 2,
				defaultFontData[0].getStyle());
		Font detailsFont = new Font(defaultFont.getDevice(), newFontData);
		headerPanel.setFont(detailsFont);
		headerPanel.setForegroundColor(ColorConstants.gray);

		// Add the panel which will contain the cards
		contentPanel = new Panel();
		add(contentPanel);
		setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));
		contentPanel.setBorder(new MarginBorder(MARGIN));
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(layout);
	}

	/**
	 * Get the panel that contains the cards.
	 * 
	 * @return The content panel.
	 */
	public Panel getCardsContainer() {
		return contentPanel;
	}

	/**
	 * Get the header panel.
	 * 
	 * @return The header panel.
	 */
	public Panel getHeaderPanel() {
		return headerPanel;
	}

}
