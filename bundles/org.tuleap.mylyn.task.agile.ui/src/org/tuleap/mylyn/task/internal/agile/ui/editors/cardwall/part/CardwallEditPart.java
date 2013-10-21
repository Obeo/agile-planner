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
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;

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

		GridLayout layout = new GridLayout();
		CardwallWrapper cardwall = (CardwallWrapper)getModel();

		layout.numColumns = 1 + cardwall.getColumns().size();
		layout.makeColumnsEqualWidth = false;

		// Spaces between columns and lines
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;

		// Spaces between head figures and the border of the cardwall
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		// The border of the cardwall
		// layer.setBorder(new LineBorder());

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
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<Object> getModelChildren() {
		List<Object> ret = new ArrayList<Object>();
		CardwallWrapper cardwall = (CardwallWrapper)getModel();
		ret.add(new String("Super Cardwall")); //$NON-NLS-1$
		for (ColumnWrapper col : cardwall.getColumns()) {
			ret.add(col);
		}
		for (SwimlaneWrapper item : cardwall.getSwimlanes()) {
			ret.add(item);
			for (ColumnWrapper col : cardwall.getColumns()) {
				List<Object> couple = new ArrayList<Object>();
				couple.add(item);
				couple.add(col);
				ret.add(couple);
			}
		}
		return ret;
	}
}
