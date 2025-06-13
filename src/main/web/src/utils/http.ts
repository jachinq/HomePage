

// 判断是开发模式还是生产模式
const isDev = process.env.NODE_ENV === 'development';
console.log('isDev:', isDev);

// 根据环境变量判断请求地址
function procUrl(url: string): string {
  if (isDev) {
    return 'http://127.0.0.1:8080' + url;
  }
  return url;
}

export function get(url: string, headers: any = {}) {
  return fetch(procUrl(url), {
    method: 'GET',
    headers: headers,
  });
}

export function post(url: string, data: any, headers: any = {}) {
  return fetch(procUrl(url), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...headers,
    },
    body: JSON.stringify(data),
  });
}