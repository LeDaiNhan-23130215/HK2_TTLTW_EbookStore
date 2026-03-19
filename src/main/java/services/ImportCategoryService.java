package services;

import DAO.CategoryDAO;
import DTO.ImportResult;
import models.Category;

import java.util.List;

public class ImportCategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public ImportResult importCategories(List<Category> categories) {
        int success = 0;
        int skipped = 0;

        for(Category category : categories) {
            if(categoryDAO.hasCategoryName(category.getName())) {
                skipped++;
                continue;
            }
            categoryDAO.addCategory(category);
            success++;
        }
        return new ImportResult(success, skipped);
    }
}
