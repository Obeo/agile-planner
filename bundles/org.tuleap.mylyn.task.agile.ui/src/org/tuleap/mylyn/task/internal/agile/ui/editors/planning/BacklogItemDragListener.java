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

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Composite;

/**
 * Drag listener for BacklogItem tables.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemDragListener implements DragSourceListener {

	/**
	 * The part (to refresh when drop is performed).
	 */
	protected final Composite part;

	/**
	 * The table viewer to listen to.
	 */
	private final TableViewer fViewer;

	/**
	 * Constructor, requires the table viewer to listen to.
	 *
	 * @param viewer
	 *            the table viewer to listen to.
	 * @param part
	 *            The part
	 */
	public BacklogItemDragListener(TableViewer viewer, Composite part) {
		this.fViewer = viewer;
		this.part = part;
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
			IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection();
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
		fViewer.refresh();
		part.layout();
	}

}
