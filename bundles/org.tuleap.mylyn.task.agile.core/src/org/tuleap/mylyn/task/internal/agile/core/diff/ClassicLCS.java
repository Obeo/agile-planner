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
package org.tuleap.mylyn.task.internal.agile.core.diff;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.reverse;

/**
 * Implementation of classic algorithm for computing the LCS.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ClassicLCS extends AbstractLCS {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.diff.LCS#longestCommonSubsequence(java.util.List,
	 *      java.util.List, org.tuleap.mylyn.task.internal.agile.core.diff.IMatcher)
	 */
	@Override
	public <T> List<T> longestCommonSubsequence(List<T> left, List<T> right, IMatcher matcher) {
		final int prefixLength = commonPrefixLength(left, right, matcher);
		if (prefixLength == left.size()) {
			return ImmutableList.copyOf(left);
		}
		final int suffixLength = commonSuffixLength(left, right, matcher);

		final List<T> reduced1 = left.subList(prefixLength, left.size() - suffixLength);
		final List<T> reduced2 = right.subList(prefixLength, right.size() - suffixLength);

		final int size1 = reduced1.size();
		final int size2 = reduced2.size();

		final List<T> middle;
		if (size1 > Short.MAX_VALUE || size2 > Short.MAX_VALUE) {
			middle = intScoreLCS(reduced1, reduced2, matcher);
		} else {
			middle = shortScoreLCS(reduced1, reduced2, matcher);
		}

		List<T> prefix = left.subList(0, prefixLength);
		List<T> suffix = left.subList(left.size() - suffixLength, left.size());
		return ImmutableList.copyOf(concat(prefix, middle, suffix));
	}

	/**
	 * Implementation based on shorts to minimize memory usage.
	 * 
	 * @param <T>
	 *            The type of elements in the list.
	 * @param left
	 *            The first list
	 * @param right
	 *            The second list
	 * @param matcher
	 *            The matcher
	 * @return (One of) the longest common subsequence.
	 */
	private <T> List<T> shortScoreLCS(List<T> left, List<T> right, IMatcher matcher) {
		final int size1 = left.size();
		final int size2 = right.size();

		final short[][] matrix = new short[size1 + 1][size2 + 1];

		// Construct the LCS score matrix
		for (int i = 1; i <= size1; i++) {
			final T first = left.get(i - 1);
			for (int j = 1; j <= size2; j++) {
				// assume array dereferencing and arithmetics faster than equals
				final short current = matrix[i - 1][j - 1];
				final short nextIfNoMatch = (short)Math.max(matrix[i - 1][j], matrix[i][j - 1]);

				if (nextIfNoMatch > current) {
					matrix[i][j] = nextIfNoMatch;
				} else {
					final T second = right.get(j - 1);
					if (matcher.match(first, second)) {
						matrix[i][j] = (short)(1 + current);
					} else {
						matrix[i][j] = nextIfNoMatch;
					}
				}
			}
		}

		// Traceback to create the final LCS
		int current1 = size1;
		int current2 = size2;
		final List<T> result = new ArrayList<T>();

		while (current1 > 0 && current2 > 0) {
			final short currentLength = matrix[current1][current2];
			final short nextLeft = matrix[current1][current2 - 1];
			final short nextUp = matrix[current1 - 1][current2];
			if (currentLength > nextLeft && currentLength > nextUp) {
				result.add(left.get(current1 - 1));
				current1--;
				current2--;
			} else if (nextLeft >= nextUp) {
				current2--;
			} else {
				current1--;
			}
		}

		return reverse(result);
	}

	/**
	 * Implementation based on ints used when shorts are not sufficient. Consumes more memory.
	 * 
	 * @param <T>
	 *            The type of elements to match.
	 * @param left
	 *            The first list
	 * @param right
	 *            The second list
	 * @param matcher
	 *            The matcher
	 * @return (One of) the longest common subsequence.
	 */
	private <T> List<T> intScoreLCS(List<T> left, List<T> right, IMatcher matcher) {
		final int size1 = left.size();
		final int size2 = right.size();

		final int[][] matrix = new int[size1 + 1][size2 + 1];

		// Construct the LCS score matrix
		for (int i = 1; i <= size1; i++) {
			final T first = left.get(i - 1);
			for (int j = 1; j <= size2; j++) {
				// assume array dereferencing and arithmetics faster than equals
				final int current = matrix[i - 1][j - 1];
				final int nextIfNoMatch = Math.max(matrix[i - 1][j], matrix[i][j - 1]);

				if (nextIfNoMatch > current) {
					matrix[i][j] = nextIfNoMatch;
				} else {
					final T second = right.get(j - 1);
					if (matcher.match(first, second)) {
						matrix[i][j] = 1 + matrix[i - 1][j - 1];
					} else {
						matrix[i][j] = nextIfNoMatch;
					}
				}
			}
		}

		// Traceback to create the final LCS
		int current1 = size1;
		int current2 = size2;
		final List<T> result = new ArrayList<T>();

		while (current1 > 0 && current2 > 0) {
			final int currentLength = matrix[current1][current2];
			final int nextLeft = matrix[current1][current2 - 1];
			final int nextUp = matrix[current1 - 1][current2];
			if (currentLength > nextLeft && currentLength > nextUp) {
				result.add(left.get(current1 - 1));
				current1--;
				current2--;
			} else if (nextLeft >= nextUp) {
				current2--;
			} else {
				current1--;
			}
		}

		return reverse(result);
	}
}
