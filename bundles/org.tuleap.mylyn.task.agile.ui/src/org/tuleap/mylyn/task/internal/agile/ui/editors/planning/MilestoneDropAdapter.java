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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.swt.widgets.Control;

/**
 * Drop Listener for BacklogItem tables.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestoneDropAdapter extends BacklogItemDropAdapter {

	/**
	 * The milestone section viewer, use to update after a drop operation.
	 */
	private final MilestoneSectionViewer fMilestoneSectionViewer;

	/**
	 * Constructor, requires a viewer. Delegates to the parent class.
	 * 
	 * @param viewer
	 *            The viewer.
	 * @param model
	 *            The TaskdataModel to use.
	 * @param milestoneSectionViewer
	 *            The milestone section viewer, top use to update the section's UI after a drag or drop
	 *            operation.
	 */
	public MilestoneDropAdapter(Viewer viewer, TaskDataModel model,
			MilestoneSectionViewer milestoneSectionViewer) {
		super(viewer, model);
		fMilestoneSectionViewer = milestoneSectionViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	@Override
	public boolean performDrop(Object data) {
		boolean ret = super.performDrop(data);
		if (ret) {
			Control section = fMilestoneSectionViewer.getControl();
			// We need to layout the whole right part
			section.getParent().getParent().getParent().layout();
			fMilestoneSectionViewer.refresh();
		}
		return ret;
	}
}
