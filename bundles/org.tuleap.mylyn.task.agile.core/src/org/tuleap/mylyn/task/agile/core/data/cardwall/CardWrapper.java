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

	/**
	 * Separator to use to compute the mylyn id of a configurable field {@link TaskAttribute}.
	 */
	public static final String FIELD_SEPARATOR = "f-"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task attribute that represents the column id.
	 */
	public static final String SUFFIX_COLUMN_ID = "col_id"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task attribute that represents the card's artifact id.
	 */
	public static final String SUFFIX_ARTIFACT_ID = "art_id"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task attribute that represents the status.
	 */
	public static final String SUFFIX_STATUS = "status"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task attribute that represents the color.
	 */
	public static final String SUFFIX_COLOR = "color"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task attribute that represents the status.
	 */
	public static final String ALLOWED_COLS = "allowed_cols"; //$NON-NLS-1$

	/**
	 * The parent card wall.
	 */
	private final SwimlaneWrapper parent;

	/**
	 * Constructor to use to wrap an existing instance.
	 * 
	 * @param parent
	 *            The parent.
	 * @param cardAtt
	 *            The {@link TaskAttribute} that represents the card
	 */
	protected CardWrapper(SwimlaneWrapper parent, TaskAttribute cardAtt) {
		super(parent.getRoot(), parent.getCardPrefix(), cardAtt.getValue());
		this.parent = parent;
	}

	/**
	 * Constructor to use when creating a new instance.
	 * 
	 * @param parent
	 *            The parent.
	 * @param id
	 *            The id of the column
	 */
	protected CardWrapper(SwimlaneWrapper parent, String id) {
		super(parent.getRoot(), parent.getCardPrefix(), id);
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		parent.fireAttributeChanged(att);
		super.fireAttributeChanged(att);
	}

	/**
	 * Computes the unique id of the Assigned column id attribute.
	 * 
	 * @return The unique id of the Assigned column id attribute.
	 */
	private String getColumnIdAttributeId() {
		return getAttributeId(attribute, SUFFIX_COLUMN_ID);
	}

	/**
	 * Computes the unique id of the Assigned column id attribute.
	 * 
	 * @return The unique id of the Assigned column id attribute.
	 */
	private String getArtifactIdAttributeId() {
		return getAttributeId(attribute, SUFFIX_ARTIFACT_ID);
	}

	/**
	 * Computes the unique id of the Assigned status attribute.
	 * 
	 * @return The unique id of the Assigned status attribute.
	 */
	private String getStatusAttributeId() {
		return getAttributeId(attribute, SUFFIX_STATUS);
	}

	/**
	 * Computes the unique id of the color attribute.
	 * 
	 * @return The unique id of the color attribute.
	 */
	private String getColorAttributeId() {
		return getAttributeId(attribute, SUFFIX_COLOR);
	}

	/**
	 * Computes the unique id of the Assigned status attribute.
	 * 
	 * @return The unique id of the Assigned status attribute.
	 */
	private String getAllowedColumnAttributeId() {
		return getAttributeId(attribute, ALLOWED_COLS);
	}

	/**
	 * Computes the unique id of the Assigned column id attribute.
	 * 
	 * @param id
	 *            The id of the field.
	 * @return The unique id of the Assigned column id attribute.
	 */
	private String getFieldAttributeId(String id) {
		return getAttributeId(attribute, FIELD_SEPARATOR + id);
	}

	/**
	 * Returns all the configurable fields {@link TaskAttribute}s.
	 * 
	 * @return all the existing configurable fields {@link TaskAttribute}s.
	 */
	public List<TaskAttribute> getFieldAttributes() {
		List<TaskAttribute> res = new ArrayList<TaskAttribute>();
		String prefix = getAttributeId(attribute, FIELD_SEPARATOR);
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
		return root.getAttribute(getFieldAttributeId(id));
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
		TaskAttribute att = root.createAttribute(getFieldAttributeId(id));
		att.getMetaData().setLabel(label);
		att.getMetaData().setType(type);
		return att;
	}

	/**
	 * Returns the field id of a given {@link TaskAttribute} if it represents a field that belongs to this
	 * card.
	 * 
	 * @param att
	 *            The attribute, should be one fo the card's fields.
	 * @return The external field ID, or null if the given attribute does not belong to this card.
	 */
	public String getFieldId(TaskAttribute att) {
		String attId = att.getId();
		String prefix = getAttributeId(attribute, FIELD_SEPARATOR);
		if (attId.startsWith(prefix) && root.getAttributes().containsKey(attId)) {
			return att.getId().substring(prefix.length());
		}
		return null;
	}

	/**
	 * Status getter.
	 * 
	 * @return The status of the card, or <code>false</code> if it is not set.
	 */
	public boolean isComplete() {
		boolean result = false;
		TaskAttribute att = root.getAttribute(getStatusAttributeId());
		if (att != null) {
			result = Boolean.parseBoolean(att.getValue());
		}
		return result;
	}

	/**
	 * Complete setter.
	 * 
	 * @param complete
	 *            The status.
	 */
	public void setComplete(boolean complete) {
		TaskAttribute att = root.getAttribute(getStatusAttributeId());
		String oldValue = null;
		if (att == null) {
			att = root.createAttribute(getStatusAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_BOOLEAN);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || oldValue != Boolean.toString(complete)) {
			att.setValue(Boolean.toString(complete));
			fireAttributeChanged(att);
		}
	}

	/**
	 * Color getter.
	 * 
	 * @return The color of the card, or null if it is not set.
	 */
	public String getAccentColor() {
		String result = null;
		TaskAttribute att = root.getAttribute(getColorAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Color setter.
	 * 
	 * @param color
	 *            The color.
	 */
	public void setAccentColor(String color) {
		if (color == null) {
			return;
		}
		TaskAttribute att = root.getAttribute(getColorAttributeId());
		if (att == null) {
			att = root.createAttribute(getColorAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		}
		att.setValue(color); // No need to notify color changes, not editable!
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the column to which this card is assigned, or null if it is not assigned, which
	 *         should not happen in an ideal world.
	 */
	public String getColumnId() {
		String result = null;
		TaskAttribute att = root.getAttribute(getColumnIdAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Assigned milestone id setter.
	 * 
	 * @param columnId
	 *            The assigned milestone id. No effect if columnId is null.
	 */
	public void setColumnId(String columnId) {
		if (columnId == null) {
			return;
		}
		TaskAttribute att = root.getAttribute(getColumnIdAttributeId());
		String oldValue = null;
		if (att == null) {
			att = root.createAttribute(getColumnIdAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(columnId)) {
			att.setValue(columnId);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the column to which this card is assigned, or null if it is not assigned, which
	 *         should not happen in an ideal world.
	 */
	public String getArtifactId() {
		String result = null;
		TaskAttribute att = root.getAttribute(getArtifactIdAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Assigned milestone id setter.
	 * 
	 * @param artifactId
	 *            The assigned milestone id. No effect if artifactId is null.
	 */
	public void setArtifactId(String artifactId) {
		if (artifactId == null) {
			return;
		}
		TaskAttribute att = root.getAttribute(getArtifactIdAttributeId());
		String oldValue = null;
		if (att == null) {
			att = root.createAttribute(getArtifactIdAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(artifactId)) {
			att.setValue(artifactId);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Get the values of the allowed column ids.
	 * 
	 * @return The list of allowed column identifiers. Never null but possibly empty.
	 */
	public List<String> getAllowedColumnIds() {
		TaskAttribute att = root.getAttribute(getAllowedColumnAttributeId());
		if (att != null) {
			return att.getValues();
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
		TaskAttribute att = root.getAttribute(getAllowedColumnAttributeId());
		if (att == null) {
			att = root.createAttribute(getAllowedColumnAttributeId());
		}
		att.addValue(columnId);
		fireAttributeChanged(att);
	}

	/**
	 * Get the value of a configurable field by its id.
	 * 
	 * @param id
	 *            The if of the field.
	 * @return The value of the field with this id, or null if cannot be found.
	 */
	public String getFieldLabel(String id) {
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att != null) {
			return att.getMetaData().getLabel();
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att != null) {
			return att.getValue();
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att != null) {
			return att.getValues();
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att == null) {
			att = root.createAttribute(getFieldAttributeId(id));
		}
		att.getMetaData().setLabel(label);
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		String oldValue = null;
		if (att == null) {
			att = root.createAttribute(getFieldAttributeId(id));
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(value)) {
			att.setValue(value);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Clears the value(s) of a configurable field.
	 * 
	 * @param id
	 *            the id of the field.
	 */
	public void clearFieldValues(String id) {
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att != null) {
			att.clearValues();
			fireAttributeChanged(att);
		}
	}

	/**
	 * Clears the allowed column ids of a card.
	 */
	public void clearAllowedColumnIds() {
		TaskAttribute att = root.getAttribute(getAllowedColumnAttributeId());
		if (att != null) {
			att.clearValues();
			fireAttributeChanged(att);
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att == null) {
			att = root.createAttribute(getFieldAttributeId(id));
		}
		att.setValues(values);
		// TODO Refine this
		fireAttributeChanged(att);
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att == null) {
			att = root.createAttribute(getFieldAttributeId(id));
		}
		att.addValue(value);
		fireAttributeChanged(att);
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
		TaskAttribute att = root.getAttribute(getFieldAttributeId(id));
		if (att == null) {
			att = root.createAttribute(getFieldAttributeId(id));
		}
		for (String value : values) {
			att.addValue(value);
		}
		fireAttributeChanged(att);
	}

	// TODO add a method that permits managing the AttachementFieldValues
}
