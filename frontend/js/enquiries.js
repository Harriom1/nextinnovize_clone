console.log("ENQUIRIES JS LOADED");

const enquiriesBody = document.getElementById("enquiries-body");

console.log("Table body:", enquiriesBody);

fetch("http://localhost:8080/api/enquiry")

    .then(function (response) {

        console.log("Response received:", response);

        if (!response.ok) {
            throw new Error(
                "Server returned: " + response.status
            );
        }

        return response.json();

    })

    .then(function (enquiries) {

        console.log("Fetched enquiries:", enquiries);

        enquiriesBody.innerHTML = "";

        enquiries.forEach(function (enquiry, index) {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${enquiry.name}</td>
                <td>${enquiry.email}</td>
                <td>${enquiry.phone}</td>
                <td>${enquiry.company}</td>
                <td>${enquiry.service}</td>
                <td>${enquiry.message}</td>
            `;

            enquiriesBody.appendChild(row);

        });

    })

    .catch(function (error) {

        console.error(
            "Error fetching enquiries:",
            error
        );

        enquiriesBody.innerHTML = `
            <tr>
                <td colspan="7">
                    Unable to load enquiries.
                </td>
            </tr>
        `;

    });