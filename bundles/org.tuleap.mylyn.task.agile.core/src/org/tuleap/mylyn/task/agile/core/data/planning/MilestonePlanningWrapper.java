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
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;

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
	public List<SubMilestoneWrapper> getSubMilestones() {
		List<SubMilestoneWrapper> result = newArrayList();
		for (TaskAttribute att : submilestoneList.getAttributes().values()) {
			result.add(wrapSubMilestone(att));
		}
		return result;
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
	 * Returns all the backlog items (whether or not they are assigned to a milestone).
	 * 
	 * @return a list of backlog item wrappers, never null but possibly empty.
	 */
	public List<BacklogItemWrapper> getAllBacklogItems() {
		List<BacklogItemWrapper> result = newArrayList();
		for (String attributeId : backlog.getValues()) {
			result.add(wrapBacklogItem(backlog.getAttribute(attributeId)));
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
		for (String attributeId : backlog.getValues()) {
			BacklogItemWrapper bi = wrapBacklogItem(backlog.getAttribute(attributeId));
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
	 * @param attribute
	 *            the task attribute to wrap, should represent a sub-milestone.
	 * @return a new sub-milestone wrapper for the given task attribute.
	 */
	public SubMilestoneWrapper wrapSubMilestone(TaskAttribute attribute) {
		return new SubMilestoneWrapper(this, attribute);
	}

	/**
	 * Returns a backlog item wrapper for an existing task attribute.
	 * 
	 * @param attribute
	 *            the task attribute to wrap, should represent a backlog item.
	 * @return a new backlog wrapper for the given task attribute.
	 */
	public BacklogItemWrapper wrapBacklogItem(TaskAttribute attribute) {
		return new BacklogItemWrapper(this, attribute);
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
		Assert.isNotNull(items);
		if (items.isEmpty()) {
			return;
		}
		String targetId = null;
		if (target != null) {
			targetId = target.getWrappedAttribute().getId();
		}
		List<String> newIds = newArrayList();
		newIds.addAll(backlog.getValues()); // Modifiable copy of ordered ids
		List<String> movedIds = newArrayList();
		for (BacklogItemWrapper bi : items) {
			TaskAttribute wrappedAtt = bi.getWrappedAttribute();
			if (wrappedAtt.getId() != targetId) {
				String movedId = wrappedAtt.getId();
				movedIds.add(movedId);
				newIds.remove(movedId);
				bi.removeAssignedMilestoneId();
			}
		}
		int insertPosition = newIds.size();
		if (targetId != null) {
			insertPosition = newIds.indexOf(targetId);
			if (!before) {
				insertPosition++;
			}
		}
		for (String movedId : movedIds) {
			newIds.add(insertPosition++, movedId);
		}
		backlog.clearValues();
		backlog.setValues(newIds);
		fireAttributeChanged(backlog);
	}

	/**
	 * Moves a list of BacklogItem before or after a target BacklogItem.
	 * 
	 * @param items
	 *            the BacklogItems list, must not be null
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
		Assert.isNotNull(items);
		if (items.isEmpty()) {
			return;
		}
		String targetId = null;
		if (target != null) {
			targetId = target.getWrappedAttribute().getId();
		}
		List<String> newIds = newArrayList();
		newIds.addAll(backlog.getValues()); // Modifiable copy of ordered ids
		List<String> movedIds = newArrayList();
		for (BacklogItemWrapper bi : items) {
			TaskAttribute wrappedAtt = bi.getWrappedAttribute();
			if (wrappedAtt.getId() != targetId) {
				String movedId = wrappedAtt.getId();
				movedIds.add(movedId);
				newIds.remove(movedId);
				bi.setAssignedMilestoneId(subMilestone.getId());
			}
		}
		int insertPosition = newIds.size();
		if (targetId != null) {
			insertPosition = newIds.indexOf(targetId);
			if (!before) {
				insertPosition++;
			}
		}
		for (String movedId : movedIds) {
			newIds.add(insertPosition++, movedId);
		}
		backlog.clearValues();
		backlog.setValues(newIds);
		fireAttributeChanged(backlog);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#getWrappedAttribute()
	 */
	@Override
	public TaskAttribute getWrappedAttribute() {
		return planning;
	}
}
