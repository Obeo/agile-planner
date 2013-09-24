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

import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;

/**
 * Provides a collection of wrapped backlog items.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IBacklog {

	/**
	 * Wrapped backlog items.
	 * 
	 * @return An iterable of wrapped backlog items
	 */
	Iterable<BacklogItemWrapper> getBacklogItems();

	/**
	 * Returns the milestone ID associated to this backlog, which is null if this backlog is the planning's
	 * backlog, and is the enclosing milestone's id if the backlog is that of a milestone.
	 * 
	 * @return The enclosing milestone's ID, or null if this is the planning's backlog (not related to a
	 *         sub-milestone).
	 */
	Integer getMilestoneId();

	/**
	 * Enclosing milestone planning.
	 * 
	 * @return The enclosing milestone planning wrapper.
	 */
	MilestonePlanningWrapper getMilestonePlanning();
}
