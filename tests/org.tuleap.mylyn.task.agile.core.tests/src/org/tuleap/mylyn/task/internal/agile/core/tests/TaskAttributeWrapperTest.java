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
package org.tuleap.mylyn.task.internal.agile.core.tests;

import java.util.Arrays;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the task attribute wrapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TaskAttributeWrapperTest {

	/**
	 * The type of some of the task attributes.
	 */
	private static final String TYPE1 = "t1"; //$NON-NLS-1$

	/**
	 * The type of some of the task attributes.
	 */
	private static final String TYPE2 = "t2"; //$NON-NLS-1$

	/**
	 * The tested task attribute wrapper.
	 */
	private TaskAttributeWrapper wrapper;

	/**
	 * This task attribute will contain several sub-task-attributes.
	 */
	private TaskAttribute containerTaskAttribute;

	/**
	 * The first task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute0;

	/**
	 * The second task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute1;

	/**
	 * The third task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute2;

	/**
	 * The fourth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute3;

	/**
	 * The fifth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute4;

	/**
	 * The sixth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute5;

	/**
	 * The seventh task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute6;

	/**
	 * The eighth task attribute of type TYPE1.
	 */
	private TaskAttribute type1TaskAttribute7;

	/**
	 * The number of task attribute with the type TYPE1.
	 */
	private final int type1TaskAttributeNumber = 8;

	/**
	 * The first task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute0;

	/**
	 * The second task attribute of type TYPE2.
	 */
	private TaskAttribute type2TaskAttribute1;

	/**
	 * The number of task attribute with the type TYPE2.
	 */
	private final int type2TaskAttributeNumber = 2;

	// Attributes c* have a null type

	/**
	 * The first task attribute of type null.
	 */
	private TaskAttribute typeNullTaskAttribute0;

	/**
	 * The second task attribute of type null.
	 */
	private TaskAttribute typeNullTaskAttribute1;

	/**
	 * The third task attribute of type null.
	 */
	private TaskAttribute typeNullTaskAttribute2;

	/**
	 * The number of task attribute with the type null.
	 */
	private final int typeNullTaskAttributeNumber = 3;

	/**
	 * An additional task attribute.
	 */
	private TaskAttribute other1;

	/**
	 * Another additional task attribute.
	 */
	private TaskAttribute other2;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		TaskRepository taskRepository = new TaskRepository("kind", "repository"); //$NON-NLS-1$ //$NON-NLS-2$
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData data = new TaskData(mapper, "kind", "repository", "id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		this.containerTaskAttribute = new TaskAttribute(data.getRoot(), "a"); //$NON-NLS-1$
		type1TaskAttribute0 = this.containerTaskAttribute.createAttribute("a-0"); //$NON-NLS-1$
		type1TaskAttribute1 = this.containerTaskAttribute.createAttribute("a-1"); //$NON-NLS-1$
		type1TaskAttribute2 = this.containerTaskAttribute.createAttribute("a-2"); //$NON-NLS-1$
		type1TaskAttribute3 = this.containerTaskAttribute.createAttribute("a-3"); //$NON-NLS-1$
		type1TaskAttribute4 = this.containerTaskAttribute.createAttribute("a-4"); //$NON-NLS-1$
		type1TaskAttribute5 = this.containerTaskAttribute.createAttribute("a-5"); //$NON-NLS-1$
		type1TaskAttribute6 = this.containerTaskAttribute.createAttribute("a-6"); //$NON-NLS-1$
		type1TaskAttribute7 = this.containerTaskAttribute.createAttribute("a-7"); //$NON-NLS-1$

		type1TaskAttribute0.getMetaData().setType(TYPE1);
		type1TaskAttribute1.getMetaData().setType(TYPE1);
		type1TaskAttribute2.getMetaData().setType(TYPE1);
		type1TaskAttribute3.getMetaData().setType(TYPE1);
		type1TaskAttribute4.getMetaData().setType(TYPE1);
		type1TaskAttribute5.getMetaData().setType(TYPE1);
		type1TaskAttribute6.getMetaData().setType(TYPE1);
		type1TaskAttribute7.getMetaData().setType(TYPE1);

		int i = 0;
		type1TaskAttribute0.setValue(String.valueOf(i++));
		type1TaskAttribute1.setValue(String.valueOf(i++));
		type1TaskAttribute2.setValue(String.valueOf(i++));
		type1TaskAttribute3.setValue(String.valueOf(i++));
		type1TaskAttribute4.setValue(String.valueOf(i++));
		type1TaskAttribute5.setValue(String.valueOf(i++));
		type1TaskAttribute6.setValue(String.valueOf(i++));
		type1TaskAttribute7.setValue(String.valueOf(i++));

		type2TaskAttribute0 = this.containerTaskAttribute.createAttribute("b-0"); //$NON-NLS-1$
		type2TaskAttribute1 = this.containerTaskAttribute.createAttribute("b-1"); //$NON-NLS-1$

		type2TaskAttribute0.getMetaData().setType(TYPE2);
		type2TaskAttribute1.getMetaData().setType(TYPE2);

		i = 0;
		type2TaskAttribute0.setValue(String.valueOf(i++));
		type2TaskAttribute1.setValue(String.valueOf(i++));

		typeNullTaskAttribute0 = this.containerTaskAttribute.createAttribute("c-0"); //$NON-NLS-1$
		typeNullTaskAttribute1 = this.containerTaskAttribute.createAttribute("c-1"); //$NON-NLS-1$
		typeNullTaskAttribute2 = this.containerTaskAttribute.createAttribute("c-2"); //$NON-NLS-1$

		i = 0;
		typeNullTaskAttribute0.setValue(String.valueOf(i++));
		typeNullTaskAttribute1.setValue(String.valueOf(i++));
		typeNullTaskAttribute2.setValue(String.valueOf(i++));

		other1 = new TaskAttribute(data.getRoot(), "other1"); //$NON-NLS-1$
		other2 = new TaskAttribute(data.getRoot(), "other2"); //$NON-NLS-1$

		wrapper = new TaskAttributeWrapper(this.containerTaskAttribute);
	}

	/**
	 * Test that the wrapper contains all the instances of the task attribute with the type TYPE1 and TYPE2
	 * but not those without a type or the container task attribute.
	 */
	@Test
	public void testContainsInstance() {
		assertTrue(wrapper.containsInstance(type1TaskAttribute0));
		assertTrue(wrapper.containsInstance(type1TaskAttribute1));
		assertTrue(wrapper.containsInstance(type1TaskAttribute2));
		assertTrue(wrapper.containsInstance(type1TaskAttribute3));
		assertTrue(wrapper.containsInstance(type1TaskAttribute4));
		assertTrue(wrapper.containsInstance(type1TaskAttribute5));
		assertTrue(wrapper.containsInstance(type1TaskAttribute6));
		assertTrue(wrapper.containsInstance(type1TaskAttribute7));
		assertTrue(wrapper.containsInstance(type2TaskAttribute0));
		assertTrue(wrapper.containsInstance(type2TaskAttribute1));

		assertFalse(wrapper.containsInstance(containerTaskAttribute));
		assertFalse(wrapper.containsInstance(other1));
		assertFalse(wrapper.containsInstance(other2));
	}

	/**
	 * Test that we have the expected number of children.
	 */
	@Test
	public void testCountChildren() {
		assertEquals(type1TaskAttributeNumber, wrapper.countChildren(TYPE1));
		assertEquals(type2TaskAttributeNumber, wrapper.countChildren(TYPE2));
		assertEquals(typeNullTaskAttributeNumber, wrapper.countChildren(null));
	}

	/**
	 * Test that elements of the wrong types are not moved.
	 */
	@Test
	public void checkThatElementsOfTheWrongTypeAreNotMoved() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type2TaskAttribute0), 1, TYPE1);
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving an element from 0 to 0.
	 */
	@Test
	public void testMoveOneElementFrom0To0() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0), 0, TYPE1);
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving an element from 0 to 1.
	 */
	@Test
	public void testMoveOneElementFrom0To1() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0), 1, TYPE1);
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving an element from 0 to the middle of the list.
	 */
	@Test
	public void testMoveOneElementFrom0ToMiddle() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0), 4, TYPE1);
		assertEquals("3", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving one element from the first position to the last.
	 */
	@Test
	public void testMoveOneElementFrom0ToLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0), 7, TYPE1);
		assertEquals("6", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving an element from the first position to after the last one.
	 */
	@Test
	public void testMoveOneElementFrom0ToAfterLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0), 8, TYPE1);
		assertEquals("7", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences to move the first and last elements to the beginning.
	 */
	@Test
	public void testMoveFirstAndLastElementsToBeginning() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0, type1TaskAttribute7), 0, TYPE1);
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the first and the last elements to the beginning with the wrong type.
	 */
	@Test
	public void testMoveFirstAndLastElementsToBeginningWithWrongType() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0, type1TaskAttribute7), 5, TYPE2);
		// Nothing must move
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$

		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$

		assertEquals("0", typeNullTaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", typeNullTaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", typeNullTaskAttribute2.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the first and last elements to the beginning with a type null.
	 */
	@Test
	public void testMoveFirstAndLastElementsToBeginningWithNullType() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0, type1TaskAttribute7), 5, null);
		// Nothing must move
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$

		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$

		assertEquals("0", typeNullTaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", typeNullTaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("2", typeNullTaskAttribute2.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the last and first elements to the beginning.
	 */
	@Test
	public void testMoveLastAndFirstElementsToBeginning() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute7, type1TaskAttribute0), 0, TYPE1);
		assertEquals("0", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the first and last elements to the middle.
	 */
	@Test
	public void testMoveFirstAndLastElementsToMiddle() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0, type1TaskAttribute7), 4, TYPE1);
		assertEquals("3", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the first and last element to the middle.
	 */
	@Test
	public void testMoveLastAndFirstElementsToMiddle() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute7, type1TaskAttribute0), 4, TYPE1);
		assertEquals("3", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the first and last elements after the last one.
	 */
	@Test
	public void testMoveFirstAndLastElementsAfterLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute0, type1TaskAttribute7), 8, TYPE1);
		assertEquals("6", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the consequences of moving the last and first elements after the last.
	 */
	@Test
	public void testMoveLastAndFirstElementsAfterLast() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute7, type1TaskAttribute0), 8, TYPE1);
		assertEquals("6", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("3", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the move of several elements.
	 */
	@Test
	public void testMoveSeveralElements() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute7, type1TaskAttribute0,
				type1TaskAttribute3), 5, TYPE1);
		assertEquals("3", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the move of several elements plus some irrelevant elements.
	 */
	@Test
	public void testMoveSeveralElementsPlusIrrelevantElements() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(type1TaskAttribute7, type1TaskAttribute0, other1,
				type1TaskAttribute3, this.containerTaskAttribute, other2), 5, TYPE1);
		assertEquals("3", type1TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("0", type1TaskAttribute1.getValue()); //$NON-NLS-1$
		assertEquals("1", type1TaskAttribute2.getValue()); //$NON-NLS-1$
		assertEquals("4", type1TaskAttribute3.getValue()); //$NON-NLS-1$
		assertEquals("2", type1TaskAttribute4.getValue()); //$NON-NLS-1$
		assertEquals("6", type1TaskAttribute5.getValue()); //$NON-NLS-1$
		assertEquals("7", type1TaskAttribute6.getValue()); //$NON-NLS-1$
		assertEquals("5", type1TaskAttribute7.getValue()); //$NON-NLS-1$
		assertEquals("0", type2TaskAttribute0.getValue()); //$NON-NLS-1$
		assertEquals("1", type2TaskAttribute1.getValue()); //$NON-NLS-1$
	}

}
