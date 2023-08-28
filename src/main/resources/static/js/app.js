'use strict'; // Использование строгого режим

const addNotes = document.querySelector('.add-notes');
const addNotesBody = document.querySelector('.create-note');
const sidebar = document.querySelector('.sidebar');
const notesBody = document.querySelector('.content');

// Добавление слушателя события клика на addNotes
addNotes.addEventListener('click', () => {
// С помощью toggle при клике будут добавляться и убираться классы
  addNotesBody.classList.toggle('show');
  notesBody.classList.toggle('show');
  sidebar.classList.toggle('hide');
});
// Добавление слушателя события изменения размера окна браузера.
window.addEventListener('resize', () => {
  if (window.innerWidth > 768) {
    addNotesBody.classList.remove('show');
    notesBody.classList.remove('show');
    sidebar.classList.remove('hide');
  }
});

// Выбор всех элементов на странице, у которых установлен атрибут placeholder, перебор элементов в коллекцию.
document.querySelectorAll('[placeholder]').forEach((element) => {
// Сохранение значения атрибута placeholder в свойстве dataset элемента.
  element.dataset.placeholder = element.getAttribute('placeholder');
// Добавление слушателя события focus. Удаление placeholder у элемента.
  element.addEventListener('focus', () => element.removeAttribute('placeholder'));
// Добавление слушателя события blur. Событие blur возникает, когда элемент теряет фокус. Восстановление placeholder.
  element.addEventListener('blur', () => element.setAttribute('placeholder', element.dataset.placeholder));
});


const fileInput = document.getElementById('file-input');
const previewImage = document.getElementById('preview-image');
const preview = document.querySelector('.preview');
const imageDelButton = document.getElementById('image-del-button');
const src = previewImage.getAttribute('src');

fileInput.addEventListener('change', () => {
  const file = fileInput.files[0];
  const reader = new FileReader();
  reader.onload = (event) => (previewImage.src = event.target.result);
  reader.readAsDataURL(file);
  preview.classList.toggle('preview-show', !!file);
  imageDelButton.style.display = file ? 'inline-block' : 'none';
});

function clearInput() {
  fileInput.value = '';
  previewImage.src = '';
  preview.classList.remove('preview-show');
  document.getElementById('delete-image-input').value = 'true';
  imageDelButton.style.display = 'none';
}

imageDelButton.style.display = src !== '/uploads/null' || fileInput.files.length > 0 ? 'block' : 'none';

// Показать - Скрыть пароль
function togglePassword(event, inputId, toggleId) {
    event.preventDefault();
    let x = document.getElementById(inputId);
    let img = document.getElementById(toggleId);
    if (x.type === "password") {
        x.type = "text";
        img.src = "/../img/hidden.png";
    } else {
        x.type = "password";
        img.src = "/../img/eye.png";
    }
}


// Адаптив
