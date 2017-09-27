package net.apanasik.springsecurityapp.controller;

import com.google.gson.Gson;
import net.apanasik.springsecurityapp.model.SearchComponent;
import net.apanasik.springsecurityapp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search_page", method = RequestMethod.GET)
    public String getSearchPage() {
        return "search";
    }

    @RequestMapping(value = "/search_query", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public String search(@ModelAttribute("query") String query, Model model) {
        searchService.search(query);
        if (searchService.hasNext()) {
            List<SearchComponent> components = searchService.next();
            model.addAttribute("components", components);
        } else if (searchService.loading()) {
            List<SearchComponent> components = searchService.next();
            model.addAttribute("components", components);
        }
        return "search";
    }

    @RequestMapping(value = "more_results", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody String getMore() {
        Gson gson = new Gson();
        if (searchService.hasNext()) {
            List<SearchComponent> components = searchService.next();
            return gson.toJson(components);
        } else if (searchService.loading()) {
            List<SearchComponent> components = searchService.next();
            return gson.toJson(components);
        } else {
            return gson.toJson(new LinkedList<SearchComponent>());
        }
    }
}
