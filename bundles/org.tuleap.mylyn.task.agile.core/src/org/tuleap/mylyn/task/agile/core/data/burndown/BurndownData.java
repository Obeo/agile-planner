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

import org.eclipse.core.runtime.Assert;

/**
 * Burn-down chart model.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BurndownData {

	/**
	 * The duration.
	 */
	private final int duration;

	/**
	 * The capacity.
	 */
	private final double capacity;

	/**
	 * The values.
	 */
	private final double[] points;

	/**
	 * Constructor.
	 *
	 * @param duration
	 *            The duration
	 * @param capacity
	 *            The capacity
	 * @param points
	 *            The values
	 */
	public BurndownData(int duration, double capacity, double[] points) {
		this.duration = duration;
		this.capacity = capacity;
		Assert.isNotNull(points);
		this.points = points.clone();
	}

	/**
	 * Duration getter.
	 *
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Capacity getter.
	 *
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * Points getter.
	 *
	 * @return the points
	 */
	public double[] getPoints() {
		return points.clone();
	}

}
