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
package org.tuleap.mylyn.task.internal.agile.mock.connector.ui.wizards;

import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.tuleap.mylyn.task.internal.agile.mock.connector.ui.util.MylynMockConnectorUIMessages;

/**
 * The new task wizard.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockNewTaskWizard extends NewTaskWizard {

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param taskSelection
	 *            The current task selection
	 */
	public MylynMockNewTaskWizard(TaskRepository taskRepository, ITaskMapping taskSelection) {
		super(taskRepository, taskSelection);
		this.setWindowTitle(MylynMockConnectorUIMessages.getString("MylynMockNewTaskWizard.Title")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard#getInitializationData()
	 */
	@Override
	protected ITaskMapping getInitializationData() {
		return super.getInitializationData();
	}
}
