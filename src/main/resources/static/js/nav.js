document.addEventListener('DOMContentLoaded', function () {
  var btn = document.querySelector('.nav-toggle');
  var menu = document.getElementById('nav-menu');
  if (!btn || !menu) return;
  btn.addEventListener('click', function () {
    var abierto = menu.classList.toggle('open');
    btn.setAttribute('aria-expanded', abierto ? 'true' : 'false');
  });
  menu.addEventListener('click', function (e) {
    if (e.target.closest('a')) { menu.classList.remove('open'); btn.setAttribute('aria-expanded','false'); }
  });
});
