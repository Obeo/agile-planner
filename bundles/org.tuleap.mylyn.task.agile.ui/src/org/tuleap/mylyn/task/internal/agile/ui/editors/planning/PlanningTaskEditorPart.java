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

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
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
		// TODO Add backlog item type label ("Release", "Sprint", ...)
		// TaskAttribute backlogItemTypeNameAtt = getTaskData().getRoot().getAttribute(
		// IMylynAgileCoreConstants.BACKLOG_ITEM_TYPE_LABEL);
		// if (backlogItemTypeNameAtt == null || backlogItemTypeNameAtt.getValue() == null) {
		backlogItemTypeName = MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.DefaulLabelColumnHeader"); //$NON-NLS-1$
		// } else {
		// backlogItemTypeName = backlogItemTypeNameAtt.getValue();
		// }
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

		for (SubMilestoneWrapper subMilestone : wrapper.getSubMilestones()) {
			createMilestoneSection(toolkit, milestoneListComp, subMilestone, backlogItemTypeName);
		}

		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(milestoneList);

		// The collapse all action
		Action collapseAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.CollapseAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.COLLAPSE_ALL)) {
			@Override
			public void run() {
				for (Control control : milestoneListComp.getChildren()) {
					if (control instanceof Section) {
						((ExpandableComposite)control).setExpanded(false);
					}
				}
			}
		};

		// The expand all action
		Action expandAll = new Action(MylynAgileUIMessages.getString("PlanningTaskEditorPart.ExpandAll"), //$NON-NLS-1$
				MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.EXPAND_ALL)) {

			@Override
			public void run() {
				for (Control control : milestoneListComp.getChildren()) {
					if (control instanceof Section) {
						((ExpandableComposite)control).setExpanded(true);
					}
				}
			}
		};

		toolBarManager.add(collapseAll);
		toolBarManager.add(expandAll);
		toolBarManager.update(true);
		milestoneList.setTextClient(toolbar);
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
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(milestoneSection);
		final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);

		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				handCursor.dispose();
			}
		});

		// Add sort edit to the tool bar
		final String subMilestoneId = subMilestone.getId();
		Action a = new Action(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.EditMilestoneActionLabel"), PlatformUI.getWorkbench() //$NON-NLS-1$
				.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE)) {
			@Override
			public void run() {
				TaskRepository repository = null;
				String repositoryUrl = PlanningTaskEditorPart.this.getTaskData().getRepositoryUrl();
				List<TaskRepository> allRepositories = TasksUi.getRepositoryManager().getAllRepositories();
				for (TaskRepository taskRepository : allRepositories) {
					if (repositoryUrl.equals(taskRepository.getRepositoryUrl())) {
						repository = taskRepository;
					}
				}
				if (repository != null) {
					TasksUiUtil.openTask(repository, String.valueOf(subMilestoneId));
				}
			}
		};
		toolBarManager.add(a);
		toolBarManager.update(true);

		milestoneSection.setTextClient(toolbar);
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
		Table table = toolkit.createTable(section, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		section.setClient(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new BacklogItemListContentProvider());

		// Column "id"
		TableViewerColumn colId = new TableViewerColumn(viewer, SWT.NONE);
		colId.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaultIdColumnHeader")); //$NON-NLS-1$
		colId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = strMissing;
				} else if (element instanceof BacklogItemWrapper) {
					ret = ((BacklogItemWrapper)element).getDisplayId();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colId.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_ID_COL_WIDTH);

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

		// Column "points", whose label is dynamic ("Story Points" if the BacklogItem represents a UserStory)
		TableViewerColumn colPoints = new TableViewerColumn(viewer, SWT.NONE);
		// TODO Manage label of initial effort
		// TaskAttribute backlogItemPointLabelAtt = getTaskData().getRoot().getAttribute(
		// IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS_LABEL);
		String backlogItemPointLabel;
		// if (backlogItemPointLabelAtt == null) {
		backlogItemPointLabel = MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.DefaultPointsColumnHeader"); //$NON-NLS-1$
		// } else {
		// backlogItemPointLabel = backlogItemPointLabelAtt.getValue();
		// }
		colPoints.getColumn().setText(backlogItemPointLabel);
		colPoints.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = MylynAgileUIMessages.getString("PlanningTaskEditorPageFactory.MissingNumericValue"); //$NON-NLS-1$;
				} else if (element instanceof BacklogItemWrapper) {
					ret = String.valueOf(((BacklogItemWrapper)element).getInitialEffort());
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
		colParent.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = strMissing;
				} else if (element instanceof BacklogItemWrapper) {
					ret = "TODO"; //$NON-NLS-1$
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
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
}
