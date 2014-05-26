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
import org.tuleap.mylyn.task.agile.core.ICardMapping;

/**
 * The mapping of the backlogItem.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class MylynMockCardMapping extends TaskMapping implements ICardMapping {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.IBacklogItemMapping#getParentMilestoneId()
	 */
	@Override
	public String getParentCard() {
		return "A/B/C"; //$NON-NLS-1$
	}

}
