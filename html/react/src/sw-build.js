const workboxBuild = require('workbox-build');
const buildPath = process.env.BUILD_PATH || 'build';

// NOTE: This should be run *AFTER* all your assets are built
const buildSW = () => {
  // This will return a Promise
  return workboxBuild.injectManifest({
    swSrc: 'src/sw.js', // this is your sw template file
    swDest: buildPath + '/sw.js', // this will be created in the build step
    globDirectory: buildPath,
    globPatterns: [
      '**\/*.{js,css,html,png,txt,frag,vert,fnt,atlas,json,glsl}',
    ],
    maximumFileSizeToCacheInBytes: 4 * 1024 * 1024 // 4MB
  }).then(({count, size, warnings}) => {
    // Optionally, log any warnings and details.
    warnings.forEach(console.warn);
    console.log(`${count} files will be precached, totaling ${size} bytes.`);
  });
}
buildSW();