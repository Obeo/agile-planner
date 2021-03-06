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
package org.tuleap.mylyn.task.agile.core.data.planning;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

/**
 * Wrapper of a TaskAttribute that represents a Sub-milestone.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class SubMilestoneWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Id of the backlog items list task attribute.
	 */
	public static final String SUFFIX_MILESTONE_CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String TYPE_MILESTONE = "mta_milestone"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	public static final String PREFIX_MILESTONE = "mta_ms-"; //$NON-NLS-1$

	/**
	 * The suffix used to indicate that a task attribute represents a milestone duration.
	 */
	public static final String SUFFIX_END_DATE = "end_date"; //$NON-NLS-1$

	/**
	 * The suffix used to indicate that a task attribute represents a planned start date.
	 */
	public static final String SUFFIX_START_DATE = "start_date"; //$NON-NLS-1$

	/**
	 * The suffix used to indicate that a task attribute represents a sub-milestone status value (which is a
	 * free label without specific semantics attached).
	 */
	public static final String SUFFIX_STATUS_VALUE = "status_value"; //$NON-NLS-1$

	/**
	 * The suffix used to indicate that a task attribute represents a sub-milestone semantic status value.
	 */
	public static final String SUFFIX_STATUS = "status"; //$NON-NLS-1$

	/**
	 * Id of the reference sub-task attribute.
	 */
	public static final String REF = "ref"; //$NON-NLS-1$

	/**
	 * The parent planning.
	 */
	private final MilestonePlanningWrapper parent;

	/**
	 * Constructor to use to wrap an existing instance.
	 *
	 * @param parent
	 *            The parent planning
	 * @param id
	 *            The id of the sub-milestone.
	 */
	protected SubMilestoneWrapper(final MilestonePlanningWrapper parent, String id) {
		super(parent.getRoot(), PREFIX_MILESTONE, id);
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#getId()
	 */
	@Override
	public String getId() {
		// The wrapped attribute is used to persist the list of items in this milestone
		return attribute.getId().substring(PREFIX_MILESTONE.length());
	}

	/**
	 * Computes the unique id of the capacity attribute.
	 *
	 * @return The unique id of the capacity attribute.
	 */
	private String getCapacityAttributeId() {
		return getAttributeId(attribute, SUFFIX_MILESTONE_CAPACITY);
	}

	/**
	 * Computes the unique id of the duration attribute.
	 *
	 * @return The unique id of the duration attribute.
	 */
	private String getEndDateAttributeId() {
		return getAttributeId(attribute, SUFFIX_END_DATE);
	}

	/**
	 * Computes the unique id of the start date attribute.
	 *
	 * @return The unique id of the start date attribute.
	 */
	private String getStartDateAttributeId() {
		return getAttributeId(attribute, SUFFIX_START_DATE);
	}

	/**
	 * Computes the unique id of the status value attribute.
	 *
	 * @return The unique id of the status value attribute.
	 */
	private String getStatusValueAttributeId() {
		return getAttributeId(attribute, SUFFIX_STATUS_VALUE);
	}

	/**
	 * Computes the unique id of the semantic status value attribute.
	 *
	 * @return The unique id of the status value attribute.
	 */
	private String getStatusAttributeId() {
		return getAttributeId(attribute, SUFFIX_STATUS);
	}

	/**
	 * Capacity getter.
	 *
	 * @return The milestone's capacity, or {@code null} if not defined.
	 */
	public String getCapacity() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getCapacityAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Capacity setter.
	 *
	 * @param capacity
	 *            The milestone's capacity.
	 */
	public void setCapacity(String capacity) {
		if (capacity == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getCapacityAttributeId());
		String oldValue = null;
		if (att == null) {
			att = createAgileAttribute(getCapacityAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(capacity)) {
			att.setValue(capacity);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Start date getter.
	 *
	 * @return The milestone's start date, or {@code null} if not defined.
	 */
	public Date getStartDate() {
		Date result = null;
		TaskAttribute att = root.getMappedAttribute(getStartDateAttributeId());
		if (att != null) {
			TaskAttributeMapper mapper = att.getTaskData().getAttributeMapper();
			result = mapper.getDateValue(att);
		}
		return result;
	}

	/**
	 * Start date setter.
	 *
	 * @param start
	 *            The milestone's start date. Nothing happens if it is null, and the former, if any, start
	 *            date is left unchanged.
	 */
	public void setStartDate(Date start) {
		if (start == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getStartDateAttributeId());
		if (att == null) {
			att = createAgileAttribute(getStartDateAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		}
		TaskAttributeMapper mapper = att.getTaskData().getAttributeMapper();
		Date oldValue = mapper.getDateValue(att);
		if (oldValue == null || !oldValue.equals(start)) {
			mapper.setDateValue(att, start);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Duration getter.
	 *
	 * @return The milestone's duration, or {@code null} if not defined.
	 */
	public Date getEndDate() {
		Date result = null;
		TaskAttribute att = root.getMappedAttribute(getEndDateAttributeId());
		if (att != null) {
			TaskAttributeMapper mapper = att.getTaskData().getAttributeMapper();
			result = mapper.getDateValue(att);
		}
		return result;
	}

	/**
	 * Start date setter.
	 *
	 * @param start
	 *            The milestone's start date. Nothing happens if it is null, and the former, if any, start
	 *            date is left unchanged.
	 */
	public void setEndDate(Date start) {
		if (start == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getEndDateAttributeId());
		if (att == null) {
			att = createAgileAttribute(getEndDateAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		}
		TaskAttributeMapper mapper = att.getTaskData().getAttributeMapper();
		Date oldValue = mapper.getDateValue(att);
		if (oldValue == null || !oldValue.equals(start)) {
			mapper.setDateValue(att, start);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Creates a new task attribute to represent a BacklogItem and returns a wrapper for this new
	 * TaskAttribute. The created TaskAttribute is inserted in the given parent, that must be non-null.
	 *
	 * @param id
	 *            the backlogItem identifier
	 * @return A wrapper for a newly created TaskAttribute representing a BacklogItem in the given parent.
	 */
	public BacklogItemWrapper addBacklogItem(String id) {
		BacklogItemWrapper w = parent.wrapBacklogItem(id);
		attribute.addValue(id);
		return w;
	}

	/**
	 * Returns the list of backlog items that is the content of this sub-milestone.
	 *
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getOrderedBacklogItems() {
		List<BacklogItemWrapper> result = Lists.newArrayList();
		for (String attributeId : attribute.getValues()) {
			result.add(parent.wrapBacklogItem(attributeId));
		}
		return result;
	}

	/**
	 * Status label getter.
	 *
	 * @return The milestone's status, or {@code null} if not defined.
	 */
	public String getStatusValue() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getStatusValueAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Status value setter.
	 *
	 * @param statusValue
	 *            The milestone's status value (i.e. label).
	 */
	public void setStatusValue(String statusValue) {
		if (statusValue == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getStatusValueAttributeId());
		String oldValue = null;
		if (att == null) {
			att = createAgileAttribute(getStatusValueAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			att.getMetaData().setReadOnly(true);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(statusValue)) {
			att.setValue(statusValue);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Semantic milestone status getter.
	 *
	 * @return The milestone's semantic status (possible values= "Open" and "Closed") , or {@code null} if not
	 *         defined.
	 */
	public String getStatus() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getStatusAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Semantic milestone status value setter.
	 *
	 * @param statusValue
	 *            The milestone semantic status value (possible values= "Open" and "Closed") .
	 */
	public void setStatus(String statusValue) {
		if (statusValue == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getStatusAttributeId());
		String oldValue = null;
		if (att == null) {
			att = createAgileAttribute(getStatusAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			att.getMetaData().setReadOnly(true);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(statusValue)) {
			att.setValue(statusValue);
			fireAttributeChanged(att);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		parent.fireAttributeChanged(att);
	}

	/**
	 * Indicates whether the list of elements in the sub-milestone content has been changed locally, compared
	 * to the lates known repository version.
	 *
	 * @return <code>true</code> if the list of items in the backlog is different from the latest known remote
	 *         list.
	 */
	public boolean hasContentChanged() {
		TaskAttribute ref = attribute.getAttribute(REF);
		if (ref == null) {
			return false;
		}
		return !ref.getValues().equals(attribute.getValues());
	}

	/**
	 * Marks the backlog reference (the common ancestor for merge operations).
	 *
	 * @param values
	 *            the reference list of values.
	 */
	public void setBacklogReference(List<String> values) {
		TaskAttribute ref = getReferenceWrappedAttribute();
		if (ref == null) {
			ref = attribute.createAttribute(REF);
		}
		if (!values.equals(ref.getValues())) {
			ref.setValues(values);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Marks the backlog reference (the common ancestor for merge operations) with the current value if and
	 * only if no reference exists.
	 */
	public void markReference() {
		TaskAttribute ref = getReferenceWrappedAttribute();
		if (ref == null) {
			ref = attribute.createAttribute(REF);
			ref.setValues(attribute.getValues());
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Provides the wrapped attribute.
	 *
	 * @return The wrapped attribute, never null.
	 */
	public TaskAttribute getReferenceWrappedAttribute() {
		return attribute.getAttribute(REF);
	}
}
