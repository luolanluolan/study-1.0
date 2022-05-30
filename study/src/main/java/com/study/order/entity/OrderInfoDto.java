package com.study.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "order_info")
@EntityListeners(AuditingEntityListener.class)
public class OrderInfoDto implements Serializable {

  //AUTO： 主键由程序控制，是默认选项，不设置即此项。
  //IDENTITY：主键由数据库自动生成，即采用数据库ID自增长的方式，Oracle不支持这种方式
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Id
  @Column(name = "id")
  private Integer id;
  @ApiModelProperty("订单号")
  @Column(name = "order_no",unique = true)
  private String orderNo;
  @ApiModelProperty("订单金额")
  @Column(name = "order_money")
  private BigDecimal orderMoney = new BigDecimal("0");
  @ApiModelProperty("订单状态")
  @Column(name = "order_status")
  private String orderStatus;
  @ApiModelProperty("创建时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
  @CreatedDate
  @Column(name = "create_time")
  private Timestamp createTime;

}
