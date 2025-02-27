package xyz.lp;

public enum ExecuteResultEnum {
    SUCCESS(0, null),
    EXIT(1, null);

    private final int code;
    private final String msg;

    ExecuteResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
