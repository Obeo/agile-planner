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
package org.tuleap.mylyn.task.agile.core.data.cardwall;

import java.util.Collections;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

/**
 * Card wall wrapper, encapsulates all the logic of writing and reading {@link TaskAttribute}s in a TaskData
 * and offers a usable API.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Separator to use to compute the mylyn id of a configurable field {@link TaskAttribute}.
	 */
	public static final String FIELD_SEPARATOR = "_field-"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task atribute that represents the column id.
	 */
	public static final String SUFFIX_STATUS_ID = "status_id"; //$NON-NLS-1$

	/**
	 * The parent card wall.
	 */
	private final SwimlaneWrapper parent;

	/**
	 * Constructor to use to wrap an existing instance.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that contains the columns
	 */
	protected CardWrapper(SwimlaneWrapper parent, TaskAttribute root) {
		super(root);
		this.parent = parent;
	}

	/**
	 * Constructor to use when creating a new instance.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that contains the columns
	 * @param id
	 *            The id of the column
	 */
	protected CardWrapper(SwimlaneWrapper parent, TaskAttribute root, String id) {
		super(root, id);
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		parent.fireAttributeChanged(attribute);
	}

	/**
	 * Computes the unique id of the Assigned status id attribute.
	 * 
	 * @return The unique id of the Assigned status id attribute.
	 */
	private String getStatusIdAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_STATUS_ID;
	}

	/**
	 * Computes the unique id of the Assigned status id attribute.
	 * 
	 * @param id
	 *            The if of the field.
	 * @return The unique id of the Assigned status id attribute.
	 */
	private String getFieldAttributeId(String id) {
		return root.getId() + FIELD_SEPARATOR + id;
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the status to which this card is assigned, or null if it is not assigned, which
	 *         should not happen in an ideal world.
	 */
	public String getStatusId() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getStatusIdAttributeId());
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}

	/**
	 * Assigned milestone id setter.
	 * 
	 * @param columnId
	 *            The assigned milestone id.
	 */
	public void setStatusId(String columnId) {
		TaskAttribute attribute = root.getMappedAttribute(getStatusIdAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createAttribute(getStatusIdAttributeId());
			attribute.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || !oldValue.equals(columnId)) {
			attribute.setValue(columnId);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Get the value of a configurable field by its id.
	 * 
	 * @param id
	 *            The if of the field.
	 * @return The value of the field with this id, or null if cannot be found.
	 */
	public String getFieldValue(String id) {
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	/**
	 * Get the values of a configurable field by its id.
	 * 
	 * @param id
	 *            The if of the field.
	 * @return The list of values, never null but possibly empty;
	 */
	public List<String> getFieldValues(String id) {
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute != null) {
			return attribute.getValues();
		}
		return Collections.emptyList();
	}

	/**
	 * Set a value to a configurable field, after creating the relevant {@link TaskAttribute} if necessary.
	 * 
	 * @param id
	 *            the id of the field.
	 * @param value
	 *            The value to set. If null, nothing is done.
	 */
	public void setFieldValue(String id, String value) {
		if (value == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getFieldAttributeId(id));
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue != null && !oldValue.equals(value)) {
			attribute.setValue(value);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Clears the value(s) of a configurable field.
	 * 
	 * @param id
	 *            the id of the field.
	 */
	public void clearFieldValues(String id) {
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute != null) {
			attribute.clearValues();
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Set a list of values to a configurable field, after creating the relevant {@link TaskAttribute} if
	 * necessary.
	 * 
	 * @param id
	 *            the id of the field.
	 * @param values
	 *            The values to set. If null or empty, nothing is done.
	 */
	public void setFieldValues(String id, List<String> values) {
		if (values == null || values.isEmpty()) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute == null) {
			attribute = root.createMappedAttribute(getFieldAttributeId(id));
		}
		attribute.setValues(values);
		// TODO Refine this
		fireAttributeChanged(attribute);
	}

	/**
	 * Add a value to a configurable field, after creating the relevant {@link TaskAttribute} if necessary.
	 * 
	 * @param id
	 *            the id of the field.
	 * @param value
	 *            The value to add. If null, nothing is done.
	 */
	public void addFieldValue(String id, String value) {
		if (value == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute == null) {
			attribute = root.createMappedAttribute(getFieldAttributeId(id));
		}
		attribute.addValue(value);
		fireAttributeChanged(attribute);
	}

	/**
	 * Add a list of values to a configurable field, after creating the relevant {@link TaskAttribute} if
	 * necessary.
	 * 
	 * @param id
	 *            the id of the field.
	 * @param values
	 *            The values to add. If null or empty, nothing is done.
	 */
	public void addFieldValues(String id, List<String> values) {
		if (values == null || values.isEmpty()) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute == null) {
			attribute = root.createMappedAttribute(getFieldAttributeId(id));
		}
		for (String value : values) {
			attribute.addValue(value);
		}
		fireAttributeChanged(attribute);
	}
}
