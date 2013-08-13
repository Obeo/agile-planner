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

/**
 * Utility class used to represent an artifact of a backlog item in the cardwall.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public final class CardwallArtifact {

	/**
	 * The identifier of the card wall artifact.
	 */
	private int id;

	/**
	 * The name of the card wall artifact.
	 */
	private String title;

	/**
	 * The kind of the card wall artifact.
	 */
	private String kind;

	/**
	 * The identifier of the card wall tracker.
	 */
	private int trackerId;

	/**
	 * The state value of the card wall artifact.
	 */
	private CardwallStateValue cardwallStateValue;

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The identifier of the cardwall artifact
	 * @param title
	 *            The title of the cardwall artifact
	 * @param kind
	 *            The kind of the cardwall artifact
	 * @param trackerId
	 *            The identifier of the card wall tracker
	 * @param cardwallStateValue
	 *            The state value of the cardwall artifact
	 */
	public CardwallArtifact(int id, String title, String kind, int trackerId,
			CardwallStateValue cardwallStateValue) {
		this.id = id;
		this.title = title;
		this.kind = kind;
		this.trackerId = trackerId;
		this.cardwallStateValue = cardwallStateValue;
	}

	/**
	 * Returns the identifier of the card wall artifact.
	 * 
	 * @return The identifier of the card wall artifact.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the title of the card wall artifact.
	 * 
	 * @return The title of the card wall artifact.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the kind of the card wall artifact.
	 * 
	 * @return The kind of the card wall artifact.
	 */
	public String getKind() {
		return this.kind;
	}

	/**
	 * Returns the identifier of the tracker.
	 * 
	 * @return the identifier of the tracker
	 */
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
	 * Returns the state of the card wall artifact.
	 * 
	 * @return the cardwallStateValue
	 */
	public CardwallStateValue getCardwallStateValue() {
		return this.cardwallStateValue;
	}
}
