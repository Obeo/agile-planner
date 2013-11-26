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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.validator.IntegerValidator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests of DoubleValidator.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class IntegerValidatorTest {

	/**
	 * The validator under test.
	 */
	private IntegerValidator validator = new IntegerValidator();

	@Test
	public void testAnIntegerStringIsValid() {
		assertNull(validator.isValid("1"));
	}

	@Test
	public void testADoubleStringIsInvalid() {
		assertNotNull(validator.isValid("1.0"));
	}

	@Test
	public void testAnEmptyStringIsInvalid() {
		assertNotNull(validator.isValid(""));
	}

	@Test
	public void testAStringIsInvalid() {
		assertNotNull(validator.isValid("three"));
	}

	@Test
	public void testADoubleIsInvalid() {
		assertNotNull(validator.isValid(Double.MAX_VALUE));
	}

	@Test
	public void testAFloatIsInvalid() {
		assertNotNull(validator.isValid(Float.MAX_VALUE));
	}

	@Test
	public void testALongIsValid() {
		assertNull(validator.isValid(Long.MAX_VALUE));
	}

	@Test
	public void testAnIntegerIsValid() {
		assertNull(validator.isValid(Integer.MAX_VALUE));
	}

	@Test
	public void testAShortIsValid() {
		assertNull(validator.isValid(Short.MAX_VALUE));
	}

	@Test
	public void testAByteIsValid() {
		assertNull(validator.isValid(Byte.MAX_VALUE));
	}

	@Test
	public void testAnObjectIsInvalid() {
		assertNotNull(validator.isValid(new Object()));
	}

	@Test
	public void testNullIsInvalid() {
		assertNotNull(validator.isValid(null));
	}

}
