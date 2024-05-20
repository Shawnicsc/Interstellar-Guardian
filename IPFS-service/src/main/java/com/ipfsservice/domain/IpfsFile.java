package com.ipfsservice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName ipfsfile
 */
@TableName(value ="ipfsfile")
@Data
public class IpfsFile implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件Hash值
     */
    private String hashcode;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 共享密钥
     */
    private String secretKey;

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 创建时间
     */
    @TableField("Create_Time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("Modified_Time")
    private Date modifiedTime;

    /**
     * 逻辑删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}