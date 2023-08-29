document.addEventListener('DOMContentLoaded', function() {
    const burgerMenu = document.getElementById('burger-menu');
    const hideMenu = document.getElementById('hide-menu');
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    burgerMenu.addEventListener('click', () => {
      sidebar.classList.toggle('show');
      content.classList.toggle('hide');
    });
    hideMenu.addEventListener('click', () => {
      content.classList.toggle('show-content');
      sidebar.classList.toggle('hide-sidebar');
    });
});