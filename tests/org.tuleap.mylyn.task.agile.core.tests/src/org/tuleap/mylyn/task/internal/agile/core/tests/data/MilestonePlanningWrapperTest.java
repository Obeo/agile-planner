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
import java.util.Iterator;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the milestone planning task mapper.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestonePlanningWrapperTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Date for tests.
	 */
	private Date testDate;

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper to ensure the TaskAttribute structure is
	 * correct when creating submilestones is allowed.
	 */
	@Test
	public void testAllowedMilestoneCreation() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("200");
		subMilestone.setCapacity("20");
		subMilestone.setLabel("Milestone 1"); //$NON-NLS-1$
		subMilestone.setStartDate(testDate);
		Date endDate = new Date(testDate.getTime() + 1000 * 60 * 60 * 24 * 11);
		subMilestone.setEndDate(endDate);
		subMilestone.setStatusValue("current");
		subMilestone.setStatus("Open");

		assertEquals("20", subMilestone.getCapacity());
		assertEquals("Milestone 1", subMilestone.getLabel());
		assertEquals(testDate, subMilestone.getStartDate());
		assertEquals(endDate, subMilestone.getEndDate());
		assertEquals("current", subMilestone.getStatusValue());
		assertEquals("Open", subMilestone.getStatus());

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);

		// Milestones
		TaskAttribute milestoneListAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		assertNotNull(milestoneListAtt);
		TaskAttribute milestone0 = root.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "200"); //$NON-NLS-1$
		assertTrue(milestone0.getMetaData().isReadOnly());

		TaskAttribute capacity = root.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_CAPACITY);
		assertEquals("20", capacity.getValue());
		assertEquals(TaskAttribute.TYPE_SHORT_TEXT, capacity.getMetaData().getType());

		TaskAttribute id = root.getAttribute(milestone0.getId());
		assertEquals(TaskAttribute.TYPE_SHORT_TEXT, id.getMetaData().getType());

		TaskAttribute label = root.getAttribute(milestone0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertEquals("Milestone 1", label.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, label.getMetaData().getType());

		TaskAttribute start = root.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_START_DATE);
		assertEquals(Long.toString(testDate.getTime()), start.getValue());
		assertEquals(TaskAttribute.TYPE_DATETIME, start.getMetaData().getType());

		TaskAttribute end = root.getAttribute(milestone0.getId() + '-' + SubMilestoneWrapper.SUFFIX_END_DATE);
		assertEquals(Long.toString(endDate.getTime()), end.getValue());
		assertEquals(TaskAttribute.TYPE_DATETIME, end.getMetaData().getType());

		TaskAttribute status = root.getAttribute(milestone0.getId() + '-'
				+ SubMilestoneWrapper.SUFFIX_STATUS_VALUE);
		assertEquals("current", status.getValue());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, status.getMetaData().getType());

		String label1 = "label of backlog item 300"; //$NON-NLS-1$

		// Backlog
		BacklogItemWrapper backlogItem = subMilestone.addBacklogItem("300");
		backlogItem.setLabel(label1);
		backlogItem.setInitialEffort("5");

		TaskAttribute backlogAtt = root.getAttribute(MilestonePlanningWrapper.BACKLOG);
		TaskAttribute bi0 = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "300"); //$NON-NLS-1$
		assertTrue(bi0.getMetaData().isReadOnly());

		TaskAttribute pointsAtt = root.getAttribute(bi0.getId() + '-'
				+ BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
		assertEquals("5", pointsAtt.getValue());
		assertEquals(TaskAttribute.TYPE_SHORT_TEXT, pointsAtt.getMetaData().getType());

		TaskAttribute biIdAtt = root.getAttribute(bi0.getId());
		assertEquals(TaskAttribute.TYPE_SHORT_TEXT, biIdAtt.getMetaData().getType());

		TaskAttribute biLabelAtt = root.getAttribute(bi0.getId() + '-'
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertEquals(label1, biLabelAtt.getValue());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, biLabelAtt.getMetaData().getType());
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper to ensure the TaskAttribute structure is
	 * correct when creating submilestones is not allowed.
	 */
	@Test(expected = IllegalStateException.class)
	public void testNotAllowedMilestoneCreation() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addSubMilestone("200");
	}

	/**
	 * Tests the setting and reading of titles in the milestone planning wrapper.
	 */
	@Test
	public void testPlanningTitles() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		assertNull(wrapper.getMilestonesTitle());
		assertNull(wrapper.getBacklogTitle());
		wrapper.setBacklogTitle("Release Backlog");
		wrapper.setMilestonesTitle("Sprints");
		assertEquals("Release Backlog", wrapper.getBacklogTitle());
		assertEquals("Sprints", wrapper.getMilestonesTitle());
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper for writing and reading.
	 */
	@Test
	public void testReadAndWrite() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("200");
		subMilestone.setCapacity("20");
		String label0 = "Milestone 1"; //$NON-NLS-1$
		subMilestone.setLabel(label0);
		subMilestone.setStartDate(testDate);
		Date endDate = new Date(testDate.getTime() + 1000 * 60 * 60 * 24 * 11);
		subMilestone.setEndDate(endDate);
		BacklogItemWrapper backlogItem = subMilestone.addBacklogItem("300");
		String label1 = "label of backlog item 300"; //$NON-NLS-1$
		backlogItem.setLabel(label1);
		backlogItem.setInitialEffort("5");

		wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		Iterator<SubMilestoneWrapper> subMilestones = wrapper.getSubMilestones().iterator();
		subMilestone = subMilestones.next();
		assertEquals("200", subMilestone.getId());
		assertEquals(label0, subMilestone.getLabel());
		assertEquals("20", subMilestone.getCapacity());
		assertEquals(testDate, subMilestone.getStartDate());
		assertEquals(endDate, subMilestone.getEndDate());

		assertFalse(subMilestones.hasNext());

		Iterator<BacklogItemWrapper> backlogItems = wrapper.getAllBacklogItems().iterator();
		backlogItem = backlogItems.next();
		assertEquals("300", backlogItem.getId());
		assertEquals(label1, backlogItem.getLabel());
		assertEquals("5", backlogItem.getInitialEffort());

		assertFalse(backlogItems.hasNext());
	}

	/**
	 * Tests the basic manipulation of a MilestonePlanningWrapper without optional fields.
	 */
	@Test
	public void testMilestoneWithoutOptionalFields() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		// Date testDate = new Date();
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("-1");
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("-1");
		// System.out.println(taskData.getRoot());

		wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		assertEquals(1, wrapper.submilestonesCount());
		Iterator<SubMilestoneWrapper> subMilestones = wrapper.getSubMilestones().iterator();
		subMilestone = subMilestones.next();
		assertEquals("-1", subMilestone.getId());
		assertNull(subMilestone.getLabel());
		assertNull(subMilestone.getCapacity());
		assertNull(subMilestone.getStartDate());
		assertNull(subMilestone.getEndDate());

		assertFalse(subMilestones.hasNext());

		assertEquals(1, wrapper.backlogItemsCount());
		Iterator<BacklogItemWrapper> backlogItems = wrapper.getAllBacklogItems().iterator();
		backlogItem = backlogItems.next();
		assertEquals("-1", backlogItem.getId());
		assertNull(backlogItem.getLabel());
		assertNull(backlogItem.getInitialEffort());

		assertFalse(backlogItems.hasNext());
	}

	/**
	 * Tests the retrieval of a milestone by its id.
	 */
	@Test
	public void testGetMilestoneById() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		wrapper.addSubMilestone("123");
		assertNull(wrapper.getSubMilestone("122"));
		SubMilestoneWrapper other = wrapper.getSubMilestone("123");
		assertNotNull(other);
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
		testDate = new Date();
	}

	/**
	 * Test the notification mechanism with one listener.
	 */
	@Test
	public void testNotificationsWithOneListener() {
		TestChangeListener listener = new TestChangeListener();
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		wrapper.addListener(listener);

		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone("200");
		subMilestone.setCapacity("12");

		String capacityKey = SubMilestoneWrapper.PREFIX_MILESTONE + "200-"
				+ SubMilestoneWrapper.SUFFIX_MILESTONE_CAPACITY;
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(capacityKey));
		subMilestone.setCapacity("12"); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(capacityKey));
		subMilestone.setCapacity("13"); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(capacityKey));

		subMilestone.setLabel("Label"); //$NON-NLS-1$
		String labelKey = SubMilestoneWrapper.PREFIX_MILESTONE + "200-"
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL;

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(labelKey));
		subMilestone.setLabel("Label"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(labelKey));
		subMilestone.setLabel("Other"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(labelKey));

		Date startDate = new Date();
		subMilestone.setStartDate(startDate);

		String start = SubMilestoneWrapper.PREFIX_MILESTONE + "200-" + SubMilestoneWrapper.SUFFIX_START_DATE;

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(start));
		subMilestone.setStartDate(startDate); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(start));
		subMilestone.setStartDate(new Date(startDate.getTime() + 1)); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(start));

		Date endDate = new Date();
		subMilestone.setEndDate(endDate);

		String end = SubMilestoneWrapper.PREFIX_MILESTONE + "200-" + SubMilestoneWrapper.SUFFIX_END_DATE;

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(end));
		subMilestone.setEndDate(endDate); // Should not notify
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(end));
		subMilestone.setEndDate(new Date(endDate.getTime() + 1)); // Should notify
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(end));

		// Backlog items
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("123");

		backlogItem.setInitialEffort("5");
		String pointsKey = BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "123-"
				+ BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS;

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(pointsKey));
		backlogItem.setInitialEffort("5");
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(pointsKey));
		backlogItem.setInitialEffort("6");
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(pointsKey));

		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		String biLabelKey = BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "123-"
				+ AbstractTaskAttributeWrapper.SUFFIX_LABEL;

		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(biLabelKey));
		backlogItem.setLabel("label of backlog item 300"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(1), listener.getInvocationsCount(biLabelKey));
		backlogItem.setLabel("Other"); //$NON-NLS-1$
		assertEquals(Integer.valueOf(2), listener.getInvocationsCount(biLabelKey));
	}

	/**
	 * Tests the behavior of get and setDisplayId on milestone wrapper classes.
	 */
	@Test
	public void testGetSetDisplayId() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		assertEquals("", wrapper.getDisplayId());
		wrapper.setDisplayId("300");
		assertEquals("300", wrapper.getDisplayId());
		String id = "3:809#123";
		String displayId = "123";
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem(id); // technical id
		assertEquals(id, backlogItem.getId());
		assertEquals(id, backlogItem.getDisplayId()); // display id = id if not set
		backlogItem.setDisplayId(displayId);
		assertEquals(displayId, backlogItem.getDisplayId());
		assertEquals(id, backlogItem.getId()); // id must not have changed
	}

	@Test
	public void testAddBacklogItem() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("987");
		backlogItem.setParent("parentId", "parentDisplayId");
		assertEquals("parentId", backlogItem.getParentId());
		assertEquals("parentDisplayId", backlogItem.getParentDisplayId());
		TaskAttribute att = taskData.getRoot().getAttribute("mta_backlog");
		assertNotNull(att);
		assertEquals("987", att.getValue());
		assertEquals(1, att.getValues().size());
		att = taskData.getRoot().getAttribute("mta_bi-987");
		assertNotNull(att);
	}

	@Test
	public void testSetBacklogItemParent() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("987");
		backlogItem.setParent("parentId", "parentDisplayId");
		assertEquals("parentId", backlogItem.getParentId());
		assertEquals("parentDisplayId", backlogItem.getParentDisplayId());
		TaskAttribute att = taskData.getRoot().getAttribute("mta_bi-987-parent_id");
		assertNotNull(att);
		assertEquals("parentId", att.getValue());
		assertEquals(1, att.getValues().size());
		att = taskData.getRoot().getAttribute("mta_bi-987-parent_display_id");
		assertNotNull(att);
		assertEquals("parentDisplayId", att.getValue());
		assertEquals(1, att.getValues().size());

		backlogItem.setParent(null, null); // Must not change the value
		assertEquals("parentId", backlogItem.getParentId());
		assertEquals("parentDisplayId", backlogItem.getParentDisplayId());
	}

	@Test
	public void testSetBacklogItemStatus() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("987");
		backlogItem.setStatus("Open");
		assertEquals("Open", backlogItem.getStatus());
		TaskAttribute att = taskData.getRoot().getAttribute("mta_bi-987-status");
		assertNotNull(att);
		assertEquals("Open", att.getValue());
		assertEquals(1, att.getValues().size());

		backlogItem.setStatus(null); // Must not change the value
		assertEquals("Open", backlogItem.getStatus());
	}

	@Test
	public void testSetBacklogItemType() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		BacklogItemWrapper backlogItem = wrapper.addBacklogItem("987");
		backlogItem.setType("User Story");
		assertEquals("User Story", backlogItem.getType());
		TaskAttribute att = taskData.getRoot().getAttribute("mta_bi-987-type");
		assertNotNull(att);
		assertEquals("User Story", att.getValue());
		assertEquals(1, att.getValues().size());

		backlogItem.setType(null); // Must not change the value
		assertEquals("User Story", backlogItem.getType());
	}

	@Test
	public void testSetHasCardwall() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		assertFalse(wrapper.getHasCardwall());
		wrapper.setHasCardwall(true);
		assertTrue(wrapper.getHasCardwall());
		TaskAttribute att = taskData.getRoot().getAttribute(MilestonePlanningWrapper.HAS_CARDWALL);
		assertNotNull(att);
		assertEquals("true", att.getValue());
	}

	@Test
	public void testHasBacklogChangedNotChangedUnmarked() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		assertFalse(wrapper.hasBacklogChanged());
	}

	@Test
	public void testHasBacklogChangedNotChangedMarked() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.markReference();
		assertFalse(wrapper.hasBacklogChanged());
	}

	@Test
	public void testHasBacklogChangedChangedByMove() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("bi0");
		wrapper.addBacklogItem("bi1");
		wrapper.addBacklogItem("bi2");
		wrapper.markReference();
		wrapper.moveItems(Arrays.asList(wrapper.wrapBacklogItem("bi0")), wrapper.wrapBacklogItem("bi2"),
				true, null);
		assertTrue(wrapper.hasBacklogChanged());
	}

	@Test
	public void testHasBacklogChangedChangedBySetReference() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("bi0");
		wrapper.addBacklogItem("bi1");
		wrapper.addBacklogItem("bi2");
		wrapper.setBacklogReference(Arrays.asList("bi1", "bi2", "bi0"));
		assertTrue(wrapper.hasBacklogChanged());
	}

	@Test
	public void testHasBacklogChangedNotChangedBySetReference() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("bi0");
		wrapper.addBacklogItem("bi1");
		wrapper.addBacklogItem("bi2");
		wrapper.setBacklogReference(Arrays.asList("bi0", "bi1", "bi2"));
		assertFalse(wrapper.hasBacklogChanged());
	}

	@Test
	public void testSetReference() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setBacklogReference(Arrays.asList("1", "2", "3"));
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		assertNotNull(att);
		assertEquals(Arrays.asList("1", "2", "3"), att.getValues());
	}

	@Test
	public void testSetReferenceTwice() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setBacklogReference(Arrays.asList("1", "2", "3"));
		wrapper.setBacklogReference(Arrays.asList("5", "6", "7"));
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		assertEquals(Arrays.asList("5", "6", "7"), att.getValues());
	}

	@Test
	public void testMarkReference() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("bi0");
		wrapper.addBacklogItem("bi1");
		wrapper.addBacklogItem("bi2");
		wrapper.markReference();
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		assertEquals(Arrays.asList("bi0", "bi1", "bi2"), att.getValues());
	}

	@Test
	public void testMarkReferenceTwice() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("bi0");
		wrapper.addBacklogItem("bi1");
		wrapper.addBacklogItem("bi2");
		wrapper.markReference();
		wrapper.addBacklogItem("bi3");
		wrapper.markReference();
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		// The reference must not change after 1st call
		assertEquals(Arrays.asList("bi0", "bi1", "bi2"), att.getValues());
	}

	@Test
	public void testMarkReferenceWithSubMilestones() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		wrapper.addBacklogItem("0");
		wrapper.addBacklogItem("1");
		wrapper.addBacklogItem("2");
		SubMilestoneWrapper mi1 = wrapper.addSubMilestone("mi1");
		mi1.addBacklogItem("3");
		mi1.addBacklogItem("4");
		SubMilestoneWrapper mi2 = wrapper.addSubMilestone("mi2");
		mi2.addBacklogItem("5");
		mi2.addBacklogItem("6");

		wrapper.markReference();
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		assertEquals(Arrays.asList("0", "1", "2"), att.getValues());
		TaskAttribute att1 = mi1.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("3", "4"), att1.getValues());
		TaskAttribute att2 = mi2.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("5", "6"), att2.getValues());
	}

	@Test
	public void testMarkReferenceTwiceWithSubMilestones() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		wrapper.addBacklogItem("0");
		wrapper.addBacklogItem("1");
		wrapper.addBacklogItem("2");
		SubMilestoneWrapper mi1 = wrapper.addSubMilestone("mi1");
		mi1.addBacklogItem("3");
		mi1.addBacklogItem("4");
		SubMilestoneWrapper mi2 = wrapper.addSubMilestone("mi2");
		mi2.addBacklogItem("5");
		mi2.addBacklogItem("6");

		wrapper.markReference();

		wrapper.addBacklogItem("10");
		mi1.addBacklogItem("11");
		mi2.addBacklogItem("12");

		wrapper.markReference();

		// References must not change
		TaskAttribute att = wrapper.getReferenceBacklogTaskAttribute();
		assertEquals(Arrays.asList("0", "1", "2"), att.getValues());
		TaskAttribute att1 = mi1.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("3", "4"), att1.getValues());
		TaskAttribute att2 = mi2.getReferenceWrappedAttribute();
		assertEquals(Arrays.asList("5", "6"), att2.getValues());
	}

	@Test
	public void testGetBacklogTaskAttribute() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.addBacklogItem("0");
		wrapper.addBacklogItem("1");
		wrapper.addBacklogItem("2");
		TaskAttribute att = wrapper.getBacklogTaskAttribute();
		assertEquals(Arrays.asList("0", "1", "2"), att.getValues());
		assertEquals("mta_backlog", att.getId());
	}

	@Test
	public void testGetSubMilestonesTaskAttribute() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		SubMilestoneWrapper mi1 = wrapper.addSubMilestone("mi1");
		mi1.addBacklogItem("3");
		mi1.addBacklogItem("4");
		SubMilestoneWrapper mi2 = wrapper.addSubMilestone("mi2");
		mi2.addBacklogItem("5");
		mi2.addBacklogItem("6");

		TaskAttribute att = wrapper.getSubMilestoneListTaskAttribute();
		assertEquals(Arrays.asList("mi1", "mi2"), att.getValues());
		assertEquals("mta_milestones", att.getId());
	}

	@Test
	public void testGetParentMilestone() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		BacklogItemWrapper bi1 = wrapper.addBacklogItem("1");
		SubMilestoneWrapper mi1 = wrapper.addSubMilestone("mi1");
		BacklogItemWrapper bi3 = mi1.addBacklogItem("3");
		assertEquals(wrapper, bi1.getParentMilestone());
		assertEquals(wrapper, bi3.getParentMilestone());
	}

	@Test
	public void testSetBacklogTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedBacklogItemTypes(Arrays.asList("123", "456"));

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.getAttribute(MilestonePlanningWrapper.BACKLOG_TRACKERS);
		assertNotNull(attribute);
		assertEquals(Arrays.asList("123", "456"), attribute.getValues());
	}

	@Test
	public void testSetContentTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedContentItemTypes(Arrays.asList("123", "456"));

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.getAttribute(MilestonePlanningWrapper.CONTENT_TRACKERS);
		assertNotNull(attribute);
		assertEquals(Arrays.asList("123", "456"), attribute.getValues());
	}

	@Test
	public void testSetSubMilestoneTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedSubmilestoneTypes(Arrays.asList("123", "456"));

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.getAttribute(MilestonePlanningWrapper.SUB_MILESTONE_TRACKERS);
		assertNotNull(attribute);
		assertEquals(Arrays.asList("123", "456"), attribute.getValues());
	}

	@Test
	public void testGetBacklogTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.createAttribute(MilestonePlanningWrapper.BACKLOG_TRACKERS);
		attribute.setValues(Arrays.asList("12", "34"));
		assertEquals(Arrays.asList("12", "34"), wrapper.getAllowedBacklogItemTypes());
	}

	@Test
	public void testGetContentTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.createAttribute(MilestonePlanningWrapper.CONTENT_TRACKERS);
		attribute.setValues(Arrays.asList("12", "34"));
		assertEquals(Arrays.asList("12", "34"), wrapper.getAllowedContentItemTypes());
	}

	@Test
	public void testGetSubMilestoneTypes() {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());

		TaskAttribute root = taskData.getRoot();
		TaskAttribute attribute = root.createAttribute(MilestonePlanningWrapper.SUB_MILESTONE_TRACKERS);
		attribute.setValues(Arrays.asList("12", "34"));
		assertEquals(Arrays.asList("12", "34"), wrapper.getAllowedSubmilestoneTypes());
	}
}
