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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

/**
 * EditPolicy used for selecting card fields and displaying proper feedback.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardFieldSelectionEditPolicy extends NonResizableEditPolicy {

	/**
	 * We override the default behavior to only display a rectangle without any points at its edges.
	 *
	 * @return A list containing only a move handle.
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createSelectionHandles()
	 */
	@Override
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		createMoveHandle(list);
		return list;
	}

	/**
	 * Drag of fields is not allowed so this returns false.
	 *
	 * @return Systematically false.
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#isDragAllowed()
	 */
	@Override
	public boolean isDragAllowed() {
		return false;
	}
}
