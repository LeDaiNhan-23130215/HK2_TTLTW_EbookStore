package controllers;

import DTO.AuthorDTO;
import DTO.CategoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AdminServices;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin-ebook/form-data")
public class AdminEbookFormDataController extends HttpServlet {

    private AdminServices adminServices;

    @Override
    public void init() {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        Map<String, Object> data = Map.of(
                "authors", adminServices.getListAuthors().stream().map(a -> new AuthorDTO(a.getId(), a.getAuthorName())).toList(),
                "categories", adminServices.getListCategory().stream().map(c -> new CategoryDTO(c.getId(), c.getName())).toList()
        );

        new ObjectMapper().writeValue(resp.getWriter(), data);
    }
}
