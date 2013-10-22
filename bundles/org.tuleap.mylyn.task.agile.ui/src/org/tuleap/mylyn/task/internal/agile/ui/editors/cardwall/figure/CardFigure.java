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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.util.URLFigure;

/**
 * Figure representing a card in the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardFigure extends RoundedRectangle {

	/**
	 * Interface for listeners having to manage changes on the configurable fields of a card.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private interface ConfigurableFieldListener {

		/**
		 * Executed behavior when the given configurable field is added to a card figure.
		 * 
		 * @param field
		 *            The added field.
		 */
		void configurableFieldAdded(ConfigurableFieldFigure field);

	}

	/**
	 * Listener to launch action on click on a URL.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class ActionURLMouseListener implements MouseListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
			// nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			// TODO Action from the URL id
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"TODO", "TODO: We should open the details of the relative item");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// nothing
		}
	}

	/**
	 * Listener to fold/unfold a group of fields and add new fields to a graphical container (folder).
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class FoldingListener implements MouseListener, ConfigurableFieldListener {

		/**
		 * The configurable fields nested in a folder.
		 */
		private List<ConfigurableFieldFigure> foldedFields;

		/**
		 * The folder to fold or unfold.
		 */
		private IFigure folder;

		/**
		 * Constructor.
		 * 
		 * @param folder
		 *            The folder to fold or unfold.
		 * @param foldedFields
		 *            The configurable fields nested in a folder.
		 */
		public FoldingListener(IFigure folder, List<ConfigurableFieldFigure> foldedFields) {
			this.folder = folder;
			this.foldedFields = foldedFields;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
			// nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			boolean isFolderExpanded = isFolderExpanded();
			for (ConfigurableFieldFigure field : foldedFields) {
				if (isFolderExpanded) {
					folder.remove(field);
				} else {
					folder.add(field);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure.ConfigurableFieldListener#configurableFieldAdded(org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure.ConfigurableFieldFigure)
		 */
		@Override
		public void configurableFieldAdded(ConfigurableFieldFigure field) {
			if (isFolderExpanded()) {
				folder.add(field);
			}
		}

		/**
		 * Checks if the folder is folded or unfolded (expanded).
		 * 
		 * @return True if the folder is expanded, False otherwise.
		 */
		private boolean isFolderExpanded() {
			return folder.getChildren().containsAll(foldedFields);
		}
	}

	/**
	 * URL figure which may react clicking on the label to fold or unfold a set of configurable fields and
	 * from the add of configurable fields.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class FoldingFigure extends URLFigure {

		/**
		 * Listeners to fold/unfold and add new fields.
		 */
		private List<FoldingListener> foldingListeners = new ArrayList<FoldingListener>();

		/**
		 * Constructor.
		 * 
		 * @param text
		 *            The flow figure which display the label of the url.
		 */
		public FoldingFigure(FlowFigure text) {
			super(text);
		}

		/**
		 * Add a folding listener to manage fold/unfold and the add of new configurable fields.
		 * 
		 * @param listener
		 *            A folding listener.
		 */
		public void addFoldingListener(FoldingListener listener) {
			textFigure.addMouseListener(listener);
			if (!foldingListeners.contains(listener)) {
				foldingListeners.add(listener);
			}
		}

		/**
		 * Notifies the folding listeners that a configurable field has been added.
		 * 
		 * @param field
		 *            A configurable field.
		 */
		public void fireFieldAdded(ConfigurableFieldFigure field) {
			for (FoldingListener listener : foldingListeners) {
				listener.configurableFieldAdded(field);
			}
		}
	}

	/**
	 * A configurable field composed of a label and a value. Each configurable field is identified by an id.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class ConfigurableFieldFigure extends Panel {

		/**
		 * The id.
		 */
		private String id;

		/**
		 * The figure which is about label.
		 */
		private TextFlow labelFlow;

		/**
		 * The figure which is about value.
		 */
		private TextFlow valueFlow;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            The id of the field.
		 * @param label
		 *            The label to use.
		 */
		public ConfigurableFieldFigure(String id, String label) {

			this.id = id;

			setLayoutManager(new GridLayout(1, false));
			FlowPage flowPage = new FlowPage();
			flowPage.setOpaque(true);
			FontData[] defaultFontData = defaultFont.getFontData();
			FontData newFontData = new FontData(defaultFontData[0].getName(),
					defaultFontData[0].getHeight() - 1, defaultFontData[0].getStyle());
			flowPage.setFont(new Font(null, newFontData));

			BlockFlow blockFlow = new BlockFlow();

			labelFlow = new TextFlow();
			labelFlow.setText(label + ": "); //$NON-NLS-1$
			blockFlow.add(labelFlow);

			valueFlow = new TextFlow();
			blockFlow.add(valueFlow);

			flowPage.add(blockFlow);

			add(flowPage);
			setConstraint(flowPage, new GridData(SWT.LEFT, SWT.TOP, true, true));
		}

		/**
		 * Get the figure which is about the label.
		 * 
		 * @return The label figure.
		 */
		public TextFlow getLabel() {
			return labelFlow;
		}

		/**
		 * Get the figure which is about the value.
		 * 
		 * @return The value figure.
		 */
		public TextFlow getValue() {
			return valueFlow;
		}

		/**
		 * Get the id.
		 * 
		 * @return The id.
		 */
		public String getId() {
			return id;
		}

	}

	/**
	 * Preferred width.
	 */
	private static final int CARD_PREFERRED_WIDTH = 150;

	/**
	 * Preferred height.
	 */
	private static final int CARD_PREFERRED_HEIGHT = 150;

	/**
	 * Red part for the color of the card.
	 */
	private static final int CARD_COLOR_RED = 255;

	/**
	 * Green part for the color of the card.
	 */
	private static final int CARD_COLOR_GREEN = 255;

	/**
	 * Blue part for the color of the card.
	 */
	private static final int CARD_COLOR_BLUE = 187;

	/**
	 * The margin between text block inside the card and the border of this one, and between the title and
	 * description.
	 */
	private static final int MARGIN = 5;

	/**
	 * The title of the card.
	 */
	private TextFlow titleTextFlow;

	/**
	 * The url of the card.
	 */
	private TextFlow urlTextFlow;

	/**
	 * The detailed description of the card.
	 */
	private TextFlow descTextFlow;

	/**
	 * The panel containing the data of the card.
	 */
	private Panel contentPanel;

	/**
	 * The panel containing the details of the card (the set of the configurable fields).
	 */
	private Panel detailsPanel;

	/**
	 * The list of configurable fields to fold or unfold in the card.
	 */
	private List<ConfigurableFieldFigure> configurableFields = new ArrayList<ConfigurableFieldFigure>();

	/**
	 * The default font to use in the card.
	 */
	private Font defaultFont = JFaceResources.getDefaultFont();

	/**
	 * Constructor.
	 */
	public CardFigure() {

		// If uncommented, the wrapping of labels will work but the height will not adjust itself.
		// setPreferredSize(CARD_PREFERRED_WIDTH, CARD_PREFERRED_HEIGHT);

		setBackgroundColor(new Color(Display.getCurrent(), new RGB(CARD_COLOR_RED, CARD_COLOR_GREEN,
				CARD_COLOR_BLUE)));
		setLayoutManager(new GridLayout());

		setLineWidth(1);
		setOpaque(true);

		Panel marginsPanel = new Panel();
		marginsPanel.setOpaque(false);
		GridLayout marginsLayout = new GridLayout(1, false);
		marginsLayout.marginHeight = MARGIN;
		marginsLayout.marginWidth = MARGIN;
		marginsPanel.setLayoutManager(marginsLayout);

		contentPanel = new Panel();

		ToolbarLayout contentLayout = new ToolbarLayout(false);
		contentLayout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		contentLayout.setStretchMinorAxis(true);
		// contentLayout.setSpacing(MARGIN);
		contentPanel.setLayoutManager(contentLayout);

		addUrl();

		addTitle();

		addDetails();

		marginsPanel.add(contentPanel);
		marginsPanel.setConstraint(contentPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		add(marginsPanel);
		setConstraint(marginsPanel, new GridData(SWT.FILL, SWT.FILL, true, true));

		setFont(defaultFont);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		// FIXME I'm not sure it is the good solution to locate this here ! This may involve performance
		// issues...
		// But this enables to have the same column width and a height which adjust itself in relation to the
		// fields added + wrapping of labels.
		setPreferredSize(CARD_PREFERRED_WIDTH, getPreferredHeight());
		super.paint(graphics);
	}

	/**
	 * Get the preferred height of the card in relation to the displayed fields.
	 * 
	 * @return The preferred height.
	 */
	public int getPreferredHeight() {
		int cardY = getLocation().y;
		int lastY = CARD_PREFERRED_HEIGHT;
		if (contentPanel.getChildren().size() > 0) {
			Object last = contentPanel.getChildren().get(contentPanel.getChildren().size() - 1);
			if (last instanceof IFigure) {
				lastY = ((IFigure)last).getBounds().y + ((IFigure)last).getBounds().height;
			}
		} else {
			lastY = detailsPanel.getLocation().y;
		}
		return lastY + MARGIN - cardY;
	}

	/**
	 * Setter of the title of the card.
	 * 
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		titleTextFlow.setText(title);
	}

	/**
	 * Setter of the url of the card.
	 * 
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		urlTextFlow.setText(url);
	}

	/**
	 * Setter of the description of the card.
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		descTextFlow.setText(description);
	}

	/**
	 * Set the value of the field identified by the given <code>id</code> with the given <code>value</code>.<br>
	 * If the field does not exist, it is added to the card before setting the value.<br>
	 * The label of the field is required if the field does not already exist.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @param label
	 *            The label of the configurable field (if the field does not exist).
	 * @param value
	 *            The value to set.
	 */
	public void setConfigurableField(String id, String label, String value) {
		TextFlow fieldValue = getConfigurableField(id);
		if (fieldValue == null) {
			fieldValue = addConfigurableField(id, label);
		}
		fieldValue.setText(value);
	}

	/**
	 * Get the field on the card from its id.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @return The graphical field.
	 */
	public TextFlow getConfigurableField(final String id) {
		ConfigurableFieldFigure field = Iterables.find(configurableFields,
				new Predicate<ConfigurableFieldFigure>() {
					/**
					 * {@inheritDoc}
					 * 
					 * @see com.google.common.base.Predicate#apply(java.lang.Object)
					 */
					@Override
					public boolean apply(ConfigurableFieldFigure input) {
						return id.equals(input.getId());
					}
				}, null);

		if (field != null) {
			return field.getValue();
		}
		return null;
	}

	/**
	 * Add the title of the card.
	 */
	private void addTitle() {
		Panel title = new Panel();
		title.setLayoutManager(new GridLayout(1, false));

		FlowPage titleFlowPage = new FlowPage();
		titleFlowPage.setOpaque(true);
		titleTextFlow = new TextFlow();
		titleFlowPage.add(titleTextFlow);

		title.add(titleFlowPage);
		title.setConstraint(titleFlowPage, new GridData(SWT.CENTER, SWT.TOP, true, true));

		contentPanel.add(title);
	}

	/**
	 * Add the url of the card.
	 */
	private void addUrl() {

		Panel url = new Panel();
		url.setLayoutManager(new GridLayout(1, false));

		urlTextFlow = new TextFlow();
		URLFigure urlFigure = new URLFigure(urlTextFlow);

		urlFigure.addMouseListener(new ActionURLMouseListener());

		url.add(urlFigure);
		url.setConstraint(urlFigure, new GridData(SWT.LEFT, SWT.TOP, true, true));

		contentPanel.add(url);
	}

	/**
	 * Add the details folder which contains a set of configurable fields.
	 */
	private void addDetails() {
		detailsPanel = new Panel();
		detailsPanel.setLayoutManager(new GridLayout(1, false));

		FoldingFigure urlFigure = new FoldingFigure(new TextFlow("détails")); //$NON-NLS-1$

		urlFigure.addFoldingListener(new FoldingListener(contentPanel, configurableFields));

		detailsPanel.add(urlFigure);
		detailsPanel.setConstraint(urlFigure, new GridData(SWT.LEFT, SWT.TOP, true, true));

		contentPanel.add(detailsPanel);
	}

	/**
	 * Add a new panel to the card to display a new field.
	 * 
	 * @param id
	 *            The id of the configurable field.
	 * @param label
	 *            The label of the configurable field.
	 * @return The text flow of the field.
	 */
	private TextFlow addConfigurableField(String id, String label) {
		ConfigurableFieldFigure field = new ConfigurableFieldFigure(id, label);

		configurableFields.add(field);
		fireConfigurableFieldAdded(field);

		return field.getValue();
	}

	/**
	 * Notifies the URL figure, which manages the folding/unfolding of the configurable fields, for the add of
	 * a configurable field to the card.
	 * 
	 * @param field
	 *            The added configurable field.
	 */
	private void fireConfigurableFieldAdded(ConfigurableFieldFigure field) {
		FoldingFigure figure = Iterables.find(detailsPanel.getChildren(), Predicates
				.instanceOf(FoldingFigure.class), null);
		if (figure != null) {
			figure.fireFieldAdded(field);
		}
	}

}
