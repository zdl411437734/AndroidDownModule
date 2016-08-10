package com.etongwl.androiddownmodule.service;


/**
 * 下载状态枚举
 */
public enum ServiceDownStatus {
    STATUS_STARTED, STATUS_CONNECTING, STATUS_CONNECTED, STATUS_PROGRESS,
    STATUS_PAUSED, STATUS_CANCEL, STATUS_COMPLETE, STATUS_FAILED
}
