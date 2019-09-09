package com.xiehang.client.core;

import com.xiehang.client.zk.ZookeeperFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

public class ServerWatcher implements CuratorWatcher {


    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
        String path = watchedEvent.getPath();//也就是 Constants.SERVER_PATH
        client.getChildren().usingWatcher(this).forPath(path);
        List<String> serverPaths = client.getChildren().forPath(path);
        ChannelManager.realServerPath.clear();
        for (String serverPath : serverPaths) {
            String[] str = serverPath.split("#");
            ChannelManager.realServerPath.add(str[0] + "#" + str[1]);//去重
        }//这里可能有重复的
        ChannelManager.clear();
        for (String realServer : ChannelManager.realServerPath) {
            String[] str = realServer.split("#");
            try {
                ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
                ChannelManager.add(channelFuture);
            } catch (Exception e) {
                System.out.println("ServerWatch 监听异常");
                e.printStackTrace();
            }
        }

    }
}
