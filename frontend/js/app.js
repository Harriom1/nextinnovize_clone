const navbar = document.querySelector(".navbar");
const menuToggle = document.getElementById("menu-toggle");
const navLinks = document.querySelector(".nav-links");
const enquiryForm = document.querySelector(".enquiry-form");
const formMessage = document.getElementById("form-message");
const chatLauncher = document.getElementById("chat-launcher");
const chatWidget = document.getElementById("chat-widget");
const chatClose = document.getElementById("chat-close");
const chatForm = document.querySelector(".chat-input");
const chatInput = document.getElementById("chat-message");
const chatBody = document.querySelector(".chat-body");

if (navbar) {
    window.addEventListener("scroll", function () {
        navbar.classList.toggle("scrolled", window.scrollY > 50);
    });
}

if (menuToggle && navLinks) {
    menuToggle.addEventListener("click", function () {
        navLinks.classList.toggle("active");
    });

    navLinks.querySelectorAll("a").forEach(function (link) {
        link.addEventListener("click", function () {
            navLinks.classList.remove("active");
        });
    });
}

if (enquiryForm) {
    enquiryForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const enquiryData = {
            name: document.querySelector("#name").value,
            email: document.querySelector("#email").value,
            phone: document.querySelector("#phone").value,
            company: document.querySelector("#company").value,
            service: document.querySelector("#service").value,
            message: document.querySelector("#message").value
        };

        if (formMessage) {
            formMessage.textContent = "Sending your enquiry...";
        }

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

            await response.json();
            enquiryForm.reset();

            if (formMessage) {
                formMessage.textContent = "Thanks! Your enquiry was submitted successfully.";
            }
        } catch (error) {
            console.error("Error sending enquiry:", error);

            if (formMessage) {
                formMessage.textContent = "The form is ready, but the backend is not responding right now.";
            }
        }
    });
}

function setChatOpen(isOpen) {
    if (!chatWidget) {
        return;
    }

    chatWidget.classList.toggle("active", isOpen);
    chatWidget.setAttribute("aria-hidden", String(!isOpen));

    if (isOpen && chatInput) {
        chatInput.focus();
    }
}

function addChatBubble(text, className) {
    if (!chatBody || !text.trim()) {
        return;
    }

    const bubble = document.createElement("p");
    bubble.className = className;
    bubble.textContent = text.trim();
    chatBody.appendChild(bubble);
    chatBody.scrollTop = chatBody.scrollHeight;
}

if (chatLauncher && chatWidget) {
    chatLauncher.addEventListener("click", function () {
        setChatOpen(!chatWidget.classList.contains("active"));
    });
}

if (chatClose) {
    chatClose.addEventListener("click", function () {
        setChatOpen(false);
    });
}

document.querySelectorAll("[data-chat-reply]").forEach(function (button) {
    button.addEventListener("click", function () {
        const message = button.getAttribute("data-chat-reply");
        addChatBubble(message, "user-message");
        addChatBubble("Great. Please share your contact details in the form and the team can follow up.", "bot-message");
    });
});

if (chatForm && chatInput) {
    chatForm.addEventListener("submit", function (event) {
        event.preventDefault();
        addChatBubble(chatInput.value, "user-message");
        chatInput.value = "";
        addChatBubble("Thanks. This chat box is ready for a future live chatbot integration.", "bot-message");
    });
}
