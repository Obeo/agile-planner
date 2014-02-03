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

import java.util.List;

/**
 * Interface for performing three-way merge of lists identified by an ID.
 * 
 * @param <T>
 *            the type of elements in the lists.
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IThreeWayList<T> {

	/**
	 * The list identifier (can be used to identify a milestone).
	 * 
	 * @return The list identifier, possibly null.
	 */
	String getListId();

	/**
	 * The common ancestor list in the three-way merge.
	 * 
	 * @return the common ancestor list, never null.
	 */
	List<T> getCommonAncestor();

	/**
	 * The remote list in the three-way merge.
	 * 
	 * @return the remote list, never null.
	 */
	List<T> getRemote();

	/**
	 * The local list in the three-way merge.
	 * 
	 * @return the local list, never null.
	 */
	List<T> getLocal();
}
