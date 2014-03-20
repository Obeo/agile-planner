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
package org.tuleap.mylyn.task.internal.agile.ui.editors.planning;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.custom.StyleRange;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Label provider for closed backlog items. Will display label like in the task list.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
class BacklogItemLabelProvider extends StyledCellLabelProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		String sValue;
		boolean closed = false;
		int cellIndex = cell.getColumnIndex();
		boolean hyperlink = cellIndex == 0 || cellIndex == 4;
		Object element = cell.getElement();
		if (element == null || !(element instanceof BacklogItemWrapper)) {
			sValue = MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingTextValue"); //$NON-NLS-1$
		} else {
			BacklogItemWrapper wrapper = (BacklogItemWrapper)element;
			sValue = getLabel(wrapper, cell);
			// TODO Replace with API on BIW isClosed()
			if ("Closed".equalsIgnoreCase(wrapper.getStatus())) { //$NON-NLS-1$
				closed = true;
			}
		}
		cell.setText(sValue);
		if (sValue != null) {
			StyleRange range = new StyleRange(0, sValue.length(), cell.getForeground(), cell.getBackground());
			if (closed) {
				if (hyperlink) {
					range.foreground = ColorConstants.lightBlue;
				} else {
					range.foreground = ColorConstants.gray;
				}
			} else if (hyperlink) {
				range.foreground = ColorConstants.blue;
			}
			range.strikeout = closed;
			range.underline = hyperlink;
			cell.setStyleRanges(new StyleRange[] {range });
		}
		super.update(cell);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.Object)
	 */
	@Override
	public String getToolTipText(Object element) {
		if (element != null && element instanceof BacklogItemWrapper) {
			return ((BacklogItemWrapper)element).getLabel();
		}
		return null;
	}

	/**
	 * Method that provides the label used by this label provider for the given cell.
	 *
	 * @param item
	 *            The item to use, never null.
	 * @param cell
	 *            The cell.
	 * @return The label to use for this item, can be null.
	 */
	protected String getLabel(BacklogItemWrapper item, ViewerCell cell) {
		String res;
		switch (cell.getColumnIndex()) {
			case 0: // ID
				res = item.getDisplayId();
				break;
			case 1: // Type
				res = item.getType();
				break;
			case 2: // Label
				res = item.getLabel();
				break;
			case 3: // Points
				res = item.getInitialEffort();
				break;
			case 4: // Parent
				res = item.getParentDisplayId();
				break;
			default: // ?
				res = ""; //$NON-NLS-1$
				break;
		}
		return res;
	}
}
