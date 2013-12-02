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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
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
	 * Listener to launch action on click on a URL.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private static class ActionURLMouseListener implements MouseListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
			// nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			// TODO Action from the URL id
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"TODO", "TODO: We should open the details of the relative item");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// nothing
		}
	}

	/**
	 * The title of the card.
	 */
	private TextFlow titleTextFlow;

	/**
	 * The url of the card.
	 */
	private TextFlow urlTextFlow;

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

		contentPanel = new Panel();
		contentPanel.setForegroundColor(ColorConstants.black);
		// contentPanel.setLayoutManager(contentLayout);
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout contentLayout = new ToolbarLayout();
		contentLayout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		contentLayout.setStretchMinorAxis(true);
		contentPanel.setLayoutManager(contentLayout);
		cardRect.add(contentPanel);
		cardRect.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		addUrl();

		addTitle();

		detailsPanel = new CardDetailsPanel();
		detailsPanel.setFont(detailsFont);
		ToolbarLayout detailslayout = new ToolbarLayout();
		// detailslayout.marginHeight = IMylynAgileUIConstants.MARGIN;
		// detailslayout.marginWidth = 0;
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
		urlTextFlow.setText(url);
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
		urlTextFlow = new TextFlow();
		URLFigure urlFigure = new URLFigure(urlTextFlow);

		urlFigure.addMouseListener(new ActionURLMouseListener());

		contentPanel.add(urlFigure);
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
}
