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
	 * The parent planning.
	 */
	private final MilestonePlanningWrapper parent;

	/**
	 * Constructor to use to wrap an existing instance.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a sub-milestone to wrap.
	 * @param id
	 *            The id of the sub-milestone.
	 */
	protected SubMilestoneWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root, String id) {
		super(root, id);
		this.parent = parent;
	}

	/**
	 * Constructor to use to wrap an existing instance.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a sub-milestone to wrap.
	 */
	protected SubMilestoneWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root) {
		super(root);
		this.parent = parent;
	}

	/**
	 * Computes the unique id of the capacity attribute.
	 * 
	 * @return The unique id of the capacity attribute.
	 */
	private String getCapacityAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_MILESTONE_CAPACITY;
	}

	/**
	 * Computes the unique id of the duration attribute.
	 * 
	 * @return The unique id of the duration attribute.
	 */
	private String getEndDateAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_END_DATE;
	}

	/**
	 * Computes the unique id of the start date attribute.
	 * 
	 * @return The unique id of the start date attribute.
	 */
	private String getStartDateAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_START_DATE;
	}

	/**
	 * Computes the unique id of the status value attribute.
	 * 
	 * @return The unique id of the status value attribute.
	 */
	private String getStatusValueAttributeId() {
		return root.getId() + ID_SEPARATOR + SUFFIX_STATUS_VALUE;
	}

	/**
	 * Capacity getter.
	 * 
	 * @return The milestone's capacity, or {@code null} if not defined.
	 */
	public Float getCapacity() {
		Float result = null;
		TaskAttribute attribute = root.getMappedAttribute(getCapacityAttributeId());
		if (attribute != null) {
			result = Float.valueOf(attribute.getValue());
		}
		return result;
	}

	/**
	 * Capacity setter.
	 * 
	 * @param capacity
	 *            The milestone's capacity.
	 */
	public void setCapacity(float capacity) {
		TaskAttribute attribute = root.getMappedAttribute(getCapacityAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getCapacityAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || Float.parseFloat(oldValue) != capacity) {
			attribute.setValue(Float.toString(capacity));
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Start date getter.
	 * 
	 * @return The milestone's start date, or {@code null} if not defined.
	 */
	public Date getStartDate() {
		Date result = null;
		TaskAttribute attribute = root.getMappedAttribute(getStartDateAttributeId());
		if (attribute != null) {
			TaskAttributeMapper mapper = attribute.getTaskData().getAttributeMapper();
			result = mapper.getDateValue(attribute);
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
		TaskAttribute attribute = root.getMappedAttribute(getStartDateAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getStartDateAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		}
		TaskAttributeMapper mapper = attribute.getTaskData().getAttributeMapper();
		Date oldValue = mapper.getDateValue(attribute);
		if (oldValue == null || !oldValue.equals(start)) {
			mapper.setDateValue(attribute, start);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Duration getter.
	 * 
	 * @return The milestone's duration, or {@code null} if not defined.
	 */
	public Date getEndDate() {
		Date result = null;
		TaskAttribute attribute = root.getMappedAttribute(getEndDateAttributeId());
		if (attribute != null) {
			TaskAttributeMapper mapper = attribute.getTaskData().getAttributeMapper();
			result = mapper.getDateValue(attribute);
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
		TaskAttribute attribute = root.getMappedAttribute(getEndDateAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getEndDateAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		}
		TaskAttributeMapper mapper = attribute.getTaskData().getAttributeMapper();
		Date oldValue = mapper.getDateValue(attribute);
		if (oldValue == null || !oldValue.equals(start)) {
			mapper.setDateValue(attribute, start);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Returns the unassigned backlog items (whether or not they are assigned to a milestone) in the oredering
	 * corresponding to their priority.
	 * 
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getOrderedBacklogItems() {
		String milestoneId = getId();
		List<BacklogItemWrapper> result = Lists.newArrayList();
		TaskAttribute backlog = parent.getBacklogTaskAttribute();
		for (String attributeId : backlog.getValues()) {
			BacklogItemWrapper bi = parent.wrapBacklogItem(backlog.getAttribute(attributeId));
			String assignedId = bi.getAssignedMilestoneId();
			if (assignedId != null && assignedId.equals(milestoneId)) {
				result.add(bi);
			}
		}
		return result;
	}

	/**
	 * Capacity getter.
	 * 
	 * @return The milestone's capacity, or {@code null} if not defined.
	 */
	public String getStatusValue() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getStatusValueAttributeId());
		if (attribute != null) {
			result = attribute.getValue();
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
		TaskAttribute attribute = root.getMappedAttribute(getStatusValueAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getStatusValueAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			attribute.getMetaData().setReadOnly(true);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || !oldValue.equals(statusValue)) {
			attribute.setValue(statusValue);
			fireAttributeChanged(attribute);
		}
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
}
