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
	public static final String PREFIX_BACKLOG_ITEM = "mta_bi-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the ID of a backlog item.
	 */
	public static final String BACKLOG_ITEM_ID = "mta_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a backlog item.
	 */
	public static final String BACKLOG_ITEM_NAME = "mta_bi_name"; //$NON-NLS-1$

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
	public static final String SUFFIX_ASSIGNED_MILESTONE_ID = "assigned_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	public static final String SUFFIX_BACKLOG_ITEM_POINTS = "points"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the parent of a backlog item.
	 */
	public static final String BACKLOG_ITEM_PARENT = "mta_parent"; //$NON-NLS-1$

	/**
	 * The value used to describe the kind of the backlog items contained in the milestone.
	 */
	public static final String MILESTONE_BACKLOG_ITEMS_KIND = "mta_ms_bi_kind"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing types.
	 */
	public static final String SUFFIX_TYPE = "type"; //$NON-NLS-1$

	/**
	 * The parent planning.
	 */
	private final MilestonePlanningWrapper parent;

	/**
	 * Constructor to use to wrap an existing task attribute that is not yet filled with sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param id
	 *            The backlog item's functional id
	 */
	protected BacklogItemWrapper(final MilestonePlanningWrapper parent, String id) {
		super(parent.getRoot(), PREFIX_BACKLOG_ITEM, id);
		this.parent = parent;
	}

	/**
	 * Computes the unique id of the initial effort attribute.
	 * 
	 * @return The unique id of the initial effort attribute.
	 */
	private String getInitialEffortAttributeId() {
		return getAttributeId(attribute, SUFFIX_BACKLOG_ITEM_POINTS);
	}

	/**
	 * Computes the unique id of the Assigned milestone id attribute.
	 * 
	 * @return The unique id of the Assigned milestone id attribute.
	 */
	private String getAssignedIdAttributeId() {
		return getAttributeId(attribute, SUFFIX_ASSIGNED_MILESTONE_ID);
	}

	/**
	 * Initial effort getter.
	 * 
	 * @return The item's initial effort, or {@code null} if not defined.
	 */
	public String getInitialEffort() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getInitialEffortAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Initial effort setter.
	 * 
	 * @param initialEffort
	 *            The item's initial effort.
	 */
	public void setInitialEffort(String initialEffort) {
		if (initialEffort == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getInitialEffortAttributeId());
		String oldValue = null;
		if (att == null) {
			att = root.createMappedAttribute(getInitialEffortAttributeId());
			att.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(initialEffort)) {
			att.setValue(initialEffort);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Assigned milestone id getter.
	 * 
	 * @return The id of the (sub) milestone to which this item is assigned, or {@code null} if it is not
	 *         assigned to any (sub) milestone.
	 */
	public String getAssignedMilestoneId() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getAssignedIdAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Assigned milestone id setter.
	 * 
	 * @param milestoneId
	 *            The assigned milestone id.
	 */
	public void setAssignedMilestoneId(String milestoneId) {
		TaskAttribute att = root.getMappedAttribute(getAssignedIdAttributeId());
		String oldValue = null;
		if (att == null) {
			att = root.createAttribute(getAssignedIdAttributeId());
			att.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		} else {
			oldValue = att.getValue();
		}
		if (oldValue == null || !oldValue.equals(milestoneId)) {
			att.setValue(milestoneId);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Remove the milestone assignment of this backlog item.
	 */
	public void removeAssignedMilestoneId() {
		root.removeAttribute(getAssignedIdAttributeId());
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
	 * Parent setter.
	 * 
	 * @param parentId
	 *            The parent identifier.
	 * @param parentDisplayId
	 *            The parent displayed identifier.
	 */
	public void setParent(String parentId, String parentDisplayId) {
		TaskAttribute idAttribute = root.getMappedAttribute(getParentIdSuffix());
		String oldIdValue = null;
		if (idAttribute == null) {
			idAttribute = root.createAttribute(getParentIdSuffix());
			idAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldIdValue = idAttribute.getValue();
		}
		if (oldIdValue == null || !oldIdValue.equals(parentId)) {
			idAttribute.setValue(parentId);
			fireAttributeChanged(idAttribute);
		}

		TaskAttribute displayedIdAttribute = root.getMappedAttribute(getParentDisplayIdSuffix());
		String oldDisplayedIdValue = null;
		if (displayedIdAttribute == null) {
			displayedIdAttribute = root.createAttribute(getParentDisplayIdSuffix());
			displayedIdAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldDisplayedIdValue = displayedIdAttribute.getValue();
		}
		if (oldDisplayedIdValue == null || !oldDisplayedIdValue.equals(parentDisplayId)) {
			displayedIdAttribute.setValue(parentDisplayId);
			fireAttributeChanged(displayedIdAttribute);
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
	 * Computes the unique parent identifier attribute.
	 * 
	 * @return The unique parent identifier attribute.
	 */
	public String getParentIdSuffix() {
		return root.getId() + ID_SEPARATOR + SUFFIX_BI_PARENT_ID;
	}

	/**
	 * Computes the unique parent displayed identifier attribute.
	 * 
	 * @return The unique parent displayed identifier attribute.
	 */
	public String getParentDisplayIdSuffix() {
		return root.getId() + ID_SEPARATOR + SUFFIX_BI_PARENT_DISPLAY_ID;
	}

	/**
	 * Parent identifier getter.
	 * 
	 * @return The id of parent, or {@code null} if it has not a parent.
	 */
	public String getParentId() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getParentIdSuffix());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Parent displayed identifier getter.
	 * 
	 * @return The displayed parent id, or {@code null} if it has not a parent.
	 */
	public String getParentDisplayId() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getParentDisplayIdSuffix());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * type getter.
	 * 
	 * @return The item's type, or {@code null} if not defined.
	 */
	public String getType() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getTypeAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Type setter.
	 * 
	 * @param type
	 *            The item's type. If it is null, nothing happens and the former type, if present, remains
	 *            unchanged.
	 */
	public void setType(String type) {
		if (type == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getTypeAttributeId());
		if (att == null) {
			att = root.createMappedAttribute(getTypeAttributeId());
			att.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		}
		String oldValue = att.getValue();
		if (!type.equals(oldValue)) {
			att.setValue(type);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Computes the unique id of the type attribute.
	 * 
	 * @return The unique id of the type attribute.
	 */
	private String getTypeAttributeId() {
		return getAttributeId(root, SUFFIX_TYPE);
	}
}
