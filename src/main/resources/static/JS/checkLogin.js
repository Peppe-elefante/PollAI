document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".login-form form").addEventListener("submit", function (event) {
        event.preventDefault(); // Prevenire l'invio del modulo fino alla validazione

        let isValid = true;

        // Rimuovi errori precedenti
        clearErrors();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Validazione email
        const emailPattern = new RegExp(/^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{2,10}$/);
        if (!emailPattern.test(email)) {
            displayError("emailError", "Invalid email format. Please use a correct email address.");
            isValid = false;
        }

        // Validazione password
        const passwordPattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\[\]{};':&quot\\|,.<>\/?]).{8,}$/);
        if (!passwordPattern.test(password)) {
            displayError("passwordError", "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, numbers, and special characters.");
            isValid = false;
        }

        // Se tutti i controlli sono passati, invia il modulo
        if (isValid) {
            event.target.submit();
        }
    });

    // Funzione per mostrare l'errore
    function displayError(elementId, message) {
        const errorElement = document.getElementById(elementId);
        errorElement.textContent = message;
        errorElement.classList.add("active");
    }

    // Funzione per rimuovere gli errori precedenti
    function clearErrors() {
        const errorElements = document.querySelectorAll(".error");
        errorElements.forEach(function (error) {
            error.textContent = "";
            error.classList.remove("active");
        });
    }
});
