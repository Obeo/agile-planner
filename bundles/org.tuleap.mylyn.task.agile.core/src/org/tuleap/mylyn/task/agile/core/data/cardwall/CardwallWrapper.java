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

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper;

/**
 * Card wall wrapper, encapsulates all the logic of writing and reading {@link TaskAttribute}s in a TaskData
 * and offers a usable API.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallWrapper extends AbstractNotifyingWrapper {

	/**
	 * Id of the cardwall task attribute.
	 */
	public static final String CARDWALL = "mta_cardwall"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            the root task attribute of the task data.
	 */
	public CardwallWrapper(TaskAttribute root) {
		super(root, CARDWALL, ""); //$NON-NLS-1$
	}

	/**
	 * Add a new column to the card wall. No control is made so don't add the same column twice!
	 * 
	 * @param id
	 *            The id of the column.
	 * @param label
	 *            The label of the column.
	 * @return The created wrapper.
	 */
	public ColumnWrapper addColumn(String id, String label) {
		return new ColumnWrapper(this, id, label);
	}

	/**
	 * Provides the card wall's columns in the right order.
	 * 
	 * @return The card wall's columns, in the right order.
	 */
	public List<ColumnWrapper> getColumns() {
		List<ColumnWrapper> result = Lists.newArrayList();
		for (TaskAttribute att : root.getAttributes().values()) {
			if (att.getId().startsWith(ColumnWrapper.PREFIX_COLUMN)
					&& att.getId().indexOf('-', ColumnWrapper.PREFIX_COLUMN.length()) == -1) {
				result.add(wrapColumn(att));
			}
		}
		return result;
	}

	/**
	 * Returns a {@link ColumnWrapper} for an existing task attribute.
	 * 
	 * @param att
	 *            the task attribute to wrap, should represent a cardwall column.
	 * @return a new {@link ColumnWrapper} for the given task attribute.
	 */
	public ColumnWrapper wrapColumn(TaskAttribute att) {
		return new ColumnWrapper(this, att);
	}

	/**
	 * Provides the card wall's swimlanes in the right order.
	 * 
	 * @return The card wall's swimlanes, in the right order.
	 */
	public List<SwimlaneWrapper> getSwimlanes() {
		List<SwimlaneWrapper> result = Lists.newArrayList();
		for (TaskAttribute att : root.getAttributes().values()) {
			if (att.getId().startsWith(SwimlaneWrapper.PREFIX_SWIMLANE)
					&& att.getId().indexOf('-', SwimlaneWrapper.PREFIX_SWIMLANE.length()) == -1) {
				result.add(wrapSwimlane(att));
			}
		}
		return result;
	}

	/**
	 * Returns a {@link SwimlaneWrapper} for an existing task attribute.
	 * 
	 * @param att
	 *            the task attribute to wrap, should represent a swimlane.
	 * @return a new {@link SwimlaneWrapper} for the given task attribute.
	 */
	public SwimlaneWrapper wrapSwimlane(TaskAttribute att) {
		return new SwimlaneWrapper(this, att);
	}

	/**
	 * Add a new swimlane to the card wall.
	 * 
	 * @param swimlaneId
	 *            The id of the swimlane.
	 * @return The created wrapper.
	 */
	public SwimlaneWrapper addSwimlane(String swimlaneId) {
		return new SwimlaneWrapper(this, swimlaneId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		// Required for delegation from other classes of this package
		super.fireAttributeChanged(att);
	}
}
