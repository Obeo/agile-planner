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

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Add a notification mechanism to wrappers.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractNotifyingWrapper extends AbstractTaskAttributeWrapper {

	/**
	 * Task Attribute Change listeners.
	 */
	private final Set<ITaskAttributeChangeListener> listeners = Sets.newHashSet();

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 */
	public AbstractNotifyingWrapper(TaskAttribute root) {
		super(root);
	}

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 * @param id
	 *            The functional ID of the wrapped element.
	 */
	public AbstractNotifyingWrapper(TaskAttribute root, int id) {
		super(root, id);
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
	 * Informs the listeners of the given attribute's modification.
	 * 
	 * @param attribute
	 *            The modified attribute.
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		for (ITaskAttributeChangeListener l : listeners) {
			l.attributeChanged(attribute);
		}
	}

}
