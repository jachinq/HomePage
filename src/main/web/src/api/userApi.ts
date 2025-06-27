import { Result } from "@/utils/entity";
import fetchUtil from "@/utils/fetchUtil";

const prefix = "/api/user"


// 获取用户信息
export function getUserInfo(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.get(prefix + '/info', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveUserInfo(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.post(prefix + '/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function setUserAvatar(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.post(prefix + '/setUserAvatar', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function changePassword(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.post(prefix + '/changePassword', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}