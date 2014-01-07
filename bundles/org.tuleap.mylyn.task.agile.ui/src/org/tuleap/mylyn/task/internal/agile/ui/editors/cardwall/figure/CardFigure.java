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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Figure representing a card in the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardFigure extends Figure {
	/**
	 * The key to register the header card's background color with the plugin.
	 */
	public static final String BI_CARD_BG_COLOR_KEY = "BI_CARD_BG_COLOR"; //$NON-NLS-1$

	/**
	 * The key to register the card's background color with the plugin.
	 */
	public static final String CARD_BG_COLOR_KEY = "CARD_BG_COLOR"; //$NON-NLS-1$

	/**
	 * The corner size of cards.
	 */
	public static final int CARD_CORNER_SIZE = 5;

	/**
	 * The title of the card.
	 */
	private TextFlow titleTextFlow;

	/**
	 * The url of the card.
	 */
	private Label urlLabel;

	/**
	 * The panel containing the data of the card.
	 */
	private Panel contentPanel;

	/**
	 * The panel containing the details of the card (the set of the configurable fields).
	 */
	private CardDetailsPanel detailsPanel;

	/**
	 * The default font to use in the card.
	 */
	private final Font defaultFont;

	/**
	 * The font to use for details.
	 */
	private final Font detailsFont;

	/**
	 * The line border.
	 */
	private final AccentedRoundedLineBorder lineBorder;

	/**
	 * Constructor.
	 * 
	 * @param isAssigned
	 *            indicates if the card is assigned or not to a column.
	 */
	public CardFigure(boolean isAssigned) {
		// TODO make this font manipulation cleaner
		defaultFont = JFaceResources.getDefaultFont();
		FontData[] defaultFontData = defaultFont.getFontData();
		FontData newFontData = new FontData(defaultFontData[0].getName(), defaultFontData[0].getHeight() - 1,
				defaultFontData[0].getStyle());
		detailsFont = new Font(defaultFont.getDevice(), newFontData);

		GridLayout globalLayout = new GridLayout(1, false);
		globalLayout.verticalSpacing = 0;
		globalLayout.marginHeight = 0;
		globalLayout.marginWidth = 0;
		setLayoutManager(globalLayout);

		RoundedRectangle cardRect = new RoundedRectangle();
		add(cardRect);
		setConstraint(cardRect, new GridData(SWT.FILL, SWT.CENTER, true, true));

		cardRect.setForegroundColor(ColorConstants.lightGray); // border color
		if (isAssigned) {
			cardRect.setBackgroundColor(getDefaultBackgroundColor());
		} else {
			cardRect.setBackgroundColor(getHeaderBackgroundColor());
		}

		GridLayout l = new GridLayout(1, false);
		l.verticalSpacing = 0;
		l.marginHeight = IMylynAgileUIConstants.MARGIN;
		l.marginWidth = IMylynAgileUIConstants.MARGIN;
		cardRect.setLayoutManager(l);

		cardRect.setLineWidth(1);
		cardRect.setOpaque(true);

		lineBorder = new AccentedRoundedLineBorder();
		lineBorder.setVertical(true);
		lineBorder.setCornerRadius(CARD_CORNER_SIZE);
		cardRect.setBorder(lineBorder);

		contentPanel = new Panel();
		contentPanel.setForegroundColor(ColorConstants.black);
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout contentLayout = new ToolbarLayout();
		contentLayout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		contentLayout.setStretchMinorAxis(true);
		contentPanel.setLayoutManager(contentLayout);
		cardRect.add(contentPanel);
		cardRect.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		addUrl();

		addTitle();

		detailsPanel = new CardDetailsPanel(false);
		detailsPanel.setFont(detailsFont);
		ToolbarLayout detailslayout = new ToolbarLayout();
		detailslayout.setStretchMinorAxis(true);
		detailsPanel.setLayoutManager(detailslayout);
		detailsPanel.setForegroundColor(ColorConstants.black);
		cardRect.add(detailsPanel);
		cardRect.setConstraint(detailsPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		setFont(defaultFont);
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
	 * Setter of the url of the card.
	 * 
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		urlLabel.setText(url);
	}

	/**
	 * Add the title of the card.
	 */
	private void addTitle() {
		titleTextFlow = new TextFlow();
		FlowPage titleFlowPage = new FlowPage();
		titleFlowPage.setForegroundColor(ColorConstants.black);
		titleFlowPage.add(titleTextFlow);

		ParagraphTextLayout paragraphLayout = new ParagraphTextLayout(titleTextFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT);
		titleTextFlow.setLayoutManager(paragraphLayout);
		FlowContext flowContext = (FlowContext)titleFlowPage.getLayoutManager();
		paragraphLayout.setFlowContext(flowContext);

		contentPanel.add(titleFlowPage);
	}

	/**
	 * Add the url of the card.
	 */
	private void addUrl() {
		Panel urlPanel = new Panel();
		GridLayout urlLayout = new GridLayout(1, false);
		urlLayout.marginWidth = 0;
		urlLayout.marginHeight = 0;
		urlPanel.setLayoutManager(urlLayout);
		urlLabel = new Label();
		urlLabel.setForegroundColor(ColorConstants.gray);
		urlPanel.add(urlLabel);
		urlLayout.setConstraint(urlLabel, new GridData(SWT.LEFT, SWT.FILL, true, true));
		contentPanel.add(urlPanel);
	}

	/**
	 * URL text flow.
	 * 
	 * @return The URL text flow.
	 */
	public Label getUrl() {
		return this.urlLabel;
	}

	/**
	 * Provides the details panel.
	 * 
	 * @return The details panel.
	 */
	public CardDetailsPanel getDetailsPanel() {
		return detailsPanel;
	}

	/**
	 * Provides the default background color for cards.
	 * 
	 * @return The default bg color for cards.
	 */
	public Color getDefaultBackgroundColor() {
		MylynAgileUIActivator activator = MylynAgileUIActivator.getDefault();
		if (activator.hasColor(CARD_BG_COLOR_KEY)) {
			return activator.getColor(CARD_BG_COLOR_KEY);
		}
		Color c = new Color(Display.getCurrent(), IMylynAgileUIConstants.CARD_BG_COLOR);
		activator.putColor(CARD_BG_COLOR_KEY, c);
		return c;
	}

	/**
	 * Provides the default background color for cards.
	 * 
	 * @return The default bg color for cards.
	 */
	public Color getHeaderBackgroundColor() {
		MylynAgileUIActivator activator = MylynAgileUIActivator.getDefault();
		if (activator.hasColor(BI_CARD_BG_COLOR_KEY)) {
			return activator.getColor(BI_CARD_BG_COLOR_KEY);
		}
		Color c = new Color(Display.getCurrent(), IMylynAgileUIConstants.BI_CARD_BG_COLOR);
		activator.putColor(BI_CARD_BG_COLOR_KEY, c);
		return c;
	}

	/**
	 * Sets the header band color.
	 * 
	 * @param rgb
	 *            The color representation as a string #rrggbb
	 */
	public void setAccentColor(String rgb) {
		Color color = MylynAgileUIActivator.getDefault().forColorName(rgb);
		if (color != null) {
			lineBorder.setAccentColor(color);
		}
	}

}
