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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;
import org.tuleap.mylyn.task.agile.mock.connector.util.IMylynMockConnectorConstants;
import org.tuleap.mylyn.task.internal.agile.mock.connector.ui.util.MylynMockConnectorUIMessages;

/**
 * The setting page used to create a new repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockRepositorySettingsPage extends AbstractRepositorySettingsPage {

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public MylynMockRepositorySettingsPage(TaskRepository taskRepository) {
		super(MylynMockConnectorUIMessages.getString("MylynMockRepositorySettingsPage.Title"), //$NON-NLS-1$
				MylynMockConnectorUIMessages.getString("MylynMockRepositorySettingsPage.Description"), //$NON-NLS-1$
				taskRepository);
		this.setNeedsAnonymousLogin(true);
		this.setNeedsValidateOnFinish(true);
		this.setNeedsHttpAuth(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return IMylynMockConnectorConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#createAdditionalControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createAdditionalControls(Composite parent) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#isValidUrl(java.lang.String)
	 */
	@Override
	protected boolean isValidUrl(String url) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#isMissingCredentials()
	 */
	@Override
	protected boolean isMissingCredentials() {
		return super.isMissingCredentials();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage#getValidator(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	protected Validator getValidator(TaskRepository taskRepository) {
		return new Validator() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				this.setStatus(Status.OK_STATUS);
			}
		};
	}

}
