package com.cbn.admincbn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.entity.Menu;
import com.cbn.admincbn.repository.MenuRepository;
import com.cbn.admincbn.service.MenuService;
import com.dto.MenuResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/menus")
@Tag(name = "Menu Management", description = "Endpoint untuk mengatur navigasi menu dinamis")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Operation(summary = "Ambil semua menu yang ada")
    @GetMapping("/tree")
    public ResponseEntity<List<MenuResponse>> getMenuTree() {
        return ResponseEntity.ok(menuService.getMenuTree());
    }

    @Operation(summary = "Tambah menu baru")
    @PostMapping("/save")
    public ResponseEntity<?> saveMenu(@RequestBody Menu menu) {
        try {
            if (menu.getId() != null && menu.getId() == 0) {
                menu.setId(null);
            }
            if (menu.getParentId() != null && menu.getParentId() == 0) {
                menu.setParentId(null);
            }
            Menu savedMenu = menuRepository.save(menu);
            return ResponseEntity.ok(savedMenu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menyimpan menu: " + e.getMessage());
        }
    }

    @Operation(summary = "Update menu secara dinamis")
    @PutMapping("/update")
    public ResponseEntity<?> updateMenu(
            @Parameter(description = "ID menu yang ingin diupdate") @RequestParam(required = false) Long id,
            @Parameter(description = "Nama menu lama jika ID tidak tahu") @RequestParam(required = false) String searchTitle,
            @RequestBody Menu menuDetails) {
        
        try {
            Optional<Menu> optionalMenu;

            if (id != null) {
                optionalMenu = menuRepository.findById(id);
            } else if (searchTitle != null && !searchTitle.isEmpty()) {
                optionalMenu = menuRepository.findByTitle(searchTitle);
            } else {
                return ResponseEntity.badRequest().body("Gagal: Masukkan ID atau masukkan nama menu untuk mencari data yang akan diubah.");
            }

            if (optionalMenu.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu tidak ditemukan.");
            }

            Menu existingMenu = optionalMenu.get();

            if (menuDetails.getTitle() != null) existingMenu.setTitle(menuDetails.getTitle());
            if (menuDetails.getIcon() != null) existingMenu.setIcon(menuDetails.getIcon());
            if (menuDetails.getUrl() != null) existingMenu.setUrl(menuDetails.getUrl());
            if (menuDetails.getParentId() != null) {
        
                existingMenu.setParentId(menuDetails.getParentId() == 0 ? null : menuDetails.getParentId());
            }

            Menu updatedMenu = menuRepository.save(existingMenu);
            return ResponseEntity.ok(updatedMenu);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal update menu: " + e.getMessage());
        }
    }

    @Operation(summary = "Hapus menu secara dinamis")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMenu(
            @Parameter(description = "ID menu yang ingin dihapus") @RequestParam(required = false) Long id,
            @Parameter(description = "Nama menu yang ingin dihapus") @RequestParam(required = false) String title) {
        
        try {
            if (id != null) {
                if (!menuRepository.existsById(id)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu dengan ID " + id + " tidak ditemukan.");
                }
                menuRepository.deleteById(id);
                return ResponseEntity.ok("Menu dengan ID " + id + " berhasil dihapus.");
            } 
            
            if (title != null && !title.isEmpty()) {
                if (!menuRepository.existsByTitle(title)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu dengan nama '" + title + "' tidak ditemukan.");
                }
                menuRepository.deleteByTitle(title);
                return ResponseEntity.ok("Menu dengan nama '" + title + "' berhasil dihapus.");
            }

            return ResponseEntity.badRequest().body("Gagal: Anda harus memasukkan ID atau nama menu.");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan saat menghapus: " + e.getMessage());
        }
    }
}