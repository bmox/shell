package xyz.lp;

public class ExecuteResult {
    
    private ExecuteResultEnum executeResultEnum;

    public ExecuteResult() {}

    public ExecuteResult(ExecuteResultEnum executeResultEnum) {
        this.executeResultEnum = executeResultEnum;
    }

    public static ExecuteResult success() {
        return new ExecuteResult(ExecuteResultEnum.SUCCESS);
    }

    public ExecuteResultEnum getExecuteResultEnum() {
        return executeResultEnum;
    }

    public void setExecuteResultEnum(ExecuteResultEnum executeResultEnum) {
        this.executeResultEnum = executeResultEnum;
    }

}
