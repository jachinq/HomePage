package com.home.homepage.entity.modal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class HabitLogListModel extends ListModal {
    private Integer year;
    private Integer month;
    private Integer habitId;
}
