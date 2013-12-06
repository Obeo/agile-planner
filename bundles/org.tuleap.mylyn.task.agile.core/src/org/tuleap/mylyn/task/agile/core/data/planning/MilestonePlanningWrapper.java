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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <p>
 * Wrapper of a TaskAttribute that represents a Milestone planning. It is supposed to store data in a
 * dedicated TaskAttribute in a parent TaskAttribute that should represent a milestone, and contain all the
 * data for the different tabs in the editors.
 * </p>
 * <p>
 * A Milestone Planning should contain all the data needed for the planning tab in the editor.
 * </p>
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class MilestonePlanningWrapper extends AbstractNotifyingWrapper {

	/**
	 * Id of the planning task attribute.
	 */
	public static final String MILESTONE_PLANNING = "mta_planning"; //$NON-NLS-1$

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String MILESTONE_LIST = "mta_milestones"; //$NON-NLS-1$

	/**
	 * Id of the milestone title task attribute.
	 */
	public static final String MILESTONES_TITLE = "mta_milestones_title"; //$NON-NLS-1$

	/**
	 * Id of the backlog items list task attribute.
	 */
	public static final String BACKLOG = "mta_backlog"; //$NON-NLS-1$

	/**
	 * Id of the backlog items title task attribute.
	 */
	public static final String BACKLOG_TITLE = "mta_backlog_title"; //$NON-NLS-1$

	/**
	 * Id of the task attribute that indicates whether there is a card wall for this planning.
	 */
	public static final String HAS_CARDWALL = "mta_has_cardwall"; //$NON-NLS-1$

	/**
	 * The attribute that represents the list of the sub-milestones.
	 */
	private final TaskAttribute submilestoneList;

	/**
	 * The attribute that represents the list of the backlog items.
	 */
	private final TaskAttribute backlog;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            The parent task attribute that contains every piece of data related to the wrapped
	 *            milestone.
	 */
	public MilestonePlanningWrapper(final TaskAttribute root) {
		super(root, MILESTONE_PLANNING, ""); //$NON-NLS-1$
		TaskAttribute milestonesAtt = root.getAttribute(MILESTONE_LIST);
		if (milestonesAtt == null) {
			milestonesAtt = root.createAttribute(MILESTONE_LIST);
			milestonesAtt.getMetaData().setReadOnly(true);
		}
		TaskAttribute backlogAtt = root.getAttribute(BACKLOG);
		if (backlogAtt == null) {
			backlogAtt = root.createAttribute(BACKLOG);
			backlogAtt.getMetaData().setReadOnly(true);
		}
		backlog = backlogAtt;
		submilestoneList = milestonesAtt;
	}

	/**
	 * Gets the cardwall flag.
	 * 
	 * @return The cardwall flag that indicates whether there ought to be a cardwall for this planning.
	 */
	public boolean getHasCardwall() {
		TaskAttribute att = backlog.getParentAttribute().getMappedAttribute(HAS_CARDWALL);
		if (att != null) {
			return att.getTaskData().getAttributeMapper().getBooleanValue(att);
		}
		return false;
	}

	/**
	 * Sets the cardwall flag.
	 * 
	 * @param hasCardwall
	 *            The cardwall flag.
	 */
	public void setHasCardwall(boolean hasCardwall) {
		TaskAttribute att = backlog.getParentAttribute().getMappedAttribute(HAS_CARDWALL);
		if (att == null) {
			att = backlog.getParentAttribute().createMappedAttribute(HAS_CARDWALL);
		}
		att.getTaskData().getAttributeMapper().setBooleanValue(att, Boolean.valueOf(hasCardwall));
	}

	/**
	 * Creates a new sub-milestone in the wrapped milestone and returns its wrapper.
	 * 
	 * @param id
	 *            the sub-milestone identifier
	 * @return A new wrapper for a new task attribute that is created by invoking this method.
	 */
	public SubMilestoneWrapper addSubMilestone(String id) {
		SubMilestoneWrapper w = wrapSubMilestone(id);
		submilestoneList.addValue(id);
		return w;
	}

	/**
	 * Returns the sub-milestones.
	 * 
	 * @return a never null iterable of sub-milestone wrappers.
	 */
	public List<SubMilestoneWrapper> getSubMilestones() {
		List<SubMilestoneWrapper> result = newArrayList();
		for (String milestoneId : submilestoneList.getValues()) {
			result.add(wrapSubMilestone(milestoneId));
		}
		return result;
	}

	/**
	 * The number of sub-milestones in this planning.
	 * 
	 * @return the number of sub-milestones in this planning.
	 */
	public int submilestonesCount() {
		return submilestoneList.getValues().size();
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
		BacklogItemWrapper w = wrapBacklogItem(id);
		backlog.addValue(id);
		return w;
	}

	/**
	 * The number of backlog items in the backlog.
	 * 
	 * @return the number of backlog items in the backlog.
	 */
	public int backlogItemsCount() {
		return backlog.getValues().size();
	}

	/**
	 * Returns all the backlog items (whether or not they are assigned to a milestone).
	 * 
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getAllBacklogItems() {
		List<BacklogItemWrapper> result = newArrayList();
		for (String backlogItemId : backlog.getValues()) {
			result.add(wrapBacklogItem(backlogItemId));
		}
		return result;
	}

	/**
	 * Returns the unassigned backlog items (whether or not they are assigned to a milestone) in the oredering
	 * corresponding to their priority.
	 * 
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getOrderedUnassignedBacklogItems() {
		List<BacklogItemWrapper> result = newArrayList();
		for (String backlogItemId : backlog.getValues()) {
			BacklogItemWrapper bi = wrapBacklogItem(backlogItemId);
			if (bi.getAssignedMilestoneId() == null) {
				result.add(bi);
			}
		}
		return result;
	}

	/**
	 * Provides the sub-milestone with the given id.
	 * 
	 * @param id
	 *            The id of the sub-milestone being looked for
	 * @return The first milestone found with the given id in the list of sub-milestones.
	 */
	public SubMilestoneWrapper getSubMilestone(final String id) {
		if (id != null) {
			Iterator<SubMilestoneWrapper> candidates = Iterators.filter(getSubMilestones().iterator(),
					new Predicate<SubMilestoneWrapper>() {
						/**
						 * {@inheritDoc}
						 */
						@Override
						public boolean apply(SubMilestoneWrapper w) {
							return id.equals(w.getId());
						}
					});
			if (candidates.hasNext()) {
				return candidates.next();
			}
		}
		return null;
	}

	/**
	 * Returns a sub-milestone wrapper for an existing task attribute.
	 * 
	 * @param milestoneId
	 *            The sub-milestone ID.
	 * @return a new sub-milestone wrapper for the given task attribute.
	 */
	public SubMilestoneWrapper wrapSubMilestone(String milestoneId) {
		return new SubMilestoneWrapper(this, milestoneId);
	}

	/**
	 * Returns a backlog item wrapper for an existing task attribute.
	 * 
	 * @param backlogItemId
	 *            The ID of the BI.
	 * @return a new backlog wrapper for the given task attribute.
	 */
	public BacklogItemWrapper wrapBacklogItem(String backlogItemId) {
		return new BacklogItemWrapper(this, backlogItemId);
	}

	/**
	 * Moves a list of BacklogItem before or after a target BacklogItem.
	 * 
	 * @param items
	 *            the BacklogItems list
	 * @param target
	 *            the target BacklogItem, if null elements are appended at the end of the backlog.
	 * @param before
	 *            a boolean parameter that indicates if moving BacklogItems will be before or after the target
	 */
	public void moveItemsToBacklog(List<BacklogItemWrapper> items, BacklogItemWrapper target, boolean before) {
		if (items == null || items.isEmpty()) {
			return;
		}
		String targetId = null;
		if (target != null) {
			targetId = target.getWrappedAttribute().getValue();
		}
		int insertPosition = -1;
		List<String> ids = newArrayList();
		ids.addAll(backlog.getValues()); // Modifiable copy of ordered ids

		List<String> movedIds = newArrayList();
		for (BacklogItemWrapper bi : items) {
			TaskAttribute wrappedAtt = bi.getWrappedAttribute();
			bi.removeAssignedMilestoneId();
			if (wrappedAtt.getValue().equals(targetId)) {
				// If the target element is among the moved elements,
				// Elements already treated must be moved before it,
				// and the remaining elements must be moved after it.
				insertPosition = ids.indexOf(targetId);
			}
			String movedId = wrappedAtt.getValue();
			// Security : we only move elements that are present in the original list
			if (ids.remove(movedId)) {
				movedIds.add(movedId);
			}
		}
		if (insertPosition == -1) {
			insertPosition = computeInsertPosition(before, targetId, ids);
		}
		for (String movedId : movedIds) {
			ids.add(insertPosition++, movedId);
		}
		backlog.clearValues();
		backlog.setValues(ids);
		fireAttributeChanged(backlog);
	}

	/**
	 * Moves a list of BacklogItem before or after a target BacklogItem.
	 * 
	 * @param items
	 *            the BacklogItems list
	 * @param target
	 *            the target BacklogItem, if null elements are appended at the end of the milestone.
	 * @param before
	 *            a boolean parameter that indicates if moving BacklogItems will be before or after the target
	 * @param subMilestone
	 *            the subMilestone to which BacklogItems will be moved. The target's assigned id should be
	 *            equal to the subMilestone's id.
	 */
	public void moveItemsToMilestone(List<BacklogItemWrapper> items, BacklogItemWrapper target,
			boolean before, SubMilestoneWrapper subMilestone) {
		if (items == null || items.isEmpty()) {
			return;
		}
		String targetId = null;
		if (target != null) {
			targetId = target.getWrappedAttribute().getValue();
		}
		int insertPosition = -1;
		List<String> formerIds = newArrayList();
		formerIds.addAll(backlog.getValues()); // Modifiable copy of ordered ids

		List<String> movedIds = newArrayList();
		for (BacklogItemWrapper bi : items) {
			TaskAttribute wrappedAtt = bi.getWrappedAttribute();
			bi.setAssignedMilestoneId(subMilestone.getId());
			if (wrappedAtt.getValue().equals(targetId)) {
				// If the target element is among the moved elements,
				// Elements already treated must be moved before it,
				// and the remaining elements must be moved after it.
				insertPosition = formerIds.indexOf(targetId);
			}
			String movedId = wrappedAtt.getValue();
			// Security : we only move elements that are present in the original list
			if (formerIds.remove(movedId)) {
				movedIds.add(movedId);
			}
		}
		if (insertPosition == -1) {
			insertPosition = computeInsertPosition(before, targetId, formerIds);
		}
		for (String movedId : movedIds) {
			formerIds.add(insertPosition++, movedId);
		}
		backlog.clearValues();
		backlog.setValues(formerIds);
		fireAttributeChanged(backlog);
	}

	/**
	 * Compute the insertion index.
	 * 
	 * @param before
	 *            flag
	 * @param targetId
	 *            id of target, can be null
	 * @param formerIds
	 *            List of ids not moved
	 * @return The insertion index in formerIds.
	 */
	private int computeInsertPosition(boolean before, String targetId, List<String> formerIds) {
		int insertPosition;
		if (targetId == null) {
			insertPosition = formerIds.size();
		} else {
			insertPosition = formerIds.indexOf(targetId);
			if (!before) {
				insertPosition++;
			}
		}
		return insertPosition;
	}

	/**
	 * Milestones list title setter. Does not notify.
	 * 
	 * @param title
	 *            The milestones list title
	 */
	public void setMilestonesTitle(String title) {
		TaskAttribute att = root.getMappedAttribute(MILESTONES_TITLE);
		if (att == null) {
			att = root.createMappedAttribute(MILESTONES_TITLE);
			att.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			att.getMetaData().setReadOnly(true);
		}
		// No notification needed, read-only
		att.setValue(title);
	}

	/**
	 * Backlog title getter.
	 * 
	 * @return The backlog title, or <code>null</code> if unset.
	 */
	public String getMilestonesTitle() {
		TaskAttribute att = root.getMappedAttribute(MILESTONES_TITLE);
		if (att != null) {
			return att.getValue();
		}
		return null;
	}

	/**
	 * Backlog title setter. Does not notify.
	 * 
	 * @param title
	 *            The backlog title
	 */
	public void setBacklogTitle(String title) {
		TaskAttribute att = root.getMappedAttribute(BACKLOG_TITLE);
		if (att == null) {
			att = root.createMappedAttribute(BACKLOG_TITLE);
			att.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			att.getMetaData().setReadOnly(true);
		}
		// No notification needed, read-only
		att.setValue(title);
	}

	/**
	 * Backlog title getter.
	 * 
	 * @return The backlog title, or <code>null</code> if unset.
	 */
	public String getBacklogTitle() {
		TaskAttribute att = root.getMappedAttribute(BACKLOG_TITLE);
		if (att != null) {
			return att.getValue();
		}
		return null;
	}

	/**
	 * Provides the wrapped task attribute that represents the backlog.
	 * 
	 * @return The wrapped task attribute that represents the backlog.
	 */
	public TaskAttribute getBacklogTaskAttribute() {
		return backlog;
	}

	/**
	 * Provides the wrapped task attribute that represents the milestone list.
	 * 
	 * @return The wrapped task attribute that represents the milestone list.
	 */
	public TaskAttribute getSubMilestoneListTaskAttribute() {
		return submilestoneList;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		// Required for delegation from other classes of this package
		super.fireAttributeChanged(att);
	}
}
