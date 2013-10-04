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

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
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
	 * Preferred width for an empty cell.
	 */
	private static final int EMPTY_CELL_PREFERRED_WIDTH = 160;

	/**
	 * Preferred height for an empty cell.
	 */
	private static final int EMPTY_CELL_PREFERRED_HEIGHT = 160;

	/**
	 * Index in the input list to retrieve the swimlane related to the current cell.
	 */
	private static final int INDEX_SWIMLANE = 0;

	/**
	 * Index in the input list to retrieve the column related to the current cell.
	 */
	private static final int INDEX_COLUMN = 1;

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
		final ColumnWrapper column = getColumn();
		return Lists.newArrayList(Iterables.filter(getCards(), new Predicate<CardWrapper>() {

			@Override
			public boolean apply(CardWrapper input) {
				return column.getId() != null && column.getId().equals(input.getStatusId());
			}

		}));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return getCellFigure().getArtifactContainer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		if (getCards().isEmpty()) {
			getFigure().setPreferredSize(
					new Dimension(EMPTY_CELL_PREFERRED_WIDTH, EMPTY_CELL_PREFERRED_HEIGHT));
		}
		super.refreshVisuals();
	}

	/**
	 * Get the cards of the whole swimlane related to the current cell.
	 * 
	 * @return The list of card wrappers.
	 */
	private List<CardWrapper> getCards() {
		SwimlaneWrapper item = getSwimlane();
		if (item != null) {
			return item.getCards();
		}
		return Lists.newArrayList();
	}

	/**
	 * Get the swimlane related to the current cell.
	 * 
	 * @return The swimlane.
	 */
	private SwimlaneWrapper getSwimlane() {
		Object mdl = getModel();
		if (mdl instanceof List) {
			List<Object> model = (List<Object>)mdl;
			return (SwimlaneWrapper)model.get(INDEX_SWIMLANE);
		}
		return null;
	}

	/**
	 * Get the column related to the current cell.
	 * 
	 * @return The column.
	 */
	private ColumnWrapper getColumn() {
		Object mdl = getModel();
		if (mdl instanceof List) {
			List<Object> model = (List<Object>)mdl;
			return (ColumnWrapper)model.get(INDEX_COLUMN);
		}
		return null;
	}

}
