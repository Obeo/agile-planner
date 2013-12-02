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

import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

/**
 * Model of a swimlane.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneModel {

	/**
	 * The card wall.
	 */
	private final CardwallModel cardwall;

	/**
	 * The swimlane.
	 */
	private final SwimlaneWrapper swimlane;

	/**
	 * The swimlane's cells.
	 */
	private List<SwimlaneCell> cells;

	/**
	 * Constructor.
	 * 
	 * @param cardwall
	 *            the cardwall.
	 * @param swimlane
	 *            The swimlane.
	 */
	public SwimlaneModel(CardwallModel cardwall, SwimlaneWrapper swimlane) {
		this.swimlane = swimlane;
		this.cardwall = cardwall;
	}

	/**
	 * The swimlane's header cards.
	 * 
	 * @return The swimlane's header cards.
	 */
	public List<CardWrapper> getHeaderCards() {
		List<CardWrapper> res = new ArrayList<CardWrapper>();
		for (CardWrapper card : swimlane.getCards()) {
			if (card.getColumnId() == null) {
				res.add(card);
			}
		}
		return res;
	}

	/**
	 * The cardwall.
	 * 
	 * @return The cardwall.
	 */
	public CardwallModel getCardwall() {
		return cardwall;
	}

	/**
	 * The swimlane.
	 * 
	 * @return the swimlane.
	 */
	public SwimlaneWrapper getWrapper() {
		return swimlane;
	}

	/**
	 * The swimlane's cells.
	 * 
	 * @return The swimlane's cells.
	 */
	public List<SwimlaneCell> getCells() {
		if (cells == null) {
			cells = Lists.newArrayList();
			for (ColumnModel col : cardwall.getColumns()) {
				cells.add(new SwimlaneCell(this, col));
			}
		}
		return cells;
	}
}
