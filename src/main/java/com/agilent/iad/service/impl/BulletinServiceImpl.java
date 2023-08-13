package com.agilent.iad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.agilent.iad.model.Bulletin;
import com.agilent.iad.repository.BulletinDao;
import com.agilent.iad.service.BulletinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告信息服务实现
 *
 * @author lifang
 * @since 2023-07-19
 */
@Service
@Slf4j
public class BulletinServiceImpl implements BulletinService {

    @Autowired
    BulletinDao bulletinDao;

    @Override
    public Bulletin doSave(Bulletin bulletin) {
        Bulletin savedItem = bulletinDao.saveAndFlush(bulletin);
        return savedItem;
    }

    @Override
    public List<Bulletin> doFindAll() {
        List<Bulletin> bulletin = bulletinDao.findAll(Sort.by(Sort.Direction.ASC, "updatedDate"));
        return bulletin;
    }

    @Override
    public void doDelete(String id) {
        if (StrUtil.isBlank(id)) {
            return;
        }
        bulletinDao.deleteById(id);
    }
}
