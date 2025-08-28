export interface HabitEntity {
    id?: number;
    userId?: number;
    name?: string;
    description?: string;
    doneIcon?: string;
    failIcon?: string;
    frequency?: number;
    startDate?: string;
    endDate?: string;
    createTime?: string;
    updateTime?: string;
}

export interface HabitLogEntity {
    id?: number;
    userId?: number;
    habitId?: number;
    done?: boolean;
    duration?: number;
    extra?: string;
    date?: string;
    createTime?: string;
    updateTime?: string;
}

export interface CalendarProp {
  key: string,
  number: number,
  date: string,
  isToday?: boolean
  habit?: HabitEntity
  logs?: HabitLogEntity[]
}