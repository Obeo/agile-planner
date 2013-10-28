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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;

/**
 * Figure representing a column header.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ColumnHeaderFigure extends Figure {

	/**
	 * The label of the cell.
	 */
	protected final TextFlow textFlow;

	/**
	 * Constructor.
	 */
	public ColumnHeaderFigure() {
		FlowPage page = new FlowPage();
		page.setFont(JFaceResources.getHeaderFont());
		textFlow = new TextFlow();
		page.add(textFlow);
		add(page);
		BorderLayout borderLayout = new BorderLayout();
		setLayoutManager(borderLayout);
		borderLayout.setConstraint(page, Integer.valueOf(PositionConstants.CENTER));
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
		getParent().setConstraint(this, new GridData(SWT.FILL, SWT.FILL, true, true));
	}

}
