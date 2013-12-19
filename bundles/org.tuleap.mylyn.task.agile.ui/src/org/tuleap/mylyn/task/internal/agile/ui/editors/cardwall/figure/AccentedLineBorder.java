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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * LineBorder with an option accent color. The accent color can appear at the top or the left, its width can
 * be set.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AccentedLineBorder extends LineBorder {

	/**
	 * The accent color.
	 */
	private Color accentColor;

	/**
	 * Flag indicating the position. If set to true, the accent will be displayed at the left of the border,
	 * otherwise (the default) it will be displayed at the top.
	 */
	private boolean vertical;

	/**
	 * The accent width.
	 */
	private int accentWidth;

	/**
	 * Default constructor.
	 */
	public AccentedLineBorder() {
		super();
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param color
	 *            The border color
	 */
	public AccentedLineBorder(Color color) {
		super(color);
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param width
	 *            the border width
	 */
	public AccentedLineBorder(int width) {
		super(width);
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param color
	 *            The border color
	 * @param width
	 *            the border width
	 */
	public AccentedLineBorder(Color color, int width) {
		super(color, width);
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param color
	 *            The border color
	 * @param width
	 *            the border width
	 * @param style
	 *            the border style. For the list of valid values, see {@link org.eclipse.draw2d.Graphics}
	 */
	public AccentedLineBorder(Color color, int width, int style) {
		super(color, width, style);
		init();
	}

	/**
	 * Initializes accent with default values.
	 */
	private void init() {
		accentWidth = 4;
		accentColor = null;
	}

	/**
	 * Accent color getter.
	 * 
	 * @return the accentColor
	 */
	public Color getAccentColor() {
		return accentColor;
	}

	/**
	 * Accent color setter.
	 * 
	 * @param accentColor
	 *            the accentColor to set
	 */
	public void setAccentColor(Color accentColor) {
		this.accentColor = accentColor;
	}

	/**
	 * Vertical flag getter.
	 * 
	 * @return the accentStyle
	 */
	public boolean isVertical() {
		return vertical;
	}

	/**
	 * Vertical flag setter.
	 * 
	 * @param vertical
	 *            the vertical flag
	 */
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	/**
	 * Accent width getter.
	 * 
	 * @return the accentWidth
	 */
	public int getAccentWidth() {
		return accentWidth;
	}

	/**
	 * Accent width setter.
	 * 
	 * @param accentWidth
	 *            the accentWidth to set
	 */
	public void setAccentWidth(int accentWidth) {
		this.accentWidth = accentWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.LineBorder#getInsets(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public Insets getInsets(IFigure figure) {
		Insets insets = super.getInsets(figure);
		int w = getWidth();
		if (vertical) {
			insets = new Insets(w, w + accentWidth, w, w);
		} else {
			insets = new Insets(w + accentWidth, w, w, w);
		}
		return insets;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.LineBorder#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics,
	 *      org.eclipse.draw2d.geometry.Insets)
	 */
	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		super.paint(figure, graphics, insets);
		// When don't draw any accent if there is no color
		// Nevertheless, we consume the location of the accent so as not to resize
		// if an accent color is set later.
		if (accentColor != null) {
			Rectangle rectangle = getPaintRectangle(figure, insets);
			int w = getWidth();
			if (vertical) {
				tempRect.setBounds(rectangle.x + w, rectangle.y + w, accentWidth, rectangle.height - 2 * w);
			} else {
				tempRect.setBounds(rectangle.x + w, rectangle.y + w, rectangle.width - 2 * w, accentWidth);
			}
			graphics.setBackgroundColor(accentColor);
			graphics.fillRectangle(tempRect);
		}
	}

}
