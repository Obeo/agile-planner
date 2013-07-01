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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.swt.dnd.TransferData;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;

/**
 * Drop Listener for BacklogItem tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemDropAdapter extends ViewerDropAdapter {

	/**
	 * Constructor, requires a viewer. Delegates to the parent class.
	 * 
	 * @param viewer
	 *            The viewer.
	 */
	public BacklogItemDropAdapter(Viewer viewer) {
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
				copySelectedElements(selection, listAtt, insertionIndex);
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
	 * Performs the copy of the selected elements at the given index in the target attribute.
	 * 
	 * @param selection
	 *            The selection of elements to move.
	 * @param listAtt
	 *            The parent attribute that receives the moved elements.
	 * @param insertionIndex
	 *            The insertion index of the moved elements
	 */
	private void copySelectedElements(IStructuredSelection selection, TaskAttribute listAtt,
			int insertionIndex) {
		List<TaskAttribute> elementsToMove = new ArrayList<TaskAttribute>();
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			Object next = it.next();
			if (next instanceof TaskAttribute) {
				elementsToMove.add((TaskAttribute)next);
			}
		}
		new TaskAttributeWrapper(listAtt).insertElementsSortedByValue(elementsToMove, insertionIndex,
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
