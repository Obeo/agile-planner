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
package org.tuleap.mylyn.task.agile.core;

import org.eclipse.mylyn.tasks.core.ITaskMapping;

/**
 * This interface will be used to initialize the new backlogItem that will be created.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public interface IBacklogItemMapping extends ITaskMapping {

	/**
	 * Returns the identifier of the parent milestone of the backlogItem to create.
	 *
	 * @return The identifier of the parent milestone of the backlogItem to create.
	 */
	String getParentMilestoneId();
}
