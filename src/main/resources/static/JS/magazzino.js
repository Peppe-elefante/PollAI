document.addEventListener("DOMContentLoaded", function() {
    // Trova l'elemento h2 con la classe "cibo-registrato-title"
    var h2 = document.querySelector(".cibo-title");
    // Trova l'elemento che contiene l'elenco di cibi
    var ciboDisplay = document.querySelector(".cibo-display ul");

    // Verifica se la lista di cibi è vuota
    if (ciboDisplay.children.length === 0) {
        // Nasconde l'elemento h2 con la classe "cibo-registrato-title"
        h2.style.display = "none";
    }
});

document.addEventListener("DOMContentLoaded", function () {
    var h2 = document.querySelector(".farmaci-title");
    var farmaciDisplay = document.querySelector(".farmaci-display ul");

    if (farmaciDisplay.children.length === 0) {
        h2.style.display = "none";
    }
});