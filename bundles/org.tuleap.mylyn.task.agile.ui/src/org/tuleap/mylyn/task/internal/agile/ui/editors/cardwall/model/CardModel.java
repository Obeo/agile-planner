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

import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;

/**
 * Model of a card for the cardwall UI.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardModel extends AbstractNotifyingModel {

	/**
	 * Status indicating whether the card is folded or not.
	 */
	private boolean folded;

	/**
	 * The card wrapper.
	 */
	private final CardWrapper wrapper;

	/**
	 * Model of a card, with a wrapper and a folded status.
	 * 
	 * @param wrapper
	 *            the card wrapper.
	 */
	public CardModel(CardWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * Getter of folded status.
	 * 
	 * @return the folded
	 */
	public boolean isFolded() {
		return folded;
	}

	/**
	 * Setter of folded status.
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
	 * Card wrapper getter.
	 * 
	 * @return the wrapper
	 */
	public CardWrapper getWrapper() {
		return wrapper;
	}

}
