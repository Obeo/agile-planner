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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Composite;

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
	 * Constructor, requires the table viewer to listen to.
	 *
	 * @param viewer
	 *            the table viewer to listen to.
	 * @param milestoneSectionViewer
	 *            The milestone section viewer, to update the section's UI after a drag operation.
	 * @param part
	 *            The part to refresh when drop is performed
	 */
	public MilestoneDragListener(TableViewer viewer, MilestoneSectionViewer milestoneSectionViewer,
			Composite part) {
		super(viewer, part);
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
		fMilestoneSectionViewer.refresh();
	}

}
