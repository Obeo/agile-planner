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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
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
		Form form = toolkit.createForm(parent);
		form.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		form.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite body = form.getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite backlog = toolkit.createComposite(body);
		backlog.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		backlog.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Section backlogSection = toolkit.createSection(backlog, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION);
		wrapper = new MilestonePlanningWrapper(getTaskData().getRoot());
		wrapper.addListener(this);
		String backlogTitle = wrapper.getBacklogTitle();
		if (backlogTitle == null) {
			backlogSection.setText(MylynAgileUIMessages
					.getString("PlanningTaskEditorPart.DefaulBacklogLabel")); //$NON-NLS-1$
		} else {
			backlogSection.setText(backlogTitle);
		}
		backlogSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		backlogSection.setLayoutData(data);
		String backlogItemTypeName;
		backlogItemTypeName = MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.DefaulLabelColumnHeader"); //$NON-NLS-1$
		TableViewer viewer = createBacklogItemsTable(toolkit, backlogSection, new MilestoneBacklogModel(
				wrapper), backlogItemTypeName);

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDragListener(viewer));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDropAdapter(viewer));

		final Section milestoneList = toolkit.createSection(body, ExpandableComposite.TITLE_BAR
				| Section.EXPANDED);
		String milestonesTitle = wrapper.getMilestonesTitle();
		if (milestonesTitle == null) {
			milestoneList.setText(MylynAgileUIMessages
					.getString("PlanningTaskEditorPart.DefaulMilestonesTitle")); //$NON-NLS-1$
		} else {
			milestoneList.setText(milestonesTitle);
		}
		milestoneList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneList.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		final Composite milestoneListComp = toolkit.createComposite(milestoneList);
		milestoneListComp.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneListComp.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		milestoneList.setClient(milestoneListComp);

		// Sub-milestones are stored in their order of creation, we want to display them
		// in the reverse order.
		for (SubMilestoneWrapper subMilestone : Lists.reverse(wrapper.getSubMilestones())) {
			createMilestoneSection(toolkit, milestoneListComp, subMilestone, backlogItemTypeName);
		}

		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(milestoneList);

		addNewMilestoneAction(toolBarManager);
		addCollapseAllAction(milestoneListComp, toolBarManager);
		addExpandAllAction(milestoneListComp, toolBarManager);

		toolBarManager.update(true);
		milestoneList.setTextClient(toolbar);
	}

	/**
	 * Add the expand all action.
	 * 
	 * @param milestoneList
	 *            Milestones
	 * @param toolBarManager
	 *            Manager
	 */
	private void addExpandAllAction(final Composite milestoneList, ToolBarManager toolBarManager) {
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
		toolBarManager.add(expandAll);
	}

	/**
	 * Add the collapse all action.
	 * 
	 * @param milestoneList
	 *            Milestones
	 * @param toolBarManager
	 *            Manager
	 */
	private void addCollapseAllAction(final Composite milestoneList, ToolBarManager toolBarManager) {
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
		toolBarManager.add(collapseAll);
	}

	/**
	 * Add the new milestone action.
	 * 
	 * @param toolBarManager
	 *            The manager.
	 */
	private void addNewMilestoneAction(ToolBarManager toolBarManager) {
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
		toolBarManager.add(newMilestone);
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
	}

	/**
	 * Creates a section for a given Milestone in a given parent composite.
	 * 
	 * @param toolkit
	 *            The toolkit to use.
	 * @param parentComposite
	 *            The parent composite that will contain the created section.
	 * @param subMilestone
	 *            The sub-milestone wrapper.
	 * @param backlogItemTypeName
	 *            The label to use for the milestone's backlog items type.
	 */
	private void createMilestoneSection(FormToolkit toolkit, Composite parentComposite,
			SubMilestoneWrapper subMilestone, String backlogItemTypeName) {
		Section milestoneSection = toolkit.createSection(parentComposite, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
		milestoneSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData milestoneLayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
		milestoneSection.setLayoutData(milestoneLayoutData);
		MilestoneSectionViewer milestoneViewer = new MilestoneSectionViewer(milestoneSection);
		milestoneViewer.setInput(new SubMilestoneBacklogModel(wrapper, subMilestone));
		milestoneViewer.refresh();
		TableViewer viewer = createBacklogItemsTable(toolkit, milestoneSection, new SubMilestoneBacklogModel(
				wrapper, subMilestone), backlogItemTypeName);

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
	 * @param container
	 *            The container of the backlog items
	 * @param backlogItemTypeName
	 *            The label to use for the type of the backlog items (used as header for one of the columns
	 * @return The TableViewer that displays the milestone's backlog items.
	 */
	private TableViewer createBacklogItemsTable(final FormToolkit toolkit, final Section section,
			final IBacklog container, final String backlogItemTypeName) {
		final String strMissing = MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingTextValue"); //$NON-NLS-1$
		final Table table = toolkit.createTable(section, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		section.setClient(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new BacklogItemListContentProvider());

		// Column "id"
		TableViewerColumn colId = new TableViewerColumn(viewer, SWT.NONE);
		colId.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultIdColumnHeader")); //$NON-NLS-1$

		// Set the HyperLink label provider
		colId.setLabelProvider(new HyperlinkLabelProvider(table));

		colId.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_ID_COL_WIDTH);

		// Add the listener

		viewer.getTable().addMouseListener(this.getMouseListener(table));
		viewer.getTable().addMouseMoveListener(this.getMouseMoveListener(table));

		// Column type
		TableViewerColumn colType = new TableViewerColumn(viewer, SWT.NONE);
		colType.getColumn().setText("Type"); //$NON-NLS-1$
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
		TableViewerColumn colLabel = new TableViewerColumn(viewer, SWT.NONE);
		colLabel.getColumn().setText(backlogItemTypeName);
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

		TableViewerColumn colPoints = new TableViewerColumn(viewer, SWT.NONE);
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
		TableViewerColumn colParent = new TableViewerColumn(viewer, SWT.NONE);
		colParent.getColumn().setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.ParentHeader")); //$NON-NLS-1$
		colParent.setLabelProvider(new ParentLabelProvider(table));
		colParent.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_PARENT_COL_WIDTH);

		viewer.setInput(container);
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
		}
		Rectangle parentColumn = item.getBounds(3);
		if (parentColumn.contains(point)) {
			table.deselectAll();
			item.setBackground(0, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			openParentTask(item);
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
				Rectangle parentColumn = item.getBounds(3);
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

}
