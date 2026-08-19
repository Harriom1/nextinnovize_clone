const enquiryForm = document.querySelector(".enquiry-form");

enquiryForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const name = document.querySelector("#name").value;
    const email = document.querySelector("#email").value;
    const phone = document.querySelector("#phone").value;
    const company = document.querySelector("#company").value;
    const service = document.querySelector("#service").value;
    const message = document.querySelector("#message").value;

    const enquiryData = {
        name: name,
        email: email,
        phone: phone,
        company: company,
        service: service,
        message: message
    };

    console.log("Enquiry data:", enquiryData);

    console.log(
        "JSON data:",
        JSON.stringify(enquiryData, null, 2)
    );

    try {

        const response = await fetch("http://localhost:8080/api/enquiry", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(enquiryData)

        });

        if (!response.ok) {
            throw new Error("Server returned: " + response.status);
        }

        const result = await response.json();

        console.log("Backend response:", result);

        alert("Enquiry submitted successfully!");

        enquiryForm.reset();

    } catch (error) {

        console.error("Error sending enquiry:", error);

        alert("Something went wrong. Please try again.");

    }

});
