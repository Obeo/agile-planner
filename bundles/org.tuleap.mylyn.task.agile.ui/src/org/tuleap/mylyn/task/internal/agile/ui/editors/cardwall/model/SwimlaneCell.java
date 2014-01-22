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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;

/**
 * A cell in a swimlane with a status (not the left column), where cards can be located.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneCell extends AbstractNotifyingModel {

	/**
	 * The swimlane of this cell.
	 */
	private final SwimlaneModel swimlane;

	/**
	 * The column of this cell.
	 */
	private final ColumnModel column;

	/**
	 * Flag indicating whether this column is folded or not.
	 */
	private boolean folded;

	/**
	 * Constructor.
	 * 
	 * @param swimlane
	 *            The swimlane
	 * @param column
	 *            The column
	 */
	public SwimlaneCell(SwimlaneModel swimlane, ColumnModel column) {
		super();
		this.swimlane = swimlane;
		this.column = column;
	}

	/**
	 * Provides the cards located in this cell, filtered according to the cardwall filter criterion.
	 * 
	 * @return The cell's cards
	 */
	public List<CardWrapper> getCards() {
		String filterLowerCase = swimlane.getCardwall().getFilter();
		if (filterLowerCase != null && !filterLowerCase.isEmpty()) {
			filterLowerCase = filterLowerCase.toLowerCase();
		} else {
			filterLowerCase = null;
		}
		List<CardWrapper> res = new ArrayList<CardWrapper>();
		for (CardWrapper card : swimlane.getWrapper().getCards()) {
			String columnId = column.getWrapper().getId();
			if (columnId != null && columnId.equals(card.getColumnId())) {
				if (filterLowerCase == null) {
					res.add(card);
				} else if (filterCard(card, filterLowerCase)) {
					res.add(card);
				}
			}
		}
		return res;
	}

	/**
	 * Indicates whether the given card passes the given filter.
	 * 
	 * @param card
	 *            Card to filter
	 * @param filterLowerCase
	 *            Filter criterion
	 * @return <code>true</code> if and only if the card passes the filter.
	 */
	private boolean filterCard(CardWrapper card, String filterLowerCase) {
		if (card.getLabel() != null && card.getLabel().toLowerCase().contains(filterLowerCase)
				|| card.getDisplayId().toLowerCase().contains(filterLowerCase)) {
			return true;
		}
		boolean result = false;
		for (TaskAttribute att : card.getFieldAttributes()) {
			result = filterAttribute(att, filterLowerCase);
		}
		return result;
	}

	/**
	 * Filter the given attribute.
	 * 
	 * @param att
	 *            Attribute to filter
	 * @param filterLowerCase
	 *            Filter criterion
	 * @return <code>true</code> if and only if the attribute passes the given filter.
	 */
	private boolean filterAttribute(TaskAttribute att, String filterLowerCase) {
		TaskAttributeMapper attributeMapper = att.getTaskData().getAttributeMapper();
		List<String> valueLabels = attributeMapper.getValueLabels(att);
		for (String valueLabel : valueLabels) {
			if (valueLabel != null && valueLabel.toLowerCase().contains(filterLowerCase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The swimlane.
	 * 
	 * @return the swimlane
	 */
	public SwimlaneModel getSwimlane() {
		return swimlane;
	}

	/**
	 * The column.
	 * 
	 * @return the column
	 */
	public ColumnModel getColumn() {
		return column;
	}

	/**
	 * folded state getter.
	 * 
	 * @return the folded
	 */
	public boolean isFolded() {
		return folded;
	}

	/**
	 * folded state setter, does notification.
	 * 
	 * @param folded
	 *            the folded to set
	 */
	public void setFolded(boolean folded) {
		if (this.folded != folded) {
			this.folded = folded;
			mPcs.firePropertyChange(ICardwallProperties.FOLDED, !folded, folded);
		}
	}
}
