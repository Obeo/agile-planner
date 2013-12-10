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
package org.tuleap.mylyn.task.agile.ui.task;

import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.data.TaskDataModelListener;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Shared {@link TaskDataModel} used to share a single taskDataModel instance between several pages of a
 * {@link TaskEditor}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class SharedTaskDataModel extends TaskDataModel {

	/**
	 * The editor in which to share a task data model.
	 */
	private final TaskEditor editor;

	/**
	 * The model registry.
	 */
	private final IModelRegistry registry;

	/**
	 * Constructor.
	 * 
	 * @param taskRepository
	 *            The repository
	 * @param task
	 *            The task
	 * @param taskDataState
	 *            The working copy
	 * @param editor
	 *            The editor
	 * @param registry
	 *            the registry
	 */
	public SharedTaskDataModel(TaskRepository taskRepository, ITask task, ITaskDataWorkingCopy taskDataState,
			TaskEditor editor, IModelRegistry registry) {
		super(taskRepository, task, taskDataState);
		this.editor = editor;
		this.registry = registry;
	}

	/**
	 * Retrieves the actual model from the registry.
	 * 
	 * @return The actual task data model.
	 */
	private TaskDataModel getActualModel() {
		TaskDataModel model = registry.getRegisteredModel(editor);
		Assert.isNotNull(model);
		return model;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#addModelListener(org.eclipse.mylyn.tasks.core.data.TaskDataModelListener)
	 */
	@Override
	public void addModelListener(TaskDataModelListener listener) {
		getActualModel().addModelListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		getActualModel().attributeChanged(attribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getChangedAttributes()
	 */
	@Override
	public Set<TaskAttribute> getChangedAttributes() {
		return getActualModel().getChangedAttributes();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getChangedOldAttributes()
	 */
	@Override
	public Set<TaskAttribute> getChangedOldAttributes() {
		return getActualModel().getChangedOldAttributes();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getLastReadAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public TaskAttribute getLastReadAttribute(TaskAttribute taskAttribute) {
		return getActualModel().getLastReadAttribute(taskAttribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getTask()
	 */
	@Override
	public ITask getTask() {
		return getActualModel().getTask();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getTaskData()
	 */
	@Override
	public TaskData getTaskData() {
		return getActualModel().getTaskData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#getTaskRepository()
	 */
	@Override
	public TaskRepository getTaskRepository() {
		return getActualModel().getTaskRepository();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#hasBeenRead()
	 */
	@Override
	public boolean hasBeenRead() {
		return getActualModel().hasBeenRead();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#hasIncomingChanges(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public boolean hasIncomingChanges(TaskAttribute taskAttribute) {
		return getActualModel().hasIncomingChanges(taskAttribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#hasOutgoingChanges(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public boolean hasOutgoingChanges(TaskAttribute taskAttribute) {
		return getActualModel().hasOutgoingChanges(taskAttribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return getActualModel().isDirty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#refresh(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void refresh(IProgressMonitor monitor) throws CoreException {
		getActualModel().refresh(monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#removeModelListener(org.eclipse.mylyn.tasks.core.data.TaskDataModelListener)
	 */
	@Override
	public void removeModelListener(TaskDataModelListener listener) {
		getActualModel().removeModelListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#revert()
	 */
	@Override
	public void revert() {
		getActualModel().revert();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#save(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void save(IProgressMonitor monitor) throws CoreException {
		getActualModel().save(monitor);
	}

	/**
	 * Creates an agile {@link TaskDataModel} and shares it in the registry for the given editor.
	 * 
	 * @param input
	 *            The editor input
	 * @param editor
	 *            The editor
	 * @param taskDataModel
	 *            The actual task data model, which will be used to delegate calls to.
	 * @return A new instance of {@link SharedTaskDataModel}
	 * @throws CoreException
	 *             If the retrieval of the working copy of the input's task causes a problem.
	 */
	public static SharedTaskDataModel shareModel(TaskEditorInput input, TaskEditor editor,
			TaskDataModel taskDataModel) throws CoreException {
		String connectorKind = input.getTaskRepository().getConnectorKind();
		AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
				.getDefault().getServiceTrackerCustomizer();
		AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer.getConnector(connectorKind);
		if (connector != null) {
			ITaskDataWorkingCopy workingCopy = TasksUi.getTaskDataManager().getWorkingCopy(input.getTask());
			IModelRegistry registry = connector.getModelRegistry();
			registry.registerModel(editor, taskDataModel);
			return new SharedTaskDataModel(input.getTaskRepository(), input.getTask(), workingCopy, editor,
					registry);
		}
		throw new IllegalStateException(MylynAgileUIMessages
				.getString("SharedTaskDataModel.agileConnectorRequired")); //$NON-NLS-1$
	}

}
