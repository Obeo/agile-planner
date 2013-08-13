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

import java.util.ArrayList;
import java.util.Collection;

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
	private int id;

	/**
	 * The label of the card wall state.
	 */
	private String label;

	/**
	 * The collection of mappings.
	 */
	private Collection<CardwallStateMapping> mappings = new ArrayList<CardwallStateMapping>();

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
	public CardwallState(int id, String label, Collection<CardwallStateMapping> mappings) {
		this.id = id;
		this.label = label;
		this.mappings.addAll(mappings);
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
	 * Returns the mappings of the card wall state.
	 * 
	 * @return The mappings of the card wall state
	 */
	public Collection<CardwallStateMapping> getMappings() {
		return this.mappings;
	}
}
