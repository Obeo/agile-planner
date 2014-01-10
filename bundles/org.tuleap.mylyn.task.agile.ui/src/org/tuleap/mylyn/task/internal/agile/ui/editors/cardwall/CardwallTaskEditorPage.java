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

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.commons.ui.CommonUiUtil;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
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
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;
import org.tuleap.mylyn.task.agile.ui.task.ISaveListener;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The Page for the CardwallTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPage extends AbstractTaskEditorPage implements ISaveListener {

	/**
	 * The editor part.
	 */
	private CardwallTaskEditorPart part;

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
	 */
	public CardwallTaskEditorPage(TaskEditor editor, String connectorKind) {
		super(editor, connectorKind);
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
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		// We just want to display the planning editor in this tab
		Set<TaskEditorPartDescriptor> descriptors = Sets.newLinkedHashSet();
		descriptors.add(new CardwallTaskEditorPartDescriptor());
		return descriptors;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createParts()
	 */
	@Override
	protected void createParts() {
		part = new CardwallTaskEditorPart();
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
			model.addModelListener(modelListener);
			registry.addSaveListener(getEditor(), this);
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
		AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
				.getDefault().getServiceTrackerCustomizer();
		AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer
				.getConnector(getConnectorKind());
		if (connector != null) {
			IModelRegistry registry = connector.getModelRegistry();
			registry.removeSaveListener(getEditor(), this);
		}
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#refresh()
	 */
	@Override
	public void refresh() {
		// Nothing to do, we'll be notified through modelListener.modelRefreshed()
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	// @Override
	// public void doSave(IProgressMonitor monitor) {
	// // if (!isDirty()) {
	// // return;
	// // }
	// // Before we actually save, we compare the state of the cardwall to the last read state
	// // In order to mark all modified fields, to be able to submit only those fields (at the time of
	// // submission, we no longer have access to the TaskDataModel).
	// TaskDataModel taskDataModel = getModel();
	// if (taskDataModel != null) {
	// markCards(taskDataModel);
	// // Only call this if the page has been initialized, otherwise, NPE occurs.
	// if (getManagedForm() != null) {
	// super.doSave(monitor);
	// }
	// }
	// }

	/**
	 * Mark modified card.
	 * 
	 * @param taskDataModel
	 *            The {@link TaskDataModel}
	 */
	private void markCards(TaskDataModel taskDataModel) {
		TaskData taskData = taskDataModel.getTaskData();
		CardwallWrapper cardwall = new CardwallWrapper(taskData.getRoot());
		for (SwimlaneWrapper swimlane : cardwall.getSwimlanes()) {
			for (CardWrapper card : swimlane.getCards()) {
				markCardChanges(taskDataModel, card);
			}
		}
	}

	/**
	 * Marks the card modification since last read from server.
	 * 
	 * @param taskDataModel
	 *            The {@link TaskDataModel}
	 * @param card
	 *            The card
	 */
	private void markCardChanges(TaskDataModel taskDataModel, CardWrapper card) {
		TaskAttribute columnIdTaskAttribute = card.getColumnIdTaskAttribute();
		if (columnIdTaskAttribute != null) {
			TaskAttribute lastReadColumnId = taskDataModel.getLastReadAttribute(columnIdTaskAttribute);
			if (card.markColumnIdChanged(!card.getColumnId().equals(lastReadColumnId.getValue()))) {
				taskDataModel.attributeChanged(columnIdTaskAttribute);
			}
		}
		for (TaskAttribute field : card.getFieldAttributes()) {
			TaskAttribute lastRead = taskDataModel.getLastReadAttribute(field);
			if (card.mark(field, !field.getValues().equals(lastRead.getValues()))) {
				taskDataModel.attributeChanged(field);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.ui.task.ISaveListener#beforeSave()
	 */
	@Override
	public void beforeSave() {
		TaskDataModel taskDataModel = getModel();
		if (taskDataModel != null) {
			markCards(taskDataModel);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.ui.task.ISaveListener#afterSave()
	 */
	@Override
	public void afterSave() {
		// Nothing to do yet
	}
}
