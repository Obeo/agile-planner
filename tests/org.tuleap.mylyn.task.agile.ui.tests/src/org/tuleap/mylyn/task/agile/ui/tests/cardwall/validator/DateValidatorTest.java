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
package org.tuleap.mylyn.task.agile.ui.tests.cardwall.validator;

import java.util.Date;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator.DateValidator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests of DoubleValidator.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DateValidatorTest {

	/**
	 * The validator under test.
	 */
	private DateValidator validator = new DateValidator();

	@Test
	public void testADateValid() {
		assertNull(validator.isValid(new Date()));
	}

	@Test
	public void testNullIsInvalid() {
		assertNotNull(validator.isValid(null));
	}

	@Test
	public void testAStringIsInvalid() {
		assertNotNull(validator.isValid(new Date().toString()));
	}

}
