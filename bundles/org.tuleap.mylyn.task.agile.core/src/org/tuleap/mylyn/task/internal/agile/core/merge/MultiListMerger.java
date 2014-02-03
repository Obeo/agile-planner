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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;

/**
 * Merger of maps of lists between which items can be moved, added, or deleted.
 * 
 * @param <T>
 *            The type of elements in the lists.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MultiListMerger<T> {

	/**
	 * Merge the given lists.
	 * 
	 * @param lists
	 *            The 3-way lists
	 * @return A Map of all the lists indexed by their ID after the merge.
	 */
	public Map<String, List<T>> merge(Iterable<? extends IThreeWayList<T>> lists) {
		MultiListDiff<T> mld = MultiListDiff.build(lists);
		Map<T, FunctionalChangeSet<T>> changes = mld.getFunctionalChangeSets();
		Map<String, List<T>> itemListsById = new LinkedHashMap<String, List<T>>();
		for (IThreeWayList<T> twl : lists) {
			itemListsById.put(twl.getListId(), Lists.newArrayList(twl.getCommonAncestor()));
		}

		ArrayListMultimap<String, ItemLocalChange<T>> changesByListId = buildChanges(changes);
		// Now we can manage the insertions, after sorting them to perform them from lower to upper index
		return performChanges(itemListsById, changesByListId);
	}

	/**
	 * Performs the given insertions in the right order.
	 * 
	 * @param itemListsById
	 *            The map of lists by list ID (i.e. milestone ID)
	 * @param changesByListId
	 *            The additions to perform.
	 * @return The map of modified lists, the same as the given one, for fluency.
	 */
	private Map<String, List<T>> performChanges(Map<String, List<T>> itemListsById,
			ArrayListMultimap<String, ItemLocalChange<T>> changesByListId) {
		for (String listId : changesByListId.keySet()) {
			List<ItemLocalChange<T>> changesToMake = changesByListId.get(listId);
			Collections.sort(changesToMake);
			List<T> itemList = itemListsById.get(listId);
			int localOffset = 0;
			int remoteOffset = 0;
			int removeOffset = 0;
			for (ItemLocalChange<T> change : changesToMake) {
				int offset = change.index;
				if (change.remote) {
					localOffset += change.offset();
				} else {
					remoteOffset += change.offset();
				}
				if (change.remove) {
					T removedId = itemList.remove(change.index + removeOffset);
					Assert.isTrue(change.itemId.equals(removedId));
				} else {
					if (change.remote) {
						offset += remoteOffset;
					} else {
						offset += localOffset;
					}
					itemList.add(offset, change.itemId);
					// if (offset >= itemList.size()) {
					// offset = itemList.size();
					// } else if (offset < 0) {
					// offset = 0;
					// }
				}
				removeOffset += change.offset();
			}
		}
		return itemListsById;
	}

	/**
	 * Execute the first part of the merge.
	 * 
	 * @param changes
	 *            The changes to apply
	 * @return An {@link ArrayListMultimap} of the additions that need to be performed after this method has
	 *         run.
	 */
	private ArrayListMultimap<String, ItemLocalChange<T>> buildChanges(Map<T, FunctionalChangeSet<T>> changes) {
		ArrayListMultimap<String, ItemLocalChange<T>> changesByListId = ArrayListMultimap.create();
		for (Entry<T, FunctionalChangeSet<T>> entry : changes.entrySet()) {
			T item = entry.getKey();
			FunctionalChangeSet<T> fcs = entry.getValue();
			FunctionalChange<T> remoteChange = fcs.getRemoteChange();
			FunctionalChange<T> localChange = fcs.getLocalChange();
			if (remoteChange == null) {
				// The change can only be a local move
				ItemState<T> initialState = localChange.fromState();
				ItemState<T> finalState = localChange.toState();
				changesByListId.put(initialState.getListId(), new ItemLocalChange<T>(initialState.getIndex(),
						item, false, true));
				changesByListId.put(finalState.getListId(), new ItemLocalChange<T>(finalState.getIndex(),
						item, false, false));
			} else if (remoteChange.isAdd()) {
				ItemState<T> finalState = remoteChange.toState();
				String targetMilestoneId = finalState.getListId();
				changesByListId.put(targetMilestoneId, new ItemLocalChange<T>(finalState.getIndex(), item,
						true, false));
			} else if (remoteChange.isDelete()) {
				ItemState<T> initialState = remoteChange.fromState();
				String targetMilestoneId = initialState.getListId();
				changesByListId.put(targetMilestoneId, new ItemLocalChange<T>(initialState.getIndex(), item,
						true, true));
			} else {
				// Remote move
				if (localChange == null) {
					ItemState<T> initialState = remoteChange.fromState();
					ItemState<T> finalState = remoteChange.toState();
					changesByListId.put(initialState.getListId(), new ItemLocalChange<T>(initialState
							.getIndex(), item, true, true));
					changesByListId.put(finalState.getListId(), new ItemLocalChange<T>(finalState.getIndex(),
							item, true, false));
				} else {
					// Conflict : moved both locally and remotely => We keep the local move
					ItemState<T> initialState = localChange.fromState();
					ItemState<T> finalState = localChange.toState();
					changesByListId.put(initialState.getListId(), new ItemLocalChange<T>(initialState
							.getIndex(), item, true, true));
					changesByListId.put(finalState.getListId(), new ItemLocalChange<T>(finalState.getIndex(),
							item, false, false));
				}
			}
		}
		return changesByListId;
	}

	/**
	 * Represents the insertion of an item at a given index. Note: this class has a natural ordering that is
	 * inconsistent with equals.
	 * 
	 * @param <T>
	 *            The type of elements in the lists
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class ItemLocalChange<T> implements Comparable<ItemLocalChange<T>> {
		/**
		 * The insertion index.
		 */
		private final int index;

		/**
		 * Whether this is a local or remote change.
		 */
		private final boolean remote;

		/**
		 * The item itemId.
		 */
		private final T itemId;

		/**
		 * WHether it's a remove (true) or an add (false).
		 */
		private final boolean remove;

		/**
		 * Constructor.
		 * 
		 * @param index
		 *            The index
		 * @param itemId
		 *            The item itemId
		 * @param remote
		 *            Whether the change is remote (<code>true</code>) or local (<code>false</code>)
		 * @param remove
		 *            Whether the change is a remove or an add
		 */
		private ItemLocalChange(int index, T itemId, boolean remote, boolean remove) {
			if (itemId == null) {
				throw new IllegalArgumentException("The ID of an item cannot be null."); //$NON-NLS-1$
			}
			this.index = index;
			this.itemId = itemId;
			this.remote = remote;
			this.remove = remove;
		}

		/**
		 * Computes the offset to apply to global offset of the other kind of changes.
		 * 
		 * @return the offset to apply to global offset of the other kind of changes.
		 */
		public int offset() {
			if (remove) {
				return -1;
			}
			return 1;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ItemLocalChange<T> o) {
			int result = index - o.index;
			if (result == 0) {
				if (remove && !o.remove) {
					result = 1;
				} else if (!remove && o.remove) {
					result = -1;
				} else {
					if (remote && !o.remote) {
						result = 1;
					} else if (!remote && o.remote) {
						result = -1;
					}
				}
			}
			return result;
		}

	}
}
