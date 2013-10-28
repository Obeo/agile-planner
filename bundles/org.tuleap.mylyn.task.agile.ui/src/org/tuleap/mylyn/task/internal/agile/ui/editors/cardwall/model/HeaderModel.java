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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model;

import java.util.List;

/**
 * Model used to represent a cardwall header.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class HeaderModel {

	/**
	 * The card wall whose header this class represents.
	 */
	private final CardwallModel cardwall;

	/**
	 * Constructor.
	 * 
	 * @param cardwall
	 *            The cardwall.
	 */
	public HeaderModel(CardwallModel cardwall) {
		this.cardwall = cardwall;
	}

	/**
	 * Backlog label accessor.
	 * 
	 * @return the backlog label.
	 */
	public String getbacklogLabel() {
		// TODO
		return "Backlog"; //$NON-NLS-1$
	}

	/**
	 * Columns getter.
	 * 
	 * @return The card wall columns.
	 */
	public List<ColumnModel> getColumns() {
		return cardwall.getColumns();
	}
}
