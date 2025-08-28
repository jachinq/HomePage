package com.home.homepage.entity.modal;

import com.home.homepage.entity.HabitLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Jachin
 * @since 25-08-20 10:05
 */
@Data
@ToString
@Accessors(chain = true)
public class BatchSaveHabitLog {
    private List<HabitLog> addList;
    private List<HabitLog> setList;
    private List<Long> delList;
}
