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
package org.tuleap.mylyn.task.internal.agile.ui.editors.planning;

import com.google.common.base.Predicate;

import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;

/**
 * Utility class providing filters.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class PlanningFilters {

	/**
	 * Predicate that is {@code true} if the filtered backlog item is not assigned to a milestone.
	 */
	private static final FilterUnassigned FILTER_UNASSIGNED = new FilterUnassigned();

	/**
	 * Private constructor to prevent instantiation.
	 */
	private PlanningFilters() {
		// Prevent instantiation
	}

	/**
	 * Returns a predicate that is {@code true} if the filtered backlog item is assigned to milestone with id
	 * {@code milestoneId}.
	 * 
	 * @param milestoneId
	 *            The milestone id
	 * @return A Guava predicate.
	 */
	public static Predicate<BacklogItemWrapper> assignedTo(String milestoneId) {
		return new FilterAssignedTo(milestoneId);
	}

	/**
	 * Returns a predicate that is {@code true} if the filtered backlog item is not assigned to a milestone.
	 * 
	 * @return A Guava predicate.
	 */
	public static Predicate<BacklogItemWrapper> unassigned() {
		return FILTER_UNASSIGNED;
	}

	/**
	 * Predicate used to filter unassigned backlog items.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class FilterUnassigned implements Predicate<BacklogItemWrapper> {
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(BacklogItemWrapper wrapper) {
			return wrapper.getAssignedMilestoneId() == null;
		}
	}

	/**
	 * Predicate used to filter backlog items assigned to a milestone with a given id.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class FilterAssignedTo implements Predicate<BacklogItemWrapper> {

		/**
		 * The milestone id to which a backlog item must be assigned to be accepted.
		 */
		private final String milestoneId;

		/**
		 * Constructor.
		 * 
		 * @param milestoneId
		 *            The milestone id
		 */
		public FilterAssignedTo(String milestoneId) {
			this.milestoneId = milestoneId;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(BacklogItemWrapper wrapper) {
			String assignedId = wrapper.getAssignedMilestoneId();
			if (assignedId != null) {
				return assignedId.equals(milestoneId);
			}
			return false;
		}
	}
}
