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

import java.util.Collections;
import java.util.List;

/**
 * Basic implementation of {@link IThreeWayList}.
 * 
 * @param <T>
 *            the type of elements in the lists.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BasicThreeWayList<T> implements IThreeWayList<T> {

	/**
	 * The list ID (can be null).
	 */
	private final String id;

	/**
	 * The common ancestor list.
	 */
	private final List<T> ancestor;

	/**
	 * The local list.
	 */
	private final List<T> local;

	/**
	 * The remote list.
	 */
	private final List<T> remote;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The id
	 * @param ancestor
	 *            the ancestor list
	 * @param local
	 *            the local list
	 * @param remote
	 *            the remote list
	 */
	public BasicThreeWayList(String id, List<T> ancestor, List<T> local, List<T> remote) {
		this.id = id;
		this.ancestor = Collections.unmodifiableList(ancestor);
		this.local = Collections.unmodifiableList(local);
		this.remote = Collections.unmodifiableList(remote);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList#getListId()
	 */
	@Override
	public String getListId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList#getCommonAncestor()
	 */
	@Override
	public List<T> getCommonAncestor() {
		return ancestor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList#getRemote()
	 */
	@Override
	public List<T> getRemote() {
		return remote;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.core.merge.IThreeWayList#getLocal()
	 */
	@Override
	public List<T> getLocal() {
		return local;
	}

}
