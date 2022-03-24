package com.mt.auth.utils;


import java.util.HashMap;

public class JsonResponse extends HashMap<String, Object> {

  private static final int SUCCESS = 0;

  private static final String KEY_CODE = "code";
  private static final String KEY_MESSAGE = "message";

  public JsonResponse setSuccess(String msg) {
    this.put(KEY_CODE, SUCCESS);
    if (msg != null && !msg.isEmpty()) {
      this.put(KEY_MESSAGE, msg);
    } else {
      this.put(KEY_MESSAGE, "SUCCESS");
    }
    return this;
  }

  /**
   * 成功code为0，失败code > 0
   */
  public JsonResponse setFailed(int code, String msg) {
    this.put(KEY_CODE, code);
    if (msg != null && !msg.isEmpty()) {
      this.put(KEY_MESSAGE, msg);
    } else {
      this.put(KEY_MESSAGE, "FAILED");
    }
    return this;
  }

}
