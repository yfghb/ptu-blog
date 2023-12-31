/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 25/05/2023 13:10:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint(200) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `summary` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属分类id',
  `thumbnail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `is_top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否置顶（0否，1是）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态（0已发布，1草稿）',
  `view_count` bigint(200) NULL DEFAULT 0 COMMENT '访问量',
  `is_comment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否允许评论 1是，0否',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 'Spring Boot文件上传功能的实现', '## 1. Spring MVC处理文件上传\r\n\r\n* 一旦有请求被DispatcherServlet类处理，DispatcherServlet类会先调用MultipartResolver实现类中的isMultipart()方法判断该请求是不是文件上传请求。\r\n\r\n* 如果是文件上传请求，DispatcherServlet类会调用MultipartResolver实现类中的resolveMultipart()方法重新封装该请求对象，并返回一个新的MultipartHttpServletRequest对象供后续处理流程使用。\r\n\r\n\r\n\r\n> **文件上传请求的结构**\r\n\r\n1. 请求头：与常规HTTP请求一样，包括请求方法、URL、HTTP版本等信息。\r\n2. 分隔符：在请求头的Content-Type字段中还会指定一个分隔符（boundary），用于分隔请求体中的不同部分。分隔符必须在每个部分的开头和结尾处都出现，并且不能与任何部分的内容重复。\r\n3. 每个部分：每个部分都由一个部分头和部分体组成，部分头包含了该部分的信息，如Content-Disposition、Content-Type等，部分体则是该部分的内容。\r\n\r\n```http\r\nPOST /uploadFile HTTP/1.1\r\nHost: localhost:8080\r\nContent-Type: multipart/form-data; boundary=----WebKitFormBoundaryjKJjwy7WfTShlnxw\r\n\r\n------WebKitFormBoundaryjKJjwy7WfTShlnxw\r\nContent-Disposition: form-data; name=\"user\"\r\n\r\n小明\r\n------WebKitFormBoundaryjKJjwy7WfTShlnxw\r\nContent-Disposition: form-data; name=\"file1\"; filename=\"example1.txt\"\r\nContent-Type: text/plain\r\n\r\nThis is the contents of the file1.\r\n\r\n------WebKitFormBoundaryjKJjwy7WfTShlnxw\r\nContent-Disposition: form-data; name=\"file2\"; filename=\"example2.png\"\r\nContent-Type: image/png\r\n\r\nThis is the contents of the file2.\r\n\r\n------WebKitFormBoundaryjKJjwy7WfTShlnxw--\r\n```\r\n\r\n\r\n\r\n## 2. Spring Boot文件上传功能的实现\r\n\r\n> **Spring Boot文件上传配置项**\r\n\r\n由于Spring Boot自动配置机制的存在，只要在pom.xml文件中引入spring-boot-starter-web依赖即可直接调用文件上传功能。开发人员在文件上传时也可能有一些特殊的需求，有如下配置项可以进行修改：\r\n\r\n**spring.servlet.multipart.enabled**：是否支持multipart上传文件，默认true。\r\n**spring.servlet.multipart.file-size-threshold**：文件大小阈值，当大于这个阈值时将写入磁盘，否则存在内存中，（默认值0，一般情况下不用特意修改）。\r\n**spring.servlet.multipart.location**：上传文件的临时目录。\r\n<span style=\"border: 1px solid; border-color: red;\">**spring.servlet.multipart.max-file-size**</span>：最大支持的文件大小，默认1MB，该值可适当调整。\r\n<span style=\"border: 1px solid; border-color: red;\">**spring.servlet.multipart.max-request-size**</span>：最大支持的请求大小，默认10MB。\r\n**spring.servlet.multipart.resolve-lazily**：判断是否要延迟解析文件，默认false（相当于懒加载，一般情况下不用特意修改）。\r\n\r\n\r\n\r\n> **实现案例**\r\n\r\n1. 在static目录下新建 `upload-test.html`\r\n\r\n   ```html\r\n   <!DOCTYPE html>\r\n   <html lang=\"en\">\r\n   <head>\r\n       <meta charset=\"UTF-8\">\r\n       <title>Spring Boot 文件上传测试</title>\r\n   </head>\r\n   <body>\r\n   <form action=\"/uploadFile\" method=\"post\" enctype=\"multipart/form-data\">\r\n       <input type=\"file\" name=\"file\" />\r\n       <input type=\"submit\" value=\"文件上传\" />\r\n   </form>\r\n   </body>\r\n   </html>\r\n   ```\r\n\r\n\r\n2. 在controller包下新建 `UploadController ` 类\r\n\r\n   * 由于Spring Boot已经自动配置了StandardServletMultipartResolver类来处理文件上传请求，因此能够直接在controller方法中使用MultipartFile读取文件信息。 \r\n\r\n   ```java\r\n   @Controller\r\n   public class UploadController {\r\n       // 上传文件的保存路径\r\n       private final static String FILE_UPLOAD_PATH = \"E:\\\\test\\\\upload\\\\\";\r\n   \r\n       @RequestMapping(value = \"/uploadFile\", method = RequestMethod.POST)\r\n       @ResponseBody\r\n       public String uploadFile(MultipartFile file) {\r\n           if (file.isEmpty()) {\r\n               return \"上传失败\";\r\n           }\r\n   \r\n           // 生成文件名，保存文件\r\n           String filename = file.getOriginalFilename();\r\n           DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"yyyyMMdd_HHmmssSSS_\");\r\n           String newFilename = LocalDateTime.now().format(formatter) + filename;\r\n           try {\r\n               file.transferTo(new File(FILE_UPLOAD_PATH + newFilename));\r\n           } catch (IOException e) {\r\n               e.printStackTrace();\r\n           }\r\n   \r\n           return \"上传成功，地址为：\" + FILE_UPLOAD_PATH + newFilename;\r\n       }\r\n   }   \r\n   ```\r\n\r\n3. 启动Spring Boot项目，打开浏览器并输入测试页面地址：[localhost:8080/upload-test.html](http://localhost:8080/upload-test.html) \r\n\r\n   * 如果文件存储目录还没有创建的话，首先需要创建该目录\r\n   * 单个文件大小超出设定值或请求的大小超出设定值，需要调整文件上传的配置项来避免这种异常信息的产生\r\n\r\n   \r\n\r\n> **Spring Boot文件上传路径回显**\r\n\r\n新建config包并在config包中新建 `MyMvcConfig` 类\r\n\r\n增加一个自定义静态资源映射配置，使得静态资源可以通过该映射地址被访问到：\r\n\r\n* `addResourceHandler()`：用于指定URL路径，即在浏览器中访问的路径\r\n* `addResourceLocations()`：用于指定静态资源路径\r\n\r\n```java\r\n@Configuration\r\npublic class MyMvcConfig implements WebMvcConfigurer {\r\n\r\n    public void addResourceHandlers(ResourceHandlerRegistry registry) {\r\n        registry.addResourceHandler(\"/upload/**\").addResourceLocations(\"file:E:\\\\test\\\\upload\\\\\");\r\n    }\r\n}\r\n```\r\n\r\n\r\n\r\n## 3. Spring Boot多文件上传功能的实现\r\n\r\n> **文件名相同时的多文件上传处理**\r\n\r\n1. 在static目录中新建 `upload-same-file-name.html`\r\n\r\n   ```html\r\n   <!DOCTYPE html>\r\n   <html lang=\"en\">\r\n   <head>\r\n       <meta charset=\"UTF-8\">\r\n       <title>Spring Boot 多文件上传测试（filename相同）</title>\r\n   </head>\r\n   <body>\r\n   <form action=\"/uploadFilesBySameName\" method=\"post\" enctype=\"multipart/form-data\">\r\n       <input type=\"file\" name=\"files\"/><br><br>\r\n       <input type=\"file\" name=\"files\"/><br><br>\r\n       <input type=\"file\" name=\"files\"/><br><br>\r\n       <input type=\"submit\" value=\"文件上传\"/>\r\n   </form>\r\n   </body>\r\n   </html>\r\n   ```\r\n\r\n2. 在 `UploadController` 类中新增 `uploadFilesBySameName()` 方法\r\n\r\n   ```java\r\n   @RequestMapping(value = \"/uploadFilesBySameName\", method = RequestMethod.POST)\r\n   @ResponseBody\r\n   public String uploadFilesBySameName(MultipartFile[] files) {\r\n       if (files == null || files.length == 0) {\r\n           return \"参数异常\";\r\n       }\r\n   \r\n       String uploadResult = \"上传成功，地址为：<br>\";\r\n       for (MultipartFile file : files) {\r\n           if (file.isEmpty()) {\r\n               continue;\r\n           }\r\n           String filename = file.getOriginalFilename();\r\n           DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"yyyyMMdd_HHmmssSSS_\");\r\n           String newFilename = LocalDateTime.now().format(formatter) + filename;\r\n           try {\r\n               file.transferTo(new File(FILE_UPLOAD_PATH + newFilename));\r\n           } catch (IOException e) {\r\n               e.printStackTrace();\r\n           }\r\n           uploadResult += FILE_UPLOAD_PATH + newFilename + \"<br>\";\r\n       }\r\n       return uploadResult;\r\n   }\r\n   ```\r\n\r\n\r\n\r\n> **文件名不同时的多文件上传处理**\r\n\r\n1. 在static目录中新建 `upload-different-file-name.html`\r\n\r\n   ```html\r\n   <!DOCTYPE html>\r\n   <html lang=\"en\">\r\n   <head>\r\n       <meta charset=\"UTF-8\">\r\n       <title>Spring Boot 多文件上传测试（filename不同）</title>\r\n   </head>\r\n   <body>\r\n   <form action=\"/uploadFilesByDifferentName\" method=\"post\" enctype=\"multipart/form-data\">\r\n       <input type=\"file\" name=\"file1\"/><br><br>\r\n       <input type=\"file\" name=\"file2\"/><br><br>\r\n       <input type=\"file\" name=\"file3\"/><br><br>\r\n       <input type=\"submit\" value=\"文件上传\"/>\r\n   </form>\r\n   </body>\r\n   </html>\r\n   ```\r\n\r\n2. 在 `UploadController` 类中新增 `uploadFilesByDifferentName()` 方法\r\n\r\n   * **HttpServletRequest** 和 **HttpServletResponse**：这是 HTTP 请求和响应的 Java 表示。可以作为参数直接传入方法，使用它们来获取更多关于请求和响应的详细信息，或者执行特定的操作。\r\n\r\n   ```java\r\n   @Autowired\r\n   private StandardServletMultipartResolver multipartResolver;\r\n   \r\n   @RequestMapping(value = \"/uploadFilesByDifferentName\", method = RequestMethod.POST)\r\n   @ResponseBody\r\n   public String uploadFilesByDifferentName(HttpServletRequest request) {\r\n       // 如果不是文件上传请求则不处理\r\n       if (!multipartResolver.isMultipart(request)) {\r\n           return \"请选择文件\";\r\n       }\r\n   \r\n       // 将 HttpServletRequest 对象转换为 MultipartHttpServletRequest 对象，之后读取文件\r\n       MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;\r\n       Iterator<String> iter = multipartRequest.getFileNames();\r\n       List<MultipartFile> files = new ArrayList<>();\r\n       while (iter.hasNext()) {\r\n           MultipartFile file = multipartRequest.getFile(iter.next());\r\n           files.add(file);\r\n       }\r\n   \r\n       if (CollectionUtils.isEmpty(files)) {\r\n           return \"请选择文件\";\r\n       }\r\n   \r\n       String uploadResult = \"上传成功，地址为：<br>\";\r\n       for (MultipartFile file : files) {\r\n           if (file.isEmpty()) {\r\n               continue;\r\n           }\r\n           String filename = file.getOriginalFilename();\r\n           DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"yyyyMMdd_HHmmssSSS_\");\r\n           String newFilename = LocalDateTime.now().format(formatter) + filename;\r\n           try {\r\n               file.transferTo(new File(FILE_UPLOAD_PATH + newFilename));\r\n           } catch (IOException e) {\r\n               e.printStackTrace();\r\n           }\r\n           uploadResult += FILE_UPLOAD_PATH + newFilename + \"<br>\";\r\n       }\r\n       return uploadResult;\r\n   }\r\n   ```\r\n\r\n\r\n\r\n', '使用Spring Boot实现文件上传及其相关的注意事项', 1, 'https://img1.baidu.com/it/u=4026470308,2412268569&fm=253&fmt=auto&app=138&f=JPEG?w=824&h=500', '1', '0', 106, '0', NULL, '2023-02-23 23:20:11', NULL, NULL, 0);
INSERT INTO `article` VALUES (2, 'Python文件读写', '本文读写的文件，都是指 `文本文件` ， 至于 视频、图片等类型的文件，它们有各自的不同的文件数据格式，不在这里讲述。\r\n\r\n我们开发程序，经常需要从文本文件中读入信息，比如从日志文件中读取日志，从而分析数据信息；\r\n\r\n也经常需要写入文本信息到文件中，比如写入操作信息到日志文件中。\r\n\r\n在python语言中，我们要读写文本文件， 首先通过内置函数open 打开一个文件。\r\n\r\nopen函数会返回一个对象，我们可以称之为文件对象。\r\n\r\n这个返回的文件对象就包含读取文本内容和写入文本内容的方法。\r\n\r\n前面的课程我们刚刚学过，要写入字符串到文件中，需要先将**字符串编码为字节串**。\r\n\r\n而从文本文件中读取的文本信息都是字节串，要进行处理之前，必须先将**字节串解码为字符串**。\r\n\r\n文件的打开，分为 文本模式 和 二进制模式。\r\n\r\n## 文本模式\r\n\r\n通常，对文本文件，都是以文本模式打开。\r\n\r\n文本模式打开文件后，我们的程序读取到的内容都是字符串对象，写入文件时传入的也是字符串对象。\r\n\r\n### open函数的参数\r\n\r\n要读写文件，首先要通过内置函数open 打开文件，获得文件对象。\r\n\r\n函数open的参数如下\r\n\r\n```py\r\nopen(\r\n    file, \r\n    mode=\'r\', \r\n    buffering=-1, \r\n    encoding=None, \r\n    errors=None, \r\n    newline=None, \r\n    closefd=True, \r\n    opener=None\r\n    ) \r\n```\r\n\r\n其中下面这3个参数是我们常用的。\r\n\r\n- 参数 file\r\n\r\n  file参数指定了要打开文件的路径。\r\n\r\n  可以是相对路径，比如 ‘log.txt’， 就是指当前工作目录下面的log.txt 文件 也可以是绝对路径，比如 ’d:\\project\\log\\log.txt\'，\r\n\r\n\r\n\r\n- 参数 mode\r\n\r\n  mode参数指定了文件打开的 `模式` ，打开文件的模式 决定了可以怎样操作文件。\r\n\r\n  常用的打开模式有\r\n\r\n  - r 只读文本模式打开，这是最常用的一种模式\r\n  - w 只写文本模式打开\r\n  - a 追加文本模式打开\r\n\r\n  如果我们要 读取文本文件内容到字符串对象中 ， 就应该使用 r 模式。\r\n\r\n  我们可以发现mode参数的缺省值 就是 ‘r’ 。\r\n\r\n  就是说，调用open函数时，如果没有指定参数mode的值，那么该参数就使用缺省值 ‘r’，表示只读打开。\r\n\r\n  如果我们要 **创建一个新文件写入内容，或者清空某个文本文件重新写入内容**， 就应该使用 ‘w’ 模式。\r\n\r\n  如果我们要 **从某个文件末尾添加内容**， 就应该使用 ‘a’ 模式。\r\n\r\n\r\n\r\n- 参数 encoding\r\n\r\n  encoding 参数指定了读写文本文件时，使用的 **字符编解码** 方式。\r\n\r\n  调用open函数时，如果传入了encoding参数值：\r\n\r\n  ```\r\n    后面调用write写入字符串到文件中，open函数会使用指定encoding编码为字节串；\r\n      \r\n    后面调用read从文件中读取内容，open函数会使用指定encoding解码为字符串对象\r\n  ```\r\n\r\n  如果调用的时候没有传入encoding参数值，open函数会使用系统缺省字符编码方式。 比如在中文的Windows系统上，就是使用cp936（就是gbk编码）。\r\n\r\n  建议大家编写代码 读写文本文件时，都指定该参数的值。\r\n\r\n### 写文件示例\r\n\r\n下面的示例代码写入文本内容到文件中， 大家可以拷贝执行一下。\r\n\r\n```py\r\n# 指定编码方式为 utf8\r\nf = open(\'tmp.txt\',\'w\',encoding=\'utf8\')\r\n\r\n# write方法会将字符串编码为utf8字节串写入文件\r\nf.write(\'白月黑羽：祝大家好运气\')\r\n\r\n# 文件操作完毕后， 使用close 方法关闭该文件对象\r\nf.close()\r\n```\r\n\r\n上面使用的是utf8编码写入文件的。\r\n\r\n运行一下，我们用notepad++文本编辑器（notepad++可以百度搜索下载）打开该文件。\r\n\r\n可以发现该文件确实是utf8编码。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36462795_36383834-5d7bc92c-15c8-11e8-8821-da79d117d45c.png)\r\n\r\n如果我们换成用gbk编码字符串写入到文件中\r\n\r\n```py\r\n# 指定编码方式为 gb2312\r\nf = open(\'tmp.txt\',\'w\',encoding=\'gb2312\')\r\n\r\n# write方法会将字符串编码为gb2312字节串存入文件中\r\nf.write(\'白月黑羽：祝大家好运气\')\r\n\r\n# 文件操作完毕后， 使用close 方法关闭该文件对象\r\nf.close()\r\n```\r\n\r\n运行一下，用notepad++文本编辑器打开该文件。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36462795_36384230-8b355fda-15c9-11e8-9365-53ea280b1510.png)\r\n\r\n可以发现该文件确实是gb2312编码。\r\n\r\n------\r\n\r\n\r\nmode参数为\'w\'，表示要覆盖写文件。 这就意味着，如果原来文件中有内容， 该模式打开文件后，文件中所有的内容都会被 **！！！删除掉！！！**\r\n\r\n所以要特别的小心。\r\n\r\n有的场合下，我们需要在文件末尾添加新的内容，而不是删除掉原来的内容重新写。比如写日志文件，在重新启动系统的时候，需要接着在原来的日志文件后面添加新的内容。\r\n\r\n这时，我们可以用追加模式 `a` 打开文件。\r\n\r\n例如\r\n\r\n```py\r\n# a 表示 追加模式 打开文件\r\nf = open(\'tmp.txt\',\'a\',encoding=\'gb2312\')\r\nf.write(\'白月黑羽再次祝大家 ：good luck\')\r\nf.close()\r\n```\r\n\r\n\r\n\r\n### 读文件示例\r\n\r\n下面这段示例代码，实现的功能是：从前面的代码生成的文本文件中，读出内容到字符串对象中，并且截取出其中的名字部分， 大家可以拷贝执行一下。\r\n\r\n```py\r\n# 指定编码方式为 gbk，gbk编码兼容gb2312\r\nf = open(\'tmp.txt\',\'r\',encoding=\'gbk\')\r\n\r\n# read 方法会在读取文件中的原始字节串后， 根据上面指定的gbk解码为字符串对象返回\r\ncontent = f.read()\r\n\r\n# 文件操作完毕后， 使用close 方法关闭该文件对象\r\nf.close()\r\n\r\n# 通过字符串的split方法获取其中用户名部分\r\nname = content.split(\'：\')[0]\r\n\r\nprint(name)\r\n```\r\n\r\n### 注意点\r\n\r\nread函数有参数size，读取文本文件的时候，用来指定这次读取多少个字符。 如果不传入该参数，就是读取文件中所有的内容。\r\n\r\n大家可以创建一个文本文件，内容如下\r\n\r\n```\r\nhello\r\ncHl0aG9uMy52aXAgYWxsIHJpZ2h0cyByZXNlcnZlZA==\r\n```\r\n\r\n我们可以这样读取该文本文件\r\n\r\n```py\r\n# 因为是读取文本文件的模式， 可以无须指定 mode参数\r\n# 因为都是 英文字符，基本上所以的编码方式都兼容ASCII，可以无须指定encoding参数\r\nf = open(\'tmp.txt\')\r\n\r\ntmp = f.read(3)  # read 方法读取3个字符\r\nprint(tmp)       # 返回3个字符的字符串 \'hel\' \r\n\r\n\r\ntmp = f.read(3)  # 继续使用 read 方法读取3个字符\r\nprint(tmp)       # 返回3个字符的字符串 \'lo\\n\'  换行符也是一个字符\r\n\r\n\r\ntmp = f.read()  # 不加参数，读取剩余的所有字符\r\nprint(tmp)       # 返回剩余字符的字符串 \'cHl0aG9uMy52aXAgYWxsIHJpZ2h0cyByZXNlcnZlZA==\' \r\n\r\n\r\n# 文件操作完毕后， 使用close 方法关闭该文件对象\r\nf.close()  \r\n```\r\n\r\n读取文本文件内容的时候，通常还会使用readlines方法，该方法会返回一个列表。 列表中的每个元素依次对应文本文件中每行内容。\r\n\r\n```py\r\nf = open(\'tmp.txt\')\r\nlinelist = f.readlines() \r\nf.close()  \r\nfor line in linelist:\r\n    print(line)\r\n```\r\n\r\n但是这种方法,列表的每个元素对应的字符串 最后有一个换行符。 如果你不想要换行符，可以使用字符串对象的splitlines方法\r\n\r\n```py\r\nf = open(\'tmp.txt\')\r\ncontent = f.read()   # 读取全部文件内容\r\nf.close()  \r\n\r\n# 将文件内容字符串 按换行符 切割 到列表中，每个元素依次对应一行\r\nlinelist = content.splitlines()\r\nfor line in linelist:\r\n    print(line)\r\n```\r\n\r\n\r\n\r\n## 二进制（字节）模式\r\n\r\n不知道大家有没有注意，前面我们讲的打开文件，读写文件。都强调这是 **文本文件**。\r\n\r\n前面我们打开文件都是 `文本模式` 打开，可能大家还听说过 `二进制模式` 打开文件。\r\n\r\n其实就文件存储的底层来说，不管什么类型的文件（文本、视频、图片、word、excel等），存储的都是字节，不存在文本和二进制的区别，可以说都是二进制。\r\n\r\n所以 二进制模式 这个名词容易引起大家的误解， 如果让我来翻译，我觉得叫做 `字节模式` 更好。\r\n\r\n读写文件底层操作读写的 **都是字节**。\r\n\r\n以文本模式打开文件后， 后面的读写文件的方法（比如 read，write等），底层实现都会自动的进行 字符串（对应Python的string对象）和字节串（对应Python的bytes对象） 的转换。\r\n\r\n我们可以指定open函数的mode参数，直接读取原始的 **二进制 字节串** 到一个bytes对象中。\r\n\r\n大家可以写入字符串 `白月黑羽` 到一个文件中，保存时使用utf8编码\r\n\r\n然后我们这样运行下面的代码\r\n\r\n```py\r\n# mode参数指定为rb 就是用二进制读的方式打开文件\r\nf = open(\'tmp.txt\',\'rb\')\r\ncontent = f.read()   \r\nf.close()  \r\n\r\n# 由于是 二进制方式打开，所以得到的content是 字节串对象 bytes\r\n# 内容为 b\'\\xe7\\x99\\xbd\\xe6\\x9c\\x88\\xe9\\xbb\\x91\\xe7\\xbe\\xbd\'\r\nprint(content) \r\n\r\n# 该对象的长度是字节串里面的字节个数，就是12，每3个字节对应一个汉字的utf8编码\r\nprint(len(content))\r\n```\r\n\r\n\r\n\r\n以二进制方式写数据到文件中，传给write方法的参数不能是字符串，只能是bytes对象\r\n\r\n比如\r\n\r\n```py\r\n# mode参数指定为 wb 就是用二进制写的方式打开文件\r\nf = open(\'tmp.txt\',\'wb\')\r\n\r\ncontent = \'白月黑羽祝大家好运连连\'\r\n# 二进制打开的文件， 写入的参数必须是bytes类型，\r\n# 字符串对象需要调用encode进行相应的编码为bytes类型\r\nf.write(content.encode(\'utf8\'))\r\n\r\nf.close()  \r\n```\r\n\r\n\r\n\r\n如果你想在代码中 `直接用数字` 表示字节串的内容，并写入文件，可以这样\r\n\r\n```py\r\ncontent = b\'\\xe7\\x99\\xbd\\xe6\\x9c\\x88\\xe9\\xbb\\x91\\xe7\\xbe\\xbd\'\r\nf.write(content)\r\n```\r\n\r\n\r\n\r\n既然任何文件都可以以字节方式进行读取 和写入， 那么我来考考你们。\r\n\r\n如何字节实现一个简单的文件拷贝功能呢？\r\n\r\n比如实现一个函数，有两个参数，第一个参数是源文件路径，第二个参数是拷贝生成的目标文件路径。\r\n\r\n可以像下面的代码这样\r\n\r\n```py\r\ndef fileCopy(srcPath,destPath):\r\n    srcF = open(srcPath,\'rb\')\r\n    content = srcF.read()\r\n    srcF.close()\r\n\r\n    destF = open(destPath,\'wb\')\r\n    destF.write(content)\r\n    destF.close()\r\n\r\nfileCopy(\'1.png\',\'1copy.png\')\r\n```\r\n\r\n## with 语句\r\n\r\n如果我们开发的程序 在进行文件读写之后，忘记使用close方法关闭文件， 就可能造成意想不到的问题。\r\n\r\n我们可以使用with 语句 打开文件，像这样，就不需要我们调用close方法关闭文件。 Python解释器会帮我们调用文件对象的close方法。\r\n\r\n如下\r\n\r\n```py\r\n# open返回的对象 赋值为 变量 f\r\nwith open(\'tmp.txt\') as f:\r\n    linelist = f.readlines() \r\n    for line in linelist:\r\n        print(line)\r\n```\r\n\r\n对文件的操作都放在with下面的缩进的代码块中。\r\n\r\n\r\n\r\n## 写入缓冲\r\n\r\n我们来看下面的代码\r\n\r\n```py\r\nf = open(\'tmp.txt\',\'w\',encoding=\'utf8\')\r\n\r\nf.write(\'白月黑羽：祝大家好运气\')\r\n\r\n# 等待 30秒，再close文件\r\nimport time\r\ntime.sleep(30)\r\n\r\nf.close()\r\n```\r\n\r\n执行该程序时，执行完写入文件内容后，会等待30秒，再关闭文件对象。\r\n\r\n在这30秒还没有结束的时候，如果你打开 tmp.txt， 将会惊奇的发现，该文件中啥内容也没有！！！\r\n\r\n为什么？\r\n\r\n不是刚刚执行过下面的代码吗？\r\n\r\n```py\r\nf.write(\'白月黑羽：祝大家好运气\')\r\n```\r\n\r\n\r\n\r\n原来，我们执行write方法写入字节到文件中的时候，其实只是把这个请求提交给 操作系统。\r\n\r\n操作系统为了提高效率，通常并不会立即把内容写到存储文件中， 而是写入内存的一个 `缓冲区` 。\r\n\r\n等缓冲区的内容堆满之后，或者程序调用close 关闭文件对象的时候，再写入到文件中。\r\n\r\n如果你确实希望，在调用write之后，立即把内容写到文件里面，可以使用 文件对象的 flush方法。\r\n\r\n如下所示\r\n\r\n```py\r\nf = open(\'tmp.txt\',\'w\',encoding=\'utf8\')\r\n\r\nf.write(\'白月黑羽：祝大家好运气\')\r\n# 立即把内容写到文件里面\r\nf.flush()\r\n\r\n# 等待 30秒，再close文件\r\nimport time\r\ntime.sleep(30)\r\n\r\nf.close()\r\n```\r\n\r\n这样再执行程序，在等待的30秒期间，你打开文件，发现里面已经有写入的字符串 “白月黑羽：祝大家好运气” 了。', 'Python读写文本文件学习笔记', 2, 'https://cdn2.byhy.net/imgs/gh/36462795_36383834-5d7bc92c-15c8-11e8-8821-da79d117d45c.png', '1', '0', 125, '0', NULL, '2023-03-21 14:58:30', NULL, NULL, 0);
INSERT INTO `article` VALUES (3, 'Pycharm的安装和使用', '开发程序项目，需要选择一款优秀的集成开发环境软件，英文缩写就是IDE。\r\n\r\nIDE 可以帮你更高效的开发项目代码。因为它提供了非常实用的功能，比如项目文件管理、语法高亮、代码导航、自动补齐代码、语法静态检查、调试、版本控制等功能。\r\n\r\nPython开发项目，目前比较推荐的IDE 有两款，一个是 Pycharm，另一个是vscode (需要安装Python语言插件) 。\r\n\r\n其中 Pycharm 是老牌 IDE 开发商 Jetbrain的产品，更加适合初学者使用。\r\n\r\n我们这里就给大家介绍一下它的使用技巧。\r\n\r\n## 安装\r\n\r\n请到下面官网地址下载安装Pycharm\r\n\r\nhttps://www.jetbrains.com/pycharm/download/\r\n\r\n点击下图箭头处，下载社区版，这是一个免费的版本。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36462795_36413121-fa945c26-1657-11e8-8c9c-33c5ea6fad75.png)\r\n\r\n\r\n\r\n下载后，直接双击安装程序安装即可。\r\n\r\n其中这一步，Create Desktop Shortcut 选项 ，是选择桌面快捷方式 是启动 32位版本 还是 64位版本的。\r\n\r\n大家可以根据自己电脑的操作系统版本，勾选即可，\r\n\r\nCreate Associations 部分，是控制 扩展名为py的文件 是否用 Pycharm 打开的。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36415352-8bf618be-1660-11e8-8d82-10286e504cb4.png)\r\n\r\n运行的时候，会有这个界面，点击 Accept\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36415564-58d9deb0-1661-11e8-95a1-71cffa4c6b15.png)\r\n\r\n\r\n\r\n## 创建项目\r\n\r\npycharm 中的代码文件都是存在一个项目中的。\r\n\r\n所以要创建代码和编辑代码，必须先创建一个项目。\r\n\r\npycharm的项目对应一个目录，里面包含了所有的项目文件，包括代码文件和其他的配置文件。\r\n\r\n首次启动后，点击这里，去创建一个项目\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36415727-dd0d12b0-1661-11e8-8742-04e9d9b141da.png)\r\n\r\n\r\n\r\n然后出现下面的界面，这个界面的操作非常重要。\r\n\r\n这是 选择 Pycharm 解析代码 和 运行代码时， 所用的解释器环境。\r\n\r\n点击下图箭头处，展开选择项\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36415860-4e3c2ae8-1662-11e8-9027-da2cb7af83f9.png)\r\n\r\n\r\n展开选项后，如下图所示\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36415954-a7e67012-1662-11e8-8e4c-c58793b819b3.png)\r\n\r\n其中\r\n\r\n第1个选项 `New environment using ...` 是 新建一个虚拟环境\r\n\r\n第2个选项 `Existing interpreter ...` 是 在已有的解释器环境里面选择\r\n\r\n！！ 建议 初学者 不要选择第1个选项 ， 那样的话 以后pycharm项目运行的时候，就会使用该虚拟环境里面的解释器。\r\n\r\n比如，创建一个如下图所示的虚拟环境，生成对应的Python解释器目录\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_38398996-2b67bcea-397a-11e8-9a28-ae405d522987.png)\r\n\r\n使用 虚拟环境 其实挺好，问题是 很多初学者 安装第三方库的时候，通常都是 直接执行命令pip安装，比如\r\n\r\n```py\r\npip install selenium\r\n```\r\n\r\n由于环境变量path里面是 `Python基础环境` 里面的 pip 目录， 所以 这些库 都是装在了基础环境里面，而不是pycharm里面创建的虚拟环境。\r\n\r\n而Pycharm运行代码的时候，如果使用虚拟环境，虚拟环境 里面 并没有 pip 安装的 库，所以执行代码时，解释器会 报 找不到库的报错。\r\n\r\n\r\n\r\n所以 我建议初学者 选择第2个选项， 使用基础 Python 解释器运行环境（就是你安装目录下面的解释器）， 如下图所示，点击这里\r\n\r\n这样选择\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36416102-09adaa2c-1663-11e8-952b-75b1912624aa.png)\r\n\r\n然后点击 Create\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36416154-3043065a-1663-11e8-8ee6-4efaf01947b0.png)\r\n\r\n\r\n\r\n### 修改项目使用的 Python解释器\r\n\r\n如果你创建项目的时候不小心选错了Python解释器环境（比如上面说的，选择了虚拟环境里面的解释器）， 没有关系，可以点击这里\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_37892886-e99575bc-310b-11e8-9830-30ec1482d26a.png)\r\n\r\n然后重新选择正确的解释器，如下所示\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_37892985-4087a1ba-310c-11e8-9555-e711580354c3.png)\r\n\r\n## 新建目录和文件\r\n\r\n我们要新建python代码文件，可以用鼠标右键点击项目的目录，在弹出的菜单里面选择 New -> Python File\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36417077-e7c80698-1665-11e8-9b0d-fcc33fa1e34a.png)\r\n\r\n在随后弹出对话框中输入文件名，就可以了\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36417171-279888a6-1666-11e8-9223-7e881436f804.png)\r\n\r\n这样就在项目根目录下面创建了Python 文件。\r\n\r\n接下来就可以在右边的编辑框里面编辑代码文件了。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36417279-720f5b58-1666-11e8-9d18-a804ddcfc40d.png)\r\n\r\n这样创建的代码文件时在项目根目录下面。如果我们需要把众多的代码分类存放，就需要创建子目录。\r\n\r\n创建子目录也非常简单，用鼠标右键点击项目的目录，在弹出的菜单里面选择 Directory 或者 Python Package\r\n\r\n后者创建目录的同时会自动在该目录下面创建一个 `__init__.py` 文件。\r\n\r\n## 编辑代码助手功能\r\n\r\nPycharm 在我们编辑代码的时候，会有很实用的功能，帮助我们提高开发效率\r\n\r\n### 自动补齐\r\n\r\n我们在Pycharm 编辑代码，输入标识符（变量名、函数名等）的时候，Pycharm会猜测我们想要输入的内容，给出候选项，如下所示。\r\n\r\n如果我们要输入的是下面列表中的第一个候选项，直接敲回车就可以自动补齐了\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36431042-83c06ad8-1691-11e8-9e1a-c556bddd73c8.png)\r\n\r\n如果我们要输入的不是第一个，可以继续输入其余部分，IDE会随着我们输入的更多，更准确的推测，给出更精确的候选项。\r\n\r\n当然我们也可以用上下方向键选择候选项。\r\n\r\n自动补齐可以帮我提高输入速度，而且可以避免输错某个字母。这样就提高了编辑代码的效率。\r\n\r\n### 代码导航\r\n\r\n我们查看代码的时候，经常需要查看当前使用的某个变量、或者函数的定义。 这时候，我们不需要回忆在哪个代码文件中定义的，再手动去打开对应的文件，上下翻动文件去查找定义。\r\n\r\n只需要按着Ctrl键，然后鼠标点击那个变量或者函数，IDE就会自动跳转到它们定义的地方。\r\n\r\n看完定义后，要返回刚才的代码位置，可以点击工具栏下图所示图标\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36462795_41018882-bb24e210-698e-11e8-9f9c-1fdb0d7e2cdc.png)\r\n\r\n就可以回到跳转前的代码位置，继续编辑。\r\n\r\n如果你只是想要查看定义，不需要修改代码，可以把光标放在该标识符上，按 Ctrl + Shift + I 三个键，就会弹出信息框，显示它们的定义。如下所示\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36431702-384dabae-1693-11e8-8990-5f214b811bcc.png)\r\n\r\n------\r\n\r\n#### Pycharm 如何寻找导入的模块？\r\n\r\n我们看下面的代码\r\n\r\n![image](https://cdn2.byhy.net/imgs/gh/36462795_52516044-5751ba80-2c5f-11e9-9432-63acfc836e3c.png)\r\n\r\n为什么 import grab 有红色波浪线呢？\r\n\r\n原来导入的 grab 模块 在目录 lib2 中。\r\n\r\nPycharm 解析代码的时候， 搜索导入模块的 路径主要包括\r\n\r\n1. 当前项目根目录\r\n2. 项目使用的 Python解释器环境的 库目录\r\n3. 环境变量 PYTHONPATH 里面包含的目录\r\n\r\n而 grab 都不在那些目录中。 所以提示找不到模块的错误。\r\n\r\n我们可以 额外的添加目录，作为 Pycharm 的模块搜索路径。\r\n\r\n方法就是 右键点击 要添加的目录，在弹出的菜单中 选择 Mark Directory as -> Source Root\r\n\r\n如下图所示\r\n\r\n![image](https://cdn2.byhy.net/imgs/gh/36462795_52516109-51100e00-2c60-11e9-99ca-5e5b28ade2eb.png)\r\n\r\n这样，会把 grab目录页设置为 模块搜索路径。 就不会出现告警了。\r\n\r\n### 错误提示\r\n\r\n当我们不小心输错标识符的时候，Pycharm会在错误的下方显示一个红色的波浪线。\r\n\r\n当把鼠标放在波浪线的下方，会显示错误提示的信息。\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36432331-e88b874c-1694-11e8-96fa-b849dc7b90d6.png)\r\n\r\n这样不需要等到我们运行程序的时候才发现错误，也会提高开发效率。\r\n\r\n### 注释一段代码\r\n\r\nPython中注释代码是在前面加 #\r\n\r\n有时，我们需用注释掉大量的代码，这时候，就可以选中这些代码，按 Ctrl + 斜杠/ 就可以注释他们了。不需要我们一个个的在每行前面加 #号\r\n\r\n如果我们后来又要取消注释，只需再次 按 Ctrl + 斜杠/ 就可以取消注释了。\r\n\r\n## 运行代码\r\n\r\nPycharm要运行某个代码，只需右键点击该文件，在弹出菜单中选择 Run xxx.py 就可以了，如下所示\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36433068-a3de735a-1696-11e8-8539-da2b5acc4d13.png)\r\n\r\n运行时终端输出到屏幕的内容 显示在下面的窗口\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36433169-da3aff54-1696-11e8-973f-c7c7f76636f8.png)\r\n\r\n如果我们要运行的脚本需要设置相应的参数，可以点击这里的Edit Configurations\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36433328-34fb23ba-1697-11e8-9cad-1f8a7722f200.png)\r\n\r\n在弹出的运行配置对话框，设置相应的参数，比如下图的脚本运行 参数 可以这样添加\r\n\r\n![白月黑羽Python3教程](https://cdn2.byhy.net/imgs/gh/36257654_36433476-901a2f16-1697-11e8-992a-6d889849b6ce.png)\r\n\r\n这样，运行代码的时候，就相当于执行如下命令\r\n\r\n```\r\npython  C:\\projects\\first\\my.py   username   password\r\n```\r\n\r\n这个对话框还有其他的参数， 比如设置运行时的工作目录、环境变量等，都可以根据需要进行相应的设置。', '适合初学者的Pycharm安装和使用教程', 2, 'https://cdn2.byhy.net/imgs/gh/36257654_36417077-e7c80698-1665-11e8-9b0d-fcc33fa1e34a.png', '1', '0', 115, '0', NULL, '2023-04-22 14:58:34', NULL, NULL, 0);
INSERT INTO `article` VALUES (4, 'Spring Boot实现验证码生成及验证功能', '## 1. 验证码介绍\r\n\r\n验证码（CAPTCHA）是Completely Automated Public Turing test to tell Computers and Humans Apart（全自动区分计算机和人类的图灵测试）的缩写，主要作用是防止不法分子在短时间内用机器进行批量的重复操作给网站带来破坏，从而保护系统的安全。\r\n\r\n\r\n\r\n验证码的形式：\r\n\r\n1. 传统输入式验证码\r\n2. 手机短信验证码\r\n3. 图片识别与选择型验证码\r\n4. 滑块类型验证码\r\n5. 其他类型验证码，如指纹识别、人脸识别等\r\n\r\n\r\n\r\n## 2. Spring Boot整合easy-captcha生成验证码\r\n\r\n> **添加easy-captcha依赖**\r\n\r\neasy-captcha是一款国人开发的验证码工具，支持GIF、中文、算术等类型，可用于Java Web等项目\r\n\r\n```xml\r\n<dependency>\r\n    <groupId>com.github.whvcse</groupId>\r\n    <artifactId>easy-captcha</artifactId>\r\n    <version>1.6.2</version>\r\n</dependency>\r\n```\r\n\r\n\r\n\r\n> **流程**\r\n\r\n* 在后端生成验证码后，对当前生成的验证码内容进行保存，可以选择保存在session对象中，或者保存在Redis缓存中等。然后，返回验证码图片并显示到前端页面。\r\n\r\n* 用户在识别验证码后，在页面对应的输入框中填写验证码并向后端发送请求，后端在接到请求后会对用户输入的验证码进行验证。\r\n* 如果用户输入的验证码与之前保存的验证码不相等的话，则返回“验证码错误”的提示消息且不会进行后续的流程，只有验证成功才会继续后续的流程。\r\n\r\n\r\n\r\n> **生成并显示验证码**\r\n\r\n1. 在static目录中新建 `captcha.html` 页面，在该页面中显示验证码\r\n\r\n   ```html\r\n   <!DOCTYPE html>\r\n   <html lang=\"en\">\r\n   <head>\r\n       <meta charset=\"UTF-8\"/>\r\n       <title>验证码显示</title>\r\n   </head>\r\n   <body>\r\n   <img src=\"/captcha\" onclick=\"this.src=\'/captcha\'\"/>\r\n   </body>\r\n   </html>\r\n   ```\r\n\r\n2. 在controller包中新建 `CaptchaController` 类。在 `generateCaptcha()` 方法里使用SpecCaptcha可以生成一个PNG类型的验证码对象，并以图片流的方式输出到前端以供显示\r\n\r\n   ```java\r\n   @RequestMapping(\"/captcha\")\r\n   public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException, FontFormatException {\r\n       // 3个设置都是为了不缓存响应数据，同时设置可以兼容不同的浏览器或缓存服务器\r\n       response.setHeader(\"Cache-Control\", \"no-store\");\r\n       response.setHeader(\"Pragma\", \"no-cache\");\r\n       response.setDateHeader(\"Expires\", 0);\r\n       // 响应内容是一个GIF格式的图像\r\n       response.setContentType(\"image/gif\");\r\n   \r\n       // 生成指定类型的验证码对象，三个参数分别为宽、高、位数\r\n       // png类型\r\n       SpecCaptcha captcha = new SpecCaptcha(300, 120,4);\r\n       // gif类型\r\n       //        GifCaptcha captcha = new GifCaptcha(300, 120, 4);\r\n       // 中文类型\r\n       //        ChineseCaptcha captcha = new ChineseCaptcha(300, 120,4);\r\n       // 中文gif类型\r\n       //        ChineseGifCaptcha captcha = new ChineseGifCaptcha(300, 120,4);\r\n       // 算术类型\r\n       //        ArithmeticCaptcha captcha = new ArithmeticCaptcha(300, 120, 5);\r\n   \r\n       // 设置字符类型\r\n       captcha.setCharType(Captcha.TYPE_DEFAULT);\r\n   \r\n       // 设置字体\r\n       captcha.setFont(Captcha.FONT_8, 40);\r\n   \r\n       // 验证码存入 session\r\n       request.getSession().setAttribute(\"verifyCode\", captcha.text().toLowerCase());\r\n   \r\n       // 输出图片流\r\n       captcha.out(response.getOutputStream());\r\n   }\r\n   ```\r\n\r\n\r\n\r\n> **验证码的输入验证**\r\n\r\n1. 在static目录中新建 `verify.html`，该页面会显示验证码，同时也包含供用户输入验证码的输入框和提交按钮\r\n\r\n   ```html\r\n   <!DOCTYPE html>\r\n   <html lang=\"en\">\r\n   <head>\r\n       <meta charset=\"UTF-8\"/>\r\n       <title>验证码测试</title>\r\n   </head>\r\n   <body>\r\n   <img src=\"/captcha\" onclick=\"this.src=\'/captcha\'\"/>\r\n   <br>\r\n   <input type=\"text\" maxlength=\"5\" id=\"code\" placeholder=\"请输入验证码\"/>\r\n   <button id=\"verify\">验证</button>\r\n   <br>\r\n   <p id=\"verifyResult\">\r\n   </p>\r\n   </body>\r\n   \r\n   <script src=\"//code.jquery.com/jquery-1.11.3.min.js\"></script>\r\n   <script>\r\n       $(\'#verify\').click(function () {\r\n           var code = $(\'#code\').val();\r\n           $.ajax({\r\n               type: \'GET\',\r\n               url: \'/verify\',\r\n               data: {\"code\": code},\r\n               success: function (result) {\r\n                   $(\'#verifyResult\').html(result);\r\n               },\r\n               error: function () {\r\n                   alert(\'请求失败\');\r\n               },\r\n           });\r\n       });\r\n   </script>\r\n   </html>\r\n   ```\r\n\r\n2. 在 `CaptchaController` 类中新增 `verify()` 方法\r\n\r\n   * **HttpSession**：可以作为参数直接传入方法，用于在会话中存储和获取属性\r\n\r\n   ```java\r\n   @RequestMapping(\"/verify\")\r\n   @ResponseBody\r\n   public String verify(String code, HttpSession session) {\r\n       if (!StringUtils.hasLength(code)) {\r\n           return \"验证码不能为空\";\r\n       }\r\n       String verifyCode = session.getAttribute(\"verifyCode\") + \"\";\r\n       if (!code.toLowerCase().equals(verifyCode)) {\r\n           return \"验证码错误\";\r\n       }\r\n       return \"验证成功\";\r\n   }\r\n   ```\r\n\r\n   \r\n\r\n\r\n\r\n\r\n\r\n', '使用Spring Boot生成验证码并进行后续的验证操作', 1, 'https://img0.baidu.com/it/u=41156083,2231346161&fm=253&fmt=auto&app=138&f=GIF?w=539&h=389', '0', '0', 44, '0', NULL, '2023-04-25 15:22:30', NULL, NULL, 0);

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `article_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `tag_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '标签id',
  PRIMARY KEY (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_tag
-- ----------------------------
INSERT INTO `article_tag` VALUES (1, 1);
INSERT INTO `article_tag` VALUES (1, 3);
INSERT INTO `article_tag` VALUES (2, 4);
INSERT INTO `article_tag` VALUES (3, 4);
INSERT INTO `article_tag` VALUES (4, 1);
INSERT INTO `article_tag` VALUES (4, 3);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(200) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名',
  `pid` bigint(200) NULL DEFAULT -1 COMMENT '父分类id，如果没有父分类为-1',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态0:正常,1禁用',
  `create_by` bigint(200) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(200) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 'Java', -1, 'java相关', '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `category` VALUES (2, 'Python', -1, 'python相关', '0', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '评论类型（0代表文章评论，1代表友链评论）',
  `article_id` bigint(20) NULL DEFAULT NULL COMMENT '文章id',
  `root_id` bigint(20) NULL DEFAULT -1 COMMENT '根评论id',
  `content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `to_comment_user_id` bigint(20) NULL DEFAULT -1 COMMENT '所回复的目标评论的userid',
  `to_comment_id` bigint(20) NULL DEFAULT -1 COMMENT '回复目标评论id',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, '1', 1, -1, '友链评论测试', -1, -1, 1, '2023-05-16 12:05:33', 1, '2023-05-16 12:05:33', 0);
INSERT INTO `comment` VALUES (2, '0', 1, -1, '文章评论测试', -1, -1, 1, '2023-05-16 12:06:38', 1, '2023-05-16 12:06:38', 0);
INSERT INTO `comment` VALUES (3, '0', 1, 2, '回复测试', 1, 2, 3, '2023-05-16 12:08:05', 3, '2023-05-16 12:08:05', 0);
INSERT INTO `comment` VALUES (4, '1', 1, 1, '回复测试', 1, 1, 3, '2023-05-16 12:08:21', 3, '2023-05-16 12:08:21', 0);
INSERT INTO `comment` VALUES (5, '0', 1, 2, '1', 3, 3, 1, '2023-05-16 12:09:02', 1, '2023-05-16 12:09:02', 0);
INSERT INTO `comment` VALUES (6, '1', 1, 1, '1', 3, 4, 1, '2023-05-16 12:09:18', 1, '2023-05-16 12:09:18', 0);

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `logo` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '2' COMMENT '审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友链' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES (1, '百度', 'https://storage-public.zhaopin.cn/user/avatar/1583843473427893323/3b455a32-17d4-4e39-b5b6-fa04a88c36ed.jpg', 'Baidu', 'https://www.baidu.com', '0', NULL, '2023-03-23 08:26:42', NULL, '2023-03-24 08:36:14', 0);
INSERT INTO `link` VALUES (2, '腾讯', 'https://bpic.51yuansu.com/pic3/cover/00/69/38/58ab18eda37be_610.jpg', 'Tencent', 'https://www.qq.com', '0', NULL, '2023-03-23 08:12:01', NULL, '2023-03-24 09:07:09', 0);
INSERT INTO `link` VALUES (3, '淘宝', 'https://img.zcool.cn/community/01febc5ff2bced11013ee04d9e732a.jpg', 'Taobao', 'https://www.taobao.com', '0', NULL, '2023-03-23 13:10:11', NULL, '2023-03-24 09:23:01', 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2029 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, 1, 'M', '0', '0', '', 'system', 0, '2021-11-12 10:46:19', 0, NULL, '系统管理目录', '0');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', 1, 'C', '0', '0', 'system:user:list', 'user', 0, '2021-11-12 10:46:19', 1, '2022-07-31 15:47:58', '用户管理菜单', '0');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', 1, 'C', '0', '0', 'system:role:list', 'peoples', 0, '2021-11-12 10:46:19', 0, NULL, '角色管理菜单', '0');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', 1, 'C', '0', '0', 'system:menu:list', 'tree-table', 0, '2021-11-12 10:46:19', 0, NULL, '菜单管理菜单', '0');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', 1, 'F', '0', '0', 'system:user:query', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', 1, 'F', '0', '0', 'system:user:add', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', 1, 'F', '0', '0', 'system:user:edit', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', 1, 'F', '0', '0', 'system:user:remove', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', 1, 'F', '0', '0', 'system:user:export', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', 1, 'F', '0', '0', 'system:user:import', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', 1, 'F', '0', '0', 'system:user:resetPwd', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', 1, 'F', '0', '0', 'system:role:query', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', 1, 'F', '0', '0', 'system:role:add', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', 1, 'F', '0', '0', 'system:role:edit', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', 1, 'F', '0', '0', 'system:role:remove', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', 1, 'F', '0', '0', 'system:role:export', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', 1, 'F', '0', '0', 'system:menu:query', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', 1, 'F', '0', '0', 'system:menu:add', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', 1, 'F', '0', '0', 'system:menu:edit', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', 1, 'F', '0', '0', 'system:menu:remove', '#', 0, '2021-11-12 10:46:19', 0, NULL, '', '0');
INSERT INTO `sys_menu` VALUES (2017, '内容管理', 0, 4, 'content', NULL, 1, 'M', '0', '0', NULL, 'table', NULL, '2022-01-08 02:44:38', 1, '2022-07-31 12:34:23', '', '0');
INSERT INTO `sys_menu` VALUES (2018, '分类管理', 2017, 1, 'category', 'content/category/index', 1, 'C', '0', '0', 'content:category:list', 'example', NULL, '2022-01-08 02:51:45', NULL, '2022-01-08 02:51:45', '', '0');
INSERT INTO `sys_menu` VALUES (2019, '文章管理', 2017, 0, 'article', 'content/article/index', 1, 'C', '0', '0', 'content:article:list', 'build', NULL, '2022-01-08 02:53:10', NULL, '2022-01-08 02:53:10', '', '0');
INSERT INTO `sys_menu` VALUES (2021, '标签管理', 2017, 6, 'tag', 'content/tag/index', 1, 'C', '0', '0', 'content:tag:index', 'button', NULL, '2022-01-08 02:55:37', NULL, '2022-01-08 02:55:50', '', '0');
INSERT INTO `sys_menu` VALUES (2022, '友链管理', 2017, 4, 'link', 'content/link/index', 1, 'C', '0', '0', 'content:link:list', '404', NULL, '2022-01-08 02:56:50', NULL, '2022-01-08 02:56:50', '', '0');
INSERT INTO `sys_menu` VALUES (2023, '写博文', 0, 0, 'write', 'content/article/write/index', 1, 'C', '0', '0', 'content:article:writer', 'build', NULL, '2022-01-08 03:39:58', 1, '2022-07-31 22:07:05', '', '0');
INSERT INTO `sys_menu` VALUES (2024, '友链新增', 2022, 0, '', NULL, 1, 'F', '0', '0', 'content:link:add', '#', NULL, '2022-01-16 07:59:17', NULL, '2022-01-16 07:59:17', '', '0');
INSERT INTO `sys_menu` VALUES (2025, '友链修改', 2022, 1, '', NULL, 1, 'F', '0', '0', 'content:link:edit', '#', NULL, '2022-01-16 07:59:44', NULL, '2022-01-16 07:59:44', '', '0');
INSERT INTO `sys_menu` VALUES (2026, '友链删除', 2022, 1, '', NULL, 1, 'F', '0', '0', 'content:link:remove', '#', NULL, '2022-01-16 08:00:05', NULL, '2022-01-16 08:00:05', '', '0');
INSERT INTO `sys_menu` VALUES (2027, '友链查询', 2022, 2, '', NULL, 1, 'F', '0', '0', 'content:link:query', '#', NULL, '2022-01-16 08:04:09', NULL, '2022-01-16 08:04:09', '', '0');
INSERT INTO `sys_menu` VALUES (2028, '导出分类', 2018, 1, '', NULL, 1, 'F', '0', '0', 'content:category:export', '#', NULL, '2022-01-21 07:06:59', NULL, '2022-01-21 07:06:59', '', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '0', '0', 0, '2021-11-12 10:46:19', 0, NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '0', '0', 0, '2021-11-12 10:46:19', 0, '2022-01-01 22:32:58', '普通角色');
INSERT INTO `sys_role` VALUES (3, '友链审核员', 'link', 1, '0', '0', NULL, '2022-01-16 06:49:30', NULL, '2022-01-16 08:05:09', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2017);
INSERT INTO `sys_role_menu` VALUES (2, 2018);
INSERT INTO `sys_role_menu` VALUES (2, 2019);
INSERT INTO `sys_role_menu` VALUES (2, 2021);
INSERT INTO `sys_role_menu` VALUES (2, 2023);
INSERT INTO `sys_role_menu` VALUES (2, 2028);
INSERT INTO `sys_role_menu` VALUES (3, 2017);
INSERT INTO `sys_role_menu` VALUES (3, 2022);
INSERT INTO `sys_role_menu` VALUES (3, 2024);
INSERT INTO `sys_role_menu` VALUES (3, 2025);
INSERT INTO `sys_role_menu` VALUES (3, 2026);
INSERT INTO `sys_role_menu` VALUES (3, 2027);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, 'SpringBoot', NULL, '2022-01-11 09:20:50', NULL, '2022-01-11 09:20:50', 0, '');
INSERT INTO `tag` VALUES (2, 'Web开发', NULL, '2022-01-11 09:20:55', NULL, '2022-01-11 09:20:55', 1, '');
INSERT INTO `tag` VALUES (3, 'Java', NULL, '2022-01-11 09:21:07', NULL, '2022-01-11 09:21:07', 0, '');
INSERT INTO `tag` VALUES (4, 'Python', NULL, '2022-01-13 15:22:43', NULL, '2022-01-13 15:22:43', 0, '');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户类型：0代表普通用户，1代表管理员',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phonenumber` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '$2a$10$Jnq31rRkNV3RNzXe0REsEOSKaYK8UgVZZqlNlNXqn.JeVcj2NdeZy', '1', '0', '', '', '1', '', NULL, '2023-02-01 09:21:12', NULL, '2023-02-01 09:25:22', 0);
INSERT INTO `user` VALUES (2, 'test', 'test', '$2a$10$Jnq31rRkNV3RNzXe0REsEOSKaYK8UgVZZqlNlNXqn.JeVcj2NdeZy', '0', '0', 'test@qq.com', '16666666666', '0', 'https://gss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/574e9258d109b3de57070594cbbf6c81810a4c96.jpg', NULL, '2023-03-02 14:16:33', NULL, '2023-03-02 18:11:31', 0);
INSERT INTO `user` VALUES (3, 'ptu', 'ptu', '$2a$10$Jnq31rRkNV3RNzXe0REsEOSKaYK8UgVZZqlNlNXqn.JeVcj2NdeZy', '0', '0', 'ptu@ptu.edu.cn', '18888888888', '0', NULL, NULL, '2023-03-02 15:14:32', NULL, '2023-03-02 15:14:40', 0);

SET FOREIGN_KEY_CHECKS = 1;
