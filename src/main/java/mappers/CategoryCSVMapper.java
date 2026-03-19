package mappers;

import models.Category;
import services.AdminServices;

public class CategoryCSVMapper implements CSVMapper<Category> {
    public Category map(String[] cols) {
        AdminServices adminServices = new AdminServices();
        String name = cols.length > 0 ? cols[0].trim() : "";
        String desc = cols.length > 1 ? cols[1].trim() : "";
        String icon = cols.length > 2 ? cols[2].trim() : "";
        String categoryCode = adminServices.generateCategoryCode(name);

        if (name.isEmpty()) return null;

        return new Category(name, desc, icon,categoryCode);
    }
}
