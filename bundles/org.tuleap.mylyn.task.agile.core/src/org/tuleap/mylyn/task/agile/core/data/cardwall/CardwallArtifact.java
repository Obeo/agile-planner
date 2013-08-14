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
	 * The id of the state value of the card wall artifact.
	 */
	private int stateValueId;

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
	 * @param stateValueId
	 *            The state value id of the cardwall artifact
	 */
	public CardwallArtifact(int id, String title, String kind, int trackerId, int stateValueId) {
		this.id = id;
		this.title = title;
		this.kind = kind;
		this.trackerId = trackerId;
		this.stateValueId = stateValueId;
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
	public int getStateValueId() {
		return this.stateValueId;
	}

	/**
	 * Sets the state value id.
	 * 
	 * @param stateValueId
	 *            the state value id.
	 */
	public void setStateValueId(int stateValueId) {
		this.stateValueId = stateValueId;
	}

}
