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
package org.tuleap.mylyn.task.internal.agile.ui.tests.cardwall.validator;

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

	/**
	 * Check integer strings are valid.
	 */
	@Test
	public void testAnIntegerStringIsValid() {
		assertNull(validator.isValid("1"));
	}

	/**
	 * Check double strings are valid.
	 */
	@Test
	public void testADoubleStringIsInvalid() {
		assertNotNull(validator.isValid("1.0"));
	}

	/**
	 * Check empty strings are invalid.
	 */
	@Test
	public void testAnEmptyStringIsInvalid() {
		assertNotNull(validator.isValid(""));
	}

	/**
	 * Check other strings are invalid.
	 */
	@Test
	public void testAStringIsInvalid() {
		assertNotNull(validator.isValid("three"));
	}

	/**
	 * Check doubles are invalid.
	 */
	@Test
	public void testADoubleIsInvalid() {
		assertNotNull(validator.isValid(Double.MAX_VALUE));
	}

	/**
	 * Check floats are invalid.
	 */
	@Test
	public void testAFloatIsInvalid() {
		assertNotNull(validator.isValid(Float.MAX_VALUE));
	}

	/**
	 * Check longs are valid.
	 */
	@Test
	public void testALongIsValid() {
		assertNull(validator.isValid(Long.MAX_VALUE));
	}

	/**
	 * Check integers are valid.
	 */
	@Test
	public void testAnIntegerIsValid() {
		assertNull(validator.isValid(Integer.MAX_VALUE));
	}

	/**
	 * Check shorts are valid.
	 */
	@Test
	public void testAShortIsValid() {
		assertNull(validator.isValid(Short.MAX_VALUE));
	}

	/**
	 * Check bytes are valid.
	 */
	@Test
	public void testAByteIsValid() {
		assertNull(validator.isValid(Byte.MAX_VALUE));
	}

	/**
	 * Check objects are invalid.
	 */
	@Test
	public void testAnObjectIsInvalid() {
		assertNotNull(validator.isValid(new Object()));
	}

	/**
	 * Check <code>null</code> is invalid.
	 */
	@Test
	public void testNullIsInvalid() {
		assertNotNull(validator.isValid(null));
	}

}
