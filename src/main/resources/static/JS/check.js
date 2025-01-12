document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("registerForm").addEventListener("submit", function(event) {
        event.preventDefault();  // Prevenire l'invio del modulo fino a quando non è valido

        let isValid = true;

        // Rimuovi errori precedenti
        clearErrors();

        const nome = document.getElementById("nome").value;
        const cognome = document.getElementById("cognome").value;
        const phoneNumber = document.getElementById("phoneNumber").value;
        const pIVA = document.getElementById("pIVA").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Validazione nome
        if (nome.length > 20) {
            displayError("nomeError", "The name must be less than 20 characters.");
            isValid = false;
        }

        // Validazione cognome
        if (cognome.length > 20) {
            displayError("cognomeError", "The lastname must be less than 20 characters.");
            isValid = false;
        }

        // Validazione numero di telefono
        const phonePattern = new RegExp(/^\+?\d{1,4}[\s\-]?\(?\d{1,3}\)?[\s\-]?\d{1,4}[\s\-]?\d{1,4}$/);
        if (!phonePattern.test(phoneNumber)) {
            displayError("phoneNumberError", "Invalid phone number. Please use the correct format.");
            isValid = false;
        }

        // Validazione partita IVA
        const pIVAPattern = new RegExp(/^IT\d{11}$/);
        if (!pIVAPattern.test(pIVA)) {
            displayError("pIVAError", "Invalid PIVA. Please use the correct format.");
            isValid = false;
        }

        // Validazione email
        const emailPattern = new RegExp(/^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{2,10}$/);
        if (!emailPattern.test(email)) {
            displayError("emailError", "Invalid email. Please use the correct format.");
            isValid = false;
        }

        // Validazione password
        const passwordPattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\[\]{};':&quot\\|,.<>\/?]).{8,}$/);
        if (!passwordPattern.test(password)) {
            displayError("passwordError", "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, numbers and special characters.");
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
        errorElements.forEach(function(error) {
            error.textContent = "";
            error.classList.remove("active");
        });
    }
});
