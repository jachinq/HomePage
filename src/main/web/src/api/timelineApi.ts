import FetchUtil from "../utils/fetchUtil.ts";
import {Result} from "../utils/entity.ts";
import {TimelineEntity, TimelineQuery} from "../interface/timeline.ts";

export function getTimelineList(params?: TimelineQuery): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/timeline/list', params).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineListWithPagination(page: number, size: number): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(`/api/timeline/list/${page}/${size}`, {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineById(id: number): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(`/api/timeline/${id}`, {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function saveTimeline(data: TimelineEntity): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.post('/api/timeline/save', data).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function deleteTimeline(id: number): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.delete(`/api/timeline/${id}`).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineByDateRange(startDate: string, endDate: string): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/timeline/date-range', {startDate, endDate}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineByCategory(category: string): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(`/api/timeline/category/${category}`, {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineByPriority(priority: string): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get(`/api/timeline/priority/${priority}`, {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function searchTimeline(keyword: string): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/timeline/search', {keyword}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineCategories(): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/timeline/categories', {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
}

export function getTimelineStatistics(): Promise<Result> {
    return new Promise((resolve, reject) =>
        FetchUtil.get('/api/timeline/statistics', {}).then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    );
} 