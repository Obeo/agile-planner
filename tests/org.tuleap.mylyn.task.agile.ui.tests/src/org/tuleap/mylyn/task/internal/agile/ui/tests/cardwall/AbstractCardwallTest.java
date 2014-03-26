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
package org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;

/**
 * Super-class of test classes that need a cardwall to operate.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractCardwallTest {

	/**
	 * The wrapped task data.
	 */
	protected TaskData taskData;

	/**
	 * The cardwall wrapper.
	 */
	protected CardwallWrapper cardwall;

	/**
	 * Model listener used for tests.
	 *
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	protected class TestModelListener implements PropertyChangeListener {
		/**
		 * Cache of received events.
		 */
		private final List<PropertyChangeEvent> eventsReceived;

		/**
		 * Constructor.
		 */
		public TestModelListener() {
			eventsReceived = new ArrayList<PropertyChangeEvent>();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			eventsReceived.add(evt);
		}

		/**
		 * Provides an unmodifiable view of events received by this listener.
		 *
		 * @return An unmodifiable view of events received by this listener.
		 */
		public List<PropertyChangeEvent> getEvents() {
			return Collections.unmodifiableList(eventsReceived);
		}

		/**
		 * Clear all received events.
		 */
		public void reset() {
			eventsReceived.clear();
		}
	}

	/**
	 * Basic set-up for tests that require a cardwall. It creates an empty cardwall in the relevatn instance
	 * variable.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository";
		String connectorKind = "kind";
		String taskId = "id";
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		cardwall = new CardwallWrapper(taskData.getRoot());
	}
}
