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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * Utility class used to represent a state of the card wall.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public final class CardwallState {

	/**
	 * The identifier of the card wall state.
	 */
	private final int id;

	/**
	 * The label of the card wall state.
	 */
	private final String label;

	/**
	 * state value Ids mapped to this CardwallState, indexed by trackerId.
	 */
	private final ListMultimap<Integer, Integer> stateValueIdsByTrackerId = ArrayListMultimap.create();

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The identifier.
	 * @param label
	 *            The label
	 * @param mappings
	 *            The mappings
	 */
	public CardwallState(int id, String label, Iterable<CardwallStateMapping> mappings) {
		this.id = id;
		this.label = label;
		for (CardwallStateMapping mapping : mappings) {
			stateValueIdsByTrackerId.putAll(Integer.valueOf(mapping.getTrackerId()), mapping
					.getStateValuesId());
		}
	}

	/**
	 * Returns the identifier of the card wall state.
	 * 
	 * @return The identifier of the card wall state.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the label of the card wall state.
	 * 
	 * @return The label of the card wall state.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Returns the first stateValueId mapped to this card wall state for the given tracker.
	 * 
	 * @param trackerId
	 *            id of the tracker
	 * @return the first stateValueId mapped to this card wall state for the given tracker
	 */
	public int getFirstStateValueIdForTracker(int trackerId) {
		return stateValueIdsByTrackerId.get(Integer.valueOf(trackerId)).get(0).intValue();
	}

	/**
	 * Indicates whether the given stateValueId of the given tracker is mapped to this card wall state.
	 * 
	 * @param trackerId
	 *            the tracker id
	 * @param stateValueId
	 *            the state value id
	 * @return {@code true} if and only if the given arguments are mapped to this card wall state.
	 */
	public boolean containsStateValue(int trackerId, int stateValueId) {
		return stateValueIdsByTrackerId.containsEntry(Integer.valueOf(trackerId), Integer
				.valueOf(stateValueId));
	}

	/**
	 * Returns the collection of actual (trackerId, stateValueIds) mappings mapped to this card wall state.
	 * 
	 * @return the collection of actual (trackerId, stateValueIds) mappings mapped to this card wall state,
	 *         which is recomputed each time this method is invoked.
	 */
	public Collection<CardwallStateMapping> getMappings() {
		Collection<CardwallStateMapping> result = Lists.newArrayList();
		for (Entry<Integer, Collection<Integer>> entry : stateValueIdsByTrackerId.asMap().entrySet()) {
			result.add(new CardwallStateMapping(entry.getKey().intValue(), entry.getValue()));
		}
		return result;
	}
}
