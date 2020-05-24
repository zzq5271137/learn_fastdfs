import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class TestFastDFS {
    public static void main(String[] args) throws Exception {
        // 1. 加载配置文件
        ClientGlobal.init("H:\\IDEA_workspace\\Learn_FastDFS\\src\\main\\resources\\fdfs_client.conf");

        // 2. 创建管理端对象
        TrackerClient trackerClient = new TrackerClient();

        // 3. 通过管理端对象获取连接
        TrackerServer connection = trackerClient.getConnection();

        // 4. 创建存储端对象
        StorageClient1 storageClient = new StorageClient1(connection, null);

        // 5. 创建文件属性信息对象数组
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("fileName", "zhengming");
        meta_list[1] = new NameValuePair("ExtName", "JPG");
        meta_list[2] = new NameValuePair("author", "zzq");

        // 6. 上传文件
        String path = storageClient.upload_file1("H:\\IDEA_workspace\\Learn_FastDFS\\upload\\pic2.JPG",
                "JPG", meta_list);

        /*
         * 客户端上传文件后, 存储服务器将文件ID返回给客户端, 此文件ID用于以后访问该文件的索引信息;
         * 文件索引信息包括: 组名、虚拟磁盘路径、数据两级目录、文件名, 例如:
         * group1/M00/00/00/wKhGA17KAwCAFfmBAAEFlMEFP-0938.JPG
         * 其中:
         * 1. "group1"为组名, 即storage服务器的配置文件中配置的组;
         * 2. "M00"为虚拟磁盘路径, 即storage配置的虚拟路径, 与磁盘选项store_path*对应,
         *    如果配置了store_path0则是M00, 如果配置了store_path1则是M01, 以此类推;
         * 3. "00/00"为两级目录, storage服务器在每个虚拟磁盘路径下创建的两级目录, 用于存储数据文件;
         * 4. "wKhGA17KAwCAFfmBAAEFlMEFP-0938.JPG"为文件名, 是由存储服务器根据特定信息生成,
         *    文件名包含: 源存储服务器IP地址、文件创建时间戳、文件大小、随机数和文件拓展名等信息;
         *
         * storage服务器默认不支持文件的访问, 需要使用Nginx, 借助FastDFS-nginx-module, 通过Nginx访问文件;
         * 需要配置nginx.conf, 让Nginx监听域名中带有"group"的(在storage服务器中配置的组名, 根据情况更改),
         * 交给FastDFS模块处理:
         *     location ~/group([0-9])/ {
         *         ngx_fastdfs_module;
         *     }
         * 配置并启动好storage服务器的Nginx后, 可以在浏览器中访问刚才上传的图片:
         * http://192.168.70.3/group1/M00/00/00/wKhGA17KAwCAFfmBAAEFlMEFP-0938.JPG
         * 其中, "group1/M00/00/00/wKhGA17KAwCAFfmBAAEFlMEFP-0938.JPG"为你要访问的文件的路径,
         * 即你在上传文件时, storage服务器返回给你的路径;
         */
        System.out.println("path: " + path);
    }
}
