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
import org.eclipse.draw2d.geometry.Insets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link AccentedRoundedLineBorder}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AccentedRoundedLineBorderTest {

	@Test
	public void testDefaultConstructor() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		assertNull(border.getAccentColor());
		assertEquals(4, border.getCornerRadius());
		assertEquals(1, border.getWidth());
	}

	@Test
	public void testConstructorWithColor() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder(ColorConstants.blue);
		assertEquals(ColorConstants.blue, border.getColor());
		assertFalse(border.isVertical());
		assertNull(border.getAccentColor());
		assertEquals(4, border.getCornerRadius());
		assertEquals(1, border.getWidth());
	}

	@Test
	public void testConstructorWithWidth() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder(3);
		assertNull(border.getAccentColor());
		assertFalse(border.isVertical());
		assertEquals(4, border.getCornerRadius());
		assertEquals(3, border.getWidth());
	}

	@Test
	public void testConstructorWithColorAndWidth() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder(ColorConstants.blue, 3);
		assertEquals(ColorConstants.blue, border.getColor());
		assertFalse(border.isVertical());
		assertNull(border.getAccentColor());
		assertEquals(4, border.getCornerRadius());
		assertEquals(3, border.getWidth());
	}

	@Test
	public void testAccentColorAcccessors() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		assertNull(border.getAccentColor());
		assertFalse(border.isVertical());
		border.setAccentColor(ColorConstants.green);
		assertEquals(ColorConstants.green, border.getAccentColor());
	}

	@Test
	public void testVerticalAcccessors() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		assertFalse(border.isVertical());
		border.setVertical(true);
		assertTrue(border.isVertical());
	}

	@Test
	public void testCornerRadiusAccessors() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		assertEquals(4, border.getCornerRadius());
		border.setCornerRadius(6);
		assertEquals(6, border.getCornerRadius());
	}

	@Test
	public void testGetInsets() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		border.setVertical(false); // Useless but robust if default changes.
		Insets insets = border.getInsets(new Figure());
		// A Horizontal accented border displays the accent color on the left
		assertEquals(new Insets(5, 1, 1, 1), insets);
	}

	@Test
	public void testGetInsetsHorizontal() {
		AccentedRoundedLineBorder border = new AccentedRoundedLineBorder();
		border.setVertical(true);
		Insets insets = border.getInsets(new Figure());
		// A Vertical accented border displays the accent color on the top
		assertEquals(new Insets(1, 5, 1, 1), insets);
	}

}
