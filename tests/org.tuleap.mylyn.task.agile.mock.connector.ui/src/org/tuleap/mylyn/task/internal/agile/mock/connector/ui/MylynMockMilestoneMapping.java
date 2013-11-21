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
package org.tuleap.mylyn.task.internal.agile.mock.connector.ui;

import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;

/**
 * The mapping of the submilestone.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockMilestoneMapping extends TaskMapping implements IMilestoneMapping {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.IMilestoneMapping#getParentMilestoneId()
	 */
	@Override
	public String getParentMilestoneId() {
		return "A/B/C"; //$NON-NLS-1$
	}

}
