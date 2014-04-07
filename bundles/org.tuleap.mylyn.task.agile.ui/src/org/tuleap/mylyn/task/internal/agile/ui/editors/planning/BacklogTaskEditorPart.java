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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.internal.agile.ui.editors.FormLayoutFactory;
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
