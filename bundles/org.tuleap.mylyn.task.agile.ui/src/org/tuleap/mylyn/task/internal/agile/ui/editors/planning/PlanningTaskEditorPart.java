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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
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
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the backlog on the left and the list of scopes on the right. This editor manages
 * Sprints Planning for a release (Release Backlog + Sprints, both containing user stories), but also Releases
 * Planning for a product (Product backlog + Releases, both containing epics), depending on what is being
 * manipulated.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPart extends AbstractTaskEditorPart {

	/**
	 * String to use as a display value when a value is missing.
	 */
	private final String strMissing = MylynAgileUIMessages
			.getString("PlanningTaskEditorPageFactory.MissingTextValue"); //$NON-NLS-1$

	/**
	 * String to use as a display value when a value is missing.
	 */
	private final String numMissing = MylynAgileUIMessages
			.getString("PlanningTaskEditorPageFactory.MissingNumericValue"); //$NON-NLS-1$

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
					.getString("PlanningTaskEditorPageFactory.DefaulBacklogLabel")); //$NON-NLS-1$
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
					.getString("PlanningTaskEditorPageFactory.DefaulLabelColumnHeader"); //$NON-NLS-1$
		} else {
			backlogItemTypeName = backlogItemTypeNameAtt.getValue();
		}
		createBacklogItemsTable(toolkit, backlogSection, backlogItemList, backlogItemTypeName);

		Section scopeList = toolkit.createSection(body, ExpandableComposite.TITLE_BAR | Section.EXPANDED);
		scopeList.setText("Sprints Planning"); // TODO Make this label dynamic, from the data model //$NON-NLS-1$
		scopeList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		scopeList.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite scopeListComp = toolkit.createComposite(scopeList);
		scopeListComp.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		scopeListComp.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		scopeList.setClient(scopeListComp);

		TaskAttribute scopeListAtt = getTaskData().getRoot()
				.getAttribute(IMylynAgileCoreConstants.SCOPE_LIST);
		for (TaskAttribute scopeAtt : scopeListAtt.getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_SCOPE.equals(scopeAtt.getMetaData().getType())) {
				createScopeSection(toolkit, scopeListComp, scopeAtt, backlogItemTypeName);
			}
		}
	}

	/**
	 * Creates a section for a given Scope in a given parent composite.
	 * 
	 * @param toolkit
	 *            The toolkit to use.
	 * @param parentComposite
	 *            The parent composite that will contain the created section.
	 * @param scopeAtt
	 *            The TaskAttribute that represents the scope.
	 * @param backlogItemTypeName
	 *            The label to use for the scope's backlog items type.
	 */
	private void createScopeSection(FormToolkit toolkit, Composite parentComposite, TaskAttribute scopeAtt,
			String backlogItemTypeName) {
		Section scopeSection = toolkit.createSection(parentComposite, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
		scopeSection.setText(getScopeSectionHeaderText(scopeAtt));
		scopeSection.setDescription(getScopeSectionItemsCapacity(scopeAtt) + " / " //$NON-NLS-1$
				+ getScopeSectionCapacity(scopeAtt));
		scopeSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData scopeLayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
		scopeSection.setLayoutData(scopeLayoutData);
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(scopeSection);
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

		Action a = new Action(
				MylynAgileUIMessages.getString("PlanningTaskEditorPart.EditScopeActionLabel"), PlatformUI.getWorkbench() //$NON-NLS-1$
						.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE)) {
			// TODO Implement action to open scope in its own editor
		};
		toolBarManager.add(a);
		toolBarManager.update(true);
		scopeSection.setTextClient(toolbar);
		createBacklogItemsTable(toolkit, scopeSection, scopeAtt, backlogItemTypeName);
	}

	/**
	 * Computes the required scope capacity by adding the points of all the scope's items.
	 * 
	 * @param scopeAtt
	 *            The scope TaskAttribute.
	 * @return the sum of all backlog items in <code>scopeAtt</code>.
	 */
	private String getScopeSectionItemsCapacity(TaskAttribute scopeAtt) {
		double sumOfPoints = 0.0;
		for (TaskAttribute child : scopeAtt.getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(child.getMetaData().getType())) {
				TaskAttribute pointsAtt = child.getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
				if (pointsAtt != null) {
					String strPoints = pointsAtt.getValue();
					if (strPoints != null) {
						try {
							sumOfPoints += Double.parseDouble(strPoints);
						} catch (NumberFormatException e) {
							// Nothing to do
						}
					}
				}
			}
		}
		return String.valueOf(sumOfPoints);
	}

	/**
	 * Computes the estimated scope capacity by retrieving it from the relevant sub-attribute in the given
	 * TaskAttribute.
	 * 
	 * @param scopeAtt
	 *            The scope TaskAttribute.
	 * @return the estimated scope capacity by retrieving it from the relevant sub-attribute in the given
	 *         TaskAttribute
	 */
	private String getScopeSectionCapacity(TaskAttribute scopeAtt) {
		String capacity = numMissing;
		TaskAttribute capacityAtt = scopeAtt.getAttribute(IMylynAgileCoreConstants.SCOPE_CAPACITY);
		if (capacityAtt != null && capacityAtt.getValue() != null) {
			capacity = capacityAtt.getValue();
		}
		return capacity;
	}

	/**
	 * Computes and returns the text to use as a header for a scope section.
	 * 
	 * @param scopeAtt
	 *            The TaskAttribute that represents the scope.
	 * @return The text to use as a header for a scope section.
	 */
	private String getScopeSectionHeaderText(TaskAttribute scopeAtt) {
		TaskAttribute nameAtt = scopeAtt.getAttribute(IMylynAgileCoreConstants.SCOPE_NAME);
		TaskAttribute startDateAtt = scopeAtt.getAttribute(IMylynAgileCoreConstants.START_DATE);
		TaskAttribute endDateAtt = scopeAtt.getAttribute(IMylynAgileCoreConstants.END_DATE);

		// Compute the title of the section
		StringBuilder titleBuilder = new StringBuilder();
		if (nameAtt == null || nameAtt.getValue() == null) {
			titleBuilder.append(strMissing);
		} else {
			titleBuilder.append(nameAtt.getValue());
		}
		titleBuilder.append(" ("); //$NON-NLS-1$
		DateFormat dateFormat = new SimpleDateFormat(MylynAgileUIMessages
				.getString("PlanningTaskEditorPageFactory.ScopeDateFormat")); //$NON-NLS-1$
		if (startDateAtt == null || startDateAtt.getValue() == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String startDate = dateFormat.format(new Date(Long.parseLong(startDateAtt.getValue())));
			titleBuilder.append(startDate);
		}
		titleBuilder.append(" - "); //$NON-NLS-1$
		if (endDateAtt == null || endDateAtt.getValue() == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String endDate = dateFormat.format(new Date(Long.parseLong(endDateAtt.getValue())));
			titleBuilder.append(endDate);
		}
		titleBuilder.append(")"); //$NON-NLS-1$
		String title = titleBuilder.toString();
		return title;
	}

	/**
	 * Creates a table for backlogItems in the given parent section, with the relevant columns.
	 * 
	 * @param toolkit
	 *            The form toolkit to use
	 * @param backlogSection
	 *            The parent section which will contain the table and use it as its client
	 * @param backlogItemList
	 *            The <code>TaskAttribute</code> that contains the backlog item <code>TaskAttribute</code>s
	 * @param backlogItemTypeName
	 *            The label to use for the type of the backlog items (used as header for one of the columns
	 */
	private void createBacklogItemsTable(FormToolkit toolkit, Section backlogSection,
			TaskAttribute backlogItemList, String backlogItemTypeName) {
		Table table = toolkit.createTable(backlogSection, SWT.BORDER | SWT.MULTI);
		backlogSection.setClient(table);
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
					ret = numMissing;
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
		colParent.getColumn().setText(
				MylynAgileUIMessages.getString("PlanningTaskEditorPageFactory.ParentHeader")); //$NON-NLS-1$
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

		viewer.setInput(backlogItemList);
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

		// Drag'n drop
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemDragListener(viewer));
		viewer.addDropSupport(DND.DROP_MOVE, new Transfer[] {LocalSelectionTransfer.getTransfer() },
				new BacklogItemListDropAdapter(viewer));
	}

	/**
	 * Content Provider for Scopes.
	 */
	private static final class BacklogItemListContentProvider implements IStructuredContentProvider {

		@Override
		public void inputChanged(Viewer pViewer, Object oldInput, Object newInput) {
			//
		}

		@Override
		public void dispose() {
			//
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof TaskAttribute) {
				TaskAttribute ta = (TaskAttribute)inputElement;
				List<Object> children = new ArrayList<Object>();
				for (TaskAttribute child : ta.getAttributes().values()) {
					if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(child.getMetaData().getType())) {
						children.add(child);
					}
				}
				return children.toArray();
			}
			return null;
		}
	}

	/**
	 * Drag listener for BacklogItem tables.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class BacklogItemDragListener implements DragSourceListener {

		/**
		 * The table viewer to listen to.
		 */
		private TableViewer viewer;

		/**
		 * Constructor, requires the table viewer to listen to.
		 * 
		 * @param pviewer
		 *            the table viewer to listen to.
		 */
		private BacklogItemDragListener(TableViewer pviewer) {
			this.viewer = pviewer;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
		 */
		@Override
		public void dragStart(DragSourceEvent event) {
			// NO-OP
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
		 */
		@Override
		public void dragSetData(DragSourceEvent event) {
			LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
			if (transfer.isSupportedType(event.dataType)) {
				IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
				transfer.setSelection(selection);
				event.data = selection;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
		 */
		@Override
		public void dragFinished(DragSourceEvent event) {
			if (event.detail == DND.DROP_MOVE) {
				TaskAttribute itemListAtt = (TaskAttribute)viewer.getInput();
				// remove the dragged element(s) from the source
				IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
				for (Iterator<?> it = selection.iterator(); it.hasNext();) {
					TaskAttribute itemAtt = (TaskAttribute)it.next();
					itemListAtt.removeAttribute(itemAtt.getId());
				}
			}
			viewer.refresh();
		}

	}

	/**
	 * Drop Listener for BacklogItem tables.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class BacklogItemListDropAdapter extends ViewerDropAdapter {

		/**
		 * Constructor, requires a viewer. Delegates to the parent class.
		 * 
		 * @param viewer
		 *            The viewer.
		 */
		private BacklogItemListDropAdapter(Viewer viewer) {
			super(viewer);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
		 */
		@Override
		public boolean performDrop(Object data) {
			boolean ret = false;
			IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer()
					.getSelection();
			Object target = getCurrentTarget();
			if (target instanceof TaskAttribute) {
				TaskAttribute targetAtt = (TaskAttribute)target;
				TaskAttribute listAtt = targetAtt.getParentAttribute();
				String id = targetAtt.getValue();
				int insertionIndex = Integer.parseInt(id);
				switch (getCurrentLocation()) {
					case LOCATION_AFTER:
						insertionIndex++;
						break;
					case LOCATION_NONE:
						return false;
					default:
						break;
				}
				if (listAtt == ((TaskAttribute)selection.getFirstElement()).getParentAttribute()) {
					moveSelectedElements(selection, listAtt, insertionIndex);
					ret = false;
				} else {
					// Drag'n drop from one list to another
					// We must compute new non-conflicting ids for the moved elements, unless we actually use
					// unique ids
					ret = true;
				}
				getViewer().refresh();
			}
			return ret;
		}

		/**
		 * Performs the move of the selected elements at the given index.
		 * 
		 * @param selection
		 *            The selection of elements to move.
		 * @param listAtt
		 *            The parent attribute that receives the moved elements.
		 * @param insertionIndex
		 *            The insertion index of the moved elements
		 */
		private void moveSelectedElements(IStructuredSelection selection, TaskAttribute listAtt,
				int insertionIndex) {
			List<TaskAttribute> elementsToMove = new ArrayList<TaskAttribute>();
			for (Iterator<?> it = selection.iterator(); it.hasNext();) {
				Object next = it.next();
				if (next instanceof TaskAttribute) {
					elementsToMove.add((TaskAttribute)next);
				}
			}
			new TaskAttributeWrapper(listAtt).moveElementsSortedByValue(elementsToMove, insertionIndex,
					IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int,
		 *      org.eclipse.swt.dnd.TransferData)
		 */
		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
				// Object target = getCurrentTarget();
				if (target instanceof TaskAttribute) {
					String taskType = ((TaskAttribute)target).getMetaData().getType();
					if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(taskType)) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
