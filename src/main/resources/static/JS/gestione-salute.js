function mostraDettagli(button) {
    console.log("Pulsante cliccato");
    const id = button.getAttribute('data-id');
    const razza = button.getAttribute('data-razza');
    const eta = button.getAttribute('data-eta');
    const peso = button.getAttribute('data-peso');

    document.getElementById('dettagli-id').textContent = id;
    document.getElementById('dettagli-razza').textContent = razza;
    document.getElementById('dettagli-eta').textContent = eta;
    document.getElementById('dettagli-peso').textContent = peso;

    document.getElementById('dettagliGallina').style.display = 'block';
}

function mostraContattoVeterinario() {
    const dettagliVeterinario = document.getElementById('dettagliVeterinario');
    dettagliVeterinario.style.display = 'block'; // Mostra i dettagli del veterinario
}

function richiediConsiglio() {
    const idGallina = document.getElementById('dettagli-id').textContent;

    fetch(`/consiglio/${idGallina}`, {
        method: 'POST'
    })
        .then(response => response.json())  // Prendi la risposta come JSON
        .then(data => {
            const messaggio = data.message; // Il messaggio che abbiamo restituito nel backend
            const messaggioSalute = document.getElementById('messaggioSalute');

            // Mostra il messaggio nella pagina
            messaggioSalute.textContent = messaggio;
        })
        .catch(error => {
            console.error("Errore:", error);
            alert("Errore nella comunicazione con il server.");
        });
}

