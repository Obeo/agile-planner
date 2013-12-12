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
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.mylyn.commons.ui.CommonUiUtil;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the backlog on the left and the list of milestones on the right. This editor manages
 * Sprints Planning for a release (Release Backlog + Sprints, both containing user stories), but also Releases
 * Planning for a product (Product backlog + Releases, both containing epics), depending on what is being
 * manipulated.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPart extends AbstractTaskEditorPart implements ITaskAttributeChangeListener {

	/**
	 * The index of the parent column.
	 */
	private static final int PARENT_COLUMN_INDEX = 4;

	/**
	 * The edited planning wrapper.
	 */
	private MilestonePlanningWrapper wrapper;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		// This method can be called to refresh the part on a TaskDataModel refresh() event
		// In such a case, the content is not disposed before, so it needs to be disposed here
		// otherwise all controls woul appear twice.
		if (!hasDisposedContent(parent)) {
			disposeContent(parent);
		}
		Form form = toolkit.createForm(parent);
		form.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		form.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite body = form.getBody();
		body.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 2));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite backlog = toolkit.createComposite(body);
		backlog.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		backlog.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Section backlogSection = toolkit.createSection(backlog, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION);
		backlogSection.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulBacklogLabel")); //$NON-NLS-1$
		backlogSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		backlogSection.setLayoutData(data);

		Section milestoneList = toolkit.createSection(body, ExpandableComposite.TITLE_BAR | Section.EXPANDED);
		milestoneList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneList.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		milestoneList.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulMilestonesTitle")); //$NON-NLS-1$

		ToolBarManager toolbarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolbarManager.createControl(milestoneList);
		toolbarManager.update(true);
		milestoneList.setTextClient(toolbar);
		if (wrapper != null) {
			wrapper.removeListener(this);
		}
		wrapper = new MilestonePlanningWrapper(getTaskData().getRoot());
		wrapper.addListener(this);
		TableViewer backlogViewer = createBacklogItemsTable(toolkit, backlogSection);

		// Drag'n drop
		backlogViewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDragListener(backlogViewer));
		backlogViewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDropAdapter(backlogViewer));
		backlogViewer.setInput(new MilestoneBacklogModel(wrapper));

		// Sub-milestones are stored in their order of creation, we want to display them
		// in the reverse order.
		Composite milestoneListClient = toolkit.createComposite(milestoneList);
		milestoneListClient.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneListClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		milestoneList.setClient(milestoneListClient);
		for (SubMilestoneWrapper subMilestone : Lists.reverse(wrapper.getSubMilestones())) {
			createMilestoneSection(toolkit, subMilestone, milestoneListClient);
		}
		addNewMilestoneAction(milestoneList, toolbarManager);
		addCollapseAllAction(milestoneList, toolbarManager);
		addExpandAllAction(milestoneList, toolbarManager);

		toolbarManager.update(true);
	}

	/**
	 * Add the expand all action.
	 * 
	 * @param milestoneList
	 *            the milestone list section
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addExpandAllAction(final Section milestoneList, ToolBarManager toolbarManager) {
		Action expandAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.ExpandAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.EXPAND_ALL)) {

			@Override
			public void run() {
				for (Control control : milestoneList.getChildren()) {
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
	 * @param milestoneList
	 *            the milestone list section
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addCollapseAllAction(final Section milestoneList, ToolBarManager toolbarManager) {
		Action collapseAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.CollapseAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.COLLAPSE_ALL)) {
			@Override
			public void run() {
				for (Control control : milestoneList.getChildren()) {
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
	 * @param milestoneList
	 *            the milestone list section
	 * @param toolbarManager
	 *            The toolbar manager
	 */
	private void addNewMilestoneAction(final Section milestoneList, ToolBarManager toolbarManager) {
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
		TaskData taskData = wrapper.getWrappedAttribute().getTaskData();
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
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		getModel().attributeChanged(wrapper.getWrappedAttribute());
		getModel().attributeChanged(attribute);
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
		milestoneViewer.setInput(new SubMilestoneBacklogModel(wrapper, subMilestone));
		milestoneViewer.refresh();
		TableViewer viewer = createBacklogItemsTable(toolkit, milestoneSection);
		viewer.setInput(new SubMilestoneBacklogModel(wrapper, subMilestone));

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDragListener(viewer, milestoneViewer));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDropAdapter(viewer, milestoneViewer));
	}

	/**
	 * Creates a table for backlogItems in the given parent section, with the relevant columns.
	 * 
	 * @param toolkit
	 *            The form toolkit to use
	 * @param section
	 *            The parent section which will contain the table and use it as its client
	 * @return The TableViewer that displays the milestone's backlog items.
	 */
	private TableViewer createBacklogItemsTable(final FormToolkit toolkit, final Section section) {
		final String strMissing = MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingTextValue"); //$NON-NLS-1$
		final Table table = toolkit.createTable(section, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		section.setClient(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new BacklogItemListContentProvider());

		// Column "id"
		final TableViewerColumn colId = new TableViewerColumn(viewer, SWT.NONE);
		colId.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultIdColumnHeader")); //$NON-NLS-1$

		// Set the HyperLink label provider
		colId.setLabelProvider(new HyperlinkLabelProvider(table));
		colId.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_ID_COL_WIDTH);

		// Add the listener
		viewer.getTable().addMouseListener(this.getMouseListener(table));
		viewer.getTable().addMouseMoveListener(this.getMouseMoveListener(table));

		// Column type
		final TableViewerColumn colType = new TableViewerColumn(viewer, SWT.NONE);
		colType.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultTypeColumnHeader")); //$NON-NLS-1$
		colType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = strMissing;
				} else if (element instanceof BacklogItemWrapper) {
					ret = ((BacklogItemWrapper)element).getType();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colType.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_TYPE_COL_WIDTH);

		// Column "label", whose label is dynamic ("User Story" if the BacklogItem represents a UserStory)
		final TableViewerColumn colLabel = new TableViewerColumn(viewer, SWT.NONE);
		colLabel.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulBacklogLabel")); //$NON-NLS-1$
		colLabel.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = strMissing;
				} else if (element instanceof BacklogItemWrapper) {
					ret = ((BacklogItemWrapper)element).getLabel();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colLabel.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_LABEL_COL_WIDTH);

		final TableViewerColumn colPoints = new TableViewerColumn(viewer, SWT.NONE);
		String backlogItemPointLabel = MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.DefaultPointsColumnHeader"); //$NON-NLS-1$
		colPoints.getColumn().setText(backlogItemPointLabel);
		colPoints.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = MylynAgileUIMessages.getString("PlanningTaskEditorPageFactory.MissingNumericValue"); //$NON-NLS-1$;
				} else if (element instanceof BacklogItemWrapper) {
					ret = ((BacklogItemWrapper)element).getInitialEffort();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colPoints.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_POINTS_COL_WIDTH);

		// Column "parent"
		final TableViewerColumn colParent = new TableViewerColumn(viewer, SWT.NONE);
		colParent.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultParentColumnHeader")); //$NON-NLS-1$
		colParent.setLabelProvider(new ParentLabelProvider(table));
		colParent.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_PARENT_COL_WIDTH);

		// Column status
		final TableViewerColumn colStatus = new TableViewerColumn(viewer, SWT.NONE);
		colStatus.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultStatusColumnHeader")); //$NON-NLS-1$
		colStatus.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = strMissing;
				} else if (element instanceof BacklogItemWrapper) {
					ret = ((BacklogItemWrapper)element).getStatus();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colStatus.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_STATUS_COL_WIDTH);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		return viewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.forms.AbstractFormPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (wrapper != null) {
			wrapper.removeListener(this);
		}
	}

	/**
	 * Create a listener on the first table column cells to open clicked backlog items.
	 * 
	 * @param table
	 *            The table.
	 * @return the listener.
	 */
	private MouseListener getMouseListener(final Table table) {
		MouseListener listener = new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				return;
			}

			@Override
			public void mouseDown(MouseEvent e) {
				return;
			}

			@Override
			public void mouseUp(MouseEvent e) {
				mouseUpAction(table, e);
			}

		};
		return listener;
	}

	/**
	 * The action to do when clicking on a table item.
	 * 
	 * @param table
	 *            the table.
	 * @param e
	 *            The event.
	 */
	private void mouseUpAction(final Table table, MouseEvent e) {
		Point point = new Point(e.x, e.y);
		TableItem item = table.getItem(point);
		if (item != null && !item.isDisposed()) {
			item.setBackground(-1, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			item.setForeground(-1, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
		if (item == null) {
			return;
		}
		Rectangle idColumn = item.getBounds(0);
		if (idColumn.contains(point)) {
			table.deselectAll();
			item.setBackground(0, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			openTask(item);
		} else {
			Rectangle parentColumn = item.getBounds(PARENT_COLUMN_INDEX);

			if (parentColumn.contains(point)) {
				table.deselectAll();
				item.setBackground(0, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				openParentTask(item);
			}
		}
	}

	/**
	 * Create a listener on the id and parent table columns to change cursor appearance.
	 * 
	 * @param table
	 *            The table.
	 * @return the listener.
	 */
	private MouseMoveListener getMouseMoveListener(final Table table) {
		MouseMoveListener listener = new MouseMoveListener() {
			TableItem item;

			@Override
			public void mouseMove(MouseEvent event) {
				Point point = new Point(event.x, event.y);
				item = table.getItem(point);
				if (item == null) {
					return;
				}
				Rectangle idColumn = item.getBounds(0);
				Rectangle parentColumn = item.getBounds(PARENT_COLUMN_INDEX);

				Cursor handCursor = new Cursor(table.getShell().getDisplay(), SWT.CURSOR_HAND);
				Cursor arrowCursor = new Cursor(table.getShell().getDisplay(), SWT.CURSOR_ARROW);
				if (idColumn.contains(point) || parentColumn.contains(point)) {
					table.setCursor(handCursor);
				} else {
					table.setCursor(arrowCursor);
				}
			}

		};
		return listener;
	}

	/**
	 * Open the task corresponding to the selected Item.
	 * 
	 * @param item
	 *            The Item
	 */
	private void openTask(TableItem item) {
		BacklogItemWrapper backlogItemwrapper = (BacklogItemWrapper)item.getData();
		TaskRepository repository = null;
		String repositoryUrl = PlanningTaskEditorPart.this.getTaskData().getRepositoryUrl();
		List<TaskRepository> allRepositories = TasksUi.getRepositoryManager().getAllRepositories();
		for (TaskRepository taskRepository : allRepositories) {
			if (repositoryUrl.equals(taskRepository.getRepositoryUrl())) {
				repository = taskRepository;
			}
		}
		if (repository != null) {
			TasksUiUtil.openTask(repository, backlogItemwrapper.getId());
		}
	}

	/**
	 * Open the task corresponding to the selected Item.
	 * 
	 * @param item
	 *            The Item
	 */
	private void openParentTask(TableItem item) {
		BacklogItemWrapper backlogItemwrapper = (BacklogItemWrapper)item.getData();
		TaskRepository repository = null;
		String repositoryUrl = PlanningTaskEditorPart.this.getTaskData().getRepositoryUrl();
		List<TaskRepository> allRepositories = TasksUi.getRepositoryManager().getAllRepositories();
		for (TaskRepository taskRepository : allRepositories) {
			if (repositoryUrl.equals(taskRepository.getRepositoryUrl())) {
				repository = taskRepository;
			}
		}
		if (repository != null) {
			TasksUiUtil.openTask(repository, backlogItemwrapper.getParentDisplayId());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#getPartId()
	 */
	@Override
	public String getPartId() {
		return IMylynAgileUIConstants.PLANNING_TASK_EDITOR_PART_DESC_ID;
	}

	/**
	 * Disposes the widgets.
	 * 
	 * @param parent
	 *            the parent.
	 */
	private void disposeContent(Composite parent) {
		Menu menu = parent.getMenu();
		CommonUiUtil.setMenu(parent, null);
		// clear old controls and parts
		for (Control control : parent.getChildren()) {
			control.dispose();
		}
		for (IFormPart part : getManagedForm().getParts()) {
			part.dispose();
			getManagedForm().removePart(part);
		}
		// restore menu
		parent.setMenu(menu);
	}

	/**
	 * Indicates whether createControl() can be called.
	 * 
	 * @param parent
	 *            the parent.
	 * @return <code>true</code> if and only if internal controls are disposed and need to be re-created.
	 */
	private boolean hasDisposedContent(Composite parent) {
		Control[] controls = parent.getChildren();
		return controls == null || controls.length == 0 || controls[0].isDisposed();
	}

}
