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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.layout.SwimlaneLayout;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;

/**
 * The edit part for a swimlane.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneEditPart extends AbstractGraphicalEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Panel panel = new Panel();
		panel.setMinimumSize(new Dimension(100, 10));

		SwimlaneModel swimlane = (SwimlaneModel)getModel();
		int nbColumns = swimlane.getCardwall().getColumns().size() + 1;
		SwimlaneLayout layout = new SwimlaneLayout(nbColumns);
		panel.setLayoutManager(layout);

		return panel;
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
	protected List<?> getModelChildren() {
		List<Object> res = new ArrayList<Object>();
		SwimlaneModel swimlane = (SwimlaneModel)getModel();
		res.add(swimlane.getWrapper());
		res.addAll(swimlane.getCells());
		return res;
	}
}
