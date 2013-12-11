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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The Page for the CardwallTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * Constructor, which delegates to the matching super constructor.
	 * 
	 * @param editor
	 *            The parent TaskEditor.
	 * @param connectorKind
	 *            The related connector kind.
	 */
	public CardwallTaskEditorPage(TaskEditor editor, String connectorKind) {
		super(editor, connectorKind);
		this.setNeedsSubmitButton(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> descriptors = super.createPartDescriptors();

		this.removeAllParts(descriptors);

		descriptors.add(new CardwallTaskEditorPartDescriptor());

		return descriptors;
	}

	/**
	 * Removes the parts from the set of part descriptors.
	 * 
	 * @param parts
	 *            the part descriptors.
	 */
	private void removeAllParts(Set<TaskEditorPartDescriptor> parts) {
		Iterator<TaskEditorPartDescriptor> iterator = parts.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#fillToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void fillToolBar(IToolBarManager toolBarManager) {
		// Do NOT call super.fillToolbar() otherwise there will be
		// several identical actions!
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
		if (connector != null) {
			IModelRegistry registry = connector.getModelRegistry();
			TaskDataModel model = registry.getRegisteredModel(getEditor());
			if (model == null) {
				model = super.createModel(input);
				registry.registerModel(getEditor(), model);
			}
			return model;
		}
		throw new IllegalStateException(MylynAgileUIMessages
				.getString("SharedTaskDataModel.agileConnectorRequired")); //$NON-NLS-1$
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
