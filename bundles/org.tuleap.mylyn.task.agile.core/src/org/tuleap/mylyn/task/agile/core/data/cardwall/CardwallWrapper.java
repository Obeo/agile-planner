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
	 * Id of the milestone list task attribute.
	 */
	public static final String COLUMN_LIST = "mta_cols"; //$NON-NLS-1$

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String SWIMLANE_LIST = "mta_lanes"; //$NON-NLS-1$

	/**
	 * The attribute that represents the list of the columns.
	 */
	private final TaskAttribute columnList;

	/**
	 * The attribute that represents the list of the swimlanes.
	 */
	private final TaskAttribute swimlaneList;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            the root task attribute of the task data.
	 */
	public CardwallWrapper(TaskAttribute root) {
		super(root);
		TaskAttribute att = root.getAttribute(COLUMN_LIST);
		if (att == null) {
			att = root.createMappedAttribute(COLUMN_LIST);
			att.getMetaData().setReadOnly(true);
		}
		columnList = att;
		att = root.getAttribute(SWIMLANE_LIST);
		if (att == null) {
			att = root.createMappedAttribute(SWIMLANE_LIST);
			att.getMetaData().setReadOnly(true);
		}
		swimlaneList = att;
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
	public ColumnWrapper addColumn(int id, String label) {
		return new ColumnWrapper(this, columnList, id, label);
	}

	/**
	 * Provides the card wall's columns in the right order.
	 * 
	 * @return The card wall's columns, in the right order.
	 */
	public List<ColumnWrapper> getColumns() {
		List<ColumnWrapper> result = Lists.newArrayList();
		for (TaskAttribute att : columnList.getAttributes().values()) {
			result.add(wrapColumn(att));
		}
		return result;
	}

	/**
	 * Returns a {@link ColumnWrapper} for an existing task attribute.
	 * 
	 * @param attribute
	 *            the task attribute to wrap, should represent a cardwall column.
	 * @return a new {@link ColumnWrapper} for the given task attribute.
	 */
	public ColumnWrapper wrapColumn(TaskAttribute attribute) {
		return new ColumnWrapper(this, attribute);
	}

	/**
	 * Provides the card wall's swimlanes in the right order.
	 * 
	 * @return The card wall's swimlanes, in the right order.
	 */
	public List<SwimlaneWrapper> getSwimlanes() {
		List<SwimlaneWrapper> result = Lists.newArrayList();
		for (TaskAttribute att : swimlaneList.getAttributes().values()) {
			result.add(wrapSwimlane(att));
		}
		return result;
	}

	/**
	 * Returns a {@link SwimlaneWrapper} for an existing task attribute.
	 * 
	 * @param attribute
	 *            the task attribute to wrap, should represent a swimlane.
	 * @return a new {@link SwimlaneWrapper} for the given task attribute.
	 */
	public SwimlaneWrapper wrapSwimlane(TaskAttribute attribute) {
		return new SwimlaneWrapper(this, attribute);
	}

	/**
	 * Add a new swimlane to the card wall.
	 * 
	 * @param itemId
	 *            The id of the swimlane's backlog ite.
	 * @return The created wrapper.
	 */
	public SwimlaneWrapper addSwimlane(int itemId) {
		return new SwimlaneWrapper(this, swimlaneList, itemId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractNotifyingWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		// Required for delegation from other classes of this package
		super.fireAttributeChanged(attribute);
	}
}
