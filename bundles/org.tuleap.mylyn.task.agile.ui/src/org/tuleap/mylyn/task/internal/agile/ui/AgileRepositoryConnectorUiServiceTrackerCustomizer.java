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
package org.tuleap.mylyn.task.internal.agile.ui;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;

/**
 * This class keeps track of registered UI Agile Repository Connectors.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AgileRepositoryConnectorUiServiceTrackerCustomizer implements ServiceTrackerCustomizer<Object, AbstractAgileRepositoryConnectorUI> {

	/**
	 * The registered agile repository connectors.
	 */
	private final Map<String, AbstractAgileRepositoryConnectorUI> connectors = new HashMap<String, AbstractAgileRepositoryConnectorUI>();

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
	public AgileRepositoryConnectorUiServiceTrackerCustomizer(BundleContext bundleContext) {
		this.context = bundleContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AbstractAgileRepositoryConnectorUI addingService(ServiceReference<Object> reference) {
		Object service = this.context.getService(reference);
		if (service instanceof AbstractAgileRepositoryConnectorUI) {
			AbstractAgileRepositoryConnectorUI connector = (AbstractAgileRepositoryConnectorUI)service;
			this.connectors.put(connector.getConnectorKind(), connector);
			return connector;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note:</b> Has no effect if the given reference doesn't reference an
	 * {@link AbstractAgileRepositoryConnectorUI}.
	 * </p>
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void modifiedService(ServiceReference<Object> reference, AbstractAgileRepositoryConnectorUI service) {
		Object newService = this.context.getService(reference);
		if (newService instanceof AbstractAgileRepositoryConnectorUI) {
			AbstractAgileRepositoryConnectorUI connector = (AbstractAgileRepositoryConnectorUI)newService;
			this.connectors.put(connector.getConnectorKind(), connector);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note:</b> Has no effect if the given reference doesn't reference an
	 * {@link AbstractAgileRepositoryConnectorUI}.
	 * </p>
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<Object> reference, AbstractAgileRepositoryConnectorUI service) {
		Object newService = this.context.getService(reference);
		if (newService instanceof AbstractAgileRepositoryConnectorUI) {
			AbstractAgileRepositoryConnectorUI connector = (AbstractAgileRepositoryConnectorUI)newService;
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
	public AbstractAgileRepositoryConnectorUI getConnector(String connectorKind) {
		return this.connectors.get(connectorKind);
	}
}
