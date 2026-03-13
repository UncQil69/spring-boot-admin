package com.dto; 

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BankAccountRequest {

    @Schema(description = "Kode Nama Perusahaan")
    private String companyId; 

    @Schema(description = "Nama Akun Bank")
    private String bankaccName;

    @Schema(description = "Nomor Rekening")
    private String bankaccNumber;

    @Schema(description = "ID COA")
    private Long coaId;

    @Schema(description = "Kode COA")
    private String coaCode;

    @Schema(description = "Nama COA")
    private String coaName;

    @Schema(description = "Label Akun")
    private String accLabel;

    @Schema(description = "ID Teller/Karyawan")
    private Long bankTeller;

    @Schema(description = "Status Aktif")
    private Boolean isActive;
}