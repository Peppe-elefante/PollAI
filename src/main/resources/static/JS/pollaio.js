// Funzione per la validazione dell'età (usata inline nel form)
function validateForm() {
    var eta = document.getElementsByName("eta")[0].value;
    if (eta <= 0) {
        alert("L'età deve essere maggiore di 0");
        return false;
    }
    return true;
}

// Aggiunta di un listener all'evento DOMContentLoaded
document.addEventListener("DOMContentLoaded", function () {
    // Recupera il form usando il nuovo ID
    const form = document.getElementById("addChickenForm");

    // Aggiunge un evento di submit al form
    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevenire l'invio finché i controlli non passano

        let isValid = true;

        // Rimuovere errori precedenti
        clearErrors();

        // Recupera i valori dai campi del modulo
        const razza = document.getElementById("razza").value.trim();
        const eta = parseInt(document.getElementById("eta").value);
        const peso = parseInt(document.getElementById("peso").value);

        // Validazione dell'età
        if (isNaN(eta) || eta > 72 || eta < 1) {
            displayError("etaError", "Age must be between 1 and 72 months.");
            isValid = false;
        }

        // Validazione del peso
        if (isNaN(peso) || peso < 0 || peso > 5000) {
            displayError("pesoError", "Weight must be between 0 and 5000 grams.");
            isValid = false;
        }


        // Se tutto è valido, invia il modulo
        if (isValid) {
            form.submit();
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
