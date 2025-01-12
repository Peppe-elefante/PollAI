document.addEventListener("DOMContentLoaded", function() {
    var h2 = document.querySelector(".cibo-title");
    var ciboDisplay = document.querySelector(".cibo-display ul");

    if (ciboDisplay.children.length === 0) {
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