export interface TimelineEntity {
    id?: number;
    userId?: number;
    title?: string;
    description?: string;
    eventDate?: string;
    eventTime?: string;
    category?: string;
    priority?: string;
    tags?: string;
    attachments?: string;
    isPublic?: boolean;
    createTime?: string;
    updateTime?: string;
}

export interface TimelineQuery {
    keyword?: string;
    category?: string;
    priority?: string;
    startDate?: string;
    endDate?: string;
    isPublic?: boolean;
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
}

export interface TimelineStatistics {
    total: number;
    categoryCount: number;
    categoryList: string[];
} 