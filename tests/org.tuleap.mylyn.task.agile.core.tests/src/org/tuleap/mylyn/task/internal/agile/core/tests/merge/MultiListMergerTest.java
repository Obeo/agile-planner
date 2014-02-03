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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.tuleap.mylyn.task.internal.agile.core.merge.BasicThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.MultiListMerger;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@RunWith(Parameterized.class)
public class MultiListMergerTest {

	private List<IThreeWayList<String>> lists;

	private Map<String, List<String>> expected;

	/**
	 * 
	 */
	public MultiListMergerTest(String label, List<IThreeWayList<String>> lists,
			Map<String, List<String>> expected) {
		this.lists = lists;
		this.expected = expected;
	}

	/**
	 * Runs the parameterized test.
	 */
	@Test
	public void test() {
		MultiListMerger<String> merger = new MultiListMerger<String>();
		Map<String, List<String>> merge = merger.merge(lists);
		assertEquals(merge.size(), expected.size());
		for (Entry<String, List<String>> entry : expected.entrySet()) {
			List<String> result = merge.get(entry.getKey());
			assertEquals(entry.getValue(), result);
		}
	}

	@Parameters(name = "{index}: {0}")
	public static List<Object[]> data() {
		List<Object[]> result = Lists.newArrayList();
		List<String> l0 = of("0", "1", "2", "3");
		List<String> l4 = of("4", "5", "6", "7");
		List<String> l8 = of("8", "9", "10", "11");
		List<String> l12 = of("12", "13", "14", "15");

		result.add(new MergeTestCase("No change").with(null, l0, l0, l0).with("m0", l4, l4, l4).expecting(
				null, l0).expecting("m0", l4).toParam());

		// CHECKSTYLE:OFF
		{
			List<String> l0IntraMove = of("0", "2", "3", "1");
			result.add(new MergeTestCase("Local move intra-list").with(null, l0, l0IntraMove, l0).with("m0",
					l4, l4, l4).expecting(null, asList("0", "2", "3", "1")).expecting("m0",
					asList("4", "5", "6", "7")).toParam());

			result.add(new MergeTestCase("Remote move intra-list").with(null, l0, l0IntraMove, l0).with("m0",
					l4, l4, l4).expecting(null, asList("0", "2", "3", "1")).expecting("m0",
					asList("4", "5", "6", "7")).toParam());
		}
		{
			List<String> l0InterMove = of("0", "2", "3");
			List<String> l4InterMove = of("4", "5", "1", "6", "7");
			result.add(new MergeTestCase("Local move inter-list").with(null, l0, l0InterMove, l0).with("m0",
					l4, l4InterMove, l4).expecting(null, asList("0", "2", "3")).expecting("m0",
					asList("4", "5", "1", "6", "7")).toParam());

			result.add(new MergeTestCase("Remote move inter-list").with(null, l0, l0, l0InterMove).with("m0",
					l4, l4, l4InterMove).expecting(null, asList("0", "2", "3")).expecting("m0",
					asList("4", "5", "1", "6", "7")).toParam());
		}
		{
			List<String> ll0InterMove = of("0", "2", "3");
			List<String> ll4InterMove = of("4", "5", "1", "6", "7");
			List<String> rl0InterMove = of("0", "2", "3");
			List<String> rl4InterMove = of("1", "4", "5", "6", "7");
			result.add(new MergeTestCase("Local & Remote same move inter-list").with(null, l0, ll0InterMove,
					ll0InterMove).with("m0", l4, ll4InterMove, ll4InterMove).expecting(null,
					asList("0", "2", "3")).expecting("m0", asList("4", "5", "1", "6", "7")).toParam());

			result.add(new MergeTestCase("Conflictual move inter-list").with(null, l0, ll0InterMove,
					rl0InterMove).with("m0", l4, ll4InterMove, rl4InterMove).expecting(null,
					asList("0", "2", "3")).expecting("m0", asList("4", "5", "1", "6", "7")).toParam());
			// local move wins
		}
		{
			// "3" moved intra, "0" move inter
			List<String> ll0InterMove = of("3", "1", "2");
			List<String> ll4InterMove = of("4", "5", "0", "6", "7");
			result.add(new MergeTestCase("Local same moves inter & intra-list").with(null, l0, ll0InterMove,
					l0).with("m0", l4, ll4InterMove, l4).expecting(null, asList("3", "1", "2")).expecting(
					"m0", asList("4", "5", "0", "6", "7")).toParam());
		}
		{
			List<String> ll0InterMove = of("0", "2", "3");
			List<String> ll4InterMove = of("4", "5", "1", "6", "7");
			List<String> rl0InterMove = of("0", "1", "3");
			List<String> rl4InterMove = of("2", "4", "5", "6", "7");
			result.add(new MergeTestCase("Non-conflictual move inter-list").with(null, l0, ll0InterMove,
					rl0InterMove).with("m0", l4, ll4InterMove, rl4InterMove)
					.expecting(null, asList("0", "3")).expecting("m0", asList("2", "4", "5", "1", "6", "7"))
					.toParam());
		}
		{
			List<String> ll0InterMove = of("2", "3");
			List<String> ll4InterMove = of("0", "4", "5", "1", "6", "7");
			List<String> rl0InterMove = of("0", "1", "3");
			List<String> rl4InterMove = of("2", "4", "5", "6", "7");
			result.add(new MergeTestCase("Conflictual and Non-conflictual moves inter-list").with(null, l0,
					ll0InterMove, rl0InterMove).with("m0", l4, ll4InterMove, rl4InterMove).expecting(null,
					asList("3")).expecting("m0", asList("0", "2", "4", "5", "1", "6", "7")).toParam());
		}
		{
			List<String> rl0Delete = of("0", "2", "3");
			result.add(new MergeTestCase("Remote delete").with(null, l0, l0, rl0Delete)
					.with("m0", l4, l4, l4).expecting(null, asList("0", "2", "3")).expecting("m0",
							asList("4", "5", "6", "7")).toParam());
		}
		{
			List<String> ll0InterMove = of("1", "2", "3");
			List<String> ll4InterMove = of("4", "0", "5", "6", "7");
			List<String> rl0Delete = of("0", "2", "3");
			result.add(new MergeTestCase("Local inter move and remote delete, no conflict").with(null, l0,
					ll0InterMove, rl0Delete).with("m0", l4, ll4InterMove, l4).expecting(null,
					asList("2", "3")).expecting("m0", asList("4", "0", "5", "6", "7")).toParam());
		}
		{
			List<String> ll0IntraMove = of("0", "3", "1", "2"); // "3" moved from index 3 to 1
			List<String> rl0Delete = of("0", "2", "3"); // "1" removed at index 1
			result.add(new MergeTestCase("Local intra move and remote delete, no conflict but same index")
					.with(null, l0, ll0IntraMove, rl0Delete).with("m0", l4, l4, l4).expecting(null,
							asList("0", "3", "2")).expecting("m0", asList("4", "5", "6", "7")).toParam());
		}
		{
			List<String> ll0IntraMove = of("0", "3", "1", "2"); // "3" moved from index 3 to 1
			List<String> rl0InterMove = of("0", "2", "3"); // "1" removed at index 1
			List<String> rl4InterMove = of("4", "1", "5", "6", "7"); // "1" added at index 1
			result.add(new MergeTestCase("Local intra move and remote inter move, no conflict but same index")
					.with(null, l0, ll0IntraMove, rl0InterMove).with("m0", l4, l4, rl4InterMove).expecting(
							null, asList("0", "3", "2")).expecting("m0", asList("4", "1", "5", "6", "7"))
					.toParam());
		}
		{
			List<String> ll0InterMove = of("0", "2", "3");
			List<String> ll4InterMove = of("4", "1", "5", "6", "7");
			List<String> rl0Delete = of("0", "2", "3");
			result.add(new MergeTestCase("Local inter move conflict with remote delete").with(null, l0,
					ll0InterMove, rl0Delete).with("m0", l4, ll4InterMove, l4).expecting(null,
					asList("0", "2", "3")).expecting("m0", asList("4", "5", "6", "7")).toParam());
		}
		{
			List<String> rl0Add = of("0", "1", "100", "2", "3");
			result.add(new MergeTestCase("Remote add").with(null, l0, l0, rl0Add).with("m0", l4, l4, l4)
					.expecting(null, asList("0", "1", "100", "2", "3")).expecting("m0",
							asList("4", "5", "6", "7")).toParam());
		}
		{
			List<String> ll0InterMove = of("0", "2", "3");
			List<String> ll4InterMove = of("4", "1", "5", "6", "7");
			List<String> rl0Add = of("0", "1", "100", "2", "3");
			result.add(new MergeTestCase("Remote add and local inter move, no conflict").with(null, l0,
					ll0InterMove, rl0Add).with("m0", l4, ll4InterMove, l4).expecting(null,
					asList("0", "100", "2", "3")).expecting("m0", asList("4", "1", "5", "6", "7")).toParam());
		}
		// CHECKSTYLE:ON
		return result;
	}

	/**
	 * Builder of test parameters
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class MergeTestCase {

		/**
		 * The test label.
		 */
		private final String label;

		/**
		 * The 3-way input lists.
		 */
		private final List<IThreeWayList<String>> lists = Lists.newArrayList();

		/**
		 * The expected lists.
		 */
		private final Map<String, List<String>> expected = Maps.newLinkedHashMap();

		/**
		 * Constructor, label is mandatory for readability.
		 * 
		 * @param label
		 *            The label
		 */
		public MergeTestCase(String label) {
			this.label = label;
		}

		/**
		 * Add a 3-way list with the given elements.
		 * 
		 * @param id
		 *            list id
		 * @param ancestor
		 *            common ancestor
		 * @param local
		 *            local list
		 * @param remote
		 *            remote list
		 * @return this for fluency
		 */
		public MergeTestCase with(String id, List<String> ancestor, List<String> local, List<String> remote) {
			lists.add(new BasicThreeWayList<String>(id, ancestor, local, remote));
			return this;
		}

		/**
		 * Add an expected result for the given id
		 * 
		 * @param id
		 *            list id
		 * @param list
		 *            expected result of the merge
		 * @return this for fluency
		 */
		public MergeTestCase expecting(String id, List<String> list) {
			expected.put(id, list);
			return this;
		}

		@Override
		public String toString() {
			return label;
		}

		/**
		 * Build the array of test parameters.
		 * 
		 * @return The test parameters.
		 */
		public Object[] toParam() {
			return new Object[] {label, lists, expected };
		}

	}
}
