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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.merge.BasicThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.FunctionalChangeSet;
import org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.ItemState;
import org.tuleap.mylyn.task.internal.agile.core.merge.MultiListDiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests of MultiListDiff.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MultiListDiffTest {

	private List<String> backlog;

	private List<String> m0;

	private List<String> m1;

	private List<String> lbacklog;

	private List<String> lm0;

	private List<String> lm1;

	private List<String> rbacklog;

	private List<String> rm0;

	private List<String> rm1;

	@Before
	public void setUp() {
		backlog = Arrays.asList("1", "2", "3", "4");
		m0 = Arrays.asList("5", "6", "7", "8");
		m1 = Arrays.asList("9", "10");
		lbacklog = Arrays.asList("1", "2", "3", "4");
		lm0 = Arrays.asList("5", "6", "7", "8");
		lm1 = Arrays.asList("9", "10");
		rbacklog = Arrays.asList("1", "2", "3", "4");
		rm0 = Arrays.asList("5", "6", "7", "8");
		rm1 = Arrays.asList("9", "10");
	}

	@Test
	public void testNoDiff() {
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		assertEquals(0, diff.getFunctionalChangeSets().size());
	}

	@Test
	public void testLocalDiffMoveInsideBacklog() {
		lbacklog = Arrays.asList("2", "3", "1", "4");
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		Map<String, FunctionalChangeSet<String>> changes = diff.getFunctionalChangeSets();
		assertEquals(1, changes.size());
		assertTrue(changes.containsKey("1"));

		FunctionalChangeSet<String> change = changes.get("1");
		assertNull(change.getRemoteChange());

		ItemState<String> initState = change.getLocalChange().fromState();
		ItemState<String> finalState = change.getLocalChange().toState();
		assertEquals(0, initState.getIndex());
		assertEquals(2, finalState.getIndex());
		assertNull(initState.getListId());
		assertNull(finalState.getListId());
	}

	@Test
	public void testLocalDiffMoveFromBacklogToM0() {
		lbacklog = Arrays.asList("2", "3", "4");
		lm0 = Arrays.asList("5", "1", "6", "7", "8");
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		@SuppressWarnings("unchecked")
		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		Map<String, FunctionalChangeSet<String>> changes = diff.getFunctionalChangeSets();
		assertEquals(1, changes.size());
		assertTrue(changes.containsKey("1"));

		FunctionalChangeSet<String> change = changes.get("1");
		assertNull(change.getRemoteChange());

		ItemState<String> initState = change.getLocalChange().fromState();
		ItemState<String> finalState = change.getLocalChange().toState();
		assertEquals(0, initState.getIndex());
		assertEquals(1, finalState.getIndex());
		assertNull(initState.getListId());
		assertEquals("m0", finalState.getListId());
	}

	@Test
	public void testRemoteDiffMoveInsideBacklog() {
		rbacklog = Arrays.asList("2", "3", "1", "4");
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		Map<String, FunctionalChangeSet<String>> changes = diff.getFunctionalChangeSets();
		assertEquals(1, changes.size());
		assertTrue(changes.containsKey("1"));

		FunctionalChangeSet<String> change = changes.get("1");
		assertNull(change.getLocalChange());

		ItemState<String> initState = change.getRemoteChange().fromState();
		ItemState<String> finalState = change.getRemoteChange().toState();
		assertEquals(0, initState.getIndex());
		assertEquals(2, finalState.getIndex());
		assertNull(initState.getListId());
		assertNull(finalState.getListId());
	}

	@Test
	public void testRemoteDiffMoveFromBacklogToM0() {
		rbacklog = Arrays.asList("2", "3", "4");
		rm0 = Arrays.asList("5", "1", "6", "7", "8");
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		@SuppressWarnings("unchecked")
		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		Map<String, FunctionalChangeSet<String>> changes = diff.getFunctionalChangeSets();
		assertEquals(1, changes.size());
		assertTrue(changes.containsKey("1"));

		FunctionalChangeSet<String> change = changes.get("1");
		assertNull(change.getLocalChange());

		ItemState<String> initState = change.getRemoteChange().fromState();
		ItemState<String> finalState = change.getRemoteChange().toState();
		assertEquals(0, initState.getIndex());
		assertEquals(1, finalState.getIndex());
		assertNull(initState.getListId());
		assertEquals("m0", finalState.getListId());
	}

	@Test
	public void testRemoteAndLocalDiffsNoConflict() {
		// local : "1" moved from backlog to m0 at index 0
		// remote: "3" moved in backlog at index 0
		lbacklog = Arrays.asList("2", "3", "4");
		lm0 = Arrays.asList("1", "5", "6", "7", "8");
		rbacklog = Arrays.asList("3", "1", "2", "4");
		IThreeWayList<String> backlog3w = new BasicThreeWayList<String>(null, backlog, lbacklog, rbacklog);
		IThreeWayList<String> m03w = new BasicThreeWayList<String>("m0", m0, lm0, rm0);
		IThreeWayList<String> m13w = new BasicThreeWayList<String>("m1", m1, lm1, rm1);

		@SuppressWarnings("unchecked")
		MultiListDiff<String> diff = MultiListDiff.build(Arrays.asList(backlog3w, m03w, m13w));
		assertNotNull(diff);
		Map<String, FunctionalChangeSet<String>> changes = diff.getFunctionalChangeSets();
		assertEquals(2, changes.size());
		assertTrue(changes.containsKey("1"));
		assertTrue(changes.containsKey("3"));

		FunctionalChangeSet<String> change = changes.get("1");
		assertNull(change.getRemoteChange());
		ItemState<String> initState = change.getLocalChange().fromState();
		ItemState<String> finalState = change.getLocalChange().toState();
		assertEquals(0, initState.getIndex());
		assertEquals(0, finalState.getIndex());
		assertNull(initState.getListId());
		assertEquals("m0", finalState.getListId());

		change = changes.get("3");
		assertNull(change.getLocalChange());
		initState = change.getRemoteChange().fromState();
		finalState = change.getRemoteChange().toState();
		assertEquals(2, initState.getIndex());
		assertEquals(0, finalState.getIndex());
		assertNull(initState.getListId());
		assertNull(finalState.getListId());
	}

}
