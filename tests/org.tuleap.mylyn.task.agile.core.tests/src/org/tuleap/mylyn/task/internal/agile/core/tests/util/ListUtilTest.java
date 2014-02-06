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
package org.tuleap.mylyn.task.internal.agile.core.tests.util;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.util.ListUtil;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ListUtilTest {

	@Test
	public void testMoveNoMove() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "a", true);
		assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
	}

	@Test
	public void testMoveNoMove2() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "a", false);
		assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
	}

	@Test
	public void testMoveNoMove3() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "b", true);
		assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
	}

	@Test
	public void testMoveNoMove4() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "c", true);
		assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
	}

	@Test
	public void testMoveNoMove5() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "d", true);
		assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
	}

	@Test
	public void testMoveAfter() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "d", false);
		assertEquals(Arrays.asList("d", "a", "b", "c", "e"), result);
	}

	@Test
	public void testMoveBeforeLast() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "e", true);
		assertEquals(Arrays.asList("d", "a", "b", "c", "e"), result);
	}

	@Test
	public void testMoveAfterLast() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), "e", false);
		assertEquals(Arrays.asList("d", "e", "a", "b", "c"), result);
	}

	@Test
	public void testMoveWithoutTargetBefore() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), null, true);
		assertEquals(Arrays.asList("d", "e", "a", "b", "c"), result);
	}

	@Test
	public void testMoveWithoutTargetAfter() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c",
				"d", "e"), null, false);
		assertEquals(Arrays.asList("d", "e", "a", "b", "c"), result);
	}

	@Test
	public void testInsertWithoutTargetAfter() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e"),
				null, false);
		assertEquals(Arrays.asList("d", "e", "a", "b", "c"), result);
	}

	@Test
	public void testInsertWithoutTargetBefore() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e"),
				null, true);
		assertEquals(Arrays.asList("d", "e", "a", "b", "c"), result);
	}

	@Test
	public void testMoveLastAndFirstInTheMiddleBefore() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "e"), Arrays.asList("a", "b", "c", "d",
				"e"), "c", true);
		assertEquals(Arrays.asList("b", "a", "e", "c", "d"), result);
	}

	@Test
	public void testMoveLastAndFirstInTheMiddleAfter() {
		List<String> result = ListUtil.mergeItems(Arrays.asList("a", "e"), Arrays.asList("a", "b", "c", "d",
				"e"), "c", false);
		assertEquals(Arrays.asList("b", "c", "a", "e", "d"), result);
	}

}
