package com.nextinnovize.backend.repository;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

import com.nextinnovize.backend.model.Enquiry;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EnquiryRepository {

    private final Datastore datastore;

    public EnquiryRepository() {

        this.datastore = DatastoreOptions.newBuilder()
                .setProjectId("nextinnovize-backend")
                .build()
                .getService();

        System.out.println("PROJECT ID: "
                + datastore.getOptions().getProjectId());
    }
    public Enquiry save(Enquiry enquiry) {

        KeyFactory keyFactory = datastore.newKeyFactory()
                .setKind("Enquiry");

        // Google Datastore generates a unique ID
        Key key = datastore.allocateId(keyFactory.newKey());

        // Capture "right now" as a plain number (milliseconds since
        // Jan 1 1970). This is the moment we're actually saving —
        // recorded fresh on every save() call.
        long now = System.currentTimeMillis();
        enquiry.setCreatedAt(now);

        Entity entity = Entity.newBuilder(key)
                .set("name", enquiry.getName())
                .set("email", enquiry.getEmail())
                .set("phone", enquiry.getPhone())
                .set("company", enquiry.getCompany())
                .set("service", enquiry.getService())
                .set("message", enquiry.getMessage())
                .set("createdAt", now)
                .build();

        // Actually saves the entity to Google Cloud
        datastore.put(entity);

        System.out.println("Enquiry saved to Google Cloud Datastore");

        return enquiry;
    }


    public List<Enquiry> findAll() {

        List<Enquiry> enquiries = new ArrayList<>();

        // Create query to get all entities
        // whose Kind is "Enquiry"
        // .setOrderBy(OrderBy.desc("createdAt")) tells Datastore:
        // "sort results by the createdAt field, largest number first."
        // Since createdAt is milliseconds-since-1970, the largest
        // number is always the MOST RECENT save — so this gives us
        // newest-first ordering.
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Enquiry")
                .setOrderBy(OrderBy.desc("createdAt"))
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

            // Older entities saved before this change won't have this
            // field, so we check with .contains() first to avoid an
            // error, and leave createdAt as null for those (that's
            // fine — it just means Datastore will list them last,
            // since a missing value sorts after all real numbers here).
            if (entity.contains("createdAt")) {
                enquiry.setCreatedAt(entity.getLong("createdAt"));
            }

            // Add the enquiry to our list
            enquiries.add(enquiry);
        }

        // Return all enquiries
        return enquiries;
    }
}