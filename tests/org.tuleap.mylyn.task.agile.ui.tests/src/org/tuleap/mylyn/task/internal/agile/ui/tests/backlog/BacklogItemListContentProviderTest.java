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
package org.tuleap.mylyn.task.internal.agile.ui.tests.backlog;

import java.util.Arrays;

import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.BacklogItemListContentProvider;
import org.tuleap.mylyn.task.internal.agile.ui.editors.planning.IBacklog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Tests of {@link BacklogItemListContentProvider}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BacklogItemListContentProviderTest extends AbstractBacklogTest {

	@Test
	public void testGetElementsOnInvalidObjects() {
		BacklogItemListContentProvider provider = new BacklogItemListContentProvider();
		assertNull(provider.getElements(null));
		assertNull(provider.getElements(provider));
	}

	@Test
	public void testGetElementsOnIBacklog() {
		BacklogItemListContentProvider provider = new BacklogItemListContentProvider();
		final BacklogItemWrapper bi0 = planning.addBacklogItem("bi0");
		final BacklogItemWrapper bi1 = planning.addBacklogItem("bi1");
		final BacklogItemWrapper bi2 = planning.addBacklogItem("bi2");
		IBacklog backlog = new IBacklog() {
			@Override
			public MilestonePlanningWrapper getMilestonePlanning() {
				fail("getMilestonePlanning should not be called.");
				return null;
			}

			@Override
			public String getMilestoneId() {
				fail("getMilestoneId should not be called.");
				return null;
			}

			@Override
			public Iterable<BacklogItemWrapper> getBacklogItems() {
				return Arrays.asList(bi0, bi1, bi2);
			}
		};
		assertArrayEquals(new Object[] {bi0, bi1, bi2 }, provider.getElements(backlog));
	}

}
