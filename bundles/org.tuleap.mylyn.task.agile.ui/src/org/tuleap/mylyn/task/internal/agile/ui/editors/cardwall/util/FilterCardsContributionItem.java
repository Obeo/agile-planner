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
package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.util;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * Contribution Item used to display a filtering field in the Mylyn Toolbar.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FilterCardsContributionItem extends ControlContribution {

	/**
	 * The ID of this contribution.
	 */
	private static final String ID = "org.tuleap.mylyn.agile.cardwall.filter"; //$NON-NLS-1$

	/**
	 * The control width.
	 */
	private static final int FILTER_WIDTH = 150;

	/**
	 * Listener of filter changes.
	 */
	private final ModifyListener listener;

	/**
	 * Constructor.
	 * 
	 * @param listener
	 *            the listener to notify when filter changes.
	 */
	public FilterCardsContributionItem(ModifyListener listener) {
		super(ID);
		this.listener = listener;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.ControlContribution#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.LEFT | SWT.SEARCH | SWT.ICON_SEARCH);
		text.setMessage(MylynAgileUIMessages.getString("FilterCardsContributionItem.Message")); //$NON-NLS-1$
		text.addModifyListener(listener);
		return text;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.ControlContribution#computeWidth(org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected int computeWidth(Control control) {
		return FILTER_WIDTH;
	}

}
