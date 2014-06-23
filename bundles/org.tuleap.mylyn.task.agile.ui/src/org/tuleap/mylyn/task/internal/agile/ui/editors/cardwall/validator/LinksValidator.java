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
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * {@link ICellEditorValidator} that checks that the value does represent a list of Integers separated by ", "
 * .
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class LinksValidator implements ICellEditorValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	@Override
	public String isValid(Object value) {
		String result = null;
		if (value instanceof String) {
			for (String link : ((String)value).split(", ")) { //$NON-NLS-1$
				try {
					Integer.parseInt(link);
				} catch (NumberFormatException e) {
					result = MylynAgileUIMessages.getString("LinksValidator.LinksValuesMustBeIntegers"); //$NON-NLS-1$
				}
			}
			//$NON-NLS-1$

		} else if (!(value instanceof Integer) && !(value instanceof Short) && !(value instanceof Long)
				&& !(value instanceof Byte)) {
			result = MylynAgileUIMessages.getString("LinksValidator.LinksValuesMustBeIntegers"); //$NON-NLS-1$
		}
		return result;
	}

}
