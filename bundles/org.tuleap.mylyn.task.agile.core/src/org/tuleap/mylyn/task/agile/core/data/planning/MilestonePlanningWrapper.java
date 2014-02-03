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

import java.util.ArrayList;
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
		TaskAttribute att = root.getMappedAttribute(HAS_CARDWALL);
		if (att == null) {
			att = createAgileAttribute(HAS_CARDWALL);
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
		for (SubMilestoneWrapper subMilestone : getSubMilestones()) {
			result.addAll(subMilestone.getOrderedBacklogItems());
		}
		return result;
	}

	/**
	 * Returns the unassigned backlog items (whether or not they are assigned to a milestone) in the ordering
	 * corresponding to their priority.
	 * 
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getOrderedUnassignedBacklogItems() {
		List<BacklogItemWrapper> result = newArrayList();
		for (String backlogItemId : backlog.getValues()) {
			result.add(wrapBacklogItem(backlogItemId));
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
	 *            the target BacklogItem, if null elements are appended at the end of the milestone.
	 * @param before
	 *            a boolean parameter that indicates if moving BacklogItems will be before or after the target
	 * @param subMilestone
	 *            the subMilestone to which BacklogItems will be moved. If null, items are assigned to the
	 *            backlog.
	 */
	public void moveItems(List<BacklogItemWrapper> items, BacklogItemWrapper target, boolean before,
			SubMilestoneWrapper subMilestone) {
		TaskAttribute itemListAtt;
		if (subMilestone == null) {
			itemListAtt = backlog;
		} else {
			itemListAtt = subMilestone.getWrappedAttribute();
		}
		List<String> subMilestoneValues = new ArrayList<String>(itemListAtt.getValues());
		List<String> idsToMove = new ArrayList<String>();
		for (BacklogItemWrapper item : items) {
			idsToMove.add(item.getId());
		}
		String targetId = null;
		if (target != null) {
			targetId = target.getId();
		}
		if (!subMilestoneValues.containsAll(idsToMove)) {
			// Clean all impacted list attributes in case of move between lists
			removeItemsFromOtherListsBeforeMove(itemListAtt, idsToMove);
		}
		// Now, perform the insertion
		int index;
		if (target == null || !subMilestoneValues.contains(targetId)) {
			index = subMilestoneValues.size();
		} else {
			index = subMilestoneValues.indexOf(targetId);
			if (!before) {
				index++;
			}
		}
		for (String idToMove : idsToMove) {
			if (idToMove.equals(targetId) && subMilestoneValues.contains(targetId)) {
				index--;
				if (!before) {
					index--;
				}
			}
			subMilestoneValues.remove(idToMove);
		}
		subMilestoneValues.addAll(index, idsToMove);
		itemListAtt.setValues(subMilestoneValues);
		fireAttributeChanged(itemListAtt);
	}

	/**
	 * Removes the elements about to be moved from their lists.
	 * 
	 * @param itemListAtt
	 *            TaskAttribute that persists the target list
	 * @param idsToMove
	 *            List of IDs to insert
	 */
	private void removeItemsFromOtherListsBeforeMove(TaskAttribute itemListAtt, List<String> idsToMove) {
		if (itemListAtt != backlog) {
			removeItemsFrom(backlog, idsToMove);
		}
		for (SubMilestoneWrapper milestone : getSubMilestones()) {
			TaskAttribute milestoneAtt = milestone.getWrappedAttribute();
			if (itemListAtt != milestoneAtt) {
				removeItemsFrom(milestoneAtt, idsToMove);
			}
		}
	}

	/**
	 * Remove all the existing IDs from the values of the given {@link TaskAttribute}.
	 * 
	 * @param attribute
	 *            The TaskAttribute to update.
	 * @param idsToMove
	 *            The list of IDs to remove
	 */
	private void removeItemsFrom(TaskAttribute attribute, List<String> idsToMove) {
		List<String> values = new ArrayList<String>(attribute.getValues());
		boolean needsUpdate = values.removeAll(idsToMove);
		if (needsUpdate) {
			attribute.setValues(values);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * An update to perform on a TaskAttribute.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class TaskAttributeUpdate {
		/**
		 * The attribute.
		 */
		private final TaskAttribute attribute;

		/**
		 * The values to set to the attribute.
		 */
		private final List<String> values;

		/**
		 * Constructor.
		 * 
		 * @param attribute
		 *            The attribute
		 * @param values
		 *            The values to set
		 */
		public TaskAttributeUpdate(TaskAttribute attribute, List<String> values) {
			super();
			this.attribute = attribute;
			this.values = values;
		}

		/**
		 * Executes this update by setting the values to the attribute, and returns the updated attribute. No
		 * notification is performed by this method.
		 * 
		 * @return The updated attribute.
		 */
		public TaskAttribute run() {
			attribute.setValues(values);
			return attribute;
		}

	}

	/**
	 * Insert in the first list the given IDs At the relevant index computed according to the target and the
	 * "before" flag.
	 * 
	 * @param att
	 *            The attribute to update (will be modified by the call)
	 * @param idsToMove
	 *            The list of ids to insert
	 * @param index
	 *            The insertion index
	 */
	private void insertBacklogItemIds(TaskAttribute att, List<String> idsToMove, int index) {
		List<String> values = new ArrayList<String>(att.getValues());
		values.addAll(index, idsToMove);
		att.setValues(values);
		fireAttributeChanged(att);
	}

	/**
	 * Compute the insertion index, taken all the parameters into account. This must be called before the
	 * target list is emptied of its former list.
	 * 
	 * @param att
	 *            The attribute to update (will be modified by the call)
	 * @param target
	 *            The target element
	 * @param before
	 *            Whether insertion must take place before or after.
	 * @return the insertion index in the list
	 */
	private int computeInsertionIndex(TaskAttribute att, BacklogItemWrapper target, boolean before) {
		int index;
		List<String> values = new ArrayList<String>(att.getValues());
		if (target != null) {
			index = values.indexOf(target.getId());
			if (index < 0) {
				index = values.size();
			} else if (target != null && values.contains(target.getId())) {
				index--;
			} else if (!before) {
				index++;
			}

		} else {
			index = values.size();
		}
		return index;
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
			att = createAgileAttribute(MILESTONES_TITLE);
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
			att = createAgileAttribute(BACKLOG_TITLE);
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

	/**
	 * Indicates whether the list of elements in the backlog ahs been changed locally, compared to the lates
	 * known repository version.
	 * 
	 * @return <code>true</code> if the list of items in the backlog is different from the latest known remote
	 *         list.
	 */
	public boolean hasBacklogChanged() {
		return backlog.getAttribute(CHANGED) != null;
	}
}
