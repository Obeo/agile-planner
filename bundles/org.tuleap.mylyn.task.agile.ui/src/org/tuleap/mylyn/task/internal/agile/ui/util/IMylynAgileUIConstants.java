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
package org.tuleap.mylyn.task.internal.agile.ui.util;

/**
 * Useful constants for the Agile Planner UI are here.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IMylynAgileUIConstants {

	/**
	 * The PlanningTaskEditorPart identifier.
	 */
	String PLANNING_TASK_EDITOR_PART_DESC_ID = "MylynAgileUIPlanning"; //$NON-NLS-1$

	/**
	 * Default width of column "Points".
	 */
	int DEFAULT_POINTS_COL_WIDTH = 80;

	/**
	 * Default width of column "Id".
	 */
	int DEFAULT_ID_COL_WIDTH = 40;

	/**
	 * Default width of column "Label".
	 */
	int DEFAULT_LABEL_COL_WIDTH = 250;

	/**
	 * Default width of column "Parent".
	 */
	int DEFAULT_PARENT_COL_WIDTH = 80;
}
