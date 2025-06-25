export enum AppType {
    SYS = 1,
    APP = 2,
    Bookmark = 3,
}



export interface AppSet {
    userId?: number
    type?: number
    status?: number
    port?: number
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
    innerDomain?: string
    outerDomain?: string
}

