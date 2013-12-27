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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Figure to represent some kind of progress, like a progress monitor.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ProgressFigure extends Panel {

	/**
	 * The total amount.
	 */
	private int total;

	/**
	 * The current amount.
	 */
	private int progress;

	/**
	 * The rectangle that displays the progress.
	 */
	private final RectangleFigureWithProgress progressRectangle;

	/**
	 * The progress label.
	 */
	private final Label label;

	/**
	 * Constructor.
	 */
	public ProgressFigure() {
		setOpaque(true);
		// setBorder(new LineBorder(ColorConstants.lightGray));
		label = new Label();
		add(label);
		progressRectangle = new RectangleFigureWithProgress();
		progressRectangle.setForegroundColor(ColorConstants.lightGray);
		add(progressRectangle);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(0);
		layout.setMinorAlignment(SWT.CENTER);
		setLayoutManager(layout);
	}

	/**
	 * Computes the label to display.
	 * 
	 * @return The label to display.
	 */
	private String computeLabel() {
		double percent;
		if (total == 0) {
			percent = 100;
		} else {
			percent = 100.0 * progress / total;
		}
		return MylynAgileUIMessages
				.getString(
						"ProgressFigure.Label", Integer.valueOf(progress), Integer.valueOf(total), Double.valueOf(percent)); //$NON-NLS-1$
	}

	/**
	 * Sets the progress.
	 * 
	 * @param progress
	 *            The progress.
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * Sets the total.
	 * 
	 * @param total
	 *            The total.
	 */
	public void setTotal(int total) {
		this.total = total;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		label.setText(computeLabel());
		progressRectangle.percent = (float)progress / (float)total;
		super.paint(graphics);
	}

	/**
	 * Figure that paints a progress bar.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class RectangleFigureWithProgress extends RectangleFigure {

		/**
		 * The progress to display.
		 */
		private float percent;

		/**
		 * Constructor.
		 */
		public RectangleFigureWithProgress() {
			setPreferredSize(-1, 4);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.RectangleFigure#fillShape(org.eclipse.draw2d.Graphics)
		 */
		@Override
		protected void fillShape(Graphics graphics) {
			super.fillShape(graphics);
			Rectangle b = new Rectangle(getBounds());
			b.width = (int)(percent * b.width);
			graphics.setBackgroundColor(ColorConstants.lightBlue);
			graphics.fillRectangle(b);
		}
	}
}
