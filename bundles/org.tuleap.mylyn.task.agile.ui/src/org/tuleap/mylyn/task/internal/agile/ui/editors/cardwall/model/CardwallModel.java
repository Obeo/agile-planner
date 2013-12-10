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

import com.google.common.collect.Lists;

import java.util.List;

import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

/**
 * UI model of a cardwall.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallModel {

	/**
	 * The cardwall wrapper.
	 */
	private final CardwallWrapper cardwall;

	/**
	 * The cardwall's columns.
	 */
	private List<ColumnModel> columns;

	/**
	 * The cardwall's swimlanes.
	 */
	private List<SwimlaneModel> swimlanes;

	/**
	 * The UI model of a cardwall.
	 * 
	 * @param cardwall
	 *            the cardwall, must not be null.
	 */
	public CardwallModel(CardwallWrapper cardwall) {
		this.cardwall = cardwall;
	}

	/**
	 * The cardwall.
	 * 
	 * @return the cardwall
	 */
	public CardwallWrapper getWrapper() {
		return cardwall;
	}

	/**
	 * The cardwall's columns.
	 * 
	 * @return The cardwall's columns.
	 */
	public List<ColumnModel> getColumns() {
		if (columns == null) {
			columns = Lists.newArrayList();
			for (ColumnWrapper col : cardwall.getColumns()) {
				ColumnModel columModel = new ColumnModel(col);
				columModel.setCardwall(this);
				columns.add(columModel);
			}
		}
		return columns;
	}

	/**
	 * The cardwall's swimlanes.
	 * 
	 * @return The cardwall's swimlanes.
	 */
	public List<SwimlaneModel> getSwimlanes() {
		if (swimlanes == null) {
			swimlanes = Lists.newArrayList();
			for (SwimlaneWrapper s : cardwall.getSwimlanes()) {
				swimlanes.add(new SwimlaneModel(this, s));
			}
		}
		return swimlanes;
	}

}
