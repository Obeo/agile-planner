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
package org.tuleap.mylyn.task.agile.core.data;

import com.google.common.base.Predicate;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Utility class providing useful {@link TaskAttribute} predicates.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class TaskAttributes {

	/**
	 * Private constructor.
	 */
	private TaskAttributes() {
		//
	}

	/**
	 * Provides a predicate that passes attributes whose ID starts with the given prefix.
	 * 
	 * @param prefix
	 *            The prefix to check, must not be null.
	 * @return A predicate that returns <code>true</code> for attributes whose ID starts with the given
	 *         prefix.
	 */
	public static Predicate<TaskAttribute> prefixedBy(final String prefix) {
		Assert.isNotNull(prefix);
		return new Predicate<TaskAttribute>() {
			@Override
			public boolean apply(TaskAttribute arg0) {
				return arg0.getId().startsWith(prefix);
			}
		};
	}
}
