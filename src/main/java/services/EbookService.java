package services;

import DAO.CategoryDAO;
import DAO.CheckoutDetailDAO;
import DAO.EbookDAO;
import DTO.EbookFilterView;
import DTO.EbookProductCardView;
import DTO.PageView;
import models.Category;
import models.Ebook;
import models.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EbookService {
    private final CheckoutDetailDAO checkoutDetailDAO = new CheckoutDetailDAO();
    private final EbookDAO ebookDAO = new EbookDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ImageServices imageService = new ImageServices();

    private static final int PAGE_SIZE = 24;

    /* ================= PRODUCT CARDS ================= */

    public List<EbookProductCardView> getProductCards() {
        List<Ebook> ebooks = ebookDAO.findAll();
        return buildProductCards(ebooks);
    }

    public List<EbookProductCardView> getNewEbookProductCards() {
        List<Ebook> ebooks = ebookDAO.getNewBook();
        for(Ebook eb : ebooks) {
            eb.setImages(imageService.getImagesByEbookID(eb.getId()));
        }
        return buildProductCards(ebooks);
    }

    public PageView<EbookProductCardView> getBooks(int page, EbookFilterView filter) {

        if (page < 1) page = 1;

        int totalItems = ebookDAO.countProductCards(filter);
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        List<EbookProductCardView> items =
                ebookDAO.findProductCards(page, PAGE_SIZE, filter);

        return new PageView<>(items, page, totalPages);
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    /* ================= CATEGORY ================= */

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategory();
    }
    public Category getCategoryById(Integer id) {
        return categoryDAO.getCategoryById(id);
    }
    /* ================= BUSINESS ================= */

    public String generateEBookCode(int categoryId) {
        String categoryCode = categoryDAO.getCategoryCodeById(categoryId);
        Integer maxNumber = ebookDAO.getMaxCodeNumberByCategory(categoryId);

        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
        return categoryCode + String.format("%03d", nextNumber);
    }

    /* ================= PRIVATE ================= */

    private List<EbookProductCardView> buildProductCards(List<Ebook> ebooks) {

        return ebooks.stream()
                .map(this::toProductCard)
                .collect(Collectors.toList());
    }

    private EbookProductCardView toProductCard(Ebook ebook) {

        String imgLink = "/assets/img/no-image.png";

        if (ebook.getImages() != null && !ebook.getImages().isEmpty()) {
            imgLink = ebook.getImages().get(0).getImgLink();
        }

        return new EbookProductCardView(
                ebook.getId(),
                ebook.getTitle(),
                ebook.getPrice(),
                imgLink
        );
    }

    public List<EbookProductCardView> getTopSaleEbookProductCards() {
        List<Integer> bid = checkoutDetailDAO.getEbookIdsTopSale();
        List<Ebook> ebooks = new ArrayList<Ebook>();

        for(Integer id : bid) {
            Ebook eb = ebookDAO.getEbookById(id);
            ebooks.add(eb);
        }
        for(Ebook eb : ebooks) {
            eb.setImages(imageService.getImagesByEbookID(eb.getId()));
        }
        return buildProductCards(ebooks);
    }


    public List<EbookProductCardView> getRandomEbook() {
        List<Ebook> eb = ebookDAO.getRandomEbook(8);
        for(Ebook ebook : eb) {
            ebook.setImages(imageService.getImagesByEbookID(ebook.getId()));
        }
        return buildProductCards(eb);
    }
}
