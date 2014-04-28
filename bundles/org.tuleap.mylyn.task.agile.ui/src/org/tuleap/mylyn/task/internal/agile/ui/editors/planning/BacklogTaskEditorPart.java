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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.IBacklogItemMapping;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the backlog of the current milestone.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogTaskEditorPart extends AbstractTableTaskEditorPart {

	/**
	 * The backlog part descriptor ID.
	 */
	public static final String BACKLOG_PART_DESC_ID = "org.tuleap.mylyn.task.agile.backlog"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		Composite panel = toolkit.createComposite(parent);
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		panel.setLayoutData(data);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 3;
		gridLayout.verticalSpacing = 3;
		panel.setLayout(gridLayout);
		Section backlogSection = toolkit.createSection(panel, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION);
		setControl(panel);
		backlogSection.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulBacklogLabel")); //$NON-NLS-1$
		backlogSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		backlogSection.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		TableViewer backlogViewer = createBacklogItemsTable(toolkit, backlogSection);
		// Drag'n drop
		backlogViewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDragListener(backlogViewer, parent));
		backlogViewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDropAdapter(backlogViewer, parent));
		backlogViewer.setInput(new MilestoneBacklogModel(getWrapper()));

		ToolBarManager toolbarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolbarManager.createControl(backlogSection);
		backlogSection.setTextClient(toolbar);
		addNewBacklogItemAction(toolbarManager);
		toolbarManager.update(true);

	}

	/**
	 * Add the new backlogItem action.
	 *
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addNewBacklogItemAction(ToolBarManager toolbarManager) {
		Action newBacklogItem = new Action(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.NewBacklogItem"), MylynAgileUIActivator //$NON-NLS-1$
				.getImageDescriptor(IMylynAgileIcons.NEW_BACKLOGITEM)) {
			@Override
			public void run() {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
						createNewBacklogItem(monitor);
					}
				};

				try {
					PlatformUI.getWorkbench().getProgressService().run(false, false, runnable);
				} catch (InvocationTargetException e) {
					MylynAgileUIActivator.log(e, true);
				} catch (InterruptedException e) {
					MylynAgileUIActivator.log(e, true);
				}
			}
		};
		toolbarManager.add(newBacklogItem);
	}

	/**
	 * Creates a new backlogItem.
	 *
	 * @param monitor
	 *            The progress monitor
	 */
	private void createNewBacklogItem(IProgressMonitor monitor) {
		TaskData taskData = getWrapper().getWrappedAttribute().getTaskData();
		String connectorKind = taskData.getConnectorKind();
		AbstractAgileRepositoryConnectorUI connector = MylynAgileUIActivator.getDefault()
				.getServiceTrackerCustomizer().getConnector(connectorKind);
		TaskRepository taskRepository = TasksUi.getRepositoryManager().getRepository(connectorKind,
				taskData.getRepositoryUrl());
		if (connector != null) {
			IBacklogItemMapping mapping = connector.getNewBacklogItemMapping(taskData, taskData.getTaskId(),
					taskRepository, monitor);
			AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryConnector(connectorKind);
			AbstractTaskDataHandler taskDataHandler = repositoryConnector.getTaskDataHandler();

			TaskData newBITaskData = new TaskData(taskDataHandler.getAttributeMapper(taskRepository),
					connectorKind, taskRepository.getRepositoryUrl(), ""); //$NON-NLS-1$

			try {
				boolean isInitialized = taskDataHandler.initializeTaskData(taskRepository, newBITaskData,
						mapping, monitor);
				if (isInitialized) {
					TasksUiInternal.createAndOpenNewTask(newBITaskData);
				} else {
					MylynAgileUIActivator
					.log(MylynAgileUIMessages
							.getString(
									"PlanningTaskEditorPart.InvalidInitializationNewBacklogItem", connectorKind), true); //$NON-NLS-1$
				}
			} catch (CoreException e) {
				MylynAgileUIActivator.log(e, true);
			}
		} else {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					"PlanningTaskEditorPart.NoAgileUiConnectorFoundWithKind", connectorKind), true); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#getPartId()
	 */
	@Override
	public String getPartId() {
		return BACKLOG_PART_DESC_ID;
	}

}
