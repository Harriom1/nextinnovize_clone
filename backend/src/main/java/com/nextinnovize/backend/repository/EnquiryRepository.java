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

        // Create query to get all entities whose Kind is "Enquiry".
        //
        // IMPORTANT: we deliberately do NOT use .setOrderBy() here.
        // Datastore has a strict rule — if you order a query by a
        // property, any entity that doesn't have that property at
        // all is SILENTLY EXCLUDED from the results (not sorted
        // last — just left out entirely). Since old enquiries saved
        // before we added createdAt don't have that field, ordering
        // at the query level would make them disappear from this
        // list completely. So instead: fetch everything unordered,
        // then sort it ourselves in Java below, where we can decide
        // exactly what happens to records missing the field.
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

            // Older entities saved before this change won't have this
            // field, so we check with .contains() first to avoid an
            // error when reading it. It's left as null for those —
            // the sort step further down treats null as "oldest",
            // so these still appear, just at the bottom of the list.
            if (entity.contains("createdAt")) {
                enquiry.setCreatedAt(entity.getLong("createdAt"));
            }

            // Add the enquiry to our list
            enquiries.add(enquiry);
        }

        // Sort the completed list ourselves, newest first.
        // We compare createdAt values, treating a missing/null value
        // as 0 (the oldest possible timestamp) — so old enquiries
        // without this field still show up, just pushed to the bottom
        // instead of disappearing. This is done here in Java, not in
        // the Datastore query, precisely to avoid excluding them.
        enquiries.sort((a, b) -> {
            long aTime = (a.getCreatedAt() != null) ? a.getCreatedAt() : 0L;
            long bTime = (b.getCreatedAt() != null) ? b.getCreatedAt() : 0L;
            return Long.compare(bTime, aTime); // bTime first = descending
        });

        // Return all enquiries
        return enquiries;
    }
}