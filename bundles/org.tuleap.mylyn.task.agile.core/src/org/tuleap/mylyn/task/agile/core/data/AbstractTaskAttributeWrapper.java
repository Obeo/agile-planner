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
package org.tuleap.mylyn.task.agile.core.data;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Abstract super-class of TaskAttribute wrappers that simplify the mylyn model manipulation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTaskAttributeWrapper {

	/**
	 * The wrapped task attribute.
	 */
	protected final TaskAttribute root;

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 */
	public AbstractTaskAttributeWrapper(TaskAttribute root) {
		this.root = root;
	}

}
