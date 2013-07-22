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
	protected TaskData taskData;

	/**
	 * Indicates if we can create non existing attributes in the task data if they do not exist.
	 */
	protected final boolean canCreateNonExistingAttributes;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param createNonExistingAttributes
	 *            Indicates if we should create new task data attributes if they do not exists.
	 */
	public AbstractTaskMapper(TaskData taskData, boolean createNonExistingAttributes) {
		this.taskData = taskData;
		this.canCreateNonExistingAttributes = createNonExistingAttributes;
	}

	/**
	 * Returns the writeable attribute with the given key and the given type.
	 * <p>
	 * If we can create non existing attribute (see constructor) and if an attribute with the given key does
	 * not exists, a new one will be created.
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
		if (this.canCreateNonExistingAttributes) {
			if (attribute == null) {
				attribute = createAttribute(attributeKey, type);
			}
		} else if (attribute != null && attribute.getMetaData().isReadOnly()) {
			return null;
		}
		return attribute;
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
	 * Returns the task data used by the mapper.
	 * 
	 * @return The task data used by the mapper
	 */
	public TaskData getTaskData() {
		return this.taskData;
	}

}
