package org.ga4gh.registry.client.controller;

import org.apache.log4j.Logger;
import org.ga4gh.registry.client.service.RegistryCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.json.JsonObject;

/**
 * Created by mduby on 10/14/17.
 */
@RestController
public class RegistryCrawlerController {
    // instance variables
    private final Logger controllerLog = Logger.getLogger(this.getClass().getName());

    @Autowired
    RegistryCrawlerService registryCrawlerService;

    /**
     * services the /peers GET REST call
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/peers", method = RequestMethod.GET, produces = "application/json")
    public String getListOfPeers(@RequestParam(value = "url", required = true) String url, @RequestParam(value = "type", required = false) String type) {
        String resultString = null;
        JsonObject resultObject = null;

        // log
        this.controllerLog.info("Got request for crawler list from registry: " + url + " with type: " + type);

        // get the result object
        resultObject = this.registryCrawlerService.getListOfPeers(url, type);

        // return
        return resultObject.toString();
    }
}
