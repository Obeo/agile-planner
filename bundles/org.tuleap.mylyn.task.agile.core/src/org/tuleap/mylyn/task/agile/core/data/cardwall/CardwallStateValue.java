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
 * Utility class used to represent a state value of the card wall artifact.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @since 1.0
 */
public final class CardwallStateValue {

	/**
	 * The identifier of the card wall artifact value.
	 */
	private int fieldId;

	/**
	 * The identifier of the card wall artifact value's field.
	 */
	private int fieldValueId;

	/**
	 * The label of the card wall artifact value.
	 */
	private String fieldLabel;

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The identifier of the card wall artifact value
	 * @param valueId
	 *            The identifier of the card wall artifact value's field
	 * @param label
	 *            The label of the card wall artifact value
	 */
	public CardwallStateValue(final int id, final int valueId, final String label) {
		this.fieldId = id;
		this.fieldValueId = valueId;
		this.fieldLabel = label;
	}

	/**
	 * Returns the identifier of the card wall artifact value.
	 * 
	 * @return The identifier of the card wall artifact value.
	 */
	public int getFieldId() {
		return this.fieldId;
	}

	/**
	 * Returns the identifier of the card wall artifact value's field.
	 * 
	 * @return the fieldValueId
	 */
	public int getFieldValueId() {
		return fieldValueId;
	}

	/**
	 * Returns the label of the card wall artifact value.
	 * 
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
}
