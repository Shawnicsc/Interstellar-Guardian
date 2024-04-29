#项目计划书

## 一、选题

**分布式安全存储 系统设计与实现**

## 二、需求分析

1. **功能需求分析**：
   - **存储、分发、下载和共享文件**：系统需要能够存储用户上传的文件，并提供分发、下载和共享的功能。
   - **数据安全加密、隐私保护及访问控制**：系统需要实现数据加密功能，确保数据在存储和传输过程中的安全性。同时，需要实现访问控制机制，确保只有授权用户能够访问相关数据。
2. **性能需求分析**：
   - **并发访问量**：系统需要能够处理大规模的并发访问，例如 1000、10000 个虚拟用户同时访问系统。
   - **响应时间、吞吐量**：系统需要具有较短的响应时间，并能够支持高吞吐量，以满足用户的需求。
3. **安全需求分析**：
   - **数据加密算法和密钥管理**：系统需要采用强大的加密算法对数据进行加密，并确保密钥的安全管理。
   - **访问控制策略**：系统需要实现身份验证和授权机制，确保只有授权用户能够访问相应的数据。

4. **易用性需求分析**：
   - **用户界面设计和交互流程**：系统需要设计简洁明了的用户界面，并提供流畅的交互体验，以确保用户能够轻松使用系统。

## 三、技术选型

### 前端

`VUE`  version：Vue3

 `Node.js` version：18.19.1

### 后端

`SpringCloud` ：Alibaba Cloud

 `SpringBoot` version ：2.6.13

 `Mybatis-plus`  3.5.6

 `Maven` version ：3.6.1

`Redis`

`Mysql`

### IPFS

`IPFS Desktop` version：0.34.0

## 四、开发流程

###1.  IPFS本地环境搭建与测试

   ![1713788646538](E:\北邮课程\2024年网络空间安全+信息安全编程教学题目-课件-代码\2024-安全编程-课程设计题目及要求\assets\1713788646538.png)

   ![1713788697780](E:\北邮课程\2024年网络空间安全+信息安全编程教学题目-课件-代码\2024-安全编程-课程设计题目及要求\assets\1713788697780.png)

###2.  Java 运行程序 向IPFS本地节点中上传，下载文件

   ```java
   @Service
   // 编写上传 下载接口
   public interface IpfsService {
       /**
        * 指定path+文件名称,上传至ipfs
        *
        * @param filePath
        * @return
        * @throws IOException
        */
       String uploadToIpfs(String filePath) throws IOException;
   
       /**
        * 将byte格式的数据,上传至ipfs
        *
        * @param data
        * @return
        * @throws IOException
        */
       String uploadToIpfs(byte[] data) throws IOException;
   
       /**
        * 根据Hash值,从ipfs下载内容,返回byte数据格式
        *
        * @param hash
        * @return
        */
       byte[] downFromIpfs(String hash);
   
       /**
        * 根据Hash值,从ipfs下载内容,并写入指定文件destFilePath
        *
        * @param hash
        * @param destFilePath
        */
       void downFromIpfs(String hash, String destFilePath);
   }
   ```

   

   ```java
   /**
    * @author Shawn i
    * @version 1.0
    * @description: 实现上传 下载函数
    * @date 2024/4/22 20:34
    */
   @Service
   public class IpfsServiceImpl implements IpfsService {
   
       // ipfs 的服务器地址和端口，与yaml文件中的配置对应
       @Value("${ipfs.config.multi-addr}")
       private String multiAddr;
   
       private IPFS ipfs;
   
       @PostConstruct
       public void setMultiAddr() {
           ipfs = new IPFS(multiAddr);
       }
   
       @Override
       public String uploadToIpfs(String filePath) throws IOException {
           NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePath));
   
           MerkleNode addResult = ipfs.add(file).get(0);
           return addResult.hash.toString();
       }
   
       @Override
       public String uploadToIpfs(byte[] data) throws IOException {
           NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
           MerkleNode addResult = ipfs.add(file).get(0);
           return addResult.hash.toString();
       }
   
       @Override
       public byte[] downFromIpfs(String hash) {
           byte[] data = null;
           try {
               data = ipfs.cat(Multihash.fromBase58(hash));
           } catch (IOException e) {
               e.printStackTrace();
           }
           return data;
       }
   
       @Override
       public void downFromIpfs(String hash, String destFile) {
           byte[] data = null;
           try {
               data = ipfs.cat(Multihash.fromBase58(hash));
           } catch (IOException e) {
               e.printStackTrace();
           }
           if (data != null && data.length > 0) {
               File file = new File(destFile);
               if (file.exists()) {
                   file.delete();
               }
               FileOutputStream fos = null;
               try {
                   fos = new FileOutputStream(file);
                   fos.write(data);
                   fos.flush();
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   try {
                       fos.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
   
               }
           }
       }
   
   }
   
   ```

   ```java
   /**
    * @author Shawn i
    * @version 1.0
    * @description: 编写测试类
    * @date 2024/4/22 20:40
    */
   @RunWith(SpringRunner.class)
   @SpringBootTest
   public class IpfsServiceTest {
       @Autowired
       private IpfsService ipfsService;
   
       // 上传
       @Test
       public void uploadIpfs() throws IOException {
           byte[] data = "hello world".getBytes();
           String hash = ipfsService.uploadToIpfs(data);
           // Qmf412jQZiuVUtdgnB36FXFX7xg5V6KEbSJ4dpQuhkLyfD
           System.out.println(hash);
       }
   
       // 下载
       @Test
       public void downloadIpfs() {
           String hash = "Qmf412jQZiuVUtdgnB36FXFX7xg5V6KEbSJ4dpQuhkLyfD";
           byte[] data = ipfsService.downFromIpfs(hash);
           System.out.println(new String(data));
       }
   
   }
   
   ```

   测试结果：

   ![1713789952185](E:\北邮课程\2024年网络空间安全+信息安全编程教学题目-课件-代码\2024-安全编程-课程设计题目及要求\assets\1713789952185.png)

   ![1713789970621](E:\北邮课程\2024年网络空间安全+信息安全编程教学题目-课件-代码\2024-安全编程-课程设计题目及要求\assets\1713789970621.png)

   ![1713789988993](E:\北邮课程\2024年网络空间安全+信息安全编程教学题目-课件-代码\2024-安全编程-课程设计题目及要求\assets\1713789988993.png)

###@TODO

####3. 实现前端交互页面

####4. 实现后端具体接口

####5. SpringCloud 解耦合 和 Redis分布式存储

####6. 数据库实现(`mysql`)

####7. 功能和性能测试

## 五、小组分工

   王惟：后端开发和接口联调

   魏瑞敏：前端开发

   杨雅雯：需求工程分析，对项目需求进行分析以及技术选型

   范茜文：性能测试

   林锦蓥：前端开发和功能测试