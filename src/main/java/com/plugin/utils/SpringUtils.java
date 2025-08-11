package com.plugin.utils;

import com.plugin.exception.PluginRuntimeException;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.lang.reflect.Method;
import java.util.Map;

import static com.plugin.utils.CommonUtils.normalizePrefix;

public class SpringUtils {

    /**
     * 判断类是否是Spring的Controller
     *
     * @param clazz 类
     * @return true:是Spring的Controller
     */
    public static boolean isSpringController(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        //跳过接口
        if (clazz.isInterface()) {
            return false;
        }
        return clazz.getAnnotation(RestController.class) != null || clazz.getAnnotation(Controller.class) != null;
    }

    /**
     * 注册Controller到Spring的RequestMappingHandlerMapping
     *
     * @param controller                   控制器实例
     * @param mapping RequestMappingHandlerMapping实例
     * @param pathPrefix                   路径前缀
     * @throws Exception 异常
     */
    public static void registerController(
            Object controller,
            RequestMappingHandlerMapping mapping,
            String pathPrefix) throws Exception {

        Class<?> handlerClazz = controller.getClass();
        //获取spring原始类
        Class<?> userType = ClassUtils.getUserClass(handlerClazz);

        // TODO: 通过反射获取RequestMappingHandlerMapping的getMappingForMethod方法,有安全性风险未来可以优化
        Method getMappingMethod = RequestMappingHandlerMapping.class
                .getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
        getMappingMethod.setAccessible(true);


        // 扫描原始Controller方法的映射信息
        Map<Method, RequestMappingInfo> detected = MethodIntrospector.selectMethods(
                userType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> {
                    try {
                        return (RequestMappingInfo) getMappingMethod.invoke(mapping, method, userType);
                    } catch (Exception e) {
                        throw new PluginRuntimeException("处理方法" + userType.getName() + ":"+ method.getName() + "时出现异常:", e);
                    }
                }
        );

        //构造前缀映射信息
        RequestMappingInfo prefixInfo = null;
        if (StringUtils.hasText(pathPrefix)) {
            String normalized = normalizePrefix(pathPrefix);
            prefixInfo = RequestMappingInfo.paths(normalized)
                    .options(mapping.getBuilderConfiguration())
                    .build();
        }

        for (Map.Entry<Method, RequestMappingInfo> e : detected.entrySet()) {
            Method method = e.getKey();
            RequestMappingInfo info = e.getValue();
            // 如果没有RequestMappingInfo则,不是请求处理方法,是普通方法
            if (info == null) continue;

            // 统一前缀如果存在就追加到info上
            if (prefixInfo != null) {
                info = prefixInfo.combine(info);
            }

            // Spring 内部会做并发与冲突检查，这里直接注册 模仿detectHandlerMethods支持AOP
            Method invocableMethod = AopUtils.selectInvocableMethod(method, userType);
            mapping.registerMapping(info, controller, invocableMethod);
        }
    }


}
