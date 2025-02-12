package com.ksy.exam.spring2502_heathyfood_test.vo;

import lombok.Data;
import lombok.Getter;

// 형식을 미리 지정하지 않음
public class ResultData<DT> {
    @Getter
    private String resultCode;
    @Getter
    private String msg;
    @Getter
    private DT data;

    public static <DT>ResultData<DT> from(String resultCode, String msg) {
        return from(resultCode,msg,null);
    }

    public static <DT>ResultData<DT> from(String resultCode, String msg, DT data) {
        ResultData<DT> rd=new ResultData<DT>();
        rd.resultCode = resultCode;
        rd.msg = msg;
        rd.data = data;

        return rd;
    }

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    public boolean isFail(){
        return !isSuccess();
    }

    public static <DT>ResultData<DT> newData(ResultData rd, DT newData) {
        return from(rd.getResultCode(),rd.getMsg(),newData);
    }
}
