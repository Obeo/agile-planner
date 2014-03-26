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
package org.tuleap.mylyn.task.agile.core.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.BurndownDataTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.BurndownMapperTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.CardFilterTests;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.CardwallWrapperTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.MilestonePlanningWrapperMovingBacklogItemsTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.MilestonePlanningWrapperTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.SubMilestoneWrapperTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.TaskAttributesTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.data.TopPlanningMapperTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.diff.ChangeTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.diff.ClassicLCSTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.diff.DiffTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.FunctionalChangeTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.ItemChangeSetTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.ItemChangeTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.MultiListDiffTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.MultiListMergerTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.merge.PlanningDataModelMergerTest;
import org.tuleap.mylyn.task.internal.agile.core.tests.util.ListUtilTest;

/**
 * This class should be used to launch all the unit tests.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		// data
		BurndownDataTest.class, BurndownMapperTest.class, CardFilterTests.class, CardwallWrapperTest.class,
		MilestonePlanningWrapperMovingBacklogItemsTest.class, MilestonePlanningWrapperTest.class,
		SubMilestoneWrapperTest.class, TaskAttributesTest.class, TopPlanningMapperTest.class,
		// diff
		ChangeTest.class, ClassicLCSTest.class, DiffTest.class,
		// merge
		FunctionalChangeTest.class, ItemChangeSetTest.class, ItemChangeTest.class, MultiListDiffTest.class,
		MultiListMergerTest.class, PlanningDataModelMergerTest.class,
		// util
		ListUtilTest.class })
public final class AllMylynAgileCoreTests {

	/**
	 * The constructor.
	 */
	private AllMylynAgileCoreTests() {
		// prevent instantiation
	}

	/**
	 * Launches the test with the given arguments.
	 *
	 * @param args
	 *            Arguments of the testCase.
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 *
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllMylynAgileCoreTests.class);
	}
}
