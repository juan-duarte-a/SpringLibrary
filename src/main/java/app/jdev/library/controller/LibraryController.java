package app.jdev.library.controller;

import app.jdev.library.model.SearchForm;
import app.jdev.library.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/")
public class LibraryController {

    @GetMapping("")
    public String requestHome(Model model) {
        model.addAttribute("action", Action.HOME.getAction());
        model.addAttribute("searchForm", new SearchForm(Action.HOME.getAction(), ""));
        LogService.log(Action.HOME, "Request homepage");
        return "home";
    }


    @GetMapping("search")
    public String search(SearchForm searchForm, Model model) {
        if (searchForm.action() == null) {
            return "redirect:/";
        }

        String redirectURL = "";

        switch (searchForm.action()) {
            case "Home", "Books" -> redirectURL = UriComponentsBuilder.fromPath("/books")
                    .queryParam("title", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
            case "Publishers" -> redirectURL = UriComponentsBuilder.fromPath("/publishers")
                    .queryParam("name", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
            case "Authors" -> redirectURL = UriComponentsBuilder.fromPath("/authors")
                    .queryParam("name", URLEncoder.encode(searchForm.searchText(), StandardCharsets.UTF_8))
                    .build().toUriString();
        }

        model.addAttribute("url", redirectURL);
        LogService.log(Action.SEARCHBAR, "Search = " + searchForm.searchText());
        return "redirect:" + redirectURL;
    }

}
