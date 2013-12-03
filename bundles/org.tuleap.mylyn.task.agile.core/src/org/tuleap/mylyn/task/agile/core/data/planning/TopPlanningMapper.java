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

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;
import org.tuleap.mylyn.task.internal.agile.core.util.MylynAgileCoreMessages;

/**
 * Mapper used to populate top planning task data.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TopPlanningMapper extends AbstractTaskMapper {

	/**
	 * Constructor.
	 * 
	 * @param taskData
	 *            The Task data.
	 */
	public TopPlanningMapper(TaskData taskData) {
		super(taskData);
	}

	/**
	 * Initialize an empty task data from the given tracker. The task kind is set to a relevant default value.
	 */
	public void initializeEmptyTaskData() {
		TaskAttribute attribute = getWriteableAttribute(TaskAttribute.TASK_KEY, TaskAttribute.TYPE_SHORT_TEXT);
		attribute.getMetaData().setReadOnly(true);

		attribute = getWriteableAttribute(TaskAttribute.TASK_KIND, TaskAttribute.TYPE_SHORT_TEXT);
		attribute.getMetaData().setReadOnly(true);
		attribute.setValue(MylynAgileCoreMessages.getString("TopPlanningMapper.GeneralPlanningOf")); //$NON-NLS-1$

		attribute = getWriteableAttribute(TaskAttribute.SUMMARY, TaskAttribute.TYPE_SHORT_RICH_TEXT);
		attribute.getMetaData().setReadOnly(true);
		attribute.setValue(MylynAgileCoreMessages.getString("TopPlanningMapper.GeneralPlanning")); //$NON-NLS-1$
	}

	/**
	 * Set the task key (using {@link TaskAttribute}.TASK_KEY).
	 * 
	 * @param taskKey
	 *            The Task key.
	 */
	public void setTaskKey(String taskKey) {
		this.getMappedAttribute(TaskAttribute.TASK_KEY).setValue(taskKey);
	}

	/**
	 * Set the task kind (using {@link TaskAttribute}.TASK_KIND). This is set by default but the default value
	 * can be changed if needed.
	 * 
	 * @param taskKind
	 *            The Task kind.
	 */
	public void setTaskKind(String taskKind) {
		this.getMappedAttribute(TaskAttribute.TASK_KIND).setValue(taskKind);
	}
}
