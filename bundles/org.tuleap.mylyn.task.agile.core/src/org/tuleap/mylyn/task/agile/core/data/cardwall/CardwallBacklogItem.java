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
 * Utility class used to represent a backlog item of the cardwall.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public final class CardwallBacklogItem {

	/**
	 * The identifier of the backlog item.
	 */
	private int id;

	/**
	 * The title of the backlog item.
	 */
	private String title;

	/**
	 * The kind of the backlog item.
	 */
	private String kind;

	/**
	 * The collection of artifacts.
	 */
	private Collection<CardwallArtifact> artifacts = new ArrayList<CardwallArtifact>();

	/**
	 * The cardwall backlog item constructor.
	 * 
	 * @param id
	 *            The cardwall backlog item identifier.
	 * @param title
	 *            The cardwall backlog item title.
	 * @param kind
	 *            The cardwall backlog item kind.
	 */
	public CardwallBacklogItem(int id, String title, String kind) {
		this.id = id;
		this.title = title;
		this.kind = kind;
	}

	/**
	 * Returns the identifier of the cardwall backlog item.
	 * 
	 * @return The identifier of the cardwall backlog item.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the title of the cardwall backlog item.
	 * 
	 * @return The title of the cardwall backlog item.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the kind of the cardwall backlog item.
	 * 
	 * @return The kind of the cardwall backlog item.
	 */
	public String getKind() {
		return this.kind;
	}

	/**
	 * Returns the collection of the cardwall backlog item artifacts, which is never null but can be empty.
	 * Not thread safe.
	 * 
	 * @return the collection of the cardwall backlog item artifacts, never null but possibly empty.
	 */
	public Collection<CardwallArtifact> getArtifacts() {
		return this.artifacts;
	}

	/**
	 * Adds an artifact to the cardwall backlog item.
	 * 
	 * @param artifact
	 *            the artifact to add
	 */
	public void addArtifact(CardwallArtifact artifact) {
		this.artifacts.add(artifact);
	}

}
