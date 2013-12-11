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
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.data.TaskDataModelEvent;
import org.eclipse.mylyn.tasks.core.data.TaskDataModelListener;
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
	 * The editor part.
	 */
	private PlanningTaskEditorPart part;

	/**
	 * The model listener.
	 */
	private final TaskDataModelListener modelListener;

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
		this.setNeedsSubmitButton(true);
		modelListener = new TaskDataModelListener() {
			@Override
			public void attributeChanged(TaskDataModelEvent event) {
				// Nothing
			}

			@Override
			public void modelRefreshed() {
				if (part != null) {
					part.createControl(getManagedForm().getForm().getBody(), getManagedForm().getToolkit());
				}
			}
		};
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
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createParts()
	 */
	@Override
	protected void createParts() {
		part = new PlanningTaskEditorPart();
		getManagedForm().addPart(part);
		part.initialize(this);
		part.createControl(getManagedForm().getForm().getBody(), getManagedForm().getToolkit());
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
			taskDataModel.removeModelListener(modelListener);
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
