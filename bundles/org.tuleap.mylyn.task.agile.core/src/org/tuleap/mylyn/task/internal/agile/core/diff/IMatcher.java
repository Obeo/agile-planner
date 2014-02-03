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
 * Matcher interface. A matcher will indicate whether two elements match, where "match" means that these two
 * elements seem to represent the same thing.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IMatcher {

	/**
	 * Indicates whether the two given elements match, according to the definition of "match" the current
	 * implementation of {@link IMatcher} uses.
	 * 
	 * @param <E>
	 *            The type of elements to match.
	 * @param obj1
	 *            The first element
	 * @param obj2
	 *            The second element
	 * @return <code>true</code> if and only if the two elements match.
	 */
	<E> boolean match(E obj1, E obj2);
}
