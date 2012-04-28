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

package org.apache.isis.core.progmodel.facets.object.immutable;

import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessClassContext;
import org.apache.isis.core.metamodel.facets.object.immutable.ImmutableFacet;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;
import org.apache.isis.core.progmodel.facets.object.immutable.annotation.ImmutableAnnotationFacetFactory;
import org.apache.isis.core.progmodel.facets.object.immutable.annotation.ImmutableFacetAnnotation;

public class ImmutableAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private ImmutableAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ImmutableAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    public void testImmutableAnnotationPickedUpOnClassAndDefaultsToAlways() {
        @Immutable
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.apache.isis.core.metamodel.facets.When.ALWAYS, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationAlwaysPickedUpOnClass() {
        @Immutable(When.ALWAYS)
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.apache.isis.core.metamodel.facets.When.ALWAYS, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationNeverPickedUpOnClass() {
        @Immutable(When.NEVER)
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.apache.isis.core.metamodel.facets.When.NEVER, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationOncePersistedPickedUpOnClass() {
        @Immutable(When.ONCE_PERSISTED)
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.apache.isis.core.metamodel.facets.When.ONCE_PERSISTED, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationUntilPersistedPickedUpOnClass() {
        @Immutable(When.UNTIL_PERSISTED)
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.apache.isis.core.metamodel.facets.When.UNTIL_PERSISTED, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

}
