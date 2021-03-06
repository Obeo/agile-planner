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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.text.TextFlow;

/**
 * Listener to change the display of the cursor when this one is on the artifact links.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class ArtifactLinksMouseMotionListener implements MouseMotionListener {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		// nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseEntered(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent me) {
		Object source = me.getSource();
		if (source instanceof IFigure) {
			IFigure figure = (TextFlow)source;
			figure.setForegroundColor(ColorConstants.gray);
			figure.setCursor(Cursors.HAND);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent me) {
		Object source = me.getSource();
		if (source instanceof IFigure) {
			IFigure figure = (TextFlow)source;
			figure.setForegroundColor(ColorConstants.black);
			figure.setCursor(Cursors.ARROW);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseHover(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseHover(MouseEvent me) {
		// nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		// nothing
	}

}
