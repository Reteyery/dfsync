package com.suning.speaker.httplibrary;
/**
 * @User: 李扬
 * @data: 2019/10/17
 */
public enum Env {
    SIT("SIT"),
    PRE("PRE"),
    PRD("PRD");

    private String env;

    private Env(String var3) {
        this.env = var3;
    }

    public String toString() {
        return this.env;
    }
}
