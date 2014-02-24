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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownData;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownMapper;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the burndown chart of the current milestone.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BurndownTaskEditorPart extends AbstractTableTaskEditorPart {

	/**
	 * The backlog part descriptor ID.
	 */
	public static final String BURNDOWN_PART_DESC_ID = "org.tuleap.mylyn.task.agile.burndown"; //$NON-NLS-1$

	/**
	 * The burn-down chart height.
	 */
	private static final int BURNDOWN_HEIGHT = 250;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		Composite panel = toolkit.createComposite(parent);
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		panel.setLayoutData(data);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 3;
		gridLayout.verticalSpacing = 3;
		panel.setLayout(gridLayout);
		Section burndownSection = toolkit.createSection(panel, ExpandableComposite.TITLE_BAR
				| Section.EXPANDED | Section.TWISTIE);
		setControl(panel);
		burndownSection.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.DefaulBurndownLabel")); //$NON-NLS-1$
		burndownSection.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(burndownSection);

		Composite client = toolkit.createComposite(burndownSection);
		burndownSection.setClient(client);
		GridDataFactory.fillDefaults().applyTo(client);
		GridLayout burndownLayout = new GridLayout(1, false);
		burndownLayout.marginLeft = 0;
		burndownLayout.marginRight = 0;
		burndownLayout.marginTop = 0;
		burndownLayout.marginBottom = 0;
		client.setLayout(burndownLayout);

		TaskDataModel taskDataModel = getModel();
		final BurndownMapper burndown;
		if (taskDataModel != null) {
			burndown = new BurndownMapper(taskDataModel.getTaskData());
		} else {
			burndown = null;
		}

		if (burndown != null && burndown.getBurndownData() != null) {
			final Canvas canvas = new Canvas(client, SWT.NONE);
			GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, BURNDOWN_HEIGHT).grab(true, true).applyTo(
					canvas);
			canvas.addPaintListener(new BurndownPaintListener(burndown, canvas));
		} else {
			Label lbl = new Label(client, SWT.LEFT);
			lbl.setText(MylynAgileUIMessages.getString("PlanningTaskEditorPart.NoBurndownAvailable")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#getPartId()
	 */
	@Override
	public String getPartId() {
		return BURNDOWN_PART_DESC_ID;
	}

	/**
	 * Burndown Chart paint listener to draw the burn-down chart on a canvas.
	 *
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class BurndownPaintListener implements PaintListener {
		/**
		 * The burn-down mapper.
		 */
		private final BurndownMapper burndownMapper;

		/**
		 * The canvas to paint.
		 */
		private final Canvas canvas;

		/**
		 * Constructor.
		 *
		 * @param burndownMapper
		 *            The burn-down mapper
		 * @param canvas
		 *            The canvas to paint
		 */
		private BurndownPaintListener(BurndownMapper burndownMapper, Canvas canvas) {
			this.burndownMapper = burndownMapper;
			this.canvas = canvas;
		}

		@Override
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			int hMargin = 5;
			int vMargin = 5;
			gc.setAntialias(SWT.ON);
			Rectangle clientArea = canvas.getClientArea();
			Rectangle r = new Rectangle(clientArea.x + hMargin, clientArea.y + vMargin, clientArea.width - 2
					* hMargin, clientArea.height - 2 * vMargin);
			gc.setForeground(e.display.getSystemColor(SWT.COLOR_GRAY));
			gc.drawRectangle(r);
			BurndownData burndown = burndownMapper.getBurndownData();
			if (burndown == null) {
				return;
			}
			double capacity = burndown.getCapacity();
			int duration = burndown.getDuration();
			double[] values = burndown.getPoints();
			double max = capacity;
			for (double v : values) {
				if (max < v) {
					max = v;
				}
			}
			double vratio = max / r.height;
			double hratio = (double)duration / r.width;
			// Draw ideal burn-down line
			gc.drawLine(r.x, r.y, r.x + r.width, r.y + (int)(capacity / vratio));
			// Draw points
			gc.setLineWidth(3);
			gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLUE));
			// We don't display more points than the duration of the sprint
			int len = Math.min(values.length, duration + 1);
			if (len > 0) {
				int x = r.x;
				int y = r.y + r.height - (int)(values[0] / vratio);
				for (int i = 1; i < len; i++) {
					int x1 = r.x + (int)(i / hratio);
					int y1 = r.y + r.height - (int)(values[i] / vratio);
					gc.drawLine(x, y, x1, y1);
					gc.fillOval(x - 2, y - 2, 4, 4);
					gc.drawOval(x - 3, y - 3, 6, 6);
					x = x1;
					y = y1;
				}
				gc.fillOval(x - 2, y - 2, 4, 4);
				gc.drawOval(x - 3, y - 3, 6, 6);
			}
		}
	}

}
