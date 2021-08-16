package com.scream.site.controllers;

import com.scream.site.models.Ad;
import com.scream.site.models.User;
import com.scream.site.repositories.AdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Controller
public class AdController {
    @Autowired
    private AdRepo adRepo;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/add")
    public String add()
    {
        return "ad_add";
    }
    @PostMapping("/add")
    public String addAdd(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file, @Valid Ad ad, BindingResult result, Model model) throws IOException {
        {
            if (result.hasErrors()){
                return "ad_add";
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            ad.setFilename(resultFilename);
            ad.setAuthor(user);
            ad.setDate(LocalDate.now());
            adRepo.save(ad);
        }
        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String adWithId(@PathVariable(value = "id") long id,Model model){
        Ad ad = adRepo.findById(id);
        model.addAttribute("ad", ad);
        return "ad_details";
    }
    @GetMapping("/{id}/edit")
    public String adEdit(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id,Model model){
        Ad ad = adRepo.findById(id);
        if(user.getId().equals(ad.getAuthor().getId())) {
            model.addAttribute("ad", ad);
            return "ad_edit";
        }
        return "redirect:/";
    }
    @PostMapping("/{id}/edit")
    public String editAd(@AuthenticationPrincipal User user,@RequestParam("file") MultipartFile file,@PathVariable(value = "id") long id,@Valid Ad ad, BindingResult result) throws IOException {
        Ad adById = adRepo.findById(id);
        if(result.hasErrors()){
            return "ad_edit";
        }
        if(user.getId().equals(adById.getAuthor().getId())) {
            if(!file.isEmpty()){
                File fileOnDB = new File(uploadPath + "/" + adById.getFilename());
                fileOnDB.delete();
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                adById.setFilename(resultFilename);
            }
            adById.setTitle(ad.getTitle());
            adById.setCategory(ad.getCategory());
            adById.setDescription(ad.getDescription());
            adById.setNumberPhone(ad.getNumberPhone());
            adById.setDate(LocalDate.now());
            adRepo.save(adById);
        }
        return "redirect:/";
    }
    @PostMapping("/{id}/remove")
    public String deleteAd(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id){
        Ad ad = adRepo.findById(id);
        File file = new File(uploadPath + "/" + ad.getFilename());
        if(user.getId().equals(ad.getAuthor().getId())) {
            file.delete();
            adRepo.deleteById(id);
        }
        return "redirect:/{id}/ad";
    }
    @GetMapping("/{id}/ad")
    public String userAd(Model model, @AuthenticationPrincipal @PathVariable(value = "id") long id){
       Iterable<Ad> ad = adRepo.findAllByAuthorId(id);
       model.addAttribute("ad", ad);
        return "my_wares";
    }

}
