package services;

import DAO.*;
import DTO.CartItem;
import enums.AddBookResult;
import models.Cart;
import models.CartDetail;
import models.Ebook;
import models.Image;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private CartDAO cartDAO = new CartDAO();
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private EbookDAO ebookDAO = new EbookDAO();
    private BookshelfDAO bookshelfDAO = new BookshelfDAO();

    public Cart getCartByUserID(int userID) {
        return cartDAO.getCartByUserId(userID);
    }

    public int createCart(int userID) {
        return cartDAO.createCart(userID);
    }

    public AddBookResult addBookToCart(
            int userId,
            int cartId,
            int bookId,
            double price
    ) {

        if (bookshelfDAO.userOwnsEbook(userId, bookId)) {
            return AddBookResult.ALREADY_OWNED;
        }

        if (cartDetailDAO.isBookInCart(cartId, bookId)) {
            return AddBookResult.ALREADY_EXISTS;
        }

        cartDetailDAO.addBookToCart(cartId, bookId, price);
        return AddBookResult.SUCCESS;
    }

    public boolean removeItem(int cartId, int bookId){
        return cartDetailDAO.removeItem(cartId, bookId);
    }

    public boolean updatePrice(int cartId, int bookId, double price) {
        return cartDetailDAO.updatePrice(cartId, bookId, price);
    }

    public List<CartDetail> getCartDetailByCartID(int cartID) {
        return cartDetailDAO.getCartDetailsByCartID(cartID);
    }

    public List<CartItem> getCartItemsByCartID(int userId, int cartID) {

        List<CartItem> items = new ArrayList<>();
        List<CartDetail> details = cartDetailDAO.getCartDetailsByCartID(cartID);

        for (CartDetail cd : details) {
            if(bookshelfDAO.userOwnsEbook(userId, cartID)){
                continue;
            }
            Ebook ebook = ebookDAO.getEbookWithDetailsById(cd.getBookID());

            if (ebook != null) {
                CartItem item = new CartItem(
                        cd.getId(),
                        ebook,
                        cd.getPrice()
                );
                items.add(item);
            }
        }
        return items;
    }

    public int getTotalCartDetails(int cartId) {
        return cartDetailDAO.getCartDetailsByCartID(cartId).size();
    }

    public void clearCart(int cartId) {
        cartDetailDAO.removeAllItems(cartId);
    }
}
