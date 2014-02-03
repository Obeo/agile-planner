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

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.diff.Change;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange.Kind;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemChange;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * {@link ItemChange} unit tests.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ItemChangeTest {

	private IChange<String> add;

	private IChange<String> delete;

	private IChange<String> move;

	@Before
	public void setUp() {
		add = new Change<String>("a", Kind.ADD, 1, -1);
		delete = new Change<String>("b", Kind.DELETE, -1, 2);
		move = new Change<String>("c", Kind.MOVE, 3, 5); // Moved from index 5 to index 3
	}

	@Test
	public void testBacklogItemChange() {
		ItemChange<String> itemChange = new ItemChange<String>(null, add);
		assertNull(itemChange.getListId());
		assertSame(add, itemChange.getChange());
	}

	@Test
	public void testMilestoneItemChange() {
		ItemChange<String> itemChange = new ItemChange<String>("id", add);
		assertEquals("id", itemChange.getListId());
		assertSame(add, itemChange.getChange());
	}

	@Test
	public void testGetStatesForAddition() {
		ItemChange<String> itemChange = new ItemChange<String>(null, add);
		assertNull(itemChange.initialState());
		ItemState state = itemChange.finalState();
		assertEquals(1, state.getIndex());
		assertNull(state.getListId());
	}

	@Test
	public void testGetStatesForDeletion() {
		ItemChange<String> itemChange = new ItemChange<String>(null, delete);
		assertNull(itemChange.finalState());
		ItemState<String> state = itemChange.initialState();
		assertEquals(2, state.getIndex());
		assertNull(state.getListId());
	}

	@Test
	public void testGetStatesForMove() {
		ItemChange<String> itemChange = new ItemChange<String>(null, move);
		ItemState<String> initialState = itemChange.initialState();
		ItemState<String> finalState = itemChange.finalState();
		assertEquals(5, initialState.getIndex());
		assertEquals(3, finalState.getIndex());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiationWithNullChange() {
		ItemChange<String> itemChange = new ItemChange<String>(null, null);
	}
}
