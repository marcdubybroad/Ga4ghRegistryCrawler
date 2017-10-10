package org.ga4gh.registry.client.json;

import org.apache.log4j.Logger;
import org.ga4gh.registry.client.bean.ServerNodeBean;
import org.ga4gh.registry.client.util.RegistryClientConstants;
import org.ga4gh.registry.client.util.RegistryException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.List;

/**
 * Builder class to build json objects
 *
 * Created by mduby on 4/5/17.
 */
@Component
public class RegistryClientJsonBuilder {
    // instance variables
    private final Logger builderLog = Logger.getLogger(this.getClass().getName());

    /**
     * builds a json object of the server node
     *
     * @param serverNodeBeanList
     * @return
     * @throws RegistryException
     */
    public JsonObject buildServerNodeListJson(List<ServerNodeBean> serverNodeBeanList) throws RegistryException {
        // local variables
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonObject jsonObject = null;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        // loop through the nodes and build the array
        for (ServerNodeBean node : serverNodeBeanList) {
            if (node.getType() == null) {
                this.builderLog.error("Got null type for node: " + node.toString());

            } else if (node.getUrl() == null) {
                this.builderLog.error("Got null url for node: " + node.toString());

            } else {
                objectBuilder.add(RegistryClientConstants.JsonKeys.TYPE, node.getType());
                objectBuilder.add(RegistryClientConstants.JsonKeys.URL, node.getUrl());

                // add in the parent url
                if (node.getRegistryUrl() != null) {
                    objectBuilder.add(RegistryClientConstants.JsonKeys.PARENT, node.getRegistryUrl());

                } else {
                    objectBuilder.add(RegistryClientConstants.JsonKeys.PARENT, JsonValue.NULL);
                }
            }

            // add to the array
            arrayBuilder.add(objectBuilder.build());
        }

        // add the array to the peers object
        objectBuilder.add(RegistryClientConstants.JsonKeys.PEERS, arrayBuilder.build());

        // build the object
        jsonObject = objectBuilder.build();

        // return
        return jsonObject;
    }
}
