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
package org.tuleap.mylyn.task.agile.core.data;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;

/**
 * Add a notification mechanism to wrappers.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractNotifyingWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * TaskAttribute id used to mark parent task attribute as changed.
	 */
	public static final String CHANGED = "changed"; //$NON-NLS-1$

	/**
	 * Task Attribute Change listeners.
	 */
	private final Set<ITaskAttributeChangeListener> listeners = Sets.newHashSet();

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 * @param prefix
	 *            the prefix to use
	 * @param id
	 *            The functional ID of the wrapped element.
	 */
	public AbstractNotifyingWrapper(TaskAttribute root, String prefix, String id) {
		super(root, prefix, id);
	}

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 *            The listener to add
	 * @return {@code true} if and only if the given listener has been added and was not already registered.
	 */
	public boolean addListener(ITaskAttributeChangeListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 *            The listener to remove
	 * @return {@code true} if and only if the listener was registered and has been removed.
	 */
	public boolean removeListener(ITaskAttributeChangeListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Add a collection of listeners.
	 * 
	 * @param someListeners
	 *            The listeners to add
	 */
	public void addAllListeners(Collection<ITaskAttributeChangeListener> someListeners) {
		this.listeners.addAll(someListeners);
	}

	/**
	 * Mark the given {@link TaskAttribute} as changed or unchanged.
	 * 
	 * @param att
	 *            The attribute to mark
	 * @param changed
	 *            flag indicating whether the {@link TaskAttribute} has been changed.
	 * @return <code>true</code> if and only if a change has really been marked.
	 */
	public boolean mark(TaskAttribute att, boolean changed) {
		Assert.isNotNull(att);
		boolean madeAChange = false;
		if (changed && att.getAttribute(CHANGED) == null) {
			att.createAttribute(CHANGED);
			madeAChange = true;
		} else if (!changed && att.getAttribute(CHANGED) != null) {
			att.removeAttribute(CHANGED);
			madeAChange = true;
		}
		return madeAChange;
	}

	/**
	 * Mark the modified attributes since last read from server. Each modified attribute is marked as changed
	 * or unchanged according to its value compared to the latest (most recent) remote value.
	 * 
	 * @param taskDataModel
	 *            The {@link TaskDataModel}
	 * @param filter
	 *            A predicate to filter those attributes that must be marked. Only attributes that pass the
	 *            predicate will be marked/unmarked.
	 */
	public void markChanges(TaskDataModel taskDataModel, Predicate<TaskAttribute> filter) {
		Set<TaskAttribute> changedOldAttributes = taskDataModel.getChangedOldAttributes();
		Set<TaskAttribute> changedAttributes = taskDataModel.getChangedAttributes();
		// Go over all attributes and check whether their local value is different from the latest remote
		// value
		TaskData localData = taskDataModel.getTaskData();
		for (TaskAttribute changedOldAtt : changedOldAttributes) {
			if (filter == null || filter.apply(changedOldAtt)) {
				TaskAttribute changedAtt = localData.getRoot().getAttribute(changedOldAtt.getId());
				if (changedAtt != null) {
					if (mark(changedAtt, !changedAtt.getValues().equals(changedOldAtt.getValues()))) {
						taskDataModel.attributeChanged(changedAtt);
					}
					changedAttributes.remove(changedAtt);
				}
			}
		}
		for (TaskAttribute changedAtt : changedAttributes) {
			// These are new attributes, they need to be marked.
			if (filter == null || filter.apply(changedAtt)) {
				if (mark(changedAtt, true)) {
					taskDataModel.attributeChanged(changedAtt);
				}
			}
		}
	}

	/**
	 * Informs the listeners of the given attribute's modification.
	 * 
	 * @param att
	 *            The modified attribute.
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute att) {
		for (ITaskAttributeChangeListener l : listeners) {
			l.attributeChanged(att);
		}
	}

}
