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
package org.tuleap.mylyn.task.internal.agile.core.merge;

/**
 * A functional change set groups a local functional change and a remote functional change.
 *
 * @param <T>
 *            The type of elements in the lists.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FunctionalChangeSet<T> {

	/**
	 * The local change.
	 */
	private final FunctionalChange<T> localChange;

	/**
	 * The remote change.
	 */
	private final FunctionalChange<T> remoteChange;

	/**
	 * Constructor.
	 *
	 * @param localChange
	 *            The local change
	 * @param remoteChange
	 *            The remote change
	 */
	public FunctionalChangeSet(FunctionalChange<T> localChange, FunctionalChange<T> remoteChange) {
		this.localChange = localChange;
		this.remoteChange = remoteChange;
	}

	/**
	 * Local change.
	 *
	 * @return the localChange
	 */
	public FunctionalChange<T> getLocalChange() {
		return localChange;
	}

	/**
	 * Remote change.
	 *
	 * @return the remoteChange
	 */
	public FunctionalChange<T> getRemoteChange() {
		return remoteChange;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FunctionalChange\n\tlocal:  " + localChange + "\n\tremote" + remoteChange; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
