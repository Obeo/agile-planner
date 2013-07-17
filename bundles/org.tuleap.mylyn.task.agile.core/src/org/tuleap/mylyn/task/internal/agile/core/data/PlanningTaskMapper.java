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
package org.tuleap.mylyn.task.internal.agile.core.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;

/**
 * The plannig task mapper.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class PlanningTaskMapper extends TaskMapper {
	/**
	 * The mapper task data.
	 */
	private TaskData taskData;

	/**
	 * The constructor requires a task data.
	 * 
	 * @param data
	 *            The task
	 */
	public PlanningTaskMapper(TaskData data) {
		super(data);
		this.taskData = data;
	}

	/**
	 * Creates a milestone for the task data.
	 * 
	 * @param name
	 *            The name of the created milestone
	 * @param capacity
	 *            The capacity of the created milestone
	 * @param startDate
	 *            The start date of the sprint
	 * @param endDate
	 *            The end date of the sprint
	 */
	public void createMilestone(String name, int capacity, Date startDate, Date endDate) {
		// Shift all the previous index with index+1

		for (TaskAttribute attribute : this.taskData.getRoot().getAttributes().values()) {

			if (attribute.getMetaData().getType() == IMylynAgileCoreConstants.TYPE_MILESTONE) {
				String oldIndex = attribute.getAttribute(IMylynAgileCoreConstants.MILESTONE_INDEX).getValue();
				int index = Integer.parseInt(oldIndex);
				attribute.setValue(String.valueOf(index++));
			}
		}

		// Create the new milestone under the task data root

		TaskAttribute milestoneAtt = this.taskData.getRoot().createAttribute(
				IMylynAgileCoreConstants.PREFIX_MILESTONE + UUID.randomUUID().toString());
		milestoneAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_MILESTONE);
		milestoneAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		TaskAttribute milestoneNameAtt = milestoneAtt
				.createAttribute(IMylynAgileCoreConstants.MILESTONE_NAME);
		milestoneNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		milestoneNameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		milestoneNameAtt.setValue(name);

		TaskAttribute milestoneIndex = milestoneAtt.createAttribute(IMylynAgileCoreConstants.MILESTONE_INDEX);
		milestoneIndex.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		milestoneIndex.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		milestoneIndex.setValue("0"); //$NON-NLS-1$

		TaskAttribute capacityAtt = milestoneAtt.createAttribute(IMylynAgileCoreConstants.MILESTONE_CAPACITY);
		capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		capacityAtt.setValue(String.valueOf(capacity));

		TaskAttribute startDateAtt = milestoneAtt.createAttribute(IMylynAgileCoreConstants.START_DATE);
		startDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		startDateAtt.getMetaData().setLabel("Creation Date"); //$NON-NLS-1$
		startDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		startDateAtt.setValue(String.valueOf(startDate.getTime()));

		TaskAttribute endDateAtt = milestoneAtt.createAttribute(IMylynAgileCoreConstants.END_DATE);
		endDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		endDateAtt.getMetaData().setLabel("End Date"); //$NON-NLS-1$
		endDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATE);
		endDateAtt.setValue(String.valueOf(endDate.getTime()));

	}

	/**
	 * Moves an artifact inside the same milestone.
	 * 
	 * @param milestone
	 *            the milestone in which occurs moving
	 * @param from
	 *            the actual position of the artifact in the milestone
	 * @param to
	 *            the target position of the artifact in the milestone
	 */
	public void moveArtifactInMilestone(TaskAttribute milestone, int from, int to) {
		TaskAttribute artifact = null;
		if (milestone.getMetaData().getType().equals(IMylynAgileCoreConstants.TYPE_MILESTONE)) {
			for (TaskAttribute taskAttribute : milestone.getAttributes().values()) {
				if (taskAttribute.getValue().equals(String.valueOf(from))) {
					artifact = taskAttribute;
				}
			}
			if (artifact != null) {
				// The start and the target positions are the same
				if (from == to) {
					return;
				}
				// The start and the target positions are different
				List<TaskAttribute> list = new ArrayList<TaskAttribute>();
				list.add(artifact);
				new TaskAttributeWrapper(milestone).moveElementsSortedByValue(list, to + 1, artifact
						.getMetaData().getType());
			}
		}
	}

	/**
	 * Moves an artifact inside the same backlog.
	 * 
	 * @param backlog
	 *            the backlog in which occurs moving
	 * @param from
	 *            the actual position of the artifact in the backlog
	 * @param to
	 *            the target position of the artifact in the backlog
	 */
	public void moveArtifactInBacklog(TaskAttribute backlog, int from, int to) {
		TaskAttribute artifact = null;
		if (!backlog.getMetaData().getType().equals(IMylynAgileCoreConstants.TYPE_MILESTONE)) {
			for (TaskAttribute taskAttribute : backlog.getAttributes().values()) {
				if (taskAttribute.getValue().equals(String.valueOf(from))) {
					artifact = taskAttribute;
				}
			}
			if (artifact != null) {
				// The start and the target positions are the same
				if (from == to) {
					return;
				}
				// The start and the target positions are different
				List<TaskAttribute> list = new ArrayList<TaskAttribute>();
				list.add(artifact);
				new TaskAttributeWrapper(backlog).moveElementsSortedByValue(list, to + 1, artifact
						.getMetaData().getType());
			}
		}
	}

	/**
	 * Moves an artifact from a backlog to a milestone.
	 * 
	 * @param backlog
	 *            the backlog containing the artifact to move
	 * @param milestone
	 *            the target milestone that will contain the moved artifact
	 * @param from
	 *            the actual position of the artifact in the backlog
	 * @param to
	 *            the target position of the artifact in the milestone
	 */
	public void moveArtifactFromBacklogToMilestone(TaskAttribute backlog, TaskAttribute milestone, int from,
			int to) {
		TaskAttribute artifact = null;
		if (!backlog.getMetaData().getType().equals(IMylynAgileCoreConstants.TYPE_MILESTONE)
				&& milestone.getMetaData().getType().equals(IMylynAgileCoreConstants.TYPE_MILESTONE)) {
			for (TaskAttribute taskAttribute : backlog.getAttributes().values()) {
				if (taskAttribute.getValue().equals(String.valueOf(from))) {
					artifact = taskAttribute;
				}
			}
			if (artifact != null) {
				List<TaskAttribute> addList = new ArrayList<TaskAttribute>();
				addList.add(artifact);
				new TaskAttributeWrapper(milestone).insertElementsSortedByValue(addList, to, artifact
						.getMetaData().getType());

				List<TaskAttribute> removeList = new ArrayList<TaskAttribute>();
				removeList.add(artifact);
				new TaskAttributeWrapper(backlog).removeElementsSortedByValue(removeList.iterator(), artifact
						.getMetaData().getType());
			}
		}
	}
}
