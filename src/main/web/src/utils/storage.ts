// 从 storge 中获取 token
function getToken(): string | null {
    const token = localStorage.getItem('token');
    return token || null;
}
// 保存 token
function setToken(token: string): void {
    localStorage.setItem('token', token);
}
// 删除 token
function removeToken(): void {
    localStorage.removeItem('token');
}

function setItem(key: string, value: string): void {
    localStorage.setItem(key, value);
}

function getItem(key: string): string | null {
    return localStorage.getItem(key) || null;
}

export default {
    getToken,
    setToken,
    removeToken,
    setItem,
    getItem
}