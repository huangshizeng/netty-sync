package com.huang.nettysync.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * @Fields serverHandler : 服务处理
     */
    @Autowired
    private NettyServerHandler serverHandler;


    @Override
    protected void initChannel(Channel ch) throws Exception {

        // 通过socketChannel去获得对应的管道
        ChannelPipeline channelPipeline = ch.pipeline();

        /*
         * channelPipeline中会有很多handler类（也称之拦截器类）
         * 获得pipeline之后，可以直接.addLast添加handler
         */
        ByteBuf buf = Unpooled.copiedBuffer(NettyServer.DELIMITER.getBytes());
        channelPipeline.addLast("framer", new DelimiterBasedFrameDecoder(1024 * 1024 * 2, buf));
        //channelPipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
        //channelPipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));

        // 自定义解码器，粘包/拆包/断包

        channelPipeline.addLast(serverHandler);
    }

}
