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

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

/**
 * A configurable field composed of a label and a value. Each configurable field is identified by an id.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ConfigurableFieldFigure extends Panel {

	/**
	 * The id.
	 */
	private String id;

	/**
	 * The figure which is about label.
	 */
	private TextFlow labelFlow;

	/**
	 * The figure which is about value.
	 */
	private TextFlow valueFlow;

	/**
	 * Constructor.
	 * 
	 * @param font
	 *            the font to use.
	 * @param id
	 *            The id of the field.
	 * @param label
	 *            The label to use.
	 */
	public ConfigurableFieldFigure(Font font, String id, String label) {
		this.id = id;

		setLayoutManager(new GridLayout(1, false));
		FlowPage flowPage = new FlowPage();
		flowPage.setOpaque(true);
		flowPage.setFont(font);

		BlockFlow blockFlow = new BlockFlow();

		labelFlow = new TextFlow();
		labelFlow.setText(label + ": "); //$NON-NLS-1$
		blockFlow.add(labelFlow);

		valueFlow = new TextFlow();
		blockFlow.add(valueFlow);

		flowPage.add(blockFlow);

		add(flowPage);
		setConstraint(flowPage, new GridData(SWT.LEFT, SWT.TOP, true, true));
	}

	/**
	 * Get the figure which is about the label.
	 * 
	 * @return The label figure.
	 */
	public TextFlow getLabel() {
		return labelFlow;
	}

	/**
	 * Get the figure which is about the value.
	 * 
	 * @return The value figure.
	 */
	public TextFlow getValue() {
		return valueFlow;
	}

	/**
	 * Get the id.
	 * 
	 * @return The id.
	 */
	public String getId() {
		return id;
	}

}
