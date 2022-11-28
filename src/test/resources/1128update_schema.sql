-- JPA规范要求，字段大小写必须转为下划线分割
ALTER TABLE cdsa.rslt CHANGE IsSqxRslt Is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';