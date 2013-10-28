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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Figure representing a backlog item card in the card wall's left column.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemCardFigure extends CardFigure {

	/**
	 * The key to register the card's background color with the plugin.
	 */
	public static final String BI_CARD_BG_COLOR_KEY = "BI_CARD_BG_COLOR"; //$NON-NLS-1$

	/**
	 * Provides the default background color for backlog item cards.
	 * 
	 * @return The default bg color for backlog item cards.
	 */
	@Override
	public Color getDefaultBackgroundColor() {
		MylynAgileUIActivator activator = MylynAgileUIActivator.getDefault();
		if (activator.hasColor(BI_CARD_BG_COLOR_KEY)) {
			return activator.getColor(BI_CARD_BG_COLOR_KEY);
		}
		Color c = new Color(Display.getCurrent(), IMylynAgileUIConstants.BI_CARD_BG_COLOR);
		activator.putColor(BI_CARD_BG_COLOR_KEY, c);
		return c;
	}
}
