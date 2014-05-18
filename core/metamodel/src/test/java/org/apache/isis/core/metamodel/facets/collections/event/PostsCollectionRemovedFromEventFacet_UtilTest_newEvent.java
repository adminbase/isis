/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.metamodel.facets.collections.event;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.services.eventbus.CollectionRemovedFromEvent;
import org.apache.isis.core.metamodel.facets.collections.event.PostsCollectionRemovedFromEventFacetAbstract;

public class PostsCollectionRemovedFromEventFacet_UtilTest_newEvent {

    public static class SomeDomainObject {
        public Set<SomeReferencedObject> getReferences() { return null; }
    }
    public static class SomeReferencedObject {}
    
    public static class SomeDomainObjectCollectionRemovedFromEvent extends CollectionRemovedFromEvent<SomeDomainObject, SomeReferencedObject> {
        private static final long serialVersionUID = 1L;
        public SomeDomainObjectCollectionRemovedFromEvent(SomeDomainObject source, Identifier identifier, SomeReferencedObject value) {
            super(source, identifier, value);
        }
    }
    
    @Test
    public void defaultEventType() throws Exception {
        SomeDomainObject sdo = new SomeDomainObject();
        SomeReferencedObject other = new SomeReferencedObject();
        Identifier identifier = Identifier.propertyOrCollectionIdentifier(SomeDomainObject.class, "references");

        final CollectionRemovedFromEvent<Object, Object> ev = PostsCollectionRemovedFromEventFacet.Util.newEvent(
                CollectionRemovedFromEvent.Default.class, sdo, identifier, other);
        assertThat(ev.getSource(), is((Object)sdo));
        assertThat(ev.getIdentifier(), is(identifier));
        assertThat(ev.getValue(), is((Object)other));
    }

    @Test
    public void customEventType() throws Exception {
        SomeDomainObject sdo = new SomeDomainObject();
        SomeReferencedObject other = new SomeReferencedObject();
        Identifier identifier = Identifier.propertyOrCollectionIdentifier(SomeDomainObject.class, "references");
        
        final CollectionRemovedFromEvent<SomeDomainObject, SomeReferencedObject> ev = PostsCollectionRemovedFromEventFacet.Util.newEvent(
                SomeDomainObjectCollectionRemovedFromEvent.class, sdo, identifier, other);
        assertThat(ev.getSource(), is(sdo));
        assertThat(ev.getIdentifier(), is(identifier));
        assertThat(ev.getValue(), is(other));
    }
    
}