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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;

/**
 * Records the {@link TaskAttribute}s modified by a merge, and can notify in batch the {@link TaskDataModel}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
class ChangeRecorder {

	/**
	 * The local set of changed attributes.
	 */
	private final Set<TaskAttribute> changedAttributes = new HashSet<TaskAttribute>();

	/**
	 * Record an attribute change.
	 * 
	 * @param att
	 *            the attribute
	 */
	public void attributeChanged(TaskAttribute att) {
		changedAttributes.add(att);
	}

	/**
	 * Record all the changes in the model. The changed attributes are cleared by this operation.
	 * 
	 * @param model
	 *            The TaskDataModel to which modifications must be applied.
	 */
	public void recordChanges(TaskDataModel model) {
		// TODO See if the TDM whould be an instance variable
		for (TaskAttribute att : changedAttributes) {
			model.attributeChanged(att);
		}
		changedAttributes.clear();
	}
}
