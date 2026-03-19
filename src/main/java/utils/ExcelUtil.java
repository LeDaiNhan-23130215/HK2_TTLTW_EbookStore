package utils;

import models.Category;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    public static List<Category> readExcel(InputStream is) throws IOException {
        List<Category> categories = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);
            if(row == null) continue;

            String name = row.getCell(0).getStringCellValue().trim();
            String description = row.getCell(1) != null
                    ? row.getCell(1).getStringCellValue().trim()
                    : "";
            String icon = row.getCell(1) != null
                    ? row.getCell(1).getStringCellValue().trim()
                    : "fa-solid fa-folder";
            if(!name.isEmpty()){
                categories.add(new Category(name, description, icon));
            }
        }
        workbook.close();
        return categories;
    }
}
