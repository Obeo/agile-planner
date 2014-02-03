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

import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.internal.agile.core.diff.IChange.Kind;

/**
 * Diff engine that uses LCS to compute differences between lists.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class Diff {

	/**
	 * The matcher.
	 */
	private final IMatcher matcher;

	/**
	 * The LCS implementation.
	 */
	private final LCS lcs;

	/**
	 * Constructor.
	 * 
	 * @param matcher
	 *            The matcher to use
	 * @param lcs
	 *            The LCS to use.
	 * @throws IllegalArgumentException
	 *             if either matcher or lcs is null.
	 */
	public Diff(IMatcher matcher, LCS lcs) throws IllegalArgumentException {
		if (matcher == null) {
			throw new IllegalArgumentException("A diff engine needs a non-null matcher."); //$NON-NLS-1$
		}
		if (lcs == null) {
			throw new IllegalArgumentException("A diff engine needs a non-null LCS implementation."); //$NON-NLS-1$
		}
		this.matcher = matcher;
		this.lcs = lcs;
	}

	/**
	 * Computes the differences between the given lists, taking moves into account. This is equivalent to
	 * calling <code>computeDifferences(left, right, true)</code>.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param left
	 *            The first list
	 * @param right
	 *            The second list
	 * @return A list of the changes between the two lists.
	 */
	public <E> List<IChange<E>> computeDifferences(List<E> left, List<E> right) {
		return computeDifferences(left, right, true);
	}

	/**
	 * Computes the differences between the given lists, taking moves into account.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param left
	 *            The first list
	 * @param right
	 *            The second list
	 * @param considerMoves
	 *            Whether or not to take moves into account
	 * @return A list of the changes between the two lists.
	 */
	public <E> List<IChange<E>> computeDifferences(List<E> left, List<E> right, boolean considerMoves) {
		final List<E> common = lcs.longestCommonSubsequence(left, right, matcher);
		final List<IndexedObject<E>> changed1 = computeChanged(left, common);
		final List<IndexedObject<E>> changed2 = computeChanged(right, common);

		final List<IChange<E>> changes = new ArrayList<IChange<E>>();

		final int size1 = changed1.size();
		for (int i = 0; i < size1; i++) {
			final IndexedObject<E> changed = changed1.get(i);
			final E o = changed.object;
			if (considerMoves) {
				final IndexedObject<E> match = removeMatch(changed2, o);
				if (match == null) {
					changes.add(createChange(o, Kind.ADD, changed.index, -1));
				} else {
					changes.add(createChange(o, Kind.MOVE, changed.index, match.index));
				}
			} else {
				changes.add(createChange(o, Kind.ADD, changed.index, -1));
			}
		}

		final int size2 = changed2.size();
		for (int i = 0; i < size2; i++) {
			final IndexedObject<E> changed = changed2.get(i);
			changes.add(createChange(changed.object, Kind.DELETE, -1, changed.index));
		}

		return changes;
	}

	/**
	 * Create a change.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param changed
	 *            the changed object
	 * @param kind
	 *            the kind
	 * @param leftIndex
	 *            first index
	 * @param rightIndex
	 *            second index
	 * @return A new instance of change.
	 */
	private <E> IChange<E> createChange(E changed, Kind kind, int leftIndex, int rightIndex) {
		return new Change<E>(changed, kind, leftIndex, rightIndex);
	}

	/**
	 * Compute the changes between the given list and the given lcs.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param list
	 *            The list
	 * @param subsequence
	 *            The subsequence, which means that all elements of the subsequence belong to the list
	 * @return The list of changed elements.
	 */
	private <E> List<IndexedObject<E>> computeChanged(List<E> list, List<E> subsequence) {
		final int size = list.size();
		int lcsCursor = 0;
		E nextLCS = getIfPresent(subsequence, lcsCursor);
		final List<IndexedObject<E>> changed = new ArrayList<IndexedObject<E>>();
		for (int i = 0; i < size; i++) {
			final E diffCandidate = list.get(i);
			if (matcher.match(diffCandidate, nextLCS)) {
				lcsCursor++;
				nextLCS = getIfPresent(subsequence, lcsCursor);
			} else {
				changed.add(new IndexedObject<E>(diffCandidate, i));
			}
		}
		return changed;
	}

	/**
	 * Get the element in the list if the index has a relevant value, otherwise return <code>null</code>.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param list
	 *            The list
	 * @param index
	 *            The index
	 * @return The element if there is one, or null.
	 */
	private <E> E getIfPresent(List<E> list, int index) {
		if (list.size() > index) {
			return list.get(index);
		}
		return null;
	}

	/**
	 * Remove a match.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @param list
	 *            The list
	 * @param object
	 *            The object
	 * @return The removed IndexedObject, or null.
	 */
	private <E> IndexedObject<E> removeMatch(List<IndexedObject<E>> list, E object) {
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			final IndexedObject<E> indexObject = list.get(i);
			if (matcher.match(indexObject.object, object)) {
				list.remove(i);
				return indexObject;
			}
		}
		return null;
	}

	/**
	 * Indexed Object, i.e. object with a position in a list.
	 * 
	 * @param <E>
	 *            The type of elements in the lists to compare.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class IndexedObject<E> {
		/**
		 * The object.
		 */
		private final E object;

		/**
		 * The index of the object.
		 */
		private final int index;

		/**
		 * Constructor.
		 * 
		 * @param object
		 *            The object
		 * @param index
		 *            The index
		 */
		public IndexedObject(E object, int index) {
			this.object = object;
			this.index = index;
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || obj instanceof IndexedObject<?>
					&& this.object.equals(((IndexedObject<?>)obj).object);
		}

		@Override
		public int hashCode() {
			return object.hashCode();
		}
	}
}
