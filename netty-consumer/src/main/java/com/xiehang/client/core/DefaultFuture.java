package com.xiehang.client.core;


import com.xiehang.client.param.ClientRequest;
import com.xiehang.client.param.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<>();
    final Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();
    private Response response;
    private long timeout = 2 * 60 * 1000;
    private long startTime = System.currentTimeMillis();

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }

    //主线程获取数据，awit 等待获取结果
    public Response get(long time) {
        lock.lock();
        try {
            while (!done()) {
                condition.await(time, TimeUnit.SECONDS);
                if ((System.currentTimeMillis() - startTime) > time) {
                    System.out.println("请求超时");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    //主线程获取数据，awit 等待获取结果
    public Response get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    private boolean done() {
        if (this.response != null) {
            return true;
        }
        return false;
    }

    public static void recive(Response response) {
        DefaultFuture df = allDefaultFuture.get(response.getId());
        if (df != null) {
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                allDefaultFuture.remove(df);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    //定时任务，移除超时连接
    static class FutureThread extends Thread {
        @Override
        public void run() {
            Set<Long> ids = allDefaultFuture.keySet();//获取所有链路的id
            for (Long id : ids) {
                DefaultFuture df = allDefaultFuture.get(id);//根据id获取链路
                if (df == null) {
                    allDefaultFuture.remove(df);//空的移除
                } else {
                    if (df.getTimeout() < (System.currentTimeMillis() - df.startTime)) {
                        //假如这个链路超时了
                        Response resp = new Response();
                        resp.setId(id);
                        resp.setCode("333333");
                        resp.setMsg("链路请求超时");
                        recive(resp);
                    }
                }
            }
        }
    }

    //定时任务，移除超时连接
    static {
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }



    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
