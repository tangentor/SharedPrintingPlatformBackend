package org.swunlp.printer.entity.function;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@NoArgsConstructor
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<?> instance(Integer code, String msg, T data){
        return new Result<>(code,msg,data);
    }

    public static <T> Result<?> error(String msg){
        return new Result<>(500,msg,null);
    }

    public static <T> Result<?> error(int code,String msg){
        return new Result<>(code,msg,null);
    }

    public static <T> Result<?> success(T data){
        return new Result<>(200,null,data);
    }

    public static <T> Result<?> adjust(T data){
        if(Objects.isNull(data)){
            return error(1001,"对象为空");
        }
        if(data instanceof List){
            if(((List<?>) data).size() == 0){
                return error(1002,"数据为空");
            }
        }
        return success(data);
    }
}