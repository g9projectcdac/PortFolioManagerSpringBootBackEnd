package com.group9project.portfoliomanager.controller;


import com.group9project.portfoliomanager.model.Portfolio;
import com.group9project.portfoliomanager.repository.PortfolioRepository;
import com.group9project.portfoliomanager.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin("http://localhost:3000")
@RestController
public class PortfolioController {


    @Autowired
    private PortfolioService portfolioService;


    @Autowired
    private PortfolioRepository portfolioRepository;


    @GetMapping("/portfolios")
    public ResponseEntity<Page<Portfolio>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Portfolio> records = portfolioService.getAllRecords(pageable, sortOrder);

        return ResponseEntity.ok(records);
    }



    @GetMapping("/search")
    public ResponseEntity<Page<Portfolio>> searchPortfolios(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by("name").ascending() : Sort.by("name").descending());

        Page<Portfolio> portfolios = sortOrder.equalsIgnoreCase("asc")
                ? portfolioService.searchPortfoliosAscending(query, pageable)
                : portfolioService.searchPortfoliosDescending(query, pageable);

        return new ResponseEntity<>(portfolios, HttpStatus.OK);
    }


    @PostMapping(value = "/portfolio", consumes = {"multipart/form-data"})
    public ResponseEntity<String> newPortfolio(@ModelAttribute Portfolio newPortfolio,
                                               @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            if (portfolioRepository.existsByEmail(newPortfolio.getEmail())) {
                return new ResponseEntity<>("Data already exists in the database.", HttpStatus.CONFLICT);
            } else if (portfolioRepository.existsByPhoneno(newPortfolio.getPhoneno())) {
                return new ResponseEntity<>("Data already exists in the database.", HttpStatus.CONFLICT);
            } else {
                if (image != null && !image.isEmpty()) {
                    newPortfolio.setImageData(image.getBytes()); // Convert and save the image bytes
                }

                return new ResponseEntity<>(portfolioService.newPortfolio(newPortfolio), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding portfolio.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/portfolio/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable long id) {
        return new ResponseEntity<Portfolio>(portfolioService.getPortfolioById(id), HttpStatus.OK);
    }

    @DeleteMapping("/portfolio/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable long id) {

        portfolioService.deletePortfolio(id);
        return new ResponseEntity<String>("Portfolio with id {" + id + "} has been deleted successfully", HttpStatus.OK);
    }


    @PutMapping("/portfolio/{id}")
    public ResponseEntity<String> updatePortfolio(@ModelAttribute Portfolio newPortfolio, @RequestParam(value = "image", required = false) MultipartFile image, @PathVariable long id) {
        if (portfolioRepository.existsByEmailAndIdIsNot(newPortfolio.getEmail(), newPortfolio.getId())) {
            return new ResponseEntity<>("Email already exists in the database.", HttpStatus.CONFLICT);
        } else if (portfolioRepository.existsByPhonenoAndIdIsNot(newPortfolio.getPhoneno(), newPortfolio.getId())) {
            return new ResponseEntity<>("Phone number already exists in the database.", HttpStatus.CONFLICT);
        } else {
            portfolioService.updatePortfolio(newPortfolio, image, id);
            return new ResponseEntity<>("Portfolio details updated successfully.", HttpStatus.OK);
        }
    }


}


