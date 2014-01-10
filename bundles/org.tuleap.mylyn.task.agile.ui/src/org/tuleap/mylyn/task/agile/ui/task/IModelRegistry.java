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
package org.tuleap.mylyn.task.agile.ui.task;

import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;

/**
 * Interface of a registry of {@link TaskDataModel} by {@link TaskEditor}. Useful to share a
 * {@link TaskDataModel} instance between pages in the Mylyn editor.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IModelRegistry {

	/**
	 * Register a shared {@link TaskDataModel} for a given editor.
	 * 
	 * @param editor
	 *            The editor
	 * @param taskDataModel
	 *            The model to share
	 */
	void registerModel(TaskEditor editor, TaskDataModel taskDataModel);

	/**
	 * Removes registered {@link TaskDataModel} for the given editor.
	 * 
	 * @param editor
	 *            The editor
	 */
	void deregisterModel(TaskEditor editor);

	/**
	 * Retrieve the registered model for the given editor, if there is one.
	 * 
	 * @param editor
	 *            The editor.
	 * @return The registered {@link TaskDataModel} or <code>null</code> if no model is registered with this
	 *         editor.
	 */
	TaskDataModel getRegisteredModel(TaskEditor editor);

	/**
	 * Register a save listener on an editor.
	 * 
	 * @param editor
	 *            The editor
	 * @param saveListener
	 *            The save listener
	 */
	void addSaveListener(TaskEditor editor, ISaveListener saveListener);

	/**
	 * Removes the given listener for the given editor.
	 * 
	 * @param editor
	 *            The editor
	 * @param saveListener
	 *            The save listener
	 */
	void removeSaveListener(TaskEditor editor, ISaveListener saveListener);

	/**
	 * Will notify save listeners that a save is about to happen.
	 * 
	 * @param editor
	 *            The editor in which a relevant page is about to be saved
	 */
	void fireBeforeSave(TaskEditor editor);

	/**
	 * Will notify save listeners that a save has taken place.
	 * 
	 * @param editor
	 *            The editor in which a relevant page has been saved
	 */
	void fireAfterSave(TaskEditor editor);
}
