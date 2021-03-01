package com.def.service;

import com.def.config.JsonData;
import com.def.enums.SendEnum;

public interface NotifyService {

    JsonData send(SendEnum type, String to);

}
