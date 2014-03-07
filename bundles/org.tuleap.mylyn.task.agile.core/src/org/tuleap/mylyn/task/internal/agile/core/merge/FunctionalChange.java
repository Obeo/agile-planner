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
 * Represents a functional change: Either an addition, a deletion, or a move from a place in a list to another
 * place in a list (which can be the same or another).
 *
 * @param <T>
 *            The type of elemenst in the lists
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class FunctionalChange<T> {

	/**
	 * The initial state.
	 */
	protected final ItemState<T> from;

	/**
	 * The final state.
	 */
	protected final ItemState<T> to;

	/**
	 * Constructor.
	 *
	 * @param from
	 *            the initial state
	 * @param to
	 *            the final state
	 */
	private FunctionalChange(ItemState<T> from, ItemState<T> to) {
		if (from == null && to == null) {
			throw new IllegalArgumentException("At least one of the states must not be null"); //$NON-NLS-1$
		}
		this.from = from;
		this.to = to;
	}

	/**
	 * Utility method to instantiate a change that represents an addition.
	 *
	 * @param to
	 *            The final state.
	 * @param <U>
	 *            The type of elements in the list
	 * @return A new instance of {@link FunctionalChange} that represents an addition.
	 */
	public static <U> FunctionalChange<U> addition(ItemState<U> to) {
		return new FunctionalChange<U>(null, to);
	}

	/**
	 * Utility method to instantiate a change that represents a deletion.
	 *
	 * @param from
	 *            The initial state.
	 * @param <U>
	 *            The type of elements in the list
	 * @return A new instance of {@link FunctionalChange} that represents a deletion.
	 */
	public static <U> FunctionalChange<U> deletion(ItemState<U> from) {
		return new FunctionalChange<U>(from, null);
	}

	/**
	 * Utility method to instantiate a change that represents a deletion.
	 *
	 * @param from
	 *            The initial state.
	 * @param to
	 *            The final state.
	 * @param <U>
	 *            The type of elements in the list
	 * @return A new instance of {@link FunctionalChange} that represents a move
	 * @throws IllegalArgumentException
	 *             if either of the parameters is null.
	 */
	public static <U> FunctionalChange<U> move(ItemState<U> from, ItemState<U> to)
			throws IllegalArgumentException {
		if (from == null || to == null) {
			throw new IllegalArgumentException("Both states must be non-null to instantiate a move"); //$NON-NLS-1$
		}
		return new FunctionalChange<U>(from, to);
	}

	/**
	 * Initial state getter.
	 *
	 * @return The initial state
	 */
	public ItemState<T> fromState() {
		return from;
	}

	/**
	 * Final state getter.
	 *
	 * @return The final state.
	 */
	public ItemState<T> toState() {
		return to;
	}

	/**
	 * Indicates if this change is a move.
	 *
	 * @return <code>true</code> if and only if the change is a move.
	 */
	public boolean isMove() {
		return from != null && to != null;
	}

	/**
	 * Indicates if this change is an addition.
	 *
	 * @return <code>true</code> if and only if the change is an addition.
	 */
	public boolean isAdd() {
		return from == null && to != null;
	}

	/**
	 * Indicates if this change is a deletion.
	 *
	 * @return <code>true</code> if and only if the change is a deletion.
	 */
	public boolean isDelete() {
		return from != null && to == null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FunctionalChange: " + from + " -> " + to; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
