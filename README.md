# JAVA常用工具类

## 1、主要工具说明

>**涉及各种常用工具**
- BeanUtil，实体工具类，用于实体工具类，对实体复制、字段附值、获取属性等等；
- CharsetUtil 字符工具类，用于字符集转换，获取系统字符等等；
- ClassUtil 类工具类，用于获取class、方法映射、注解反射等等；
- CollectionUtil 集合工具类，用于List、Map等一些集合处理；
- ConvertUtil 类型工具类，用于类型转换；
- DateUtil 日期工具类，用于转换日期类型、获取日期格式等等；
- DigestUtil 算法工具类，用于加密、解密算法等等；
- EscapeUtil 转义工具类，用于字节码转义、反转等等；
- FileTypeUtil 文件类型具类，用于获取文件类型、文件头部信息等等；
- FileUtil 文件工具类，用于获取文件、文件名、文件查找等等；
- HashUtil 哈希工具类，用于旋转哈希、一次哈希等等；
- HexUtil 进制工具类，用于判断是否16进制、转换16进制等等
- HttpUtil http工具类，用于头处理、数据交互、网络传输等等；
- ImageUtil 图片工具类，用于图片缩放、获取图片高度像素、图片切割等等；
- IoUtil 流工具类，用于流处理、流读写操作；
- JsonUtil 杰森工具类，用于序列化、反序列化、数据转换等等；
- LocalUtil 本地工具类，用于获到机器名、本地IP、IP较验等等；
- MathUtil 数学工具类，用于计算加、减、乘、除等复杂的计算等等；
- NetUtil 网络工具类，用于获取网络端口、IP转换、验证有效端口等等；
- ObjectUtil 总类工具类，用于判断对象是否为空、对象大小等等；
- RandomUtil 随便数工具类，用于获取随便种子、多少位随机数等等；
- ReUtil 正则工具类，用于将正则反解、字符验证等等；
- SecureUtil 安全工具类，用于MD5验证，文件加密等等；
- SerializationUtil 序列化工具类，用于文件序列化、反序列化等等；
- StringUtil 字符工具类，用于字符是否、是否相等、转换数组等等；
- SystemUtil 系统工具类，用于获到系统环境参数，例如：系统名称、操作系统等等；
- ThreadUtil 线程工具类，用于异步处理、线程池、并发等等；
- URLUtil 资源工具类，用于获取URL、URL路径、格式化等等；
- UuidUtil UUID工具类，用于获取UUID、UUID格式化等等；
- XmlUtil XML工具类，用于XML解析、创建、读取等等；
- ZipUtil 压缩工具类，用于打包、压缩等等；
>

## 2、使用规范

### 2.1、POM引用



```java
SNAPSHOT版本
<dependency>
	<groupId>com.jd</groupId>
	<artifactId>meeop</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>

RELEASE版本

<dependency>
	<groupId>com.jd</groupId>
	<artifactId>meeop</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 3、About

>
>    这里把开发中常用的一些工具类做一个整理，主要用到的时候不用再费力的Baidu或者Google了，大家有需要的可以随时借鉴走，同时也希望这个越来越强大，有什么错误的地方还希望各位不吝指出，让我得以完善，当然，在此声明，这些并不是我一个人，也用了许多前辈们的东西，这里一并感谢，目前持续更新中，希望大家也可以提出更好的意见，如对你有用就给一个Start吧！
>  
