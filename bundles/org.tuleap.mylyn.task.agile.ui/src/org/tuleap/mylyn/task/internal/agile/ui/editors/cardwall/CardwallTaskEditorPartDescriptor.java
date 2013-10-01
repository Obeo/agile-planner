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

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Descriptor of the CardwallTaskEditorPart.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CardwallTaskEditorPartDescriptor extends TaskEditorPartDescriptor {

	/**
	 * Default constructor, encapsulating construction with the relevant descriptor identifier.
	 */
	public CardwallTaskEditorPartDescriptor() {
		super(IMylynAgileUIConstants.CARDWALL_TASK_EDITOR_PART_DESC_ID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor#createPart()
	 */
	@Override
	public AbstractTaskEditorPart createPart() {
		return new CardwallTaskEditorPart();
	}

}
