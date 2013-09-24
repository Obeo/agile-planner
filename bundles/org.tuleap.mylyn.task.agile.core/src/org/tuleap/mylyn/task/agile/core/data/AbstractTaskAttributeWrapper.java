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

import org.eclipse.core.runtime.Assert;
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
	 * Suffix appended to the ids of Task Attributes representing IDs.
	 */
	public static final String SUFFIX_ID = "id"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing labels.
	 */
	public static final String SUFFIX_LABEL = "lbl"; //$NON-NLS-1$

	/**
	 * Default value used for IDs not set.
	 */
	protected static final int UNSET_ID = -1;

	/**
	 * The wrapped task attribute.
	 */
	protected final TaskAttribute root;

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 */
	public AbstractTaskAttributeWrapper(TaskAttribute root) {
		Assert.isNotNull(root);
		this.root = root;
	}

	/**
	 * Constructor that receives the taskAttribute to wrap.
	 * 
	 * @param root
	 *            The task attribute to wrap
	 * @param id
	 *            The functional ID of the wrapped element.
	 */
	public AbstractTaskAttributeWrapper(TaskAttribute root, int id) {
		this(root);
		TaskAttribute attribute = root.getMappedAttribute(getIdAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getIdAttributeId());
			attribute.getMetaData().setReadOnly(true);
			attribute.getMetaData().setType(TaskAttribute.TYPE_INTEGER);
		}
		attribute.setValue(Integer.toString(id));
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
		return getAttributeId(root, SUFFIX_ID);
	}

	/**
	 * Computes the unique id of the Label attribute.
	 * 
	 * @return The unique id of the label attribute.
	 */
	protected String getLabelAttributeId() {
		return getAttributeId(root, SUFFIX_LABEL);
	}

	/**
	 * Id getter.
	 * 
	 * @return The item's id.
	 */
	public int getId() {
		TaskAttribute attribute = root.getMappedAttribute(getIdAttributeId());
		if (attribute != null) {
			return Integer.parseInt(attribute.getValue());
		}
		return UNSET_ID;
	}

	/**
	 * Label getter.
	 * 
	 * @return The item's label, or {@code null} if not defined.
	 */
	public String getLabel() {
		String result = null;
		TaskAttribute attribute = root.getMappedAttribute(getLabelAttributeId());
		if (attribute != null) {
			result = attribute.getValue();
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
		TaskAttribute attribute = root.getMappedAttribute(getLabelAttributeId());
		if (attribute == null) {
			attribute = root.createMappedAttribute(getLabelAttributeId());
			attribute.getMetaData().setKind(TaskAttribute.KIND_DEFAULT);
			attribute.getMetaData().setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
		}
		String oldValue = attribute.getValue();
		if (!label.equals(oldValue)) {
			attribute.setValue(label);
			fireAttributeChanged(attribute);
		}
	}

	/**
	 * Provides the wrapped attribute.
	 * 
	 * @return The wrapped attribute, never null.
	 */
	public TaskAttribute getWrappedAttribute() {
		return root;
	}

	/**
	 * Informs the listeners of the given attribute's modification.
	 * 
	 * @param attribute
	 *            The modified attribute.
	 */
	protected abstract void fireAttributeChanged(TaskAttribute attribute);
}
