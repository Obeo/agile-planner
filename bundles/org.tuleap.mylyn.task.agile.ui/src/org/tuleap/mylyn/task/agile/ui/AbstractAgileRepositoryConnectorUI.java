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
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.IBacklogItemMapping;
import org.tuleap.mylyn.task.agile.core.ICardMapping;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;
import org.tuleap.mylyn.task.internal.agile.ui.task.ModelRegistry;

/**
 * UI Connector to an Agile repository.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 1.0
 */
public abstract class AbstractAgileRepositoryConnectorUI {

	/**
	 * The model register.
	 */
	private IModelRegistry modelRegistry;

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
	 * Provides the mapping for BacklogItem creation. A BacklogItem is created within a parent planning. This
	 * mapping will be provided to the {@link org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler} of
	 * the connector in order to create the BacklogItem.
	 *
	 * @param planningTaskData
	 *            The task data of the parent planning where the BacklogItem is to be created.
	 * @param parentMilestoneId
	 *            The identifier of the parent milestone
	 * @param taskRepository
	 *            The task repository of the planning that should also host the new BacklogItem.
	 * @param monitor
	 *            The progress monitor to use for monitoring progress...
	 * @return The wizard to create the task that represents the BacklogItem.
	 */
	public abstract IBacklogItemMapping getNewBacklogItemMapping(TaskData planningTaskData,
			String parentMilestoneId, TaskRepository taskRepository, IProgressMonitor monitor);

	/**
	 * Provides the mapping for card creation.
	 *
	 * @param planningTaskData
	 *            The task data of the parent planning where the BacklogItem is to be created.
	 * @param parentCardId
	 *            The identifier of the parent milestone
	 * @param taskRepository
	 *            The task repository of the planning that should also host the new BacklogItem.
	 * @param monitor
	 *            The progress monitor to use for monitoring progress...
	 * @return The wizard to create the task that represents the BacklogItem.
	 */
	public abstract ICardMapping getNewCardMapping(TaskData planningTaskData, String parentCardId,
			TaskRepository taskRepository, IProgressMonitor monitor);

	/**
	 * <p>
	 * Indicates the list of the identifier of the task editor page factories that are in conflict with the
	 * task editor page factory identifier provided. Every task editor page factory of the Agile UI bundle
	 * will call this method before initialization. As a result, one can say that for a specific use case, the
	 * planning page factory or the context page factory is in conflict with the editor.
	 * </p>
	 * <p>
	 * The default implementation returns an empty array. This method is meant to be overridden by
	 * implementors if needed.
	 * </p>
	 *
	 * @param taskEditorPageFactoryId
	 *            The identifier of the task editor page factory that is being used
	 * @param task
	 *            The task that the editor tries to open
	 * @param repository
	 *            The task repository
	 * @return The list of the identifiers of the page factories that should not be used
	 */
	public String[] getConflictingIds(String taskEditorPageFactoryId, ITask task, TaskRepository repository) {
		return new String[] {};
	}

	/**
	 * Indicates whether the given {@link ITask} should have a cardwall tab displayed.
	 *
	 * @param task
	 *            The task
	 * @param repository
	 *            The task repository
	 * @return <code>true</code> if and only if a cardwall tab should be displayed in this task editor.
	 */
	public abstract boolean hasCardwall(ITask task, TaskRepository repository);

	/**
	 * Indicates whether the given {@link ITask} should have a planning tab displayed.
	 *
	 * @param task
	 *            The task
	 * @param repository
	 *            The task repository
	 * @return <code>true</code> if and only if a planning tab should be displayed in this task editor.
	 */
	public abstract boolean hasPlanning(ITask task, TaskRepository repository);

	/**
	 * Indicates whether the agile editor should create the standard toolbar actions given {@link ITask}
	 * should have a cardwall tab displayed. Implementors should return <code>true</code> only if they don't
	 * want to display the main tab which already creates the standard actions.
	 *
	 * @param task
	 *            The task
	 * @param repository
	 *            The task repository
	 * @return <code>true</code> if and only if a cardwall tab should be displayed in this task editor.
	 */
	public abstract boolean mustCreateToolbarActions(ITask task, TaskRepository repository);

	/**
	 * Provides the model register that keeps track of shared models.
	 *
	 * @return The model register
	 */
	public IModelRegistry getModelRegistry() {
		if (modelRegistry == null) {
			modelRegistry = new ModelRegistry();
		}
		return modelRegistry;
	}

}
