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

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;

/**
 * The Page for the PlanningTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * Flag to indicate whether standard mylyn actions should be created by this page.
	 */
	private boolean createActionsInToolbar;

	/**
	 * Constructor, which delegates to the matching super constructor.
	 * 
	 * @param editor
	 *            The parent TaskEditor.
	 * @param connectorKind
	 *            The related connector kind.
	 * @param createActionsInToolbar
	 *            Flag to indicate whether standard mylyn actions should be created by this page.
	 */
	public PlanningTaskEditorPage(TaskEditor editor, String connectorKind, boolean createActionsInToolbar) {
		super(editor, connectorKind);
		this.createActionsInToolbar = createActionsInToolbar;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		// We just want to display the planning editor in this tab
		Set<TaskEditorPartDescriptor> descriptors = Sets.newLinkedHashSet();
		descriptors.add(new PlanningTaskEditorPartDescriptor());
		return descriptors;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#fillToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void fillToolBar(IToolBarManager toolBarManager) {
		if (createActionsInToolbar) {
			super.fillToolBar(toolBarManager);
		}
	}
}
