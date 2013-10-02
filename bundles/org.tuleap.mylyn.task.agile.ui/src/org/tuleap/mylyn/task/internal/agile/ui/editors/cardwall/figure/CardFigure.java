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
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Figure representing a card in the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardFigure extends Panel {

	/**
	 * Preferred width.
	 */
	private static final int CARD_PREFERRED_WIDTH = 150;

	/**
	 * Preferred height.
	 */
	private static final int CARD_PREFERRED_HEIGHT = 150;

	/**
	 * Red part for the color of the card.
	 */
	private static final int CARD_COLOR_RED = 231;

	/**
	 * Green part for the color of the card.
	 */
	private static final int CARD_COLOR_GREEN = 255;

	/**
	 * Blue part for the color of the card.
	 */
	private static final int CARD_COLOR_BLUE = 175;

	/**
	 * The margin between text block inside the card and the border of this one, and between the title and
	 * description.
	 */
	private static final int MARGIN = 5;

	/**
	 * The title of the card.
	 */
	private TextFlow titleTextFlow;

	/**
	 * The detailed description of the card.
	 */
	private TextFlow descTextFlow;

	/**
	 * Constructor.
	 */
	public CardFigure() {

		setPreferredSize(CARD_PREFERRED_WIDTH, CARD_PREFERRED_HEIGHT);

		setBackgroundColor(new Color(Display.getCurrent(), new RGB(CARD_COLOR_RED, CARD_COLOR_GREEN,
				CARD_COLOR_BLUE)));
		setLayoutManager(new StackLayout());

		setBorder(new LineBorder());
		setOpaque(true);
		setLayoutManager(new StackLayout());

		Panel marginsPanel = new Panel();
		marginsPanel.setOpaque(false);
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		marginsPanel.setLayoutManager(marginsLayout);

		Panel contentPanel = new Panel();
		ToolbarLayout contentLayout = new ToolbarLayout(false);
		contentLayout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		contentLayout.setStretchMinorAxis(true);
		contentLayout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(contentLayout);

		Panel title = new Panel();
		title.setLayoutManager(new GridLayout(1, false));

		FlowPage titleFlowPage = new FlowPage();
		titleFlowPage.setOpaque(true);
		titleTextFlow = new TextFlow();
		titleFlowPage.add(titleTextFlow);

		Panel desc = new Panel(); // TODO: Manage a scroll pane...
		desc.setLayoutManager(new GridLayout(1, false));

		FlowPage descFlowPage = new FlowPage();
		descFlowPage.setOpaque(true);
		descTextFlow = new TextFlow();
		descFlowPage.add(descTextFlow);

		title.add(titleFlowPage);
		title.setConstraint(titleFlowPage, new GridData(SWT.CENTER, SWT.TOP, true, true));

		desc.add(descFlowPage);
		desc.setConstraint(descFlowPage, new GridData(SWT.LEFT, SWT.TOP, true, true));

		contentPanel.add(title);
		contentPanel.add(desc);

		marginsPanel.add(contentPanel);
		marginsPanel.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		add(marginsPanel);
		setConstraint(marginsPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

	}

	/**
	 * Setter of the title of the card.
	 * 
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		titleTextFlow.setText(title);
	}

	/**
	 * Setter of the description of the card.
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		descTextFlow.setText(description);
	}

}
