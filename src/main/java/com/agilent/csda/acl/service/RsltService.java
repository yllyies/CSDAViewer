package com.agilent.csda.acl.service;

import com.agilent.csda.acl.model.Rslt;
import com.agilent.csda.acl.model.User;

import java.util.List;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface RsltService {

    List<Rslt> doFindAll();
}
