/**
 * IK 中文分词 版本 5.0 IK Analyzer release 5.0
 *
 * <p>Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>源代码由林良益(linliangyi2005@gmail.com)提供 版权声明 2012，乌龙茶工作室 provided by Linliangyi and copyright 2012
 * by Oolong studio
 */
package com.rymcu.forest.lucene.cfg;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration 默认实现 2012-5-8
 */
@Component
public class DefaultConfig implements Configuration {

    /**
     * 分词器默认字典路径
     */
    private static final String PATH_DIC_MAIN = "lucene/main2012.dic";
    /**
     * 题词字典路径
     */
    private static final String PATH_DIC_QUANTIFIER = "lucene/quantifier.dic";
    /**
     * 用户自定义字典路径
     */
    private static final String PATH_USER_DIC =
            "lucene/userDic/userDic.dic";
    /**
     * 配置属性——扩展字典
     */
    private String extDic = "lucene/ext.dic;" + PATH_USER_DIC;
    /**
     * 配置属性——扩展停止词典
     */
    private String extStopword = "lucene/stopword.dic";
    /**
     * 是否使用smart方式分词
     */
    private boolean useSmart;

    /**
     * 返回单例
     *
     * @return Configuration单例
     */
    public static Configuration getInstance() {
        return new DefaultConfig();
    }

    /**
     * 返回useSmart标志位 useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @return useSmart
     */
    @Override
    public boolean useSmart() {
        return useSmart;
    }

    /**
     * 设置useSmart标志位 useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @param useSmart
     */
    @Override
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * 获取主词典路径
     *
     * @return String 主词典路径
     */
    @Override
    public String getMainDictionary() {
        return PATH_DIC_MAIN;
    }

    /**
     * 获取量词词典路径
     *
     * @return String 量词词典路径
     */
    @Override
    public String getQuantifierDictionary() {
        return PATH_DIC_QUANTIFIER;
    }

    /**
     * 获取扩展字典配置路径
     *
     * @return List<String> 相对类加载器的路径
     */
    @Override
    public List<String> getExtDictionary() {
        List<String> extDictFiles = new ArrayList<String>(2);
        if (extDic != null) {
            // 使用;分割多个扩展字典配置
            String[] filePaths = extDic.split(";");
            for (String filePath : filePaths) {
                if (filePath != null && !"".equals(filePath.trim())) {
                    extDictFiles.add(filePath.trim());
                }
            }
        }
        return extDictFiles;
    }

    /**
     * 获取扩展停止词典配置路径
     *
     * @return List<String> 相对类加载器的路径
     */
    @Override
    public List<String> getExtStopWordDictionary() {
        List<String> extStopWordDictFiles = new ArrayList<>(2);
        if (extStopword != null) {
            // 使用;分割多个扩展字典配置
            String[] filePaths = extStopword.split(";");
            for (String filePath : filePaths) {
                if (filePath != null && !"".equals(filePath.trim())) {
                    extStopWordDictFiles.add(filePath.trim());
                }
            }
        }
        return extStopWordDictFiles;
    }
}
