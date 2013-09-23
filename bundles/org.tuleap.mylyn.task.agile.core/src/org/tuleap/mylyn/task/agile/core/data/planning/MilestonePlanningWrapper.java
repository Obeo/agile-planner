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
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;

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
public final class MilestonePlanningWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String MILESTONE_PLANNING = "mta_planning"; //$NON-NLS-1$

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String MILESTONE_LIST = "mta_milestones"; //$NON-NLS-1$

	/**
	 * Id of the backlog items list task attribute.
	 */
	public static final String BACKLOG = "mta_backlog"; //$NON-NLS-1$

	/**
	 * The attribute that represents the list of the sub-milestones.
	 */
	private final TaskAttribute submilestoneList;

	/**
	 * The attribute that represents the list of the backlog items.
	 */
	private final TaskAttribute backlog;

	/**
	 * The attribute that represents the milestone.
	 */
	private final TaskAttribute planning;

	/**
	 * Function to transform a task attribute into a wrapper.
	 */
	private final TaskAttributeToMilestone toMilestone;

	/**
	 * Function to transform a task attribute into a wrapper.
	 */
	private final TaskAttributeToBacklogItem toBacklogItem;

	/**
	 * Task Attribute Change listeners.
	 */
	private final Set<ITaskAttributeChangeListener> listeners = Sets.newHashSet();

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            The parent task attribute that contains every piece of data related to the wrapped
	 *            milestone.
	 */
	public MilestonePlanningWrapper(final TaskAttribute root) {
		super(root);
		TaskAttribute milestoneAtt = root.getAttribute(MILESTONE_PLANNING);
		if (milestoneAtt == null) {
			milestoneAtt = root.createAttribute(MILESTONE_PLANNING);
			milestoneAtt.getMetaData().setReadOnly(true);
		}
		planning = milestoneAtt;
		TaskAttribute milestonesAtt = milestoneAtt.getAttribute(MILESTONE_LIST);
		if (milestonesAtt == null) {
			milestonesAtt = milestoneAtt.createAttribute(MILESTONE_LIST);
			milestonesAtt.getMetaData().setReadOnly(true);
		}
		TaskAttribute backlogAtt = milestoneAtt.getAttribute(BACKLOG);
		if (backlogAtt == null) {
			backlogAtt = milestoneAtt.createAttribute(BACKLOG);
			backlogAtt.getMetaData().setReadOnly(true);
		}
		backlog = backlogAtt;
		submilestoneList = milestonesAtt;
		toMilestone = new TaskAttributeToMilestone(this);
		toBacklogItem = new TaskAttributeToBacklogItem(this);
	}

	/**
	 * Creates a new sub-milestone in the wrapped milestone and returns its wrapper.
	 * 
	 * @param id
	 *            the submilestone identifier
	 * @return A new wrapper for a new task attribute that is created by invoking this method.
	 */
	public SubMilestoneWrapper addSubMilestone(int id) {
		SubMilestoneWrapper res = SubMilestoneWrapper.createSubMilestone(this, id);
		return res;
	}

	/**
	 * Returns the sub-milestones.
	 * 
	 * @return a never null iterable of sub-milestone wrappers.
	 */
	public Iterable<SubMilestoneWrapper> getSubMilestones() {
		return Iterables.transform(submilestoneList.getAttributes().values(), toMilestone);
	}

	/**
	 * The number of sub-milestones in this planning.
	 * 
	 * @return the number of sub-milestones in this planning.
	 */
	public int submilestonesCount() {
		return submilestoneList.getAttributes().size();
	}

	/**
	 * Creates a new task attribute to represent a BacklogItem and returns a wrapper for this new
	 * TaskAttribute. The created TaskAttribute is inserted in the given parent, that must be non-null.
	 * 
	 * @param id
	 *            the backlogItem identifier
	 * @return A wrapper for a newly created TaskAttribute representing a BacklogItem in the given parent.
	 */
	public BacklogItemWrapper addBacklogItem(int id) {
		return BacklogItemWrapper.createBacklogItem(this, id);
	}

	/**
	 * The number of backlog items in the backlog.
	 * 
	 * @return the number of backlog items in the backlog.
	 */
	public int backlogItemsCount() {
		return backlog.getAttributes().size();
	}

	/**
	 * Returns the backlog items.
	 * 
	 * @return a never null iterable of backlog item wrappers.
	 */
	public Iterable<BacklogItemWrapper> getBacklogItems() {
		return Iterables.transform(backlog.getAttributes().values(), toBacklogItem);
	}

	/**
	 * Provides the sub-milestone with the given id.
	 * 
	 * @param id
	 *            The id of the sub-milestone being looked for
	 * @return The first milestone found with the given id in the list of sub-milestones.
	 */
	public SubMilestoneWrapper getSubMilestone(final int id) {
		Iterator<SubMilestoneWrapper> candidates = Iterators.filter(getSubMilestones().iterator(),
				new Predicate<SubMilestoneWrapper>() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public boolean apply(SubMilestoneWrapper w) {
						return id == w.getId();
					}
				});
		if (candidates.hasNext()) {
			return candidates.next();
		}
		return null;
	}

	/**
	 * Returns a sub-milestone wrapper for an existing task attribute.
	 * 
	 * @param subMilestoneTaskAttribute
	 *            the task attribute to wrap, should represent a sub-milestone.
	 * @return a new sub-milestone wrapper for the given task attribute.
	 */
	public SubMilestoneWrapper wrapSubMilestone(TaskAttribute subMilestoneTaskAttribute) {
		return new SubMilestoneWrapper(this, subMilestoneTaskAttribute);
	}

	/**
	 * Returns a backlog item wrapper for an existing task attribute.
	 * 
	 * @param backlogItemTaskAttribute
	 *            the task attribute to wrap, should represent a backlog item.
	 * @return a new backlog wrapper for the given task attribute.
	 */
	public BacklogItemWrapper wrapBacklogItem(TaskAttribute backlogItemTaskAttribute) {
		return new BacklogItemWrapper(this, backlogItemTaskAttribute);
	}

	/**
	 * Moves a list of BacklogItem before or after a target BacklogItem.
	 * 
	 * @param items
	 *            the BacklogItems list
	 * @param target
	 *            the target BacklogItem
	 * @param before
	 *            a boolean parameter that indicates if moving BacklogItems will be before or after the target
	 */
	public void moveItemsToBacklog(List<BacklogItemWrapper> items, BacklogItemWrapper target, boolean before) {

		List<String> addedItemsIds = new ArrayList<String>();
		int insertposition = 0;
		if (items != null) {
			for (BacklogItemWrapper backlogItemWrapper : items) {
				addedItemsIds.add(Integer.toString(backlogItemWrapper.getId()));
				backlogItemWrapper.removeAssignedMilestoneId();
			}

			if (before) {
				for (String id : backlog.getValues()) {
					if (id == Integer.toString(target.getId())) {
						insertposition = getBacklogItemIndex(target) - 1;
					}
				}
			} else {
				for (String id : backlog.getValues()) {
					if (id == Integer.toString(target.getId())) {
						insertposition = getBacklogItemIndex(target) + 1;
					}
				}
			}
		}
		if (intersect(addedItemsIds, backlog.getValues()).size() == 0) {
			backlog.getValues().addAll(insertposition, addedItemsIds);
		}

	}

	/**
	 * Moves a list of BacklogItem before or after a target BacklogItem.
	 * 
	 * @param items
	 *            the BacklogItems list
	 * @param target
	 *            the target BacklogItem
	 * @param before
	 *            a boolean parameter that indicates if moving BacklogItems will be before or after the target
	 * @param subMilestone
	 *            the subMilestone to which BacklogItems will be moved
	 */
	public void moveItemsToMilestone(List<BacklogItemWrapper> items, BacklogItemWrapper target,
			boolean before, SubMilestoneWrapper subMilestone) {
		List<String> addedItemsIds = new ArrayList<String>();
		int insertposition = 0;
		if (items != null) {
			for (BacklogItemWrapper backlogItemWrapper : items) {
				addedItemsIds.add(Integer.toString(backlogItemWrapper.getId()));
				backlogItemWrapper.setAssignedMilestoneId(subMilestone.getId());
			}

			if (before) {
				for (String id : backlog.getValues()) {
					if (id == Integer.toString(target.getId())) {
						insertposition = getBacklogItemIndex(target) - 1;
					}
				}
			} else {
				for (String id : backlog.getValues()) {
					if (id == Integer.toString(target.getId())) {
						insertposition = getBacklogItemIndex(target) + 1;
					}
				}
			}
		}
		if (intersect(addedItemsIds, backlog.getValues()).size() == 0) {
			backlog.getValues().addAll(insertposition, addedItemsIds);
		}
	}

	/**
	 * Returns the index of a backlogItem.
	 * 
	 * @param target
	 *            the backlogItem
	 * @return the index of the BacklogItem in the list
	 */
	private int getBacklogItemIndex(BacklogItemWrapper target) {
		for (int i = 0; i < backlog.getValues().size(); i++) {
			if (backlog.getValues().get(i) == Integer.toString(target.getId())) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * returns the ordered list of backlogItems assigned to a submilestone.
	 * 
	 * @param subMilestone
	 *            the submilestone
	 * @return the ordered list of BacklogItems assigned to a submilestone
	 */
	public Iterable<BacklogItemWrapper> getMilestoneOrderedBacklogItemsList(SubMilestoneWrapper subMilestone) {

		List<BacklogItemWrapper> result = new ArrayList<BacklogItemWrapper>();
		for (String backlogItemId : backlog.getValues()) {
			for (BacklogItemWrapper backlogItemWrapper : this.getBacklogItems()) {
				if (backlogItemWrapper.getAssignedMilestoneId() != null) {
					if (backlogItemId.equals(Integer.toString(backlogItemWrapper.getId()))
							&& backlogItemWrapper.getAssignedMilestoneId().intValue() == subMilestone.getId()) {
						result.add(backlogItemWrapper);
					}
				}
			}
		}
		return result;
	}

	/**
	 * returns the ordered list of backlogItems not assigned to any submilestone.
	 * 
	 * @return the ordered list of BacklogItems not assigned to any submilestone
	 */
	public Iterable<BacklogItemWrapper> getBacklogOrderedBacklogItemsList() {

		List<BacklogItemWrapper> result = new ArrayList<BacklogItemWrapper>();
		for (String backlogItemId : backlog.getValues()) {
			for (BacklogItemWrapper backlogItemWrapper : this.getBacklogItems()) {
				if (backlogItemWrapper.getAssignedMilestoneId() != null) {
					if (backlogItemId.equals(Integer.toString(backlogItemWrapper.getId()))
							&& backlogItemWrapper.getAssignedMilestoneId() == null) {
						result.add(backlogItemWrapper);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Return the intersection between two lists.
	 * 
	 * @param firstList
	 *            the first list
	 * @param secondList
	 *            the second list
	 * @return the intersection list
	 */
	private List<String> intersect(List<String> firstList, List<String> secondList) {
		List<String> rtnList = new LinkedList<String>();
		for (String element : firstList) {
			if (secondList.contains(element)) {
				rtnList.add(element);
			}
		}
		return rtnList;
	}

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 *            The listener to add
	 * @return {@code true} if and only if the given listener has been added and was not already registered.
	 */
	public boolean addListener(ITaskAttributeChangeListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 *            The listener to remove
	 * @return {@code true} if and only if the listener was registered and has been removed.
	 */
	public boolean removeListener(ITaskAttributeChangeListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Add a collection of listeners.
	 * 
	 * @param someListeners
	 *            The listeners to add
	 */
	public void addAllListeners(Collection<ITaskAttributeChangeListener> someListeners) {
		this.listeners.addAll(someListeners);
	}

	/**
	 * Informs the listeners of the given attribute's modification.
	 * 
	 * @param attribute
	 *            The modified attribute.
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		for (ITaskAttributeChangeListener l : listeners) {
			l.attributeChanged(attribute);
		}
	}

	/**
	 * Provides the wrapped task attribute that represents the planning.
	 * 
	 * @return The wrapped task attribute that represents the planning.
	 */
	public TaskAttribute getPlanningTaskAttribute() {
		return planning;
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

}
