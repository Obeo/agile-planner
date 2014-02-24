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
package org.tuleap.mylyn.task.agile.core.data.burndown;

import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * Burn-down chart wrapper.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BurndownMapper {

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String TYPE_BURNDOWN = "mta_burndown"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String BURNDOWN_ID = "org.eclipse.mylyn.task.agile.burndown"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String META_DURATION = "org.eclipse.mylyn.task.agile.burndow.duration"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a milestone (for instance, a sprint).
	 */
	public static final String META_CAPACITY = "org.eclipse.mylyn.task.agile.burndow.capacity"; //$NON-NLS-1$

	/**
	 * The TaskData.
	 */
	private final TaskData taskData;

	/**
	 * Constructor.
	 *
	 * @param taskData
	 *            The task data.
	 */
	public BurndownMapper(TaskData taskData) {
		this.taskData = taskData;
	}

	/**
	 * Data setter.
	 *
	 * @param data
	 *            The burn-down data
	 */
	public void setBurndownData(BurndownData data) {
		TaskAttribute att = taskData.getRoot().getAttribute(BURNDOWN_ID);
		if (att == null) {
			att = taskData.getRoot().createAttribute(BURNDOWN_ID);
		} else {
			att.clearValues();
		}
		att.getMetaData().putValue(META_DURATION, Integer.toString(data.getDuration()));
		att.getMetaData().putValue(META_CAPACITY, Double.toString(data.getCapacity()));
		for (double v : data.getPoints()) {
			att.addValue(Double.toString(v));
		}
	}

	/**
	 * Data getter.
	 *
	 * @return the burn-down data
	 */
	public BurndownData getBurndownData() {
		TaskAttribute att = taskData.getRoot().getAttribute(BURNDOWN_ID);
		if (att == null) {
			return null;
		}
		int duration = Integer.parseInt(att.getMetaData().getValue(META_DURATION));
		double capacity = Double.parseDouble(att.getMetaData().getValue(META_CAPACITY));
		List<String> values = att.getValues();
		double[] points = new double[values.size()];
		int i = 0;
		for (String value : values) {
			points[i++] = Double.parseDouble(value);
		}
		return new BurndownData(duration, capacity, points);
	}

}
