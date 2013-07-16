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
package org.tuleap.mylyn.task.agile.core.util;

/**
 * This interface is a container of constants used accross the Mylyn Tasks Agile Core bundle.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface IMylynAgileCoreConstants {
	/**
	 * The key used to indicate the kind of a mylyn task data.
	 */
	String TASK_KIND_KEY = "mta_kind"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represent an agile release.
	 */
	String TASK_KIND_RELEASE = "mta_kind_release"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	String TYPE_MILESTONE = "mta_milestone"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	String PREFIX_MILESTONE = "mta_milestone-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a milestone.
	 */
	String MILESTONE_NAME = "mta_milestone_name"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone capacity.
	 */
	String MILESTONE_CAPACITY = "mta_milestone_capacity"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the milestone's index.
	 */
	String MILESTONE_INDEX = "mta_milestone_index"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a planned start date.
	 */
	String START_DATE = "mta_start_date"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a planned end date.
	 */
	String END_DATE = "mta_end_date"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a list of backlog items in a backlog (for
	 * instance, a list of user stories in a backlog).
	 */
	String BACKLOG_ITEM_LIST = "mta_backlog_item_list"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	String TYPE_BACKLOG_ITEM = "mta_backlog_item"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of milestones (there are several milestones in a parent Planning).
	 */
	String PREFIX_BACKLOG_ITEM = "mta_backlog_item-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the Tuleap ID of a backlog item.
	 */
	String BACKLOG_ITEM_ID = "mta_backlog_item_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a backlog item.
	 */
	String BACKLOG_ITEM_NAME = "mta_backlog_item_name"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of the type of a backlog. This name
	 * will be usable for labels, for example "Release Backlog" if the backlog is a release backlog.
	 */
	String BACKLOG_TYPE_LABEL = "mta_backlog_type_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of the type of a backlog item. This
	 * name will be usable for labels, for example "User Story" if the backlog item is a user story.
	 */
	String BACKLOG_ITEM_TYPE_LABEL = "mta_backlog_item_type_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the label to use for points ("Story Points" for
	 * example).
	 */
	String BACKLOG_ITEM_POINTS_LABEL = "mta_backlog_item_points_label"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	String BACKLOG_ITEM_POINTS = "mta_backlog_item_points"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the parent of a backlog item.
	 */
	String BACKLOG_ITEM_PARENT = "mta_backlog_item_parent"; //$NON-NLS-1$

	/**
	 * The value used to describe the kind of the backlog items contained in the milestone.
	 */
	String MILESTONE_BACKLOG_ITEMS_KIND = "mta_milestone_backlog_items_kind"; //$NON-NLS-1$

	/**
	 * The prefix used for the identifier of the milestone state.
	 */
	String PREFIX_MILESTONE_STATE = "mta_milestone_state_prefix-"; //$NON-NLS-1$

	/**
	 * The value used to specify the type of a milestone state.
	 */
	String TYPE_MILESTONE_STATE = "mta_type_milestone_state"; //$NON-NLS-1$

	/**
	 * The value used as a prefix for all the state mapping of a milestone.
	 */
	String PREFIX_MILESTONE_STATE_MAPPING = "mta_milestone_state_mapping_prefix-"; //$NON-NLS-1$

	/**
	 * The value used to specify the type of a milestones state mapping.
	 */
	String TYPE_MILESTONE_STATE_MAPPING = "mta_type_milestone_state_mapping"; //$NON-NLS-1$

	/**
	 * The value used to indicate that the task attribute represents a tracker id.
	 */
	String TRACKER_ID = "mta_tracker_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that the task attribute represents the ids of the values of the states used
	 * in the mapping.
	 */
	String MILESTONE_STATE_MAPPING_STATE_VALUES_IDS = "mta_milestone_state_mapping_state_values_ids"; //$NON-NLS-1$
}
