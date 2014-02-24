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
package org.tuleap.mylyn.task.internal.agile.core.tests.data;

import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BurndownDataTest {

	@Test
	public void testBurndownData() {
		BurndownData data = new BurndownData(10, 20.0, new double[] {20., 21., 18. });
		assertEquals(10, data.getDuration());
		assertEquals(20.0, data.getCapacity(), Double.MIN_VALUE);
		double[] points = data.getPoints();
		assertEquals(3, points.length);
		assertEquals(20.0, points[0], Double.MIN_VALUE);
		assertEquals(21.0, points[1], Double.MIN_VALUE);
		assertEquals(18.0, points[2], Double.MIN_VALUE);
	}

	@Test
	public void testBurndownDataWithNullArrayThrowsException() {
		try {
			new BurndownData(10, 20.0, null);
			fail("There should have been an exception");
			// CHECKSTYLE:OFF
		} catch (RuntimeException e) {
			// CHECKSTYLE:ON
			// test ok
		}
	}
}
