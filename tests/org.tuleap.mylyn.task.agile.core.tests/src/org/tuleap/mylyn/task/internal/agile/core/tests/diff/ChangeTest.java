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
package org.tuleap.mylyn.task.internal.agile.core.tests.diff;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.diff.Change;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ChangeTest {

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#getObject()}.
	 */
	@Test
	public void testGetObject() {
		String o = "Changed";
		Change<String> c = new Change<String>(o, Change.Kind.ADD, 0, 1);
		assertSame(o, c.getObject());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#getKind()}.
	 */
	@Test
	public void testGetKind() {
		String o = "Changed";
		Change<String> c = new Change<String>(o, Change.Kind.ADD, 0, 1);
		assertSame(Change.Kind.ADD, c.getKind());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#getLeftIndex()}.
	 */
	@Test
	public void testGetLeftIndex() {
		String o = "Changed";
		Change<String> c = new Change<String>(o, Change.Kind.ADD, 0, 1);
		assertSame(0, c.getLeftIndex());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#getRightIndex()}.
	 */
	@Test
	public void testGetRightIndex() {
		String o = "Changed";
		Change<String> c = new Change<String>(o, Change.Kind.ADD, 0, 1);
		assertSame(1, c.getRightIndex());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#toString()}.
	 */
	@Test
	public void testToString() {
		String o = "Changed";
		Change<String> c = new Change<String>(o, Change.Kind.ADD, 0, 1);
		assertEquals("ADD Changed", c.toString());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#toString()}.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testNullObject() {
		new Change<String>(null, Change.Kind.ADD, 0, 1);
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.internal.agile.core.diff.Change#toString()}.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testNullKind() {
		String o = "Changed";
		new Change<String>(o, null, 0, 1);
	}

}
