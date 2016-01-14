/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rhiot.scale;

import io.rhiot.scale.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver implements Callable<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(Driver.class);

    List<Feature> features = new ArrayList<Feature>();
    Transport transport;
    Result result = new Result();

    @Override
    public Void call() throws Exception {
        LOG.info(this + " started");
        if (transport != null) {
            transport.connect();
        }

        ExecutorService executorService = Executors.newFixedThreadPool(features.size());
        executorService.invokeAll(features);
        executorService.shutdown();

        if (transport != null) {
            transport.disconnect();
        }
        LOG.info(this + " stopped");
        return null;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Result getResult() {
        return result;
    }

}