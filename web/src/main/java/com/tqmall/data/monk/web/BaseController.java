package com.tqmall.data.monk.web;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by zxg on 16/2/2.
 * 11:38
 */
 public class BaseController {
    @Value(value = "${web.version}")
    protected String webVersion;
}
