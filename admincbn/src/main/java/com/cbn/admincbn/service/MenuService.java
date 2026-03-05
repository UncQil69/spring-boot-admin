package com.cbn.admincbn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbn.admincbn.entity.Menu;
import com.cbn.admincbn.repository.MenuRepository;
import com.dto.MenuResponse;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuResponse> getMenuTree() {
        List<Menu> allMenus = menuRepository.findAll();
        Map<Long, MenuResponse> lookup = new HashMap<>();
        List<MenuResponse> rootMenus = new ArrayList<>();

        for (Menu menu : allMenus) {
            lookup.put(menu.getId(), new MenuResponse(
                menu.getId(), 
                menu.getTitle(), 
                menu.getIcon(), 
                menu.getUrl()
            ));
        }

        for (Menu menu : allMenus) {
            MenuResponse dto = lookup.get(menu.getId());
            if (menu.getParentId() == null) {
                rootMenus.add(dto);
            } else {
                MenuResponse parent = lookup.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }
        return rootMenus;
    }
}