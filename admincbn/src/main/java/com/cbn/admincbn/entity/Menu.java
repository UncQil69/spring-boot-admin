package com.cbn.admincbn.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String icon;
    private String url;

    @Column(name = "parent_id")
    private Long parentId;

    // Getter & Setter Manual
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}