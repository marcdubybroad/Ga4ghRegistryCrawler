package org.ga4gh.registry.client.json;

import org.apache.log4j.Logger;
import org.ga4gh.registry.client.bean.ServerNodeBean;
import org.ga4gh.registry.client.util.RegistryClientConstants;
import org.ga4gh.registry.client.util.RegistryException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse input json
 *
 * Created by mduby on 4/5/17.
 */
@Component
public class RegistryClientJsonParser {
    // instance variables
    private final Logger parserLog = Logger.getLogger(this.getClass().getName());

    /**
     * build the server node bean from the json
     *
     * @param jsonString
     * @return
     * @throws RegistryException
     */
    public List<ServerNodeBean> parseJsonString(String jsonString) throws RegistryException {
        // local variables
        List<ServerNodeBean> serverNodeBeanList = new ArrayList<ServerNodeBean>();
        JsonObject jsonObject = null;
        JsonReader jsonReader = null;
        JsonArray jsonArray = null;

        // get the json object
        try {
            jsonReader = Json.createReader(new StringReader(jsonString));
            jsonObject = jsonReader.readObject();

        } catch (JsonParsingException exception) {
            String message = "Got error parsing registry node input: " + exception.getMessage();
            this.parserLog.error(message);
            throw new RegistryException(message);
        }

        // get the peers
        if (jsonObject.containsKey(RegistryClientConstants.JsonKeys.PEERS)) {
            jsonArray = jsonObject.getJsonArray(RegistryClientConstants.JsonKeys.PEERS);

            // make sure not null
            if (jsonArray != null) {
                serverNodeBeanList = this.getServerList(jsonArray);

            } else {
                this.parserLog.info("Got null peers array");
            }
        }

        // return
        return serverNodeBeanList;
    }

    /**
     * build the server node list from the array
     *
     * @param jsonArray
     * @return
     * @throws RegistryException
     */
    protected List<ServerNodeBean> getServerList(JsonArray jsonArray) throws RegistryException {
        // local variables
        List<ServerNodeBean> serverNodeBeanList = new ArrayList<ServerNodeBean>();
        JsonObject jsonObject = null;

        // loop
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getValueType().equals(JsonValue.ValueType.OBJECT)) {
                jsonObject = jsonArray.getJsonObject(i);
                String type = jsonObject.getString(RegistryClientConstants.JsonKeys.TYPE);
                String url = jsonObject.getString(RegistryClientConstants.JsonKeys.URL);

                // build the server node object
                serverNodeBeanList.add(new ServerNodeBean(type, url));
            }
        }

        // return
        return serverNodeBeanList;
    }
}
