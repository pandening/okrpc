package io.hujian.npc.pubisher;

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hujian06 on 2017/10/4.
 *
 *
 * you should using the resource file
 * {@link io.hujian.npc.common.NpcConstant#NPC_SERVICE_PUBLISH_XML_RESOURCE_FILE_PATH}
 * to publish your service. or , this rpc framework will just offer base implementation.
 *
 * and, you should using another thread to run this class's method, do not using the main
 * thread to do it!
 *
 */
public class PublishBeanParser {
    private static final NpcLogger LOGGER = NpcLogger.getLogger(PublishBeanParser.class.getName());

    public static class PublishBeanParserHolder {
        public static final PublishBeanParser PUBLISH_BEAN_PARSER = new PublishBeanParser();
    }

    private static final String scanResourceFile = NpcConstant.NPC_SERVICE_PUBLISH_XML_RESOURCE_FILE_PATH; // do not change the file.

    /**
     * using this method to get total service bean.
     * @return the bean name -> bean list Map.
     */
    public Map<String, RpcServicePublishBean> getWholeServiceBeans() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(scanResourceFile);

        int size = applicationContext.getBeanDefinitionCount();

        LOGGER.info("scan file:" +scanResourceFile + " find publish service: " +
                applicationContext.getBeanDefinitionCount());

        if (size == 0) {
            return Collections.emptyMap();
        }

        Map<String, RpcServicePublishBean> beanMap = new HashMap<>();

        Object o;
        for(String bean : applicationContext.getBeanDefinitionNames()) {
            if ( (o = applicationContext.getBean(bean)) instanceof RpcServicePublishBean) {
                beanMap.put(bean, (RpcServicePublishBean) o);

                LOGGER.info("Find Publish Service:" + ((RpcServicePublishBean)o).toString());
            }
        }

        return beanMap;
    }



}
