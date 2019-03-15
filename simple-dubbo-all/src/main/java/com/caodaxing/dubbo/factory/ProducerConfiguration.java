package com.caodaxing.dubbo.factory;

import com.caodaxing.common.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description dubbo生产者配置类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerConfiguration {

    private String registerHost;
    private int registerPort;
    private int producerPort;

    public boolean validateAddress(String address){
        String[] split = address.split(":");
        if(split.length == 2){
            boolean isHost = RegexUtil.IP_REGEX.matcher(split[0]).matches();
            boolean isPort = RegexUtil.PORT_REGEX.matcher(split[1]).matches();
            if(isHost && isPort){
                return true;
            }
        }
        return false;
    }

    public void checkPropertyValues()throws RuntimeException {
        if(StringUtils.isEmpty(registerHost) || registerPort == 0 || producerPort == 0){
            throw new RuntimeException("Missing necessary parameters!");
        }
        if(!RegexUtil.IP_REGEX.matcher(this.registerHost).matches()){
            throw new RuntimeException("format error on 'registerHost' property!");
        }
    }

}
