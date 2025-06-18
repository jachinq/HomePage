import FetchUtil from "../utils/fetchUtil.ts";
import {Result} from "../utils/entity.ts";

const prefix = "/api/auth"

export function login(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post(prefix + '/login', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function register(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post(prefix + '/register', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function validate(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post(prefix + '/validate', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

// 注销
export function logout(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post(prefix + '/logout', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

// 获取用户信息
export function getUserInfo(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(prefix + '/user/info', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}