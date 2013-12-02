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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper;

/**
 * Card wall wrapper, encapsulates all the logic of writing and reading {@link TaskAttribute}s in a TaskData
 * and offers a usable API.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardWrapper extends AbstractNotifyingWrapper {

	// BEGIN Just used for tests with connector mock. TODO: We have to know the id of the configurable fields
	// to display them.

	/**
	 * The id of the value field of a card.
	 */
	public static final String CARD_VALUE_FIELD_ID = "1000"; //$NON-NLS-1$

	/**
	 * The id of the value field of a card.
	 */
	public static final String CARD_REMAINING_EFFORT_FIELD_ID = "2000"; //$NON-NLS-1$

	/**
	 * The id of the "assigned to" field of a card.
	 */
	public static final String CARD_ASSIGNED_TO_FIELD_ID = "3000"; //$NON-NLS-1$

	// END

	/**
	 * Separator to use to compute the mylyn id of a configurable field {@link TaskAttribute}.
	 */
	public static final String FIELD_SEPARATOR = "_field-"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task atribute that represents the column id.
	 */
	public static final String SUFFIX_COLUMN_ID = "column_id"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task atribute that represents the status.
	 */
	public static final String SUFFIX_STATUS = "status"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task atribute that represents the status.
	 */
	public static final String ALLOWED_COLS = "allowed_columns_ids"; //$NON-NLS-1$

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
		super.fireAttributeChanged(attribute);
	}

	/**
	 * Computes the unique id of the Assigned column id attribute.
	 * 
	 * @return The unique id of the Assigned column id attribute.
	 */
	private String getColumnIdAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_COLUMN_ID;
	}

	/**
	 * Computes the unique id of the Assigned status attribute.
	 * 
	 * @return The unique id of the Assigned status attribute.
	 */
	private String getStatusAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_STATUS;
	}

	/**
	 * Computes the unique id of the Assigned status attribute.
	 * 
	 * @return The unique id of the Assigned status attribute.
	 */
	private String getAllowedColumnAttributeId() {
		return root.getId() + ID_SEPARATOR + ALLOWED_COLS;
	}

	/**
	 * Computes the unique id of the Assigned column id attribute.
	 * 
	 * @param id
	 *            The id of the field.
	 * @return The unique id of the Assigned column id attribute.
	 */
	private String getFieldAttributeId(String id) {
		return getFieldAttributePrefix() + id;
	}

	/**
	 * Computes the prefix to use for task attributes representing configurable fields.
	 * 
	 * @return The prefix to use for task attributes representing configurable fields.
	 */
	private String getFieldAttributePrefix() {
		return root.getId() + FIELD_SEPARATOR;
	}

	/**
	 * Returns all the configurable fields {@link TaskAttribute}s.
	 * 
	 * @return all the existing configurable fields {@link TaskAttribute}s.
	 */
	public List<TaskAttribute> getFieldAttributes() {
		List<TaskAttribute> res = new ArrayList<TaskAttribute>();
		String prefix = getFieldAttributePrefix();
		for (Entry<String, TaskAttribute> entry : root.getAttributes().entrySet()) {
			if (entry.getKey().startsWith(prefix)) {
				res.add(entry.getValue());
			}
		}
		return res;
	}

	/**
	 * Provides the {@link TaskAttribute} of a configurable field contained by this card.
	 * 
	 * @param id
	 *            ID of the field (without the card prefix)
	 * @return The field {@link TaskAttribute}, or null if it does not exist.
	 */
	public TaskAttribute getFieldAttribute(String id) {
		return root.getMappedAttribute(getFieldAttributeId(id));
	}

	/**
	 * Add a configurable field by creating the relevant {@link TaskAttribute} as a child of this card's
	 * {@link TaskAttribute}.
	 * 
	 * @param id
	 *            The id of the field.
	 * @param label
	 *            The label of the field.
	 * @param type
	 *            The type of the field.
	 * @return The newly created {@link TaskAttribute}.
	 */
	public TaskAttribute addField(String id, String label, String type) {
		TaskAttribute attribute = root.createMappedAttribute(getFieldAttributeId(id));
		attribute.getMetaData().setLabel(label);
		attribute.getMetaData().setType(type);
		return attribute;
	}

	/**
	 * Returns the field id of a given {@link TaskAttribute} if it represents a field that belongs to this
	 * card.
	 * 
	 * @param attribute
	 *            The attribute, should be one fo the card's fields.
	 * @return The external field ID, or null if the given attribute does not belong to this card.
	 */
	public String getFieldId(TaskAttribute attribute) {
		String attId = attribute.getId();
		if (attId.startsWith(getFieldAttributePrefix()) && root.getAttributes().containsKey(attId)) {
			return attribute.getId().substring(getFieldAttributePrefix().length());
		}
		return null;
	}

	/**
	 * Status getter.
	 * 
	 * @return The status of the card, or null if it is not set, which should not happen in an ideal world.
	 */
	public String getStatus() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getStatusAttributeId());
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}

	/**
	 * Status setter.
	 * 
	 * @param status
	 *            The status.
	 */
	public void setStatus(String status) {
		TaskAttribute attribute = root.getMappedAttribute(getStatusAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createAttribute(getStatusAttributeId());
			attribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || !oldValue.equals(status)) {
			attribute.setValue(status);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the column to which this card is assigned, or null if it is not assigned, which
	 *         should not happen in an ideal world.
	 */
	public String getColumnId() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getColumnIdAttributeId());
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
	public void setColumnId(String columnId) {
		TaskAttribute attribute = root.getMappedAttribute(getColumnIdAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createAttribute(getColumnIdAttributeId());
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
	 * Get the values of the allowed column ids.
	 * 
	 * @return The list of allowed colum identifiers
	 */
	public List<String> getAllowedColumnIds() {
		TaskAttribute attribute = root.getMappedAttribute(getAllowedColumnAttributeId());
		if (attribute != null) {
			return attribute.getValues();
		}
		return Collections.emptyList();
	}

	/**
	 * Add an allowed column id, after creating the relevant {@link TaskAttribute} if necessary.
	 * 
	 * @param columnId
	 *            The column id to add.
	 */
	public void addAllowedColumn(String columnId) {
		if (columnId == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getAllowedColumnAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getAllowedColumnAttributeId());
		}
		attribute.addValue(columnId);
		fireAttributeChanged(attribute);
	}

	/**
	 * Get the value of a configurable field by its id.
	 * 
	 * @param id
	 *            The if of the field.
	 * @return The value of the field with this id, or null if cannot be found.
	 */
	public String getFieldLabel(String id) {
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute != null) {
			return attribute.getMetaData().getLabel();
		}
		return null;
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
	 * Set a label of a configurable field, after creating the relevant {@link TaskAttribute} if necessary.
	 * 
	 * @param id
	 *            the id of the field.
	 * @param label
	 *            The label to set. If null, nothing is done.
	 */
	public void setFieldLabel(String id, String label) {
		if (label == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getFieldAttributeId(id));
		if (attribute == null) {
			attribute = root.createMappedAttribute(getFieldAttributeId(id));
		}
		attribute.getMetaData().setLabel(label);
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
		if (oldValue == null || !oldValue.equals(value)) {
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
	 * Clears the allowed column ids of a card.
	 */
	public void clearAllowedColumnIds() {
		TaskAttribute attribute = root.getMappedAttribute(getAllowedColumnAttributeId());
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

	// TODO add a method that permits managing the AttachementFieldValues
}
