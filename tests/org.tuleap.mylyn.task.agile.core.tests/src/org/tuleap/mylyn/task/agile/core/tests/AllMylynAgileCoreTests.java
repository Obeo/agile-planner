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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.mylyn.commons.sdk.util.ManagedTestSuite;
import org.tuleap.mylyn.task.internal.agile.core.tests.SomeTest;

/**
 * This class should be used to launch all the unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
@SuppressWarnings("restriction")
public final class AllMylynAgileCoreTests {
	/**
	 * The constructor.
	 */
	private AllMylynAgileCoreTests() {
		// prevent instantiation
	}

	/**
	 * Returns the tests suite to run.
	 * 
	 * @return The tests suite to run
	 */
	public static Test suite() {
		TestSuite suite = new ManagedTestSuite(AllMylynAgileCoreTests.class.getName());
		suite.addTestSuite(SomeTest.class);
		return suite;
	}
}
