package com.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Request untuk menambah Employee baru")
public class EmployeeRequestDTO {

    @Schema(example = "C001", description = "ID Company (varchar 4)")
    private String company_id;

    @Schema(example = "EMP12345", description = "ID Employee (varchar 50)")
    private String employee_id;

    @Schema(example = "Budi Santoso", description = "Nama Lengkap (varchar 255)")
    private String person_name;

    @Schema(example = "budi@mail.com", description = "Alamat Email (varchar 50)")
    private String email;

    @Schema(example = "08123456789", description = "Nomor Telepon (varchar 20)")
    private String phone;

    @Schema(example = "1995-05-20", description = "Tanggal Lahir")
    private LocalDate date_of_birth;

    @Schema(example = "Jakarta", description = "Tempat Lahir")
    private String town_of_birth;

    @Schema(example = "Jl. Sudirman No. 123", description = "Alamat Lengkap")
    private String address;

    @Schema(example = "true", description = "Status Aktif")
    private Boolean is_active;

    @Schema(example = "1", description = "ID PSA (int4)")
    private Integer psa_id;

    @Schema(example = "PSA01", description = "Kode PSA (varchar 4)")
    private String psa_code;

    @Schema(example = "PSA Name Area 1", description = "Nama PSA (varchar 255)")
    private String psa_name;

    @Schema(example = "10", description = "ID Job Function (int4)")
    private Integer jobfunction_id;

    @Schema(example = "JF001", description = "Kode Job Function (varchar 6)")
    private String jobfunction_code;

    @Schema(example = "Software Engineer", description = "Nama Jabatan (varchar 255)")
    private String jobfunction_name;

    @Schema(example = "2024-01-01", description = "Tanggal Masuk")
    private LocalDate date_of_hire;

    @Schema(example = "2024-01-10", description = "Tanggal Mulai Kerja")
    private LocalDate date_of_work;

    @Schema(example = "2050-12-31", description = "Tanggal Pensiun")
    private LocalDate date_of_retire;
    
    @Schema(example = "1", description = "ID User yang membuat data")
    private Integer created_by;
}
