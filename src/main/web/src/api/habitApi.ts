import FetchUtil from "../utils/fetchUtil.ts";
import {Result} from "../utils/entity.ts";

export function getHabitList(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/habit/list', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveHabit(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/habit/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function deleteHabit(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/habit/del', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getHabitLogList(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/habitLog/list', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveHabitLog(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/habitLog/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function deleteHabitLog(data: any): Promise<Result> {

    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/habitLog/del', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}