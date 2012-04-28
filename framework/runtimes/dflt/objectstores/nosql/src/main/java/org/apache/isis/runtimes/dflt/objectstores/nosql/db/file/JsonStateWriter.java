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

package org.apache.isis.runtimes.dflt.objectstores.nosql.db.file;

import java.util.List;

import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.isis.runtimes.dflt.objectstores.nosql.NoSqlStoreException;
import org.apache.isis.runtimes.dflt.objectstores.nosql.db.StateWriter;

public class JsonStateWriter implements StateWriter {

    private final JSONObject dbObject = new JSONObject();
    
    private String type;
    private String oid;
    private String currentVersion;
    private String newVersion;

    @Override
    public StateWriter addAggregate(final String id) {
        final JsonStateWriter jsonStateWriter = new JsonStateWriter();
        try {
            dbObject.put(id, jsonStateWriter.dbObject);
        } catch (final JSONException e) {
            throw new NoSqlStoreException(e);
        }
        return jsonStateWriter;
    }

    @Override
    public void writeType(final String type) {
        this.type = type;
        writeField("_type", type);
    }

    @Override
    public void writeId(final String oid) {
        this.oid = oid;
        writeField("_id", oid);
    }

    @Override
    public void writeEncryptionType(final String type) {
        writeField("_encrypt", type);
    }

    @Override
    public void writeVersion(final String currentVersion, final String newVersion) {
        this.currentVersion = currentVersion;
        this.newVersion = newVersion;
        writeField("_version", newVersion);
    }

    @Override
    public void writeTime(final String time) {
        writeField("_time", time);
    }

    @Override
    public void writeUser(final String user) {
        writeField("_user", user);
    }

    @Override
    public void writeField(final String id, final String data) {
        try {
            dbObject.put(id, data == null ? JSONObject.NULL : data);
        } catch (final JSONException e) {
            throw new NoSqlStoreException(e);
        }
    }

    @Override
    public void writeField(final String id, final long l) {
        try {
            dbObject.put(id, Long.toString(l));
        } catch (final JSONException e) {
            throw new NoSqlStoreException(e);
        }
    }

    public String getRequest() {
        return type + " " + oid + " " + currentVersion + " " + newVersion;
    }

    public String getData() {
        try {
            return dbObject.toString(4);
        } catch (final JSONException e) {
            throw new NoSqlStoreException(e);
        }
    }

    @Override
    public StateWriter createElementWriter() {
        return new JsonStateWriter();
    }

    @Override
    public void writeCollection(final String id, final List<StateWriter> elements) {
        final List<JSONObject> collection = Lists.newArrayList();
        for (final StateWriter writer : elements) {
            collection.add(((JsonStateWriter) writer).dbObject);
        }
        try {
            dbObject.put(id, collection);
        } catch (final JSONException e) {
            throw new NoSqlStoreException(e);
        }
    }

}
