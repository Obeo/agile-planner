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

import java.util.Date;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

/**
 * Wrapper of a TaskAttribute that represents a Sub-milestone.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class SubMilestoneWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Id of the backlog items list task attribute.
	 */
	public static final String MILESTONE_CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String TYPE_MILESTONE = "mta_milestone"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	public static final String PREFIX_MILESTONE = "mta_ms-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone duration.
	 */
	public static final String MILESTONE_DURATION = "duration"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a planned start date.
	 */
	public static final String START_DATE = "start_date"; //$NON-NLS-1$

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
	 */
	protected SubMilestoneWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root) {
		super(root);
		this.parent = parent;
	}

	/**
	 * Computes the unique id of the ID attribute.
	 * 
	 * @return The unique id of the id attribute.
	 */
	private String getIdAttributeId() {
		return root.getId() + ID_SEPARATOR + IMylynAgileCoreConstants.ID;
	}

	/**
	 * Computes the unique id of the Label attribute.
	 * 
	 * @return The unique id of the label attribute.
	 */
	private String getLabelAttributeId() {
		return root.getId() + ID_SEPARATOR + IMylynAgileCoreConstants.LABEL;
	}

	/**
	 * Computes the unique id of the capacity attribute.
	 * 
	 * @return The unique id of the capacity attribute.
	 */
	private String getCapacityAttributeId() {
		return root.getId() + ID_SEPARATOR + MILESTONE_CAPACITY;
	}

	/**
	 * Computes the unique id of the duration attribute.
	 * 
	 * @return The unique id of the duration attribute.
	 */
	private String getDurationAttributeId() {
		return root.getId() + ID_SEPARATOR + MILESTONE_DURATION;
	}

	/**
	 * Computes the unique id of the start date attribute.
	 * 
	 * @return The unique id of the start date attribute.
	 */
	private String getStartDateAttributeId() {
		return root.getId() + ID_SEPARATOR + START_DATE;
	}

	/**
	 * Id getter.
	 * 
	 * @return The milestone's id, or {@code -1} if the id is not set.
	 */
	public int getId() {
		int result = -1;
		TaskAttribute attribute = root.getMappedAttribute(getIdAttributeId());
		if (attribute != null) {
			result = Integer.parseInt(attribute.getValue());
		}
		return result;
	}

	/**
	 * Id setter.
	 * 
	 * @param id
	 *            The milestone's id.
	 */
	public void setId(int id) {
		TaskAttribute attribute = root.getMappedAttribute(getIdAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getIdAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		}
		attribute.setValue(Integer.toString(id));
	}

	/**
	 * Label getter.
	 * 
	 * @return The milestone's label, or {@code null} if not defined.
	 */
	public String getLabel() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getLabelAttributeId());
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}

	/**
	 * Label setter.
	 * 
	 * @param label
	 *            The milestone's label. If it is null, nothing is performed, the former label, if any,
	 *            remains unchanged.
	 */
	public void setLabel(String label) {
		if (label == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(getLabelAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getLabelAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldValue = attribute.getValue();
		}
		if (!label.equals(oldValue)) {
			attribute.setValue(label);
			fireAttributeChanged(attribute);
		}
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
	public Float getDuration() {
		Float result = null;
		TaskAttribute attribute = root.getMappedAttribute(getDurationAttributeId());
		if (attribute != null) {
			result = Float.valueOf(attribute.getValue());
		}
		return result;
	}

	/**
	 * Duration setter.
	 * 
	 * @param duration
	 *            The milestone's duration.
	 */
	public void setDuration(float duration) {
		TaskAttribute attribute = root.getMappedAttribute(getDurationAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getDurationAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || Float.parseFloat(oldValue) != duration) {
			attribute.setValue(Float.toString(duration));
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

	/**
	 * Creates a new task attribute in the given parent attribute to represent a new sub-milestone and returns
	 * its wrapper.
	 * 
	 * @param wrapper
	 *            the parent planning
	 * @param id
	 *            the sub-milestone identifier
	 * @return a new wrapper for a new task attribute that represents a sub-milestone.
	 */
	public static SubMilestoneWrapper createSubMilestone(MilestonePlanningWrapper wrapper, int id) {
		TaskAttribute parent = wrapper.getSubMilestoneListTaskAttribute();
		TaskAttribute root = parent.createAttribute(PREFIX_MILESTONE + parent.getAttributes().size());
		root.getMetaData().setReadOnly(true);
		SubMilestoneWrapper sub = new SubMilestoneWrapper(wrapper, root);
		sub.setId(id);
		return sub;
	}
}
