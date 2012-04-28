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

package org.apache.isis.viewer.html.context;

import org.apache.isis.core.commons.debug.DebugBuilder;
import org.apache.isis.core.commons.ensure.Assert;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.oid.Oid;
import org.apache.isis.core.metamodel.adapter.version.Version;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.AdapterManager;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;

public class PersistentObjectMapping implements ObjectMapping {
    
    private static final long serialVersionUID = 1L;
    
    private final Oid oid;
    private final ObjectSpecification specification;
    private Version version;

    public PersistentObjectMapping(final ObjectAdapter adapter) {
        oid = adapter.getOid();
        Assert.assertFalse("OID is for transient", oid.isTransient());
        Assert.assertFalse("adapter is for transient", adapter.isTransient());
        specification = adapter.getSpecification();
        version = adapter.getVersion();
    }

    @Override
    public void debug(final DebugBuilder debug) {
        debug.appendln(specification.getFullIdentifier());
        if (version != null) {
            debug.appendln(version.toString());
        }
    }

    @Override
    public Oid getOid() {
        return oid;
    }

    @Override
    public ObjectAdapter getObject() {
        return getPersistenceSession().loadObject(oid, specification);
    }

    @Override
    public int hashCode() {
        return oid.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj.getClass() == PersistentObjectMapping.class) {
            return ((PersistentObjectMapping) obj).oid.equals(oid);
        }
        return false;
    }

    @Override
    public String toString() {
        return (specification == null ? "null" : specification.getSingularName()) + " : " + oid + " : " + version;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public void checkVersion(final ObjectAdapter object) {
        object.checkLock(getVersion());
    }

    @Override
    public void updateVersion() {
        final ObjectAdapter adapter = getAdapterManager().getAdapterFor(oid);
        version = adapter.getVersion();
    }

    @Override
    public void restoreToLoader() {
        final Oid oid = getOid();
        final ObjectAdapter adapter = getPersistenceSession().recreateAdapter(oid, specification);
        adapter.setVersion(getVersion());
    }

    // /////////////////////////////////////////////////////
    // Dependencies (from context)
    // /////////////////////////////////////////////////////

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }

}
