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

//import java.util.HashMap;
import java.util.List;

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
 * Edit policy for direct edit on Multi-selection fields.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardMultiSelectionFieldEditPolicy extends DirectEditPolicy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// We retrieve the values of the selected item

		// @SuppressWarnings("unchecked") added due to a List cast warning
		@SuppressWarnings("unchecked")
		List<String> values = (List<String>)request.getCellEditor().getValue();

		// for CellEditor, null is always returned for invalid values
		if (values == null) {
			return null;
		}

		CardFieldEditPart host = (CardFieldEditPart)getHost();
		TaskAttribute attribute = (TaskAttribute)host.getModel();

		CardEditPart cardPart = (CardEditPart)host.getParent();
		CardWrapper cardWrapper = ((CardModel)cardPart.getModel()).getWrapper();

		if (values.isEmpty()) {
			values.add(""); //$NON-NLS-1$
		}

		return new SetFieldValuesCommand(cardWrapper, attribute, values);
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
