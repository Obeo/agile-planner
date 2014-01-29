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
package org.tuleap.mylyn.task.internal.agile.mock.connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;

/**
 * The task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockTaskDataHandler extends AbstractTaskDataHandler {

	/**
	 * The number of milliseconds in a day.
	 */
	private static final long MILLISECOND_IN_DAY = 24L * 3600L * 1000L;

	/**
	 * The number of milliseconds in a week.
	 */
	private static final long MILLISECOND_IN_WEEK = 7L * MILLISECOND_IN_DAY;

	/**
	 * Capacity.
	 */
	private static final String CAPACITY = "20"; //$NON-NLS-1$

	/**
	 * Duration.
	 */
	private static final float DURATION = 11F;

	/**
	 * Id of field value.
	 */
	private static final String CARD_VALUE_FIELD_ID = "3000"; //$NON-NLS-1$

	/**
	 * id of field assigne-to.
	 */
	private static final String CARD_ASSIGNED_TO_FIELD_ID = "3001"; //$NON-NLS-1$

	/**
	 * id of field remaining effort.
	 */
	private static final String CARD_REMAINING_EFFORT_FIELD_ID = "3002"; //$NON-NLS-1$

	/**
	 * id of field date.
	 */
	private static final String CARD_DATE_FIELD_ID = "3003"; //$NON-NLS-1$

	/**
	 * id of Multi selection field.
	 */
	private static final String CARD_MULTI_SELECTION_FIELD_ID = "3004"; //$NON-NLS-1$

	/**
	 * The backlog item index.
	 */
	private int backlogItemIndex = 100;

	/**
	 * The backlog item index.
	 */
	private String milestoneId = "100"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#postTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, java.util.Set,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RepositoryResponse postTaskData(TaskRepository repository, TaskData taskData,
			Set<TaskAttribute> oldAttributes, IProgressMonitor monitor) throws CoreException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#initializeTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, org.eclipse.mylyn.tasks.core.ITaskMapping,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean initializeTaskData(TaskRepository repository, TaskData data,
			ITaskMapping initializationData, IProgressMonitor monitor) throws CoreException {
		TaskAttribute root = data.getRoot();

		TaskAttribute summary = root.createAttribute(TaskAttribute.SUMMARY);
		summary.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		summary.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summary.getMetaData().setLabel("GSome label"); //$NON-NLS-1$
		if (initializationData instanceof IMilestoneMapping) {
			summary.setValue("New milestone"); //$NON-NLS-1$
		} else {
			summary.setValue("Project Test"); //$NON-NLS-1$
		}

		TaskAttribute kindAttribute = root.createAttribute(TaskAttribute.TASK_KIND);
		kindAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		kindAttribute = root.createAttribute("mta_kind"); // TODO use constant
		kindAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		TaskAttribute urlAttribute = root.createAttribute(TaskAttribute.TASK_URL);
		urlAttribute.getMetaData().setType(TaskAttribute.TYPE_URL);
		urlAttribute.setValue("http://google.com"); //$NON-NLS-1$

		TaskAttribute descAttribute = root.createAttribute(TaskAttribute.DESCRIPTION);
		descAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		descAttribute.setValue("Release Planning Description"); //$NON-NLS-1$

		// TODO Fiw these tests when these labels are managed
		// The label of the types of the backlog
		// TaskAttribute backlogTypeAtt = root.createAttribute(IMylynAgileCoreConstants.BACKLOG_TYPE_LABEL);
		//		backlogTypeAtt.setValue("Release Backlog"); //$NON-NLS-1$

		// The label of the types of elements contained in backlog and milestones
		// TaskAttribute backlogItemTypeAtt = root
		// .createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_TYPE_LABEL);
		//		backlogItemTypeAtt.setValue("User Story"); //$NON-NLS-1$

		// The label of the types of elements contained in backlog and milestones
		// TaskAttribute backlogItemPointsLabelAtt = root
		// .createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS_LABEL);
		//		backlogItemPointsLabelAtt.setValue("Story Points"); //$NON-NLS-1$

		// First Sprint
		Date startDate = new Date(System.currentTimeMillis() - 4 * MILLISECOND_IN_WEEK);
		Date endDate = new Date(startDate.getTime() + (long)(DURATION * MILLISECOND_IN_DAY));

		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(root);
		this.createSprint(wrapper, milestoneId, "Sprint 1", CAPACITY, startDate, endDate); //$NON-NLS-1$ 
		addNewBacklogItem(wrapper, milestoneId);
		addNewBacklogItem(wrapper, milestoneId);
		addNewBacklogItem(wrapper, milestoneId);
		addNewBacklogItem(wrapper, milestoneId);
		milestoneId = Integer.toString(Integer.parseInt(milestoneId) + 1);

		// Second Sprint
		startDate = new Date(startDate.getTime() - 2 * MILLISECOND_IN_WEEK);
		endDate = new Date(startDate.getTime() + (long)(DURATION * MILLISECOND_IN_DAY));
		this.createSprint(wrapper, milestoneId, "Sprint 2", "18", startDate, endDate); //$NON-NLS-1$ 

		addNewBacklogItem(wrapper, milestoneId);
		addNewBacklogItem(wrapper, milestoneId);
		addNewBacklogItem(wrapper, milestoneId);
		milestoneId = Integer.toString(Integer.parseInt(milestoneId) + 1);

		addNewBacklogItem(wrapper);
		addNewBacklogItem(wrapper);
		addNewBacklogItem(wrapper);
		addNewBacklogItem(wrapper);
		addNewBacklogItem(wrapper);

		addNewCardwall(root);

		return true;
	}

	/**
	 * Adds a new cardwall to the given task attribute.
	 * 
	 * @param root
	 *            The task attribute
	 */
	private void addNewCardwall(TaskAttribute root) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(root);
		wrapper.setHasCardwall(true);

		cardwall3(root);
	}

	/**
	 * Data set 1 for an example of cardwall.
	 * 
	 * @param root
	 *            The task attribute
	 */
	private void cardwall1(TaskAttribute root) {
		// CHECKSTYLE:OFF
		CardwallWrapper cardwallWrapper = new CardwallWrapper(root);
		for (int i = 0; i < 4; i++) {
			cardwallWrapper.addColumn(Integer.toString(10 + i), "Column" + i); //$NON-NLS-1$
		}
		SwimlaneWrapper swimlane = cardwallWrapper.addSwimlane("123"); //$NON-NLS-1$

		for (int i = 0; i < 4; i++) {
			CardWrapper card = swimlane.addCard(Integer.toString(200 + i));
			card.setLabel("Label " + (200 + i)); //$NON-NLS-1$
			card.setColumnId(Integer.toString(10 + i));
			card.setFieldLabel(CARD_VALUE_FIELD_ID, "Value for test");
			card.addFieldValue(CARD_VALUE_FIELD_ID, "Value 100" + i); //$NON-NLS-1$
		}
		// CHECKSTYLE:ON
	}

	/**
	 * Data set 2 for an example of cardwall.
	 * 
	 * @param root
	 *            The task attribute
	 */
	private void cardwall2(TaskAttribute root) {
		// CHECKSTYLE:OFF
		CardwallWrapper cardwallWrapper = new CardwallWrapper(root);
		for (int i = 0; i < 3; i++) {
			cardwallWrapper.addColumn(Integer.toString(10 + i), "Column " + i); //$NON-NLS-1$
		}
		for (int i = 0; i < 3; i++) {
			SwimlaneWrapper swimlane = cardwallWrapper.addSwimlane(Integer.toString(20 + i));

			switch (i) {
				case 0:
					CardWrapper card00 = swimlane.addCard(Integer.toString(300) + i);
					card00.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card00.setColumnId(Integer.toString(10 + i));
					card00.setFieldLabel(CARD_VALUE_FIELD_ID, "Value for test");
					card00.addFieldValue(CARD_VALUE_FIELD_ID,
							"Blablafdfdjjdkeds  deffsfsrfersfrqsefhqz euofhzquofgeo zfuiegpgzfe fgpzeufg"); //$NON-NLS-1$ 

					CardWrapper card10 = swimlane.addCard(Integer.toString(301) + i);
					card10.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					card10.setColumnId(Integer.toString(10 + i));
					card10.setFieldLabel(CARD_VALUE_FIELD_ID, "Value for test");
					card10.addFieldValue(
							CARD_VALUE_FIELD_ID,
							"Blablafdfdjjdkedsdeffsfrezgtzehrtehrthertherjhretyherthrt\ndezgfdiezlruerf\nzedfezaferfgrzegtr\ncezfezafazerfze\nferfrezfgr\ngusmzad\nkdzedezeyf\ndezdeazd\ntfz\nsdzgdzeyi\ndezqdgeui"); //$NON-NLS-1$ 
					break;
				case 1:
					CardWrapper card01 = swimlane.addCard(Integer.toString(300) + i);
					card01.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card01.setColumnId(Integer.toString(10 + i));
					card01.setFieldLabel(CARD_VALUE_FIELD_ID, "Value for test");
					card01.addFieldValue(CARD_VALUE_FIELD_ID, "Blablafdfdjjdkedsdeffsdegzufigeuofzgyoz"); //$NON-NLS-1$ 
					break;
				case 2:
					// CardWrapper card02 = swimlane.addCard(Integer.toString(300 + i));
					// CardWrapper card12 = swimlane.addCard(Integer.toString(301 + i));
					// CardWrapper card22 = swimlane.addCard(Integer.toString(302 + i));
					break;
				default:
					break;
			}

		}
		// CHECKSTYLE:ON
	}

	// CHECKSTYLE:OFF
	/**
	 * Data set 3 for an example of cardwall.
	 * 
	 * @param root
	 *            The task attribute
	 */
	private void cardwall3(TaskAttribute root) {

		CardwallWrapper cardwallWrapper = new CardwallWrapper(root);
		for (int i = 0; i < 3; i++) {
			ColumnWrapper column = cardwallWrapper.addColumn(Integer.toString(10 + i), "Column " + i); //$NON-NLS-1$
			if (i != 0) {
				column.setColor("#ff" + 49 * i + "00");
			}
		}
		for (int i = 0; i < 3; i++) {
			SwimlaneWrapper swimlane = cardwallWrapper.addSwimlane(Integer.toString(20 + i));
			switch (i) {
				case 0:
					CardWrapper card00 = swimlane.addCard(Integer.toString(300) + i);
					card00.setLabel("This is very very long title to test line wrapping, Title of " + (300 + i)); //$NON-NLS-1$
					// card00.setColumnId(Integer.toString(10 + i));
					card00.addField(CARD_REMAINING_EFFORT_FIELD_ID,
							"Remaining Effort", TaskAttribute.TYPE_DOUBLE); //$NON-NLS-1$
					card00.setFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "2.5"); //$NON-NLS-1$
					TaskAttribute assignedToAtt = card00.addField(CARD_ASSIGNED_TO_FIELD_ID,
							"Assigned to", TaskAttribute.TYPE_SINGLE_SELECT); //$NON-NLS-1$
					assignedToAtt.putOption("101", "cnotot");
					assignedToAtt.putOption("102", "ldelaigue");
					assignedToAtt.putOption("103", "fbacha");
					assignedToAtt.putOption("104", "sbegaudeau");
					// We set an invalid value here on purpose
					// To test Request #5955
					card00.setFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "100"); //$NON-NLS-1$ 
					card00.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card00.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card00.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATE);

					TaskAttribute multiSelectionAttOO = card00.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAttOO.putOption("101", "cnotot");
					multiSelectionAttOO.putOption("102", "ldelaigue");
					multiSelectionAttOO.putOption("103", "fbacha");
					multiSelectionAttOO.putOption("104", "sbegaudeau");
					card00.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card10 = swimlane.addCard(Integer.toString(301) + i);
					card10.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					// card10.setColumnId(Integer.toString(10 + i));
					card10.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card10.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "5"); //$NON-NLS-1$
					card10.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card10.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "cnotot"); //$NON-NLS-1$ 
					card10.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card10.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card10.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATE);

					TaskAttribute multiSelectionAtt10 = card10.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt10.putOption("101", "cnotot");
					multiSelectionAtt10.putOption("102", "ldelaigue");
					multiSelectionAtt10.putOption("103", "fbacha");
					multiSelectionAtt10.putOption("104", "sbegaudeau");
					List<String> values = new ArrayList<String>();
					values.add("102");
					values.add("103");
					card10.setFieldValues(CARD_MULTI_SELECTION_FIELD_ID, values);

					CardWrapper card20 = swimlane.addCard(Integer.toString(321) + i);
					card20.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					card20.setColumnId(Integer.toString(10 + i));

					card20.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card20.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "5"); //$NON-NLS-1$
					card20.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card20.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 

					card20.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card20.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card20.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					card20.setAccentColor("#6699aa");

					card20.addAllowedColumn("11");
					card20.addAllowedColumn("10");

					TaskAttribute multiSelectionAtt20 = card20.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt20.putOption("101", "cnotot");
					multiSelectionAtt20.putOption("102", "ldelaigue");
					multiSelectionAtt20.putOption("103", "fbacha");
					multiSelectionAtt20.putOption("104", "sbegaudeau");
					multiSelectionAtt20.putOption("105",
							"This is very very very very long multi-selection to test line wrapping");
					values.clear();
					// values.add("101");
					// values.add("103");
					// values.add("104");
					// values.add("102");
					values.add("105");
					card20.setFieldValues(CARD_MULTI_SELECTION_FIELD_ID, values);

					break;
				case 1:

					CardWrapper card30 = swimlane.addCard(Integer.toString(300) + i);
					card30.setLabel("This is very very long title to test line wrapping, Title of " + (300 + i)); //$NON-NLS-1$

					card30.addField(CARD_REMAINING_EFFORT_FIELD_ID,
							"Remaining Effort", TaskAttribute.TYPE_DOUBLE); //$NON-NLS-1$
					card30.setFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "2.5"); //$NON-NLS-1$
					TaskAttribute assignedToAttribute = card30.addField(CARD_ASSIGNED_TO_FIELD_ID,
							"Assigned to", TaskAttribute.TYPE_SINGLE_SELECT); //$NON-NLS-1$
					assignedToAttribute.putOption("101", "cnotot");
					assignedToAttribute.putOption("102", "ldelaigue");
					assignedToAttribute.putOption("103", "fbacha");
					assignedToAttribute.putOption("104", "sbegaudeau");
					card30.setFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "101"); //$NON-NLS-1$ 
					card30.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card30.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card30.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt30 = card30.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt30.putOption("101", "cnotot");
					multiSelectionAtt30.putOption("102", "ldelaigue");
					multiSelectionAtt30.putOption("103", "fbacha");
					multiSelectionAtt30.putOption("104", "sbegaudeau");
					values = new ArrayList<String>();
					values.add("102");
					values.add("104");
					card30.setFieldValues(CARD_MULTI_SELECTION_FIELD_ID, values);

					CardWrapper card01 = swimlane.addCard(Integer.toString(301) + i);
					card01.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card01.setColumnId(Integer.toString(10 + i));
					card01.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card01.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card01.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card01.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
					card01.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card01.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card01.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt01 = card01.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt01.putOption("101", "cnotot");
					multiSelectionAtt01.putOption("102", "ldelaigue");
					multiSelectionAtt01.putOption("103", "fbacha");
					multiSelectionAtt01.putOption("104", "sbegaudeau");
					card01.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card31 = swimlane.addCard(Integer.toString(302) + i);
					card31.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card31.setColumnId(Integer.toString(10 + i));
					card31.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card31.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card31.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card31.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card31.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card31.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card31.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt31 = card31.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt31.putOption("101", "cnotot");
					multiSelectionAtt31.putOption("102", "ldelaigue");
					multiSelectionAtt31.putOption("103", "fbacha");
					multiSelectionAtt31.putOption("104", "sbegaudeau");
					values.clear();
					values.add("101");
					values.add("102");
					values.add("103");
					card31.setFieldValues(CARD_MULTI_SELECTION_FIELD_ID, values);

					CardWrapper card32 = swimlane.addCard(Integer.toString(303) + i);
					card32.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card32.setColumnId(Integer.toString(10));
					card32.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card32.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card32.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card32.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card32.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card32.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card32.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt32 = card32.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt32.putOption("101", "cnotot");
					multiSelectionAtt32.putOption("102", "ldelaigue");
					multiSelectionAtt32.putOption("103", "fbacha");
					multiSelectionAtt32.putOption("104", "sbegaudeau");
					card32.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card33 = swimlane.addCard(Integer.toString(304) + i);
					card33.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card33.setColumnId(Integer.toString(11 + i));
					card33.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card33.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card33.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card33.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card33.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card33.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card33.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt33 = card33.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt33.putOption("101", "cnotot");
					multiSelectionAtt33.putOption("102", "ldelaigue");
					multiSelectionAtt33.putOption("103", "fbacha");
					multiSelectionAtt33.putOption("104", "sbegaudeau");
					card33.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card34 = swimlane.addCard(Integer.toString(305) + i);
					card34.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card34.setColumnId(Integer.toString(11 + i));
					card34.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card34.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card34.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card34.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "sbegaudeau"); //$NON-NLS-1$ 
					card34.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card34.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card34.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt34 = card34.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt34.putOption("101", "cnotot");
					multiSelectionAtt34.putOption("102", "ldelaigue");
					multiSelectionAtt34.putOption("103", "fbacha");
					multiSelectionAtt34.putOption("104", "sbegaudeau");
					card34.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card35 = swimlane.addCard(Integer.toString(306) + i);
					card35.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card35.setColumnId(Integer.toString(11 + i));
					card35.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card35.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card35.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card35.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
					card35.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card35.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card35.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt35 = card35.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt35.putOption("101", "cnotot");
					multiSelectionAtt35.putOption("102", "ldelaigue");
					multiSelectionAtt35.putOption("103", "fbacha");
					multiSelectionAtt35.putOption("104", "sbegaudeau");
					card35.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 
					break;
				case 2:
					CardWrapper card36 = swimlane.addCard(Integer.toString(300) + i);
					card36.setLabel("This is very very long title to test line wrapping, Title of " + (300 + i)); //$NON-NLS-1$

					card36.addField(CARD_REMAINING_EFFORT_FIELD_ID,
							"Remaining Effort", TaskAttribute.TYPE_DOUBLE); //$NON-NLS-1$
					card36.setFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "2.5"); //$NON-NLS-1$
					TaskAttribute assignedTo = card36.addField(CARD_ASSIGNED_TO_FIELD_ID,
							"Assigned to", TaskAttribute.TYPE_SINGLE_SELECT); //$NON-NLS-1$
					assignedTo.putOption("101", "cnotot");
					assignedTo.putOption("102", "ldelaigue");
					assignedTo.putOption("103", "fbacha");
					assignedTo.putOption("104", "sbegaudeau");
					card36.setFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "101"); //$NON-NLS-1$ 
					card36.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card36.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card36.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt36 = card36.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt36.putOption("101", "cnotot");
					multiSelectionAtt36.putOption("102", "ldelaigue");
					multiSelectionAtt36.putOption("103", "fbacha");
					multiSelectionAtt36.putOption("104", "sbegaudeau");
					card36.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card37 = swimlane.addCard(Integer.toString(301) + i);
					card37.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card37.setColumnId(Integer.toString(10 + i));
					card37.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card37.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card37.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card37.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
					card37.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card37.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card37.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt37 = card37.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt37.putOption("101", "cnotot");
					multiSelectionAtt37.putOption("102", "ldelaigue");
					multiSelectionAtt37.putOption("103", "fbacha");
					multiSelectionAtt37.putOption("104", "sbegaudeau");
					card37.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card38 = swimlane.addCard(Integer.toString(302) + i);
					card38.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card38.setColumnId(Integer.toString(10 + i));
					card38.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card38.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card38.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card38.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card38.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card38.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card38.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt38 = card38.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt38.putOption("101", "cnotot");
					multiSelectionAtt38.putOption("102", "ldelaigue");
					multiSelectionAtt38.putOption("103", "fbacha");
					multiSelectionAtt38.putOption("104", "sbegaudeau");
					card38.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card39 = swimlane.addCard(Integer.toString(303) + i);
					card39.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card39.setColumnId(Integer.toString(11));
					card39.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card39.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card39.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card39.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card39.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card39.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card39.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt39 = card39.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt39.putOption("101", "cnotot");
					multiSelectionAtt39.putOption("102", "ldelaigue");
					multiSelectionAtt39.putOption("103", "fbacha");
					multiSelectionAtt39.putOption("104", "sbegaudeau");
					card39.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card40 = swimlane.addCard(Integer.toString(304) + i);
					card40.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card40.setColumnId(Integer.toString(10));
					card40.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card40.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card40.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card40.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 
					card40.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card40.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card40.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt40 = card40.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt40.putOption("101", "cnotot");
					multiSelectionAtt40.putOption("102", "ldelaigue");
					multiSelectionAtt40.putOption("103", "fbacha");
					multiSelectionAtt40.putOption("104", "sbegaudeau");
					card40.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card41 = swimlane.addCard(Integer.toString(305) + i);
					card41.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card41.setColumnId(Integer.toString(10 + i));
					card41.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card41.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card41.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card41.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "sbegaudeau"); //$NON-NLS-1$ 
					card41.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card41.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card41.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt41 = card41.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt41.putOption("101", "cnotot");
					multiSelectionAtt41.putOption("102", "ldelaigue");
					multiSelectionAtt41.putOption("103", "fbacha");
					multiSelectionAtt41.putOption("104", "sbegaudeau");
					card41.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 

					CardWrapper card42 = swimlane.addCard(Integer.toString(306) + i);
					card42.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card42.setColumnId(Integer.toString(10 + i));
					card42.setFieldLabel(CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort"); //$NON-NLS-1$
					card42.addFieldValue(CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card42.setFieldLabel(CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card42.addFieldValue(CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
					card42.setFieldLabel(CARD_DATE_FIELD_ID, "Date"); //$NON-NLS-1$
					card42.setFieldValue(CARD_DATE_FIELD_ID, "1220227200"); //$NON-NLS-1$
					card42.getFieldAttribute(CARD_DATE_FIELD_ID).getMetaData().setType(
							TaskAttribute.TYPE_DATETIME);

					TaskAttribute multiSelectionAtt42 = card42.addField(CARD_MULTI_SELECTION_FIELD_ID,
							"Multi-selection", TaskAttribute.TYPE_MULTI_SELECT); //$NON-NLS-1$
					multiSelectionAtt42.putOption("101", "cnotot");
					multiSelectionAtt42.putOption("102", "ldelaigue");
					multiSelectionAtt42.putOption("103", "fbacha");
					multiSelectionAtt42.putOption("104", "sbegaudeau");
					card42.setFieldValue(CARD_MULTI_SELECTION_FIELD_ID, "102"); //$NON-NLS-1$ 
					break;
				default:
					break;
			}

		}
		// CHECKSTYLE:ON
	}

	/**
	 * Creates a task attributes for the sprint under the root.
	 * 
	 * @param wrapper
	 *            The milestone planning wrapper
	 * @param id
	 *            The id of the sprint
	 * @param name
	 *            The name
	 * @param capacity
	 *            The sprint capacity
	 * @param startDate
	 *            The start date of the sprint
	 * @param endDate
	 *            The sprint's end date
	 */
	private void createSprint(MilestonePlanningWrapper wrapper, String id, String name, String capacity,
			Date startDate, Date endDate) {
		SubMilestoneWrapper subMilestone = wrapper.addSubMilestone(id);
		subMilestone.setLabel(name);
		subMilestone.setCapacity(capacity);
		subMilestone.setStartDate(startDate);
		subMilestone.setEndDate(endDate);
	}

	/**
	 * Adds a new backlog item to a milestone planning.
	 * 
	 * @param wrapper
	 *            The milestone planning wrapper
	 * @return A new wrapper of backlog item
	 */
	private BacklogItemWrapper addNewBacklogItem(MilestonePlanningWrapper wrapper) {
		BacklogItemWrapper bi = wrapper.addBacklogItem(Integer.toString(backlogItemIndex));
		bi.setInitialEffort("4"); //$NON-NLS-1$
		bi.setLabel("User Story " + backlogItemIndex); //$NON-NLS-1$
		bi.setParent("3:809#" + (backlogItemIndex + 1), Integer.toString(backlogItemIndex + 1)); //$NON-NLS-1$
		bi.setType("User stories"); //$NON-NLS-1$
		bi.setStatus("Closed"); //$NON-NLS-1$
		backlogItemIndex++;
		return bi;
	}

	/**
	 * Adds a new backlog item to a milestone planning.
	 * 
	 * @param wrapper
	 *            The milestone planning wrapper
	 * @param id
	 *            the milestone id to assign the backlog item to
	 * @return A new wrapper assigned to the given milestone id
	 */
	private BacklogItemWrapper addNewBacklogItem(MilestonePlanningWrapper wrapper, String id) {
		BacklogItemWrapper bi = addNewBacklogItem(wrapper);
		bi.setAssignedMilestoneId(id);
		return bi;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#getAttributeMapper(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public TaskAttributeMapper getAttributeMapper(TaskRepository repository) {
		return new TaskAttributeMapper(repository);
	}

}
