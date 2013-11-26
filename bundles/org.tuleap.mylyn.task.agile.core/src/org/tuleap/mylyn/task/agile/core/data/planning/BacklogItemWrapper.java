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
package org.tuleap.mylyn.task.agile.core.data.planning;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.AbstractBacklogItemWrapper;

/**
 * Wrapper of a TaskAttribute that represents a Backlog Item.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemWrapper extends AbstractBacklogItemWrapper {

	/**
	 * The parent planning.
	 */
	private final MilestonePlanningWrapper parent;

	/**
	 * Constructor to use to wrap an existing task attribute that is not yet filled with sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 * @param id
	 *            The backlog item's functional id
	 */
	protected BacklogItemWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root, String id) {
		super(root, id);
		Assert.isNotNull(parent);
		this.parent = parent;
		parent.getBacklogTaskAttribute().addValue(root.getId());
	}

	/**
	 * Constructor to use to wrap an existing instance that already has sub-attributes.
	 * 
	 * @param parent
	 *            The parent planning
	 * @param root
	 *            The non-null task attribute that represents a backlog item to wrap.
	 */
	protected BacklogItemWrapper(final MilestonePlanningWrapper parent, final TaskAttribute root) {
		super(root);
		Assert.isNotNull(parent);
		this.parent = parent;
	}

	/**
	 * Return the parent planning as a wrapper.
	 * 
	 * @return The (never null) parent planning, as a wrapper.
	 */
	public MilestonePlanningWrapper getParent() {
		return parent;
	}

	/**
	 * Parent setter.
	 * 
	 * @param parentId
	 *            The parent identifier.
	 * @param parentDisplayId
	 *            The parent displayed identifier.
	 */
	public void setParent(String parentId, String parentDisplayId) {
		TaskAttribute idAttribute = root.getMappedAttribute(getParentIdSuffix());
		String oldIdValue = null;
		if (idAttribute == null) {
			idAttribute = root.createAttribute(getParentIdSuffix());
			idAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldIdValue = idAttribute.getValue();
		}
		if (oldIdValue == null || !oldIdValue.equals(parentId)) {
			idAttribute.setValue(parentId);
			fireAttributeChanged(idAttribute);
		}

		TaskAttribute displayedIdAttribute = root.getMappedAttribute(getParentDisplayIdSuffix());
		String oldDisplayedIdValue = null;
		if (displayedIdAttribute == null) {
			displayedIdAttribute = root.createAttribute(getParentDisplayIdSuffix());
			displayedIdAttribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		} else {
			oldDisplayedIdValue = displayedIdAttribute.getValue();
		}
		if (oldDisplayedIdValue == null || !oldDisplayedIdValue.equals(parentDisplayId)) {
			displayedIdAttribute.setValue(parentDisplayId);
			fireAttributeChanged(displayedIdAttribute);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper#fireAttributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void fireAttributeChanged(TaskAttribute attribute) {
		parent.fireAttributeChanged(attribute);
	}

	/**
	 * Computes the unique parent identifier attribute.
	 * 
	 * @return The unique parent identifier attribute.
	 */
	public String getParentIdSuffix() {
		return root.getId() + ID_SEPARATOR + SUFFIX_BI_PARENT_ID;
	}

	/**
	 * Computes the unique parent displayed identifier attribute.
	 * 
	 * @return The unique parent displayed identifier attribute.
	 */
	public String getParentDisplayIdSuffix() {
		return root.getId() + ID_SEPARATOR + SUFFIX_BI_PARENT_DISPLAY_ID;
	}

	/**
	 * Parent identifier getter.
	 * 
	 * @return The id of parent, or {@code null} if it has not a parent.
	 */
	public String getParentId() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getParentIdSuffix());
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}

	/**
	 * Parent displayed identifier getter.
	 * 
	 * @return The displayed parent id, or {@code null} if it has not a parent.
	 */
	public String getParentDisplayId() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getParentDisplayIdSuffix());
		if (attribute != null) {
			result = attribute.getValue();
		}
		return result;
	}
}
