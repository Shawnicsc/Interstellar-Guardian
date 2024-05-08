package com.ipfsservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Shawn i
 * @version 1.0
 * @description: 作为文件上传的载体
 * @date 2024/5/6 1:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class fileDao {
    private MultipartFile file;
    private String userId;
}
