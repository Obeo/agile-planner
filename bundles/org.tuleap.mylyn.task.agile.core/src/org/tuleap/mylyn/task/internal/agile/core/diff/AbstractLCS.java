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

import java.util.List;

/**
 * Abstract super-class of LCS implementations to provide basic support.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractLCS implements LCS {
	/**
	 * Returns the length of the common prefix, if any, of the two given sequences.
	 * 
	 * @param <E>
	 *            Type of elements to match.
	 * @param sequence1
	 *            First of the two sequences to consider.
	 * @param sequence2
	 *            Second of the two sequences to consider.
	 * @param matcher
	 *            Tells us how to compare elements of the two lists together.
	 * @return The length of the common prefix for these two lists. <code>0</code> if none.
	 */
	public <E> int commonPrefixLength(List<? extends E> sequence1, List<? extends E> sequence2,
			IMatcher matcher) {
		final int size1 = sequence1.size();
		final int size2 = sequence2.size();

		int cursor = 0;
		boolean matching = true;
		while (cursor < size1 && cursor < size2 && matching) {
			final E first = sequence1.get(cursor);
			final E second = sequence2.get(cursor);
			if (matcher.match(first, second)) {
				cursor++;
			} else {
				// break
				matching = false;
			}
		}

		return cursor;
	}

	/**
	 * Returns the length of the common suffix, if any, of the two given sequences.
	 * 
	 * @param <E>
	 *            Type of elements to match.
	 * @param sequence1
	 *            First of the two sequences to consider.
	 * @param sequence2
	 *            Second of the two sequences to consider.
	 * @param matcher
	 *            Tells us how to compare elements of the two lists together.
	 * @return The length of the common suffix for these two lists. <code>0</code> if none.
	 */
	public <E> int commonSuffixLength(List<? extends E> sequence1, List<? extends E> sequence2,
			IMatcher matcher) {
		int cursor1 = sequence1.size() - 1;
		int cursor2 = sequence2.size() - 1;
		int length = 0;
		boolean matching = true;
		while (cursor1 >= 0 && cursor2 >= 0 && matching) {
			final E first = sequence1.get(cursor1);
			final E second = sequence2.get(cursor2);
			if (matcher.match(first, second)) {
				length++;
				cursor1--;
				cursor2--;
			} else {
				// break
				matching = false;
			}
		}

		return length;
	}
}
