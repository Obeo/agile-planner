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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Foldable details panel in a card.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardDetailsPanel extends Panel {

	/**
	 * The label of the URL figure.
	 */
	private final Label titleLabel;

	/**
	 * Icon for open state.
	 */
	private Image openIcon;

	/**
	 * Icon for closed state.
	 */
	private Image closedIcon;

	/**
	 * The actual panel that contains the fields.
	 */
	private final Panel panel;

	/**
	 * Constructor.
	 * 
	 * @param folded
	 *            Indicates whether the details panel must be visible.
	 */
	public CardDetailsPanel(boolean folded) {
		Panel headerPanel = new Panel();
		headerPanel.setForegroundColor(ColorConstants.gray);
		add(headerPanel);

		GridLayout headerlayout = new GridLayout(1, false);
		headerlayout.marginHeight = 0;
		headerlayout.marginWidth = 0;
		headerPanel.setLayoutManager(headerlayout);

		titleLabel = new Label(MylynAgileUIMessages.getString("CardDetailsPanel.Title")); //$NON-NLS-1$
		titleLabel.addMouseMotionListener(new DetailsMouseMotionListener(this));
		headerPanel.add(titleLabel);
		headerlayout.setConstraint(titleLabel, new GridData(SWT.LEFT, SWT.FILL, true, true));

		panel = new Panel();
		add(panel);
		setFolded(folded);
		openIcon = MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_OPEN);
		closedIcon = MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_CLOSED);
		if (folded) {
			titleLabel.setIcon(openIcon);
		} else {
			titleLabel.setIcon(closedIcon);
		}

		ToolbarLayout panelLayout = new ToolbarLayout();
		panelLayout.setSpacing(IMylynAgileUIConstants.MARGIN);
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
	 * Sets details visibility.
	 * 
	 * @param folded
	 *            Indicates whether details must be visible.
	 */
	public void setFolded(boolean folded) {
		panel.setVisible(!folded);
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
		int h = titleLabel.getPreferredSize(wHint, -1).height + 3;
		if (panel.isVisible()) {
			h += panel.getPreferredSize(wHint, -1).height + 3;
		}
		return new Dimension(d.width, h);
	}

	/**
	 * Title label getter.
	 * 
	 * @return The title label.
	 */
	public Label getTitleLabel() {
		return titleLabel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#repaint()
	 */
	@Override
	public void repaint() {
		if (isFolded()) {
			titleLabel.setIcon(closedIcon);
		} else {
			titleLabel.setIcon(openIcon);
		}
		super.repaint();
	}

	/**
	 * Sets the icons for open and closed status.
	 * 
	 * @param open
	 *            Icon for open status
	 * @param closed
	 *            Icon for closed status
	 */
	void setIcons(Image open, Image closed) {
		this.openIcon = open;
		this.closedIcon = closed;
	}
}
