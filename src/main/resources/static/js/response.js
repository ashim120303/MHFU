document.addEventListener('DOMContentLoaded', function() {
    const burgerMenu = document.getElementById('burger-menu');
    const hideMenu = document.getElementById('hide-menu');
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    burgerMenu.addEventListener('click', () => {
      sidebar.classList.add('show');
      content.classList.add('hide');
      sidebar.classList.remove('hide-sidebar'); // Reset if previously hidden
      content.classList.remove('show-content'); // Reset if previously shown
    });

    hideMenu.addEventListener('click', () => {
      content.classList.add('show-content');
      sidebar.classList.add('hide-sidebar');
      sidebar.classList.remove('show'); // Reset if previously shown
      content.classList.remove('hide'); // Reset if previously hidden
    });
});
