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
import com.google.common.collect.ImmutableList;

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
	 * @return A predicate that will accept attributes whose ID starts with one of the given prefixes.
	 */
	public static Predicate<TaskAttribute> prefixedBy(final String... prefix) {
		Assert.isNotNull(prefix);
		return new Predicate<TaskAttribute>() {
			@Override
			public boolean apply(TaskAttribute att) {
				boolean res = false;
				for (int i = 0; i < prefix.length && !res; i++) {
					String p = prefix[i];
					if (p != null) {
						res = att.getId().startsWith(p);
					}
				}
				return res;
			}
		};
	}

	/**
	 * Provides a predicate that passes attributes whose ID is in the given list.
	 * 
	 * @param prefix
	 *            The list of prefixes
	 * @return A predicate which will accept attributes whose ID is one of the given arguments.
	 */
	public static Predicate<TaskAttribute> identifiedBy(final String... prefix) {
		Assert.isNotNull(prefix);
		final ImmutableList<String> prefixes = ImmutableList.copyOf(prefix);
		return new Predicate<TaskAttribute>() {
			@Override
			public boolean apply(TaskAttribute att) {
				return prefixes.contains(att.getId());
			}
		};
	}
}
