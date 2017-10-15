package org.ga4gh.registry.client.service;

import org.apache.log4j.Logger;
import org.ga4gh.registry.client.bean.CrawlerResultBean;
import org.ga4gh.registry.client.bean.ServerNodeBean;
import org.ga4gh.registry.client.json.RegistryClientJsonBuilder;
import org.ga4gh.registry.client.json.RegistryClientJsonParser;
import org.ga4gh.registry.client.rest.ClientRestCall;
import org.ga4gh.registry.client.util.RegistryClientConstants;
import org.ga4gh.registry.client.util.RegistryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class to retrieve registry data based on input
 *
 * Created by mduby on 3/28/17.
 */
@Service
public class RegistryCrawlerService {
    // instance variables
    private final Logger serviceLog = Logger.getLogger(this.getClass().getName());

    // autowired just to keep from receretig it every request; no state necessary
    @Autowired
    RegistryClientJsonParser registryClientJsonParser;

    // autowired just to keep from receretig it every request; no state necessary
    @Autowired
    RegistryClientJsonBuilder registryClientJsonBuilder;

    /**
     * returns a list of peers, filtered by type if the type parameter is not null; will return all types otherwise
     *
     * @param type
     * @return
     */
    public JsonObject getListOfPeers(String registryUrl, String type) {
        // local variables
        JsonObject resultObject = null;
        CrawlerResultBean crawlerResultBean = new CrawlerResultBean();
        List<ServerNodeBean> serverNodeBeanList = null;
        Set<String> visitedRegistryUrlSet = new HashSet<String>();

        try {
            // make the REST call if not already visited
            this.populateCrawlerResult(registryUrl, type, crawlerResultBean);

            // log
            this.serviceLog.info("From starting point: " + registryUrl + ", got number of node: " +
                    crawlerResultBean.getServerNodeBeanMap().size() + " and visited number of registries: " +
                    crawlerResultBean.getRegistryUrlVisitedSet().size());

            // get the resulting server list
            serverNodeBeanList = new ArrayList<ServerNodeBean>(crawlerResultBean.getServerNodeBeanMap().values());

            // build the json to return
            resultObject = this.registryClientJsonBuilder.buildServerNodeListJson(serverNodeBeanList);

        } catch (RegistryException exception) {
            resultObject = this.buildErrorObject(exception);
        }

        // return
        return resultObject;
    }

    /**
     * recursively traverse a registry tree with REST calls
     *
     * @param registryUrl
     * @param type
     * @param crawlerResultBean
     * @throws RegistryException
     */
    protected void populateCrawlerResult (String registryUrl, String type, CrawlerResultBean crawlerResultBean) throws RegistryException {
        // local variables
        JsonObject registryCallResultObject = null;
        List<ServerNodeBean> serverNodeBeanList = null;

        // make the REST call if the url hasn't been
        if (!crawlerResultBean.getRegistryUrlVisitedSet().contains(registryUrl)) {
            // log
            this.serviceLog.info("Visiting registry url: " + registryUrl);

            // add the url to the visited set
            crawlerResultBean.addVisitedRegistryUrl(registryUrl);

            // call the REST service
            ClientRestCall clientRestCall = new ClientRestCall();
            registryCallResultObject = clientRestCall.getRestCall(registryUrl, new HashMap<String, String>());

            // parse the results
            if (registryCallResultObject != null) {
                serverNodeBeanList = this.registryClientJsonParser.parseJsonString(registryCallResultObject.toString());

                // add to the result object
                for (ServerNodeBean serverNodeBean : serverNodeBeanList) {
                    crawlerResultBean.addServerNode(serverNodeBean);
                }

                // if any are registriesm recursively call
                for (ServerNodeBean serverNodeBean : serverNodeBeanList) {
                    if ((serverNodeBean.getType() != null) && RegistryClientConstants.RegistryType.REGISTRY.equals(serverNodeBean.getType())) {
                        // catch the errors here just in case
                        try {
                            this.populateCrawlerResult(serverNodeBean.getUrl(), type, crawlerResultBean);

                        } catch (RegistryException exception) {
                            this.serviceLog.error("Got exception for registry: " + serverNodeBean.getUrl() + " of: " + exception.getMessage());
                        }
                    }
                }

            } else {
                this.serviceLog.error("Got null json response to registry call: " + registryUrl);
            }

        } else {
            this.serviceLog.info("Skipping already visited registry url: " + registryUrl);
        }
    }

    /**
     * returns a json error object reflecting the exception thrown
     *
     * @param exception
     * @return
     */
    public JsonObject buildErrorObject(RegistryException exception) {
        // local variables
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonObject errorObject = null;

        // create the error
        builder.add(RegistryClientConstants.JsonKeys.IS_ERROR, true);
        builder.add(RegistryClientConstants.JsonKeys.ERROR_MESSAGE, exception.getMessage());

        // return
        errorObject = builder.build();
        return errorObject;
    }


}
