package com.xiehang.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.xiehang.client.core.DefaultFuture;
import com.xiehang.client.param.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleClientHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("ping".equals(msg.toString())) {
            //接受到了服务器的ping，响应pong，告诉服务器我没死
            ctx.channel().writeAndFlush("pong\r\n");
            return;
        }

        //通道关闭之后 f.channel().closeFuture().sync()
//        ctx.channel().close();  //这里是短连接，后面改成长连接，注释掉
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        System.out.println("接受服务器返回数据：" + JSONObject.toJSONString(response));
        DefaultFuture.recive(response);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
