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
            displayError("nomeError", "Il nome deve avere meno di 20 caratteri.");
            isValid = false;
        }

        // Validazione cognome
        if (cognome.length > 20) {
            displayError("cognomeError", "Il cognome deve avere meno di 20 caratteri.");
            isValid = false;
        }

        // Validazione numero di telefono
        const phonePattern = new RegExp(/^\+?\d{1,4}[\s\-]?\(?\d{1,3}\)?[\s\-]?\d{1,4}[\s\-]?\d{1,4}$/);
        if (!phonePattern.test(phoneNumber)) {
            displayError("phoneNumberError", "Numero di telefono non valido. Usa il formato corretto.");
            isValid = false;
        }

        // Validazione partita IVA
        const pIVAPattern = new RegExp(/^IT\d{11}$/);
        if (!pIVAPattern.test(pIVA)) {
            displayError("pIVAError", "La Partita IVA non è valida. Usa il formato corretto.");
            isValid = false;
        }

        // Validazione email
        const emailPattern = new RegExp(/^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{2,10}$/);
        if (!emailPattern.test(email)) {
            displayError("emailError", "Email non valida.");
            isValid = false;
        }

        // Validazione password
        const passwordPattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\[\]{};':&quot\\|,.<>\/?]).{8,}$/);
        if (!passwordPattern.test(password)) {
            displayError("passwordError", "La password deve essere di almeno 8 caratteri e contenere lettere maiuscole, minuscole, numeri e caratteri speciali.");
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
