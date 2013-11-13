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
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;

/**
 * Figure to display like a URL<br>
 * It is composed of of flow figure, is displayed in gray color. Its color changes to black when the mouse
 * enters on it and the mouse becomes a hand.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class URLFigure extends FlowPage {

	/**
	 * Listener to change the display of the cursor when this one is on the URL label.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class URLMouseMotionListener implements MouseMotionListener {

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
			IFigure target = null;
			if (source instanceof IFigure) {
				if (((IFigure)source).getParent() != null) {
					target = ((IFigure)source).getParent();
				} else {
					target = ((IFigure)source).getParent();
				}
			}
			if (target != null) {
				target.setForegroundColor(ColorConstants.black);
			}
			setCursor(Cursors.HAND);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent me) {
			Object source = me.getSource();
			IFigure target = null;
			if (source instanceof IFigure) {
				if (((IFigure)source).getParent() != null) {
					target = ((IFigure)source).getParent();
				} else {
					target = ((IFigure)source).getParent();
				}
			}
			if (target != null) {
				target.setForegroundColor(ColorConstants.gray);
			}
			// FIXME: Retrieve the "system" cursor.
			setCursor(Cursors.ARROW);
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

	/**
	 * The figure which is about the label to display for the URL.
	 */
	protected FlowFigure textFigure;

	/**
	 * Constructor.
	 * 
	 * @param textFigure
	 *            The figure which is about the label to display.
	 */
	public URLFigure(FlowFigure textFigure) {
		this.textFigure = textFigure;
		add(textFigure);
		setForegroundColor(ColorConstants.gray);
		setOpaque(true);
		textFigure.addMouseMotionListener(new URLMouseMotionListener());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#addMouseListener(org.eclipse.draw2d.MouseListener)
	 */
	@Override
	public void addMouseListener(MouseListener listener) {
		textFigure.addMouseListener(listener);
	}

}
