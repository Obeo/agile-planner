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
package org.tuleap.mylyn.task.internal.agile.ui.task;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;

/**
 * Default implementation of {@link IModelRegistry}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ModelRegistry implements IModelRegistry {

	/**
	 * Map of {@link TaskDataModel} by editor.
	 */
	private final Map<TaskEditor, TaskDataModel> modelsByEditor = Maps.newHashMap();

	/**
	 * Register a shared {@link TaskDataModel} for a given editor.
	 * 
	 * @param editor
	 *            The editor
	 * @param taskDataModel
	 *            The model to share
	 */
	@Override
	public void registerModel(TaskEditor editor, TaskDataModel taskDataModel) {
		Assert.isNotNull(editor);
		Assert.isNotNull(taskDataModel);
		modelsByEditor.put(editor, taskDataModel);
	}

	/**
	 * Removes registered {@link TaskDataModel} for the given editor.
	 * 
	 * @param editor
	 *            The editor
	 */
	@Override
	public void deregisterModel(TaskEditor editor) {
		modelsByEditor.remove(editor);
	}

	/**
	 * Retrieve the registered model for the given editor, if there is one.
	 * 
	 * @param editor
	 *            The editor.
	 * @return The registered {@link TaskDataModel} or <code>null</code> if no model is registered with this
	 *         editor.
	 */
	@Override
	public TaskDataModel getRegisteredModel(TaskEditor editor) {
		return modelsByEditor.get(editor);
	}

}
