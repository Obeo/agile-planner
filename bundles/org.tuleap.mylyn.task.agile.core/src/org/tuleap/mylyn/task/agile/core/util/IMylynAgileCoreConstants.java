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
	 * The value used to indicate that a task data represents a list of scopes in a planning (for instance, a
	 * list of sprints in a release).
	 */
	String SCOPE_LIST = "mta_planning_scope_list"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a scope (for instance, a sprint).
	 */
	String TYPE_SCOPE = "mta_scope"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of scopes (there are several scopes in a parent Planning).
	 */
	String PREFIX_SCOPE = "mta_scope-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a scope.
	 */
	String SCOPE_NAME = "mta_scope_name"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a scope capacity.
	 */
	String SCOPE_CAPACITY = "mta_scope_capacity"; //$NON-NLS-1$

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
	 * The value used to indicate that a task data represents a scope (for instance, a sprint).
	 */
	String TYPE_BACKLOG_ITEM = "mta_backlog_item"; //$NON-NLS-1$

	/**
	 * Prefix used to generate ids of scopes (there are several scopes in a parent Planning).
	 */
	String PREFIX_BACKLOG_ITEM = "mta_backlog_item-"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the name of a backlog item.
	 */
	String BACKLOG_ITEM_NAME = "mta_backlog_item_name"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	String BACKLOG_ITEM_POINTS = "mta_backlog_item_points"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents the parent of a backlog item.
	 */
	String BACKLOG_ITEM_PARENT = "mta_backlog_item_parent"; //$NON-NLS-1$
}
