package com.tanhua.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackList extends BasePojo{

    private Long id;
    private Long userId;
    private Long blackUserId;
}
