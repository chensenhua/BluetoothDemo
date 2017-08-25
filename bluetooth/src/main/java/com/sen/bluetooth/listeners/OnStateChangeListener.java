package com.sen.bluetooth.listeners;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public interface OnStateChangeListener {
    void enabled();
    void enabling();
    void disabled();
    void disabling();
}
