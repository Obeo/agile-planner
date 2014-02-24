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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
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
public abstract class AbstractTableTaskEditorPart extends AbstractTaskEditorPart {

	/**
	 * The index of the parent column.
	 */
	protected static final int PARENT_COLUMN_INDEX = 4;

	/**
	 * <p>
	 * Checks that the given editor page is an instance of {@link PlanningTaskEditorPage}.
	 * </p>
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#initialize(org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage)
	 */
	@Override
	public void initialize(AbstractTaskEditorPage taskEditorPage) {
		Assert.isTrue(taskEditorPage instanceof PlanningTaskEditorPage);
		super.initialize(taskEditorPage);
	}

	/**
	 * Provides the page's wrapper.
	 *
	 * @return The page's wrapper.
	 */
	protected MilestonePlanningWrapper getWrapper() {
		PlanningTaskEditorPage page = (PlanningTaskEditorPage)getTaskEditorPage();
		if (page == null) {
			throw new IllegalStateException(
					"Developer error: the editor page must be set before using getWrapper()"); //$NON-NLS-1$
		}
		return page.getWrapper();
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
	protected TableViewer createBacklogItemsTable(final FormToolkit toolkit, final Section section) {
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

		// Column "label"
		final TableViewerColumn colLabel = new TableViewerColumn(viewer, SWT.NONE);
		colLabel.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulLabelColumnHeader")); //$NON-NLS-1$
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
	 * Create a listener on the first table column cells to open clicked backlog items.
	 *
	 * @param table
	 *            The table.
	 * @return the listener.
	 */
	protected MouseListener getMouseListener(final Table table) {
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
	protected void mouseUpAction(final Table table, MouseEvent e) {
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
	protected MouseMoveListener getMouseMoveListener(final Table table) {
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
	protected void openTask(TableItem item) {
		BacklogItemWrapper backlogItemwrapper = (BacklogItemWrapper)item.getData();
		TaskRepository repository = null;
		String repositoryUrl = getTaskData().getRepositoryUrl();
		List<TaskRepository> allRepositories = TasksUi.getRepositoryManager().getAllRepositories();
		for (TaskRepository taskRepository : allRepositories) {
			if (repositoryUrl.equals(taskRepository.getRepositoryUrl())) {
				repository = taskRepository;
				break;
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
	protected void openParentTask(TableItem item) {
		BacklogItemWrapper backlogItemwrapper = (BacklogItemWrapper)item.getData();
		TaskRepository repository = null;
		String repositoryUrl = getTaskData().getRepositoryUrl();
		List<TaskRepository> allRepositories = TasksUi.getRepositoryManager().getAllRepositories();
		for (TaskRepository taskRepository : allRepositories) {
			if (repositoryUrl.equals(taskRepository.getRepositoryUrl())) {
				repository = taskRepository;
			}
		}
		if (repository != null) {
			TasksUiUtil.openTask(repository, backlogItemwrapper.getParentId());
		}
	}

}
