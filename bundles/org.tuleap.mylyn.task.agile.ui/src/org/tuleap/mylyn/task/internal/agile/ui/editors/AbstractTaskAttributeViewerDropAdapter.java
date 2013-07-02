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
package org.tuleap.mylyn.task.internal.agile.ui.editors;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;

/**
 * Drop Adapter used for viewers mapped on models which are {@code TaskAttribute}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTaskAttributeViewerDropAdapter extends ViewerDropAdapter {

	/**
	 * The model to use to manage dirty state.
	 */
	private final TaskDataModel fModel;

	/**
	 * Constructor that receives the TaskDataModel to use.
	 * 
	 * @param viewer
	 *            The viewer.
	 * @param model
	 *            The TaskDataModel to use for "dirty-ness".
	 */
	public AbstractTaskAttributeViewerDropAdapter(Viewer viewer, TaskDataModel model) {
		super(viewer);
		fModel = model;
	}

	/**
	 * Provides access to the wrapped taskDataModel.
	 * 
	 * @return Return the TaskData Model wrappe dby this DropAdapter.
	 */
	public TaskDataModel getModel() {
		return fModel;
	}
}
