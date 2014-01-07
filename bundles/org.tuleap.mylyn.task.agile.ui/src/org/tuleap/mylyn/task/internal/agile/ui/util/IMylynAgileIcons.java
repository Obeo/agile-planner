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
 * This interface will be used to hold the paths of all the icons used in the bundle.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface IMylynAgileIcons {
	/**
	 * Collapse all.
	 */
	String COLLAPSE_ALL = "icons/commons/collapseall.gif"; //$NON-NLS-1$

	/**
	 * Expand all.
	 */
	String EXPAND_ALL = "icons/commons/expandall.gif"; //$NON-NLS-1$

	/**
	 * The cardwall icon.
	 */
	String CARDWALL_16X16 = "icons/editors/cardwall_16x16.png"; //$NON-NLS-1$

	/**
	 * The planning icon.
	 */
	String PLANNING_16X16 = "icons/editors/planning_16x16.png"; //$NON-NLS-1$

	/**
	 * The new milestone icon.
	 */
	String NEW_MILESTONE_16X16 = "icons/editors/planning/new-milestone_16x16.png"; //$NON-NLS-1$

	/**
	 * Collapse a cardwall cell.
	 */
	String COLLAPSE_TSK = "icons/commons/collapse_tsk.gif"; //$NON-NLS-1$

	/**
	 * Expand a cardwall cell.
	 */
	String EXPAND_TSK = "icons/commons/expand_tsk.gif"; //$NON-NLS-1$

	/**
	 * Collapsed (arrow towards right).
	 */
	String DETAILS_CLOSED = "icons/commons/details_closed.gif"; //$NON-NLS-1$

	/**
	 * Expanded (arrow towards bottom).
	 */
	String DETAILS_OPEN = "icons/commons/details_open.gif"; //$NON-NLS-1$

	/**
	 * Collapsed hovered (arrow towards right).
	 */
	String DETAILS_CLOSED_HOVER = "icons/commons/details_closed_hov.gif"; //$NON-NLS-1$

	/**
	 * Expanded hovered (arrow towards bottom).
	 */
	String DETAILS_OPEN_HOVER = "icons/commons/details_open_hov.gif"; //$NON-NLS-1$
}
