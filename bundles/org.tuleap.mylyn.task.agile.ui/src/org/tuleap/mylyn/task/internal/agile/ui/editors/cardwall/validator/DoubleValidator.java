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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * {@link ICellEditorValidator} that checks that the value does represent an integer.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DoubleValidator implements ICellEditorValidator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	@Override
	public String isValid(Object value) {
		if (value instanceof String) {
			try {
				Double.parseDouble((String)value);
				return null;
			} catch (NumberFormatException e) {
				// Nothing to do
			}
		}
		return "Value must be numeric";
	}

}
