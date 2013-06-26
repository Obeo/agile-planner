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

import java.util.ArrayList;
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

		// TaskAttribute summaryAtt = getTaskData().getRoot().getAttribute(TaskAttribute.SUMMARY);
		// if (summaryAtt != null) {
		// form.setText(summaryAtt.getValue());
		// }

		Composite body = form.getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite backlog = toolkit.createComposite(body);
		backlog.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		backlog.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Section backlogSection = toolkit.createSection(backlog, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION);
		backlogSection.setText("Backlog");
		backlogSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		backlogSection.setLayoutData(data);
		TaskAttribute backlogItemList = getTaskData().getRoot().getAttribute(
				IMylynAgileCoreConstants.BACKLOG_ITEM_LIST);
		Table btable = toolkit.createTable(backlogSection, SWT.NONE);
		backlogSection.setClient(btable);
		GridData bgd = new GridData(GridData.FILL_BOTH);
		// gd.heightHint = 20;
		// gd.widthHint = 400;
		btable.setLayoutData(bgd);
		TableViewer bviewer = new TableViewer(btable);
		bviewer.setContentProvider(new BacklogItemListContentProvider());
		TableViewerColumn bcol = new TableViewerColumn(bviewer, SWT.NONE);
		bcol.getColumn().setText("Id");
		bcol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof TaskAttribute) {
					return ((TaskAttribute)element).getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME)
							.getValue();
				}
				return element == null ? "N/A" : element.toString();
			}
		});
		bviewer.setInput(backlogItemList);
		btable.setHeaderVisible(true);
		btable.setLinesVisible(true);

		Composite scopeList = toolkit.createComposite(body);
		scopeList.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		scopeList.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		TaskAttribute scopeListAtt = getTaskData().getRoot()
				.getAttribute(IMylynAgileCoreConstants.SCOPE_LIST);
		for (TaskAttribute scopeAtt : scopeListAtt.getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_SCOPE.equals(scopeAtt.getMetaData().getType())) {
				Section scopeSection = toolkit.createSection(scopeList, ExpandableComposite.TITLE_BAR
						| Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
				TaskAttribute nameAtt = scopeAtt.getAttribute(IMylynAgileCoreConstants.SCOPE_NAME);
				scopeSection.setText(nameAtt == null ? "N/A" : nameAtt.getValue());
				scopeSection.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
				TableWrapData scopeLayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
				scopeSection.setLayoutData(scopeLayoutData);
				ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
				ToolBar toolbar = toolBarManager.createControl(scopeSection);
				final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
				toolbar.setCursor(handCursor);
				// Cursor needs to be explicitly disposed
				toolbar.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						handCursor.dispose();
					}
				});
				// Add sort action to the tool bar

				Action a = new Action("Edit", PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJ_FILE)) {
					// TODO Implement action to open scope in its own editor
				};
				toolBarManager.add(a);
				toolBarManager.update(true);
				scopeSection.setTextClient(toolbar);
				Table table = toolkit.createTable(scopeSection, SWT.MULTI | SWT.BORDER);
				GridData gd = new GridData(GridData.FILL_BOTH);
				// gd.heightHint = 20;
				// gd.widthHint = 400;
				table.setLayoutData(gd);
				TableViewer viewer = new TableViewer(table);
				viewer.setContentProvider(new BacklogItemListContentProvider());
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
				col.getColumn().setText("Name");
				col.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						if (element instanceof TaskAttribute) {
							String label = ((TaskAttribute)element).getAttribute(
									IMylynAgileCoreConstants.BACKLOG_ITEM_NAME).getValue();
							return label;
						}
						return element == null ? "N/A" : element.toString();
					}
				});
				col.getColumn().pack();
				scopeSection.setClient(table);
				viewer.setInput(scopeAtt);
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
			}
		}
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
