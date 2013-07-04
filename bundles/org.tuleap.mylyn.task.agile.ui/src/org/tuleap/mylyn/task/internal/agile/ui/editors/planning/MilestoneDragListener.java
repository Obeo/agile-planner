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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Drag listener for Milestone section tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestoneDragListener extends BacklogItemDragListener {

	/**
	 * The milestone section fViewer to refresh after drag operations.
	 */
	private MilestoneSectionViewer fMilestoneSectionViewer;

	/**
	 * Constructor, requires the table fViewer to listen to.
	 * 
	 * @param viewer
	 *            the table fViewer to listen to.
	 * @param model
	 *            The task data model to use.
	 * @param milestoneSectionViewer
	 *            The milestone section viewer, top use to update the section's UI after a drag operation.
	 */
	public MilestoneDragListener(TableViewer viewer, TaskDataModel model,
			MilestoneSectionViewer milestoneSectionViewer) {
		super(viewer, model);
		fMilestoneSectionViewer = milestoneSectionViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
		super.dragFinished(event);
		Section section = (Section)fMilestoneSectionViewer.getControl();
		// We need to layout the whole right part
		section.getParent().getParent().getParent().layout();
		fMilestoneSectionViewer.refresh();
	}

}
