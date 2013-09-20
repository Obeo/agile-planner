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

import com.google.common.collect.Iterables;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

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
		toMilestone = new TaskAttributeToMilestone();
		toBacklogItem = new TaskAttributeToBacklogItem();
	}

	/**
	 * Creates a new sub-milestone in the wrapped milestone and returns its wrapper.
	 * 
	 * @return A new wrapper for a new task attribute that is created by invoking this method.
	 */
	public SubMilestoneWrapper addSubMilestone() {
		return SubMilestoneWrapper.createSubMilestone(submilestoneList);
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
	 * @return A wrapper for a newly created TaskAttribute representing a BacklogItem in the given parent.
	 */
	public BacklogItemWrapper addBacklogItem() {
		return BacklogItemWrapper.createBacklogItem(backlog);
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

}
