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
package org.tuleap.mylyn.task.internal.agile.ui.editors;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * Wrapper of a TaskDataWorkingCopy that intercepts certain calls useful for agile management.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class WorkingCopyWrapper implements ITaskDataWorkingCopy {

	/**
	 * The delegate.
	 */
	private final ITaskDataWorkingCopy delegate;

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            the delegate.
	 */
	public WorkingCopyWrapper(ITaskDataWorkingCopy delegate) {
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getEditsData()
	 */
	@Override
	public TaskData getEditsData() {
		return delegate.getEditsData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getLastReadData()
	 */
	@Override
	public TaskData getLastReadData() {
		return delegate.getLastReadData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getLocalData()
	 */
	@Override
	public TaskData getLocalData() {
		return delegate.getLocalData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getRepositoryData()
	 */
	@Override
	public TaskData getRepositoryData() {
		return delegate.getRepositoryData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#isSaved()
	 */
	@Override
	public boolean isSaved() {
		return delegate.isSaved();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#revert()
	 */
	@Override
	public void revert() {
		delegate.revert();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#refresh(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void refresh(IProgressMonitor monitor) throws CoreException {
		delegate.refresh(monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#save(java.util.Set,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void save(Set<TaskAttribute> edits, IProgressMonitor monitor) throws CoreException {
		delegate.save(edits, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return delegate.getConnectorKind();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getRepositoryUrl()
	 */
	@Override
	public String getRepositoryUrl() {
		return delegate.getRepositoryUrl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy#getTaskId()
	 */
	@Override
	public String getTaskId() {
		return delegate.getTaskId();
	}

}
