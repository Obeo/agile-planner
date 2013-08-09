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
package org.tuleap.mylyn.task.agile.core.data;

import org.eclipse.mylyn.tasks.core.data.AbstractTaskSchema.Field;
import org.eclipse.mylyn.tasks.core.data.DefaultTaskSchema;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * Common superclass of all the task mappers used by the agile editors. This utility class has been created
 * because several utility operations and fields from the class TaskMapper are not visible to classes that
 * will extend it.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public abstract class AbstractTaskMapper {

	/**
	 * The task data.
	 */
	protected final TaskData taskData;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 */
	public AbstractTaskMapper(TaskData taskData) {
		this.taskData = taskData;
	}

	/**
	 * Returns the existing mapped attribute with the given id, or {@code null} if an attribute with this id
	 * doesn't exist.
	 * 
	 * @param attributeKey
	 *            The task attribute id
	 * @return The existing mapped attribute, or null if it doesn't exist (or is not mapped)
	 */
	protected TaskAttribute getMappedAttribute(String attributeKey) {
		return taskData.getRoot().getMappedAttribute(attributeKey);
	}

	/**
	 * Returns the existing mapped attribute with the given id, or {@code null} if an attribute with this id
	 * doesn't exist.
	 * 
	 * @param attributeKey
	 *            The task attribute id
	 * @return The existing mapped attribute, or null if it doesn't exist (or is not mapped)
	 */
	protected TaskAttribute getMappedAttribute(int attributeKey) {
		return taskData.getRoot().getMappedAttribute(String.valueOf(attributeKey));
	}

	/**
	 * Creates the attribute with the given key and the given type under the root of the task data.
	 * 
	 * @param attributeKey
	 *            The key of the attribute
	 * @param type
	 *            The type of the attribute
	 * @return The attribute with the given key and the given type under the root of the task data
	 */
	protected TaskAttribute createAttribute(String attributeKey, String type) {
		TaskAttribute attribute;
		Field field = DefaultTaskSchema.getField(attributeKey);
		if (field != null) {
			attribute = field.createAttribute(this.taskData.getRoot());
		} else {
			attribute = this.taskData.getRoot().createMappedAttribute(attributeKey);
			if (type != null) {
				attribute.getMetaData().defaults().setType(type);
			}
		}
		return attribute;
	}

	/**
	 * Returns the writeable attribute with the given key and the given type.
	 * <p>
	 * If an attribute with the given key does not exists, a new one will be created.
	 * </p>
	 * 
	 * @param attributeKey
	 *            The key of the attribute
	 * @param type
	 *            The type of the attribute
	 * @return The writeable attribute with the given key and the given type
	 */
	protected TaskAttribute getWriteableAttribute(String attributeKey, String type) {
		TaskAttribute attribute = this.taskData.getRoot().getMappedAttribute(attributeKey);
		if (attribute == null) {
			attribute = createAttribute(attributeKey, type);
		}
		return attribute;
	}

	/**
	 * Returns the task data used by the mapper.
	 * 
	 * @return The task data used by the mapper
	 */
	public TaskData getTaskData() {
		return this.taskData;
	}

}
