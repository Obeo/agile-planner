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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.commons.ui.CommonUiUtil;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.data.TaskDataModelEvent;
import org.eclipse.mylyn.tasks.core.data.TaskDataModelListener;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.IFormPart;
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
	 * Flag to indicate whether this page is the master page, in which case it mus create standard mylyn
	 * actions and ensure it refreshes its {@link TaskDataModel} on refresh.
	 */
	private boolean isMasterPage;

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
	 * @param isMasterPage
	 *            Flag to indicate whether this page is the master page, in which case it mus create standard
	 *            mylyn actions and ensure it refreshes its {@link TaskDataModel} on refresh.
	 */
	public PlanningTaskEditorPage(TaskEditor editor, String connectorKind, boolean isMasterPage) {
		super(editor, connectorKind);
		this.isMasterPage = isMasterPage;
		this.setNeedsSubmitButton(true);
		modelListener = new TaskDataModelListener() {
			@Override
			public void attributeChanged(TaskDataModelEvent event) {
				// Nothing
			}

			@Override
			public void modelRefreshed() {
				// The TaskDataModel has been refreshed, we dispose the page content and recreate it
				if (part != null) {
					disposeContent();
					createParts();
					reflow();
				}
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#refresh()
	 */
	@Override
	public void refresh() {
		if (isMasterPage) {
			TaskDataModel model = getModel();
			if (model != null) {
				try {
					model.refresh(null);
				} catch (CoreException e) {
					MylynAgileUIActivator.log(e, true);
				}
			}
		}
		// else nothing to do, we'll be notified through modelListener.modelRefreshed()
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
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(part.getControl());
	}

	/**
	 * Disposes the page content.
	 */
	private void disposeContent() {
		Composite parent = getManagedForm().getForm().getBody();
		Menu menu = parent.getMenu();
		CommonUiUtil.setMenu(parent, null);
		// clear old controls and parts
		for (Control control : parent.getChildren()) {
			control.dispose();
		}
		for (IFormPart formPart : getManagedForm().getParts()) {
			formPart.dispose();
			getManagedForm().removePart(formPart);
		}
		// restore menu
		parent.setMenu(menu);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#fillToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void fillToolBar(IToolBarManager toolBarManager) {
		if (isMasterPage) {
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
			model.addModelListener(modelListener);
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
