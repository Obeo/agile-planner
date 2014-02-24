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

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownData;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests of the {@link BurndownMapper} class.
 */
public class BurndownMapperTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * The test burndown data.
	 */
	private BurndownData burndownData;

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.burndown.BurndownMapper#setBurndownData(org.tuleap.mylyn.task.agile.core.data.burndown.BurndownData)}
	 * .
	 */
	@Test
	public void testSetGetBurndownData() {
		BurndownMapper mapper = new BurndownMapper(taskData);
		mapper.setBurndownData(burndownData);

		BurndownData data = mapper.getBurndownData();

		assertEquals(10, data.getDuration());
		assertEquals(20.0, data.getCapacity(), Double.MIN_VALUE);
		double[] points = data.getPoints();
		assertEquals(3, points.length);
		assertEquals(20.0, points[0], Double.MIN_VALUE);
		assertEquals(21.0, points[1], Double.MIN_VALUE);
		assertEquals(18.0, points[2], Double.MIN_VALUE);
	}

	@Test
	public void testGetBurndownDataThatWasNeverSet() {
		BurndownMapper mapper = new BurndownMapper(taskData);
		assertNull(mapper.getBurndownData());
	}

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);

		burndownData = new BurndownData(10, 20.0, new double[] {20., 21., 18. });
	}

}
