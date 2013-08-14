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

import java.util.Collection;

/**
 * Utility class used to represent a card wall state mapping.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public final class CardwallStateMapping {
	/**
	 * The identifier of the tracker.
	 */
	private int trackerId;

	/**
	 * The identifiers of the possible values of the field used to determine the state.
	 */
	private Collection<Integer> stateValuesId;

	/**
	 * The constructor.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param stateValuesId
	 *            The identifiers of the possible values of the field used to determine the state.
	 */
	public CardwallStateMapping(int trackerId, Collection<Integer> stateValuesId) {
		this.trackerId = trackerId;
		this.stateValuesId = stateValuesId;
	}

	/**
	 * Returns the identifier of the tracker.
	 * 
	 * @return The identifier of the tracker
	 */
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
	 * Returns the identifiers of the values of the state.
	 * 
	 * @return The identifiers of the values of the state
	 */
	public Collection<Integer> getStateValuesId() {
		return this.stateValuesId;
	}
}
