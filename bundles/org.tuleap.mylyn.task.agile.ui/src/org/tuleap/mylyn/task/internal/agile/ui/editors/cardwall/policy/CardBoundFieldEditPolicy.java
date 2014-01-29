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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy;

import com.google.common.collect.Iterables;

import java.util.Collections;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.command.SetFieldValuesCommand;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardFieldEditPart;

/**
 * Edit policy for direct edit on String fields.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardBoundFieldEditPolicy extends DirectEditPolicy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// We retrieve the index of the selected item
		Integer valueIndex = (Integer)request.getCellEditor().getValue();

		// for CellEditor, null is always returned for invalid values
		if (valueIndex == null) {
			return null;
		}

		CardFieldEditPart host = (CardFieldEditPart)getHost();
		TaskAttribute attribute = (TaskAttribute)host.getModel();
		// Now we transform the index into the actual value to set, which is the key of the index-th entry in
		// the attribute options
		String key = Iterables.get(attribute.getOptions().entrySet(), valueIndex.intValue()).getKey();

		CardEditPart cardPart = (CardEditPart)host.getParent();
		CardWrapper cardWrapper = ((CardModel)cardPart.getModel()).getWrapper();

		return new SetFieldValuesCommand(cardWrapper, attribute, Collections.singletonList(key));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		// Nothing to do here, we don't want to change the background value during edition
	}

}
