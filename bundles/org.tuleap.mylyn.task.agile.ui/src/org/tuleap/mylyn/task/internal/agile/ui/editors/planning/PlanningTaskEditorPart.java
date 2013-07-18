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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
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
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
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
public class PlanningTaskEditorPart extends AbstractTaskEditorPart {

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
		TaskAttribute backlogTypeNameAtt = getTaskData().getRoot().getAttribute(
				IMylynAgileCoreConstants.BACKLOG_TYPE_LABEL);
		if (backlogTypeNameAtt == null || backlogTypeNameAtt.getValue() == null) {
			backlogSection.setText(MylynAgileUIMessages
					.getString("PlanningTaskEditorPart.DefaulBacklogLabel")); //$NON-NLS-1$
		} else {
			backlogSection.setText(backlogTypeNameAtt.getValue());
		}
		backlogSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		backlogSection.setLayoutData(data);
		TaskAttribute backlogItemList = getTaskData().getRoot().getAttribute(
				IMylynAgileCoreConstants.BACKLOG_ITEM_LIST);
		TaskAttribute backlogItemTypeNameAtt = getTaskData().getRoot().getAttribute(
				IMylynAgileCoreConstants.BACKLOG_ITEM_TYPE_LABEL);
		String backlogItemTypeName;
		if (backlogItemTypeNameAtt == null || backlogItemTypeNameAtt.getValue() == null) {
			backlogItemTypeName = MylynAgileUIMessages
					.getString("PlanningTaskEditorPart.DefaulLabelColumnHeader"); //$NON-NLS-1$
		} else {
			backlogItemTypeName = backlogItemTypeNameAtt.getValue();
		}
		TableViewer viewer = createBacklogItemsTable(toolkit, backlogSection, backlogItemList,
				backlogItemTypeName);

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDragListener(viewer, getModel()));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDropAdapter(viewer, getModel()));

		final Section milestoneList = toolkit.createSection(body, ExpandableComposite.TITLE_BAR
				| Section.EXPANDED);
		milestoneList.setText("Sprints Planning"); // TODO Make this label dynamic, from the data model //$NON-NLS-1$
		milestoneList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneList.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		final Composite milestoneListComp = toolkit.createComposite(milestoneList);
		milestoneListComp.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		milestoneListComp.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		milestoneList.setClient(milestoneListComp);

		for (TaskAttribute milestoneAtt : getTaskData().getRoot().getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_MILESTONE.equals(milestoneAtt.getMetaData().getType())) {
				createMilestoneSection(toolkit, milestoneListComp, milestoneAtt, backlogItemTypeName);
			}
		}

		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(milestoneList);

		// The collapse all action
		Action collapseAll = new Action(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.CollapseAll"), PlatformUI.getWorkbench() //$NON-NLS-1$
						.getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL)) {
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
		Action expandAll = new Action(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.ExpandAll"), PlatformUI.getWorkbench() //$NON-NLS-1$
						.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD)) {

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
	 * Creates a section for a given Milestone in a given parent composite.
	 * 
	 * @param toolkit
	 *            The toolkit to use.
	 * @param parentComposite
	 *            The parent composite that will contain the created section.
	 * @param milestoneAtt
	 *            The TaskAttribute that represents the milestone.
	 * @param backlogItemTypeName
	 *            The label to use for the milestone's backlog items type.
	 */
	private void createMilestoneSection(FormToolkit toolkit, Composite parentComposite,
			TaskAttribute milestoneAtt, String backlogItemTypeName) {
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
		// Add sort action to the tool bar

		Action a = new Action(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.EditMilestoneActionLabel"), PlatformUI.getWorkbench() //$NON-NLS-1$
				.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE)) {
			// TODO Implement action to open milestone in its own editor
		};
		toolBarManager.add(a);
		toolBarManager.update(true);
		milestoneSection.setTextClient(toolbar);
		MilestoneSectionViewer milestoneViewer = new MilestoneSectionViewer(milestoneSection);
		milestoneViewer.setInput(milestoneAtt);
		milestoneViewer.refresh();
		TableViewer viewer = createBacklogItemsTable(toolkit, milestoneSection, milestoneAtt,
				backlogItemTypeName);

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDragListener(viewer, getModel(), milestoneViewer));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new MilestoneDropAdapter(viewer, getModel(), milestoneViewer));
	}

	/**
	 * Creates a table for backlogItems in the given parent section, with the relevant columns.
	 * 
	 * @param toolkit
	 *            The form toolkit to use
	 * @param section
	 *            The parent section which will contain the table and use it as its client
	 * @param milestoneAtt
	 *            The <code>TaskAttribute</code> that contains the backlog item <code>TaskAttribute</code>s
	 * @param backlogItemTypeName
	 *            The label to use for the type of the backlog items (used as header for one of the columns
	 * @return The TableViewer that displays the milestone's bakclog items.
	 */
	private TableViewer createBacklogItemsTable(final FormToolkit toolkit, final Section section,
			final TaskAttribute milestoneAtt, final String backlogItemTypeName) {
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
				} else if (element instanceof TaskAttribute) {
					ret = ((TaskAttribute)element).getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID)
							.getValue();
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
				} else if (element instanceof TaskAttribute) {
					ret = ((TaskAttribute)element).getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME)
							.getValue();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colLabel.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_LABEL_COL_WIDTH);

		// Column "points", whose label is dynamic ("Story Points" if the BacklogItem represents a UserStory)
		TableViewerColumn colPoints = new TableViewerColumn(viewer, SWT.NONE);
		TaskAttribute backlogItemPointLabelAtt = getTaskData().getRoot().getAttribute(
				IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS_LABEL);
		String backlogItemPointLabel;
		if (backlogItemPointLabelAtt == null) {
			backlogItemPointLabel = MylynAgileUIMessages
					.getString("PlanningTaskEditorPart.DefaultPointsColumnHeader"); //$NON-NLS-1$
		} else {
			backlogItemPointLabel = backlogItemPointLabelAtt.getValue();
		}
		colPoints.getColumn().setText(backlogItemPointLabel);
		colPoints.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String ret;
				if (element == null) {
					ret = MylynAgileUIMessages.getString("PlanningTaskEditorPageFactory.MissingNumericValue"); //$NON-NLS-1$;
				} else if (element instanceof TaskAttribute) {
					ret = ((TaskAttribute)element).getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS)
							.getValue();
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
				} else if (element instanceof TaskAttribute) {
					ret = ((TaskAttribute)element).getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT)
							.getValue();
				} else {
					ret = element.toString();
				}
				return ret;
			}
		});
		colParent.getColumn().setWidth(IMylynAgileUIConstants.DEFAULT_PARENT_COL_WIDTH);

		viewer.setInput(milestoneAtt);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		viewer.setSorter(new ViewerSorter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Viewer aViewer, Object e1, Object e2) {
				if (e1 instanceof TaskAttribute && e2 instanceof TaskAttribute) {
					int v1 = Integer.parseInt(((TaskAttribute)e1).getValue());
					int v2 = Integer.parseInt(((TaskAttribute)e2).getValue());
					return v1 - v2;
				}
				return super.compare(aViewer, e1, e2);
			}
		});
		return viewer;
	}
}
