package controllers;

import DTO.CartItem;
import enums.AddBookResult;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.User;
import services.CartService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getId();

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());
        double totalPrice = 0;
        for (CartItem ci : cartItems) {
            totalPrice += ci.getPriceAtADD();
        }

        request.setAttribute("cart", cart);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalPrice", totalPrice);

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        String action = request.getParameter("action");
        int userId = user.getId();
        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        if ("add".equals(action)) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            double price = Double.parseDouble(request.getParameter("price"));

            AddBookResult result = cartService.addBookToCart(
                    userId, cart.getId(), bookId, price
            );

            switch (result) {
                case ALREADY_OWNED:
                    request.getSession().setAttribute(
                            "toastError",
                            "📚 Bạn đã sở hữu sách này rồi"
                    );
                    break;

                case ALREADY_EXISTS:
                    request.getSession().setAttribute(
                            "toastWarning",
                            "⚠️ Sách đã có trong giỏ hàng"
                    );
                    break;

                case SUCCESS:
                    request.getSession().setAttribute(
                            "toastSuccess",
                            "✅ Đã thêm sách vào giỏ hàng"
                    );
                    break;
            }
        }

        if ("remove".equals(action)) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            cartService.removeItem(cart.getId(), bookId);
        }

        if ("update".equals(action)) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            double price = Double.parseDouble(request.getParameter("price"));

            cartService.updatePrice(cart.getId(), bookId, price);
        }

        int totalCartDetails = cartService.getTotalCartDetails(cart.getId());
        session.setAttribute("totalCartDetails", totalCartDetails);

        response.sendRedirect("cart");
    }
}