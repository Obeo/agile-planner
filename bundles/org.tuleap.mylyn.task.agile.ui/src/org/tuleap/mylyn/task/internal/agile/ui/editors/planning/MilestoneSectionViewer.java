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
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
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
	 * The viewer's model, which provides the milestone's assigned backlog items.
	 */
	private SubMilestoneBacklogModel fInput;

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
		Assert.isTrue(input instanceof SubMilestoneBacklogModel);
		SubMilestoneBacklogModel model = (SubMilestoneBacklogModel)input;
		this.fInput = model;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		fSection.setText(getMilestoneSectionHeaderText());
		float requiredCapacity = getMilestoneSectionRequiredCapacity();
		Float capacity = getMilestoneSectionCapacity();
		if (capacity == null) {
			fSection.setDescription(MylynAgileUIMessages.getString(
					"MilestoneSectionViewer.CapacityLabelWithoutCapacity", //$NON-NLS-1$
					Double.valueOf(requiredCapacity)));
		} else {
			fSection.setDescription(MylynAgileUIMessages.getString("MilestoneSectionViewer.CapacityLabel", //$NON-NLS-1$
					Float.valueOf(requiredCapacity), capacity));
			if (requiredCapacity > capacity.floatValue()) {
				fSection.getDescriptionControl().setForeground(ColorConstants.red);
			} else {
				fSection.getDescriptionControl().setForeground(ColorConstants.black);
			}
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
	private float getMilestoneSectionRequiredCapacity() {
		float sumOfPoints = 0.0F;
		for (BacklogItemWrapper bi : fInput.getBacklogItems()) {
			Float effort = bi.getInitialEffort();
			if (effort != null) {
				sumOfPoints += effort.floatValue();
			}
		}
		return sumOfPoints;
	}

	/**
	 * Computes the estimated milestone capacity by retrieving it from the relevant sub-attribute in the given
	 * TaskAttribute.
	 * 
	 * @return the estimated milestone capacity by retrieving it from the relevant sub-attribute in the given
	 *         TaskAttribute, or -1 if the capacity is not present
	 */
	private Float getMilestoneSectionCapacity() {
		return fInput.getSubMilestone().getCapacity();
	}

	/**
	 * Computes and returns the text to use as a header for a milestone section.
	 * 
	 * @return The text to use as a header for a milestone section.
	 */
	private String getMilestoneSectionHeaderText() {
		SubMilestoneWrapper subMilestone = fInput.getSubMilestone();
		String label = subMilestone.getLabel();
		Date startDate = subMilestone.getStartDate();
		Date endDate = subMilestone.getEndDate();

		// Compute the title of the section
		StringBuilder titleBuilder = new StringBuilder();
		if (label == null) {
			titleBuilder.append(MylynAgileUIMessages.getString("PlanningTaskEditorPart.MissingTextValue")); //$NON-NLS-1$
		} else {
			titleBuilder.append(label);
		}
		titleBuilder.append(" ("); //$NON-NLS-1$
		DateFormat dateFormat = new SimpleDateFormat(MylynAgileUIMessages
				.getString("PlanningTaskEditorPart.MilestoneDateFormat")); //$NON-NLS-1$
		if (startDate == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String formattedDate = dateFormat.format(startDate);
			titleBuilder.append(formattedDate);
		}
		titleBuilder.append(" - "); //$NON-NLS-1$
		if (endDate == null) {
			titleBuilder.append("?"); //$NON-NLS-1$
		} else {
			String formattedDate = dateFormat.format(endDate);
			titleBuilder.append(formattedDate);
		}
		titleBuilder.append(")"); //$NON-NLS-1$
		String title = titleBuilder.toString();
		return title;
	}
}
