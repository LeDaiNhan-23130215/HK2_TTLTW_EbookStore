package services;

import DAO.CategoryDAO;
import DAO.CheckoutDetailDAO;
import DAO.EbookDAO;
import DTO.EbookFilterView;
import DTO.EbookProductCardView;
import DTO.PageView;
import filter.FilterBuilder;
import filter.QueryBase;
import models.Category;

import java.util.ArrayList;
import java.util.List;

public class EbookService {
    private final CheckoutDetailDAO checkoutDetailDAO = new CheckoutDetailDAO();
    private final EbookDAO ebookDAO = new EbookDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    private static final int PAGE_SIZE = 24;

    public List<EbookProductCardView> getNewEbookProductCards() {
        return ebookDAO.getNewEbookCardsWithThumbnail(15);
    }

    public PageView<EbookProductCardView> getBooks(int page,
             EbookFilterView filter) {
        if (page < 1) {
            page = 1;
        }
        QueryBase countQuery = FilterBuilder.buildForCount(filter).apply(new QueryBase());

        QueryBase listQuery = FilterBuilder.buildForList(filter).apply(new QueryBase());

        int totalItems = ebookDAO.countProductCards(countQuery);

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        List<EbookProductCardView> items = ebookDAO.findProductCards(page, PAGE_SIZE, listQuery);

        return new PageView<>(items, page, totalPages);
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategory();
    }
    public Category getCategoryById(Integer id) {
        return categoryDAO.getCategoryById(id);
    }

    public String generateEBookCode(int categoryId) {
        String categoryCode = categoryDAO.getCategoryCodeById(categoryId);
        Integer maxNumber = ebookDAO.getMaxCodeNumberByCategory(categoryId);

        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
        return categoryCode + String.format("%03d", nextNumber);
    }

    public List<EbookProductCardView> getTopSaleEbookProductCards() {
        List<Integer> bid = checkoutDetailDAO.getEbookIdsTopSale(8);
        List<EbookProductCardView> ebooks = new ArrayList<>();

        for(Integer id : bid) {
            EbookProductCardView ebv = ebookDAO.getEbookCardsWithThumbnailById(id);
            ebooks.add(ebv);
        }
        return ebooks;
    }

    public List<EbookProductCardView> getRandomEbook() {
        return ebookDAO.getRandomEbookCardsWithThumbnail(8);
    }
}
