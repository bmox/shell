package xyz.lp;

public class Result {
    
    private ExecuteResultEnum executeResultEnum;

    public Result() {}

    public Result(ExecuteResultEnum executeResultEnum) {
        this.executeResultEnum = executeResultEnum;
    }

    public static Result success() {
        return new Result(ExecuteResultEnum.SUCCESS);
    }

    public ExecuteResultEnum getExecuteResultEnum() {
        return executeResultEnum;
    }

    public void setExecuteResultEnum(ExecuteResultEnum executeResultEnum) {
        this.executeResultEnum = executeResultEnum;
    }

}
