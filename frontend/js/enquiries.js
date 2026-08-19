const enquiriesContainer =
    document.getElementById("enquiries-container");

fetch("http://localhost:8080/api/enquiry")

    .then(function (response) {

        if (!response.ok) {
            throw new Error(
                "Server returned: " + response.status
            );
        }

        return response.json();

    })

    .then(function (enquiries) {

        console.log("Fetched enquiries:", enquiries);

        enquiriesContainer.innerHTML = "";

        enquiries.forEach(function (enquiry) {

            const enquiryCard = document.createElement("div");

            enquiryCard.classList.add("enquiry-card");

            enquiryCard.innerHTML = `
                <h2>${enquiry.name}</h2>

                <p><strong>Email:</strong> ${enquiry.email}</p>

                <p><strong>Phone:</strong> ${enquiry.phone}</p>

                <p><strong>Company:</strong> ${enquiry.company}</p>

                <p><strong>Service:</strong> ${enquiry.service}</p>

                <p><strong>Message:</strong> ${enquiry.message}</p>
            `;

            enquiriesContainer.appendChild(enquiryCard);

        });

    })

    .catch(function (error) {

        console.error(
            "Error fetching enquiries:",
            error
        );

        enquiriesContainer.innerHTML =
            "<p>Unable to load enquiries.</p>";

    });