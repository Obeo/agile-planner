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

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Figure of a configurable card field.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardFieldFigure extends Figure {

	/**
	 * The field label.
	 */
	private final TextFlow label;

	/**
	 * The actual field values.
	 */
	private List<String> values;

	/**
	 * The value of the configurable field.
	 */
	private final TextFlow value;

	/**
	 * Constructor.
	 */
	public CardFieldFigure() {
		label = new TextFlow();
		Panel labelPanel = new Panel();
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout labelLayout = new ToolbarLayout();
		labelLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		labelLayout.setStretchMinorAxis(true);
		labelPanel.setLayoutManager(labelLayout);
		initTextFlow(label, labelPanel, ColorConstants.gray, PositionConstants.RIGHT);
		add(labelPanel);

		value = new TextFlow();
		Panel valuePanel = new Panel();
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout valueLayout = new ToolbarLayout();
		valueLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		valueLayout.setStretchMinorAxis(true);
		valuePanel.setLayoutManager(valueLayout);
		initTextFlow(value, valuePanel, ColorConstants.black, PositionConstants.LEFT);
		add(valuePanel);

		GridLayout layout = new GridLayout(2, true);
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = IMylynAgileUIConstants.MARGIN;
		layout.marginHeight = 0;
		layout.marginWidth = 5;
		setLayoutManager(layout);
		layout.setConstraint(labelPanel, new GridData(SWT.FILL, SWT.FILL, true, true));
		layout.setConstraint(valuePanel, new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Set the field data.
	 * 
	 * @param lbl
	 *            The label
	 * @param someValues
	 *            The values
	 */
	public void setField(String lbl, List<String> someValues) {
		this.label.setText(lbl + ":"); //$NON-NLS-1$
		setValues(someValues);
	}

	/**
	 * Sets the values of this field.
	 * 
	 * @param values
	 *            The values to set.
	 */
	public void setValues(List<String> values) {
		this.values = Lists.newArrayList(values);
		StringBuilder b = new StringBuilder();
		if (!values.isEmpty()) {
			Iterator<String> it = values.iterator();
			b.append(it.next());
			while (it.hasNext()) {
				b.append(", ").append(it.next()); //$NON-NLS-1$
			}
		}
		value.setText(b.toString());
	}

	/**
	 * Provides access to the value label, for direct editing.
	 * 
	 * @return The value label.
	 */
	public TextFlow getValueLabel() {
		return value;
	}

	/**
	 * Provides the actual values displayed by the field. Modifying the returned list has no effect on this
	 * figure.
	 * 
	 * @return a List, never <code>null</code> but possibly empty, which is a copy of this figure's list of
	 *         values.
	 */
	public List<String> getValues() {
		if (values == null) {
			return Collections.emptyList();
		}
		return Lists.newArrayList(values);
	}

	/**
	 * Set-up of a TextFlow in a given panel.
	 * 
	 * @param textFlow
	 *            the text flow to setup
	 * @param panel
	 *            The panel that should contain the text flow
	 * @param color
	 *            the foreground color of the text
	 * @param alignment
	 *            the alignment of the text flow
	 */
	private void initTextFlow(TextFlow textFlow, Panel panel, Color color, int alignment) {
		FlowPage labelFlowPage = new FlowPage();

		labelFlowPage.setForegroundColor(color);
		labelFlowPage.add(textFlow);
		labelFlowPage.setHorizontalAligment(alignment);

		ParagraphTextLayout paragraphLayout = new ParagraphTextLayout(textFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT);
		textFlow.setLayoutManager(paragraphLayout);
		FlowContext flowContext = (FlowContext)labelFlowPage.getLayoutManager();
		paragraphLayout.setFlowContext(flowContext);

		panel.add(labelFlowPage);
	}

}
