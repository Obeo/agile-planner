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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;

/**
 * The edit part factory for the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		if (model instanceof CardwallWrapper) {
			part = new CardwallEditPart();
		} else if (model instanceof CardWrapper) {
			part = new CardEditPart();
		} else if (model instanceof List) {
			part = new CellContentEditPart();
		} else {
			part = new CellHeadEditPart();
		}
		part.setModel(model);
		return part;
	}

}
