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
 * This interface will be used to initialize the new milestone that will be created.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface IMilestoneMapping extends ITaskMapping {

	/**
	 * Returns the identifier of the parent milestone of the milestone to create.
	 * 
	 * @return The identifier of the parent milestone of the milestone to create.
	 */
	String getParentMilestoneId();
}
