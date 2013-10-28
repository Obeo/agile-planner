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
import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.PositionConstants;

/**
 * Figure representing a column header.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FoldableColumnHeaderFigure extends ColumnHeaderFigure {

	/**
	 * The checkbox used to fold and unfold children figures.
	 */
	private final CheckBox foldCheckbox;

	/**
	 * Constructor.
	 */
	public FoldableColumnHeaderFigure() {
		super();
		// Add the checkbox that will fold/unfold cards in this cell
		foldCheckbox = new CheckBox();
		add(foldCheckbox);
		setConstraint(foldCheckbox, Integer.valueOf(PositionConstants.RIGHT));
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

}
