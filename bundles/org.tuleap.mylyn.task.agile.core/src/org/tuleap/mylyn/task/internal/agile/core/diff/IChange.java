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
 * This interface represents a modification.
 * 
 * @param <E>
 *            The type of the element that has been modified.
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IChange<E> {

	/**
	 * Provides access to the element that has changed.
	 * 
	 * @return The element that has changed.
	 */
	E getObject();

	/**
	 * Provides the kind of change the wrapped element has been submitted to.
	 * 
	 * @return The kind of modification that was applied to the wrapped element.
	 */
	Kind getKind();

	/**
	 * The position of the wrapped element in the first list.
	 * 
	 * @return The position of the wrapped element in the "left" list.
	 */
	int getLeftIndex();

	/**
	 * The position of the wrapped element in the second list.
	 * 
	 * @return The position of the wrapped element in the "right" list.
	 */
	int getRightIndex();

	/**
	 * This enumeration lists the known kinds of modifications that can be made to an element.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	enum Kind {
		/**
		 * An addition: the element has been added to the list.
		 */
		ADD,

		/**
		 * A deletion: the element has been removed from the list.
		 */
		DELETE,

		/**
		 * A move: the element has been moved in the list.
		 */
		MOVE;
	}
}
