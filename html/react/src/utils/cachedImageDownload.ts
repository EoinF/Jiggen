
export default async function downloadImage (src: string, setTotalBytes: Function, setDownloadedBytes: Function,
    onFailure: Function, onSuccess: Function) {
      const response = await fetch(src, {mode: 'cors'});

      if (response.status >= 300 || response.status < 200) {
        onFailure(response.statusText)
        return response;
      }

      // Clone the response because the body can only be read once
      const clonedResponse = response.clone();
      const reader = response.body!.getReader();
        
      setTotalBytes(parseInt(response.headers.get('content-length') || '-1'));

      const chunks = [];
      let downloadedBytes = 0;

      while(true) {
        const {done, value} = await reader.read();
                
        if (done) {
          break;
        }

        chunks.push(value);
        downloadedBytes += value.length;
        setDownloadedBytes(downloadedBytes);
      }
      const blob = new Blob(chunks);
      onSuccess(window.URL.createObjectURL(blob));
      
      return clonedResponse;
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