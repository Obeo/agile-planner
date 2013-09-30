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
	 * Suffix used for computing the card list task attribute id.
	 */
	public static final String SUFFIX_CARD_LIST = "cards"; //$NON-NLS-1$

	/**
	 * Suffix used for computing the swimlane item task attribute id.
	 */
	public static final String SUFFIX_SWIMLANE_ITEM = "item"; //$NON-NLS-1$

	/**
	 * The parent card wall.
	 */
	private final CardwallWrapper parent;

	/**
	 * The backlog item of this swimlane.
	 */
	private final SwimlaneItemWrapper swimlaneItem;

	/**
	 * The {@link TaskAttribute} that represents the list of cards in this swimlane.
	 */
	private final TaskAttribute cardList;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that represents the swimlane.
	 */
	protected SwimlaneWrapper(CardwallWrapper parent, TaskAttribute root) {
		super(root);
		this.parent = parent;
		this.cardList = root.getMappedAttribute(getAttributeId(root, SUFFIX_CARD_LIST));
		TaskAttribute swimlaneItemAtt = root.getAttribute(getAttributeId(root, SUFFIX_SWIMLANE_ITEM));
		swimlaneItem = new SwimlaneItemWrapper(this, swimlaneItemAtt);
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent.
	 * @param root
	 *            The {@link TaskAttribute} that represents the swimlane list.
	 * @param id
	 *            The id of the swimlane's backlog item.
	 */
	protected SwimlaneWrapper(CardwallWrapper parent, TaskAttribute root, int id) {
		super(root);
		this.parent = parent;
		cardList = root.createMappedAttribute(getAttributeId(root, SUFFIX_CARD_LIST));
		TaskAttribute swimlaneItemAtt = root
				.createMappedAttribute(getAttributeId(root, SUFFIX_SWIMLANE_ITEM));
		swimlaneItem = new SwimlaneItemWrapper(this, swimlaneItemAtt, id);
	}

	/**
	 * Creates a new task attribute to represent a BacklogItem and returns a wrapper for this new
	 * TaskAttribute. The created TaskAttribute is inserted in the given parent, that must be non-null.
	 * 
	 * @return A wrapper for a newly created TaskAttribute representing a BacklogItem in the given parent.
	 */
	public SwimlaneItemWrapper getSwimlaneItem() {
		return swimlaneItem;
	}

	/**
	 * Creates a new Card wrapper and adds it to the list of cards in this swimlane.
	 * 
	 * @param id
	 *            Id of the card to create.
	 * @return A new wrapper. No control is made as to existing card with this id.
	 */
	public CardWrapper addCard(int id) {
		TaskAttribute cardAtt = cardList.createMappedAttribute(getCardAttributeId());
		cardAtt.getMetaData().setReadOnly(true);
		return new CardWrapper(this, cardAtt, id);
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
	 * @return The unique ID of the {@link TaskAttribute} that represents a card in this swimlane.
	 */
	private String getCardAttributeId() {
		return cardList.getId() + ID_SEPARATOR + cardList.getAttributes().size();
	}

	/**
	 * Returns a Card wrapper for the given ID, or null if it cannot be found.
	 * 
	 * @param id
	 *            Id of the being looked for.
	 * @return A new wrapper, or null if the card with this id doesn't exist.
	 */
	public CardWrapper getCard(int id) {
		TaskAttribute cardAtt = cardList.getAttribute(getCardAttributeId());
		cardAtt.getMetaData().setReadOnly(true);
		return new CardWrapper(this, root, id);
	}

	/**
	 * Provides the list of the cards of this swimlane.
	 * 
	 * @return A list never null of the cards of this swimlane.
	 */
	public List<CardWrapper> getCards() {
		List<CardWrapper> cards = Lists.newArrayList();
		for (TaskAttribute cardAtt : cardList.getAttributes().values()) {
			cards.add(wrapCard(cardAtt));
		}
		return cards;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		parent.fireAttributeChanged(attribute);
	}
}
