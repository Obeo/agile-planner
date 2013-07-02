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
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.swt.dnd.TransferData;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.AbstractTaskAttributeViewerDropAdapter;

/**
 * Drop Listener for BacklogItem tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemDropAdapter extends AbstractTaskAttributeViewerDropAdapter {

	/**
	 * Constructor, requires a viewer. Delegates to the parent class.
	 * 
	 * @param viewer
	 *            The viewer.
	 * @param model
	 *            The task data model to use.
	 */
	public BacklogItemDropAdapter(Viewer viewer, TaskDataModel model) {
		super(viewer, model);
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
			if (isBacklogItem(targetAtt)) {
				ret = performDropOnBacklogItem(selection, targetAtt);
			} else {
				ret = false;
			}
		} else if (target == null) {
			// Drop on the whole list
			ret = performDropOnContainer(selection);
		}
		return ret;
	}

	/**
	 * Performs the drop on a backlogItem TaskAttribute.
	 * 
	 * @param selection
	 *            The current selection that contains the elements to drop.
	 * @param targetAtt
	 *            The target attribute that represents a backlog item.
	 * @return {@code true} if the drop has been performed from an external list, and {@code false} if the
	 *         drop has not been performed or has been performed but was only inside of the list.
	 */
	private boolean performDropOnBacklogItem(IStructuredSelection selection, TaskAttribute targetAtt) {
		boolean ret;
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
		getModel().attributeChanged(listAtt);
		getViewer().refresh();
		return ret;
	}

	/**
	 * Performs the drop on the table, adding elements at the end.
	 * 
	 * @param selection
	 *            The current selection that contains the elements to drop.
	 * @return {@code true} if the drop has been performed from an external list, and {@code false} if the
	 *         drop has not been performed or has been performed but was only inside of the list.
	 */
	private boolean performDropOnContainer(IStructuredSelection selection) {
		boolean ret;
		TaskAttribute containerAtt = (TaskAttribute)getViewer().getInput();
		int insertionIndex = new TaskAttributeWrapper(containerAtt)
				.countChildren(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		if (containerAtt == ((TaskAttribute)selection.getFirstElement()).getParentAttribute()) {
			// don't do anything, we don't support moving elements at the end of their container this way
			// moveSelectedElements(selection, listAtt, insertionIndex);
			ret = false;
		} else {
			// Drag'n drop from one list to another
			copySelectedElements(selection, containerAtt, insertionIndex);
			ret = true;
			getModel().attributeChanged(containerAtt);
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
		boolean ret = false;
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
			if (target == null) {
				// empty table, or drop above empty line or table header
				ret = true;
			} else if (target instanceof TaskAttribute) {
				TaskAttribute att = (TaskAttribute)target;
				if (isBacklogItem(att)) {
					ret = true;
				}
			}
		}
		return ret;
	}

	/**
	 * Indicates whether a task attribute is a backlog item.
	 * 
	 * @param att
	 *            The task attribute.
	 * @return {@code true} if and only if the type of {@code att} is {@link IMylynAgileCoreConstants} .
	 *         {@code TYPE_BACKLOG_ITEM}.
	 */
	protected boolean isBacklogItem(TaskAttribute att) {
		return IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(att.getMetaData().getType());
	}
}
