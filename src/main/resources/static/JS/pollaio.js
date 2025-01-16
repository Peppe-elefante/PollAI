// Funzione per la validazione dell'età
function validateForm() {
    var eta = document.getElementsByName("eta")[0].value;
    if (eta <= 0) {
        alert("L'età deve essere maggiore di 0");
        return false;
    }
    return true;
}

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("addChickenForm").addEventListener("submit", function (event) {
        event.preventDefault(); // Prevenire l'invio finché i controlli non passano

        let isValid = true;

        // Rimuovere errori precedenti
        clearErrors();

        const razza = document.getElementById("razza").value.trim();
        const eta = parseInt(document.getElementById("eta").value);
        const peso = parseInt(document.getElementById("peso").value);

        // Validazione della razza
        if (razza.length > 15) {
            displayError("razzaError", "The breed name must be 15 characters or fewer.");
            isValid = false;
        }

        // Validazione dell'età
        if (isNaN(eta) || eta > 72 || eta < 1) {
            displayError("etaError", "Age must be between 1 and 72 months.");
            isValid = false;
        }

        // Validazione del peso
        if (isNaN(peso) || peso < 5000) {
            displayError("pesoError", "Weight must be at least 5000 grams.");
            isValid = false;
        }

        // Se tutto è valido, invia il modulo
        if (isValid) {
            event.target.submit();
        }
    });

    // Funzione per mostrare un errore
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
