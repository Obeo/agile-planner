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

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFieldFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardBoundFieldEditPolicy;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldCellEditorLocator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldDirectEditManager;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy.CardFieldEditPolicy;
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
			String type = metaData.getType();
			if (TaskAttribute.TYPE_SINGLE_SELECT.equals(type) || TaskAttribute.TYPE_MULTI_SELECT.equals(type)) {
				// bound field
				installEditPolicy(REQ_DIRECT_EDIT, new CardBoundFieldEditPolicy());
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
		Label label = ((CardFieldFigure)getFigure()).getValueLabel();
		TaskAttribute attribute = (TaskAttribute)getModel();
		String attributeType = attribute.getMetaData().getType();
		DirectEditManager manager = null;
		if (TaskAttribute.TYPE_SINGLE_SELECT.equals(attributeType)) {
			manager = new CardBoundFieldDirectEditManager(this, ComboBoxCellEditor.class,
					new CardBoundFieldCellEditorLocator(label), attribute);
		} else if (TaskAttribute.TYPE_MULTI_SELECT.equals(attributeType)) {
			// manager = new CardFieldDirectEditManager(this, ComboBoxCellEditor.class,
			// new CardFieldMultiCellEditorLocator(label), label, attribute.getValues(),
			// attribute.getOptions());
		} else if (TaskAttribute.TYPE_ATTACHMENT.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "attachment"), false); //$NON-NLS-1$
		} else if (TaskAttribute.TYPE_COMMENT.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "comment"), false); //$NON-NLS-1$
		} else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "dependency"), //$NON-NLS-1$
					false);
		} else if (TaskAttribute.TYPE_URL.equals(attributeType)) {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					IMylynAgileUIConstants.DIRECT_EDIT_NOT_SUPPORTED, "URL"), false); //$NON-NLS-1$
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
		((CardWrapper)getParent().getModel()).addListener(attributeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		((CardWrapper)getParent().getModel()).removeListener(attributeListener);
		attributeListener = null;
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
		if (TaskAttribute.TYPE_SINGLE_SELECT.equals(type) || TaskAttribute.TYPE_MULTI_SELECT.equals(type)) {
			List<String> values = Lists.newArrayList();
			for (String key : attribute.getValues()) {
				values.add(attribute.getOption(key));
			}
			f.setField(attribute.getMetaData().getLabel(), values);
		} else {
			f.setField(attribute.getMetaData().getLabel(), attribute.getValues());
		}
	}
}
