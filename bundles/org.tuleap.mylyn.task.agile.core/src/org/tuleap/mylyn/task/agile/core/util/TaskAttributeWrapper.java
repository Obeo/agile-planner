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
package org.tuleap.mylyn.task.agile.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Utility class supposed to manage moving, adding and removing elements from lists of TaskAttributes in their
 * parent. This is not trivial due to the fact that the parent contains its children by key in a
 * LinkedHashMap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TaskAttributeWrapper {

	/**
	 * The wrapped attribute.
	 */
	private final TaskAttribute attribute;

	/**
	 * Constructor of the wrapper.
	 * 
	 * @param anAttribute
	 *            The attribute to wrap.
	 */
	public TaskAttributeWrapper(TaskAttribute anAttribute) {
		this.attribute = anAttribute; // TODO Check not null
	}

	/**
	 * Moves children elements of the wrapped attribute to a given position, restricting the reordering to
	 * attributes of the given type if it is not null.
	 * 
	 * @param attributes
	 *            The set of attributes to move. Attributes must be direct children of the wrapped attribute,
	 *            and they must match the given {@code type} if it is not {@code null}.
	 *            <p>
	 *            <b>Warning</b> Any attribute that does not respect these criteria will be silently ignored.
	 *            </p>
	 * @param position
	 *            The position where the moved elements must be inserted.
	 * @param type
	 *            The type to restrict elements concerned by the move.
	 *            <p>
	 *            Only children of this type in the parent attribute will be reordered if not null. If
	 *            {@code type} is not null, all the elements in {@code attributes} must be of this type. (i.e.
	 *            {@code type.equals(att.getMetaData().getType())} must return {@code true}.)
	 *            </p>
	 *            <p>
	 *            <b>Warning</b> Attributes that are not of this type will be silently ignored.
	 *            </p>
	 */
	public void moveElementsSortedByValue(final List<TaskAttribute> attributes, final int position,
			final String type) {
		removeNonChildrenElementsFrom(attributes);
		// We sort the moved elements by their value to keep their order after the move operation
		Collections.sort(attributes, new TaskAttributeComparator());
		TaskAttributeList tal = new TaskAttributeList(attributes);
		int nbMovedElements = attributes.size();
		// int nbMovedElementsBeforeTarget = tal.getNumberHavingValueLessThan(position);
		for (TaskAttribute child : attribute.getAttributes().values()) {
			if (type == null || type.equals(child.getMetaData().getType())) {
				if (!attributes.contains(child)) {
					int attRank = Integer.parseInt(child.getValue());
					if (attRank < position) {
						child.setValue(String.valueOf(attRank - tal.getNumberHavingValueLessThan(attRank)));
					} else {
						child.setValue(String.valueOf(attRank + nbMovedElements
								- tal.getNumberHavingValueLessThan(attRank)));
					}
				}
			}
		}
		int i = 0;
		int nbElementsBeforePosition = tal.getNumberHavingValueLessThan(position);
		for (TaskAttribute movedAttribute : attributes) {
			movedAttribute.setValue(String.valueOf(position - nbElementsBeforePosition + i++));
		}
	}

	/**
	 * Wrapper of a list of attributes.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class TaskAttributeList {
		/**
		 * The wrapped list of attributes.
		 */
		private final List<TaskAttribute> attributes;

		/**
		 * Constructor.
		 * 
		 * @param someAttributes
		 *            the wrapped list.
		 */
		private TaskAttributeList(final List<TaskAttribute> someAttributes) {
			this.attributes = someAttributes;
		}

		/**
		 * Computes the number of elements in the wrapped list that have a value less than the given value.
		 * 
		 * @param value
		 *            The value to compare with.
		 * @return the number of elements in the wrapped list that have a value less than the given value.
		 */
		public int getNumberHavingValueLessThan(int value) {
			int ret = 0;
			for (TaskAttribute att : attributes) {
				if (Integer.parseInt(att.getValue()) < value) {
					ret++;
				}
			}
			return ret;
		}

		/**
		 * Computes the number of elements in the wrapped list that have a value less than the given
		 * attribute's value.
		 * 
		 * @param attribute
		 *            The attribute to compare with.
		 * @return the number of elements in the wrapped list that have a value less than the given
		 *         attribute's value.
		 */
		@SuppressWarnings("unused")
		public int getNumberHavingValueLessThan(TaskAttribute attribute) {
			int attRank = Integer.parseInt(attribute.getValue());
			return getNumberHavingValueLessThan(attRank);
		}
	}

	/**
	 * Removes from the given list all the {@code TaskAttribute}s that are not direct children of the wrapped
	 * {@code TaskAttribute}.
	 * 
	 * @param attributes
	 *            The list of {@link TaskAttribute}s to filter, whose content is modified during execution.
	 */
	private void removeNonChildrenElementsFrom(final List<TaskAttribute> attributes) {
		List<TaskAttribute> uselessElements = new ArrayList<TaskAttribute>();
		for (TaskAttribute att : attributes) {
			if (att.getParentAttribute() != attribute) {
				uselessElements.add(att);
			}
		}
		attributes.removeAll(uselessElements);
	}

	/**
	 * Comparator of {@link TaskAttribute}s based on the integer value they contain.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class TaskAttributeComparator implements Comparator<TaskAttribute> {

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(TaskAttribute att1, TaskAttribute att2) {
			int v1 = Integer.parseInt(att1.getValue());
			int v2 = Integer.parseInt(att2.getValue());
			return v1 - v2;
		}

	}

	/**
	 * Indicates whether the wrapped attribute contains the actual given instance in its direct children.
	 * 
	 * @param anAttribute
	 *            The attribute to test.
	 * @return {@code true} if and only if the wrapped attribute contains the given attribute in its direct
	 *         (non-recursive) children attributes.
	 */
	public boolean containsInstance(final TaskAttribute anAttribute) {
		String id = anAttribute.getId();
		if (attribute.getAttributes().containsKey(id)) {
			return anAttribute == attribute.getAttribute(id);
		}
		return false;
	}

	/**
	 * Retrieve the suffix id from a given attribute's id.
	 * <p>
	 * <b>Precondition:</b> Ids must be "<some prefix>-" followed by an integer, and only the integer must
	 * vary.
	 * </p>
	 * 
	 * @return The id after the last "-" character in the given attribute's id.
	 */
	public String getTaskAttributeSuffixId() {
		String id = attribute.getId();
		id = id.substring(id.lastIndexOf('-') + 1);
		return id;
	}
}
