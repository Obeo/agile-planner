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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneCell;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;

/**
 * The edit part factory for the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		if (model instanceof CardwallModel) {
			part = new CardwallEditPart();
		} else if (model instanceof HeaderModel) {
			part = new HeaderEditPart();
		} else if (model instanceof CardWrapper) {
			part = new CardEditPart();
		} else if (model instanceof CardWrapper) {
			part = new CardEditPart();
		} else if (model instanceof SwimlaneCell) {
			part = new CellEditPart();
		} else if (model instanceof SwimlaneModel) {
			part = new SwimlaneEditPart();
		} else if (model instanceof SwimlaneWrapper) {
			part = new SwimlaneHeaderEditPart();
		} else if (model instanceof TaskAttribute) {
			part = new CardFieldEditPart();
		} else {
			part = new ColumnHeaderEditPart();
		}
		part.setModel(model);
		return part;
	}

}
