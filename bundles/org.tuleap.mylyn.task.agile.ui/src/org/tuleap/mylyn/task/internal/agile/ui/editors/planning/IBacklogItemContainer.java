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
 * Provides a collection of wrapped backlog items .
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IBacklogItemContainer {

	/**
	 * Wrapped backlog items.
	 * 
	 * @return An iterable of wrapped backlog items
	 */
	Iterable<BacklogItemWrapper> getBacklogItems();

	/**
	 * Enclosing milestone planning.
	 * 
	 * @return The enclosing milestone planning wrapper.
	 */
	MilestonePlanningWrapper getMilestonePlanning();
}
