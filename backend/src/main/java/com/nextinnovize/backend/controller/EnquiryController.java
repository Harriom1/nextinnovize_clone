package com.nextinnovize.backend.controller;
import java.util.List;
import com.nextinnovize.backend.model.Enquiry;
import com.nextinnovize.backend.service.EnquiryService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enquiry")
@CrossOrigin

public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }
    
    @GetMapping
    public List<Enquiry> getAllEnquiries() {

        return enquiryService.getAllEnquiries();
    }

    @PostMapping
    public Enquiry createEnquiry(@RequestBody Enquiry enquiry) {

        return enquiryService.saveEnquiry(enquiry);
    }
}