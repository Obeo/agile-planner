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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.tuleap.mylyn.task.agile.mock.connector.util.IMylynMockConnectorConstants;
import org.tuleap.mylyn.task.internal.agile.mock.connector.ui.wizards.MockRepositoryQueryWizard;
import org.tuleap.mylyn.task.internal.agile.mock.connector.ui.wizards.MylynMockNewTaskWizard;
import org.tuleap.mylyn.task.internal.agile.mock.connector.ui.wizards.MylynMockRepositorySettingsPage;

/**
 * The mocked repository connector UI.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockRepositoryConnectorUi extends AbstractRepositoryConnectorUi {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return IMylynMockConnectorConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getTaskKindLabel(org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public String getTaskKindLabel(ITask task) {
		if (task.getTaskKind() != null) {
			return task.getTaskKind();
		}
		return super.getTaskKindLabel(task);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#hasSearchPage()
	 */
	@Override
	public boolean hasSearchPage() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getSettingsPage(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository taskRepository) {
		return new MylynMockRepositorySettingsPage(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getQueryWizard(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.IRepositoryQuery)
	 */
	@Override
	public IWizard getQueryWizard(TaskRepository taskRepository, IRepositoryQuery queryToEdit) {
		return new MockRepositoryQueryWizard(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getNewTaskWizard(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITaskMapping)
	 */
	@Override
	public IWizard getNewTaskWizard(TaskRepository taskRepository, ITaskMapping selection) {
		return new MylynMockNewTaskWizard(taskRepository, selection);
	}

}
