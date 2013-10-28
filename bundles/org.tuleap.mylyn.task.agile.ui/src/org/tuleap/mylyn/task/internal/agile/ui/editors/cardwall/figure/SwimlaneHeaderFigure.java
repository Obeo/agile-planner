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

import static org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants.MARGIN;

/**
 * Figure representing a column header.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneHeaderFigure extends AbstractCellFigure {

	/**
	 * The content panel.
	 */
	private final Panel contentPanel;

	/**
	 * Constructor.
	 */
	public SwimlaneHeaderFigure() {
		Panel marginsPanel = new Panel();
		// marginsPanel.setOpaque(false);
		add(marginsPanel);
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		marginsPanel.setLayoutManager(marginsLayout);
		setConstraint(marginsPanel, new GridData(SWT.FILL, SWT.FILL, true, false));

		// Add the panel which will contain the artifact figures
		contentPanel = new Panel();
		marginsPanel.add(contentPanel);
		marginsPanel.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.TOP, true, true));
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(layout);
	}

	/**
	 * Provides the cell's content panel.
	 * 
	 * @return The cell's content panel.
	 */
	public Panel getContentPanel() {
		return contentPanel;
	}
}
