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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;

/**
 * The Page for the CardwallTaskEditor. This page parameterizes the editor to display only the relevant tabs
 * and parts.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * Constructor, which delegates to the matching super constructor.
	 * 
	 * @param editor
	 *            The parent TaskEditor.
	 * @param connectorKind
	 *            The related connector kind.
	 */
	public CardwallTaskEditorPage(TaskEditor editor, String connectorKind) {
		super(editor, connectorKind);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> descriptors = super.createPartDescriptors();

		this.removeAllParts(descriptors);

		descriptors.add(new CardwallTaskEditorPartDescriptor());

		return descriptors;
	}

	/**
	 * Removes the parts from the set of part descriptors.
	 * 
	 * @param parts
	 *            the part descriptors.
	 */
	private void removeAllParts(Set<TaskEditorPartDescriptor> parts) {
		Iterator<TaskEditorPartDescriptor> iterator = parts.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}
}
