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
	public static final String MILESTONE_CAPACITY = "mta_capacity"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String TYPE_MILESTONE = "mta_milestone"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	public static final String PREFIX_MILESTONE = "mta_milestone-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone duration.
	 */
	public static final String MILESTONE_DURATION = "mta_duration"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a planned start date.
	 */
	public static final String START_DATE = "mta_start_date"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a planned end date.
	 */
	public static final String END_DATE = "mta_end_date"; //$NON-NLS-1$

	/**
	 * Constructor to use to wrap an existing instance.
	 * 
	 * @param root
	 *            The non-null task attribute that represents a sub-milestone to wrap.
	 */
	protected SubMilestoneWrapper(final TaskAttribute root) {
		super(root);
	}

	/**
	 * Id getter.
	 * 
	 * @return The milestone's id, or {@code -1} if the id is not set.
	 */
	public int getId() {
		int result = -1;
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(IMylynAgileCoreConstants.ID);
		if (milestoneNameAtt != null) {
			result = Integer.parseInt(milestoneNameAtt.getValue());
		}
		return result;
	}

	/**
	 * Id setter.
	 * 
	 * @param id
	 *            The milestone's id.
	 */
	private void setId(int id) {
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(IMylynAgileCoreConstants.ID);
		if (milestoneNameAtt == null) {
			milestoneNameAtt = root.createMappedAttribute(IMylynAgileCoreConstants.ID);
			milestoneNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			milestoneNameAtt.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		}
		milestoneNameAtt.setValue(Integer.toString(id));
	}

	/**
	 * Label getter.
	 * 
	 * @return The milestone's label, or {@code null} if not defined.
	 */
	public String getLabel() {
		String result = null;
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(IMylynAgileCoreConstants.LABEL);
		if (milestoneNameAtt != null) {
			result = milestoneNameAtt.getValue();
		}
		return result;
	}

	/**
	 * Label setter.
	 * 
	 * @param label
	 *            The milestone's label.
	 */
	public void setLabel(String label) {
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(IMylynAgileCoreConstants.LABEL);
		if (milestoneNameAtt == null) {
			milestoneNameAtt = root.createMappedAttribute(IMylynAgileCoreConstants.LABEL);
			milestoneNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			milestoneNameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		}
		milestoneNameAtt.setValue(label);
	}

	/**
	 * Capacity getter.
	 * 
	 * @return The milestone's capacity, or {@code null} if not defined.
	 */
	public Float getCapacity() {
		Float result = null;
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(MILESTONE_CAPACITY);
		if (milestoneNameAtt != null) {
			result = Float.valueOf(milestoneNameAtt.getValue());
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
		TaskAttribute capacityAtt = root.getMappedAttribute(MILESTONE_CAPACITY);
		if (capacityAtt == null) {
			capacityAtt = root.createMappedAttribute(MILESTONE_CAPACITY);
			capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		}
		capacityAtt.setValue(Float.toString(capacity));
	}

	/**
	 * Start date getter.
	 * 
	 * @return The milestone's start date, or {@code null} if not defined.
	 */
	public Date getStartDate() {
		Date result = null;
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(START_DATE);
		if (milestoneNameAtt != null) {
			result = new Date(Long.parseLong(milestoneNameAtt.getValue()));
		}
		return result;
	}

	/**
	 * Start date setter.
	 * 
	 * @param start
	 *            The milestone's start date.
	 */
	public void setStartDate(Date start) {
		TaskAttribute capacityAtt = root.getMappedAttribute(START_DATE);
		if (capacityAtt == null) {
			capacityAtt = root.createMappedAttribute(START_DATE);
			capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		}
		capacityAtt.setValue(Long.toString(start.getTime()));
	}

	/**
	 * Duration getter.
	 * 
	 * @return The milestone's duration, or {@code null} if not defined.
	 */
	public Float getDuration() {
		Float result = null;
		TaskAttribute milestoneNameAtt = root.getMappedAttribute(MILESTONE_DURATION);
		if (milestoneNameAtt != null) {
			result = Float.valueOf(milestoneNameAtt.getValue());
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
		TaskAttribute capacityAtt = root.getMappedAttribute(MILESTONE_DURATION);
		if (capacityAtt == null) {
			capacityAtt = root.createMappedAttribute(MILESTONE_DURATION);
			capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		}
		capacityAtt.setValue(Float.toString(duration));
	}

	/**
	 * Creates a new task attribute in the given parent attribute to represent a new sub-milestone and returns
	 * its wrapper.
	 * 
	 * @param parent
	 *            The parent task attribute, should represent a list of sub-milestones.
	 * @param id
	 *            the submilestone identifier
	 * @return a new wrapper for a new task attribute that represents a sub-milestone.
	 */
	public static SubMilestoneWrapper createSubMilestone(TaskAttribute parent, int id) {
		TaskAttribute root = parent.createAttribute(PREFIX_MILESTONE + parent.getAttributes().size());
		root.getMetaData().setReadOnly(true);
		SubMilestoneWrapper subMilestoneWrapper = new SubMilestoneWrapper(root);
		subMilestoneWrapper.setId(id);
		return subMilestoneWrapper;
	}

	/**
	 * Returns a sub-milestone wrapper for an existing task attribute.
	 * 
	 * @param root
	 *            the task attribute to wrap, should represent a sub-milestone.
	 * @return a new wrapper for the given task attribute.
	 */
	public static SubMilestoneWrapper wrapSubMilestone(TaskAttribute root) {
		return new SubMilestoneWrapper(root);
	}
}
