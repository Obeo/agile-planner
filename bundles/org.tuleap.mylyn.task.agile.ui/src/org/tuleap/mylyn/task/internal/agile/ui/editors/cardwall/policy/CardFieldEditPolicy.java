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

import java.util.Collections;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.command.SetFieldValuesCommand;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFieldFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardFieldEditPart;

/**
 * Edit policy for direct edit on String fields.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldEditPolicy extends DirectEditPolicy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		String text = (String)request.getCellEditor().getValue();

		// for CellEditor, null is always returned for invalid values
		if (text == null) {
			return null;
		}

		CardFieldEditPart compartment = (CardFieldEditPart)getHost();
		TaskAttribute attribute = (TaskAttribute)compartment.getModel();

		CardEditPart cardPart = (CardEditPart)compartment.getParent();
		CardWrapper cardWrapper = (CardWrapper)cardPart.getModel();

		return new SetFieldValuesCommand(cardWrapper, attribute, Collections.singletonList(text));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String)request.getCellEditor().getValue();
		((CardFieldFigure)getHostFigure()).setValues(Collections.singletonList(value));
	}

}
