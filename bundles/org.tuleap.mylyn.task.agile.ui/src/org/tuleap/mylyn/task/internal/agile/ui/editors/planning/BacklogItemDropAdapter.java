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
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

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
		if (target instanceof BacklogItemWrapper) {
			BacklogItemWrapper bi = (BacklogItemWrapper)target;
			ret = performDropOnBacklogItem(selection, bi);
		} else if (target == null) {
			// Drop on the whole list
			ret = performDropOnContainer(selection);
		}
		if (ret) {
			getViewer().refresh();
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
		IBacklog container = (IBacklog)getViewer().getInput();
		boolean mustUpdate;
		boolean before = false;
		switch (getCurrentLocation()) {
			case LOCATION_AFTER:
				mustUpdate = true;
				break;
			case LOCATION_NONE:
				mustUpdate = false;
				break;
			default: // ON or BEFORE
				mustUpdate = true;
				before = true;
				break;
		}
		if (mustUpdate) {
			// Only manage BacklogItemWrappers, nothing else
			List<BacklogItemWrapper> selectedBacklogItems = Lists.newArrayList();
			for (Iterator<?> it = selection.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof BacklogItemWrapper) {
					selectedBacklogItems.add((BacklogItemWrapper)o);
				}
			}
			if (selectedBacklogItems.isEmpty()) {
				ret = false;
			} else {
				String milestoneId = container.getMilestoneId();
				MilestonePlanningWrapper planningWrapper = container.getMilestonePlanning();
				if (milestoneId == null) {
					// Drop on planning's backlog
					planningWrapper.moveItems(selectedBacklogItems, wrapper, before, null);
				} else {
					// Drop on a milestone's backlog item
					planningWrapper.moveItems(selectedBacklogItems, wrapper, before, planningWrapper
							.getSubMilestone(milestoneId));
				}
				ret = true;
			}
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
		// Only manage BacklogItemWrappers, nothing else
		List<BacklogItemWrapper> selectedBacklogItems = Lists.newArrayList();
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof BacklogItemWrapper) {
				selectedBacklogItems.add((BacklogItemWrapper)o);
			}
		}
		if (selectedBacklogItems.isEmpty()) {
			return false;
		}
		boolean ret = false;
		IBacklog container = (IBacklog)getViewer().getInput();
		Iterable<BacklogItemWrapper> biWrappers = container.getBacklogItems();
		BacklogItemWrapper lastBacklogItem = null;
		if (biWrappers.iterator().hasNext()) {
			lastBacklogItem = Iterables.getLast(biWrappers);
		}
		String targetAssignedMilestoneId = container.getMilestoneId();
		if (targetAssignedMilestoneId == null) {
			// Move to the backlog
			container.getMilestonePlanning().moveItems(selectedBacklogItems, lastBacklogItem, false, null);
			ret = true;
		} else {
			SubMilestoneWrapper targetMilestone = container.getMilestonePlanning().getSubMilestone(
					targetAssignedMilestoneId);
			container.getMilestonePlanning().moveItems(selectedBacklogItems, lastBacklogItem, false,
					targetMilestone);
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
			IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer()
					.getSelection();
			if (selection != null
					&& (selection.isEmpty() || !(selection.getFirstElement() instanceof BacklogItemWrapper))) {
				ret = false;
			} else if (target == null) {
				// empty table, or drop above empty line or table header
				ret = true;
			} else if (target instanceof BacklogItemWrapper) {
				ret = true;
			}
		}
		return ret;
	}
}
