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

import com.google.common.collect.Iterables;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content Provider for Milestones and Backlogs.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
final class BacklogItemListContentProvider implements IStructuredContentProvider {

	@Override
	public void inputChanged(Viewer pViewer, Object oldInput, Object newInput) {
		//
	}

	@Override
	public void dispose() {
		//
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IBacklogItemContainer) {
			IBacklogItemContainer container = (IBacklogItemContainer)inputElement;
			return Iterables.toArray(container.getBacklogItems(), Object.class);
		}
		return null;
	}
}
