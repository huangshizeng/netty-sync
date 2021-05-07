package com.huang.nettysync.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

//@Component
@ChannelHandler.Sharable //标注一个channel handler可以被多个channel安全地共享
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    public static AtomicInteger nConnection = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        String txt = msg.toString(CharsetUtil.UTF_8);

        LOGGER.info("收到客户端的消息：{}", txt);


        ackMessage(ctx, txt);
    }

    /**
     * @param ctx
     * @param message
     * @Description: 确认消息
     * @Author:杨攀
     * @Since: 2019年9月17日上午11:22:27
     */
    public void ackMessage(ChannelHandlerContext ctx, String message) {

        //自定义分隔符
        String msg = message + NettyServer.DELIMITER;

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);

        //回应客户端
        ctx.writeAndFlush(byteBuf);
    }


    /**
     * @param ctx
     * @throws Exception
     * @Description: 每次来一个新连接就对连接数加一
     * @Author:杨攀
     * @Since: 2019年9月16日下午3:04:42
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nConnection.incrementAndGet();

        LOGGER.info("请求连接...{}，当前连接数: ：{}", ctx.channel().id(), nConnection.get());
    }

    /**
     * @param ctx
     * @throws Exception
     * @Description: 每次与服务器断开的时候，连接数减一
     * @Author:杨攀
     * @Since: 2019年9月16日下午3:06:10
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nConnection.decrementAndGet();
        LOGGER.info("断开连接...当前连接数: ：{}", nConnection.get());
    }


    /**
     * @param ctx
     * @param cause
     * @throws Exception
     * @Description: 连接异常的时候回调
     * @Author:杨攀
     * @Since: 2019年9月16日下午3:06:55
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        // 打印错误日志
        cause.printStackTrace();

        Channel channel = ctx.channel();

        if (channel.isActive()) {
            ctx.close();
        }

    }

}
