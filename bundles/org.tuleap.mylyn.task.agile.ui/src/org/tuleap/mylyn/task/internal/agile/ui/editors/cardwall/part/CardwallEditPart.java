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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.HeaderModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * The root edit part for the card wall.
 *
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallEditPart extends AbstractGraphicalEditPart {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();

		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(IMylynAgileUIConstants.MARGIN);
		layer.setLayoutManager(layout);

		return layer;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<Object> getModelChildren() {
		List<Object> ret = new ArrayList<Object>();
		CardwallModel cardwall = (CardwallModel)getModel();
		ret.add(new HeaderModel(cardwall));
		for (SwimlaneModel swimlane : cardwall.getSwimlanes()) {
			ret.add(swimlane);
		}
		return ret;
	}
}
