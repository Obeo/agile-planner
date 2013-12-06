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

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * Abstract super-class of TaskAttribute wrappers that simplify the mylyn model manipulation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTaskAttributeWrapper {

	/**
	 * Separator used in computed ids.
	 */
	public static final char ID_SEPARATOR = '-';

	/**
	 * Suffix appended to the ids of Task Attributes representing internal IDs.
	 */
	public static final String SUFFIX_ID = "id"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing IDs to display to an end-user.
	 */
	public static final String SUFFIX_DISPLAY_ID = "display_id"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing labels.
	 */
	public static final String SUFFIX_LABEL = "lbl"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a BacklogItem parent identifier.
	 */
	public static final String SUFFIX_BI_PARENT_ID = "parent_id"; //$NON-NLS-1$

	/**
	 * The value used to indicate that a task data represents a BacklogItem parent display identifier.
	 */
	public static final String SUFFIX_BI_PARENT_DISPLAY_ID = "parent_display_id"; //$NON-NLS-1$

	/**
	 * Default value used for IDs not set.
	 */
	protected static final String UNSET_ID = ""; //$NON-NLS-1$

	/**
	 * The root task attribute.
	 */
	protected final TaskAttribute root;

	/**
	 * The id.
	 */
	protected final TaskAttribute attribute;

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The root task attribute.
	 * @param prefix
	 *            The prefix to use
	 * @param id
	 *            The task attribute id of the wrapped element
	 */
	public AbstractTaskAttributeWrapper(TaskAttribute root, String prefix, String id) {
		this.root = root;
		TaskAttribute att = root.getMappedAttribute(prefix + id);
		if (att == null) {
			att = root.createMappedAttribute(prefix + id);
			att.getMetaData().setReadOnly(true);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
			att.setValue(id);
		}
		attribute = att;
	}

	/**
	 * Computes the technical ID (used by mylyn) of a TaskAttribute given its parent and a suffix to use.
	 * 
	 * @param parent
	 *            The parent task attribute
	 * @param suffix
	 *            The suffix
	 * @return The technical ID, should be unique inside this TaskData.
	 */
	protected String getAttributeId(TaskAttribute parent, String suffix) {
		return parent.getId() + ID_SEPARATOR + suffix;
	}

	/**
	 * Computes the unique id of the ID attribute.
	 * 
	 * @return The unique id of the id attribute.
	 */
	protected String getIdAttributeId() {
		return getAttributeId(attribute, SUFFIX_ID);
	}

	/**
	 * Computes the unique id of the ID attribute.
	 * 
	 * @return The unique id of the id attribute.
	 */
	protected String getDisplayIdAttributeId() {
		return getAttributeId(attribute, SUFFIX_DISPLAY_ID);
	}

	/**
	 * Computes the unique id of the Label attribute.
	 * 
	 * @return The unique id of the label attribute.
	 */
	protected String getLabelAttributeId() {
		return getAttributeId(attribute, SUFFIX_LABEL);
	}

	/**
	 * Id getter.
	 * 
	 * @return The item's id.
	 */
	public String getId() {
		return attribute.getValue();
	}

	/**
	 * Root {@link TaskAttribute} getter.
	 * 
	 * @return The root {@link TaskAttribute}.
	 */
	public TaskAttribute getRoot() {
		return root;
	}

	/**
	 * Display ID setter.
	 * 
	 * @param displayId
	 *            The item's display id.
	 */
	public void setDisplayId(String displayId) {
		TaskAttribute att = root.getMappedAttribute(getDisplayIdAttributeId());
		if (att == null) {
			att = root.createMappedAttribute(getDisplayIdAttributeId());
		}
		att.setValue(displayId);
	}

	/**
	 * Display ID getter.
	 * 
	 * @return The item's display id, or the item's id if the display id is not set.
	 */
	public String getDisplayId() {
		TaskAttribute att = root.getMappedAttribute(getDisplayIdAttributeId());
		if (att != null) {
			return att.getValue();
		}
		return getId();
	}

	/**
	 * Label getter.
	 * 
	 * @return The item's label, or {@code null} if not defined.
	 */
	public String getLabel() {
		String result = null;
		TaskAttribute att = root.getMappedAttribute(getLabelAttributeId());
		if (att != null) {
			result = att.getValue();
		}
		return result;
	}

	/**
	 * Label setter.
	 * 
	 * @param label
	 *            The item's label. If it is null, nothing happens and the former label, if present, remains
	 *            unchanged.
	 */
	public void setLabel(String label) {
		if (label == null) {
			return;
		}
		TaskAttribute att = root.getMappedAttribute(getLabelAttributeId());
		if (att == null) {
			att = root.createMappedAttribute(getLabelAttributeId());
			att.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			att.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		}
		String oldValue = att.getValue();
		if (!label.equals(oldValue)) {
			att.setValue(label);
			fireAttributeChanged(att);
		}
	}

	/**
	 * Provides the wrapped attribute.
	 * 
	 * @return The wrapped attribute, never null.
	 */
	public TaskAttribute getWrappedAttribute() {
		return attribute;
	}

	/**
	 * Informs the listeners of the given attribute's modification.
	 * 
	 * @param att
	 *            The modified attribute.
	 */
	protected abstract void fireAttributeChanged(TaskAttribute att);
}
