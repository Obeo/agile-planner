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
package org.tuleap.mylyn.task.internal.agile.core.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Utility class to manipulate lists.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ListUtil {

	/**
	 * Private constructor.
	 */
	private ListUtil() {
		//
	}

	/**
	 * Computes the result of merging a list of elements into a target list before or after a target element.
	 * If the target is null, insertion is made at the end of the target list.
	 * 
	 * @param movedElements
	 *            Elements to move or insert
	 * @param targetElements
	 *            Target list
	 * @param targetId
	 *            Id of the target element
	 * @param before
	 *            Whether insertion must take place before or after the target.
	 * @return A new list that contains the result of the merge.
	 */
	public static List<String> mergeItems(List<String> movedElements, List<String> targetElements,
			String targetId, boolean before) {
		List<String> newValues = Lists.newArrayList();
		boolean insertedValues = false;
		for (String v : targetElements) {
			if (!movedElements.contains(v)) {
				if (v.equals(targetId)) {
					if (before) {
						newValues.addAll(movedElements);
						newValues.add(v);
					} else {
						newValues.add(v);
						newValues.addAll(movedElements);
					}
					insertedValues = true;
				} else {
					newValues.add(v);
				}
			} else if (v.equals(targetId)) {
				newValues.addAll(movedElements);
				insertedValues = true;
			}
		}
		if (!insertedValues) {
			newValues.addAll(movedElements);
		}
		return newValues;
	}

}
