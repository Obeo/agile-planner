/*******************************************************************************
 * Copyright (c) 2004, 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *     Obeo - Refactoring and integration in the Mylyn Connector for Tuleap 
 *******************************************************************************/

package org.tuleap.mylyn.task.internal.agile.ui.editors.cardwall.figure;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.mylyn.commons.ui.CommonImages;
import org.eclipse.mylyn.commons.ui.dialogs.IInPlaceDialogListener;
import org.eclipse.mylyn.commons.ui.dialogs.InPlaceDialogEvent;
import org.eclipse.mylyn.commons.workbench.forms.InPlaceDateSelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * The widget that displays the date.
 * 
 * @author Bahadir Yagan
 * @author Mik Kersten
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class DateWidget extends Composite {

	/**
	 * The date widget part.
	 */
	private Text dateText;

	/**
	 * The pick button widget part.
	 */
	private Button pickButton;

	/**
	 * The clear control widget part.
	 */
	private ImageHyperlink clearControl;

	/**
	 * The calendar.
	 */
	private Calendar date;

	/**
	 * The list of widget listeners.
	 */
	private final List<SelectionListener> widgetListeners = new LinkedList<SelectionListener>();

	/**
	 * The formatted date.
	 */
	private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

	/**
	 * The initial text.
	 */
	private String initialText = "Choose date"; //$NON-NLS-1$

	/**
	 * <code>True</code> if displaying time on widget.
	 */
	private final boolean includeTimeOfday;

	/**
	 * The selected hour of the day.
	 */
	private int selectedHourOfDay;

	/**
	 * The constructor.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param style
	 *            The widget style
	 * @param initialText
	 *            The widget initial text
	 * @param includeHours
	 *            Boolean parameter to indicates whether the widget will include time or not.
	 * @param selectedHourOfDay
	 *            The selected time
	 */
	public DateWidget(Composite parent, int style, String initialText, boolean includeHours,
			int selectedHourOfDay) {
		super(parent, style);
		this.initialText = initialText;
		this.includeTimeOfday = includeHours;
		this.selectedHourOfDay = selectedHourOfDay;
		initialize(style);
	}

	/**
	 * Initialize the widget.
	 * 
	 * @param style
	 *            The widget style
	 */
	private void initialize(int style) {
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);

		dateText = new Text(this, style);
		GridData dateTextGridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		dateTextGridData.heightHint = 5;
		dateTextGridData.grabExcessHorizontalSpace = true;
		dateTextGridData.verticalAlignment = SWT.FILL;

		dateText.setLayoutData(dateTextGridData);
		dateText.setText(initialText);
		dateText.addFocusListener(new FocusAdapter() {
			Calendar calendar = Calendar.getInstance();

			@Override
			public void focusLost(FocusEvent e) {
				Date reminderDate;
				try {
					reminderDate = dateFormat.parse(dateText.getText());
					calendar.setTime(reminderDate);
					date = calendar;
					updateDateText();

				} catch (ParseException e1) {
					updateDateText();
				}

			}
		});

		clearControl = new ImageHyperlink(this, SWT.NONE);
		clearControl.setImage(CommonImages.getImage(CommonImages.FIND_CLEAR_DISABLED));
		clearControl.setHoverImage(CommonImages.getImage(CommonImages.FIND_CLEAR));
		clearControl.setToolTipText("Clear"); //$NON-NLS-1$
		clearControl.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				dateSelected(false, null);
			}

		});
		clearControl.setBackground(clearControl.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData clearButtonGridData = new GridData();
		clearButtonGridData.horizontalIndent = 3;
		clearControl.setLayoutData(clearButtonGridData);

		pickButton = new Button(this, style | SWT.ARROW | SWT.DOWN);
		GridData pickButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, true);
		pickButtonGridData.verticalIndent = 0;
		pickButtonGridData.horizontalIndent = 3;
		pickButton.setLayoutData(pickButtonGridData);
		pickButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final Calendar newCalendar = Calendar.getInstance();
				dateWidgetSelected(newCalendar);
			}

		});
		updateClearControlVisibility();
		pack();
		setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}

	@Override
	public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		dateText.setBackground(backgroundColor);
		if ((getStyle() & SWT.FLAT) != 0) {
			pickButton.setBackground(backgroundColor);
			clearControl.setBackground(backgroundColor);
		} else {
			pickButton.setBackground(null);
			clearControl.setBackground(null);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		dateText.setEnabled(enabled);
		pickButton.setEnabled(enabled);
		clearControl.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * The action to do when the date widget is selected.
	 * 
	 * @param calendar
	 *            The calendar
	 */
	private void dateWidgetSelected(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (date != null) {
			calendar.setTime(date.getTime());
		}
		Shell shell = pickButton.getShell();
		if (shell == null) {
			// fall back
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
				shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			} else {
				shell = new Shell(PlatformUI.getWorkbench().getDisplay());
			}
		}

		final InPlaceDateSelectionDialog dialog = new InPlaceDateSelectionDialog(shell, pickButton, calendar,
				"Choose Date", includeTimeOfday, selectedHourOfDay); //$NON-NLS-1$
		dialog.addEventListener(new IInPlaceDialogListener() {

			@Override
			public void buttonPressed(InPlaceDialogEvent event) {
				Calendar selectedCalendar = null;
				if (event.getReturnCode() == Window.OK && dialog.getDate() != null) {
					selectedCalendar = calendar;
					selectedCalendar.setTime(dialog.getDate());
				}
				dateSelected(event.getReturnCode() == Window.CANCEL, selectedCalendar);
			}
		});
		dialog.open();
	}

	/**
	 * Add a selection listener on the widget.
	 * 
	 * @param listener
	 *            The listener to add
	 */
	public void addWidgetSelectionListener(SelectionListener listener) {
		widgetListeners.add(listener);
	}

	/**
	 * Get the widget date value.
	 * 
	 * @return Calendar
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Set the widget date value.
	 * 
	 * @param date
	 *            The date value to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
		updateDateText();
	}

	/**
	 * Called when the user has selected a date.
	 * 
	 * @param canceled
	 *            Boolean value indicates whether the widget is canceled.
	 * @param selectedDate
	 *            The selected date
	 */
	protected void dateSelected(boolean canceled, Calendar selectedDate) {

		if (!canceled) {
			if (selectedDate != null) {
				this.date = selectedDate;
			} else {
				this.date = null;
			}
			updateDateText();
			notifyWidgetListeners();
		}

	}

	/**
	 * Notify the widget listeners.
	 */
	private void notifyWidgetListeners() {
		for (SelectionListener listener : widgetListeners) {
			listener.widgetSelected(null);
		}
	}

	/**
	 * Update the date text.
	 */
	private void updateDateText() {
		if (date != null) {
			Date currentDate = new Date(date.getTimeInMillis());
			dateText.setText(dateFormat.format(currentDate));
		} else {
			dateText.setEnabled(false);
			dateText.setText("Choose Date"); //$NON-NLS-1$
			dateText.setEnabled(true);
		}

		updateClearControlVisibility();
	}

	/**
	 * Update the clear control visibility.
	 */
	private void updateClearControlVisibility() {
		if (clearControl != null && clearControl.getLayoutData() instanceof GridData) {
			GridData gd = (GridData)clearControl.getLayoutData();
			gd.exclude = date == null;
			clearControl.getParent().layout();
		}
	}

}
