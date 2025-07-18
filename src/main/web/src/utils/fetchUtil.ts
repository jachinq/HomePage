import storage from './storage';

// 判断是开发模式还是生产模式
const isDev = process.env.NODE_ENV === 'development';
// console.log('isDev:', isDev);

// 根据环境变量判断请求地址
function procUrl(url: string): string {
    if (isDev) {
        return 'http://127.0.0.1:8080' + url;
    }
    return url;
}

// 请求头附带token处理
function procHeaders(url: string, headers: any = {}) {
    if (url.startsWith('/api/auth')) {
        return headers;
    }
    const token = storage.getItem('token');
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }
    return headers;
}

// 封装请求
export default {

    get: (url: string, data: any, headers: any = {}) => {
        if (data) {
            url += '?' + Object.keys(data).map(key => key + '=' + data[key]).join('&');
        }
        return fetch(procUrl(url), {
            method: 'GET',
            headers: {...procHeaders(url, headers)},
        });
    },

    post: (url: string, data: any, headers: any = {}) => {
        let dataStr = null;
        let requestHeaders = {...procHeaders(url, headers)};
        
        if (data instanceof FormData) {
            dataStr = data;
            // 对于 FormData，不要设置 Content-Type，让浏览器自动设置
        } else {
            dataStr = JSON.stringify(data);
            requestHeaders['Content-Type'] = 'application/json';
        }
        
        return fetch(procUrl(url), {
            method: 'POST',
            headers: requestHeaders,
            body: dataStr,
        });
    },

    delete: (url: string, headers: any = {}) => {
        return fetch(procUrl(url), {
            method: 'DELETE',
            headers: {...procHeaders(url, headers)},
        });
    }
}
