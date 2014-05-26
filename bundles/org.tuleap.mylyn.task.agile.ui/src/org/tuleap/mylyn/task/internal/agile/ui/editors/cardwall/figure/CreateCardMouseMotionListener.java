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

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;

/**
 * Listener to change the display of the cursor when this one is on the card creation icon.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CreateCardMouseMotionListener implements MouseMotionListener {

	/**
	 * The card figure.
	 */
	private final Label label;

	/**
	 * Constructor.
	 *
	 * @param panel
	 *            The foldable panel.
	 */
	public CreateCardMouseMotionListener(Label panel) {
		this.label = panel;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		// Nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseEntered(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent me) {
		label.setCursor(Cursors.HAND);
		label.repaint();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent me) {
		label.setCursor(Cursors.ARROW);
		label.repaint();
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
