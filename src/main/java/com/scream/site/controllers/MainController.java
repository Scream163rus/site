package com.scream.site.controllers;

import com.scream.site.models.Ad;
import com.scream.site.models.User;
import com.scream.site.repositories.AdRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
    private AdRepo adRepo;
    MainController(AdRepo adRepo){
        this.adRepo = adRepo;
    }

    @GetMapping("/")
    public String adMain(@AuthenticationPrincipal User user, Model model) {
        if(user == null){
            Iterable<Ad> ad = adRepo.findAll();
            model.addAttribute("ad", ad);
            return "home";
        }
        Iterable<Ad> ad = adRepo.findAll();
        model.addAttribute("ad", ad);
        model.addAttribute("id", user.getId());
        return "home";
    }
    @PostMapping("/")
    public String search(Model model,@RequestParam(name = "Category") String category, @RequestParam String searchText) {
        if (!searchText.isBlank() && !category.isBlank()) {
            Iterable<Ad> ad = adRepo.findAllByTitleAndCategory(searchText, category);
            model.addAttribute("ad", ad);
            return "home";
        }
        else if (searchText.isBlank() && !category.isBlank()) {
            Iterable<Ad> ad = adRepo.findAllByCategory(category);
            model.addAttribute("ad", ad);
            return "home";
        }
        if(category.isBlank() && !searchText.isBlank()) {
            Iterable<Ad> ad = adRepo.findAllByTitle(searchText);
            model.addAttribute("ad", ad);
            return "home";
        }
        Iterable<Ad> ad = adRepo.findAll();
        model.addAttribute("ad", ad);
        return "home";
    }
}
