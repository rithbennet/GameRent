// Get the modals
var addGameModal = document.getElementById("addGameModal");
var editGameModal = document.getElementById("editGameModal");

// Get the buttons that open the modals
var addBtn = document.getElementById("openModalButton");
var editBtns = document.getElementsByClassName("editButton");

// Get the <span> elements that close the modals
var spans = document.getElementsByClassName("close");

// When the user clicks the button, open the add game modal
addBtn.onclick = function () {
  addGameModal.style.display = "block";
};

// When the user clicks on <span> (x), close the modals
for (var i = 0; i < spans.length; i++) {
  spans[i].onclick = function () {
    addGameModal.style.display = "none";
    editGameModal.style.display = "none";
  };
}

// When the user clicks anywhere outside of the modals, close them
window.onclick = function (event) {
  if (event.target == addGameModal) {
    addGameModal.style.display = "none";
  }
  if (event.target == editGameModal) {
    editGameModal.style.display = "none";
  }
};

// When the user clicks the edit button, open the edit game modal
for (var i = 0; i < editBtns.length; i++) {
  editBtns[i].onclick = function () {
    var gameId = this.getAttribute("data-id");
    // Fetch game details using gameId and populate the form fields
    fetch("/api/games/" + gameId)
      .then((response) => response.json())
      .then((game) => {
        document.getElementById("editGameId").value = game.id;
        document.getElementById("editTitle").value = game.title;
        document.getElementById("editGenre").value = game.genre;
        document.getElementById("editPlatform").value = game.platform;
        document.getElementById("editPricePerDay").value = game.pricePerDay;
        document.getElementById("editReleaseDate").value = game.releaseDate;
        document.getElementById("editRequirementMemory").value =
          game.requirementMemory;
        document.getElementById("editRequirementProcessor").value =
          game.requirementProcessor;
        document.getElementById("editRequirementGraphics").value =
          game.requirementGraphics;
        document.getElementById("editRequirementStorage").value =
          game.requirementStorage;
        document.getElementById("editDescription").value = game.description;
        editGameModal.style.display = "block";
      });
  };
}
