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
 * Longest Common Subsequence interface.
 * 
 * @see <a
 *      href="http://en.wikipedia.org/wiki/Longest_common_subsequence_problem">http://en.wikipedia.org/wiki/Longest_common_subsequence_problem</a>
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface LCS {

	/**
	 * Compute the longest common subsequence of the two given lists.
	 * 
	 * @param <T>
	 *            The type of elements in the list, must be a subtype of E.
	 * @param leftSequence
	 *            The first list (usually called "left")
	 * @param rightSequence
	 *            The second list (usually called "right")
	 * @param matcher
	 *            The matcher to use to compare the given sequences.
	 * @return A new list that contains (one of) the longest common subsequence of the two given lists.
	 */
	<T> List<T> longestCommonSubsequence(List<T> leftSequence, List<T> rightSequence, IMatcher matcher);
}
