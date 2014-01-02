/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Obeo - Refactoring and integration in the Mylyn Connector for Tuleap
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Toggle;
import org.eclipse.swt.graphics.Image;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;

/**
 * Collapse and Expand all figure.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CollapseExpandAllFigure extends Toggle {

	/**
	 * The state of the figure when its is unchecked.
	 */
	static final Image UNCHECKED = MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.COLLAPSE_ALL)
			.createImage();

	/**
	 * The state of the figure when its is checked.
	 */
	static final Image CHECKED = MylynAgileUIActivator.getImageDescriptor(IMylynAgileIcons.EXPAND_ALL)
			.createImage();

	/**
	 * The figure label.
	 */
	private Label label;

	/**
	 * Constructs a CheckBox with no text.
	 * 
	 * @since 2.0
	 */
	public CollapseExpandAllFigure() {
		this(""); //$NON-NLS-1$
	}

	/**
	 * Constructs a CheckBox with the passed text in its label.
	 * 
	 * @param text
	 *            The label text
	 * @since 2.0
	 */
	public CollapseExpandAllFigure(String text) {
		label = new Label(text, UNCHECKED);
		setContents(label);
	}

	/**
	 * Adjusts CheckBox's icon depending on selection status.
	 * 
	 * @since 2.0
	 */
	protected void handleSelectionChanged() {
		if (isSelected()) {
			label.setIcon(CHECKED);
		} else {
			label.setIcon(UNCHECKED);
		}
	}

	/**
	 * Initializes this Clickable by setting a default model and adding a clickable event handler for that
	 * model. Also adds a ChangeListener to update icon when selection status changes.
	 * 
	 * @since 2.0
	 */
	@Override
	protected void init() {
		super.init();
		addChangeListener(new ChangeListener() {
			@Override
			public void handleStateChanged(ChangeEvent changeEvent) {
				if (changeEvent.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY)) {
					handleSelectionChanged();
				}
			}
		});
	}

}
