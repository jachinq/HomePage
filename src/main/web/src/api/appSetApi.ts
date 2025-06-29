import FetchUtil from "../utils/fetchUtil";
import {Result} from "../utils/entity";

export function getAppSetList(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/appSet/list', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveAppSet(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/appSet/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}
export function batchSaveAppSet(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/appSet/batchSave', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function deleteAppSet(data: any): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/appSet/delete', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
} 