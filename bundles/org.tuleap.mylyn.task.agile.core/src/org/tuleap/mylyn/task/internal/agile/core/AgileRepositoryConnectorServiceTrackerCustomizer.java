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
package org.tuleap.mylyn.task.internal.agile.core;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.tuleap.mylyn.task.agile.core.AbstractAgileRepositoryConnector;

/**
 * This class keeps track of registered Agile Repository Connectors.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AgileRepositoryConnectorServiceTrackerCustomizer implements ServiceTrackerCustomizer<Object, AbstractAgileRepositoryConnector> {

	/**
	 * The registered agile repository connectors.
	 */
	private final Map<String, AbstractAgileRepositoryConnector> connectors = new HashMap<String, AbstractAgileRepositoryConnector>();

	/**
	 * The context of the bundle.
	 */
	private final BundleContext context;

	/**
	 * The constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context
	 */
	public AgileRepositoryConnectorServiceTrackerCustomizer(BundleContext bundleContext) {
		this.context = bundleContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AbstractAgileRepositoryConnector addingService(ServiceReference<Object> reference) {
		Object service = this.context.getService(reference);
		if (service instanceof AbstractAgileRepositoryConnector) {
			AbstractAgileRepositoryConnector connector = (AbstractAgileRepositoryConnector)service;
			this.connectors.put(connector.getConnectorKind(), connector);
			return (AbstractAgileRepositoryConnector)service;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note:</b> Has no effect if the given reference doesn't reference an
	 * {@link AbstractAgileRepositoryConnector}.
	 * </p>
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void modifiedService(ServiceReference<Object> reference, AbstractAgileRepositoryConnector service) {
		Object newService = this.context.getService(reference);
		if (newService instanceof AbstractAgileRepositoryConnector) {
			AbstractAgileRepositoryConnector connector = (AbstractAgileRepositoryConnector)newService;
			this.connectors.put(connector.getConnectorKind(), connector);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note:</b> Has no effect if the given reference doesn't reference an
	 * {@link AbstractAgileRepositoryConnector}.
	 * </p>
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<Object> reference, AbstractAgileRepositoryConnector service) {
		Object newService = this.context.getService(reference);
		if (newService instanceof AbstractAgileRepositoryConnector) {
			AbstractAgileRepositoryConnector connector = (AbstractAgileRepositoryConnector)newService;
			this.connectors.remove(connector.getConnectorKind());
		}
	}

	/**
	 * Provides the registered connector for the given connector kind, or {@code null} if no connector is
	 * registered for this kind.
	 * 
	 * @param connectorKind
	 *            The kind of connector being looked for.
	 * @return The connector registered for the given kind, or {@code null} if no connector is registered for
	 *         this kind.
	 */
	public AbstractAgileRepositoryConnector getConnector(String connectorKind) {
		return this.connectors.get(connectorKind);
	}
}
