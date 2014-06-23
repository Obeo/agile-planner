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
	 * The value text flow.
	 */
	private final TextFlow value;

	/**
	 * The link value text flow.
	 */
	private TextFlow linkValue;

	/**
	 * The text flow used to locate links direct editing.
	 */
	private TextFlow linkLocator;

	/**
	 * The label flow page.
	 */
	private FlowPage labelFlowPage;

	/**
	 * The value panel.
	 */
	private Panel valuePanel;

	/**
	 * The value panel.
	 */
	private Panel labelPanel;

	/**
	 * Constructor.
	 */
	public CardFieldFigure() {
		label = new TextFlow();
		labelPanel = new Panel();
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout labelLayout = new ToolbarLayout();
		labelLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		labelLayout.setStretchMinorAxis(true);
		labelPanel.setLayoutManager(labelLayout);
		initTextFlow(label, labelPanel, ColorConstants.gray, PositionConstants.RIGHT);
		add(labelPanel);

		value = new TextFlow();
		valuePanel = new Panel();
		// The content panel MUST have a ToolbarLayout for PageFlows to work all right.
		ToolbarLayout valueLayout = new ToolbarLayout();
		valueLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		valueLayout.setStretchMinorAxis(true);
		valuePanel.setLayoutManager(valueLayout);
		initTextFlow(value, valuePanel, ColorConstants.black, PositionConstants.LEFT);
		add(valuePanel);

		managePanelsLayouts();
	}

	/**
	 * Manage panels layouts.
	 */
	private void managePanelsLayouts() {
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
	 * Set the links field data.
	 *
	 * @param lbl
	 *            The label
	 * @param someValues
	 *            The links values
	 */
	public void setLinkField(String lbl, List<String> someValues) {
		this.label.setText(lbl + ":"); //$NON-NLS-1$
		setLinksValues(someValues);
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
	 * Sets the links values.
	 *
	 * @param theValues
	 *            The links values
	 */
	public void setLinksValues(List<String> theValues) {
		labelFlowPage = new FlowPage();
		if (!theValues.isEmpty()) {
			Iterator<String> it = theValues.iterator();
			linkValue = new TextFlow(it.next());
			linkLocator = linkValue;
			manageLinksLayouts(null);
			while (it.hasNext()) {
				TextFlow separator = new TextFlow(", "); //$NON-NLS-1$
				linkValue = new TextFlow(it.next());
				manageLinksLayouts(separator);
			}
		}
	}

	/**
	 * Manage the links field layouts.
	 *
	 * @param separator
	 *            The links separator
	 */
	private void manageLinksLayouts(TextFlow separator) {
		this.remove(valuePanel);
		valuePanel = new Panel();
		managePanelsLayouts();

		ToolbarLayout valueLayout = new ToolbarLayout();
		valueLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		valueLayout.setStretchMinorAxis(true);
		valuePanel.setLayoutManager(valueLayout);
		add(valuePanel);
		labelFlowPage.setForegroundColor(ColorConstants.gray);
		labelFlowPage.setHorizontalAligment(PositionConstants.LEFT);
		FlowContext flowContext = (FlowContext)labelFlowPage.getLayoutManager();
		if (separator != null) {
			labelFlowPage.add(separator);
			ParagraphTextLayout separatorLayout = new ParagraphTextLayout(separator,
					ParagraphTextLayout.WORD_WRAP_SOFT);
			separator.setLayoutManager(separatorLayout);
			separatorLayout.setFlowContext(flowContext);
		}
		labelFlowPage.add(linkValue);
		ParagraphTextLayout linkValueLayout = new ParagraphTextLayout(linkValue,
				ParagraphTextLayout.WORD_WRAP_SOFT);
		linkValue.setLayoutManager(linkValueLayout);
		linkValueLayout.setFlowContext(flowContext);
		valuePanel.add(labelFlowPage);
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
	public void initTextFlow(TextFlow textFlow, Panel panel, Color color, int alignment) {
		labelFlowPage = new FlowPage();

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

	/**
	 * Get the flow page.
	 *
	 * @return The flow page.
	 */
	public FlowPage getFlowPage() {
		return this.labelFlowPage;
	}

	/**
	 * Provides access to the link locator, for direct editing.
	 *
	 * @return The value label.
	 */
	public TextFlow getLocator() {
		return linkLocator;
	}

}
