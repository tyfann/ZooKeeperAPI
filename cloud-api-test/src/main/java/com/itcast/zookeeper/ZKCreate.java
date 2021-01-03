package com.itcast.zookeeper;


import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author tyfann
 * @date 2021/1/3 9:06 下午
 */
public class ZKCreate {

    String IP = "192.168.1.106:2181"

    ZooKeeper zooKeeper;

    @Before
    public void before()throws Exception{
        // 计数器对象
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // arg1:服务器的ip和端口
        // arg2:客户端与服务端之间的会话超时时间（以毫秒为单位）
        // arg3:监视器对象
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState()== Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功");
                    countDownLatch.countDown();
                }
            }
        });
        // 主线程阻塞等待连接对象的创建成功
        countDownLatch.await();
    }

    @After
    public void after()throws Exception{
        zooKeeper.close();
    }

    @Test
    public void create1()throws Exception{
        zooKeeper.create("/create/node1/","node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

}
