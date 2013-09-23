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

import com.google.common.collect.Iterables;

import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.swt.dnd.TransferData;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
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
		if (target instanceof BacklogItemWrapper) {
			BacklogItemWrapper bi = (BacklogItemWrapper)target;
			ret = performDropOnBacklogItem(selection, bi);
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
	 * @param wrapper
	 *            The target attribute wrapper.
	 * @return {@code true} if the drop has been performed from an external list, and {@code false} if the
	 *         drop has not been performed or has been performed but was only inside of the list.
	 */
	private boolean performDropOnBacklogItem(IStructuredSelection selection, BacklogItemWrapper wrapper) {
		boolean ret = false;
		IBacklogItemContainer container = (IBacklogItemContainer)getViewer().getInput();
		boolean mustUpdate;
		boolean before;
		switch (getCurrentLocation()) {
			case LOCATION_AFTER:
				mustUpdate = true;
				before = false;
				break;
			case LOCATION_NONE:
				mustUpdate = false;
				before = true;
				break;
			default: // ON or BEFORE
				mustUpdate = true;
				before = true;
				break;
		}
		if (mustUpdate) {
			Integer milestoneId = wrapper.getAssignedMilestoneId();
			MilestonePlanningWrapper planningWrapper = container.getMilestonePlanning();
			if (milestoneId == null) {
				planningWrapper.moveItemsToBacklog(selection.toList(), wrapper, before);
			} else {
				planningWrapper.moveItemsToMilestone(selection.toList(), wrapper, before, planningWrapper
						.getSubMilestone(milestoneId.intValue()));
			}
			// TODO getModel().attributeChanged(listAtt);
			getViewer().refresh();
		}
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
		@SuppressWarnings("unchecked")
		List<BacklogItemWrapper> selectedBacklogItems = selection.toList();
		if (selectedBacklogItems.isEmpty()) {
			return false;
		}
		boolean ret = false;
		IBacklogItemContainer container = (IBacklogItemContainer)getViewer().getInput();
		Iterable<BacklogItemWrapper> biWrappers = container.getBacklogItems();
		BacklogItemWrapper lastBacklogItem = Iterables.getLast(biWrappers);
		Integer targetAssignedMilestoneId = lastBacklogItem.getAssignedMilestoneId();

		BacklogItemWrapper firstSelectedElement = selectedBacklogItems.get(0);
		Integer selectedAssignedMilestoneId = firstSelectedElement.getAssignedMilestoneId();
		if (targetAssignedMilestoneId == null) {
			// Move to the backlog
			if (selectedAssignedMilestoneId != null) {
				container.getMilestonePlanning().moveItemsToBacklog(selectedBacklogItems, lastBacklogItem,
						false);
				ret = true;
			}
		} else if (!targetAssignedMilestoneId.equals(selectedAssignedMilestoneId)) {
			SubMilestoneWrapper targetMilestone = container.getMilestonePlanning().getSubMilestone(
					targetAssignedMilestoneId.intValue());
			container.getMilestonePlanning().moveItemsToMilestone(selectedBacklogItems, lastBacklogItem,
					false, targetMilestone);
			ret = true;
		}
		return ret;
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
			} else if (target instanceof BacklogItemWrapper) {
				ret = true;
			}
		}
		return ret;
	}
}
