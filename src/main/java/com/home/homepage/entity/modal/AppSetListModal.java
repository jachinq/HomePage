package com.home.homepage.entity.modal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Jachin
 * @since 25-06-17 10:05
 */
@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AppSetListModal extends ListModal{
    private String name;
    private Integer type = -1;
}
