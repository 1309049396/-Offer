package com.xiehang.client.proxy;

import com.xiehang.client.annotation.RemoteInvoke;
import com.xiehang.client.core.TcpClient;
import com.xiehang.client.param.ClientRequest;
import com.xiehang.client.param.Response;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)) {
                //判断一下有没有这个注解
                field.setAccessible(true);//设置可进行修改

                final Map<Method, Class> methodClassMap = new HashMap<>();
                putMethodClass(methodClassMap, field);
                Enhancer enhancer = new Enhancer();//cglib动态代理
                enhancer.setInterfaces(new Class[]{field.getType()});//动态代理哪些接口
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                        //执行指定的方法时进行拦截
                        //采用netty客户端调用服务端
                        ClientRequest request = new ClientRequest();
                        //需要获取接口的名字和方法
                        request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                        request.setContent(args[0]);
                        Response response = TcpClient.send(request);
                        return response;
                    }
                });

                try {
                    field.set(bean, enhancer.create());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    /**
     * 将属性的所有方法和属性接口类型放入到一个map中
     * @param methodClassMap
     * @param field
     */
    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method method : methods) {
            methodClassMap.put(method, field.getType());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
