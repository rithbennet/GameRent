// Get the modals
var addGameModal = document.getElementById("addGameModal");
var editGameModal = document.getElementById("editGameModal");
var deleteGameModal = document.getElementById("deleteGameModal");

// Get the buttons that open the modals
var addBtn = document.getElementById("openModalButton");
var editBtns = document.getElementsByClassName("editButton");
var deleteBtns = document.getElementsByClassName("deleteButton");

// Get the <span> elements that close the modals
var spans = document.getElementsByClassName("close");

// When the user clicks the button, open the add game modal
addBtn.onclick = function () {
  addGameModal.style.display = "flex";
};

// When the user clicks on <span> (x), close the modals
for (var i = 0; i < spans.length; i++) {
  spans[i].onclick = function () {
    addGameModal.style.display = "none";
    editGameModal.style.display = "none";
    deleteGameModal.style.display = "none";
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
  if (event.target == deleteGameModal) {
    deleteGameModal.style.display = "none";
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
        editGameModal.style.display = "flex";
      });
  };
}

// When the user clicks the delete button, open the delete confirmation modal
for (var i = 0; i < deleteBtns.length; i++) {
  deleteBtns[i].onclick = function () {
    var gameId = this.getAttribute("data-id");
    var gameTitle = this.getAttribute("data-title");
    document.getElementById("deleteMessage").innerText =
      "Are you sure you want to delete " + gameTitle + "?";
    document.getElementById("confirmDeleteButton").onclick = function () {
      fetch("/games/" + gameId, {
        method: "DELETE",
      }).then(() => {
        window.location.reload();
      });
    };
    deleteGameModal.style.display = "flex";
  };
}

// When the user clicks the cancel button, close the delete confirmation modal
document.getElementById("cancelDeleteButton").onclick = function () {
  deleteGameModal.style.display = "none";
};
