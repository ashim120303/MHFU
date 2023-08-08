// Modal window
let modal = document.getElementById("myModal");
let btn = document.getElementById("myBtn");
let span = document.getElementsByClassName("close")[0];
btn.onclick = function() {
  modal.style.display = "block";
}
span.onclick = function() {
  modal.style.display = "none";
}
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

// Смена темы
document.querySelectorAll('.theme-option').forEach(function(option) {
    option.addEventListener('click', function() {
        let theme = this.dataset.theme;
        document.getElementById('theme-link').href = theme;
        localStorage.setItem('theme', theme); // сохраняем выбор темы
    });
});
