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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.part;

import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallModel;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.SwimlaneModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests of the {@link SwimlaneEditPart} class.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class SwimlaneEditPartTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Test the basic behavior of SwimlaneEditPart.
	 */
	@Test
	public void testWithoutCard() {
		SwimlaneEditPart part = new SwimlaneEditPart();
		CardwallWrapper cardwall = new CardwallWrapper(taskData.getRoot());
		CardwallModel cardwallModel = new CardwallModel(cardwall);
		SwimlaneWrapper swimlane = cardwall.addSwimlane("swi1");
		SwimlaneModel swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		part.setModel(swimlaneModel);
		assertSame(swimlaneModel, part.getModel());
		List<?> children = part.getModelChildren();
		assertEquals(1, children.size());
		assertSame(swimlane, children.get(0));
	}

	/**
	 * Test the basic behavior of SwimlaneEditPart.
	 */
	@Test
	public void testWithCards() {
		SwimlaneEditPart part = new SwimlaneEditPart();
		CardwallWrapper cardwall = new CardwallWrapper(taskData.getRoot());
		CardwallModel cardwallModel = new CardwallModel(cardwall);
		SwimlaneWrapper swimlane = cardwall.addSwimlane("swi1");
		SwimlaneModel swimlaneModel = new SwimlaneModel(cardwallModel, swimlane);
		part.setModel(swimlaneModel);
		assertSame(swimlaneModel, part.getModel());
		List<?> children = part.getModelChildren();
		assertEquals(1, children.size());
		assertSame(swimlane, children.get(0));
	}

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}
}
