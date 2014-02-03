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
package org.tuleap.mylyn.task.internal.agile.core.merge;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tuleap.mylyn.task.internal.agile.core.diff.ClassicLCS;
import org.tuleap.mylyn.task.internal.agile.core.diff.Diff;
import org.tuleap.mylyn.task.internal.agile.core.diff.EqualityMatcher;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange;

/**
 * Diff engine for a set of lists between which items can be moved. Computes the differences between the
 * local, the remote, and the common ancestor versions of a milestone planning. This engine requires the use
 * of a matcher that uses equality.
 * 
 * @param <T>
 *            the type of elements in the lists
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class MultiListDiff<T> {

	/**
	 * Cache of Change sets used to record all the changes performed on backlog items.
	 */
	private final Map<T, ItemChangeSet<T>> changeSetsByItemId = new LinkedHashMap<T, ItemChangeSet<T>>();

	/**
	 * Private constructor.
	 */
	private MultiListDiff() {
		// Force usage of build to instantiate
	}

	/**
	 * Utility method to instantiate a Planning Diff from a list of three-way lists.
	 * 
	 * @param allLists
	 *            The lists of items to compare.
	 * @param <U>
	 *            The type of elements in the lists.
	 * @return An new instance of {@link MultiListDiff}, initialized.
	 */
	public static <U> MultiListDiff<U> build(Iterable<? extends IThreeWayList<U>> allLists) {
		MultiListDiff<U> result = new MultiListDiff<U>();
		for (IThreeWayList<U> twl : allLists) {
			result.indexChanges(twl);
		}
		return result;
	}

	/**
	 * Provides the functional changes that have been performed on the lists used to build this diff.
	 * 
	 * @return A Map of the functional changes that have been performed, with one entry per modified item.
	 */
	public Map<T, FunctionalChangeSet<T>> getFunctionalChangeSets() {
		Map<T, FunctionalChangeSet<T>> result = new LinkedHashMap<T, FunctionalChangeSet<T>>();
		for (Entry<T, ItemChangeSet<T>> entry : changeSetsByItemId.entrySet()) {
			result.put(entry.getKey(), entry.getValue().toFunctionalChangeSet());
		}
		return result;
	}

	/**
	 * Index the changes in the given three-way list.
	 * 
	 * @param twl
	 *            The states of a list to index.
	 */
	private void indexChanges(IThreeWayList<T> twl) {
		Diff diff = new Diff(new EqualityMatcher(), new ClassicLCS());
		List<T> ancestor = twl.getCommonAncestor();
		List<IChange<T>> localDiffs = diff.computeDifferences(twl.getLocal(), ancestor);
		List<IChange<T>> repoDiffs = diff.computeDifferences(twl.getRemote(), ancestor);

		for (IChange<T> localChange : localDiffs) {
			T itemId = localChange.getObject();
			getChangeSet(itemId).addLocalChange(new ItemChange<T>(twl.getListId(), localChange));
		}
		for (IChange<T> repoChange : repoDiffs) {
			T itemId = repoChange.getObject();
			getChangeSet(itemId).addRemoteChange(new ItemChange<T>(twl.getListId(), repoChange));
		}
	}

	/**
	 * Get the builder to use for the given item ID.
	 * 
	 * @param itemId
	 *            The item ID
	 * @return The builder to use, which is created and cached if necessary.
	 */
	private ItemChangeSet<T> getChangeSet(T itemId) {
		ItemChangeSet<T> result;
		if (changeSetsByItemId.containsKey(itemId)) {
			result = changeSetsByItemId.get(itemId);
		} else {
			result = new ItemChangeSet<T>();
			changeSetsByItemId.put(itemId, result);
		}
		return result;
	}
}
