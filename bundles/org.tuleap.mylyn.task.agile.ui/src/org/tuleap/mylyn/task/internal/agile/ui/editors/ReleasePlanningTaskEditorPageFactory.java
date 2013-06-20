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

import org.eclipse.mylyn.commons.ui.CommonImages;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.IFormPage;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreContants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The editor page factory will instantiate the editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ReleasePlanningTaskEditorPageFactory extends AbstractTaskEditorPageFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#canCreatePageFor(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	public boolean canCreatePageFor(TaskEditorInput input) {
		ITask task = input.getTask();
		String taskKind = task.getAttribute(IMylynAgileCoreContants.TASK_KIND_KEY);
		if (IMylynAgileCoreContants.TASK_KIND_RELEASE.equals(taskKind)) {
			return true;
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
		return MylynAgileUIMessages.getString("ReleasePlanningTaskEditorPageFactory.PageText"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory#createPage(org.eclipse.mylyn.tasks.ui.editors.TaskEditor)
	 */
	@Override
	public IFormPage createPage(TaskEditor parentEditor) {
		// TODO create the page!
		return null;
	}

}
