package com.googlecode.tlb.support.cruise;

public class ResponseResult {
    private int returnCode;
    private String responseBody;

    public ResponseResult(int statusCode, String responseBody) {
        this.returnCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
