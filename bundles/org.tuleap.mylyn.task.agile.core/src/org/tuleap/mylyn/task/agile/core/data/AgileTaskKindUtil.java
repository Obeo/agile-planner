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

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * Utility class used to set and retrieve the kind of a task.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public final class AgileTaskKindUtil {
	/**
	 * The value used for a non-agile artifact.
	 */
	public static final String TASK_KIND_ARTIFACT = "artifact"; //$NON-NLS-1$

	/**
	 * The value used for a milestone.
	 */
	public static final String TASK_KIND_MILESTONE = "milestone"; //$NON-NLS-1$

	/**
	 * The value used for a backlog item.
	 */
	public static final String TASK_KIND_BACKLOG_ITEM = "backlog_item"; //$NON-NLS-1$

	/**
	 * The value used for a top planning.
	 */
	public static final String TASK_KIND_TOP_PLANNING = "top_planning"; //$NON-NLS-1$

	/**
	 * The key used to indicate the kind of a mylyn task data.
	 */
	public static final String TASK_KIND_KEY = "mta_kind"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	private AgileTaskKindUtil() {
		// prevent instantiation
	}

	/**
	 * Sets the kind of the task.
	 * 
	 * @param taskData
	 *            The task data
	 * @param agileTaskKind
	 *            The kind of the task
	 * @return The task data with its kind set
	 */
	public static TaskData setAgileTaskKind(TaskData taskData, String agileTaskKind) {
		TaskAttribute mappedAttribute = taskData.getRoot().getMappedAttribute(TASK_KIND_KEY);
		if (mappedAttribute == null) {
			mappedAttribute = taskData.getRoot().createMappedAttribute(TASK_KIND_KEY);
		}
		mappedAttribute.setValue(agileTaskKind);
		return taskData;
	}

	/**
	 * Gets the kind of a task.
	 * 
	 * @param taskData
	 *            The task data
	 * @return The kind of the task
	 */
	public static String getAgileTaskKind(TaskData taskData) {
		TaskAttribute mappedAttribute = taskData.getRoot().getMappedAttribute(TASK_KIND_KEY);
		if (mappedAttribute != null) {
			return mappedAttribute.getValue();
		}
		return null;
	}

}
