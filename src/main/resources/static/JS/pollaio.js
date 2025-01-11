// Funzione per la validazione dell'età
function validateForm() {
    var eta = document.getElementsByName("eta")[0].value;
    if (eta <= 0) {
        alert("L'età deve essere maggiore di 0");
        return false;
    }
    return true;
}