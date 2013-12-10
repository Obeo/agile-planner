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

import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;

/**
 * Model used by UI for a cardwall column.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ColumnModel extends AbstractNotifyingModel {

	/**
	 * The card wall.
	 */
	private CardwallModel cardwall;

	/**
	 * Column wrapper.
	 */
	private final ColumnWrapper column;

	/**
	 * Flag indicating whether this column is folded or not.
	 */
	private boolean folded;

	/**
	 * Constructor.
	 * 
	 * @param column
	 *            The column wrapper.
	 */
	public ColumnModel(ColumnWrapper column) {
		this.column = column;
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

	/**
	 * column wrapper getter.
	 * 
	 * @return the column
	 */
	public ColumnWrapper getWrapper() {
		return column;
	}

	/**
	 * The column's label.
	 * 
	 * @return The column's label;
	 */
	public String getLabel() {
		return column.getLabel();
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
	 * Cardwall setter.
	 * 
	 * @param cardwall
	 *            The cardwall model
	 */
	public void setCardwall(CardwallModel cardwall) {
		this.cardwall = cardwall;
	}
}
