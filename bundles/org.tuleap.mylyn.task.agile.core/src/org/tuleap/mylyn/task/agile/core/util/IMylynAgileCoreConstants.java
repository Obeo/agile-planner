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
	 * The value used to indicate that a task data represents an id.
	 */
	String ID = "mta_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a label (for a milestone, a backlog item, ...).
	 */
	String LABEL = "mta_label"; //$NON-NLS-1$

	/**
	 * The key used to indicate the kind of a mylyn task data.
	 */
	String TASK_KIND_KEY = "mta_kind"; //$NON-NLS-1$

	/**
	 * The value used in a field attribute with the key TASK_KIND_KEY to indicate that a task data represents
	 * a top planning.
	 */
	String KIND_TOP_PLANNING = "mta_top_planning"; //$NON-NLS-1$

	/**
	 * The value used in a field attribute with the key TASK_KIND_KEY to indicate that a task data represents
	 * a milestone.
	 */
	String KIND_MILESTONE = "mta_milestone"; //$NON-NLS-1$

	/**
	 * The value used in a field attribute with the key TASK_KIND_KEY to indicate that a task data represents
	 * a backlog item.
	 */
	String KIND_BACKLOG_ITEM = "mta_backlog_item"; //$NON-NLS-1$
}
