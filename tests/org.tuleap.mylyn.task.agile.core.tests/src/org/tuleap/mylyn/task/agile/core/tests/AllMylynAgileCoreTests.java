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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuleap.mylyn.task.internal.agile.core.tests.TaskAttributeWrapperTest;

/**
 * This class should be used to launch all the unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TaskAttributeWrapperTest.class })
public final class AllMylynAgileCoreTests {
	// Nothing to add, JUnit 4 Test Suite
}
