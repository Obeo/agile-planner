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
 * Set of changes for an item, that rroups at most two local changes and two remote changes. Local changes :
 * at most, removed from a list and added to another, and the same goes for a remote change.
 * 
 * @param <T>
 *            The type of elemenst in the lists
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ItemChangeSet<T> {
	/**
	 * The local move.
	 */
	private ItemChange<T> localMove;

	/**
	 * The local add.
	 */
	private ItemChange<T> localAdd;

	/**
	 * The local deletion.
	 */
	private ItemChange<T> localDelete;

	/**
	 * The remote move.
	 */
	private ItemChange<T> remoteMove;

	/**
	 * The remote add.
	 */
	private ItemChange<T> remoteAdd;

	/**
	 * The remote deletion.
	 */
	private ItemChange<T> remoteDelete;

	/**
	 * Add a local change. Only one local change is permitted.
	 * 
	 * @param change
	 *            The local change
	 * @throws IllegalStateException
	 *             If a local change has already been recorded in this builder.
	 * @throws IllegalArgumentException
	 *             If a local change is null or has a null IChange.
	 * @return this for a fluent API.
	 */
	public ItemChangeSet<T> addLocalChange(ItemChange<T> change) throws IllegalStateException,
			IllegalArgumentException {
		if (change == null) {
			throw new IllegalArgumentException("Adding a null change is forbidden"); //$NON-NLS-1$
		}
		if (localMove != null) {
			throw new IllegalStateException("A move already exists for this item"); //$NON-NLS-1$
		}
		switch (change.getChange().getKind()) {
			case MOVE:
				if (localAdd != null || localDelete != null) {
					throw new IllegalStateException("An addition or deletion already exists for this item"); //$NON-NLS-1$
				}
				localMove = change;
				break;
			case ADD:
				if (localAdd != null) {
					throw new IllegalStateException("An addition already exists for this item"); //$NON-NLS-1$
				}
				localAdd = change;
				break;
			case DELETE:
				if (localDelete != null) {
					throw new IllegalStateException("A deletion already exists for this item"); //$NON-NLS-1$
				}
				localDelete = change;
				break;
			default:
				throw new IllegalArgumentException("The given change must describe a change"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * Add a remote change. Only one remote change is permitted.
	 * 
	 * @param change
	 *            The local change
	 * @throws IllegalStateException
	 *             If a remote change has already been recorded in this builder.
	 * @throws IllegalArgumentException
	 *             If a local change has a null IChange.
	 * @return this for a fluent API.
	 */
	public ItemChangeSet<T> addRemoteChange(ItemChange<T> change) throws IllegalStateException,
			IllegalArgumentException {
		if (change == null) {
			throw new IllegalArgumentException("Adding a null change is forbidden"); //$NON-NLS-1$
		}
		if (remoteMove != null) {
			throw new IllegalStateException("A move already exists for this item"); //$NON-NLS-1$
		}
		switch (change.getChange().getKind()) {
			case MOVE:
				if (remoteAdd != null || remoteDelete != null) {
					throw new IllegalStateException("An addition or deletion already exists for this item"); //$NON-NLS-1$
				}
				remoteMove = change;
				break;
			case ADD:
				if (remoteAdd != null) {
					throw new IllegalStateException("An addition already exists for this item"); //$NON-NLS-1$
				}
				remoteAdd = change;
				break;
			case DELETE:
				if (remoteDelete != null) {
					throw new IllegalStateException("An deletion already exists for this item"); //$NON-NLS-1$
				}
				remoteDelete = change;
				break;
			default:
				throw new IllegalArgumentException("The given change must describe a change"); //$NON-NLS-1$
		}
		return this;
	}

	/**
	 * Indicates whether this change makes sense and can be treated.
	 * <ul>
	 * <li>For a local ADD without a local DELETE, there cannot be a remote MOVE or DELETE</li>
	 * <li>For a remote ADD without a remote DELETE, there cannot be a local MOVE or DELETE</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if and only if the change is valid according to the rules above.
	 */
	public boolean isValid() {
		boolean result = true;
		if (localAdd != null && localDelete == null) {
			result = remoteMove == null && remoteDelete == null;
		}
		if (result && remoteAdd != null && remoteDelete == null) {
			result = localMove == null && localDelete == null;
		}
		return result;
	}

	/**
	 * Converts this change set to a functional form.
	 * 
	 * @return a new instance of {@link FunctionalChangeSet};
	 */
	public FunctionalChangeSet<T> toFunctionalChangeSet() {
		if (!isValid()) {
			throw new IllegalStateException("This change set is invalid."); //$NON-NLS-1$
		}
		return new FunctionalChangeSet<T>(getLocalChange(), getRemoteChange());
	}

	/**
	 * Computes the local change, assuming this ChangeSet is valid, Which must have been checked before.
	 * 
	 * @return The local functional change, which can only be a move.
	 */
	private FunctionalChange<T> getRemoteChange() {
		FunctionalChange<T> result = null;
		if (remoteAdd != null) {
			if (remoteDelete != null) {
				result = FunctionalChange.move(remoteDelete.initialState(), remoteAdd.finalState());
			} else {
				result = FunctionalChange.addition(remoteAdd.finalState());
			}
		} else if (remoteDelete != null) {
			result = FunctionalChange.deletion(remoteDelete.initialState());
		} else if (remoteMove != null) {
			result = FunctionalChange.move(remoteMove.initialState(), remoteMove.finalState());
		}
		return result;
	}

	/**
	 * Computes the local change, assuming this ChangeSet is valid, Which must have been checked before.
	 * 
	 * @return The local functional change, which can only be a move.
	 */
	private FunctionalChange<T> getLocalChange() {
		FunctionalChange<T> result = null;
		if (localAdd != null) {
			if (localDelete != null) {
				result = FunctionalChange.move(localDelete.initialState(), localAdd.finalState());
			} else {
				result = FunctionalChange.addition(localAdd.finalState());
			}
		} else if (localDelete != null) {
			result = FunctionalChange.deletion(localDelete.initialState());
		} else if (localMove != null) {
			result = FunctionalChange.move(localMove.initialState(), localMove.finalState());
		}
		return result;
	}
}
