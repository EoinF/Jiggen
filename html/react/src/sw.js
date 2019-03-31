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
        
        workbox.routing.registerRoute(
            /\.(?:png|gif|jpg|jpeg)$/,
            workbox.strategies.cacheFirst({
            cacheName: 'images',
            plugins: [
                new workbox.expiration.Plugin({
                  maxEntries: 60,
                  maxAgeSeconds: 30 * 24 * 60 * 60, // 30 Days
                }),
              ],
            })
        );

        
        workbox.routing.registerRoute(
          /api\.jiggen\.app\/?[a-z\-]*$/,
          workbox.strategies.cacheFirst({
          cacheName: 'baseRoutes',
          plugins: [
              new workbox.expiration.Plugin({
                maxEntries: 10,
                maxAgeSeconds: 1 * 24 * 60 * 60, // 30 Days
              }),
            ],
          })
        );
        
        workbox.routing.registerRoute(
          /api\.jiggen\.app\/templates\/[0-9a-f\-]*/,
          workbox.strategies.cacheFirst({
          cacheName: 'templates',
          plugins: [
              new workbox.expiration.Plugin({
                maxEntries: 60,
                maxAgeSeconds: 7 * 24 * 60 * 60, // 7 Days
              }),
          ],
        })
      );

      workbox.routing.registerRoute(
        /api\.jiggen\.app\/atlases\/[0-9a-f\-]*/,
        workbox.strategies.cacheFirst({
        cacheName: 'atlases',
        plugins: [
            new workbox.expiration.Plugin({
              maxEntries: 20,
              maxAgeSeconds: 7 * 24 * 60 * 60, // 7 Days
            }),
        ],
      })
    );
    
    workbox.routing.registerRoute(
      /api\.jiggen\.app\/generated-templates\/[0-9a-f\-]*/,
      workbox.strategies.cacheFirst({
      cacheName: 'generatedTemplates',
      plugins: [
          new workbox.expiration.Plugin({
          maxEntries: 20,
          maxAgeSeconds: 30 * 24 * 60 * 60, // 30 Days
          }),
      ],
    })
  );
        
        workbox.routing.registerRoute(
          /api\.jiggen\.app\/images\/[0-9a-f\-]*/,
          workbox.strategies.cacheFirst({
          cacheName: 'images',
          plugins: [
              new workbox.expiration.Plugin({
                maxEntries: 60,
                maxAgeSeconds: 30 * 24 * 60 * 60, // 30 Days
              }),
          ],
        })
      );
    } else {
      console.log('Workbox could not be loaded. No Offline support');
    }
}