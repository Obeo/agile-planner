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
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

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
	public static final String ASSIGNED_MILESTONE_ID = "mta_assigned_to_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	public static final String BACKLOG_ITEM_POINTS = "mta_bi_points"; //$NON-NLS-1$

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
	 * Constructor to use to wrap an existing instance.
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
	 * Id getter.
	 * 
	 * @return The item's id.
	 */
	public int getId() {
		int result = -1;
		TaskAttribute attribute = root.getMappedAttribute(IMylynAgileCoreConstants.ID);
		if (attribute != null) {
			result = Integer.parseInt(attribute.getValue());
		}
		return result;
	}

	/**
	 * Id setter.
	 * 
	 * @param id
	 *            The item's id.
	 */
	private void setId(int id) {
		TaskAttribute attribute = root.getMappedAttribute(IMylynAgileCoreConstants.ID);
		if (attribute == null) {
			attribute = root.createMappedAttribute(IMylynAgileCoreConstants.ID);
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		}
		attribute.setValue(Integer.toString(id));
	}

	/**
	 * Label getter.
	 * 
	 * @return The item's label, or {@code null} if not defined.
	 */
	public String getLabel() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(IMylynAgileCoreConstants.LABEL);
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}

	/**
	 * Label setter.
	 * 
	 * @param label
	 *            The item's label. If it is null, nothing happens and the former label, if present, remains
	 *            unchanged.
	 */
	public void setLabel(String label) {
		if (label == null) {
			return;
		}
		TaskAttribute attribute = root.getMappedAttribute(IMylynAgileCoreConstants.LABEL);
		if (attribute == null) {
			attribute = root.createMappedAttribute(IMylynAgileCoreConstants.LABEL);
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		}
		String oldValue = attribute.getValue();
		if (oldValue == null || !oldValue.equals(label)) {
			attribute.setValue(label);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Initial effort getter.
	 * 
	 * @return The item's initial effort, or {@code null} if not defined.
	 */
	public Float getInitialEffort() {
		Float result = null;
		TaskAttribute attribute = root.getMappedAttribute(BACKLOG_ITEM_POINTS);
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
		TaskAttribute attribute = root.getMappedAttribute(BACKLOG_ITEM_POINTS);
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createMappedAttribute(BACKLOG_ITEM_POINTS);
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
		TaskAttribute attribute = root.getMappedAttribute(ASSIGNED_MILESTONE_ID);
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
		TaskAttribute attribute = root.getMappedAttribute(ASSIGNED_MILESTONE_ID);
		String oldValue = null;
		if (attribute == null) {
			attribute = root.createAttribute(ASSIGNED_MILESTONE_ID);
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
		root.removeAttribute(ASSIGNED_MILESTONE_ID);
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
		BacklogItemWrapper backlogItemWrapper = new BacklogItemWrapper(parent, backlogAtt);
		backlogItemWrapper.setId(id);
		backlogAtt.addValue(Integer.toString(id));
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
