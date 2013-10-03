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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CellContentFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CellContentEditPolicy;

/**
 * The edit part for the cells containing cards.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CellContentEditPart extends AbstractCellEditPart {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new CellContentFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.AbstractCellEditPart#getCellFigure()
	 */
	@Override
	public CellContentFigure getCellFigure() {
		return (CellContentFigure)getFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CellContentEditPolicy());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<CardWrapper> getModelChildren() {
		Object mdl = getModel();
		if (mdl instanceof List) {
			List<Object> model = (List<Object>)mdl;
			SwimlaneWrapper item = (SwimlaneWrapper)model.get(0);
			final ColumnWrapper col = (ColumnWrapper)model.get(1);
			return Lists.newArrayList(Iterables.filter(item.getCards(), new Predicate<CardWrapper>() {

				@Override
				public boolean apply(CardWrapper input) {
					return col.getId() != null && col.getId().equals(input.getStatusId());
				}

			}));
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		CellContentFigure figure = getCellFigure();
		return figure.getArtifactContainer();
	}

}
