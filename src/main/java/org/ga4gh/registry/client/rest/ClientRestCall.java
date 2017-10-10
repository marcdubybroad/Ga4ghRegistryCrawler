package org.ga4gh.registry.client.rest;

import org.apache.log4j.Logger;
import org.ga4gh.registry.client.util.RegistryException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

/**
 * Concrete REST call class to perform generic GET/POST calls
 *
 * Created by mduby on 5/19/17.
 */
public class ClientRestCall {
    // instance variables
    private final Logger restLogger = Logger.getLogger(this.getClass().getName());

    /**
     * perform a REST GET call with given url and parameter map
     *
     * @param url
     * @param parameterMap
     * @return
     * @throws RegistryException
     */
    public JsonObject getRestCall(String url, Map<String, String> parameterMap) throws RegistryException {
        // local variables
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        Iterator<String> keyIterator = null;
        UriComponentsBuilder builder = null;
        RestTemplate restTemplate = new RestTemplate();
        JsonReader jsonReader = null;
        JsonObject jsonObject = null;

        // log
        this.restLogger.info("Making REST GET call to url: " + url + " with parameters: " + parameterMap.toString());

        // get the keys to the map
        keyIterator = parameterMap.keySet().iterator();

        // get the url builder
        builder = UriComponentsBuilder.fromHttpUrl(url);

        // loop through the map add parameters
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            builder.queryParam(key, parameterMap.get(key));
        }

        // make the call
        try {
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);


            if (response.getStatusCode() == HttpStatus.OK) {
                String responseString = response.getBody();
                jsonReader = Json.createReader(new StringReader(responseString));
                jsonObject = jsonReader.readObject();

            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                RegistryException exception = new RegistryException("Got unauthorized " + url + " error");
                this.restLogger.error(exception.getMessage());
                throw exception;
            }

        } catch (HttpClientErrorException exception) {
            String message = "Got Http error for url: " + url + " :" + exception.getMessage();
            this.restLogger.error(message);
            throw new RegistryException(message);

        } catch (Exception exception) {
            String message = "Got IO error for url: " + url + " :" + exception.getMessage();
            this.restLogger.error(message);
            throw new RegistryException(message);
        }

        // make sure have a json object to return
        if (jsonObject == null) {
            RegistryException exception = new RegistryException("Got null " + url + " results json");
            this.restLogger.error(exception.getMessage());
            throw exception;
        }

        // return
        return jsonObject;
    }

}
