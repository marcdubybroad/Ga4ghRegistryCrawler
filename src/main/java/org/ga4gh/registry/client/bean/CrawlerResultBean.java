package org.ga4gh.registry.client.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bean to keep result information and tracking of registry nodes visited
 *
 * Created by mduby on 10/9/17.
 */
public class CrawlerResultBean {
    // instance variables
    Map<String, ServerNodeBean> serverNodeBeanMap = new HashMap<String, ServerNodeBean>();
    Set<String> registryUrlVisitedSet = new HashSet<String>();

    public void addServerNode(ServerNodeBean bean) {
        if (bean.getType() == null) {

        } else if (bean.getUrl() == null) {

        } else {
            this.serverNodeBeanMap.put(bean.getUrl(), bean);
        }
    }

    public void addVisitedRegistryUrl(String url) {
        this.getRegistryUrlVisitedSet().add(url);
    }

    public Map<String, ServerNodeBean> getServerNodeBeanMap() {
        return serverNodeBeanMap;
    }

    public Set<String> getRegistryUrlVisitedSet() {
        return registryUrlVisitedSet;
    }
}
