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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileIcons;

/**
 * Listener to change the display of the cursor when this one is on the URL label.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DetailsMouseMotionListener implements MouseMotionListener {

	/**
	 * The foldable panel.
	 */
	private final CardDetailsPanel panel;

	/**
	 * Constructor.
	 * 
	 * @param panel
	 *            The foldable panel.
	 */
	public DetailsMouseMotionListener(CardDetailsPanel panel) {
		this.panel = panel;
	}

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
		panel.getTitleLabel().setForegroundColor(ColorConstants.black);
		panel.getTitleLabel().setCursor(Cursors.HAND);
		panel.setIcons(MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_OPEN_HOVER),
				MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_CLOSED_HOVER));
		panel.repaint();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent me) {
		panel.getTitleLabel().setForegroundColor(ColorConstants.gray);
		// FIXME: Retrieve the "system" cursor.
		panel.getTitleLabel().setCursor(Cursors.ARROW);
		panel.setIcons(MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_OPEN),
				MylynAgileUIActivator.getDefault().getImage(IMylynAgileIcons.DETAILS_CLOSED));
		panel.repaint();
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
