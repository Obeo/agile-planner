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
import org.eclipse.ui.forms.widgets.Section;

/**
 * Drag listener for Scope section tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ScopeDragListener extends BacklogItemDragListener {

	/**
	 * The scope section fViewer to refresh after drag operations.
	 */
	private ScopeSectionViewer fScopeSectionViewer;

	/**
	 * Constructor, requires the table fViewer to listen to.
	 * 
	 * @param viewer
	 *            the table fViewer to listen to.
	 * @param scopeSectionViewer
	 *            The scope section viewer, top use to update the section's UI after a drag operation.
	 */
	ScopeDragListener(TableViewer viewer, ScopeSectionViewer scopeSectionViewer) {
		super(viewer);
		fScopeSectionViewer = scopeSectionViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
		super.dragFinished(event);
		Section section = (Section)fScopeSectionViewer.getControl();
		// We need to layout the whole right part
		section.getParent().getParent().getParent().layout();
		fScopeSectionViewer.refresh();
	}

}
