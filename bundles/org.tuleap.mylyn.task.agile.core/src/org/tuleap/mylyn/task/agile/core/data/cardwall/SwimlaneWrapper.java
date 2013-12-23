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
package org.tuleap.mylyn.task.agile.core.data.cardwall;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;

/**
 * Card wall wrapper, encapsulates all the logic of writing and reading {@link TaskAttribute}s in a TaskData
 * and offers a usable API.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Prefix used for swimlanes.
	 */
	public static final String PREFIX_SWIMLANE = "mta_swi-"; //$NON-NLS-1$

	/**
	 * The parent card wall.
	 */
	private final CardwallWrapper parent;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent.
	 * @param att
	 *            The {@link TaskAttribute} that represents the swimlane.
	 */
	protected SwimlaneWrapper(CardwallWrapper parent, TaskAttribute att) {
		super(parent.getRoot(), PREFIX_SWIMLANE, att.getValue());
		Assert.isNotNull(parent);
		this.parent = parent;
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent.
	 * @param id
	 *            The id of the swimlane.
	 */
	protected SwimlaneWrapper(CardwallWrapper parent, String id) {
		super(parent.getRoot(), PREFIX_SWIMLANE, id);
		this.parent = parent;
	}

	/**
	 * Creates a new Card wrapper and adds it to the list of cards in this swimlane.
	 * 
	 * @param id
	 *            Id of the card to create.
	 * @return A new wrapper. No control is made as to existing card with this id.
	 */
	public CardWrapper addCard(String id) {
		return new CardWrapper(this, id);
	}

	/**
	 * Creates a new Card wrapper and adds it to the list of cards in this swimlane.
	 * 
	 * @param cardAtt
	 *            The {@link TaskAttribute} that represents an existing card.
	 * @return A new wrapper.
	 */
	public CardWrapper wrapCard(TaskAttribute cardAtt) {
		return new CardWrapper(this, cardAtt);
	}

	/**
	 * Computes the ID of the {@link TaskAttribute} that represents a card in this swimlane.
	 * 
	 * @param id
	 *            The card's ID.
	 * @return The unique ID of the {@link TaskAttribute} that represents a card in this swimlane.
	 */
	private String getCardAttributeId(String id) {
		return getAttributeId(attribute, id);
	}

	/**
	 * Returns a Card wrapper for the given ID, or null if it cannot be found.
	 * 
	 * @param id
	 *            Id of the being looked for.
	 * @return A new wrapper, or null if the card with this id doesn't exist.
	 */
	public CardWrapper getCard(String id) {
		TaskAttribute cardAtt = root.getAttribute(getCardAttributeId(id));
		return wrapCard(cardAtt);
	}

	/**
	 * Provides the list of the cards of this swimlane.
	 * 
	 * @return A list never null of the cards of this swimlane.
	 */
	public List<CardWrapper> getCards() {
		List<CardWrapper> cards = Lists.newArrayList();
		String prefix = attribute.getId() + '-';
		for (TaskAttribute att : root.getAttributes().values()) {
			if (att.getId().startsWith(prefix) && att.getId().indexOf('-', prefix.length()) == -1) {
				cards.add(wrapCard(att));
			}
		}
		return cards;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		parent.fireAttributeChanged(att);
	}
}
