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
	 */
	public CardwallTaskMapper(TaskData taskData) {
		super(taskData);
	}

	/**
	 * Initialize the task data with all the necessary task data attributes to create a new empty Mylyn
	 * Cardwall.
	 */
	public void initializeEmptyTaskData() {
		// Summary
		this.createAttribute(TaskAttribute.STATUS, TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Kind
		this.createAttribute(TaskAttribute.TASK_KIND, TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Backlog Items Kind
		this.createAttribute(IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, null);
	}

	/**
	 * Sets the kind of the backlog items.
	 * 
	 * @param backlogItemsKind
	 *            The kind of the backlog items
	 */
	public void setBacklogItemsKind(String backlogItemsKind) {
		TaskAttribute taskAttribute = this.getWriteableAttribute(
				IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND, TaskAttribute.TYPE_SHORT_TEXT);
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
		TaskAttribute taskAttribute = this.createAttribute(IMylynAgileCoreConstants.PREFIX_MILESTONE_STATE
				+ String.valueOf(cardwallState.getId()), IMylynAgileCoreConstants.TYPE_MILESTONE_STATE);
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
		TaskAttribute taskAttribute = this.createAttribute(IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM
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

					TaskAttribute artifactStateValueTaskAttributeFieldId = artifactStateValueTaskAttribute
							.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE_FIELD_ID);
					artifactStateValueTaskAttributeFieldId.setValue(String.valueOf(cardwallArtifact
							.getCardwallStateValue().getFieldId()));

					TaskAttribute artifactStateValueTaskAttributeFieldLabel = artifactStateValueTaskAttribute
							.createAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE_FIELD_LABEL);
					artifactStateValueTaskAttributeFieldLabel.setValue(String.valueOf(cardwallArtifact
							.getCardwallStateValue().getFieldLabel()));

				}
			}
		}
	}

	/**
	 * Returns the kind of the backlog items. The kind of the backlog item is a human readable string.
	 * 
	 * @return The kind of the backlog items, which can be null.
	 */
	public String getBacklogItemsKind() {
		TaskAttribute taskAttribute = this
				.getMappedAttribute(IMylynAgileCoreConstants.MILESTONE_BACKLOG_ITEMS_KIND);
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

		for (TaskAttribute stateTaskAttribute : this.taskData.getRoot().getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_MILESTONE_STATE.equals(stateTaskAttribute.getMetaData()
					.getType())) {

				TaskAttribute labelAttribute = stateTaskAttribute
						.getMappedAttribute(IMylynAgileCoreConstants.MILESTONE_NAME);
				Collection<CardwallStateMapping> mappingsList = new ArrayList<CardwallStateMapping>();
				for (TaskAttribute mappingAttribute : stateTaskAttribute.getAttributes().values()) {
					if (mappingAttribute.getMetaData().getType() != null
							&& IMylynAgileCoreConstants.TYPE_MILESTONE_STATE_MAPPING.equals(mappingAttribute
									.getMetaData().getType())) {

						TaskAttribute trackerIdTaskAttribute = mappingAttribute
								.getMappedAttribute(IMylynAgileCoreConstants.TRACKER_ID);

						TaskAttribute stateValuesIdTaskAttribute = mappingAttribute
								.getMappedAttribute(IMylynAgileCoreConstants.MILESTONE_STATE_MAPPING_STATE_VALUES_IDS);

						Collection<Integer> values = convertStringArraytoIntArray(stateValuesIdTaskAttribute
								.getValues());

						CardwallStateMapping cardwallStateMapping = new CardwallStateMapping(Integer
								.parseInt(trackerIdTaskAttribute.getValue()), values);

						mappingsList.add(cardwallStateMapping);
					}
				}

				CardwallState cardwallState = new CardwallState(Integer.parseInt(stateTaskAttribute.getId()
						.replace(IMylynAgileCoreConstants.PREFIX_MILESTONE_STATE, "")), labelAttribute //$NON-NLS-1$
						.getValue(), mappingsList);

				cardwallStateList.add(cardwallState);
			}
		}

		return cardwallStateList;
	}

	/**
	 * A method that permits converting a string list to an integer one.
	 * 
	 * @param stringList
	 *            a list of strings
	 * @return a list of Integers
	 */
	public List<Integer> convertStringArraytoIntArray(List<String> stringList) {
		if (stringList != null) {
			List<Integer> integerList = new ArrayList<Integer>();
			for (int i = 0; i < stringList.size(); i++) {
				integerList.add(Integer.valueOf(stringList.get(i)));
			}
			return integerList;
		}
		return null;
	}

	/**
	 * Returns the collection of all the backlog items of the card wall.
	 * 
	 * @return The collection of all the backlog items of the card wall
	 */
	public Collection<CardwallBacklogItem> getAllCardwallBacklogItems() {
		Collection<CardwallBacklogItem> backlogItems = new ArrayList<CardwallBacklogItem>();

		for (TaskAttribute backlogItemsTaskAttribute : this.taskData.getRoot().getAttributes().values()) {
			if (backlogItemsTaskAttribute.getMetaData().getType() != null
					&& IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(backlogItemsTaskAttribute
							.getMetaData().getType())) {

				TaskAttribute labelTaskAttribute = backlogItemsTaskAttribute
						.getMappedAttribute(IMylynAgileCoreConstants.MILESTONE_NAME);

				TaskAttribute kindTaskAttribute = backlogItemsTaskAttribute
						.getMappedAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_KIND);

				Collection<CardwallArtifact> artifacts = new ArrayList<CardwallArtifact>();

				if (backlogItemsTaskAttribute.getAttributes().values() != null) {

					for (TaskAttribute artifactAttribute : backlogItemsTaskAttribute.getAttributes().values()) {

						if (artifactAttribute.getMetaData().getType() != null
								&& IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM_ARTIFACT
										.equals(artifactAttribute.getMetaData().getType())) {

							// tracker id
							TaskAttribute trackerIdTaskAttribute = artifactAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.TRACKER_ID);

							// the name
							TaskAttribute titleTaskAttribute = artifactAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.TITLE_BACKLOG_ITEM_ARTIFACT);

							// the kind
							TaskAttribute artifactKindTaskAttribute = artifactAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.KIND_BACKLOG_ITEM_ARTIFACT);

							// the values
							TaskAttribute artifactStateValueTaskAttribute = artifactAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE);

							TaskAttribute artifactStateValueTaskAttributeFieldId = artifactStateValueTaskAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE_FIELD_ID);

							TaskAttribute artifactStateValueTaskAttributeFieldLabel = artifactStateValueTaskAttribute
									.getMappedAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_ARTIFACT_STATE_VALUE_FIELD_LABEL);

							CardwallStateValue cardwallStateValue = new CardwallStateValue(Integer
									.parseInt(artifactStateValueTaskAttribute.getValue()), Integer
									.parseInt(artifactStateValueTaskAttributeFieldId.getValue()),
									artifactStateValueTaskAttributeFieldLabel.getValue());

							CardwallArtifact cardwallArtifact = new CardwallArtifact(
									Integer.parseInt(artifactAttribute.getId().replace(
											IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM_ARTIFACT, "")), //$NON-NLS-1$
									titleTaskAttribute.getValue(), artifactKindTaskAttribute.getValue(),
									Integer.parseInt(trackerIdTaskAttribute.getValue()), cardwallStateValue);

							artifacts.add(cardwallArtifact);
						}
					}

					CardwallBacklogItem cardwallBacklogItem = new CardwallBacklogItem(Integer
							.parseInt(backlogItemsTaskAttribute.getId().replace(
									IMylynAgileCoreConstants.PREFIX_BACKLOG_ITEM, "")), labelTaskAttribute //$NON-NLS-1$
							.getValue(), kindTaskAttribute.getValue());
					backlogItems.add(cardwallBacklogItem);
				}
			}
		}

		return backlogItems;
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

		for (CardwallState cardwallState : this.getAllCardwallStates()) {
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
	 * Change the state of the given cardwall artifact to the given cardwall state.
	 * 
	 * @param cardwallArtifact
	 *            The cardwall artifact
	 * @param cardwallState
	 *            The cardwall state
	 */
	public void changeState(CardwallArtifact cardwallArtifact, CardwallState cardwallState) {
		// TODO change the state in the task data!!!
	}
}
