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

import java.util.Iterator;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

/**
 * Drag listener for BacklogItem tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemDragListener implements DragSourceListener {

	/**
	 * The table fViewer to listen to.
	 */
	private TableViewer fViewer;

	/**
	 * Constructor, requires the table fViewer to listen to.
	 * 
	 * @param viewer
	 *            the table fViewer to listen to.
	 */
	BacklogItemDragListener(TableViewer viewer) {
		this.fViewer = viewer;
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
		if (event.detail == DND.DROP_MOVE) {
			TaskAttribute itemListAtt = (TaskAttribute)fViewer.getInput();
			// remove the dragged element(s) from the source
			IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection();
			for (Iterator<?> it = selection.iterator(); it.hasNext();) {
				TaskAttribute itemAtt = (TaskAttribute)it.next();
				itemListAtt.removeAttribute(itemAtt.getId());
			}
			// Recompute the dragged elements indexes
			int index = 0;
			for (TaskAttribute att : itemListAtt.getAttributes().values()) {
				if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(att.getMetaData().getType())) {
					att.setValue(String.valueOf(index++));
				}
			}
		}
		fViewer.refresh();
	}

}
