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
package org.tuleap.mylyn.task.internal.agile.ui.editors.planning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * A viewer for milestone sections, which manages the refresh of the section's dynamic parts on demand.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MilestoneSectionViewer extends Viewer {

	/**
	 * The wrapped section.
	 */
	private final Section fSection;

	/**
	 * The viewer's model, which is a task attribute representing a section.
	 */
	private TaskAttribute fInput;

	/**
	 * Constructor receiving an existing section.
	 * 
	 * @param section
	 *            The section to wrap in this Viewer.
	 */
	public MilestoneSectionViewer(final Section section) {
		Assert.isNotNull(section);
		fSection = section;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return fSection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getInput()
	 */
	@Override
	public Object getInput() {
		return fInput;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		Assert.isNotNull(input);
		Assert.isTrue(input instanceof TaskAttribute);
		TaskAttribute ta = (TaskAttribute)input;
		Assert.isTrue(IMylynAgileCoreConstants.TYPE_MILESTONE.equals(ta.getMetaData().getType()));
		this.fInput = ta;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		fSection.setText(getMilestoneSectionHeaderText());
		double requiredCapacity = getMilestoneSectionRequiredCapacity();
		double capacity = getMilestoneSectionCapacity();
		fSection.setDescription(MylynAgileUIMessages.getString("MilestoneSectionViewer.CapacityLabel", //$NON-NLS-1$
				Double.valueOf(requiredCapacity), Double.valueOf(capacity)));
		if (requiredCapacity > capacity) {
			fSection.getDescriptionControl().setForeground(ColorConstants.red);
		} else {
			fSection.getDescriptionControl().setForeground(ColorConstants.black);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		//
	}

	/**
	 * Computes the required milestone capacity by adding the points of all the milestone's items.
	 * 
	 * @return the sum of all backlog items in <code>milestoneAtt</code>.
	 */
	private double getMilestoneSectionRequiredCapacity() {
		double sumOfPoints = 0.0;
		for (TaskAttribute child : fInput.getAttributes().values()) {
			if (IMylynAgileCoreConstants.TYPE_BACKLOG_ITEM.equals(child.getMetaData().getType())) {
				TaskAttribute pointsAtt = child.getAttribute(IMylynAgileCoreConstants.BACKLOG_ITEM_POINTS);
				if (pointsAtt != null) {
					String strPoints = pointsAtt.getValue();
					if (strPoints != null) {
						try {
							sumOfPoints += Double.parseDouble(strPoints);
						} catch (NumberFormatException e) {
							// Nothing to do
						}
					}
				}
			}
		}
		return sumOfPoints;
	}

	/**
	 * Computes the estimated milestone capacity by retrieving it from the relevant sub-attribute in the given
	 * TaskAttribute.
	 * 
	 * @return the estimated milestone capacity by retrieving it from the relevant sub-attribute in the given
	 *         TaskAttribute
	 */
	private double getMilestoneSectionCapacity() {
		String capacity = MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingNumericValue"); //$NON-NLS-1$;
		TaskAttribute capacityAtt = fInput.getAttribute(IMylynAgileCoreConstants.MILESTONE_CAPACITY);
		if (capacityAtt != null && capacityAtt.getValue() != null) {
			capacity = capacityAtt.getValue();
		}
		return Double.parseDouble(capacity);
	}

	/**
	 * Computes and returns the text to use as a header for a milestone section.
	 * 
	 * @return The text to use as a header for a milestone section.
	 */
	private String getMilestoneSectionHeaderText() {
		TaskAttribute nameAtt = fInput.getAttribute(IMylynAgileCoreConstants.LABEL);
		TaskAttribute startDateAtt = fInput.getAttribute(IMylynAgileCoreConstants.START_DATE);
		TaskAttribute endDateAtt = fInput.getAttribute(IMylynAgileCoreConstants.END_DATE);

		// Compute the title of the section
		StringBuilder titleBuilder = new StringBuilder();
		if (nameAtt == null || nameAtt.getValue() == null) {
			titleBuilder.append(MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingTextValue")); //$NON-NLS-1$
		} else {
			titleBuilder.append(nameAtt.getValue());
		}
		titleBuilder.append(" ("); //$NON-NLS-1$
		DateFormat dateFormat = new SimpleDateFormat(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.MilestoneDateFormat")); //$NON-NLS-1$
		if (startDateAtt == null || startDateAtt.getValue() == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String startDate = dateFormat.format(new Date(Long.parseLong(startDateAtt.getValue())));
			titleBuilder.append(startDate);
		}
		titleBuilder.append(" - "); //$NON-NLS-1$
		if (endDateAtt == null || endDateAtt.getValue() == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String endDate = dateFormat.format(new Date(Long.parseLong(endDateAtt.getValue())));
			titleBuilder.append(endDate);
		}
		titleBuilder.append(")"); //$NON-NLS-1$
		String title = titleBuilder.toString();
		return title;
	}
}
