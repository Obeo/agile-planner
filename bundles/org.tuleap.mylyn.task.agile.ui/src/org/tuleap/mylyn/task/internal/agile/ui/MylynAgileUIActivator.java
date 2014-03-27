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
package org.tuleap.mylyn.task.internal.agile.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.agile.ui.util.MylynAgileUIMessages;

/**
 * The activator in charge of managing the life-cycle of the bundle.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MylynAgileUIActivator extends AbstractUIPlugin {

	/**
	 * The ID of the plugin.
	 */
	public static final String PLUGIN_ID = "org.tuleap.mylyn.task.agile.ui"; //$NON-NLS-1$

	/**
	 * The sole instance of the activator.
	 */
	private static MylynAgileUIActivator plugin;

	/**
	 * The OSGI service tracker.
	 */
	private ServiceTracker<Object, AbstractAgileRepositoryConnectorUI> serviceTracker;

	/**
	 * The service tracker customizer which will hold all the registered Agile Repository Connectors.
	 */
	private AgileRepositoryConnectorUiServiceTrackerCustomizer customizer;

	/**
	 * The images.
	 */
	private Map<String, Image> imageMap = new HashMap<String, Image>();

	/**
	 * Cache of colors by arbitrary id.
	 */
	private Map<Object, Color> colorsById = new HashMap<Object, Color>();

	/**
	 * Pattern to match RGB colors.
	 */
	private Pattern rgbPattern = Pattern.compile("#([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})"); //$NON-NLS-1$

	/**
	 * Returns the sole instance of the activator.
	 *
	 * @return The sole instance of the activator.
	 */
	public static MylynAgileUIActivator getDefault() {
		return plugin;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		this.customizer = new AgileRepositoryConnectorUiServiceTrackerCustomizer(context);
		this.serviceTracker = new ServiceTracker<Object, AbstractAgileRepositoryConnectorUI>(context,
				AbstractAgileRepositoryConnectorUI.class.getName(), customizer);
		this.serviceTracker.open();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		this.serviceTracker.close();
		this.customizer = null;
		plugin = null;

		Iterator<Image> imageIterator = imageMap.values().iterator();
		while (imageIterator.hasNext()) {
			Image image = imageIterator.next();
			image.dispose();
		}
		imageMap.clear();

		// Dispose the used colors.
		for (Color c : colorsById.values()) {
			c.dispose();
		}
		colorsById.clear();

		super.stop(context);
	}

	/**
	 * Returns the service tracker customizer.
	 *
	 * @return The service tracker customizer.
	 */
	public AgileRepositoryConnectorUiServiceTrackerCustomizer getServiceTrackerCustomizer() {
		return this.customizer;
	}

	/**
	 * Returns an image at the given plug-in relative path.
	 *
	 * @param path
	 *            is a plug-in relative path
	 * @return the image
	 */
	public Image getImage(String path) {
		Image result = imageMap.get(path);
		if (result == null) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (descriptor != null) {
				result = descriptor.createImage();
				imageMap.put(path, result);
			}
		}
		return result;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Trace an Exception in the error log.
	 *
	 * @param e
	 *            Exception to log.
	 * @param blocker
	 *            <code>True</code> if the exception must be logged as error, <code>False</code> to log it as
	 *            a warning.
	 */
	public static void log(Exception e, boolean blocker) {
		if (e == null) {
			throw new NullPointerException(MylynAgileUIMessages
					.getString("MylynAgileUIActivator.LogNullException")); //$NON-NLS-1$
		}

		if (getDefault() == null) {
			// We are out of eclipse. Prints the stack trace on standard error.
			// CHECKSTYLE:OFF
			e.printStackTrace();
			// CHECKSTYLE:ON
		} else if (e instanceof CoreException) {
			log(((CoreException)e).getStatus());
		} else if (e instanceof NullPointerException) {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			log(new Status(severity, PLUGIN_ID, severity, MylynAgileUIMessages
					.getString("MylynAgileUIActivator.ElementNotFound"), e)); //$NON-NLS-1$
		} else {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			log(new Status(severity, PLUGIN_ID, severity, e.getMessage(), e));
		}
	}

	/**
	 * Puts the given status in the error log view.
	 *
	 * @param status
	 *            Error Status.
	 */
	public static void log(IStatus status) {
		// Eclipse platform displays NullPointer on standard error instead of throwing it.
		// We'll handle this by throwing it ourselves.
		if (status == null) {
			throw new NullPointerException(MylynAgileUIMessages
					.getString("MylynAgileUIActivator.LogNullStatus")); //$NON-NLS-1$
		}

		if (getDefault() != null) {
			getDefault().getLog().log(status);
		} else {
			// We are out of eclipse. Prints the message on standard error.
			// CHECKSTYLE:OFF
			System.err.println(status.getMessage());
			status.getException().printStackTrace();
			// CHECKSTYLE:ON
		}
	}

	/**
	 * Puts the given message in the error log view, as error or warning.
	 *
	 * @param message
	 *            The message to put in the error log view.
	 * @param blocker
	 *            <code>True</code> if the message must be logged as error, <code>False</code> to log it as a
	 *            warning.
	 */
	public static void log(String message, boolean blocker) {
		if (getDefault() == null) {
			// We are out of eclipse. Prints the message on standard error.
			// CHECKSTYLE:OFF
			System.err.println(message);
			// CHECKSTYLE:ON
		} else {
			int severity = IStatus.WARNING;
			if (blocker) {
				severity = IStatus.ERROR;
			}
			String errorMessage = message;
			if (errorMessage == null || "".equals(errorMessage)) { //$NON-NLS-1$
				errorMessage = MylynAgileUIMessages.getString("MylynAgileUIActivator.UnexpectedException"); //$NON-NLS-1$
			}
			log(new Status(severity, PLUGIN_ID, errorMessage));
		}
	}

	/**
	 * Get the color for the given id, if it exists.
	 *
	 * @param key
	 *            The color key (arbitrary)
	 * @return The cached color, possibly <code>null</code>.
	 */
	public Color getColor(Object key) {
		return colorsById.get(key);
	}

	/**
	 * Indicates whether there is a cached color for the given key.
	 *
	 * @param key
	 *            the color key;
	 * @return <code>true</code> if and only if there is an entry for the given key.
	 */
	public boolean hasColor(Object key) {
		return colorsById.containsKey(key);
	}

	/**
	 * Caches the given color for the given key and returns the cached color. If the given color was already
	 * present in the cache, this method returns the cached color which is different from the given color. It
	 * is the client's responsibility to dispose the given color properly. The cached color (the one that is
	 * returned) must not be disposed of.
	 *
	 * @param key
	 *            The key
	 * @param color
	 *            The color.
	 * @return The given color if a color was not already cached for the given key, or the cached color if
	 *         here already was one in the cache. If the returned color is different from the given color, the
	 *         caller should dispose the given color.
	 */
	public synchronized Color putColor(Object key, Color color) {
		if (colorsById.containsKey(key)) {
			return colorsById.get(key);
		}
		colorsById.put(key, color);
		return color;
	}

	/**
	 * Provides the color for the given RGB code, after creating it if necessary.
	 *
	 * @param rgb
	 *            The RGB code in CSS format, #rrggbb with exactly 6 hexadecimal digits.
	 * @return The relevant color or null if the given RGB code has an invalid format or is null.
	 */
	public synchronized Color forColorName(String rgb) {
		if (rgb == null) {
			return null;
		}
		Color c = null;
		if (colorsById.containsKey(rgb)) {
			c = colorsById.get(rgb);
		} else {
			Matcher rgbMatcher = rgbPattern.matcher(rgb);
			try {
				if (rgbMatcher.matches()) {
					int r = Integer.parseInt(rgbMatcher.group(1), Short.SIZE);
					int g = Integer.parseInt(rgbMatcher.group(2), Short.SIZE);
					int b = Integer.parseInt(rgbMatcher.group(3), Short.SIZE);
					c = new Color(getDisplay(), new RGB(r, g, b));
					colorsById.put(rgb, c);
				}
			} catch (NumberFormatException e) {
				MylynAgileUIActivator.log(e, false);
			}
		}
		return c;
	}

	/**
	 * Provides the Color for the given {@link RGB}, after creating it if necessary. This ensures a proper
	 * disposal of system resources when the plug-in is deactivated.
	 * 
	 * @param rgb
	 *            The RGB for the desired Color.
	 * @return The desired Color.
	 */
	public synchronized Color forRgb(RGB rgb) {
		if (hasColor(rgb)) {
			return getColor(rgb);
		}
		Color c = new Color(getDisplay(), rgb);
		return putColor(rgb, c);
	}

	/**
	 * Provides the {@link Display} to use.
	 *
	 * @return The current display, or the default display if there's no current display.
	 */
	public Display getDisplay() {
		Display result = Display.getCurrent();
		if (result == null) {
			result = Display.getDefault();
		}
		return result;
	}
}
