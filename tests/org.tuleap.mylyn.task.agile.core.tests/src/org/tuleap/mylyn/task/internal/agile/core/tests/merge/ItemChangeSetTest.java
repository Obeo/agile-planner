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
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange.Kind;
import org.tuleap.mylyn.task.internal.agile.core.merge.FunctionalChange;
import org.tuleap.mylyn.task.internal.agile.core.merge.FunctionalChangeSet;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemChange;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemChangeSet;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests of {@link ItemChangeSet}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ItemChangeSetTest {

	private ItemChange<String> addBacklog2;

	private ItemChange<String> addMilestone3;

	private ItemChange<String> deleteBacklog1;

	private ItemChange<String> deleteMilestone4;

	private ItemChange<String> moveBacklog1to4;

	private ItemChange<String> moveMilestone3to5;

	@Before
	public void setUp() {
		moveBacklog1to4 = new ItemChange<String>(null, new Change<String>("a", Kind.MOVE, 4, 1));
		moveMilestone3to5 = new ItemChange<String>("id", new Change<String>("a", Kind.MOVE, 5, 3));
		addBacklog2 = new ItemChange<String>(null, new Change<String>("a", Kind.ADD, 2, -1));
		addMilestone3 = new ItemChange<String>("id", new Change<String>("a", Kind.ADD, 3, -1));
		deleteBacklog1 = new ItemChange<String>(null, new Change<String>("a", Kind.DELETE, -1, 1));
		deleteMilestone4 = new ItemChange<String>("id", new Change<String>("a", Kind.DELETE, -1, 4));
	}

	@Test
	public void testFunctionalChangeSetCreationWithBasicMoves() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(moveBacklog1to4);
		ics.addRemoteChange(moveBacklog1to4); // same as local

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		FunctionalChange<String> localChange = fcs.getLocalChange();
		assertTrue(localChange.isMove());

		ItemState<String> localInitState = localChange.fromState();
		assertNull(localInitState.getListId());
		assertEquals(1, localInitState.getIndex());
		ItemState<String> localFinalState = localChange.toState();
		assertNull(localFinalState.getListId());
		assertEquals(4, localFinalState.getIndex());

		FunctionalChange<String> remoteChange = fcs.getRemoteChange();
		assertTrue(remoteChange.isMove());

		ItemState<String> remoteInitState = remoteChange.fromState();
		assertNull(remoteInitState.getListId());
		assertEquals(1, remoteInitState.getIndex());
		ItemState<String> remoteFinalState = remoteChange.toState();
		assertNull(remoteFinalState.getListId());
		assertEquals(4, remoteFinalState.getIndex());
	}

	@Test
	public void testFunctionalChangeSetCreationWithComplexLocalMove() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(deleteBacklog1);
		ics.addRemoteChange(moveBacklog1to4);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		FunctionalChange<String> localChange = fcs.getLocalChange();
		assertTrue(localChange.isMove());

		ItemState<String> localInitState = localChange.fromState();
		assertNull(localInitState.getListId());
		assertEquals(1, localInitState.getIndex());
		ItemState<String> localFinalState = localChange.toState();
		assertEquals("id", localFinalState.getListId());
		assertEquals(3, localFinalState.getIndex());

		FunctionalChange<String> remoteChange = fcs.getRemoteChange();
		assertTrue(remoteChange.isMove());

		ItemState remoteInitState = remoteChange.fromState();
		assertNull(remoteInitState.getListId());
		assertEquals(1, remoteInitState.getIndex());
		ItemState remoteFinalState = remoteChange.toState();
		assertNull(remoteFinalState.getListId());
		assertEquals(4, remoteFinalState.getIndex());
	}

	@Test
	public void testFunctionalChangeSetCreationWithComplexMoves() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(deleteBacklog1);
		ics.addRemoteChange(addMilestone3);
		ics.addRemoteChange(deleteBacklog1);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		FunctionalChange<String> localChange = fcs.getLocalChange();
		assertTrue(localChange.isMove());

		ItemState<String> localInitState = localChange.fromState();
		assertNull(localInitState.getListId());
		assertEquals(1, localInitState.getIndex());
		ItemState<String> localFinalState = localChange.toState();
		assertEquals("id", localFinalState.getListId());
		assertEquals(3, localFinalState.getIndex());

		FunctionalChange<String> remoteChange = fcs.getRemoteChange();
		assertTrue(remoteChange.isMove());

		ItemState<String> remoteInitState = remoteChange.fromState();
		assertNull(remoteInitState.getListId());
		assertEquals(1, remoteInitState.getIndex());
		ItemState<String> remoteFinalState = remoteChange.toState();
		assertEquals("id", remoteFinalState.getListId());
		assertEquals(3, remoteFinalState.getIndex());
	}

	@Test
	public void testFunctionalChangeSetCreationWithRemoteDelete() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(deleteBacklog1);
		ics.addRemoteChange(deleteBacklog1);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		FunctionalChange<String> localChange = fcs.getLocalChange();
		assertTrue(localChange.isMove());
		ItemState<String> localInitState = localChange.fromState();
		assertNull(localInitState.getListId());
		assertEquals(1, localInitState.getIndex());
		ItemState<String> localFinalState = localChange.toState();
		assertEquals("id", localFinalState.getListId());
		assertEquals(3, localFinalState.getIndex());

		FunctionalChange<String> remoteChange = fcs.getRemoteChange();
		assertTrue(remoteChange.isDelete());

		assertNull(remoteChange.toState());
		ItemState<String> remoteInitialState = remoteChange.fromState();
		assertNull(remoteInitialState.getListId());
		assertEquals(1, remoteInitialState.getIndex());
	}

	@Test
	public void testLocalAddAndDeleteWithoutRemoteChange() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(deleteBacklog1);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();
		assertNull(fcs.getRemoteChange());

		FunctionalChange<String> localChange = fcs.getLocalChange();
		ItemState<String> initialState = localChange.fromState();
		ItemState<String> finalState = localChange.toState();

		assertNull(initialState.getListId());
		assertEquals(1, initialState.getIndex());
		assertEquals("id", finalState.getListId());
		assertEquals(3, finalState.getIndex());
	}

	@Test
	public void testLocalMoveWithoutRemoteChange() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		ics.addLocalChange(moveBacklog1to4);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();
		assertNull(fcs.getRemoteChange());

		FunctionalChange<String> localChange = fcs.getLocalChange();
		ItemState<String> initialState = localChange.fromState();
		ItemState<String> finalState = localChange.toState();

		assertNull(initialState.getListId());
		assertEquals(1, initialState.getIndex());
		assertNull(finalState.getListId());
		assertEquals(4, finalState.getIndex());
	}

	@Test
	public void testFunctionalChangeSetCreationWithRemoteAdd() {
		ItemChangeSet<String> ics = new ItemChangeSet();
		// No local change, otherwise invalid
		// remote change = add in milestone "id" at index 3
		ics.addRemoteChange(addBacklog2);
		// /assertTrue(ics.isValid());

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		FunctionalChange<String> localChange = fcs.getLocalChange();
		assertNull(localChange);

		FunctionalChange<String> remoteChange = fcs.getRemoteChange();
		assertTrue(remoteChange.isAdd());

		assertNull(remoteChange.fromState());
		ItemState<String> finalState = remoteChange.toState();
		assertNull(finalState.getListId());
		assertEquals(2, finalState.getIndex());
	}

	@Test
	public void testFunctionalChangeSetCreationWithLocalMoveAndRemoteAddIsInvalid() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		// local change = moved from backlog @ index 1 to milestone "id" @ index 3
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(deleteBacklog1);
		// remote change = add in milestone "id" at index 3
		ics.addRemoteChange(addMilestone3);

		assertFalse(ics.isValid());
		try {
			ics.toFunctionalChangeSet();
			fail("There should have been an exception");
		} catch (IllegalStateException e) {
			// test is ok
		}
	}

	@Test
	public void testFunctionalChangeSetLocalAddWithoutLocalRemoveOrRemoteAdd() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(addBacklog2);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();
		assertNull(fcs.getRemoteChange());
		assertTrue(fcs.getLocalChange().isAdd());
	}

	@Test
	public void testFunctionalChangeSetLocalAddWithRemoteAdd() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(addBacklog2);
		ics.addRemoteChange(addBacklog2);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();

		assertTrue(fcs.getLocalChange().isAdd());
		assertTrue(fcs.getRemoteChange().isAdd());
	}

	@Test
	public void testFunctionalChangeSetLocalRemoveWithoutLocalAdd() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(deleteBacklog1);

		FunctionalChangeSet<String> fcs = ics.toFunctionalChangeSet();
		assertNull(fcs.getRemoteChange());
		assertTrue(fcs.getLocalChange().isDelete());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullLocalChangeThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullRemoteChangeThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2LocalMovesThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(moveBacklog1to4);
		ics.addLocalChange(moveMilestone3to5);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2RemoteMovesThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(moveBacklog1to4);
		ics.addRemoteChange(moveMilestone3to5);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2LocalAddsThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(addBacklog2);
		ics.addLocalChange(addMilestone3);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2RemoteAddsThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(addBacklog2);
		ics.addRemoteChange(addMilestone3);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2LocalDeletesThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(deleteBacklog1);
		ics.addLocalChange(deleteMilestone4);
	}

	@Test(expected = IllegalStateException.class)
	public void testAdding2RemoteDeletesThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(deleteBacklog1);
		ics.addRemoteChange(deleteMilestone4);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingRemoteMoveAfterDeleteThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(deleteBacklog1);
		ics.addRemoteChange(moveBacklog1to4);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingRemoteMoveAfterAddThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(addMilestone3);
		ics.addRemoteChange(moveMilestone3to5);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingLocalMoveAfterDeleteThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(deleteBacklog1);
		ics.addLocalChange(moveBacklog1to4);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingLocalMoveAfterAddThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(addMilestone3);
		ics.addLocalChange(moveMilestone3to5);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingRemoteDeleteAfterMoveThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(moveBacklog1to4);
		ics.addRemoteChange(deleteBacklog1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingRemoteAddAfterMoveThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addRemoteChange(moveMilestone3to5);
		ics.addRemoteChange(addMilestone3);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingLocalDeleteAfterMoveThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(moveBacklog1to4);
		ics.addLocalChange(deleteBacklog1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddingLocalAddAfterMoveThrowsException() {
		ItemChangeSet<String> ics = new ItemChangeSet<String>();
		ics.addLocalChange(moveMilestone3to5);
		ics.addLocalChange(addMilestone3);
	}

}
