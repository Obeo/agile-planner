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

import org.tuleap.mylyn.task.internal.agile.core.diff.IChange;
import org.tuleap.mylyn.task.internal.agile.core.diff.IChange.Kind;

/**
 * Represents the modification of a backlog item.
 * 
 * @param <T>
 *            The type of elemenst in the lists
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ItemChange<T> {
	/**
	 * The milestone id of the change (null for items in the backlog).
	 */
	private final String listId;

	/**
	 * The change applied to the item.
	 */
	private final IChange<T> change;

	/**
	 * Constructor.
	 * 
	 * @param listId
	 *            The id of the list the change must apply to
	 * @param change
	 *            The changed applied to the item
	 * @throws IllegalArgumentException
	 *             If a local change has a null IChange.
	 */
	public ItemChange(String listId, IChange<T> change) throws IllegalArgumentException {
		if (change == null) {
			throw new IllegalArgumentException("The given change must not be null"); //$NON-NLS-1$
		}
		this.listId = listId;
		this.change = change;
	}

	/**
	 * List ID getter.
	 * 
	 * @return the list ID
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * Change getter.
	 * 
	 * @return the change
	 */
	public IChange<T> getChange() {
		return change;
	}

	/**
	 * Provides a representation of the initial state of this change.
	 * 
	 * @return A representation of the initial state of this change, or null if the change is an addition.
	 */
	public ItemState initialState() {
		ItemState result = null;
		if (change.getKind() != Kind.ADD) {
			result = new ItemState(listId, change.getRightIndex());
		}
		return result;
	}

	/**
	 * Provides a representation of the final state of this change.
	 * 
	 * @return A representation of the final state of this change, or null if the change is a deletion.
	 */
	public ItemState finalState() {
		ItemState result = null;
		if (change.getKind() != Kind.DELETE) {
			result = new ItemState(listId, change.getLeftIndex());
		}
		return result;
	}
}
