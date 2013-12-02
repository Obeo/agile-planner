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

import java.util.Date;
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
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
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
	private static final float CAPACITY = 20F;

	/**
	 * Duration.
	 */
	private static final float DURATION = 11F;

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
		kindAttribute.setValue(AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);

		kindAttribute = root.createAttribute("mta_kind"); // TODO use constant
		kindAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		kindAttribute.setValue(AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);

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
		this.createSprint(wrapper, milestoneId, "Sprint 2", CAPACITY - 2, startDate, endDate); //$NON-NLS-1$ 

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
			card.setFieldLabel(CardWrapper.CARD_VALUE_FIELD_ID, "Value for test");
			card.addFieldValue(CardWrapper.CARD_VALUE_FIELD_ID, "Value 100" + i); //$NON-NLS-1$
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
					card00.setFieldLabel(CardWrapper.CARD_VALUE_FIELD_ID, "Value for test");
					card00.addFieldValue(CardWrapper.CARD_VALUE_FIELD_ID,
							"Blablafdfdjjdkeds  deffsfsrfersfrqsefhqz euofhzquofgeo zfuiegpgzfe fgpzeufg"); //$NON-NLS-1$ 

					CardWrapper card10 = swimlane.addCard(Integer.toString(301) + i);
					card10.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					card10.setColumnId(Integer.toString(10 + i));
					card10.setFieldLabel(CardWrapper.CARD_VALUE_FIELD_ID, "Value for test");
					card10.addFieldValue(
							CardWrapper.CARD_VALUE_FIELD_ID,
							"Blablafdfdjjdkedsdeffsfrezgtzehrtehrthertherjhretyherthrt\ndezgfdiezlruerf\nzedfezaferfgrzegtr\ncezfezafazerfze\nferfrezfgr\ngusmzad\nkdzedezeyf\ndezdeazd\ntfz\nsdzgdzeyi\ndezqdgeui"); //$NON-NLS-1$ 
					break;
				case 1:
					CardWrapper card01 = swimlane.addCard(Integer.toString(300) + i);
					card01.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card01.setColumnId(Integer.toString(10 + i));
					card01.setFieldLabel(CardWrapper.CARD_VALUE_FIELD_ID, "Value for test");
					card01.addFieldValue(CardWrapper.CARD_VALUE_FIELD_ID,
							"Blablafdfdjjdkedsdeffsdegzufigeuofzgyoz"); //$NON-NLS-1$ 
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

	/**
	 * Data set 3 for an example of cardwall.
	 * 
	 * @param root
	 *            The task attribute
	 */
	private void cardwall3(TaskAttribute root) {
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
					card00.setLabel("This is very very long title to test line wrapping, Title of " + (300 + i)); //$NON-NLS-1$
					// card00.setColumnId(Integer.toString(10 + i));
					card00.addField(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID,
							"Remaining Effort", TaskAttribute.TYPE_DOUBLE); //$NON-NLS-1$
					card00.setFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "2.5"); //$NON-NLS-1$
					TaskAttribute assignedToAtt = card00.addField(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID,
							"Assigned to", TaskAttribute.TYPE_SINGLE_SELECT); //$NON-NLS-1$
					assignedToAtt.putOption("101", "cnotot");
					assignedToAtt.putOption("102", "ldelaigue");
					assignedToAtt.putOption("103", "fbacha");
					assignedToAtt.putOption("104", "sbegaudeau");
					card00.setFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "101"); //$NON-NLS-1$ 

					CardWrapper card10 = swimlane.addCard(Integer.toString(301) + i);
					card10.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					// card10.setColumnId(Integer.toString(10 + i));
					card10.setFieldLabel(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card10.addFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "5"); //$NON-NLS-1$
					card10.setFieldLabel(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card10.addFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "cnotot"); //$NON-NLS-1$ 

					CardWrapper card20 = swimlane.addCard(Integer.toString(321) + i);
					card20.setLabel("Title of " + (301 + i)); //$NON-NLS-1$
					card20.setColumnId(Integer.toString(10 + i));
					card20.setFieldLabel(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card20.addFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "5"); //$NON-NLS-1$
					card20.setFieldLabel(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card20.addFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "fbacha"); //$NON-NLS-1$ 

					break;
				case 1:
					CardWrapper card01 = swimlane.addCard(Integer.toString(300) + i);
					card01.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card01.setColumnId(Integer.toString(10 + i));
					card01.setFieldLabel(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card01.addFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card01.setFieldLabel(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card01.addFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
					break;
				case 2:
					CardWrapper card02 = swimlane.addCard(Integer.toString(300) + i);
					card02.setLabel("Title of " + (300 + i)); //$NON-NLS-1$
					card02.setColumnId(Integer.toString(10 + i));
					card02.setFieldLabel(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "Remaining Effort");
					card02.addFieldValue(CardWrapper.CARD_REMAINING_EFFORT_FIELD_ID, "3"); //$NON-NLS-1$ 
					card02.setFieldLabel(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "Assigned to");
					card02.addFieldValue(CardWrapper.CARD_ASSIGNED_TO_FIELD_ID, "ldelaigue"); //$NON-NLS-1$ 
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
	private void createSprint(MilestonePlanningWrapper wrapper, String id, String name, float capacity,
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
		bi.setInitialEffort(4F);
		bi.setLabel("User Story " + backlogItemIndex); //$NON-NLS-1$
		bi.setParent("3:809#" + (backlogItemIndex + 1), Integer.toString(backlogItemIndex + 1)); //$NON-NLS-1$
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
