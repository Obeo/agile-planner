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
package org.tuleap.mylyn.task.internal.agile.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.IFormPage;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.editors.ITaskEditorPageFactoryConstants;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.PlanningTaskEditorPage;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The editor page factory will instantiate the editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPageFactory extends AbstractTaskEditorPageFactory {

	/**
	 * Used to order the tabs in Mylyn. 22 in order to be after the context (20) but before the task (30).
	 */
	private static final int PRIORITY_AGILE_PLANNING = 22;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#canCreatePageFor(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	public boolean canCreatePageFor(TaskEditorInput input) {
		ITask task = input.getTask();
		try {
			@SuppressWarnings("restriction")
			TaskData taskData = TasksUiPlugin.getTaskDataManager().getTaskData(task);
			String taskKind = AgileTaskKindUtil.getAgileTaskKind(taskData);

			return AgileTaskKindUtil.TASK_KIND_MILESTONE.equals(taskKind)
					|| AgileTaskKindUtil.TASK_KIND_TOP_PLANNING.equals(taskKind);
		} catch (CoreException e) {
			MylynAgileUIActivator.log(e, true);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getPageImage()
	 */
	@Override
	public Image getPageImage() {
		return MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.PLANNING_16X16);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getPageText()
	 */
	@Override
	public String getPageText() {
		return MylynAgileUIMessages.getString("PlanningTaskEditorPageFactory.PageText"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#createPage(org.eclipse.mylyn.tasks.ui.editors.TaskEditor)
	 */
	@Override
	public IFormPage createPage(TaskEditor parentEditor) {
		TaskEditorInput taskEditorInput = parentEditor.getTaskEditorInput();
		String connectorKind = taskEditorInput.getTask().getConnectorKind();
		try {
			@SuppressWarnings("restriction")
			TaskData taskData = TasksUiPlugin.getTaskDataManager().getTaskData(taskEditorInput.getTask());
			String taskKind = AgileTaskKindUtil.getAgileTaskKind(taskData);
			// We want to create the toolbar actions (synchronize etc.) only for top-plannings
			// because otherwise another tab will do it.
			return new PlanningTaskEditorPage(parentEditor, connectorKind,
					AgileTaskKindUtil.TASK_KIND_TOP_PLANNING.equals(taskKind));
		} catch (CoreException e) {
			MylynAgileUIActivator.log(e, true);
		}
		return new PlanningTaskEditorPage(parentEditor, connectorKind, false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getConflictingIds(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	public String[] getConflictingIds(TaskEditorInput input) {
		ITask task = input.getTask();
		String connectorKind = input.getTaskRepository().getConnectorKind();
		try {
			@SuppressWarnings("restriction")
			TaskData taskData = TasksUiPlugin.getTaskDataManager().getTaskData(task);

			AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
					.getDefault().getServiceTrackerCustomizer();
			AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer
					.getConnector(connectorKind);
			if (connector != null) {
				return connector.getConflictingIds(
						ITaskEditorPageFactoryConstants.PLANNING_TASK_EDITOR_PAGE_FACTORY_ID, taskData);
			}
		} catch (CoreException e) {
			MylynAgileUIActivator.log(e, true);
		}

		return new String[] {};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getPriority()
	 */
	@Override
	public int getPriority() {
		return PRIORITY_AGILE_PLANNING;
	}
}
