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

// CHECKSTYLE:OFF

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.util.TaskAttributeWrapper;

/**
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("all")
public class TaskAttributeWrapperTest {

	private TaskAttributeWrapper wrapper;

	private TaskAttribute a;

	private TaskAttribute a0;

	private TaskAttribute a1;

	private TaskAttribute a2;

	private TaskAttribute a3;

	private TaskAttribute a4;

	private TaskAttribute a5;

	private TaskAttribute a6;

	private TaskAttribute a7;

	private TaskAttribute b0;

	private TaskAttribute b1;

	private String TYPE1 = "t1";

	private String TYPE2 = "t2";

	@Test
	public void testMoveOneElementFrom0To0() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0), 0, TYPE1);
		assertEquals("0", a0.getValue());
		assertEquals("1", a1.getValue());
		assertEquals("2", a2.getValue());
		assertEquals("3", a3.getValue());
		assertEquals("4", a4.getValue());
		assertEquals("5", a5.getValue());
		assertEquals("6", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveOneElementFrom0To1() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0), 1, TYPE1);
		assertEquals("0", a0.getValue());
		assertEquals("1", a1.getValue());
		assertEquals("2", a2.getValue());
		assertEquals("3", a3.getValue());
		assertEquals("4", a4.getValue());
		assertEquals("5", a5.getValue());
		assertEquals("6", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveOneElementFrom0ToMiddle() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0), 4, TYPE1);
		assertEquals("3", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("4", a4.getValue());
		assertEquals("5", a5.getValue());
		assertEquals("6", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveOneElementFrom0ToLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0), 7, TYPE1);
		assertEquals("6", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("3", a4.getValue());
		assertEquals("4", a5.getValue());
		assertEquals("5", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveOneElementFrom0ToAfterLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0), 8, TYPE1);
		assertEquals("7", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("3", a4.getValue());
		assertEquals("4", a5.getValue());
		assertEquals("5", a6.getValue());
		assertEquals("6", a7.getValue());
	}

	@Test
	public void testMoveFirstAndLastElementsToBeginning() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0, a7), 0, TYPE1);
		assertEquals("0", a0.getValue());
		assertEquals("2", a1.getValue());
		assertEquals("3", a2.getValue());
		assertEquals("4", a3.getValue());
		assertEquals("5", a4.getValue());
		assertEquals("6", a5.getValue());
		assertEquals("7", a6.getValue());
		assertEquals("1", a7.getValue());
	}

	@Test
	public void testMoveLastAndFirstElementsToBeginning() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(a7, a0), 0, TYPE1);
		assertEquals("0", a0.getValue());
		assertEquals("2", a1.getValue());
		assertEquals("3", a2.getValue());
		assertEquals("4", a3.getValue());
		assertEquals("5", a4.getValue());
		assertEquals("6", a5.getValue());
		assertEquals("7", a6.getValue());
		assertEquals("1", a7.getValue());
	}

	@Test
	public void testMoveFirstAndLastElementsToMiddle() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0, a7), 4, TYPE1);
		assertEquals("3", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("5", a4.getValue());
		assertEquals("6", a5.getValue());
		assertEquals("7", a6.getValue());
		assertEquals("4", a7.getValue());
	}

	@Test
	public void testMoveLastAndFirstElementsToMiddle() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(a7, a0), 4, TYPE1);
		assertEquals("3", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("5", a4.getValue());
		assertEquals("6", a5.getValue());
		assertEquals("7", a6.getValue());
		assertEquals("4", a7.getValue());
	}

	@Test
	public void testMoveFirstAndLastElementsAfterLast() {
		wrapper.moveElementsSortedByValue(Arrays.asList(a0, a7), 8, TYPE1);
		assertEquals("6", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("3", a4.getValue());
		assertEquals("4", a5.getValue());
		assertEquals("5", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveLastAndFirstElementsAfterLast() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(a7, a0), 8, TYPE1);
		assertEquals("6", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("2", a3.getValue());
		assertEquals("3", a4.getValue());
		assertEquals("4", a5.getValue());
		assertEquals("5", a6.getValue());
		assertEquals("7", a7.getValue());
	}

	@Test
	public void testMoveSeveralElements() {
		// To check that the original ordering of the moved elements is stable
		wrapper.moveElementsSortedByValue(Arrays.asList(a7, a0, a3), 5, TYPE1);
		assertEquals("3", a0.getValue());
		assertEquals("0", a1.getValue());
		assertEquals("1", a2.getValue());
		assertEquals("4", a3.getValue());
		assertEquals("2", a4.getValue());
		assertEquals("6", a5.getValue());
		assertEquals("7", a6.getValue());
		assertEquals("5", a7.getValue());
	}

	@Before
	public void setup() {
		TaskRepository taskRepository = new TaskRepository("kind", "repository");
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		TaskData data = new TaskData(mapper, "kind", "repository", "id");
		a = new TaskAttribute(data.getRoot(), "a");
		a0 = a.createAttribute("a-0");
		a1 = a.createAttribute("a-1");
		a2 = a.createAttribute("a-2");
		a3 = a.createAttribute("a-3");
		a4 = a.createAttribute("a-4");
		a5 = a.createAttribute("a-5");
		a6 = a.createAttribute("a-6");
		a7 = a.createAttribute("a-7");

		a0.getMetaData().setType(TYPE1);
		a1.getMetaData().setType(TYPE1);
		a2.getMetaData().setType(TYPE1);
		a3.getMetaData().setType(TYPE1);
		a4.getMetaData().setType(TYPE1);
		a5.getMetaData().setType(TYPE1);
		a6.getMetaData().setType(TYPE1);
		a7.getMetaData().setType(TYPE1);

		int i = 0;
		a0.setValue(String.valueOf(i++));
		a1.setValue(String.valueOf(i++));
		a2.setValue(String.valueOf(i++));
		a3.setValue(String.valueOf(i++));
		a4.setValue(String.valueOf(i++));
		a5.setValue(String.valueOf(i++));
		a6.setValue(String.valueOf(i++));
		a7.setValue(String.valueOf(i++));

		b0 = a.createAttribute("b-0");
		b1 = a.createAttribute("b-1");

		b0.getMetaData().setType(TYPE2);
		b1.getMetaData().setType(TYPE2);

		i = 0;
		b0.setValue(String.valueOf(i++));
		b1.setValue(String.valueOf(i++));

		wrapper = new TaskAttributeWrapper(a);
	}

}
