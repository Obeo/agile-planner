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
package org.tuleap.mylyn.task.internal.agile.mock.connector.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.IBacklogItemMapping;
import org.tuleap.mylyn.task.agile.core.ICardMapping;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.mock.connector.util.IMylynMockConnectorConstants;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;

/**
 * The agile ui connector.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockConnectorAgileUi extends AbstractAgileRepositoryConnectorUI {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return IMylynMockConnectorConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getConflictingIds(java.lang.String,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public String[] getConflictingIds(String taskEditorPageFactoryId, ITask task, TaskRepository repository) {
		return new String[] {};
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getNewMilestoneMapping(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      java.lang.String, org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IMilestoneMapping getNewMilestoneMapping(TaskData planningTaskData, String parentMilestoneId,
			TaskRepository taskRepository, IProgressMonitor monitor) {
		return new MylynMockMilestoneMapping();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getNewMilestoneMapping(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      java.lang.String, org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IBacklogItemMapping getNewBacklogItemMapping(TaskData planningTaskData, String parentMilestoneId,
			TaskRepository taskRepository, IProgressMonitor monitor) {
		return new MylynMockBacklogItemMapping();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#hasCardwall(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean hasCardwall(ITask task, TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#hasPlanning(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean hasPlanning(ITask task, TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#mustCreateToolbarActions(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean mustCreateToolbarActions(ITask task, TaskRepository repository) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getNewCardMapping(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      java.lang.String, org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ICardMapping getNewCardMapping(TaskData planningTaskData, String parentCardId,
			TaskRepository taskRepository, IProgressMonitor monitor) {
		return new MylynMockCardMapping();
	}

}
