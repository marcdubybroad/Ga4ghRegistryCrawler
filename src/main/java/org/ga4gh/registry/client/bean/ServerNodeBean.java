package org.ga4gh.registry.client.bean;

/**
 * Bean class to encapsulate the server settings
 *
 * Created by mduby on 4/5/17.
 */
public class ServerNodeBean {
    // instance variables
    private Long id;
    private String type = null;
    private String url = null;
    private String registryUrl;

    public ServerNodeBean() {
        super();
    }

    public ServerNodeBean(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
    }
}
