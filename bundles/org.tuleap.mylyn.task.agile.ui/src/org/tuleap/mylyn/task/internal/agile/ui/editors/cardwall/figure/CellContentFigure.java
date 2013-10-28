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

import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

import static org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants.MARGIN;

/**
 * Figure representing a cell containing cards in the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellContentFigure extends AbstractCellFigure {

	/**
	 * The panel which contains cards.
	 */
	private Panel contentPanel;

	/**
	 * The checkbox used to fold and unfold children figures.
	 */
	private final CheckBox foldCheckbox;

	/**
	 * Constructor.
	 */
	public CellContentFigure() {
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		setLayoutManager(marginsLayout);

		// Add the checkbox that will fold/unfold cards in this cell
		foldCheckbox = new CheckBox();
		foldCheckbox.setToolTip(new Label(MylynAgileUIMessages.getString("CellContentFigure.FoldTooltip"))); //$NON-NLS-1$
		add(foldCheckbox);
		setConstraint(foldCheckbox, new GridData(SWT.RIGHT, SWT.FILL, true, false));

		// Add the panel which will contain the artifact figures
		contentPanel = new Panel();
		add(contentPanel);
		setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, false));
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(layout);
	}

	/**
	 * Get the panel which contains the cards.
	 * 
	 * @return The panel.
	 */
	public Panel getCardsContainer() {
		return contentPanel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		int h = 2 * MARGIN + 1;
		for (Object child : getChildren()) {
			if (child instanceof IFigure) {
				h += ((IFigure)child).getPreferredSize(wHint, -1).height + MARGIN;
			}
		}
		return new Dimension(1, h);
	}

	/**
	 * Adds the given listener to the checkbox used for folding.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public void addFoldingListener(ChangeListener listener) {
		foldCheckbox.addChangeListener(listener);
	}

	/**
	 * Removes the given listener from the checkbox used for folding.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	public void removeFoldingListener(ChangeListener listener) {
		foldCheckbox.removeChangeListener(listener);
	}

	/**
	 * Getter of the folded status.
	 * 
	 * @return <code>true</code> if and only if the fold checkbox is selected.
	 */
	public boolean isFolded() {
		return foldCheckbox.isSelected();
	}

	/**
	 * Sets the folded status.
	 * 
	 * @param folded
	 *            the folded status.
	 */
	public void setFolded(boolean folded) {
		foldCheckbox.setSelected(folded);
	}
}
