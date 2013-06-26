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

// CHECKSTYLE:OFF

/**
 * The task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class MylynMockTaskDataHandler extends AbstractTaskDataHandler {

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
		summary.setValue("First Release");

		TaskAttribute kindAttribute = root.createAttribute(TaskAttribute.TASK_KIND);
		kindAttribute.setValue("Release");
		TaskAttribute urlAttribute = root.createAttribute(TaskAttribute.TASK_URL);
		urlAttribute.getMetaData().setType(TaskAttribute.TYPE_URL);
		urlAttribute.setValue("http://google.com");
		TaskAttribute descAttribute = root.createAttribute(TaskAttribute.DESCRIPTION);
		descAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		descAttribute.setValue("Release Planning Description");

		// The label of the types of the backlog
		TaskAttribute backlogTypeAtt = root.createAttribute(IMylynAgileCoreConstants.BACKLOG_TYPE_LABEL);
		backlogTypeAtt.setValue("Release Backlog");

		// The label of the types of elements contained in backlog and scopes
		TaskAttribute backlogItemTypeAtt = root
				.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_TYPE_LABEL);
		backlogItemTypeAtt.setValue("User Story");

		// The label of the types of elements contained in backlog and scopes
		TaskAttribute backlogItemPointsLabelAtt = root
				.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS_LABEL);
		backlogItemPointsLabelAtt.setValue("Story Points");

		// The attribute containing the list of scopes
		TaskAttribute scopeListAtt = root.createAttribute(IMylynAgileCoreConstants.SCOPE_LIST);

		int scopeIndex = 0;
		// First Sprint
		TaskAttribute scopeAtt = scopeListAtt.createAttribute(IMylynAgileCoreConstants.PREFIX_SCOPE
				+ scopeIndex++);
		scopeAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_SCOPE);
		scopeAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		TaskAttribute scopeNameAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_NAME);
		scopeNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		scopeNameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		scopeNameAtt.setValue("Sprint 1");

		TaskAttribute capacityAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_CAPACITY);
		capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		capacityAtt.setValue("20");

		TaskAttribute startDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.START_DATE);
		startDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		startDateAtt.getMetaData().setLabel("Creation Date");
		startDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		startDateAtt.setValue(String.valueOf(new Date().getTime()));

		TaskAttribute endDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.END_DATE);
		endDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		endDateAtt.getMetaData().setLabel("End Date");
		endDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATE);
		endDateAtt.setValue(String.valueOf(new Date().getTime()));

		// Items in the first sprint
		int backlogItemIndex = 0;
		TaskAttribute scopeItemAtt = scopeAtt.createAttribute(scopeAtt.getId() + "-" + backlogItemIndex++);
		scopeItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		scopeItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		TaskAttribute idAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		TaskAttribute nameAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex);
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		TaskAttribute pointsAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("4");
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		TaskAttribute summaryAtt = scopeItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary");
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		TaskAttribute parentAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent");
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 201");

		scopeItemAtt = scopeAtt.createAttribute(scopeAtt.getId() + "-" + backlogItemIndex++);
		scopeItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		scopeItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		idAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		nameAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex);
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		pointsAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("5");
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		summaryAtt = scopeItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary");
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		parentAtt = scopeItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent");
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 201");

		// Second Sprint
		scopeAtt = scopeListAtt.createAttribute(IMylynAgileCoreConstants.PREFIX_SCOPE + scopeIndex++);
		scopeAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_SCOPE);
		scopeAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		scopeNameAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_NAME);
		scopeNameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		scopeNameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		scopeNameAtt.setValue("Sprint 2");

		capacityAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.SCOPE_CAPACITY);
		capacityAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		capacityAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);
		capacityAtt.setValue("18");

		startDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.START_DATE);
		startDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		startDateAtt.getMetaData().setLabel("Creation Date");
		startDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATETIME);
		startDateAtt.setValue(String.valueOf(new Date().getTime()));

		endDateAtt = scopeAtt.createAttribute(IMylynAgileCoreConstants.END_DATE);
		endDateAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		endDateAtt.getMetaData().setLabel("End Date");
		endDateAtt.getMetaData().setType(TaskAttribute.TYPE_DATE);
		endDateAtt.setValue(String.valueOf(new Date().getTime()));

		// Backlog items (left)
		TaskAttribute backlogItemList = root.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_LIST);

		TaskAttribute backlogItemAtt = backlogItemList
				.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM + backlogItemIndex++);
		backlogItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		backlogItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		idAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		nameAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex);
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		pointsAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("4");
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		summaryAtt = backlogItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary");
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		parentAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent");
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 201");

		backlogItemAtt = backlogItemList.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM
				+ backlogItemIndex++);
		backlogItemAtt.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);
		backlogItemAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);

		idAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ID);
		idAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		idAtt.setValue(String.valueOf(100 + backlogItemIndex));
		idAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);

		nameAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_NAME);
		nameAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		nameAtt.setValue("User Story " + backlogItemIndex);
		nameAtt.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);

		pointsAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
		pointsAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		pointsAtt.setValue("5");
		pointsAtt.getMetaData().setType(TaskAttribute.TYPE_DOUBLE);

		summaryAtt = backlogItemAtt.createAttribute(TaskAttribute.SUMMARY);
		summaryAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		summaryAtt.getMetaData().setLabel("Summary");
		summaryAtt.getMetaData().setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		parentAtt = backlogItemAtt.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_PARENT);
		parentAtt.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
		parentAtt.getMetaData().setLabel("Parent");
		parentAtt.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		parentAtt.setValue("Epic 202");

		return true;
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
