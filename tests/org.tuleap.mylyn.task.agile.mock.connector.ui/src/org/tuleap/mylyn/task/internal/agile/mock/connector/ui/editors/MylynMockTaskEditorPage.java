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
package org.tuleap.mylyn.task.internal.agile.mock.connector.ui.editors;

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.tuleap.mylyn.task.agile.mock.connector.util.IMylynMockConnectorConstants;

/**
 * The generic editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * The constructor.
	 * 
	 * @param editor
	 *            The editor
	 */
	public MylynMockTaskEditorPage(TaskEditor editor) {
		super(editor, IMylynMockConnectorConstants.CONNECTOR_KIND);
		this.setNeedsPrivateSection(false);
		this.setNeedsSubmitButton(true);
	}

}
