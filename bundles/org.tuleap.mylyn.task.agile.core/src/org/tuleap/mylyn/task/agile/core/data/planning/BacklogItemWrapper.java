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

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

/**
 * Wrapper of a TaskAttribute that represents a Backlog Item.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String TYPE_BACKLOG_ITEM = "mta_backlog_item"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	public static final String PREFIX_BACKLOG_ITEM = "mta_backlog_item-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the Tuleap ID of a backlog item.
	 */
	public static final String BACKLOG_ITEM_ID = "mta_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a backlog item.
	 */
	public static final String BACKLOG_ITEM_NAME = "mta_backlog_item_name"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of the type of a backlog. This name
	 * will be usable for labels, for example "Release Backlog" if the backlog is a release backlog.
	 */
	public static final String BACKLOG_TYPE_LABEL = "mta_backlog_type_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of the type of a backlog item. This
	 * name will be usable for labels, for example "User Story" if the backlog item is a user story.
	 */
	public static final String BACKLOG_ITEM_TYPE_LABEL = "mta_bi_type_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the label to use for points ("Story Points" for
	 * example).
	 */
	public static final String BACKLOG_ITEM_POINTS_LABEL = "mta_bi_points_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the label to use for points ("Story Points" for
	 * example).
	 */
	public static final String ASSIGNED_MILESTONE_ID = "assigned_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	public static final String BACKLOG_ITEM_POINTS = "points"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the parent of a backlog item.
	 */
	public static final String BACKLOG_ITEM_PARENT = "mta_parent"; //$NON-NLS-1$

	/**
	 * The value used to describe the kind of the backlog items contained in the milestone.
	 */
	public static final String MILESTONE_BACKLOG_ITEMS_KIND = "mta_milestone_bi_kind"; //$NON-NLS-1$

	/**
	 * The parent planning.
	 */
	private final MilestonePlanningWrapper parent;

	/**
	 * Constructor to use to wrap an existing task attribute that is not yet filled with sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 * @param id
	 *            The backlog item's functional id
	 */
	protected BacklogItemWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root, int id) {
		super(root, id);
		Assert.isNotNull(parent);
		this.parent = parent;
		parent.getBacklogTaskAttribute().addValue(root.getId());
	}

	/**
	 * Constructor to use to wrap an existing instance that already has sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 */
	protected BacklogItemWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root) {
		super(root);
		Assert.isNotNull(parent);
		this.parent = parent;
	}

	/**
	 * Computes the unique id of the initial effort attribute.
	 * 
	 * @return The unique id of the initial effort attribute.
	 */
	private String getInitialEffortAttributeId() {
		return root.getId() + ID_SEPARATOR + BACKLOG_ITEM_POINTS;
	}

	/**
	 * Computes the unique id of the Assigned milestone id attribute.
	 * 
	 * @return The unique id of the Assigned milestone id attribute.
	 */
	private String getAssignedIdAttributeId() {
		return root.getId() + ID_SEPARATOR + ASSIGNED_MILESTONE_ID;
	}

	/**
	 * Initial effort getter.
	 * 
	 * @return The item's initial effort, or {@code null} if not defined.
	 */
	public Float getInitialEffort() {
		Float result = null;
		TaskAttribute attribute = root.getMappedAttribute(getInitialEffortAttributeId());
		if (attribute != null) {
			try {
				result = Float.valueOf(attribute.getValue());
			} catch (NumberFormatException e) {
				// Nothing to do
			}
		}
		return result;
	}

	/**
	 * Initial effort setter.
	 * 
	 * @param initialEffort
	 *            The item's initial effort.
	 */
	public void setInitialEffort(float initialEffort) {
		TaskAttribute attribute = root.getMappedAttribute(getInitialEffortAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(getInitialEffortAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || Float.parseFloat(oldValue) != initialEffort) {
			attribute.setValue(Float.toString(initialEffort));
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the (sub) milestone to which this item is assigned, or {@code null} if it is not
	 *         assigned to any (sub) milestone.
	 */
	public Integer getAssignedMilestoneId() {
		Integer result = null;
		TaskAttribute attribute = root.getMappedAttribute(getAssignedIdAttributeId());
		if (attribute != null) {
			result = Integer.valueOf(attribute.getValue());
		}
		return result;
	}

	/**
	 * Assigned milestone id setter.
	 * 
	 * @param milestoneId
	 *            The assigned milestone id.
	 */
	public void setAssignedMilestoneId(int milestoneId) {
		TaskAttribute attribute = root.getMappedAttribute(getAssignedIdAttributeId());
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createAttribute(getAssignedIdAttributeId());
			attribute.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		} else {
			oldValue = attribute.getValue();
		}
		if (oldValue == null || Integer.parseInt(oldValue) != milestoneId) {
			attribute.setValue(Integer.toString(milestoneId));
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Remove the milestone assignment of this backlog item.
	 */
	public void removeAssignedMilestoneId() {
		root.removeAttribute(getAssignedIdAttributeId());
	}

	/**
	 * Creates a new task attribute in the given parent attribute to represent a new backlog item and returns
	 * its wrapper.
	 * 
	 * @param parent
	 *            The parent planning wrapper.
	 * @param id
	 *            The backlogItem identifier
	 * @return a new wrapper for a new task attribute that represents a backlog items.
	 */
	public static BacklogItemWrapper createBacklogItem(MilestonePlanningWrapper parent, int id) {
		TaskAttribute backlogAtt = parent.getBacklogTaskAttribute();
		TaskAttribute root = backlogAtt.createAttribute(PREFIX_BACKLOG_ITEM
				+ backlogAtt.getAttributes().size());
		root.getMetaData().setReadOnly(true);
		BacklogItemWrapper backlogItemWrapper = new BacklogItemWrapper(parent, root, id);
		return backlogItemWrapper;
	}

	/**
	 * Return the parent planning as a wrapper.
	 * 
	 * @return The (never null) parent planning, as a wrapper.
	 */
	public MilestonePlanningWrapper getParent() {
		return parent;
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
