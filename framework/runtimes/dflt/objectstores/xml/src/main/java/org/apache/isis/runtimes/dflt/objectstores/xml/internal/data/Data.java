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

package org.apache.isis.runtimes.dflt.objectstores.xml.internal.data;

import com.google.common.base.Objects;

import org.apache.isis.core.metamodel.adapter.oid.RootOid;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.SpecificationLookup;
import org.apache.isis.runtimes.dflt.objectstores.xml.internal.version.FileVersion;

public abstract class Data {
    
    private final RootOid oid;
    private final FileVersion version;

    Data(final RootOid oid, final FileVersion version) {
        this.oid = oid;
        this.version = version;
    }

    public RootOid getRootOid() {
        return oid;
    }

    public FileVersion getVersion() {
        return version;
    }

    public ObjectSpecification getSpecification(SpecificationLookup specificationLookup) {
        final String objectType = oid.getObjectType();
        return specificationLookup.lookupByObjectType(objectType);
    }

    public String getObjectType() {
        return getRootOid().getObjectType();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Data) {
            final Data data = (Data) obj;
            return Objects.equal(data.getObjectType(), getObjectType()) && Objects.equal(data.oid, oid);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + getObjectType().hashCode();
        h = 37 * h + oid.hashCode();
        return h;
    }

}
