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
package org.tuleap.mylyn.task.internal.agile.ui.editors.planning;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;

/**
 * The Page for the PlanningTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * Flag to indicate whether standard mylyn actions should be created by this page.
	 */
	private boolean createActionsInToolbar;

	/**
	 * Constructor, which delegates to the matching super constructor.
	 * 
	 * @param editor
	 *            The parent TaskEditor.
	 * @param connectorKind
	 *            The related connector kind.
	 * @param createActionsInToolbar
	 *            Flag to indicate whether standard mylyn actions should be created by this page.
	 */
	public PlanningTaskEditorPage(TaskEditor editor, String connectorKind, boolean createActionsInToolbar) {
		super(editor, connectorKind);
		this.createActionsInToolbar = createActionsInToolbar;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		// We just want to display the planning editor in this tab
		Set<TaskEditorPartDescriptor> descriptors = Sets.newLinkedHashSet();
		descriptors.add(new PlanningTaskEditorPartDescriptor());
		return descriptors;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#fillToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void fillToolBar(IToolBarManager toolBarManager) {
		if (createActionsInToolbar) {
			super.fillToolBar(toolBarManager);
		}
	}

	/**
	 * Creates the TaskDataModel.
	 * 
	 * @param task
	 *            the task
	 * @param workingCopy
	 *            The working copy
	 * @return The newly created TaskDataModel, which uses the given workingCopy.
	 */
	protected TaskDataModel doCreateModel(ITask task, ITaskDataWorkingCopy workingCopy) {
		TaskRepository taskRepository = TasksUi.getRepositoryManager().getRepository(
				workingCopy.getConnectorKind(), workingCopy.getRepositoryUrl());
		return new TaskDataModel(taskRepository, task, workingCopy) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.mylyn.tasks.core.data.TaskDataModel#refresh(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			public void refresh(IProgressMonitor monitor) throws CoreException {
				super.refresh(monitor);
			}

		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createModel(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	protected TaskDataModel createModel(TaskEditorInput input) throws CoreException {
		String connectorKind = input.getTaskRepository().getConnectorKind();
		AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
				.getDefault().getServiceTrackerCustomizer();
		AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer.getConnector(connectorKind);
		TaskDataModel taskDataModel;
		ITaskDataWorkingCopy workingCopy = TasksUi.getTaskDataManager().getWorkingCopy(input.getTask());
		if (connector != null) {
			taskDataModel = connector.getModelRegistry().getRegisteredModel(getEditor());
			if (taskDataModel == null) {
				taskDataModel = doCreateModel(input.getTask(), workingCopy);
				connector.getModelRegistry().registerModel(getEditor(), taskDataModel);
			}
		} else {
			taskDataModel = doCreateModel(input.getTask(), workingCopy);
		}
		return taskDataModel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#dispose()
	 */
	@Override
	public void dispose() {
		TaskDataModel taskDataModel = getModel();
		if (taskDataModel != null) {
			String connectorKind = taskDataModel.getTaskRepository().getConnectorKind();
			AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
					.getDefault().getServiceTrackerCustomizer();
			AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer
					.getConnector(connectorKind);
			if (connector != null) {
				connector.getModelRegistry().deregisterModel(getEditor());
			}
		}
		super.dispose();
	}
}
