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
package org.tuleap.mylyn.task.agile.ui.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuleap.mylyn.task.internal.agile.ui.tests.MylynAgileUIActivatorTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.backlog.BacklogItemListContentProviderTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.backlog.BacklogModelTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.model.CardwallModelTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.validator.DateValidatorTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.validator.DoubleValidatorTest;
import org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.validator.IntegerValidatorTest;

/**
 * This class should be used to launch all the unit tests.
 *
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	// editors.cardwall.part
	// Removed because does not run in jenkins
	// due to same package in different projects trick...
	// SwimlaneEditPartTest.class,
	// backlog
	BacklogItemListContentProviderTest.class, BacklogModelTest.class,
	// cardwall.model
	CardwallModelTest.class,
	// cardwall.validator
	DateValidatorTest.class, DoubleValidatorTest.class, IntegerValidatorTest.class,
	//
	MylynAgileUIActivatorTest.class })
public final class AllMylynAgileUITests {

	/**
	 * The constructor.
	 */
	private AllMylynAgileUITests() {
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
		return new JUnit4TestAdapter(AllMylynAgileUITests.class);
	}
}
