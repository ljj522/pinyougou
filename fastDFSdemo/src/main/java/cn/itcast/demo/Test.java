package cn.itcast.demo;


import org.csource.fastdfs.*;

public class Test {

    public static void main(String[] args) throws Exception{
        //加载配置文件
        ClientGlobal.init("D:\\idea_workspaces\\pinyougouparent\\fastDFSdemo\\src\\main\\resources\\fdfs_client.conf");
        //2.构建一个管理者客户端
        TrackerClient client = new TrackerClient();
        //3.连接管理者服务端
        TrackerServer trackerServer = client.getConnection();

        //4.构建存储服务端
        StorageServer storageServer=null;
        //5.获取存储服务器的客户端对象
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        //6.上传文件
        String[] strings = storageClient.upload_file("d:\\mvn\\22.jpg", "jpg", null);
        //7.显示上传结果file_id
        for (String str : strings){
            System.out.println("str = " + str);
        }
    }
}
