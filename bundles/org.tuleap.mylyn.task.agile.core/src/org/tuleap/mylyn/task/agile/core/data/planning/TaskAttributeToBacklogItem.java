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
package org.tuleap.mylyn.task.agile.core.data.planning;

import com.google.common.base.Function;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Guava function to transform a TaskAttribute to a SubMilestoneWrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class TaskAttributeToBacklogItem implements Function<TaskAttribute, BacklogItemWrapper> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public BacklogItemWrapper apply(TaskAttribute att) {
		return new BacklogItemWrapper(att);
	}
}
