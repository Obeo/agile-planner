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

/**
 * The state of an item.
 *
 * @param <T>
 *            The type of elemenst in the lists
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ItemState<T> {
	/**
	 * The milestone ID.
	 */
	private final String listId;

	/**
	 * The item index.
	 */
	private final int index;

	/**
	 * Constructor.
	 *
	 * @param listId
	 *            the ID of the list containing the element this state describes
	 * @param index
	 *            The index
	 */
	public ItemState(String listId, int index) {
		super();
		this.listId = listId;
		this.index = index;
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
	 * Index getter.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(listId) + '[' + index + ']';
	}
}
