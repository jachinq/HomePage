import fetchUtil from "../utils/fetchUtil.ts";
import { Result } from "../utils/entity.ts";

export function bing(keyword: string): Promise<Result> {
    const url = "/api/cors/bing";
    return new Promise((resolve, reject) =>
        fetchUtil.get(url, { keyword })
            .then(res => res.json())
            .then(res => resolve(res))
            .catch(err => reject(err))
    );
}
