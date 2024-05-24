package com.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author: Shawn i
 * @description: 加密 entity
 * @date: 2024/5/24 12:03
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cipher {
    private String text;
    private String alg;
}
