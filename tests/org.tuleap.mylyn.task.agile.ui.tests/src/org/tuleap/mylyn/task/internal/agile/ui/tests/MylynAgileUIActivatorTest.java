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
package org.tuleap.mylyn.task.internal.agile.ui.tests;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynAgileUIActivatorTest {

	/**
	 * Checks behavior for #ffffff value.
	 */
	@Test
	public void testForColorNameFFFFFFLowerCase() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#ffffff");
		assertEquals(new RGB(255, 255, 255), color.getRGB());
	}

	/**
	 * Checks behavior for #FFFFFF value.
	 */
	@Test
	public void testForColorNameFFFFFFUpperCase() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#FFFFFF");
		assertEquals(new RGB(255, 255, 255), color.getRGB());
	}

	/**
	 * Checks behavior for #000000 value.
	 */
	@Test
	public void testForColorName000000() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#000000");
		assertEquals(new RGB(0, 0, 0), color.getRGB());
	}

	/**
	 * Test behavior with arbitrary value.
	 */
	@Test
	public void testForColorName123456() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#123456");
		assertEquals(new RGB(0x12, 0x34, 0x56), color.getRGB());
	}

	/**
	 * Test behavior with invalid value (contains an invalid hex digit).
	 */
	@Test
	public void testForColorNameInvalidHexDigit() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#fffffg");
		assertNull(color);
	}

	/**
	 * Test behavior with invalid value (too short).
	 */
	@Test
	public void testForColorNameInvalidTooShort() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#fffff");
		assertNull(color);
	}

	/**
	 * Test behavior with invalid value (too long).
	 */
	@Test
	public void testForColorNameInvalidTooLong() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color color = activator.forColorName("#fffffff");
		assertNull(color);
	}

	/**
	 * Checks that putColor behaves as expected.
	 */
	@Test
	public void testPutColor() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color newColor = new Color(null, 12, 34, 56);
		Color color = activator.putColor("nice", newColor);
		assertSame(color, newColor);
	}

	/**
	 * Checks that putColor behaves as expected when a color is already cached for the given key.
	 */
	@Test
	public void testPutCachedColor() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color newColor = new Color(null, 12, 34, 56);
		Color color = activator.putColor("nice", newColor);

		// The following call must not modify the color that is already cached
		color = activator.putColor("nice", new Color(null, 65, 43, 21));
		assertSame(color, newColor);
	}

	/**
	 * Checks that getColor returns null for non-cached color.
	 */
	@Test
	public void testGetNonExistantColor() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		assertNull(activator.getColor("nice"));
	}

	/**
	 * Checks that getColor returns null for non-cached color.
	 */
	@Test
	public void testGetExistantColor() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color newColor = new Color(null, 12, 34, 56);
		activator.putColor("nice", newColor);
		assertSame(newColor, activator.getColor("nice"));
	}

	/**
	 * Checks that getColor returns null for non-cached color.
	 */
	@Test
	public void testHasColor() {
		MylynAgileUIActivator activator = new MylynAgileUIActivator();
		Color newColor = new Color(null, 12, 34, 56);
		activator.putColor("nice", newColor);
		assertTrue(activator.hasColor("nice"));
		assertFalse(activator.hasColor("not nice"));
	}

}
