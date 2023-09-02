package com.group9project.portfoliomanager.service;

import com.group9project.portfoliomanager.model.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PortfolioService {


    Page<Portfolio> getAllRecords(Pageable pageable, String sortOrder);

    Page<Portfolio> searchPortfoliosAscending(String query, Pageable pageable);

    Page<Portfolio> searchPortfoliosDescending(String query, Pageable pageable);

    public String newPortfolio(Portfolio portfolio);

    public List<Portfolio> getAllPortfolios();

    public Portfolio getPortfolioById(long id);

    public String deletePortfolio(long id);

    public void updatePortfolio(Portfolio newPortfolio, MultipartFile image, long id);

}
