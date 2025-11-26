/* UI helpers: toggles, small toasts, init helpers */
// Global fetch wrapper: attach CSRF header automatically for non-GET requests
(function(){
  try {
    const originalFetch = window.fetch.bind(window);
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = csrfMeta ? csrfMeta.getAttribute('content') : null;
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN';

    if (originalFetch && csrfToken) {
      window.fetch = function(input, init) {
        init = init || {};
        const method = (init.method || 'GET').toString().toUpperCase();
        if (method !== 'GET' && method !== 'HEAD') {
          // normalize headers into a plain object when possible
          if (!init.headers) {
            init.headers = {};
          }
          if (init.headers instanceof Headers) {
            init.headers.set(csrfHeader, csrfToken);
          } else if (Array.isArray(init.headers)) {
            init.headers.push([csrfHeader, csrfToken]);
          } else {
            init.headers[csrfHeader] = csrfToken;
          }
        }
        return originalFetch(input, init);
      };
    }
  } catch (e) {
    console.warn('CSRF fetch wrapper not installed:', e);
  }
})();
// Central action registry (templates or modules can register handlers)
// Moved to top-level so templates that run inline scripts can register handlers
// before DOMContentLoaded fires.
const __actionRegistry = {};
window.registerAction = function(name, fn) { if (!name || typeof fn !== 'function') return; __actionRegistry[name] = fn; try { console.debug('[ui] registerAction:', name, '->', fn.name || 'anonymous'); } catch(e){} };
window.unregisterAction = function(name) { if (!name) return; delete __actionRegistry[name]; try { console.debug('[ui] unregisterAction:', name); } catch(e){} };

document.addEventListener('DOMContentLoaded', function(){
  // show unobtrusive toasts by data attribute
  document.querySelectorAll('[data-ui-toast]').forEach(function(el){
    const msg = el.getAttribute('data-ui-toast');
    const toastEl = document.createElement('div');
    toastEl.className = 'toast align-items-center text-bg-primary border-0';
    toastEl.setAttribute('role','alert');
    toastEl.setAttribute('aria-live','assertive');
    toastEl.setAttribute('aria-atomic','true');
    toastEl.innerHTML = '<div class="d-flex"><div class="toast-body">'+msg+'</div><button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button></div>';
    const container = document.querySelector('.toast-container') || (function(){const c=document.createElement('div');c.className='toast-container';document.body.appendChild(c);return c;})();
    container.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, {delay:3000});
    toast.show();
  });
  
  // Convert any remaining <a href="/logout"> elements into POST submits with CSRF
  document.querySelectorAll('a[href="/logout"]').forEach(function(anchor){
    anchor.addEventListener('click', function(e){
      e.preventDefault();
      // Attempt to read CSRF token from meta
      const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
      const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

      // Create a form and submit
      const form = document.createElement('form');
      form.method = 'post';
      form.action = '/logout';
      form.style.display = 'none';

      if (csrfTokenMeta) {
        const input = document.createElement('input');
        input.type = 'hidden';
        // If header name is available, still use the parameter name expected by Spring ("_csrf")
        input.name = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : '_csrf';
        // Use the conventional parameter name for form input
        input.name = '_csrf';
        input.value = csrfTokenMeta.getAttribute('content');
        form.appendChild(input);
      }

      document.body.appendChild(form);
      form.submit();
    });
  });

  

  // Delegated generic handler for elements with class .btn-accion
  $(document).on('click', '.btn-accion', function(e){
    e.preventDefault();
    const $btn = $(this);
    const id = $btn.data('id') || $btn.attr('data-id');
    let action = $btn.data('action') || $btn.attr('data-action');
    if (!action) return;

    action = action.toString().trim();

    // 1) Check registry exact match
    if (__actionRegistry[action]) {
      try { console.debug('[ui] invoking registered action', action, 'id=', id); __actionRegistry[action](id, $btn); } catch(err){ console.error('Error in registered action', action, err); }
      return;
    }

    // 2) Try direct global function name (camelCase or exact)
    if (typeof window[action] === 'function') {
      try { console.debug('[ui] invoking global function', action, 'id=', id); window[action](id, $btn); } catch(err){ console.error('Error calling global function', action, err); }
      return;
    }

    // 3) Normalize to lowercase and try some canonical mappings
    const a = action.toLowerCase();
    const canonical = {
      'ver': 'verProducto', 'verproducto': 'verProducto', 'verusuario': 'verUsuario', 'verregistro': 'verRegistro',
      'editar': 'editarProducto', 'editarproducto': 'editarProducto', 'editarusuario': 'editarUsuario',
      'movimiento': 'nuevoMovimiento', 'nuevomovimiento': 'nuevoMovimiento',
      'historial': 'verHistorial', 'verhistorial': 'verHistorial',
      'imprimirregistro': 'imprimirRegistro', 'generarpdf': 'generarPDF',
      'confirmarcita': 'confirmarCita'
    };
    const mapped = canonical[a];
    if (mapped && typeof window[mapped] === 'function') {
      try { console.debug('[ui] invoking mapped function', mapped, 'for', action, 'id=', id); window[mapped](id, $btn); } catch(err){ console.error('Error calling mapped function', mapped, err); }
      return;
    }

    // 4) Try prefix-based routing (e.g., 'ver*' -> verProducto)
    if (a.startsWith('ver')) {
      if (typeof window.verProducto === 'function') { try { console.debug('[ui] prefix ver -> verProducto, id=', id); window.verProducto(id, $btn); } catch(err){ console.error(err); } return; }
      if (typeof window.ver === 'function') { try { console.debug('[ui] prefix ver -> ver, id=', id); window.ver(id, $btn); } catch(err){ console.error(err); } return; }
    }
    if (a.startsWith('editar')) { if (typeof window.editarProducto === 'function') { try { window.editarProducto(id, $btn); } catch(err){ console.error(err); } return; } }
    if (a.startsWith('nuevo') || a.startsWith('mov')) { if (typeof window.nuevoMovimiento === 'function') { try { window.nuevoMovimiento(id, $btn); } catch(err){ console.error(err); } return; } }

    console.warn('No handler found for action', action, ' (id=', id, ')');
  });

  // Card clickable navigation
  $(document).on('click', '.card-clickable', function(e){
    const href = $(this).data('href') || $(this).attr('data-href');
    if (href) { window.location.href = href; }
  });

  // Bind header dark mode toggle if function exists
  $('#darkModeToggle').on('click', function(){ if (typeof window.toggleDarkMode === 'function') window.toggleDarkMode(); });

  // Bind generic buttons by id if functions exist (non-exhaustive)
  $('#btnRecargarCitas').on('click', function(){ if (typeof window.cargarCitasAPI === 'function') window.cargarCitasAPI(); });
  $('#btnExportarReporte').on('click', function(){ if (typeof window.exportarReporte === 'function') window.exportarReporte(); });
  $('#btnActualizarReportes').on('click', function(){ if (typeof window.actualizarDatos === 'function') window.actualizarDatos(); });
  $('#btnGenerarReportes').on('click', function(){ if (typeof window.generarReportes === 'function') window.generarReportes(); });
  $('#btnLimpiarUsuarios').on('click', function(){ if (typeof window.limpiarFiltros === 'function') window.limpiarFiltros(); });
  $('#btnImprimirDetalle').on('click', function(){ if (typeof window.imprimirDetalle === 'function') window.imprimirDetalle(); });
  $('#btnAplicarHistorial').on('click', function(){ if (typeof window.aplicarFiltros === 'function') window.aplicarFiltros(); });
  $('#btnLimpiarHistorial').on('click', function(){ if (typeof window.limpiarFiltros === 'function') window.limpiarFiltros(); });

});
