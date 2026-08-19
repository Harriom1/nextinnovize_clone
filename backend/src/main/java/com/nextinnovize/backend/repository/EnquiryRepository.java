package com.nextinnovize.backend.repository;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

import com.nextinnovize.backend.model.Enquiry;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EnquiryRepository {

    private final Datastore datastore;

    public EnquiryRepository() {
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    // =========================
    // SAVE ENQUIRY
    // =========================

    public Enquiry save(Enquiry enquiry) {

        KeyFactory keyFactory = datastore.newKeyFactory()
                .setKind("Enquiry");

        // Google Datastore generates a unique ID
        Key key = datastore.allocateId(keyFactory.newKey());

        Entity entity = Entity.newBuilder(key)
                .set("name", enquiry.getName())
                .set("email", enquiry.getEmail())
                .set("phone", enquiry.getPhone())
                .set("company", enquiry.getCompany())
                .set("service", enquiry.getService())
                .set("message", enquiry.getMessage())
                .build();

        // Actually saves the entity to Google Cloud
        datastore.put(entity);

        System.out.println("Enquiry saved to Google Cloud Datastore");

        return enquiry;
    }


    // =========================
    // GET ALL ENQUIRIES
    // =========================

    public List<Enquiry> findAll() {

        List<Enquiry> enquiries = new ArrayList<>();

        // Create query to get all entities
        // whose Kind is "Enquiry"
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Enquiry")
                .build();

        // Run the query
        QueryResults<Entity> results = datastore.run(query);

        // Read each result one by one
        while (results.hasNext()) {

            Entity entity = results.next();

            // Convert Google Datastore Entity
            // into our Java Enquiry object
            Enquiry enquiry = new Enquiry();

            enquiry.setName(entity.getString("name"));
            enquiry.setEmail(entity.getString("email"));
            enquiry.setPhone(entity.getString("phone"));
            enquiry.setCompany(entity.getString("company"));
            enquiry.setService(entity.getString("service"));
            enquiry.setMessage(entity.getString("message"));

            // Add the enquiry to our list
            enquiries.add(enquiry);
        }

        // Return all enquiries
        return enquiries;
    }
}