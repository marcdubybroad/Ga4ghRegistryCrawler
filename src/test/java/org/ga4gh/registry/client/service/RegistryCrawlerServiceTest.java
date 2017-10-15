package org.ga4gh.registry.client.service;

import junit.framework.TestCase;
import org.ga4gh.registry.client.ClientApplication;
import org.ga4gh.registry.client.util.RegistryClientConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.json.JsonObject;

/**
 * Test class to tes the permission service
 *
 * Created by mduby on 6/26/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClientApplication.class)
@WebAppConfiguration
public class RegistryCrawlerServiceTest extends TestCase {
    // instance variables

    @Autowired
    RegistryCrawlerService registryCrawlerService;

    @Test
    public void testgetListOfPeers() {
        // local variables
        String registryUrl = "http://localhost:8090/registry/peers";
        JsonObject resultObject = null;

        // get the result json
        resultObject = this.registryCrawlerService.getListOfPeers(registryUrl, null);

        // test
        assertNotNull(resultObject);
        assertTrue(resultObject.containsKey(RegistryClientConstants.JsonKeys.PEERS));
    }

    @Test
    public void testgetListOfPeers2() {
        // local variables
        String registryUrl = "http://localhost:8071/registry/peers";
        JsonObject resultObject = null;

        // get the result json
        resultObject = this.registryCrawlerService.getListOfPeers(registryUrl, null);

        // test
        assertNotNull(resultObject);
        assertTrue(resultObject.containsKey(RegistryClientConstants.JsonKeys.PEERS));
    }
}
