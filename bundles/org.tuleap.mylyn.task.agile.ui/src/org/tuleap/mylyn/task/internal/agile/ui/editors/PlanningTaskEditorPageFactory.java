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
import org.eclipse.mylyn.commons.ui.CommonImages;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.IFormPage;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.PlanningTaskEditorPage;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The editor page factory will instantiate the editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class PlanningTaskEditorPageFactory extends AbstractTaskEditorPageFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#canCreatePageFor(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	public boolean canCreatePageFor(TaskEditorInput input) {
		ITask task = input.getTask();
		try {
			TaskData taskData = TasksUiPlugin.getTaskDataManager().getTaskData(task);
			TaskAttribute backlogItemListAtt = taskData.getRoot().getAttribute(
					IMylynAgileCoreConstants.BACKLOG_ITEM_LIST);
			TaskAttribute planningScopeListAtt = taskData.getRoot().getAttribute(
					IMylynAgileCoreConstants.SCOPE_LIST);

			if (backlogItemListAtt != null && planningScopeListAtt != null) {
				return true;
			}
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
		return CommonImages.getImage(TasksUiImages.REPOSITORY_SMALL);
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
		return new PlanningTaskEditorPage(parentEditor, connectorKind);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getConflictingIds(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	public String[] getConflictingIds(TaskEditorInput input) {
		return new String[] {ITasksUiConstants.ID_PAGE_PLANNING };
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#getPriority()
	 */
	@Override
	public int getPriority() {
		return PRIORITY_PLANNING;
	}
}
