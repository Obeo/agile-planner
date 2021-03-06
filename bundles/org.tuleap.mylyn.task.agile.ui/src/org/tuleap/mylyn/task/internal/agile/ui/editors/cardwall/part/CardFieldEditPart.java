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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part;

import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.ArtifactLinksMouseMotionListener;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFieldFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardDateFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardDateFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardDateFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldSelectionEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardLinksFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardLinksFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardMultiSelectionFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardMultiSelectionFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardMultiSelectionFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util.CardDateCellEditor;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util.CardDateTimeCellEditor;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util.CardMultiSelectionFieldEditor;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator.DoubleValidator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator.IntegerValidator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator.LinksValidator;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Edit part for a configurable field in a card.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldEditPart extends AbstractGraphicalEditPart {

	/**
	 * The task attribute listener.
	 */
	private ITaskAttributeChangeListener attributeListener;

	/**
	 * Listener for hovering the artifact links ID.
	 */
	private ArtifactLinksMouseMotionListener artifactLinksMouseMotionListener;

	/**
	 * Listener for clicking on the artifact links ID's.
	 */
	private MouseListener artifactLinksMouseListener;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new CardFieldFigure();
	}

	/**
	 * Returns this edit part's main figure, as a {@link CardFieldFigure}.
	 *
	 * @return This edit part's main figure, as a {@link CardFieldFigure}.
	 */
	public CardFieldFigure getCardFieldFigure() {
		return (CardFieldFigure)getFigure();
	}

	@Override
	protected void createEditPolicies() {
		TaskAttribute attribute = (TaskAttribute)getModel();
		// Only editable if the TaskAttribute is NOT read-only or disabled
		TaskAttributeMetaData metaData = attribute.getMetaData();
		if (!metaData.isReadOnly() && !metaData.isDisabled()) {
			// Indicate selected field if it's editable.
			installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new CardFieldSelectionEditPolicy());
			String type = metaData.getType();
			if (TaskAttribute.TYPE_SINGLE_SELECT.equals(type)) {
				// bound field
				installEditPolicy(REQ_DIRECT_EDIT, new CardBoundFieldEditPolicy());
			} else if (TaskAttribute.TYPE_DATE.equals(type)) {
				installEditPolicy(REQ_DIRECT_EDIT, new CardDateFieldEditPolicy());
			} else if (TaskAttribute.TYPE_DATETIME.equals(type)) {
				installEditPolicy(REQ_DIRECT_EDIT, new CardDateFieldEditPolicy());
			} else if (TaskAttribute.TYPE_MULTI_SELECT.equals(type)) {
				installEditPolicy(REQ_DIRECT_EDIT, new CardMultiSelectionFieldEditPolicy());
			} else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(type)) {
				installEditPolicy(REQ_DIRECT_EDIT, new CardLinksFieldEditPolicy());
			} else {
				installEditPolicy(REQ_DIRECT_EDIT, new CardFieldEditPolicy());
			}
		}
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType() == REQ_DIRECT_EDIT) {
			performDirectEditing();
		} else {
			super.performRequest(req);
		}
	}

	/**
	 * Performs the direct edit.
	 */
	private void performDirectEditing() {
		TaskAttribute attribute = (TaskAttribute)getModel();
		// Only editable if the TaskAttribute is NOT read-only or disabled
		TaskAttributeMetaData metaData = attribute.getMetaData();
		if (metaData.isReadOnly() || metaData.isDisabled()) {
			return;
		}
		TextFlow label = ((CardFieldFigure)getFigure()).getValueLabel();
		String attributeType = metaData.getType();
		DirectEditManager manager = null;
		if (TaskAttribute.TYPE_SINGLE_SELECT.equals(attributeType)) {
			manager = new CardBoundFieldDirectEditManager(this, ComboBoxCellEditor.class,
					new CardBoundFieldCellEditorLocator(label), attribute);
		} else if (TaskAttribute.TYPE_MULTI_SELECT.equals(attributeType)) {
			manager = new CardMultiSelectionFieldDirectEditManager(this, CardMultiSelectionFieldEditor.class,
					new CardMultiSelectionFieldCellEditorLocator(label), attribute);
		} else if (TaskAttribute.TYPE_ATTACHMENT.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "attachment"), false); //$NON-NLS-1$
		} else if (TaskAttribute.TYPE_COMMENT.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "comment"), false); //$NON-NLS-1$
		} else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(attributeType)) {
			manager = new CardLinksFieldDirectEditManager(this, TextCellEditor.class,
					new CardFieldCellEditorLocator(((CardFieldFigure)getFigure()).getLocator()), attribute
							.getValues(), new LinksValidator());
		} else if (TaskAttribute.TYPE_URL.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "URL"), false); //$NON-NLS-1$
		} else if (TaskAttribute.TYPE_INTEGER.equals(attributeType)) {
			manager = new CardFieldDirectEditManager(this, TextCellEditor.class,
					new CardFieldCellEditorLocator(label), label, new IntegerValidator());
		} else if (TaskAttribute.TYPE_DOUBLE.equals(attributeType)) {
			manager = new CardFieldDirectEditManager(this, TextCellEditor.class,
					new CardFieldCellEditorLocator(label), label, new DoubleValidator());
		} else if (TaskAttribute.TYPE_DATE.equals(attributeType)) {
			manager = new CardDateFieldDirectEditManager(this, CardDateCellEditor.class,
					new CardDateFieldCellEditorLocator(label), attribute);
		} else if (TaskAttribute.TYPE_DATETIME.equals(attributeType)) {
			manager = new CardDateFieldDirectEditManager(this, CardDateTimeCellEditor.class,
					new CardDateFieldCellEditorLocator(label), attribute);
		} else {
			manager = new CardFieldDirectEditManager(this, TextCellEditor.class,
					new CardFieldCellEditorLocator(label), label);
		}
		if (manager != null) {
			manager.show();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		// Listen to the model
		attributeListener = new ITaskAttributeChangeListener() {
			@Override
			public void attributeChanged(TaskAttribute attribute) {
				if (attribute == getModel()) {
					refreshVisuals();
				}
			}
		};
		((CardModel)getParent().getModel()).getWrapper().addListener(attributeListener);
		artifactLinksMouseListener = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {
				//
			}

			@Override
			public void mousePressed(MouseEvent me) {
				String type = ((TaskAttribute)getModel()).getMetaData().getType();
				if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(type)) {
					if (me.getSource() instanceof TextFlow) {
						String text = ((TextFlow)me.getSource()).getText();
						if (text.contains(", ")) { //$NON-NLS-1$
							text = text.substring(2, text.length());
						}
						openTask(text);
					}
				}
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				//
			}
		};
		artifactLinksMouseMotionListener = new ArtifactLinksMouseMotionListener();
		for (Object child : getCardFieldFigure().getFlowPage().getChildren()) {
			if (child instanceof TextFlow
					&& !", ".equals(((TextFlow)child).getText()) //$NON-NLS-1$
					&& TaskAttribute.TYPE_TASK_DEPENDENCY.equals(((TaskAttribute)getModel()).getMetaData()
							.getType())) {
				((TextFlow)child).addMouseListener(artifactLinksMouseListener);
				((TextFlow)child).addMouseMotionListener(artifactLinksMouseMotionListener);

			}
		}
	}

	/**
	 * Open the task corresponding to the card.
	 *
	 * @param artifactLinkId
	 *            the artifact link id
	 */
	private void openTask(String artifactLinkId) {
		CardWrapper card = ((CardModel)getParent().getModel()).getWrapper();
		TaskData taskData = card.getWrappedAttribute().getTaskData();
		TaskRepository repository = TasksUi.getRepositoryManager().getRepository(taskData.getConnectorKind(),
				taskData.getRepositoryUrl());
		if (repository != null) {
			TasksUiUtil.openTask(repository, artifactLinkId);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		((CardModel)getParent().getModel()).getWrapper().removeListener(attributeListener);
		for (Object child : getCardFieldFigure().getFlowPage().getChildren()) {
			if (child instanceof TextFlow) {
				((TextFlow)child).addMouseListener(artifactLinksMouseListener);
				((TextFlow)child).addMouseMotionListener(artifactLinksMouseMotionListener);

			}
		}
		attributeListener = null;
		artifactLinksMouseListener = null;
		artifactLinksMouseMotionListener = null;
		super.deactivate();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		CardFieldFigure f = getCardFieldFigure();
		TaskAttribute attribute = (TaskAttribute)getModel();
		String type = attribute.getMetaData().getType();

		// Indicates if the attributes' values are null or not
		boolean isNull = false;
		if (TaskAttribute.TYPE_SINGLE_SELECT.equals(type)) {
			List<String> values = Lists.newArrayList();
			for (String key : attribute.getValues()) {
				values.add(attribute.getOption(key));
			}
			f.setField(attribute.getMetaData().getLabel(), values);

		} else if (TaskAttribute.TYPE_MULTI_SELECT.equals(type)) {
			List<String> values = Lists.newArrayList();
			for (String key : attribute.getValues()) {
				if (attribute.getOption(key) == null) {
					isNull = true;
				}
				values.add(attribute.getOption(key));
			}

			if (isNull) {
				values.clear();
				values.add(""); //$NON-NLS-1$
			}
			f.setField(attribute.getMetaData().getLabel(), values);

		} else if (TaskAttribute.TYPE_DATETIME.equals(type)) {
			List<String> values = Lists.newArrayList();
			String attValue = attribute.getValue();
			if (attValue.isEmpty()) {
				values.add("-"); //$NON-NLS-1$
			} else {
				try {
					long longDate = Long.parseLong(attribute.getValue());
					Date date = new Date(longDate);
					DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
							DateFormat.MEDIUM);
					values.add(dateFormat.format(date));
				} catch (NumberFormatException nfe) {
					// Null or invalid dates are ignored
					MylynAgileUIActivator.log(new Status(IStatus.WARNING, MylynAgileUIActivator.PLUGIN_ID,
							MylynAgileUIMessages.getString("CardFieldEditPart.InvalidDate", attribute //$NON-NLS-1$
									.getMetaData().getLabel()), nfe));
					values.add("???"); //$NON-NLS-1$
				}
			}
			f.setField(attribute.getMetaData().getLabel(), values);

		} else if (TaskAttribute.TYPE_DATE.equals(type)) {
			List<String> values = Lists.newArrayList();
			String attValue = attribute.getValue();
			if (attValue.isEmpty()) {
				values.add("-"); //$NON-NLS-1$
			} else {
				try {
					Long longDate = Long.valueOf(attribute.getValue());
					Date date = new Date(longDate.longValue());
					DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
					values.add(dateFormat.format(date));
				} catch (NumberFormatException nfe) {
					// Null or invalid dates are ignored
					MylynAgileUIActivator.log(new Status(IStatus.WARNING, MylynAgileUIActivator.PLUGIN_ID,
							MylynAgileUIMessages.getString("CardFieldEditPart.InvalidDate", attribute //$NON-NLS-1$
									.getMetaData().getLabel()), nfe));
					values.add("???"); //$NON-NLS-1$
				}
			}
			f.setField(attribute.getMetaData().getLabel(), values);

		} else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(type)) {
			f.setLinkField(attribute.getMetaData().getLabel(), attribute.getValues());
			f.getFlowPage().setForegroundColor(ColorConstants.black);

		} else {
			f.setField(attribute.getMetaData().getLabel(), attribute.getValues());
		}
		// The wrapping page needs to reflow so that scrollbars don't appear where they shouldn't
		((CardEditPart)getParent()).refreshVisuals();
	}
}
