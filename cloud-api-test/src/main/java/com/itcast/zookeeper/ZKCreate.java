package com.itcast.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author tyfann
 * @date 2021/1/3 9:06 下午
 */
public class ZKCreate {

    String IP = "192.168.1.106:2181";

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
        // arg1:节点的路径
        // arg2:节点的数据
        // arg3:权限列表 world:anyone:cdrwa
        // arg4:节点类型（持久化节点）
        zooKeeper.create("/create/node1","node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create2()throws Exception{
        zooKeeper.create("/create/node2","node2".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create3()throws Exception{
        // world授权模式
        // 权限列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权对象
        Id id = new Id("world","anyone");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        acls.add(new ACL(ZooDefs.Perms.WRITE, id));
        zooKeeper.create("/create/node3","node3".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create4()throws Exception{

        // ip授权模式
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("ip","192.168.1.106");
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node4","node4".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create5()throws Exception{
        // auth授权模式
        zooKeeper.addAuthInfo("digest","itcast:123456".getBytes());
        // 权限设置
        zooKeeper.create("/create/node5","node5".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    }

    @Test
    public void create6()throws Exception{
        // auth授权模式
        zooKeeper.addAuthInfo("digest","itcast:123456".getBytes());
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("auth","itcast");
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zooKeeper.create("/create/node6","node6".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create7()throws Exception{
        // digest授权模式
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("digest","ittyfann:UaZBUx8/iGhCa1uyBZf5M+sF39c=");
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node7","node7".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create8()throws Exception{
        //持久化有序节点
        String result = zooKeeper.create("/create/node8","node8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(result);
    }


    @Test
    public void create9()throws Exception{
        //临时节点
        String result = zooKeeper.create("/create/node9","node9".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    @Test
    public void create10()throws Exception{
        //临时节点
        String result = zooKeeper.create("/create/node10","node10".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(result);
    }

}
