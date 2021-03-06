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
package org.tuleap.mylyn.task.internal.agile.core.tests.data;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;

/**
 * Test implementation of {@link ITaskAttributeChangeListener}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TestChangeListener implements ITaskAttributeChangeListener {

	/**
	 * Invocations count map.
	 */
	private Map<String, Integer> invocationsCount = new HashMap<String, Integer>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		Integer count = invocationsCount.get(attribute.getId());
		if (count == null) {
			invocationsCount.put(attribute.getId(), Integer.valueOf(1));
		} else {
			invocationsCount.put(attribute.getId(), Integer.valueOf(count.intValue() + 1));
		}
	}

	/**
	 * Provides the number of times this listener has been called for the given attribute.
	 * 
	 * @param id
	 *            the {@link TaskAttribute} id.
	 * @return The number of times this listener has been called for the given attribute.
	 */
	public Integer getInvocationsCount(String id) {
		return invocationsCount.get(id);
	}
}
