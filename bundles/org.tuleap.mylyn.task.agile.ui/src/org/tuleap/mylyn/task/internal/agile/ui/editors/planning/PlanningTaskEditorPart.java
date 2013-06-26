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
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.SWT;
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
				Section scopeSection = toolkit.createSection(scopeListComp, ExpandableComposite.TITLE_BAR
						| Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
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
				scopeSection.setText(titleBuilder.toString());
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

				Action a = new Action(MylynAgileUIMessages
						.getString("PlanningTaskEditorPart.EditScopeActionLabel"), PlatformUI.getWorkbench() //$NON-NLS-1$
						.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE)) {
					// TODO Implement action to open scope in its own editor
				};
				toolBarManager.add(a);
				toolBarManager.update(true);
				scopeSection.setTextClient(toolbar);
				createBacklogItemsTable(toolkit, scopeSection, scopeAtt, backlogItemTypeName);
			}
		}
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
		Table btable = toolkit.createTable(backlogSection, SWT.BORDER | SWT.MULTI);
		backlogSection.setClient(btable);
		GridData bgd = new GridData(GridData.FILL_BOTH);
		// gd.heightHint = 20;
		// gd.widthHint = 400;
		btable.setLayoutData(bgd);
		TableViewer bviewer = new TableViewer(btable);
		bviewer.setContentProvider(new BacklogItemListContentProvider());

		// Column "id"
		TableViewerColumn colId = new TableViewerColumn(bviewer, SWT.NONE);
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
		TableViewerColumn colLabel = new TableViewerColumn(bviewer, SWT.NONE);
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
		TableViewerColumn colPoints = new TableViewerColumn(bviewer, SWT.NONE);
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
		TableViewerColumn colParent = new TableViewerColumn(bviewer, SWT.NONE);
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

		bviewer.setInput(backlogItemList);
		btable.setHeaderVisible(true);
		btable.setLinesVisible(true);
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
}
