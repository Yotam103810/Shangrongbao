package com.chen.srb.sms.service;

import java.util.Map;

public interface SmsService {
    void send(String mobile, String templateCode, Map<String, Object> param);
}
