package org.tuleap.mylyn.task.internal.agile.core.tests.diff;

import org.tuleap.mylyn.task.internal.agile.core.diff.ClassicLCS;
import org.tuleap.mylyn.task.internal.agile.core.diff.LCS;

/**
 * Test of {@link ClassicLCS}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ClassicLCSTest extends AbstractLCSTest {
	@Override
	protected LCS getImplementation() {
		return new ClassicLCS();
	}
}
