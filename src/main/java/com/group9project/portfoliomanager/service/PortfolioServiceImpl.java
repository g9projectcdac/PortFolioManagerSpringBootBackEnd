package com.group9project.portfoliomanager.service;

import com.group9project.portfoliomanager.exception.PortfolioNotFoundException;
import com.group9project.portfoliomanager.model.Portfolio;
import com.group9project.portfoliomanager.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;


    @Override
    public Page<Portfolio> getAllRecords(Pageable pageable, String sortOrder) {
        if (sortOrder.equalsIgnoreCase("asc")) {
            return portfolioRepository.findAllByOrderByNameAsc(pageable);
        } else {
            return portfolioRepository.findAllByOrderByNameDesc(pageable);
        }
    }


    @Override
    public Page<Portfolio> searchPortfoliosAscending(String query, Pageable pageable) {
        return portfolioRepository.searchPortfoliosAscending(query, pageable);
    }

    @Override
    public Page<Portfolio> searchPortfoliosDescending(String query, Pageable pageable) {
        return portfolioRepository.searchPortfoliosDescending(query, pageable);
    }


    @Override
    public String newPortfolio(Portfolio newportfolio) {
        portfolioRepository.save(newportfolio);
        return "Portfolio with id {" + newportfolio.getId() + "} has been added successfully";
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    @Override
    public Portfolio getPortfolioById(long id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(id));
    }

    @Override
    public String deletePortfolio(long id) {
        if (!portfolioRepository.existsById(id)) {
            throw new PortfolioNotFoundException(id);
        }
        portfolioRepository.deleteById(id);
        return "Portfolio with id {" + id + "} has been deleted successfully";
    }

    @Override
    public void updatePortfolio(Portfolio newPortfolio, MultipartFile image, long id) {
        portfolioRepository.findById(id)
                .map(portfolio -> {
                    portfolio.setName(newPortfolio.getName());
                    portfolio.setEmail(newPortfolio.getEmail());
                    portfolio.setPhoneno(newPortfolio.getPhoneno());
                    portfolio.setUniversity(newPortfolio.getUniversity());
                    portfolio.setQualification(newPortfolio.getQualification());
                    portfolio.setPercentage(newPortfolio.getPercentage());
                    portfolio.setSkills(newPortfolio.getSkills());
                    portfolio.setCertification(newPortfolio.getCertification());
                    portfolio.setLocation(newPortfolio.getLocation());
                    portfolio.setGender(newPortfolio.getGender());
                    portfolio.setStatus(newPortfolio.getStatus());
                    portfolio.setGrade(newPortfolio.getGrade());
                    if (image != null && !image.isEmpty()) {
                        try {
                            portfolio.setImageData(image.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to update image.", e);
                        }
                    }
                    return portfolioRepository.save(portfolio);
                }).orElseThrow(() -> new PortfolioNotFoundException(id));
    }
}
