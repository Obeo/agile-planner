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

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The backlogItem parent label provider.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public final class ParentLabelProvider extends StyledCellLabelProvider {

	/**
	 * The hyperlink.
	 */
	HyperlinkLabel control;

	/**
	 * The missing label message.
	 */
	final String strMissing = MylynAgileUIMessages.getString("PlanningTaskEditorPart.ParentHeader"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	ParentLabelProvider(Composite parent) {
		control = new HyperlinkLabel(parent, SWT.WRAP);
	}

	@Override
	protected void paint(Event event, Object element) {
		String sValue;
		if (element == null) {
			sValue = strMissing;
		} else if (element instanceof BacklogItemWrapper) {
			sValue = ((BacklogItemWrapper)element).getParentDisplayId();
		} else {
			sValue = element.toString();
		}

		control.setText(sValue);

		GC gc = event.gc;
		Rectangle cellRect = new Rectangle(event.x, event.y, event.width, event.height);
		final int width = 4000;
		cellRect.width = width;
		control.paintText(gc, cellRect);
	}
}
