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

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;

import static org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants.MARGIN;

/**
 * Figure representing a column header.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ColumnHeaderFigure extends Figure {

	/**
	 * The panel to which children should be added.
	 */
	protected final Panel panel;

	/**
	 * The label of the cell.
	 */
	protected final TextFlow textFlow;

	/**
	 * The line border.
	 */
	private final AccentedLineBorder lineBorder;

	/**
	 * Constructor.
	 */
	public ColumnHeaderFigure() {
		lineBorder = new AccentedLineBorder(ColorConstants.lightGray);
		setBorder(lineBorder);

		panel = new Panel();
		add(panel);

		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		setLayoutManager(marginsLayout);
		setConstraint(panel, new GridData(SWT.FILL, SWT.FILL, true, true));

		FlowPage page = new FlowPage();
		page.setFont(JFaceResources.getHeaderFont());
		textFlow = new TextFlow();
		page.add(textFlow);
		panel.add(page);

		BorderLayout innerLayout = new BorderLayout();
		panel.setLayoutManager(innerLayout);
		panel.setConstraint(page, Integer.valueOf(PositionConstants.CENTER));
	}

	/**
	 * Sets the header label.
	 * 
	 * @param lbl
	 *            the label text;
	 */
	public void setLabel(String lbl) {
		textFlow.setText(lbl);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics graphics) {
		// The cell is completely stretched according to the parent grid.
		// getParent().setConstraint(this, new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Sets the header band color.
	 * 
	 * @param rgb
	 *            The color representation as a string #rrggbb
	 */
	public void setHeaderBandColor(String rgb) {
		Color color = MylynAgileUIActivator.getDefault().forColorName(rgb);
		if (color != null) {
			lineBorder.setAccentColor(color);
		}
	}

}
