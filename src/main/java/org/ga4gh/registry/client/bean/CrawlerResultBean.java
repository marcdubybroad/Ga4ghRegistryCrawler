package org.ga4gh.registry.client.bean;

import org.apache.log4j.Logger;

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
    private final Logger beanLog = Logger.getLogger(this.getClass().getName());
    Map<String, ServerNodeBean> serverNodeBeanMap = new HashMap<String, ServerNodeBean>();
    Set<String> registryUrlVisitedSet = new HashSet<String>();

    public void addServerNode(ServerNodeBean bean) {
        if (bean.getType() == null) {
            this.beanLog.error("server bean has null type, so skip");

        } else if (bean.getUrl() == null) {
            this.beanLog.error("server bean has null url, so skip");

        } else {
            this.beanLog.info("Adding " + bean.getType() + " node with url: " + bean.getUrl());
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
