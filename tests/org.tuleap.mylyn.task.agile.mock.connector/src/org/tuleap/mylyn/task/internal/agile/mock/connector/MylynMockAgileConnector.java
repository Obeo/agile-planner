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
package org.tuleap.mylyn.task.internal.agile.mock.connector;

import org.tuleap.mylyn.task.agile.core.AbstractAgileRepositoryConnector;
import org.tuleap.mylyn.task.agile.mock.connector.util.IMylynMockConnectorConstants;

/**
 * The agile connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynMockAgileConnector extends AbstractAgileRepositoryConnector {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.core.AbstractAgileRepositoryConnector#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return IMylynMockConnectorConstants.CONNECTOR_KIND;
	}

}
