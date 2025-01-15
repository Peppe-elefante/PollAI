function mostraDettagli(button) {
    console.log("Pulsante cliccato");
    // Reset messaggi e contatto veterinario
    const messaggioSalute = document.getElementById('messaggioSalute');
    const dettagliVeterinario = document.getElementById('dettagliVeterinario');

    messaggioSalute.textContent = '';  // Reset del consiglio precedente
    messaggioSalute.style.color = '';  // Reset colore messaggio
    dettagliVeterinario.style.display = 'none';

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

    // Invia una richiesta al server per ottenere un consiglio
    fetch(`/consiglio/${idGallina}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            const messaggio = data.message;
            const messaggioSalute = document.getElementById('messaggioSalute');

            // Mostra il messaggio nella pagina
            messaggioSalute.textContent = messaggio;

            // Imposta il colore del messaggio in base alla salute
            if (messaggio.includes("not")) {
                messaggioSalute.style.color = "red";
            } else {
                messaggioSalute.style.color = "green";
            }
        })
        .catch(error => {
            console.error("Errore:", error);
            alert("Errore nella comunicazione con il server.");
        });
}

