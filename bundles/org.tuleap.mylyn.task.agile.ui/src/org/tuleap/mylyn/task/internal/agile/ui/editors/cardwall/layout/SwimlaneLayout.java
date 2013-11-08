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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.layout;

import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Layout for cardwall swimlanes.
 * <p>
 * This layout is a simplified {@link org.eclipse.draw2d.GridLayout}, which lays out children horizontally.
 * The main trick is that when no width hint is passed, this layout tries to take as little width as
 * possible,which differs from the {@link org.eclipse.draw2d.GridLayout} behavior.
 * </p>
 * <p>
 * All columns are forced to have the same width, so all cells have the same size. The number of children can
 * be different from the number of columns but, unlike with a {@link org.eclipse.draw2d.GridLayout}, children
 * are added on the same row. That's why the number of columns manipulated her is the number of visible
 * columns and not that actual number of columns.
 * </p>
 * <p>
 * <b>Note:</b> This layout is made to work with a number of children equal to the number of columns. Any
 * other use is not guaranteed.
 * </p>
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneLayout extends AbstractHintLayout {

	/**
	 * The default minimum column width.
	 */
	public static final int DEFAULT_MIN_COLUMN_WIDTH = 150;

	/**
	 * Number of visible columns.
	 */
	protected int nbColumnsVisible;

	/**
	 * Minimum column width.
	 */
	protected int minColumnWidth = DEFAULT_MIN_COLUMN_WIDTH;

	/**
	 * Horizontal spacing between cells.
	 */
	protected int spacing = IMylynAgileUIConstants.MARGIN;

	/**
	 * Horizontal margins, used at the left and right of the container figure.
	 */
	protected int marginWidth = IMylynAgileUIConstants.MARGIN;

	/**
	 * Vertical margins, 0 by default, used at the top and bottom of the container figure.
	 */
	protected int marginHeight;

	/**
	 * Constructor.
	 * 
	 * @param nbColumnsVisible
	 *            Number of visible columns.
	 */
	public SwimlaneLayout(int nbColumnsVisible) {
		this.nbColumnsVisible = nbColumnsVisible;
	}

	/**
	 * Constructor.
	 * 
	 * @param nbColumnsVisible
	 *            Number of visible columns.
	 * @param minColumnWidth
	 *            The minimum column width.
	 */
	public SwimlaneLayout(int nbColumnsVisible, int minColumnWidth) {
		this.nbColumnsVisible = nbColumnsVisible;
		this.minColumnWidth = minColumnWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		return layout(container, false, 0, 0, wHint, hHint);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return <code>false</code> since this layout does not care about vertical hints.
	 * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveVertically(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected boolean isSensitiveVertically(IFigure container) {
		return false;
	}

	/**
	 * Get the number of visible columns.
	 * 
	 * @return the nbColumnsVisible
	 */
	public int getNbColumnsVisible() {
		return nbColumnsVisible;
	}

	/**
	 * Set the number of visible columns.
	 * 
	 * @param nbColumnsVisible
	 *            the nbColumnsVisible to set
	 */
	public void setNbColumnsVisible(int nbColumnsVisible) {
		this.nbColumnsVisible = nbColumnsVisible;
	}

	/**
	 * Get the minimum width of columns. All columns have the same width.
	 * 
	 * @return the minColumnWidth
	 */
	public int getMinColumnWidth() {
		return minColumnWidth;
	}

	/**
	 * Set the minimum width of columns. All columns have the same width.
	 * 
	 * @param minColumnWidth
	 *            the minColumnWidth to set
	 */
	public void setMinColumnWidth(int minColumnWidth) {
		this.minColumnWidth = minColumnWidth;
	}

	/**
	 * Get the spacing between cells.
	 * 
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * Set the spacing between cells.
	 * 
	 * @param spacing
	 *            the spacing to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * Get the margin width, used at the left and right of the container figure.
	 * 
	 * @return the marginWidth
	 */
	public int getMarginWidth() {
		return marginWidth;
	}

	/**
	 * Set the margin width, used at the left and right of the container figure.
	 * 
	 * @param marginWidth
	 *            the marginWidth to set
	 */
	public void setMarginWidth(int marginWidth) {
		this.marginWidth = marginWidth;
	}

	/**
	 * Get the margin width, used at the top and bottom of the container figure.
	 * 
	 * @return the marginHeight
	 */
	public int getMarginHeight() {
		return marginHeight;
	}

	/**
	 * Set the margin width, used at the top and bottom of the container figure.
	 * 
	 * @param marginHeight
	 *            the marginHeight to set
	 */
	public void setMarginHeight(int marginHeight) {
		this.marginHeight = marginHeight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure container) {
		Rectangle rect = container.getClientArea();
		layout(container, true, rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Calculates the cell size, taking into account the desired number of visible columns and the given
	 * hints. If the given hint is -1 (SWT.DEFAULT), the cell attempts to take as little width as possible
	 * (this is the difference with GridLayout which makes this layout work as expected).
	 * 
	 * @param container
	 *            The container figure
	 * @param width
	 *            The with hint.
	 * @return The cell dimension of every cell in this layout.
	 */
	private Dimension calculateCellSize(IFigure container, int width) {
		Dimension d;
		if (width != SWT.DEFAULT) {
			int availableWidth = width - 2 * marginWidth - (nbColumnsVisible - 1) * spacing;
			int cellWidth = Math.max(minColumnWidth, availableWidth / nbColumnsVisible);
			int h = 0;
			for (Object child : container.getChildren()) {
				IFigure fig = (IFigure)child;
				Dimension childPreferredSize = fig.getPreferredSize(cellWidth, -1);
				h = Math.max(h, childPreferredSize.height);
			}
			d = new Dimension(cellWidth, h);
		} else {
			d = new Dimension();
			for (Object child : container.getChildren()) {
				IFigure fig = (IFigure)child;
				d.union(fig.getPreferredSize(minColumnWidth, -1));
			}
		}
		return d;
	}

	/**
	 * Computes the children dimensions, and performs the layout of children by locating them correctly and
	 * having them fill the available cells, if the move parameter si <code>true</code>.
	 * 
	 * @param container
	 *            The container figure.
	 * @param move
	 *            Indicates whether children figures must have their bounds set.
	 * @param x
	 *            the x position.
	 * @param y
	 *            the y position.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The calculated dimension for the container figure, whose width depends on the number of
	 *         columns, the minimum column width, and the actual number of children, and whose height is the
	 *         maximum child height (margins and spacing also contribute to the calculation).
	 */
	Dimension layout(IFigure container, boolean move, int x, int y, int width, int height) {
		if (nbColumnsVisible < 1) {
			return new Dimension(marginWidth * 2, marginHeight * 2);
		}
		Dimension cellDimension = calculateCellSize(container, width);
		Dimension res = new Dimension(marginWidth * 2, marginHeight * 2 + cellDimension.height);
		if (move) {
			int gridY = y + marginHeight;
			int gridX = x + marginWidth;
			for (Object child : container.getChildren()) {
				IFigure fig = (IFigure)child;
				fig.setBounds(new Rectangle(gridX, gridY, cellDimension.width, cellDimension.height));
				gridX += spacing + cellDimension.width;
			}
		}
		int nbChildren = container.getChildren().size();
		res.width += cellDimension.width * nbChildren + (nbChildren - 1) * spacing;

		return res;
	}
}
