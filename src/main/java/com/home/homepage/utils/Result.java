package com.home.homepage.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 11:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result {
    private int code;
    private int total;
    private String msg;
    private Boolean success;
    private Object data;

    public static Result success() {
        return success("success");
    }

    public static Result success(Object data) {
        return success("ok", data);
    }

    public static Result success(String msg) {
        return success(msg, null);
    }

    public static <T> Result success(List<T> list, int total) {
        Result success = success(list);
        success.setTotal(total);
        return success;
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(false);
        return result;
    }
}
