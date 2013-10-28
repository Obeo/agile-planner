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

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.SwimlaneHeaderFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

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

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = IMylynAgileUIConstants.MARGIN;
		layout.verticalSpacing = 0;
		SwimlaneModel swimlane = (SwimlaneModel)getModel();
		layout.numColumns = swimlane.getCardwall().getColumns().size() + 1;
		layout.makeColumnsEqualWidth = true;
		panel.setLayoutManager(layout);

		return panel;
	}

	/**
	 * Returns the figure as a {@link SwimlaneHeaderFigure}.
	 * 
	 * @return the figure as a {@link SwimlaneHeaderFigure}.
	 */
	public SwimlaneHeaderFigure getHeaderFigure() {
		return (SwimlaneHeaderFigure)getFigure();
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
