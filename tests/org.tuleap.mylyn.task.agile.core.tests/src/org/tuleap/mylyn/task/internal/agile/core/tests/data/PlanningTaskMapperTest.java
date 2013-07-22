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

import java.util.Date;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.PlanningTaskMapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the planning task mapper.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class PlanningTaskMapperTest {

	/**
	 * The task data.
	 */

	private TaskData data;

	/**
	 * The first task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute0;

	/**
	 * The second task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute1;

	/**
	 * The third task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute2;

	/**
	 * The fourth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute3;

	/**
	 * The fifth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute4;

	/**
	 * The sixth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute5;

	/**
	 * The seventh task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute6;

	/**
	 * The eighth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute7;

	/**
	 * The first task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute0;

	/**
	 * The second task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute1;

	/**
	 * The third task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute2;

	/**
	 * The fourth task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute3;

	/**
	 * The fifth task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute4;

	/**
	 * The sixth task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute5;

	/**
	 * The seventh task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute6;

	/**
	 * The eighth task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute7;

	/**
	 * The first container task attribute of type TYPE1.
	 */
	private TaskAttribute tA;

	/**
	 * The first container task attribute of type TYPE2.
	 */
	private TaskAttribute tB;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		TaskRepository taskRepository = new TaskRepository("kind", "repository"); //$NON-NLS-1$ //$NON-NLS-2$
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		data = new TaskData(mapper, "kind", "repository", "id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tA = new TaskAttribute(data.getRoot(), "MyFirstTaskAttribute"); //$NON-NLS-1$

		tA.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute0 = tA.createAttribute("a-0"); //$NON-NLS-1$
		type1TaskAttribute1 = tA.createAttribute("a-1"); //$NON-NLS-1$
		type1TaskAttribute2 = tA.createAttribute("a-2"); //$NON-NLS-1$
		type1TaskAttribute3 = tA.createAttribute("a-3"); //$NON-NLS-1$
		type1TaskAttribute4 = tA.createAttribute("a-4"); //$NON-NLS-1$
		type1TaskAttribute5 = tA.createAttribute("a-5"); //$NON-NLS-1$
		type1TaskAttribute6 = tA.createAttribute("a-6"); //$NON-NLS-1$
		type1TaskAttribute7 = tA.createAttribute("a-7"); //$NON-NLS-1$

		int i = 0;
		type1TaskAttribute0.setValue(String.valueOf(i++));
		type1TaskAttribute1.setValue(String.valueOf(i++));
		type1TaskAttribute2.setValue(String.valueOf(i++));
		type1TaskAttribute3.setValue(String.valueOf(i++));
		type1TaskAttribute4.setValue(String.valueOf(i++));
		type1TaskAttribute5.setValue(String.valueOf(i++));
		type1TaskAttribute6.setValue(String.valueOf(i++));
		type1TaskAttribute7.setValue(String.valueOf(i++));

		type1TaskAttribute0.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute1.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute2.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute3.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute4.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute5.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute6.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type1TaskAttribute7.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		tB = new TaskAttribute(data.getRoot(), IMylynAgileCoreConstants.PREFIX_MILESTONE
				+ "MySecondTaskAttribute"); //$NON-NLS-1$

		tB.getMetaData().setType(IMylynAgileCoreConstants.TYPE_MILESTONE);

		// for each created Milestone we have to affect a task attribute of type "MILESTONE INDEX"

		TaskAttribute milestoneIndex = tB.createAttribute(IMylynAgileCoreConstants.MILESTONE_INDEX);
		milestoneIndex.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		milestoneIndex.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		milestoneIndex.setValue("0"); //$NON-NLS-1$

		type2TaskAttribute0 = tB.createAttribute("b-0"); //$NON-NLS-1$
		type2TaskAttribute1 = tB.createAttribute("b-1"); //$NON-NLS-1$
		type2TaskAttribute2 = tB.createAttribute("b-2"); //$NON-NLS-1$
		type2TaskAttribute3 = tB.createAttribute("b-3"); //$NON-NLS-1$
		type2TaskAttribute4 = tB.createAttribute("b-4"); //$NON-NLS-1$
		type2TaskAttribute5 = tB.createAttribute("b-5"); //$NON-NLS-1$
		type2TaskAttribute6 = tB.createAttribute("b-6"); //$NON-NLS-1$
		type2TaskAttribute7 = tB.createAttribute("b-7"); //$NON-NLS-1$

	}

	/**
	 * Test the new milestone creation.
	 */
	@Test
	public void testCreateMilestone() {

		new PlanningTaskMapper(data).createMilestone("Milestone1", 20, new Date(), new Date()); //$NON-NLS-1$
		new PlanningTaskMapper(data).createMilestone("Milestone2", 15, new Date(), new Date()); //$NON-NLS-1$
		new PlanningTaskMapper(data).createMilestone("Milestone3", 10, new Date(), new Date()); //$NON-NLS-1$
		new PlanningTaskMapper(data).createMilestone("Milestone4", 10, new Date(), new Date()); //$NON-NLS-1$
		new PlanningTaskMapper(data).createMilestone("Milestone5", 10, new Date(), new Date()); //$NON-NLS-1$

		// Testing the milestone indexes shifting

		assertEquals("5", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "MySecondTaskAttribute").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());

		assertEquals("4", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "Milestone1").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());

		assertEquals("3", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "Milestone2").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());

		assertEquals("2", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "Milestone3").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());

		assertEquals("1", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "Milestone4").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());

		assertEquals("0", data.getRoot().getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.PREFIX_MILESTONE + "Milestone5").getAttribute(//$NON-NLS-1$
				IMylynAgileCoreConstants.MILESTONE_INDEX).getValue());
	}

	/**
	 * Tests moving an artifact inside a backlog.
	 */
	@Test
	public void testMoveArtifactInBacklog() {

		new PlanningTaskMapper(data).moveArtifactInBacklog(tA, 2, 6);

		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$

	}

	/**
	 * Tests moving an artifact inside a milestone.
	 */
	@Test
	public void testMoveArtifactInMilestone() {

		int x = 0;
		type2TaskAttribute0.setValue(String.valueOf(x++));
		type2TaskAttribute1.setValue(String.valueOf(x++));
		type2TaskAttribute2.setValue(String.valueOf(x++));
		type2TaskAttribute3.setValue(String.valueOf(x++));
		type2TaskAttribute4.setValue(String.valueOf(x++));
		type2TaskAttribute5.setValue(String.valueOf(x++));
		type2TaskAttribute6.setValue(String.valueOf(x++));
		type2TaskAttribute7.setValue(String.valueOf(x++));

		type2TaskAttribute0.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute1.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute2.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute3.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute4.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute5.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute6.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute7.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		new PlanningTaskMapper(data).moveArtifactInMilestone(tB, 1, 5);

		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("5", type2TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type2TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type2TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type2TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type2TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type2TaskAttribute7.getValue()); //$NON-NLS-1$

	}

	/**
	 * Tests moving an artifact from a Backlog to a milestone.
	 */
	@Test
	public void testmoveArtifactFromBacklogToMilestone() {

		int x = 0;
		type2TaskAttribute0.setValue(String.valueOf(x++));
		type2TaskAttribute1.setValue(String.valueOf(x++));
		type2TaskAttribute2.setValue(String.valueOf(x++));
		type2TaskAttribute3.setValue(String.valueOf(x++));
		type2TaskAttribute4.setValue(String.valueOf(x++));
		type2TaskAttribute5.setValue(String.valueOf(x++));
		type2TaskAttribute6.setValue(String.valueOf(x++));
		type2TaskAttribute7.setValue(String.valueOf(x++));

		type2TaskAttribute0.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute1.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute2.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute3.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute4.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute5.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute6.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		type2TaskAttribute7.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		assertTrue(containsInstance(tA, type1TaskAttribute2));

		assertTrue(!containsInstance(tB, type1TaskAttribute2));

		new PlanningTaskMapper(data).moveArtifactFromBacklogToMilestone(tA, tB, 2, 5);

		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute7.getValue()); //$NON-NLS-1$

		assertTrue(!containsInstance(tA, type1TaskAttribute2));

		assertTrue(containsInstance(tB, type1TaskAttribute2));

		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type2TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type2TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type2TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type2TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type2TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("8", type2TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute2.getValue()); //$NON-NLS-1$

	}

	/**
	 * Indicates whether the parent attribute contains the given child.
	 * 
	 * @param parent
	 *            the parent attribute
	 * @param child
	 *            the child attribute
	 * @return {@code true} if the parent attribute contains the given child.
	 */
	public boolean containsInstance(TaskAttribute parent, TaskAttribute child) {

		if (parent.getAttributes().containsKey(child.getId())) {
			return true;
		}
		return false;
	}
}
