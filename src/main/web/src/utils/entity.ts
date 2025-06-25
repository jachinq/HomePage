export interface Result {
    code: number;
    message: string;
    success: boolean;
    total: number;
    data: any;
    page: {
        total: number;
        totalPages: number;
    }
}