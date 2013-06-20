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
public interface IMylynAgileCoreContants {
	/**
	 * The key used to indicate the kind of a mylyn task data.
	 */
	String TASK_KIND_KEY = "mylyn_task_agile_kind"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represent an agile release.
	 */
	String TASK_KIND_RELEASE = "mylyn_task_agile_kind_release"; //$NON-NLS-1$
}
