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
package org.apache.isis.core.metamodel.services.grid.fixedcols.applib;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.layout.component.CollectionLayoutData;
import org.apache.isis.applib.layout.component.CollectionLayoutDataOwner;
import org.apache.isis.applib.layout.component.FieldSet;
import org.apache.isis.applib.layout.component.FieldSetOwner;
import org.apache.isis.applib.layout.component.Owned;
import org.apache.isis.applib.layout.component.PropertyLayoutData;

/**
 * The column contains a mixture of {@link FieldSet}s (of {@link PropertyLayoutData properties}) and also
 * {@link CollectionLayoutData collection}s.
 *
 * <p>
 * A column generally is used within a {@link FCTab}; there can be up to three such (left, middle and right).  It is
 * also possible for their to be a column far-left on the top-level {@link FCGrid page}, and another far-right.
 * </p>
 *
 */
@XmlType(
        propOrder = {
                "fieldSets"
                , "collections"
        }
)
public class FCColumn implements Serializable, FieldSetOwner, CollectionLayoutDataOwner, Owned<FCColumnOwner> {

    private static final long serialVersionUID = 1L;

    public FCColumn() {
    }

    public FCColumn(final int span) {
        setSpan(span);
    }

    private int span = 4;

    @XmlAttribute(required = true)
    public int getSpan() {
        return span;
    }

    public void setSpan(final int span) {
        this.span = span;
    }



    private List<FieldSet> fieldSets = Lists.newArrayList();

    // no wrapper
    @XmlElementRef(type = FieldSet.class, name = "fieldSet", required = false)
    public List<FieldSet> getFieldSets() {
        return fieldSets;
    }

    public void setFieldSets(final List<FieldSet> fieldSets) {
        this.fieldSets = fieldSets;
    }


    private List<CollectionLayoutData> collections = Lists.newArrayList();

    // no wrapper
    @XmlElementRef(type = CollectionLayoutData.class, name = "collection", required = false)
    public List<CollectionLayoutData> getCollections() {
        return collections;
    }

    public void setCollections(final List<CollectionLayoutData> collections) {
        this.collections = collections;
    }


    private FCColumnOwner owner;
    /**
     * Owner.
     *
     * <p>
     *     Set programmatically by framework after reading in from XML.
     * </p>
     */
    @XmlTransient
    public FCColumnOwner getOwner() {
        return owner;
    }

    public void setOwner(final FCColumnOwner owner) {
        this.owner = owner;
    }




    private Hint hint;

    @XmlTransient
    public Hint getHint() {
        return hint;
    }

    public void setHint(final Hint hint) {
        this.hint = hint;
    }



    public enum Hint {
        LEFT,
        MIDDLE,
        RIGHT;

        public int from(MemberGroupLayout.ColumnSpans columnSpans) {
            if(this == LEFT) return columnSpans.getLeft();
            if(this == MIDDLE) return columnSpans.getMiddle();
            if(this == RIGHT) return columnSpans.getRight();
            throw new IllegalStateException();
        }

        public FCColumn from(final FCTab fcTab) {
            if(fcTab == null) {
                return null;
            }
            if(this == LEFT) return fcTab.getLeft();
            if(this == MIDDLE) return fcTab.getMiddle();
            if(this == RIGHT) return fcTab.getRight();
            throw new IllegalStateException();
        }

    }
}