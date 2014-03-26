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

import com.google.common.base.Predicate;

import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * Predicate that filters cards according to a String criterion.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFilter implements Predicate<CardWrapper> {

	/**
	 * The filtering criterion.
	 */
	private final String filterLowerCase;

	/**
	 * Constructor.
	 *
	 * @param filter
	 *            The filter used to filter cards. Must be non-null and non-empty. Otherwise, no need to
	 *            filter anything.
	 * @throws IllegalArgumentException
	 *             if filter is null or empty.
	 */
	public CardFilter(String filter) throws IllegalArgumentException {
		if (filter == null || filter.length() == 0) {
			throw new IllegalArgumentException("Dev error: Filter must be non-null and non-empty"); //$NON-NLS-1$
		}
		this.filterLowerCase = filter.toLowerCase();
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * Only cards that contain the filter in their display ID, label, description, or one of their field
	 * labels will pass the filter.
	 * </p>
	 *
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(CardWrapper card) {
		if (card.getLabel() != null && card.getLabel().toLowerCase().contains(filterLowerCase)
				|| card.getDisplayId().toLowerCase().contains(filterLowerCase)) {
			return true;
		}
		boolean matchFound = false;
		for (TaskAttribute att : card.getFieldAttributes()) {
			if (filterAttribute(att)) {
				matchFound = true;
				break;
			}
		}
		return matchFound;
	}

	/**
	 * Filter the given attribute.
	 *
	 * @param att
	 *            Attribute to filter
	 * @return <code>true</code> if and only if the attribute passes the given filter.
	 */
	private boolean filterAttribute(TaskAttribute att) {
		TaskAttributeMapper attributeMapper = att.getTaskData().getAttributeMapper();
		List<String> valueLabels = attributeMapper.getValueLabels(att);
		for (String valueLabel : valueLabels) {
			if (valueLabel != null && valueLabel.toLowerCase().contains(filterLowerCase)) {
				return true;
			}
		}
		return false;
	}

}
