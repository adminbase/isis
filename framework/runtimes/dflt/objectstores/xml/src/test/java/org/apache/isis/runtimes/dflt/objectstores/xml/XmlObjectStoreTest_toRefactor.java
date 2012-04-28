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

package org.apache.isis.runtimes.dflt.objectstores.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.version.Version;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2.Mode;
import org.apache.isis.runtimes.dflt.objectstores.xml.internal.clock.DefaultClock;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.PersistenceCommand;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.PersistenceCommandContext;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.apache.isis.runtimes.dflt.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.apache.isis.runtimes.dflt.testsupport.IsisSystemWithFixtures;

public class XmlObjectStoreTest_toRefactor {
    
    @Rule
    public IsisSystemWithFixtures iswf = IsisSystemWithFixtures.builder().with(new XmlPersistenceMechanismInstaller()).build();

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    private XmlObjectStore objectStore;

    @Mock
    private PersistenceQueryBuiltIn persistenceHelper;
    
    private PersistenceCommandContext transaction;

    ObjectAdapter mockAdapter;

    private ObjectAdapter adapter1;

    private ObjectSpecification specification;


    @Before
    public void setUpSystem() throws Exception {

        // system
        objectStore = iswf.getObjectStore(XmlObjectStore.class);
        objectStore.setClock(new DefaultClock());

        // objects
//        adapter.setOptimisticLock(new SerialNumberVersion(23, null, null));
//        adapter.setupOid(RootOidDefault.createPersistent(objectType, ""+1));
        
        transaction = null;
    }


    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void createSaveObjectCommand_setsVersionOnAdapter() throws Exception {
        
        allowingGetOidAndGetObjectAndTitleStringFromAdapter();
        context.checking(new Expectations() {
            {
                one(mockAdapter).setVersion(with(any(Version.class)));
            }
        });
        objectStore.createSaveObjectCommand(mockAdapter);
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void createCreateObjectCommand_andExecute_persistsNewInstance() throws Exception {
        // given
        final CreateObjectCommand command = objectStore.createCreateObjectCommand(adapter1);
        // when
        command.execute(transaction);
        // then
        assertFalse(objectStore.hasInstances(specification));
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesDestroyObjectCommand() throws Exception {
        final DestroyObjectCommand command = objectStore.createDestroyObjectCommand(adapter1);
        command.execute(transaction);
        assertFalse(objectStore.hasInstances(specification));
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesSaveObjectCommand() throws Exception {
        final SaveObjectCommand command = objectStore.createSaveObjectCommand(adapter1);
        command.execute(transaction);
        assertTrue(objectStore.hasInstances(specification));
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesGettingObjectStoreInstances() throws Exception {
        final SaveObjectCommand command = objectStore.createSaveObjectCommand(adapter1);
        objectStore.execute(Collections.<PersistenceCommand> singletonList(command));
        final ObjectAdapter[] array = objectStore.getInstances(persistenceHelper);
        assertTrue(array.length == 1);
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesObjectStoreHasInstances() throws Exception {
        final SaveObjectCommand command = objectStore.createSaveObjectCommand(adapter1);
        objectStore.execute(Collections.<PersistenceCommand> singletonList(command));
        assertTrue(objectStore.hasInstances(specification));
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesObjectStoreIfFixtureIsInstalled() throws Exception {
        final SaveObjectCommand command = objectStore.createSaveObjectCommand(adapter1);
        objectStore.execute(Collections.<PersistenceCommand> singletonList(command));
        objectStore.open();
        assertTrue(objectStore.isFixturesInstalled());
    }

    @Ignore // DKH: refactor to use contract tests (see in-memory object store for basis)
    @Test
    public void validatesObjectStoreGetObject() throws Exception {
        final SaveObjectCommand command = objectStore.createSaveObjectCommand(adapter1);
        objectStore.execute(Collections.<PersistenceCommand> singletonList(command));
        assertTrue(objectStore.getObject(adapter1.getOid(), adapter1.getSpecification()).getOid().equals(adapter1.getOid()));
    }


    private void allowingGetOidAndGetObjectAndTitleStringFromAdapter() {
        context.checking(new Expectations() {
            {
                allowing(mockAdapter).getOid();
                allowing(mockAdapter).getObject();
                allowing(mockAdapter).titleString();
            }
        });
    }

}
