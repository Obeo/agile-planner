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

import com.google.common.collect.Lists;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the list of milestones on the right. This edit part manages the backlog items
 * contained in each sub-milestone of the current milestone. It can be empty. It provides actions to create a
 * new sub-milestone, and fold/unfold all sub-milestones.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class SubMilestoneListTaskEditorPart extends AbstractTableTaskEditorPart {

	/**
	 * The sub-milestone part descriptor ID.
	 */
	public static final String SUBMILESTONE_PART_DESC_ID = "org.tuleap.mylyn.task.agile.submilestone"; //$NON-NLS-1$

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
		Section milestoneList = toolkit
				.createSection(panel, ExpandableComposite.TITLE_BAR | Section.EXPANDED);
		setControl(panel);
		milestoneList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneList.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulMilestonesTitle")); //$NON-NLS-1$
		milestoneList.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		ToolBarManager toolbarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolbarManager.createControl(milestoneList);
		toolbarManager.update(true);
		milestoneList.setTextClient(toolbar);

		// Sub-milestones are stored in their order of creation, we want to display them
		// in the reverse order.
		Composite milestoneListClient = toolkit.createComposite(milestoneList);
		milestoneListClient.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneListClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		milestoneList.setClient(milestoneListClient);
		for (SubMilestoneWrapper subMilestone : Lists.reverse(getWrapper().getSubMilestones())) {
			createMilestoneSection(toolkit, subMilestone, milestoneListClient);
		}
		addNewMilestoneAction(toolbarManager);
		addCollapseAllAction(milestoneListClient, toolbarManager);
		addExpandAllAction(milestoneListClient, toolbarManager);

		toolbarManager.update(true);
	}

	/**
	 * Add the expand all action.
	 *
	 * @param milestoneListClient
	 *            the milestone list section client composite
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addExpandAllAction(final Composite milestoneListClient, ToolBarManager toolbarManager) {
		Action expandAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.ExpandAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.EXPAND_ALL)) {
			@Override
			public void run() {
				for (Control control : milestoneListClient.getChildren()) {
					if (control instanceof Section) {
						((ExpandableComposite)control).setExpanded(true);
					}
				}
			}
		};
		toolbarManager.add(expandAll);
	}

	/**
	 * Add the collapse all action.
	 *
	 * @param milestoneListClient
	 *            the milestone list section client composite
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addCollapseAllAction(final Composite milestoneListClient, ToolBarManager toolbarManager) {
		Action collapseAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.CollapseAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.COLLAPSE_ALL)) {
			@Override
			public void run() {
				for (Control control : milestoneListClient.getChildren()) {
					if (control instanceof Section) {
						((ExpandableComposite)control).setExpanded(false);
					}
				}
			}
		};
		toolbarManager.add(collapseAll);
	}

	/**
	 * Add the new milestone action.
	 *
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addNewMilestoneAction(ToolBarManager toolbarManager) {
		Action newMilestone = new Action(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.NewMilestone"), MylynAgileUIActivator //$NON-NLS-1$
				.getImageDescriptor(IMylynAgileIcons.NEW_MILESTONE_16X16)) {
			@Override
			public void run() {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						createNewMilestone(monitor);
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
		toolbarManager.add(newMilestone);
	}

	/**
	 * Creates a new milestone.
	 *
	 * @param monitor
	 *            The progress monitor
	 */
	private void createNewMilestone(IProgressMonitor monitor) {
		TaskData taskData = getWrapper().getWrappedAttribute().getTaskData();
		String connectorKind = taskData.getConnectorKind();
		AbstractAgileRepositoryConnectorUI connector = MylynAgileUIActivator.getDefault()
				.getServiceTrackerCustomizer().getConnector(connectorKind);
		TaskRepository taskRepository = TasksUi.getRepositoryManager().getRepository(connectorKind,
				taskData.getRepositoryUrl());
		if (connector != null) {
			IMilestoneMapping mapping = connector.getNewMilestoneMapping(taskData, taskData.getTaskId(),
					taskRepository, monitor);
			AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryConnector(connectorKind);
			AbstractTaskDataHandler taskDataHandler = repositoryConnector.getTaskDataHandler();

			TaskData newMilestoneTaskData = new TaskData(taskDataHandler.getAttributeMapper(taskRepository),
					connectorKind, taskRepository.getRepositoryUrl(), ""); //$NON-NLS-1$

			try {
				boolean isInitialized = taskDataHandler.initializeTaskData(taskRepository,
						newMilestoneTaskData, mapping, monitor);
				if (isInitialized) {
					TasksUiInternal.createAndOpenNewTask(newMilestoneTaskData);
				} else {
					MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
							"PlanningTaskEditorPart.InvalidInitializationNewMilestone", connectorKind), true); //$NON-NLS-1$
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
	 * Creates a section for a given Milestone in a given parent composite.
	 *
	 * @param toolkit
	 *            The toolkit to use.
	 * @param subMilestone
	 *            The sub-milestone wrapper.
	 * @param milestoneListClient
	 *            The milestone list client composite
	 */
	private void createMilestoneSection(FormToolkit toolkit, SubMilestoneWrapper subMilestone,
			Composite milestoneListClient) {
		Section milestoneSection = toolkit.createSection(milestoneListClient, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
		milestoneSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData milestoneLayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
		milestoneSection.setLayoutData(milestoneLayoutData);
		MilestoneSectionViewer milestoneViewer = new MilestoneSectionViewer(milestoneSection);
		milestoneViewer.setInput(new SubMilestoneBacklogModel(getWrapper(), subMilestone));
		milestoneViewer.refresh();
		TableViewer viewer = createBacklogItemsTable(toolkit, milestoneSection);
		viewer.setInput(new SubMilestoneBacklogModel(getWrapper(), subMilestone));

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDragListener(viewer, milestoneViewer, milestoneListClient));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDropAdapter(viewer, milestoneViewer, milestoneListClient));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#getPartId()
	 */
	@Override
	public String getPartId() {
		return SUBMILESTONE_PART_DESC_ID;
	}

}
