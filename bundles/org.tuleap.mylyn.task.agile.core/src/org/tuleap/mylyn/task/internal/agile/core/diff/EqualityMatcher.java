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
 * Implementation of {@link IMatcher} that uses equality to match elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class EqualityMatcher implements IMatcher {

	/**
	 * {@inheritDoc} This implementation uses Object.equals() to match elements, which means that the given
	 * elements match if they are the same, or if they are equal according to their equals() method.
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.diff.IMatcher#match(java.lang.Object, java.lang.Object)
	 */
	@Override
	public <E> boolean match(E obj1, E obj2) {
		return obj1 == obj2 || obj1 != null && obj1.equals(obj2);
	}
}
