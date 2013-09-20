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

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;

/**
 * The milestone planning task mapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 1.0
 */
public class MilestonePlanningTaskMapper extends AbstractTaskMapper {

	/**
	 * The cached wrapper.
	 */
	private MilestonePlanningWrapper wrapper;

	/**
	 * The constructor requires a task data.
	 * 
	 * @param taskData
	 *            The task
	 */
	public MilestonePlanningTaskMapper(TaskData taskData) {
		super(taskData);
	}

	/**
	 * Provides a wrapper for the task attribute that will contain all what's related to planning information
	 * in this task data, after creating it if necessary. Not thread-safe.
	 * 
	 * @return the cached wrapper for this task data's milestone planning attribute.
	 */
	public MilestonePlanningWrapper getMilestonePlanningWrapper() {
		if (wrapper == null) {
			wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		}
		return wrapper;
	}
}
