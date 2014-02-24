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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardwallEditPartFactory;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util.FilterCardsContributionItem;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Editor part containing the cards from the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPart extends AbstractTaskEditorPart implements ITaskAttributeChangeListener {

	/**
	 * Selection provider to use with the cardwall. Introduced to solve Eclipse Agile Planner:Requests-6285.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class CardwallSelectionProvider implements ISelectionProvider {
		/**
		 * The current selectionProvider.
		 */
		private final ISelectionProvider selectionProvider;

		/**
		 * The cardwall viewer.
		 */
		private final GraphicalViewer viewer;

		/**
		 * Constructor.
		 * 
		 * @param selectionProvider
		 *            The current selection provider
		 * @param viewer
		 *            The veiwer
		 */
		private CardwallSelectionProvider(ISelectionProvider selectionProvider, GraphicalViewer viewer) {
			this.selectionProvider = selectionProvider;
			this.viewer = viewer;
		}

		@Override
		public void setSelection(ISelection selection) {
			if (selection instanceof EditPart) {
				viewer.setSelection(selection);
			}
			if (selectionProvider != null) {
				selectionProvider.setSelection(selection);
			}
		}

		@Override
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			viewer.removeSelectionChangedListener(listener);
			if (selectionProvider != null) {
				selectionProvider.removeSelectionChangedListener(listener);
			}
		}

		@Override
		public ISelection getSelection() {
			if (selectionProvider != null) {
				return selectionProvider.getSelection();
			}
			return viewer.getSelection();
		}

		@Override
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			viewer.addSelectionChangedListener(listener);
			if (selectionProvider != null) {
				selectionProvider.addSelectionChangedListener(listener);
			}
		}
	}

	/**
	 * The edited cardwall model.
	 */
	private CardwallModel cardwallModel;

	/**
	 * The GEF viewer.
	 */
	private GraphicalViewer viewer;

	/**
	 * The GEF edit domain.
	 */
	private EditDomain editDomain;

	/**
	 * The listener for filtering the cardwall content.
	 */
	private ModifyListener filterModifyListener;

	/**
	 * Constructor.
	 */
	public CardwallTaskEditorPart() {
		setPartName(MylynAgileUIMessages.getString("CardwallTaskEditorPart.SectionTitle")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		Section section = createSection(parent, toolkit, true);
		Composite cardwallComposite = toolkit.createComposite(section);
		cardwallComposite.setLayout(new GridLayout(1, false));

		editDomain = new EditDomain();
		viewer = new ScrollingGraphicalViewer();

		Control gefControl = viewer.createControl(cardwallComposite);
		gefControl.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		editDomain.addViewer(viewer);

		viewer.getControl().setBackground(ColorConstants.listBackground);
		viewer.setEditPartFactory(new CardwallEditPartFactory(this));

		// synchronizer = new SelectionSynchronizer();
		// synchronizer.addViewer(viewer);
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		IWorkbenchPartSite site = activeEditor.getSite();

		// Eclipse Agile Planner:Requests-6285
		site.setSelectionProvider(new CardwallSelectionProvider(site.getSelectionProvider(), viewer));

		CardwallWrapper wrapper = new CardwallWrapper(getTaskData().getRoot());
		wrapper.addListener(this);
		cardwallModel = new CardwallModel(wrapper);

		viewer.setContents(cardwallModel);

		toolkit.paintBordersFor(cardwallComposite);
		section.setClient(cardwallComposite);
		setSection(toolkit, section);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		getModel().attributeChanged(attribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.forms.AbstractFormPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (cardwallModel != null) {
			cardwallModel.getWrapper().removeListener(this);
		}
		filterModifyListener = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#fillToolBar(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void fillToolBar(ToolBarManager toolBarManager) {
		filterModifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text)e.getSource();
				cardwallModel.setFilter(text.getText());
			}
		};
		// FIXME It'd be better not to pass the listener to the constructor...
		FilterCardsContributionItem item = new FilterCardsContributionItem(filterModifyListener);
		toolBarManager.add(item);
	}

}
