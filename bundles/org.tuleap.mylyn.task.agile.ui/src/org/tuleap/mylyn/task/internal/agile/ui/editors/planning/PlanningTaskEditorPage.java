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

import com.google.common.base.Predicates;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.TaskAttributes;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownMapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;
import org.tuleap.mylyn.task.agile.ui.task.ISaveListener;
import org.tuleap.mylyn.task.internal.agile.ui.AgileRepositoryConnectorUiServiceTrackerCustomizer;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The Page for the PlanningTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPage extends AbstractTaskEditorPage implements ISaveListener, ITaskAttributeChangeListener {

	/**
	 * Flag to indicate whether this page is the master page, in which case it mus create standard mylyn
	 * actions and ensure it refreshes its {@link TaskDataModel} on refresh.
	 */
	private boolean isMasterPage;

	/**
	 * The backlog editor part.
	 */
	private BacklogTaskEditorPart backlogPart;

	/**
	 * The burn-down chart editor part.
	 */
	private BurndownTaskEditorPart burndownPart;

	/**
	 * The sub-milestone editor part.
	 */
	private SubMilestoneListTaskEditorPart milestonesPart;

	/**
	 * The model listener.
	 */
	private final TaskDataModelListener modelListener;

	/**
	 * The planning wrapper.
	 */
	private MilestonePlanningWrapper wrapper;

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
				if (backlogPart != null) {
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
					showEditorBusy(true);
					// This is important, will apply local changes
					doSave(new NullProgressMonitor());
					model.refresh(null);
				} catch (CoreException e) {
					MylynAgileUIActivator.log(e, true);
				} finally {
					showEditorBusy(false);
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
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createParts()
	 */
	@Override
	protected void createParts() {
		Composite body = getManagedForm().getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 2));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		FormToolkit toolkit = getManagedForm().getToolkit();
		Composite leftPane = toolkit.createComposite(body);
		leftPane.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		leftPane.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		Composite rightPane = toolkit.createComposite(body);
		rightPane.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		rightPane.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		BurndownMapper burndown = new BurndownMapper(getModel().getTaskData());
		if (burndown.getBurndownData() != null) {
			burndownPart = new BurndownTaskEditorPart();
			burndownPart.initialize(this);
			burndownPart.createControl(leftPane, toolkit);
		}

		backlogPart = new BacklogTaskEditorPart();
		getManagedForm().addPart(backlogPart);
		backlogPart.initialize(this);
		backlogPart.createControl(leftPane, toolkit);

		milestonesPart = new SubMilestoneListTaskEditorPart();
		getManagedForm().addPart(milestonesPart);
		milestonesPart.initialize(this);
		milestonesPart.createControl(rightPane, toolkit);
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
	 * Getter of the cached wrapper.
	 *
	 * @return The cached wrapper.
	 */
	protected MilestonePlanningWrapper getWrapper() {
		return wrapper;
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
			wrapper = new MilestonePlanningWrapper(model.getTaskData().getRoot());
			wrapper.addListener(this);
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
		if (wrapper != null) {
			wrapper.removeListener(this);
			wrapper = null;
		}
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

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.task.ISaveListener#beforeSave()
	 */
	@Override
	public void beforeSave() {
		TaskDataModel taskDataModel = getModel();
		if (taskDataModel != null) {
			TaskData taskData = taskDataModel.getTaskData();
			CardwallWrapper cardwall = new CardwallWrapper(taskData.getRoot());
			cardwall.markChanges(taskDataModel, Predicates.or(TaskAttributes
					.identifiedBy(MilestonePlanningWrapper.BACKLOG), TaskAttributes
					.prefixedBy(SubMilestoneWrapper.PREFIX_MILESTONE)));
		}
	}

	@Override
	public boolean isDirty() {
		return getManagedForm() != null && super.isDirty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (isMasterPage) {
			String connectorKind = getTaskRepository().getConnectorKind();
			AgileRepositoryConnectorUiServiceTrackerCustomizer serviceTrackerCustomizer = MylynAgileUIActivator
					.getDefault().getServiceTrackerCustomizer();
			AbstractAgileRepositoryConnectorUI connector = serviceTrackerCustomizer
					.getConnector(connectorKind);
			IModelRegistry registry = null;
			if (connector != null) {
				registry = connector.getModelRegistry();
			}
			if (registry != null) {
				registry.fireBeforeSave(getEditor());
			}
			super.doSave(monitor);
			if (registry != null) {
				registry.fireAfterSave(getEditor());
			}
		} else {
			super.doSave(monitor);
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

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		getModel().attributeChanged(attribute);
	}
}
