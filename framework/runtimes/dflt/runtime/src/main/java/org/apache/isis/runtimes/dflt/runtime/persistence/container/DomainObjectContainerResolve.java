/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.runtimes.dflt.runtime.persistence.container;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.ResolveState;
import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.AdapterManager;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;

/**
 * Helper class that encapsulates the processing performed by domain object
 * containers that are performing a resolve.
 * 
 * <p>
 * Implementation note: rather than inject in its dependencies, we instead look
 * up dependencies from the {@link IsisContext}. This is necessary, for the
 * {@link PersistenceSession} at least, because class enhancers may hold a
 * reference to the factory as part of the generated bytecode. Since the
 * {@link PersistenceSession} could change over the lifetime of the instance (eg
 * when using the {@link InMemoryObjectStore}), we must always look the
 * {@link PersistenceSession} from the {@link IsisContext}. The same applies to
 * the {@link ServicesInjector}.
 */
public class DomainObjectContainerResolve {

    public DomainObjectContainerResolve() {
    }

    public void resolve(final Object parent) {
        final ObjectAdapter adapter = adapterFor(parent);
        final ResolveState resolveState = adapter.getResolveState();
        if (resolveState.canChangeTo(ResolveState.RESOLVING)) {
            getPersistenceSession().resolveImmediately(adapter);
        }
    }

    public void resolve(final Object parent, final Object field) {
        if (field == null) {
            resolve(parent);
        }
    }

    private ObjectAdapter adapterFor(final Object object) {
        return getAdapterManager().adapterFor(object);
    }

    // /////////////////////////////////////////////////////////////////
    // Dependencies (looked up from context)
    // /////////////////////////////////////////////////////////////////

    protected PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }

    protected AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

}
