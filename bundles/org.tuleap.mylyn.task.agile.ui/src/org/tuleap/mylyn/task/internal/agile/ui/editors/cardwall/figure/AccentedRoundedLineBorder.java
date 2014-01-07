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
public class AccentedRoundedLineBorder extends LineBorder {

	/**
	 * 90 degrees is a quarter of a turn.
	 */
	private static final int QUARTER_TURN = 90;

	/**
	 * 180 degrees is a half turn.
	 */
	private static final int HALF_TURN = 180;

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
	private int cornerRadius;

	/**
	 * Default constructor.
	 */
	public AccentedRoundedLineBorder() {
		super();
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param color
	 *            The border color
	 */
	public AccentedRoundedLineBorder(Color color) {
		super(color);
		init();
	}

	/**
	 * Constructor with the border color.
	 * 
	 * @param width
	 *            the border width
	 */
	public AccentedRoundedLineBorder(int width) {
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
	public AccentedRoundedLineBorder(Color color, int width) {
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
	public AccentedRoundedLineBorder(Color color, int width, int style) {
		super(color, width, style);
		init();
	}

	/**
	 * Initializes accent with default values.
	 */
	private void init() {
		cornerRadius = 4;
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
	 * Corner radius getter.
	 * 
	 * @return the cornerRadius
	 */
	public int getCornerRadius() {
		return cornerRadius;
	}

	/**
	 * Corner radius setter.
	 * 
	 * @param cornerRadius
	 *            the cornerRadius to set
	 */
	public void setCornerRadius(int cornerRadius) {
		this.cornerRadius = cornerRadius;
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
			insets = new Insets(w, w + cornerRadius, w, w);
		} else {
			insets = new Insets(w + cornerRadius, w, w, w);
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
		// Partly copied from org.eclipse.draw2d.LineBorder
		/*******************************************************************************
		 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved. This program and the
		 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which
		 * accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
		 * Contributors: IBM Corporation - initial API and implementation
		 *******************************************************************************/
		tempRect.setBounds(getPaintRectangle(figure, insets));
		int w = getWidth();
		if (w % 2 != 0) {
			tempRect.width--;
			tempRect.height--;
		}
		tempRect.shrink(w / 2, w / 2);
		Rectangle r = new Rectangle(tempRect);
		// End of copy
		// When don't draw any accent if there is no color
		// Nevertheless, we consume the location of the accent so as not to resize
		// if an accent color is set later.
		if (accentColor != null) {
			graphics.setBackgroundColor(accentColor);
			if (vertical) {
				tempRect.setBounds(r.x, r.y - w, 2 * cornerRadius, 2 * cornerRadius);
				graphics.fillArc(tempRect, QUARTER_TURN, QUARTER_TURN);
				tempRect.setBounds(r.x, r.y + r.height + 2 * (w - cornerRadius), 2 * cornerRadius,
						2 * cornerRadius);
				graphics.fillArc(tempRect, HALF_TURN, QUARTER_TURN);
				tempRect.setBounds(r.x, r.y - w + cornerRadius, cornerRadius, r.height + w + 2
						* (w - cornerRadius));
				graphics.fillRectangle(tempRect);
			} else {
				tempRect.setBounds(r.x - w, r.y, 2 * cornerRadius, 2 * cornerRadius);
				graphics.fillArc(tempRect, QUARTER_TURN, QUARTER_TURN);
				tempRect.setBounds(r.x + r.width + 2 * (w - cornerRadius), r.y, 2 * cornerRadius,
						2 * cornerRadius);
				graphics.fillArc(tempRect, 0, QUARTER_TURN);
				tempRect.setBounds(r.x - w + cornerRadius, r.y, r.width + w + 2 * (w - cornerRadius),
						cornerRadius);
				graphics.fillRectangle(tempRect);
			}
		}

		if (getColor() != null) {
			graphics.setForegroundColor(getColor());
		}
		graphics.setLineWidth(w);
		graphics.setLineStyle(getStyle());
		graphics.drawRoundRectangle(r, cornerRadius, cornerRadius);
	}
}
