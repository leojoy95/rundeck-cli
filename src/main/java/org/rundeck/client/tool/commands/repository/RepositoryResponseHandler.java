/*
 * Copyright 2019 Rundeck, Inc. (http://rundeck.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.client.tool.commands.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rundeck.client.api.model.repository.ArtifactActionMessage;
import org.rundeck.client.util.ServiceClient;
import org.rundeck.toolbelt.CommandOutput;

import java.io.IOException;

public class RepositoryResponseHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void handle(final ServiceClient.WithErrorResponse<ArtifactActionMessage>
                                      response, final CommandOutput output) throws IOException {
        ArtifactActionMessage msg = response.getResponse().body();

        if(response.isError400()) {
            ArtifactActionMessage err = mapper.readValue(response.getErrorBody().repeatBody().bytes(), ArtifactActionMessage.class);
            err.getErrors().forEach(error -> {
                output.error(error.getMsg());
            });
        } else {
            output.output(msg.getMsg());
        }
    }
}
