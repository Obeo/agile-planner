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
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

/**
 * The task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockTaskDataHandler extends AbstractTaskDataHandler {

	/**
	 * The number of milliseconds in a week.
	 */
	private static final long MILLISECOND_IN_WEEK = 7L * 24L * 3600L * 1000L;

	/**
	 * The backlog item index.
	 */
	private int backlogItemIndex;

	/**
	 * The rank.
	 */
	private int rank;

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
		summary.setValue("First Release"); //$NON-NLS-1$

		TaskAttribute kindAttribute = root.createAttribute(TaskAttribute.TASK_KIND);
		kindAttribute.setValue("Release"); //$NON-NLS-1$
		TaskAttribute urlAttribute = root.createAttribute(TaskAttribute.TASK_URL);
		urlAttribute.getMetaData().setType(TaskAttribute.TYPE_URL);
		urlAttribute.setValue("http://google.com"); //$NON-NLS-1$
		TaskAttribute descAttribute = root.createAttribute(TaskAttribute.DESCRIPTION);
		descAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		descAttribute.setValue("Release Planning Description"); //$NON-NLS-1$

		// The label of the types of the backlog
		TaskAttribute backlogTypeAtt = root.createAttribute(IMylynAgileCoreConstants.BACKLOG_TYPE_LABEL);
		backlogTypeAtt.setValue("Release Backlog"); //$NON-NLS-1$

		// The label of the types of elements contained in backlog and scopes
		TaskAttribute backlogItemTypeAtt = root
				.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_TYPE_LABEL);
		backlogItemTypeAtt.setValue("User Story"); //$NON-NLS-1$

		// The label of the types of elements contained in backlog and scopes
		TaskAttribute backlogItemPointsLabelAtt = root
				.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS_LABEL);
		backlogItemPointsLabelAtt.setValue("Story Points"); //$NON-NLS-1$

		int scopeIndex = 0;
		// First Sprint
		String startDate = String.valueOf(new Date().getTime() - 4 * MILLISECOND_IN_WEEK);
		String endDate = String.valueOf(new Date().getTime() - 2 * MILLISECOND_IN_WEEK);

		TaskAttribute scopeAtt = this.createSprint(root, scopeIndex++, "Sprint 1", "20", startDate, endDate); //$NON-NLS-1$ //$NON-NLS-2$
		addNewScopeItem(scopeAtt);
		addNewScopeItem(scopeAtt);
		addNewScopeItem(scopeAtt);
		addNewScopeItem(scopeAtt);

		// Second Sprint
		startDate = String.valueOf(new Date().getTime() - 2 * MILLISECOND_IN_WEEK);
		endDate = String.valueOf(new Date().getTime());
		scopeAtt = this.createSprint(root, scopeIndex++, "Sprint 2", "18", startDate, endDate); //$NON-NLS-1$ //$NON-NLS-2$

		// Re-initialization of the rank, to start from 0 in each list.
		rank = 0;
		addNewScopeItem(scopeAtt);
		addNewScopeItem(scopeAtt);
		addNewScopeItem(scopeAtt);

		// Backlog items (left)
		TaskAttribute backlogItemList = root.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_LIST);

		// Re-initialization of the rank, to start from 0 in each list.
		rank = 0;
		addNewBacklogItem(backlogItemList);
		addNewBacklogItem(backlogItemList);
		addNewBacklogItem(backlogItemList);
		addNewBacklogItem(backlogItemList);
		addNewBacklogItem(backlogItemList);

		return true;
	}

	/**
	 * Creates a task attributes for the sprint under the root.
	 * 
	 * @param root
	 *            The root of the task data
	 * @param scopeIndex
	 *            The index of the sprint
	 * @param name
	 *            The name
	 * @param capacity
	 *            The capacity
	 * @param startDate
	 *            The start date of the sprint
	 * @param endDate
	 *            The end date of the sprint
	 * @return The task attribute representing the sprint under the task data's root
	 */
	private TaskAttribute createSprint(TaskAttribute root, int scopeIndex, String name, String capacity,
			String startDate, String endDate) {
		TaskAttribute scopeAtt = root.createAttribute(IMylynAgileCoreConstants.PREFIX_SCOPE + scopeIndex);
		scopeAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_SCOPE);
		scopeAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		TaskAttribute scopeNameAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_NAME);
		scopeNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		scopeNameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		scopeNameAtt.setValue(name);

		TaskAttribute capacityAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_CAPACITY);
		capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		capacityAtt.setValue(capacity);

		TaskAttribute startDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.START_DATE);
		startDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		startDateAtt.getMetaData().setLabel("Creation Date"); //$NON-NLS-1$
		startDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		startDateAtt.setValue(startDate);

		TaskAttribute endDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.END_DATE);
		endDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		endDateAtt.getMetaData().setLabel("End Date"); //$NON-NLS-1$
		endDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATE);
		endDateAtt.setValue(endDate);

		return scopeAtt;
	}

	/**
	 * Adds a new backlog item to the list of backlog item.
	 * 
	 * @param backlogItemList
	 *            The backlog item list
	 */
	private void addNewBacklogItem(TaskAttribute backlogItemList) {
		TaskAttribute idAtt;
		TaskAttribute nameAtt;
		TaskAttribute pointsAtt;
		TaskAttribute summaryAtt;
		TaskAttribute parentAtt;
		TaskAttribute backlogItemAtt = backlogItemList
				.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM + backlogItemIndex++);
		backlogItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		backlogItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		backlogItemAtt.setValue(String.valueOf(rank++));

		idAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		nameAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex); //$NON-NLS-1$
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		pointsAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("4"); //$NON-NLS-1$
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		summaryAtt = backlogItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary"); //$NON-NLS-1$
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		parentAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent"); //$NON-NLS-1$
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 201"); //$NON-NLS-1$
	}

	/**
	 * Adds a new scope attribute.
	 * 
	 * @param scopeAtt
	 *            The scope attribute
	 */
	private void addNewScopeItem(TaskAttribute scopeAtt) {
		TaskAttribute scopeItemAtt = scopeAtt.createAttribute(scopeAtt.getId() + "-" + backlogItemIndex++); //$NON-NLS-1$
		scopeItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		scopeItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		scopeItemAtt.setValue(String.valueOf(rank++));

		TaskAttribute idAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		TaskAttribute nameAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex); //$NON-NLS-1$
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		TaskAttribute pointsAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("4"); //$NON-NLS-1$
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		TaskAttribute summaryAtt = scopeItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary"); //$NON-NLS-1$
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		TaskAttribute parentAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent"); //$NON-NLS-1$
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 201"); //$NON-NLS-1$
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
