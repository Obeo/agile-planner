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
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;

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
	 * Provides the wizard to use for milestone creation. A milestone is created within a parent planning.
	 * 
	 * @param planningTaskData
	 *            The task data of the parent planning where the milestone is to be created.
	 * @param taskRepository
	 *            The task repository of the planning that should also host the new milestone.
	 * @param monitor
	 *            The progress monitor to use for monitoring progress...
	 * @return The wizard to create the task that represents the milestone.
	 */
	public abstract IWizard getNewMilestoneWizard(TaskData planningTaskData, TaskRepository taskRepository,
			IProgressMonitor monitor);

}
