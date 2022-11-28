package com.agilent.csda.acl.service.impl;

import com.agilent.csda.acl.dao.RsltDao;
import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.service.RsltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class RsltServiceImpl implements RsltService {

    @Autowired
    private RsltDao rsltDao;

    @Override
    public List<Rslt> doFindAll() {
        List<Rslt> all = rsltDao.findAll();
        return all;
    }
}
