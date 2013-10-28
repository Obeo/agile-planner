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

/**
 * Cardwall event.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallEvent {

	/**
	 * The property whose modification caused this event.
	 */
	private final Type type;

	/**
	 * The event source.
	 */
	private final Object source;

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            The type of the event.
	 * @param source
	 *            The event source.
	 */
	public CardwallEvent(Type type, Object source) {
		this.type = type;
		this.source = source;
	}

	/**
	 * The property.
	 * 
	 * @return the property
	 */
	public Type getType() {
		return type;
	}

	/**
	 * The source.
	 * 
	 * @return the source.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Available cardwall properties.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static enum Type {
		/**
		 * A change of folding for a column or a cell.
		 */
		FOLDING_CHANGED,
		/**
		 * A change of configurable field value.
		 */
		FIELD_VALUE_CHANGED;
	}
}
