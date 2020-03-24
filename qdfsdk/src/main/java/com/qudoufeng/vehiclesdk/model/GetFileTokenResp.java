package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2018/7/27/027.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class GetFileTokenResp implements Serializable {
    public class UpLoadInfo {
        public String expire;
        public List<String> resids;
        public String token;
    }
    public UpLoadInfo upload;
}
