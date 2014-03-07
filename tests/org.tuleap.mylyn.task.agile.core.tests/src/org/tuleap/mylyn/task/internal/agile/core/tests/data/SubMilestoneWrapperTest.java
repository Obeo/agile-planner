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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link SubMilestoneWrapper}.
 */
public class SubMilestoneWrapperTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Date for tests.
	 */
	private Date testDate;

	/**
	 * The parent wrapper used for the tests.
	 */
	private MilestonePlanningWrapper planning;

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
		testDate = new Date();
		planning = new MilestonePlanningWrapper(taskData.getRoot());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getId()}.
	 */
	@Test
	public void testGetId() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertEquals("an id", wrapper.getId());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getCapacity()}.
	 */
	@Test
	public void testCapacity() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertNull(wrapper.getCapacity());
		wrapper.setCapacity("elephant");
		assertEquals("elephant", wrapper.getCapacity());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getStartDate()}.
	 */
	@Test
	public void testStartDate() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertNull(wrapper.getStartDate());
		wrapper.setStartDate(testDate);
		assertEquals(testDate, wrapper.getStartDate());
	}

	/**
	 * Test method for {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getEndDate()}
	 * .
	 */
	@Test
	public void testEndDate() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertNull(wrapper.getEndDate());
		wrapper.setEndDate(testDate);
		assertEquals(testDate, wrapper.getEndDate());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#addBacklogItem(java.lang.String)}
	 * .
	 */
	@Test
	public void testAddBacklogItem() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		BacklogItemWrapper item = wrapper.addBacklogItem("bi1");
		assertNotNull(item);
		assertEquals("bi1", item.getId());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getOrderedBacklogItems()}.
	 */
	@Test
	public void testGetOrderedBacklogItems() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		BacklogItemWrapper bi1 = wrapper.addBacklogItem("bi1");
		BacklogItemWrapper bi2 = wrapper.addBacklogItem("bi2");
		List<BacklogItemWrapper> items = wrapper.getOrderedBacklogItems();
		assertEquals(2, items.size());
		assertTrue(isEqual(bi1, items.get(0)));
		assertTrue(isEqual(bi2, items.get(1)));
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#getStatusValue()}.
	 */
	@Test
	public void testStatusValue() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertNull(wrapper.getStatusValue());
		wrapper.setStatusValue("some status");
		assertEquals("some status", wrapper.getStatusValue());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#hasContentChanged()}.
	 */
	@Test
	public void testHasContentChanged() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		assertFalse(wrapper.hasContentChanged());

		wrapper.addBacklogItem("bi1");
		assertFalse(wrapper.hasContentChanged());

		wrapper.markReference();
		wrapper.addBacklogItem("bi2");
		assertTrue(wrapper.hasContentChanged());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#hasContentChanged()}.
	 */
	@Test
	public void testHasContentChangedWhenValueChangedThenResetToOldValue() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		wrapper.addBacklogItem("bi1");
		wrapper.markReference();
		assertFalse(wrapper.hasContentChanged());
		wrapper.setBacklogReference(Arrays.asList("bi1", "bi2"));
		assertTrue(wrapper.hasContentChanged());
		wrapper.setBacklogReference(Arrays.asList("bi1"));
		assertFalse(wrapper.hasContentChanged());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#setBacklogReference(java.util.List)}
	 * .
	 */
	@Test
	public void testSetBacklogReference() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		wrapper.setBacklogReference(Arrays.asList("1", "2", "3"));
		TaskAttribute attribute = wrapper.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("1", "2", "3"), attribute.getValues());
	}

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper#markReference()}.
	 */
	@Test
	public void testMarkReference() {
		SubMilestoneWrapper wrapper = planning.addSubMilestone("an id");
		wrapper.addBacklogItem("bi1");
		wrapper.markReference();
		TaskAttribute attribute = wrapper.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("bi1"), attribute.getValues());

		wrapper.addBacklogItem("bi2");
		// Second call must not change the reference since it's already set.
		wrapper.markReference();
		assertEquals(Arrays.asList("bi1"), attribute.getValues());
	}

	private boolean isEqual(BacklogItemWrapper w1, BacklogItemWrapper w2) {
		if (w1 == null) {
			return w2 == null;
		}
		if (w2 == null) {
			return false;
		}
		// CHECKSTYLE:OFF
		return isEqual(w1.getId(), w2.getId()) && isEqual(w1.getDisplayId(), w2.getDisplayId())
				&& isEqual(w1.getInitialEffort(), w2.getInitialEffort())
				&& isEqual(w1.getLabel(), w2.getLabel())
				&& isEqual(w1.getParentDisplayId(), w2.getParentDisplayId())
				&& isEqual(w1.getParentId(), w2.getParentId()) && isEqual(w1.getStatus(), w2.getStatus())
				&& isEqual(w1.getType(), w2.getType())
				&& w1.getWrappedAttribute().equals(w2.getWrappedAttribute());
		// CHECKSTYLE:ON
	}

	private boolean isEqual(String s1, String s2) {
		return s1 == null && s2 == null || s1 != null && s1.equals(s2);
	}
}
