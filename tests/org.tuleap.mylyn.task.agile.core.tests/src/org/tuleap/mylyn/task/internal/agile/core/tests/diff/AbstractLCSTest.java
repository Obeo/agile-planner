package org.tuleap.mylyn.task.internal.agile.core.tests.diff;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.core.diff.AbstractLCS;
import org.tuleap.mylyn.task.internal.agile.core.diff.EqualityMatcher;
import org.tuleap.mylyn.task.internal.agile.core.diff.IMatcher;
import org.tuleap.mylyn.task.internal.agile.core.diff.LCS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Abstract super class of unit tests for LCS implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractLCSTest {

	protected abstract LCS getImplementation();

	protected IMatcher getMatcher() {
		return new EqualityMatcher();
	}

	@Test
	public void testCommonAffixLength1() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("czdab");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertSame(0, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, getMatcher()));
			assertSame(0, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, getMatcher()));
		}
	}

	@Test
	public void testLCS1() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("czdab");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Character> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.charactersOf("cd"), lcs);
	}

	@Test
	public void testCommonAffixLength2() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("abcde");
		final ImmutableList<Character> seq2 = Lists.charactersOf("ycdeb");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertEquals(0, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, matcher));
			assertEquals(0, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, matcher));
		}
	}

	@Test
	public void testLCS2() {
		final List<Character> seq1 = Lists.charactersOf("abcde");
		final List<Character> seq2 = Lists.charactersOf("ycdeb");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Character> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.charactersOf("cde"), lcs);
	}

	@Test
	public void testCommonAffixLength3() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(8, 9, 2, 3, 4, 1, 0);

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertSame(0, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, matcher));
			assertSame(0, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, matcher));
		}
	}

	@Test
	public void testLCS3() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(8, 9, 2, 3, 4, 1, 0);

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Integer> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.newArrayList(2, 3, 4), lcs);
	}

	@Test
	public void testCommonAffixLength4() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(6, 2, 9, 3, 0, 4, 1, 7);

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertSame(0, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, matcher));
			assertSame(1, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, matcher));
		}
	}

	@Test
	public void testLCS4() {
		final ArrayList<Integer> seq1 = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final ArrayList<Integer> seq2 = Lists.newArrayList(6, 2, 9, 3, 0, 4, 1, 7);

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Integer> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.newArrayList(2, 3, 4, 7), lcs);
	}

	@Test
	public void testCommonAffixLength5() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("AGTGCTGAAAGTTGCGCCAGTGAC");
		final ImmutableList<Character> seq2 = Lists.charactersOf("AGTGCTGAAGTTCGCCAGTTGACG");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertSame(9, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, matcher));
			assertSame(0, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, matcher));
		}
	}

	@Test
	public void testLCS5() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("AGTGCTGAAAGTTGCGCCAGTGAC");
		final ImmutableList<Character> seq2 = Lists.charactersOf("AGTGCTGAAGTTCGCCAGTTGACG");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Character> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.charactersOf("AGTGCTGAAGTTCGCCAGTGAC"), lcs);
	}

	@Test
	public void testCommonAffixLength6() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("CACAATTTGTTCCCAGAGAGA");
		final ImmutableList<Character> seq2 = Lists.charactersOf("CGAATTGTTTCGCCAGAGAGA");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		if (implementation instanceof AbstractLCS) {
			assertSame(1, ((AbstractLCS)implementation).commonPrefixLength(seq1, seq2, matcher));
			assertSame(9, ((AbstractLCS)implementation).commonSuffixLength(seq1, seq2, matcher));
		}
	}

	@Test
	public void testLCS6() {
		final ImmutableList<Character> seq1 = Lists.charactersOf("CACAATTTGTTCCCAGAGAGA");
		final ImmutableList<Character> seq2 = Lists.charactersOf("CGAATTGTTTCGCCAGAGAGA");

		final LCS implementation = getImplementation();
		final IMatcher matcher = getMatcher();
		final List<Character> lcs = implementation.longestCommonSubsequence(seq1, seq2, matcher);

		assertEqualContents(Lists.charactersOf("CAATTGTTCCCAGAGAGA"), lcs);
	}

	/**
	 * Ensures that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	private static <T> void assertEqualContents(List<T> list1, List<T> list2) {
		final int size = list1.size();
		assertEquals(size, list2.size());

		for (int i = 0; i < size; i++) {
			assertEquals(list1.get(i), list2.get(i));
		}
	}
}
