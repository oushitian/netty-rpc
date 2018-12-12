package com.ost.nettyrpc.response;

/**
 * @Author xyl
 * @Create 2018-11-28 16:03
 * @Desc 封装调用方法返回的结果或异常；Id为对应Request的Id，一一对应
 **/
public class RpcResponse{

    private Long id;
    private Exception exception;
    private Object result;

    public Object getResult() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "id=" + id +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}
