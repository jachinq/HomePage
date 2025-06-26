export enum AppType {
    SYS = 1,
    APP = 2,
    Bookmark = 3,
}



export interface AppSet {
    id?: number
    userId?: number
    type?: number
    status?: number
    port?: number
    sort?: number
    name?: string
    description?: string
    icon?: string
    category?: string
    outerUrl?: string
}

export interface AppConfig {
    id?: number
    userId?: number
    blankApp?: boolean
    blankBookmark?: boolean
    innerMode?: boolean // 是否内网模式-前端独有
    innerDomain?: string
    outerDomain?: string
}

