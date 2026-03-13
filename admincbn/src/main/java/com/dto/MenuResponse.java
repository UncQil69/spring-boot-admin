package com.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String title;
    private String icon;
    private String url;
    private List<MenuResponse> children = new ArrayList<>();

    public MenuResponse() {}

    public MenuResponse(Long id, String title, String icon, String url) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.url = url;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public List<MenuResponse> getChildren() { return children; }
    public void setChildren(List<MenuResponse> children) { this.children = children; }
}