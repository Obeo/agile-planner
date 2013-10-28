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
package org.tuleap.mylyn.task.agile.ui.tests.cardwall.model;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallEvent;
import org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.model.CardwallEvent.Type;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link CardwallEvent}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallEventTest {

	/**
	 * Tests.
	 */
	@Test
	public void test() {
		CardwallEvent e = new CardwallEvent(Type.FIELD_VALUE_CHANGED, "Hello");
		assertEquals(Type.FIELD_VALUE_CHANGED, e.getType());
		assertEquals("Hello", e.getSource());
	}

}
