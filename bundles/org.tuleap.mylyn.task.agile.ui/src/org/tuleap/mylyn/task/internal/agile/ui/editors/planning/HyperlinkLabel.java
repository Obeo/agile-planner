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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * The label hyper link class.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
class HyperlinkLabel extends Hyperlink {

	/**
	 * The HypelinkLabel constructor.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public HyperlinkLabel(Composite parent, int style) {
		super(parent, style);
		this.setUnderlined(true);
		this.setForeground(ColorConstants.blue);
	}

	/**
	 * The method painting text.
	 * 
	 * @param gc
	 *            the GC
	 * @param bounds
	 *            the bounds
	 */
	@Override
	public void paintText(GC gc, Rectangle bounds) {
		super.paintText(gc, bounds);
	}
}
