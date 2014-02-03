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

/**
 * Default implementation of {@link IChange}.
 * 
 * @param <E>
 *            The type of elements to match.
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class Change<E> implements IChange<E> {
	/**
	 * The changed object.
	 */
	private final E object;

	/**
	 * The kind of this change.
	 */
	private final Kind kind;

	/**
	 * The left index.
	 */
	private final int leftIndex;

	/**
	 * The right index.
	 */
	private final int rightIndex;

	/**
	 * Constructor.
	 * 
	 * @param object
	 *            The changed object
	 * @param kind
	 *            The kind of the change
	 * @param leftIndex
	 *            The left index
	 * @param rightIndex
	 *            The right index
	 * @throws IllegalArgumentException
	 *             if either object or kind is null.
	 */
	public Change(E object, Kind kind, int leftIndex, int rightIndex) throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException("The modified object cannot be null"); //$NON-NLS-1$
		}
		if (kind == null) {
			throw new IllegalArgumentException("The kind of a change cannot be null"); //$NON-NLS-1$
		}
		this.object = object;
		this.kind = kind;
		this.leftIndex = leftIndex;
		this.rightIndex = rightIndex;
	}

	@Override
	public E getObject() {
		return object;
	}

	@Override
	public Kind getKind() {
		return kind;
	}

	@Override
	public int getLeftIndex() {
		return leftIndex;
	}

	@Override
	public int getRightIndex() {
		return rightIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		boolean equal = false;
		if (obj instanceof Change) {
			final Change<?> other = (Change<?>)obj;
			equal = (this.object == other.object || this.object.equals(other.object))
					&& this.kind == other.kind;
			equal = equal && this.leftIndex == other.leftIndex && this.rightIndex == other.rightIndex;
		}
		return equal;
	}

	@Override
	public int hashCode() {
		// CHECKSTYLE:OFF
		return 31 * (object.hashCode() + kind.hashCode() + leftIndex + rightIndex);
		// CHECKSTYLE:ON
	}

	@Override
	public String toString() {
		return object.toString() + " - " + kind; //$NON-NLS-1$
	}
}
