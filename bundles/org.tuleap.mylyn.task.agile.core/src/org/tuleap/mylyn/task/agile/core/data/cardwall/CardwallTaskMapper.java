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
package org.tuleap.mylyn.task.agile.core.data.cardwall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

/**
 * This task attribute mapper will be used in order to manipulate a task data objects as a cardwall at a
 * higher level of abstraction. With the use of this mapper, clients should not have to manipulate most of the
 * task attributes of the task data to create, modify or query the task data object on its cardwall
 * information.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class CardwallTaskMapper extends AbstractTaskMapper {

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param createNonExistingAttributes
	 *            Indicates if we should create new task data attributes if they do not exist.
	 */
	public CardwallTaskMapper(TaskData taskData, boolean createNonExistingAttributes) {
		super(taskData, createNonExistingAttributes);
	}

	/**
	 * Initialize the task data with all the necessary task data attributes to create a new empty Mylyn
	 * Cardwall.
	 */
	public void initializeEmptyTaskData() {
		// Summary
		this.getWriteableAttribute(TaskAttribute.STATUS, TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Kind
		this.getWriteableAttribute(TaskAttribute.TASK_KIND, TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Backlog Items Kind
		this.getWriteableAttribute(IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, null);
	}

	/**
	 * Sets the kind of the backlog items.
	 * 
	 * @param backlogItemsKind
	 *            The kind of the backlog items
	 */
	public void setBacklogItemsKind(String backlogItemsKind) {
		TaskAttribute taskAttribute = this.getWriteableAttribute(
				IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, null);
		if (taskAttribute != null) {
			taskAttribute.setValue(backlogItemsKind);
		}
	}

	/**
	 * Creates a state in the cardwall with its mappings.
	 * 
	 * @param cardwallState
	 *            The cardwall state to add
	 */
	public void addCardwallState(CardwallState cardwallState) {
		TaskAttribute taskAttribute = this.getWriteableAttribute(
				IMylynAgileCoreConstants.PREFIX_MILESTONE_STATE + String.valueOf(cardwallState.getId()),
				IMylynAgileCoreConstants.TYPE_MILESTONE_STATE);
		if (taskAttribute != null) {
			// Label
			TaskAttribute labelTaskAttribute = taskAttribute
					.createMappedAttribute(IMylynAgileCoreConstants.MILESTONE_NAME);
			labelTaskAttribute.setValue(cardwallState.getLabel());

			// Mappings
			int count = 0;
			Collection<CardwallStateMapping> mappings = cardwallState.getMappings();
			for (CardwallStateMapping cardwallStateMapping : mappings) {
				TaskAttribute mappingAttribute = taskAttribute
						.createAttribute(IMylynAgileCoreConstants.PREFIX_MILESTONE_STATE_MAPPING
								+ String.valueOf(count));
				mappingAttribute.getMetaData().setType(IMylynAgileCoreConstants.TYPE_MILESTONE_STATE_MAPPING);

				TaskAttribute trackerIdTaskAttribute = mappingAttribute
						.createAttribute(IMylynAgileCoreConstants.TRACKER_ID);
				trackerIdTaskAttribute.setValue(String.valueOf(cardwallStateMapping.getTrackerId()));

				TaskAttribute stateValuesIdTaskAttribute = mappingAttribute
						.createAttribute(IMylynAgileCoreConstants.MILESTONE_STATE_MAPPING_STATE_VALUES_IDS);
				List<String> values = new ArrayList<String>();

				Collection<Integer> stateValuesId = cardwallStateMapping.getStateValuesId();
				for (Integer integer : stateValuesId) {
					values.add(integer.toString());
				}
				stateValuesIdTaskAttribute.setValues(values);

				count++;
			}
		}
	}

	/**
	 * Creates a backlog item in the cardwall.
	 */
	public void addCardwallBacklogItem() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates an artifact in a backlog item.
	 */
	public void addCardwallBacklogArtifact() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the kind of the backlog items. The kind of the backlog item is a human readable string.
	 * 
	 * @return The kind of the backlog items
	 */
	public String getBacklogItemsKind() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the collection of all the title of the states of the cardwall.
	 * 
	 * @return The collection of all the title of the states of the cardwall
	 */
	public Collection<CardwallState> getAllCardwallStates() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the collection of all the backlog items of the cardwall.
	 * 
	 * @return The collection of all the backlog items of the cardwall
	 */
	public Collection<CardwallBacklogItem> getAllCardwallBacklogItems() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the collection of all the cardwall artifacts from the given cardwall backlog item.
	 * 
	 * @param cardwallBacklogItem
	 *            The cardwall backlog item
	 * @return The collection of all the cardwall artifacts from the given cardwall backlog item
	 */
	public Collection<CardwallArtifact> getAllCardwallArtifacts(CardwallBacklogItem cardwallBacklogItem) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the cardwall state from the given cardwall artifact.
	 * 
	 * @param cardwallArtifact
	 *            The cardwall artifact
	 * @return The cardwall state from the given cardwall artifact
	 */
	public CardwallState getCardwallState(CardwallArtifact cardwallArtifact) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns all the cardwall artifacts from te given cardwall backlog item that matches the given cardwall
	 * state.
	 * 
	 * @param cardwallBacklogItem
	 *            The cardwall backlog item
	 * @param cardwallState
	 *            The cardwall state
	 * @return All the cardwall artifacts from te given cardwall backlog item that matches the given cardwall
	 *         state
	 */
	public Collection<CardwallArtifact> getCardwallArtifacts(CardwallBacklogItem cardwallBacklogItem,
			CardwallState cardwallState) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Change the state of the given cardwall artifact to the given cardwall state.
	 * 
	 * @param cardwallArtifact
	 *            The cardwall artifact
	 * @param cardwallState
	 *            The cardwall state
	 */
	public void changeState(CardwallArtifact cardwallArtifact, CardwallState cardwallState) {
		throw new UnsupportedOperationException();
	}
}
