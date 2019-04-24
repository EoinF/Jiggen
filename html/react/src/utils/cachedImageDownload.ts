
export default function downloadImage (src: string, setTotalBytes: Function, setDownloadedBytes: Function,
    onFailure: Function, onSuccess: Function) {
        const xhr = new XMLHttpRequest();
        let notifiedNotComputable = false;
      
        xhr.open('GET', src, true);
        xhr.responseType = 'arraybuffer';
      
        xhr.onprogress = function (ev) {
          if (ev.lengthComputable) {
              setTotalBytes(ev.total);
          } else {
            if (!notifiedNotComputable) {
              notifiedNotComputable = true;
              setTotalBytes(-1);
            }
          }
          setDownloadedBytes(ev.loaded);
        }
      
        xhr.onloadend = function () {
          if (!xhr.status.toString().match(/^2/)) {
            onFailure(xhr.statusText);
          } else {
            const options: any = {};
            var headers = xhr.getAllResponseHeaders();
            var m = headers.match(/^Content-Type\:\s*(.*?)$/mi);
      
            if (m && m[1]) {
              options.type = m[1];
            }
      
            var blob = new Blob([this.response], options);
      
            onSuccess(window.URL.createObjectURL(blob));
          }
        }
      
        xhr.send();
  };
  
  export function downloadImageAsPromise(src: string) {
    return new Promise<string>((resolve, reject) => {
      downloadImage(src, 
        () => {}, 
        () => {},
        reject,
        resolve)
    });
  }