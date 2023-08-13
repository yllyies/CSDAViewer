package com.agilent.iad.service;

import com.agilent.iad.model.Bulletin;

import java.util.List;

/**
 * 公告信息服务接口
 *
 * @author lifang
 * @since 2023-08-13
 */
public interface BulletinService {

    Bulletin doSave(Bulletin bulletin);

    List<Bulletin> doFindAll();

    void doDelete(String id);
}
