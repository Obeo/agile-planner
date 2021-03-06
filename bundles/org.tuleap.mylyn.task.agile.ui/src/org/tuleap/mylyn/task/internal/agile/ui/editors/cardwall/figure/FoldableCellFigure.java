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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure;

import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Figure representing a cell containing cards in the card wall, that can be folded thanks to a checkbox in
 * the upper right corner.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FoldableCellFigure extends CellFigure {

	/**
	 * The checkbox used to fold and unfold children figures.
	 */
	private final CollapseExpandFigure foldCheckbox;

	/**
	 * Constructor.
	 */
	public FoldableCellFigure() {
		// Add the checkbox that will fold/unfold cards in this cell
		foldCheckbox = new CollapseExpandFigure();
		foldCheckbox.setToolTip(new Label(MylynAgileUIMessages.getString("FoldableCellFigure.FoldTooltip"))); //$NON-NLS-1$
		getHeaderPanel().add(foldCheckbox, 0);
		getHeaderPanel().setConstraint(foldCheckbox, new GridData(SWT.RIGHT, SWT.FILL, true, true));
	}

	/**
	 * Adds the given listener to the checkbox used for folding.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public void addFoldingListener(ChangeListener listener) {
		foldCheckbox.addChangeListener(listener);
	}

	/**
	 * Removes the given listener from the checkbox used for folding.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	public void removeFoldingListener(ChangeListener listener) {
		foldCheckbox.removeChangeListener(listener);
	}

	/**
	 * Getter of the folded status.
	 * 
	 * @return <code>true</code> if and only if the fold checkbox is selected.
	 */
	public boolean isFolded() {
		return foldCheckbox.isSelected();
	}

	/**
	 * Sets the folded status.
	 * 
	 * @param folded
	 *            the folded status.
	 */
	public void setFolded(boolean folded) {
		foldCheckbox.setSelected(folded);
	}
}
