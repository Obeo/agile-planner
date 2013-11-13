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

import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.text.TextFlow;

/**
 * Foldable details panel in a card.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardDetailsPanel extends Panel {

	/**
	 * The URL figure used as title.
	 */
	private final URLFigure urlFigure;

	/**
	 * The label of the URL figure.
	 */
	private final TextFlow titleLabel;

	/**
	 * The actual panel that contains the fields.
	 */
	private final Panel panel;

	/**
	 * Constructor.
	 */
	public CardDetailsPanel() {
		titleLabel = new TextFlow();
		urlFigure = new URLFigure(titleLabel);
		add(urlFigure);

		panel = new Panel();
		add(panel);

		ToolbarLayout panelLayout = new ToolbarLayout();
		panelLayout.setStretchMinorAxis(true);
		panel.setLayoutManager(panelLayout);
	}

	/**
	 * The fields panel.
	 * 
	 * @return The fields panel.
	 */
	public Panel getFieldsPanel() {
		return panel;
	}

	/**
	 * Get details visibility.
	 * 
	 * @return <code>true</code> if and only if details are visible.
	 */
	public boolean isFolded() {
		return !panel.isVisible();
	}

	/**
	 * Toggles details visibility.
	 */
	public void toggleDetails() {
		panel.setVisible(isFolded());
	}

	/**
	 * Sets this panel's title.
	 * 
	 * @param title
	 *            The title.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int h = urlFigure.getPreferredSize(wHint, -1).height + 3;
		if (panel.isVisible()) {
			h += panel.getPreferredSize(wHint, -1).height + 3;
		}
		return new Dimension(d.width, h);
	}

	/**
	 * Adds the given listener to the URL label used to fold the details.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public void addFoldingListener(MouseListener listener) {
		urlFigure.addMouseListener(listener);
	}

	/**
	 * Removes the given listener from the URL label used to fold the details.
	 * 
	 * @param listener
	 *            listener to reove
	 */
	public void removeFoldingListener(MouseListener listener) {
		urlFigure.removeMouseListener(listener);
	}
}
