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

import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.tuleap.mylyn.task.internal.agile.ui.util.IMylynAgileUIConstants;

/**
 * Descriptor of the PlanningTaskEditorPart.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PlanningTaskEditorPartDescriptor extends TaskEditorPartDescriptor {

	/**
	 * Default constructor, encapsulating construction with the relevant descriptor identifier.
	 */
	public PlanningTaskEditorPartDescriptor() {
		super(IMylynAgileUIConstants.PLANNING_TASK_EDITOR_PART_DESC_ID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor#createPart()
	 */
	@Override
	public AbstractTaskEditorPart createPart() {
		return new PlanningTaskEditorPart();
	}

}
