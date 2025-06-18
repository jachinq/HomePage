import FetchUtil from "../utils/fetchUtil.ts";
import {Result} from "../utils/entity.ts";

export function login(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/auth/login', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function register(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/auth/register', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

// 注销
export function logout(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/auth/logout', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

// 获取用户信息
export function getUserInfo(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/auth/user/info', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}