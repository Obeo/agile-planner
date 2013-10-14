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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.BlockFlow;
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
	private static final int CARD_COLOR_RED = 255;

	/**
	 * Green part for the color of the card.
	 */
	private static final int CARD_COLOR_GREEN = 255;

	/**
	 * Blue part for the color of the card.
	 */
	private static final int CARD_COLOR_BLUE = 238;

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
	 * The panel containing the data of the card.
	 */
	private Panel contentPanel;

	/**
	 * Map containing the configurable fields on the card, indexed by their id.
	 */
	private Map<String, TextFlow> configurableFields = new HashMap<String, TextFlow>();

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

		contentPanel = new Panel();
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

		title.add(titleFlowPage);
		title.setConstraint(titleFlowPage, new GridData(SWT.CENTER, SWT.TOP, true, true));

		contentPanel.add(title);

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

	/**
	 * Set the value of the field identified by the given <code>id</code> with the given <code>value</code>.<br>
	 * If the field does not exist, it is added to the card before setting the value.<br>
	 * The label of the field is required if the field does not already exist.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @param label
	 *            The label of the configurable field (if the field does not exist).
	 * @param value
	 *            The value to set.
	 */
	public void setField(String id, String label, String value) {
		TextFlow fieldValue = getField(id);
		if (fieldValue == null) {
			fieldValue = addConfigurablePanel(id, label);
		}
		fieldValue.setText(value);
	}

	/**
	 * Get the field on the card from its id.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @return The graphical field.
	 */
	public TextFlow getField(String id) {
		return configurableFields.get(id);
	}

	/**
	 * Add a new panel to the card to display a new field.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @param label
	 *            The label of the configurable field.
	 * @return The text flow of the field.
	 */
	private TextFlow addConfigurablePanel(String id, String label) {
		Panel panel = new Panel();
		panel.setLayoutManager(new GridLayout(1, false));

		FlowPage flowPage = new FlowPage();
		flowPage.setOpaque(true);

		BlockFlow blockFlow = new BlockFlow();

		TextFlow labelFlow = new TextFlow();
		labelFlow.setText(label + ": "); //$NON-NLS-1$
		blockFlow.add(labelFlow);

		TextFlow textFlow = new TextFlow();
		blockFlow.add(textFlow);

		flowPage.add(blockFlow);

		panel.add(flowPage);
		panel.setConstraint(flowPage, new GridData(SWT.LEFT, SWT.TOP, true, true));

		contentPanel.add(panel);
		configurableFields.put(id, textFlow);

		return textFlow;
	}

}
