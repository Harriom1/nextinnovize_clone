// ===== NAVBAR: transparent at top, solid once scrolled =====
// window.scrollY = how many pixels the page has been scrolled
// down from the very top. It's 0 when you're at the top.
const navbar = document.querySelector(".navbar");
const SCROLL_THRESHOLD = 50; // pixels — small buffer so it doesn't
                              // flicker the instant you nudge the page

window.addEventListener("scroll", function () {
    if (window.scrollY > SCROLL_THRESHOLD) {
        navbar.classList.add("scrolled");
    } else {
        navbar.classList.remove("scrolled");
    }
});

// ===== MOBILE HAMBURGER MENU =====
// The button (#menu-toggle) and the CSS (.nav-links.active) already
// existed on the page, but nothing was toggling the class on click.
const menuToggle = document.getElementById("menu-toggle");
const navLinks = document.querySelector(".nav-links");

if (menuToggle && navLinks) {
    menuToggle.addEventListener("click", function () {
        navLinks.classList.toggle("active");
    });
}

// ===== ENQUIRY FORM SUBMISSION =====
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
