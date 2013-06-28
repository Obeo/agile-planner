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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;

/**
 * Content Provider for Scopes.
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
		if (inputElement instanceof TaskAttribute) {
			TaskAttribute ta = (TaskAttribute)inputElement;
			List<Object> children = new ArrayList<Object>();
			for (TaskAttribute child : ta.getAttributes().values()) {
				if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(child.getMetaData().getType())) {
					children.add(child);
				}
			}
			return children.toArray();
		}
		return null;
	}
}
