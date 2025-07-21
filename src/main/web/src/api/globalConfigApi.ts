import fetchUtil from "../utils/fetchUtil.ts";
import { Result } from "../utils/entity.ts";

const url = "/api/globalConfig";
export function getGlobalConfig(): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.get(url + '/info', { })
            .then(res => res.json())
            .then(res => resolve(res))
            .catch(err => reject(err))
    );
}

export function saveGlobalConfig(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        fetchUtil.post(url + '/save', data)
            .then(res => res.json())
            .then(res => resolve(res))
            .catch(err => reject(err))
    );
}
