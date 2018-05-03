(()=>{
  function appendNav() {
    let nav = document.querySelector('.page-wrapper #anchors-navbar');
    if (!nav) return;

    nav.style.visibility = 'visible';
    document.querySelector('.book-body').append(nav);
  }

  appendNav();
  window.addEventListener('mousemove', appendNav);
})()
