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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.tuleap.mylyn.task.agile.core.ICardMapping;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.MylynAgileUIActivator;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardDetailsPanel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.CardFigure;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure.URLMouseMotionListener;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.ICardwallProperties;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The edit part for the cards.
 *
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class CardEditPart extends AbstractGraphicalEditPart {
	/**
	 * The identifier of the has parent task attribute.
	 */
	public static final String HAS_PARENT = "has_parent"; //$NON-NLS-1$

	/**
	 * Listener for folding the card's details.
	 */
	private MouseListener labelMouseListener;

	/**
	 * Listener of folding state of the card.
	 */
	private PropertyChangeListener cardFoldingListener;

	/**
	 * Listener for clicking on the card's ID.
	 */
	private MouseListener urlMouseListener;

	/**
	 * Listener for clicking on the new card creation.
	 */
	private MouseListener createCardMouseListener;

	/**
	 * Listener for hovering the card's ID.
	 */
	private URLMouseMotionListener urlMouseMotionListener;

	/**
	 * The task editor taskEditorPart displaying the cardwall.
	 */
	private AbstractTaskEditorPart taskEditorPart;

	/**
	 * Constructor.
	 *
	 * @param taskEditorPart
	 *            The task editor taskEditorPart that displays the cardwall.
	 */
	public CardEditPart(AbstractTaskEditorPart taskEditorPart) {
		this.taskEditorPart = taskEditorPart;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		CardModel cardModel = (CardModel)getModel();
		CardWrapper wrapper = cardModel.getWrapper();
		return new CardFigure(wrapper.getColumnId() != null, wrapper.hasChildren());
	}

	/**
	 * Get the figure of the card.
	 *
	 * @return The card figure.
	 */
	public CardFigure getCardFigure() {
		return (CardFigure)getFigure();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		CardFigure fig = getCardFigure();
		CardModel cardModel = (CardModel)getModel();
		CardWrapper card = cardModel.getWrapper();

		fig.setTitle(card.getLabel());
		fig.setUrl(card.getDisplayId());
		fig.setAccentColor(card.getAccentColor());

		CardDetailsPanel panel = getDetailsPanel();
		panel.invalidateTree();
		// The wrapping page needs to reflow so that scrollbars don't appear where they shouldn't
		taskEditorPart.getTaskEditorPage().reflow();
	}

	/**
	 * Provides the detail panel.
	 *
	 * @return The details panel.
	 */
	public CardDetailsPanel getDetailsPanel() {
		return getCardFigure().getDetailsPanel();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return getCardFigure().getDetailsPanel().getFieldsPanel();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getChildren()
	 */
	@Override
	public List<?> getModelChildren() {
		CardModel card = (CardModel)getModel();
		return card.getWrapper().getFieldAttributes();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		cardFoldingListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (ICardwallProperties.FOLDED.equals(evt.getPropertyName())) {
					boolean folded = ((Boolean)evt.getNewValue()).booleanValue();
					getDetailsPanel().setFolded(folded);
					getDetailsPanel().invalidateTree();
					refreshVisuals();
				}
			}
		};
		((CardModel)getModel()).addPropertyChangeListener(cardFoldingListener);
		labelMouseListener = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent me) {
				//
			}

			@Override
			public void mousePressed(MouseEvent me) {
				CardModel model = (CardModel)getModel();
				model.setFolded(!model.isFolded());
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				//
			}
		};
		getCardFigure().getDetailsPanel().getTitleLabel().addMouseListener(labelMouseListener);
		urlMouseListener = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {
				//
			}

			@Override
			public void mousePressed(MouseEvent me) {
				openTask();
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				//
			}
		};
		getCardFigure().getUrl().addMouseListener(urlMouseListener);
		urlMouseMotionListener = new URLMouseMotionListener();
		getCardFigure().getUrl().addMouseMotionListener(urlMouseMotionListener);

		createCardMouseListener = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {
				//
			}

			@Override
			public void mousePressed(MouseEvent me) {
				createNewcard();
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				//
			}
		};
		if (getCardFigure().hasChildren()) {
			getCardFigure().getNewCardLabel().addMouseListener(createCardMouseListener);
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		getCardFigure().getDetailsPanel().getTitleLabel().removeMouseListener(labelMouseListener);
		getCardFigure().getUrl().removeMouseListener(urlMouseListener);
		getCardFigure().getDetailsPanel().getTitleLabel().removeMouseMotionListener(urlMouseMotionListener);
		labelMouseListener = null;
		super.deactivate();
	}

	/**
	 * Open the task corresponding to the card.
	 */
	private void openTask() {
		CardModel cardModel = (CardModel)getModel();
		CardWrapper card = cardModel.getWrapper();
		TaskData taskData = card.getWrappedAttribute().getTaskData();
		TaskRepository repository = TasksUi.getRepositoryManager().getRepository(taskData.getConnectorKind(),
				taskData.getRepositoryUrl());
		if (repository != null) {
			TasksUiUtil.openTask(repository, card.getArtifactId());
		}
	}

	/**
	 * Create new card.
	 */
	private void createNewcard() {
		CardModel cardModel = (CardModel)getModel();
		CardWrapper card = cardModel.getWrapper();
		TaskData taskData = card.getRoot().getTaskData();
		String connectorKind = taskData.getConnectorKind();
		AbstractAgileRepositoryConnectorUI connector = MylynAgileUIActivator.getDefault()
				.getServiceTrackerCustomizer().getConnector(connectorKind);
		TaskRepository taskRepository = TasksUi.getRepositoryManager().getRepository(connectorKind,
				taskData.getRepositoryUrl());
		if (connector != null) {
			ICardMapping mapping = connector.getNewCardMapping(taskData, card.getArtifactId(),
					taskRepository, null);
			AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryConnector(connectorKind);
			AbstractTaskDataHandler taskDataHandler = repositoryConnector.getTaskDataHandler();

			TaskData newCardTaskData = new TaskData(taskDataHandler.getAttributeMapper(taskRepository),
					connectorKind, taskRepository.getRepositoryUrl(), ""); //$NON-NLS-1$

			try {
				boolean isInitialized = taskDataHandler.initializeTaskData(taskRepository, newCardTaskData,
						mapping, null);
				if (isInitialized) {
					TasksUiInternal.createAndOpenNewTask(newCardTaskData);
				}
			} catch (CoreException e) {
				MylynAgileUIActivator.log(e, true);
			}
		} else {
			MylynAgileUIActivator.log(MylynAgileUIMessages.getString(
					"PlanningTaskEditorPart.NoAgileUiConnectorFoundWithKind", connectorKind), true); //$NON-NLS-1$
		}
	}
}
