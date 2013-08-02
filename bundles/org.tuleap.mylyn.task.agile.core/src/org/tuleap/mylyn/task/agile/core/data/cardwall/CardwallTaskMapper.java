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
	 * Creates a backlog item in the card wall.
	 * 
	 * @param cardwallBacklogItem
	 *            The backlog item to add
	 */
	public void addCardwallBacklogItem(CardwallBacklogItem cardwallBacklogItem) {
		TaskAttribute taskAttribute = this.getWriteableAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM
				+ String.valueOf(cardwallBacklogItem.getId()), IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		if (taskAttribute != null) {

			// title
			TaskAttribute labelTaskAttribute = taskAttribute
					.createMappedAttribute(IMylynAgileCoreConstants.MILESTONE_NAME);
			labelTaskAttribute.setValue(cardwallBacklogItem.getTitle());

			// Kind
			TaskAttribute kindTaskAttribute = taskAttribute
					.createMappedAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_KIND);
			kindTaskAttribute.setValue(cardwallBacklogItem.getKind());

			// Artifacts
			Collection<CardwallArtifact> artifacts = cardwallBacklogItem.getArtifacts();

			if (artifacts != null) {

				for (CardwallArtifact cardwallArtifact : artifacts) {
					TaskAttribute artifactAttribute = taskAttribute
							.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM_ARTIFACT
									+ String.valueOf(cardwallArtifact.getId()));
					artifactAttribute.getMetaData().setType(
							IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM_ARTIFACT);

					// tracker id
					TaskAttribute trackerIdTaskAttribute = artifactAttribute
							.createAttribute(IMylynAgileCoreConstants.TRACKER_ID);
					trackerIdTaskAttribute.setValue(String.valueOf(cardwallArtifact.getTrackerId()));

					// the name
					TaskAttribute titleTaskAttribute = artifactAttribute
							.createAttribute(IMylynAgileCoreConstants.TITLE_BACKLOG_ITEM_ARTIFACT);
					titleTaskAttribute.setValue(cardwallArtifact.getTitle());

					// the kind
					TaskAttribute artifactKindTaskAttribute = artifactAttribute
							.createAttribute(IMylynAgileCoreConstants.KIND_BACKLOG_ITEM_ARTIFACT);
					artifactKindTaskAttribute.setValue(cardwallArtifact.getKind());

					// the values
					TaskAttribute artifactStateValueTaskAttribute = artifactAttribute
							.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE);
					artifactStateValueTaskAttribute.setValue(String.valueOf(cardwallArtifact
							.getCardwallStateValue().getFieldValueId()));

				}
			}
		}
	}

	/**
	 * Creates an artifact in a backlog item.
	 * 
	 * @param cardwallBacklogItem
	 *            the backlog item
	 * @param cardwallArtifact
	 *            the artifact to add
	 */
	public void addCardwallBacklogArtifact(CardwallBacklogItem cardwallBacklogItem,
			CardwallArtifact cardwallArtifact) {

		TaskAttribute backLogtaskAttribute = this.getWriteableAttribute(
				IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM + String.valueOf(cardwallBacklogItem.getId()),
				IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		TaskAttribute artifactAttribute = backLogtaskAttribute
				.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM_ARTIFACT
						+ String.valueOf(cardwallArtifact.getId()));
		artifactAttribute.getMetaData().setType(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM_ARTIFACT);

		// tracker id
		TaskAttribute trackerIdTaskAttribute = artifactAttribute
				.createAttribute(IMylynAgileCoreConstants.TRACKER_ID);
		trackerIdTaskAttribute.setValue(String.valueOf(cardwallArtifact.getTrackerId()));

		// the name
		TaskAttribute titleTaskAttribute = artifactAttribute
				.createAttribute(IMylynAgileCoreConstants.TITLE_BACKLOG_ITEM_ARTIFACT);
		titleTaskAttribute.setValue(cardwallArtifact.getTitle());

		// the kind
		TaskAttribute artifactKindTaskAttribute = artifactAttribute
				.createAttribute(IMylynAgileCoreConstants.KIND_BACKLOG_ITEM_ARTIFACT);
		artifactKindTaskAttribute.setValue(cardwallArtifact.getKind());

		// the values
		TaskAttribute artifactStateValueTaskAttribute = artifactAttribute
				.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE);
		artifactStateValueTaskAttribute.setValue(String.valueOf(cardwallArtifact.getCardwallStateValue()
				.getFieldValueId()));

		cardwallBacklogItem.addArtifact(cardwallArtifact);

	}

	/**
	 * Returns the kind of the backlog items. The kind of the backlog item is a human readable string.
	 * 
	 * @return The kind of the backlog items
	 */
	public String getBacklogItemsKind() {
		TaskAttribute taskAttribute = this.getWriteableAttribute(
				IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, null);
		if (taskAttribute != null) {
			return taskAttribute.getValue();
		}
		return null;
	}

	/**
	 * Returns the collection of all the title of the states of the card wall.
	 * 
	 * @return The collection of all the title of the states of the card wall
	 */
	public Collection<CardwallState> getAllCardwallStates() {

		Collection<CardwallState> cardwallStateList = new ArrayList<CardwallState>();
		TaskAttribute attribute = this.taskData.getRoot();
		for (CardwallState cardwallState : CardwallState.getObjectsList()) {
			for (TaskAttribute taskAttribute : attribute.getAttributes().values()) {
				if (taskAttribute.getMetaData().getType().equals(
						IMylynAgileCoreConstants.TYPE_MILESTONE_STATE)
						&& taskAttribute.getId().equals(
								IMylynAgileCoreConstants.PREFIX_MILESTONE_STATE
										+ String.valueOf(cardwallState.getId()))) {
					cardwallStateList.add(cardwallState);
				}
			}
		}
		return cardwallStateList;
	}

	/**
	 * Returns the collection of all the backlog items of the card wall.
	 * 
	 * @return The collection of all the backlog items of the card wall
	 */
	public Collection<CardwallBacklogItem> getAllCardwallBacklogItems() {

		Collection<CardwallBacklogItem> cardwallBacklogItemsList = new ArrayList<CardwallBacklogItem>();
		TaskAttribute attribute = this.taskData.getRoot();

		for (CardwallBacklogItem cardwallBacklogItem : CardwallBacklogItem.getObjectsList()) {
			for (TaskAttribute taskAttribute : attribute.getAttributes().values()) {
				if (taskAttribute.getMetaData().getType().equals(IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM)
						&& taskAttribute.getId().equals(
								IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM
										+ String.valueOf(cardwallBacklogItem.getId()))) {
					cardwallBacklogItemsList.add(cardwallBacklogItem);
				}
			}
		}
		return cardwallBacklogItemsList;
	}

	/**
	 * Returns the collection of all the cardwall artifacts from the given cardwall backlog item.
	 * 
	 * @param cardwallBacklogItem
	 *            The cardwall backlog item
	 * @return The collection of all the cardwall artifacts from the given cardwall backlog item
	 */
	public Collection<CardwallArtifact> getAllCardwallArtifacts(CardwallBacklogItem cardwallBacklogItem) {

		Collection<CardwallArtifact> cardwallArtifactsList = new ArrayList<CardwallArtifact>();
		TaskAttribute taskAttribute = this.getWriteableAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM
				+ String.valueOf(cardwallBacklogItem.getId()), IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM);

		for (CardwallArtifact cardwallArtifact : CardwallArtifact.getObjectsList()) {
			for (TaskAttribute attribute : taskAttribute.getAttributes().values()) {

				if (attribute.getId().equals(
						IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM_ARTIFACT
								+ String.valueOf(cardwallArtifact.getId()))
						&& attribute.getMetaData().getType().equals(
								IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM_ARTIFACT)) {
					cardwallArtifactsList.add(cardwallArtifact);
				}
			}
		}
		return cardwallArtifactsList;

	}

	/**
	 * Returns the cardwall state from the given cardwall artifact.
	 * 
	 * @param cardwallArtifact
	 *            The cardwall artifact
	 * @return The cardwall state from the given cardwall artifact
	 */
	public CardwallState getCardwallState(CardwallArtifact cardwallArtifact) {

		CardwallState state = null;

		for (CardwallState cardwallState : CardwallState.getObjectsList()) {
			for (CardwallStateMapping cardwallStateMapping : cardwallState.getMappings()) {
				if (cardwallStateMapping.getTrackerId() == cardwallArtifact.getTrackerId()
						&& cardwallStateMapping.getStateValuesId().contains(
								Integer.valueOf(cardwallArtifact.getCardwallStateValue().getFieldId()))) {
					state = cardwallState;
				}

			}
		}
		return state;
	}

	/**
	 * Returns all the cardwall artifacts from the given cardwall backlog item that matches the given cardwall
	 * state.
	 * 
	 * @param cardwallBacklogItem
	 *            The cardwall backlog item
	 * @param cardwallState
	 *            The cardwall state
	 * @return All the cardwall artifacts from the given cardwall backlog item that matches the given cardwall
	 *         state
	 */
	public Collection<CardwallArtifact> getCardwallArtifacts(CardwallBacklogItem cardwallBacklogItem,
			CardwallState cardwallState) {

		Collection<CardwallArtifact> cardwallArtifactsList = new ArrayList<CardwallArtifact>();

		if (cardwallBacklogItem.getArtifacts() != null) {

			for (CardwallArtifact cardwallArtifact : cardwallBacklogItem.getArtifacts()) {
				for (CardwallStateMapping cardwallStateMapping : cardwallState.getMappings()) {
					if (cardwallStateMapping.getTrackerId() == cardwallArtifact.getTrackerId()
							&& cardwallStateMapping.getStateValuesId().contains(
									Integer.valueOf(cardwallArtifact.getCardwallStateValue().getFieldId()))) {
						cardwallArtifactsList.add(cardwallArtifact);
					}
				}
			}
		}
		return cardwallArtifactsList;
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

		for (CardwallStateMapping cardwallStateMapping : cardwallState.getMappings()) {
			if (cardwallStateMapping.getTrackerId() == cardwallArtifact.getTrackerId()) {

				// we take the first element of the collection
				cardwallArtifact.getCardwallStateValue().setFieldId(
						cardwallStateMapping.getStateValuesId().iterator().next().intValue());
			}
		}

	}
}
