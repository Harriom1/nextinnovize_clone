package com.nextinnovize.backend.service;

import com.nextinnovize.backend.model.Enquiry;
import com.nextinnovize.backend.repository.EnquiryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service

public class EnquiryService { 
	public List<Enquiry> getAllEnquiries() {

	    return enquiryRepository.findAll();
	}
    private final EnquiryRepository enquiryRepository;

    public EnquiryService(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    public Enquiry saveEnquiry(Enquiry enquiry) {

        return enquiryRepository.save(enquiry);
    }
}