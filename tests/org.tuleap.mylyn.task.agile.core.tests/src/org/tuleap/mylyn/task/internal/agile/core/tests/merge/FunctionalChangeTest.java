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
package org.tuleap.mylyn.task.internal.agile.core.tests.merge;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.merge.FunctionalChange;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests of {@link FunctionalChange}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FunctionalChangeTest {

	@Test
	public void testAddition() {
		FunctionalChange<String> change = FunctionalChange.addition(new ItemState<String>("id", 12));
		assertTrue(change.isAdd());
		assertFalse(change.isMove());
		assertFalse(change.isDelete());
		assertNull(change.fromState());
		assertEquals("id", change.toState().getListId());
		assertEquals(12, change.toState().getIndex());
	}

	@Test
	public void testDeletion() {
		FunctionalChange<String> change = FunctionalChange.deletion(new ItemState<String>("id", 12));
		assertTrue(change.isDelete());
		assertFalse(change.isAdd());
		assertFalse(change.isMove());
		assertNull(change.toState());
		assertEquals("id", change.fromState().getListId());
		assertEquals(12, change.fromState().getIndex());
	}

	@Test
	public void testMove() {
		FunctionalChange<String> change = FunctionalChange.move(new ItemState<String>("old", 4),
				new ItemState<String>("id", 12));
		assertTrue(change.isMove());
		assertFalse(change.isAdd());
		assertFalse(change.isDelete());

		assertEquals("old", change.fromState().getListId());
		assertEquals(4, change.fromState().getIndex());
		assertEquals("id", change.toState().getListId());
		assertEquals(12, change.toState().getIndex());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAdditionWithNullArgument() {
		FunctionalChange.addition(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDeletionWithNullArgument() {
		FunctionalChange.deletion(null);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMoveWithNullFrom() {
		FunctionalChange.move(null, new ItemState<String>("id", 12));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMoveWithNullTo() {
		FunctionalChange.move(new ItemState<String>("id", 12), null);
	}
}
