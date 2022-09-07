package com.github.zzzj1233.protocol;

import cn.hutool.core.util.StrUtil;
import com.github.zzzj1233.utils.BytesReader;

/**
 * @author zzzj
 * @create 2022-09-06 18:05
 */
public class ErrorPacket {

    private final int errCode;

    private final String sqlMark;

    private final String errMsg;

    public ErrorPacket(byte[] response) {
        BytesReader reader = new BytesReader(response);

        reader.skip(1);

        this.errCode = reader.readInt(2);

        reader.skip(1);

        this.sqlMark = reader.readString(5);

        this.errMsg = reader.readUntilEOF();
    }

    @Override
    public String toString() {
        StringBuilder builder = StrUtil.builder();
        builder.append("Error Packet { code = ");
        builder.append(errCode);
        builder.append(" , msg = ");
        builder.append(errMsg);
        builder.append(" }");
        return builder.toString();
    }


    public int getErrCode() {
        return errCode;
    }

    public String getSqlMark() {
        return sqlMark;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
