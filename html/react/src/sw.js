if ('function' === typeof importScripts) {
    importScripts(
      'https://storage.googleapis.com/workbox-cdn/releases/3.5.0/workbox-sw.js'
    );
    /* global workbox */
    if (workbox) {
        console.log('Workbox is loaded');
        /* injection point for manifest files.  */
        workbox.precaching.precacheAndRoute([]);

        /* custom cache rules*/
        workbox.routing.registerNavigationRoute('/index.html', {
                blacklist: [/^\/_/, /\/[^\/]+\.[^\/]+$/],
        });

        self.addEventListener('fetch', (event) => {
          event.respondWith(
            caches.open("customPuzzles").then(cache => {
              return cache.match(event.request).then(function(cachedResponse) {
                if (cachedResponse == null) {
                  return fetch(event.request).then(function(networkResponse) {
                    return networkResponse;
                  });
                } else {
                  return cachedResponse;
                }
              })
            })
          );
        });
    } else {
      console.log('Workbox could not be loaded. No Offline support');
    }
}