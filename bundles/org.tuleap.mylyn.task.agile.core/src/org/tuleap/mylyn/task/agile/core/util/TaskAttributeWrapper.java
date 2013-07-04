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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
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
		Assert.isNotNull(anAttribute);
		this.attribute = anAttribute;
	}

	/**
	 * Counts the number of children attributes of the given type. If the type is {@code null}, only children
	 * with a type {@code null} are counted.
	 * 
	 * @param type
	 *            The type to filter children attributes.
	 * @return Returns the number of child attributes of the given type, or the number of children without
	 *         type if the given type is {@code null}.
	 */
	public int countChildren(String type) {
		// now, type is not null
		int ret = 0;
		for (TaskAttribute child : attribute.getAttributes().values()) {
			if (type == null && child.getMetaData().getType() == null || type != null
					&& type.equals(child.getMetaData().getType())) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * Moves children elements of the wrapped attribute to a given position, restricting the reordering to
	 * attributes of the given type. The method assumes that the rank of the subattributes is materialized by
	 * their value.
	 * 
	 * @param attributes
	 *            The set of attributes to move. Attributes must be direct children of the wrapped attribute,
	 *            and they must match the given {@code type}.
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
	 *            If the given type is {@code null}, only attributes with a null type will be treated.
	 *            </p>
	 *            <p>
	 *            <b>Warning</b> Attributes that are not of this type will be silently ignored.
	 *            </p>
	 */
	public void moveElementsSortedByValue(final List<TaskAttribute> attributes, final int position,
			final String type) {
		List<TaskAttribute> attributesToMove = removeNonChildrenElementsFrom(attributes, type);
		// We sort the moved elements by their value to keep their order after the move operation
		Collections.sort(attributesToMove, new TaskAttributeComparator());
		TaskAttributeList tal = new TaskAttributeList(attributesToMove);
		int nbMovedElements = attributesToMove.size();
		for (TaskAttribute child : attribute.getAttributes().values()) {
			String actualType = child.getMetaData().getType();
			if (type == null && actualType == null || type != null && type.equals(actualType)) {
				if (!attributesToMove.contains(child)) {
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
		for (TaskAttribute movedAttribute : attributesToMove) {
			// No need to check the type, the list has been filtered already
			movedAttribute.setValue(String.valueOf(position - nbElementsBeforePosition + i++));
		}
	}

	/**
	 * Inserts the given attributes in the wrapped attribute at the given position among its children of the
	 * given type. The method assumes that the rank of the subattributes is materialized by their value.
	 * 
	 * @param attributes
	 *            The list af attributes to insert.
	 * @param position
	 *            The insertion index for these elements.
	 * @param type
	 *            The type of elements to manipulate. Any element with a different type will be silently
	 *            ignored.
	 */
	public void insertElementsSortedByValue(final List<TaskAttribute> attributes, final int position,
			final String type) {
		List<TaskAttribute> attributesToInsert = removeChildrenElementsFrom(attributes, type);
		int nbAttributesToInsert = attributesToInsert.size();
		// We sort the moved elements by their value to keep their order after the move operation
		Collections.sort(attributesToInsert, new TaskAttributeComparator());
		// First, update the values of inserted elements to insert them in the right place,
		// since the insertion location is materialized by the value, used as an index.
		int i = 0;
		for (TaskAttribute att : attribute.getAttributes().values()) {
			String actualType = att.getMetaData().getType();
			if (type == null && actualType == null || type != null && type.equals(actualType)) {
				int index = Integer.parseInt(att.getValue());
				if (index >= position) {
					att.setValue(String.valueOf(index + nbAttributesToInsert));
				}
			}
		}
		for (TaskAttribute movedAttribute : attributesToInsert) {
			// No need to check the type, the list has been filtered already
			movedAttribute.setValue(String.valueOf(position + i++));
			if (attribute.getAttributes().containsKey(movedAttribute.getId())) {
				// TODO Create a new id... is it really necessary?
				// N.B. backlogItems should all be created in a backlog (with a unique id there)
				// then moved to a given milestone.
				// Maybe the data model should be changed so that backlog items stay in the backlog
				// and have an additional sub-attribute indicating their milestone, if any...
				// But then, we would need also to manage the rank in this milestone.
			} else {
				attribute.deepAddCopy(movedAttribute);
			}
		}
	}

	/**
	 * Removes the given element from the wrapped attribute.
	 * 
	 * @param attributesToRemove
	 *            Attributes to remove from the wrapped attribute.
	 * @param type
	 *            The type of removed attributes. Any attribute not of this type will be silently ignored.
	 * @return Returns the number of attributes actually removed from the wrapped attribute.
	 */
	public int removeElementsSortedByValue(Iterator<TaskAttribute> attributesToRemove, String type) {
		int nbAttributesRemoved = 0;
		// remove the dragged element(s) from the source
		while (attributesToRemove.hasNext()) {
			TaskAttribute itemAtt = attributesToRemove.next();
			if (itemAtt.getParentAttribute() == attribute) {
				String itemType = itemAtt.getMetaData().getType();
				if (type == null && itemType == null || type != null && type.equals(itemType)) {
					attribute.removeAttribute(itemAtt.getId());
					nbAttributesRemoved++;
				}
			}
		}
		// Recompute the remaining elements indexes
		int index = 0;
		List<TaskAttribute> attributes = new ArrayList<TaskAttribute>();
		for (TaskAttribute att : attribute.getAttributes().values()) {
			String attType = att.getMetaData().getType();
			if (type == null && attType == null || type != null && type.equals(attType)) {
				attributes.add(att);
			}
		}
		Collections.sort(attributes, new TaskAttributeWrapper.TaskAttributeComparator());
		for (TaskAttribute att : attributes) {
			att.setValue(String.valueOf(index++));
		}
		return nbAttributesRemoved;
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
		public int getNumberHavingValueLessThan(TaskAttribute attribute) {
			int attRank = Integer.parseInt(attribute.getValue());
			return getNumberHavingValueLessThan(attRank);
		}
	}

	/**
	 * Removes from the given list all the {@code TaskAttribute}s that are not direct children of the wrapped
	 * {@code TaskAttribute} and are not of the right type.
	 * 
	 * @param attributes
	 *            The list of {@link TaskAttribute}s to filter, whose content is modified during execution.
	 * @param type
	 *            The type of elements to keep, all elements not of this type will be removed from the list.
	 * @return Returns a new mutable list containing the relevant elements in the same order as in the input
	 *         list.
	 */
	private List<TaskAttribute> removeNonChildrenElementsFrom(final List<TaskAttribute> attributes,
			final String type) {
		List<TaskAttribute> ret = new ArrayList<TaskAttribute>();
		for (TaskAttribute att : attributes) {
			if (att.getParentAttribute() == attribute) {
				String actualType = att.getMetaData().getType();
				if (type == null && actualType == null || type != null && type.equals(actualType)) {
					ret.add(att);
				}
			}
		}
		return ret;
	}

	/**
	 * Removes from the given list all the {@code TaskAttribute}s that are direct children of the wrapped
	 * {@code TaskAttribute} and are not of the right type.
	 * 
	 * @param attributes
	 *            The list of {@link TaskAttribute}s to filter, whose content is modified during execution.
	 * @param type
	 *            The type of elements to keep, all elements not of this type will be removed from the list.
	 * @return Returns a new mutable list containing the relevant elements in the same order as in the input
	 *         list.
	 */
	private List<TaskAttribute> removeChildrenElementsFrom(final List<TaskAttribute> attributes,
			final String type) {
		List<TaskAttribute> ret = new ArrayList<TaskAttribute>();
		for (TaskAttribute att : attributes) {
			if (att.getParentAttribute() != attribute) {
				String actualType = att.getMetaData().getType();
				if (type == null && actualType == null || type != null && type.equals(actualType)) {
					ret.add(att);
				}
			}
		}
		return ret;
	}

	/**
	 * Comparator of {@link TaskAttribute}s based on the integer value they contain.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class TaskAttributeComparator implements Comparator<TaskAttribute>, Serializable {

		/**
		 * For serialization.
		 */
		private static final long serialVersionUID = 1L;

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
