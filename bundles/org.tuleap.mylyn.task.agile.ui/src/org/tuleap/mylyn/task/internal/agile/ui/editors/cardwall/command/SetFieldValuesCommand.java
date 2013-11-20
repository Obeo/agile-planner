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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.command;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;

/**
 * Command to set the value of a configurable field.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SetFieldValuesCommand extends Command {

	/**
	 * The card wrapper, to use for editing the TaskAttribute.
	 */
	private final CardWrapper wrapper;

	/**
	 * The {@link TaskAttribute} being edited.
	 */
	private final TaskAttribute attribute;

	/**
	 * The new value.
	 */
	private final List<String> newValues;

	/**
	 * The old value.
	 */
	private final ImmutableList<String> oldValues;

	/**
	 * Constructor.
	 * 
	 * @param wrapper
	 *            The card wrapper.
	 * @param attribute
	 *            ID of the attribute being edited.
	 * @param newValues
	 *            The values to set.
	 */
	public SetFieldValuesCommand(CardWrapper wrapper, TaskAttribute attribute, List<String> newValues) {
		this.wrapper = wrapper;
		this.attribute = attribute;
		this.oldValues = ImmutableList.copyOf(attribute.getValues());
		this.newValues = newValues;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		wrapper.setFieldValues(wrapper.getFieldId(attribute), newValues);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		wrapper.setFieldValues(wrapper.getFieldId(attribute), oldValues);
	}
}
