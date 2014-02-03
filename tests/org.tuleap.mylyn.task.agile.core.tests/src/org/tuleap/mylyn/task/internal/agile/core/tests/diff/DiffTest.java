package org.tuleap.mylyn.task.internal.agile.core.tests.diff;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.diff.Change;
import org.tuleap.mylyn.task.internal.agile.core.diff.ClassicLCS;
import org.tuleap.mylyn.task.internal.agile.core.diff.Diff;
import org.tuleap.mylyn.task.internal.agile.core.diff.EqualityMatcher;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange.Kind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests of {@link Diff}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DiffTest {

	private Diff diff;

	@Before
	public void setUp() {
		diff = new Diff(new EqualityMatcher(), new ClassicLCS());
	}

	@Test
	public void testDiff1() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("czdab");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('a', Kind.MOVE,
				0, 3).add('b', Kind.MOVE, 1, 4).add('e', Kind.ADD, 4, -1).add('z', Kind.DELETE, -1, 1)
				.build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves1() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("czdab");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('a', Kind.ADD,
				0, -1).add('b', Kind.ADD, 1, -1).add('e', Kind.ADD, 4, -1).add('z', Kind.DELETE, -1, 1).add(
				'b', Kind.DELETE, -1, 4).add('b', Kind.DELETE, -1, 4).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiff2() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("ycdeb");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('a', Kind.ADD,
				0, -1).add('y', Kind.DELETE, -1, 0).add('b', Kind.MOVE, 1, 4).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves2() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("ycdeb");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('a', Kind.ADD,
				0, -1).add('y', Kind.DELETE, -1, 0).add('b', Kind.ADD, 1, -1).add('b', Kind.DELETE, -1, 4)
				.build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiff3() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(8, 9, 2, 3, 4, 1, 0);

		final List<IChange<Integer>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Integer>> expected = ExpectedDiffList.<Integer> builder().add(1, Kind.MOVE, 0, 5)
				.add(5, Kind.ADD, 4, -1).add(6, Kind.ADD, 5, -1).add(7, Kind.ADD, 6, -1).add(8, Kind.DELETE,
						-1, 0).add(9, Kind.DELETE, -1, 1).add(0, Kind.DELETE, -1, 6).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves3() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(8, 9, 2, 3, 4, 1, 0);

		final List<IChange<Integer>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Integer>> expected = ExpectedDiffList.<Integer> builder().add(1, Kind.ADD, 0, -1)
				.add(5, Kind.ADD, 4, -1).add(6, Kind.ADD, 5, -1).add(7, Kind.ADD, 6, -1).add(8, Kind.DELETE,
						-1, 0).add(9, Kind.DELETE, -1, 1).add(1, Kind.DELETE, -1, 5).add(0, Kind.DELETE, -1,
						6).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiff4() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(6, 2, 9, 3, 0, 4, 1, 7);

		final List<IChange<Integer>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Integer>> expected = ExpectedDiffList.<Integer> builder().add(1, Kind.MOVE, 0, 6)
				.add(5, Kind.ADD, 4, -1).add(6, Kind.MOVE, 5, 0).add(9, Kind.DELETE, -1, 2).add(0,
						Kind.DELETE, -1, 4).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves4() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(6, 2, 9, 3, 0, 4, 1, 7);

		final List<IChange<Integer>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Integer>> expected = ExpectedDiffList.<Integer> builder().add(1, Kind.ADD, 0, -1)
				.add(5, Kind.ADD, 4, -1).add(6, Kind.ADD, 5, -1).add(6, Kind.DELETE, -1, 0).add(9,
						Kind.DELETE, -1, 2).add(0, Kind.DELETE, -1, 4).add(1, Kind.DELETE, -1, 6).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiff5() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("AGTGCTGAAAGTTGCGCCAGTGAC");
		final ImmutableList<Character> seq2 = Lists.charactersOf("AGTGCTGAAGTTCGCCAGTTGACG");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('A', Kind.ADD,
				9, -1).add('G', Kind.MOVE, 13, 23).add('T', Kind.DELETE, -1, 19).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves5() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("AGTGCTGAAAGTTGCGCCAGTGAC");
		final ImmutableList<Character> seq2 = Lists.charactersOf("AGTGCTGAAGTTCGCCAGTTGACG");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('A', Kind.ADD,
				9, -1).add('G', Kind.ADD, 13, -1).add('T', Kind.DELETE, -1, 19).add('G', Kind.DELETE, -1, 23)
				.build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiff6() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("CACAATTTGTTCCCAGAGAGA");
		final ImmutableList<Character> seq2 = Lists.charactersOf("CGAATTGTTTCGCCAGAGAGA");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('C', Kind.ADD,
				2, -1).add('A', Kind.ADD, 4, -1).add('T', Kind.MOVE, 7, 9).add('G', Kind.DELETE, -1, 1).add(
				'G', Kind.DELETE, -1, 11).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	@Test
	public void testDiffNoMoves6() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("CACAATTTGTTCCCAGAGAGA");
		final ImmutableList<Character> seq2 = Lists.charactersOf("CGAATTGTTTCGCCAGAGAGA");

		final List<IChange<Character>> differences = diff.computeDifferences(seq1, seq2, false);

		// @formatter:off
		final List<IChange<Character>> expected = ExpectedDiffList.<Character> builder().add('C', Kind.ADD,
				2, -1).add('A', Kind.ADD, 4, -1).add('T', Kind.ADD, 7, -1).add('G', Kind.DELETE, -1, 1).add(
				'T', Kind.DELETE, -1, 9).add('G', Kind.DELETE, -1, 11).build();
		// @formatter:on

		assertEquals(expected.size(), differences.size());
		assertContainsAll(differences, expected);
	}

	private static <E> void assertContainsAll(List<E> list, List<E> expectedContained) {
		// Use contains instead of containsAll to get a pretty error message
		for (E expected : expectedContained) {
			if (!list.contains(expected)) {
				fail(expected + " was not found in " + list);
			}
		}
	}

	private static final class ExpectedDiffList<E> {
		private final List<IChange<E>> changes = new ArrayList<IChange<E>>();

		private ExpectedDiffList() {
		}

		public static <E> ExpectedDiffList<E> builder() {
			return new ExpectedDiffList<E>();
		}

		public ExpectedDiffList<E> add(E object, Kind kind, int indexInSource, int indexInReference) {
			changes.add(new Change<E>(object, kind, indexInSource, indexInReference));
			return this;
		}

		public List<IChange<E>> build() {
			return changes;
		}
	}
}
