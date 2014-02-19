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
package org.tuleap.mylyn.task.agile.core.data.planning;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.tuleap.mylyn.task.internal.agile.core.merge.BasicThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList;
import org.tuleap.mylyn.task.internal.agile.core.merge.MultiListMerger;

/**
 * Used to merge plannings.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningDataModelMerger {

	/**
	 * The model.
	 */
	private final TaskDataModel model;

	/**
	 * The wrapper.
	 */
	private final MilestonePlanningWrapper wrapper;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            The model
	 */
	public PlanningDataModelMerger(TaskDataModel model) {
		this.model = model;
		this.wrapper = new MilestonePlanningWrapper(model.getTaskData().getRoot());
	}

	/**
	 * Perform the merge.
	 */
	public void merge() {
		MultiListMerger<String> merger = new MultiListMerger<String>();
		Map<String, List<String>> merge = merger.merge(buildLists());
		for (Entry<String, List<String>> entry : merge.entrySet()) {
			String key = entry.getKey();
			TaskAttribute attribute;
			if (key == null) {
				attribute = wrapper.getBacklogTaskAttribute();
			} else {
				attribute = wrapper.getSubMilestone(key).getWrappedAttribute();
			}
			List<String> values = entry.getValue();
			if (!values.equals(attribute.getValues())) {
				attribute.setValues(values);
				model.attributeChanged(attribute);
			}
		}
	}

	/**
	 * Compute the 3-way lists from the {@link TaskDataModel}.
	 * 
	 * @return The list of 3-way lists including the backlog and each milestone's content.
	 */
	private List<IThreeWayList<String>> buildLists() {
		List<IThreeWayList<String>> lists = Lists.newArrayList();
		TaskAttribute local = wrapper.getBacklogTaskAttribute();
		TaskAttribute ancestor = model.getLastReadAttribute(local);
		TaskAttribute remote = getRepositoryAttribute(local);
		if (ancestor == null) {
			ancestor = remote;
		}
		BasicThreeWayList<String> list3W = new BasicThreeWayList<String>(null, ancestor.getValues(), local
				.getValues(), remote.getValues());
		lists.add(list3W);
		for (SubMilestoneWrapper subMilestone : wrapper.getSubMilestones()) {
			String milestoneId = subMilestone.getId();
			local = subMilestone.getWrappedAttribute();
			ancestor = model.getLastReadAttribute(local);
			remote = getRepositoryAttribute(local);
			if (ancestor == null) {
				ancestor = remote;
			}
			list3W = new BasicThreeWayList<String>(milestoneId, ancestor.getValues(), local.getValues(),
					remote.getValues());
			lists.add(list3W);
		}
		return lists;
	}

	/**
	 * Get the repository TaskAttribute for the given ID.
	 * 
	 * @param localAtt
	 *            the local task attribute
	 * @return the corresponding repository TaskAttribute, never null.
	 */
	private TaskAttribute getRepositoryAttribute(TaskAttribute localAtt) {
		TaskAttribute result = null;
		String id = localAtt.getId();
		for (TaskAttribute att : model.getChangedOldAttributes()) {
			if (id.equals(att.getId())) {
				result = att;
				break;
			}
		}
		if (result == null) {
			result = localAtt;
		}
		return result;
	}
}
