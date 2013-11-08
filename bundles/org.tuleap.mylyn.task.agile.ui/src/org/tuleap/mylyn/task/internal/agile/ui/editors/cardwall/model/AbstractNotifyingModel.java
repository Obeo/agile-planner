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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 * Abstract super-class of notifying model elements.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractNotifyingModel {

	/**
	 * The property change support.
	 */
	protected PropertyChangeSupport mPcs = new PropertyChangeSupport(this);

	/**
	 * The vetoable change support.
	 */
	protected VetoableChangeSupport mVcs = new VetoableChangeSupport(this);

	/**
	 * Adds the given listener.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		mPcs.addPropertyChangeListener(listener);
	}

	/**
	 * Removes the given listener.
	 * 
	 * @param listener
	 *            listener to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		mPcs.removePropertyChangeListener(listener);
	}

	/**
	 * Adds the given listener.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addVetoableChangeListener(VetoableChangeListener listener) {
		mVcs.addVetoableChangeListener(listener);
	}

	/**
	 * Removes the given listener.
	 * 
	 * @param listener
	 *            listener to remove.
	 */
	public void removeVetoableChangeListener(VetoableChangeListener listener) {
		mVcs.removeVetoableChangeListener(listener);
	}
}
