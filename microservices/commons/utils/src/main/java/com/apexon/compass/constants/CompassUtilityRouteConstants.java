package com.apexon.compass.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompassUtilityRouteConstants {

    public static final String VALIDATE_PSR_EXCEL_DATA = "/validatePsrExcelData";

    public static final String UPLOAD_PSR_EXCEL = "/{id}/uploadPsrExcel";

}
