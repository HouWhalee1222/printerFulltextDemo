# 安装指南

## 安装 Solr
* 下载Solr, 从Solr[官网](https://lucene.apache.org/solr/)下载最新版Solr并解压
* 启动Solr  
    cd solr-{your version}/bin  
    ./solr start  
    若可以成功访问 localhost:8983，说明Solr安装成功
* 创建 core  
    ./solr create -c printerData，最后一项为库的名字，这里以printerData为例  
## 配置 Solr
### 配置 core
* 上一步创建的 core 会在 solr/server/solr 目录下，删除 printerData 目录下的 conf 文件夹，复制 solr/example/example-DIH/solr/db 目录下的 conf 文件夹到 solr/server/solr/printerData 目录下  
* 找到 printerData/conf 文件夹下面的 managed-schema 文件，做如下修改：  
    删除所有不必要的 <field ...>（大致在125行到180行之间）  
    添加如下 filed：
    ```
    <field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" />
    <field name="title" type="text_cn" indexed="true" stored="true" multiValued="false"/>
    <field name="author" type="text_cn" indexed="true" stored="true" multiValued="false"/>
    <field name="last_modified" type="pdate" indexed="true" stored="true"/>
    <field name="printTime" type="pdate" indexed="true" stored="true" />
    <field name="content" type="text_cn" indexed="false" stored="true" multiValued="false"/>
    <field name="text" type="text_cn" indexed="true" stored="false" multiValued="true"/>
    ```
    这些只是 Sample，具体需要哪些 field，可以根据具体需要额外添加。  
    删除多余的 <copyField ... > (大致在230行前后)，根据之前添加的 field 配置 copyField，如：  
    ```
    <copyField source="title" dest="text"/>
    <copyField source="author" dest="text"/>
    <copyField source="content" dest="text"/>
    ```
### 配置中文分词  
将 solr/contrib/analysis-extras/lucene-libs/lucene-analyzers-smartcn-{version}.jar 复制到 solr/server/solr-webapp/webapp/WEB-INF/lib 目录下，并在 managed-schema 中添加如下信息（可以根据需要添加其他的中文分词器）：
```
<fieldType name="text_cn" class="solr.TextField" positionIncrementGap="100">
  <analyzer> 
    <tokenizer class="solr.org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
  </analyzer>
</fieldType>
```
## 配置 OCR
从[此处](https://github.com/tesseract-ocr/tessdata)下载所需识别的文字训练数据，放在服务器任意位置
## 修改项目中的配置
* 将 Document 文件中的 @SolrDocument(collection = "printerDemo") 中的 collection 设置为 Solr 中 core 的名字
* 将 data.solr.host 改为 solr 服务器所在的地址
* 将 data.solr.core 改为 solr 中创建的 core 的名字
* 将 ocr.datapath 设置为上一步中存放数据的路径
* 根据需要配置 datasource，注意，使用的表中的列名必须与 document 类以及 solr 中 managed-schema 中的 field 一一对应
* data.solr.keepDays 决定了保留几个月的数据，大于该天之前的数据会被清除
* data.solr.commitRate 决定了为几个文件建立索引后会 commit 到 solr 服务器
* async 为一些线程池的配置
* scheduler.cleanOldDocument.cron 设置了清理文件在每天什么时间段运行


    


    
    
    