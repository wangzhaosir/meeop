# 金采交易系统

## 1、主要功能

>**只做核心交易，不处理业务复杂流程**
- 订单消费支付；
- 订单退款；
- 订单还款（线上收银台、线下、账扣）；
>

## 2、使用规范

### 2.1、POM引用

SNAPSHOT版本

```java
<dependency>
	<groupId>com.jd.jr</groupId>
	<artifactId>alchemist</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```
RELEASE版本
```java
<dependency>
	<groupId>com.jd.jr</groupId>
	<artifactId>alchemist</artifactId>
	<version>1.0.0</version>
</dependency>
```
### 2.2、JSF引用
```java
<!-- 金采交易服务 -->
<jsf:consumer id="alchemistService" interface="com.jd.jr.alchemist.export.AlchemistService" protocol="jsf"
              alias="${alchemist.alias}" timeout="60000" retries="0" check="false" />

```
alchemist.alias版本说明

>
- 测试环境 alchemist_1.0.0_test
- 预发环境 alchemist_1.0.0_pre
- 线上环境 alchemist_1.0.0
>

## 3、DEMO

```java
    alchemistService.trade("T001001001","{"aa"}");
```


## 4、研发规范

两个核心交易方法，invoke用来做转发请求，trade用来做交易请求；
```java
public interface AlchemistService<RES extends ResponseData> {
    /**
     * 转发请求
     * @param transCode
     * @param $
     * @return
     */
    RES invoke(String transCode, String $);

    /**
     * 交易请求
     * @param $ 请求参数
     * @return
     */
    RES trade(String transCode, String $);
}
```
