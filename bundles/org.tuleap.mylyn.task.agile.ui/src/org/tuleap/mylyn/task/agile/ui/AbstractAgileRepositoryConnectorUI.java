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
package org.tuleap.mylyn.task.agile.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;

/**
 * UI Connector to an Agile repository.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 1.0
 */
public abstract class AbstractAgileRepositoryConnectorUI {

	/**
	 * Provides the type of connector supported by this connector.
	 * 
	 * @return The type of connector supported.
	 */
	public abstract String getConnectorKind();

	/**
	 * Provides the mapping for milestone creation. A milestone is created within a parent planning. This
	 * mapping will be provided to the {@link org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler} of
	 * the connector in order to create the submilestone.
	 * 
	 * @param planningTaskData
	 *            The task data of the parent planning where the milestone is to be created.
	 * @param parentMilestoneId
	 *            The identifier of the parent milestone
	 * @param taskRepository
	 *            The task repository of the planning that should also host the new milestone.
	 * @param monitor
	 *            The progress monitor to use for monitoring progress...
	 * @return The wizard to create the task that represents the milestone.
	 */
	public abstract IMilestoneMapping getNewMilestoneMapping(TaskData planningTaskData,
			String parentMilestoneId, TaskRepository taskRepository, IProgressMonitor monitor);

	/**
	 * Indicates the list of the identifier of the task editor page factories that are in conflict with the
	 * task editor page factory identifier provided. Every task editor page factory of the Agile UI bundle
	 * will call this method before initialization. As a result, one can say that for a specific use case, the
	 * planning page factory or the context page factory is in conflict with the editor.
	 * 
	 * @param taskEditorPageFactoryId
	 *            The identifier of the task editor page factory that is being used
	 * @param taskData
	 *            The task data of the task that the editor tries to open
	 * @return The list of the identifiers of the page factories that should not be used
	 */
	public abstract String[] getConflictingIds(String taskEditorPageFactoryId, TaskData taskData);

}
