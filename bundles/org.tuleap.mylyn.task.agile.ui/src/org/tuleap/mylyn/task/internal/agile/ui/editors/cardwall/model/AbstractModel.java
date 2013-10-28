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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Abstract super-class of notifying model elements.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractModel {

	/**
	 * The registered listeners.
	 */
	private List<IModelListener> listeners;

	/**
	 * Adds the given listener.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addModelListener(IModelListener listener) {
		if (listeners == null) {
			listeners = Lists.newArrayList();
		}
		listeners.add(listener);
	}

	/**
	 * Removes the given listener.
	 * 
	 * @param listener
	 *            Listener to remove.
	 */
	public void removeModelListener(IModelListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	/**
	 * Notifies all the registered listeners of the given event.
	 * 
	 * @param event
	 *            Event that occurred.
	 */
	public void fireEvent(CardwallEvent event) {
		if (listeners != null) {
			for (IModelListener listener : listeners) {
				listener.eventOccurred(event);
			}
		}

	}
}
