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
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part.CardwallEditPartFactory;

/**
 * Editor part containing the cards from the card wall.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPart extends AbstractTaskEditorPart implements ITaskAttributeChangeListener {

	/**
	 * The edited cardwall wrapper.
	 */
	private CardwallWrapper wrapper;

	/**
	 * The GEF viewer.
	 */
	private GraphicalViewer viewer;

	/**
	 * The GEF edit domain.
	 */
	private EditDomain editDomain;

	/**
	 * The GEF selection synchronizer.
	 */
	private SelectionSynchronizer synchronizer;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {

		editDomain = new EditDomain();
		viewer = new ScrollingGraphicalViewer();

		setControl(viewer.createControl(parent));

		editDomain.addViewer(viewer);

		viewer.getControl().setBackground(ColorConstants.listBackground);
		viewer.setEditPartFactory(new CardwallEditPartFactory());

		synchronizer = new SelectionSynchronizer();
		synchronizer.addViewer(viewer);
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		IWorkbenchPartSite site = activeEditor.getSite();
		site.setSelectionProvider(viewer);

		wrapper = new CardwallWrapper(getTaskData().getRoot());
		wrapper.addListener(this);

		viewer.setContents(wrapper);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.data.ITaskAttributeChangeListener#attributeChanged(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public void attributeChanged(TaskAttribute attribute) {
		// FIXME: This causes an issue when called.
		getModel().attributeChanged(wrapper.getWrappedAttribute());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.forms.AbstractFormPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (wrapper != null) {
			wrapper.removeListener(this);
		}
	}

}
