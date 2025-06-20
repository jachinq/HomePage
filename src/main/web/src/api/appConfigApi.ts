import FetchUtil from "../utils/fetchUtil.ts";
import {Result} from "../utils/entity.ts";

const url = '/api/appConfig';

export function getAppConfig(): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(url + '/info', null).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveAppConfig(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post(url + '/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}