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
package com.rymcu.forest.lucene.dic;

import com.rymcu.forest.lucene.cfg.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * 词典管理类,单例模式
 */
@Slf4j
public class Dictionary {

    /**
     * 词典单例
     */
    private static Dictionary singleton;
    /**
     * 主词典对象
     */
    private DictSegment _MainDict;
    /**
     * 停止词词典
     */
    private DictSegment _StopWordDict;
    /**
     * 量词词典
     */
    private DictSegment _QuantifierDict;
    /**
     * 用户自定义词典路径
     */
    private static final String PATH_USER_DIC =
            "lucene/userDic/userDic.dic";
    /**
     * 配置对象
     */
    private final Configuration cfg;

    private Dictionary(Configuration cfg) {
        this.cfg = cfg;
        this.loadMainDict();
        this.loadStopWordDict();
        this.loadQuantifierDict();
    }

    /**
     * 词典初始化 由于IK Analyzer的词典采用Dictionary类的静态方法进行词典初始化 只有当Dictionary类被实际调用时，才会开始载入词典， 这将延长首次分词操作的时间
     * 该方法提供了一个在应用加载阶段就初始化字典的手段
     */
    public static void initial(Configuration cfg) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary(cfg);
                }
            }
        }
    }

    /**
     * 获取词典单子实例
     *
     * @return Dictionary 单例对象
     */
    public static Dictionary getSingleton() {
        if (singleton == null) {
            throw new IllegalStateException("词典尚未初始化，请先调用initial方法");
        }
        return singleton;
    }

    /**
     * 批量加载新词条
     *
     * @param words Collection<String>词条列表
     */
    public void addWords(Collection<String> words) {
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    // 批量加载词条到主内存词典中
                    singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }
    }

    /**
     * 批量移除（屏蔽）词条
     *
     * @param words
     */
    public void disableWords(Collection<String> words) {
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    // 批量屏蔽词条
                    singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }
    }

    /**
     * 检索匹配主词典
     *
     * @param charArray
     * @return Hit 匹配结果描述
     */
    public Hit matchInMainDict(char[] charArray) {
        return singleton._MainDict.match(charArray);
    }

    /**
     * 检索匹配主词典
     *
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return singleton._MainDict.match(charArray, begin, length);
    }

    /**
     * 检索匹配量词词典
     *
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
        return singleton._QuantifierDict.match(charArray, begin, length);
    }

    /**
     * 从已匹配的Hit中直接取出DictSegment，继续向下匹配
     *
     * @param charArray
     * @param currentIndex
     * @param matchedHit
     * @return Hit
     */
    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);
    }

    /**
     * 判断是否是停止词
     *
     * @param charArray
     * @param begin
     * @param length
     * @return boolean
     */
    public boolean isStopWord(char[] charArray, int begin, int length) {
        return singleton._StopWordDict.match(charArray, begin, length).isMatch();
    }

    /**
     * 加载主词典及扩展词典
     */
    private void loadMainDict() {
        // 建立一个主词典实例
        _MainDict = new DictSegment((char) 0);
        // 读取主词典文件
        Resource resource = new ClassPathResource(cfg.getMainDictionary());
        try {
            InputStream is = resource.getInputStream();
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 512);
            String theWord;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                }
            } while (theWord != null);
        } catch (IOException e) {
            System.err.println("Main Dictionary loading exception.");
            e.printStackTrace();
        }
        // 加载扩展词典
        this.loadExtDict();
    }

    /**
     * 加载用户配置的扩展词典到主词库表
     */
    private void loadExtDict() {
        // 加载扩展词典配置
        List<String> extDictFiles = cfg.getExtDictionary();
        if (extDictFiles != null) {
            InputStream is;
            for (String extDictName : extDictFiles) {
                // 读取扩展词典文件
                log.info("加载扩展词典：" + extDictName);
                is = this.getClass().getClassLoader().getResourceAsStream(extDictName);
                // 如果找不到扩展的字典，则忽略
                if (is == null) {
                    try {
                        is = new FileInputStream(extDictName);
                    } catch (FileNotFoundException e) {
                        continue;
                    }
                }
                try {
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 512);
                    String theWord;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            // 加载扩展词典数据到主内存词典中
                            log.info(theWord);
                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                        }
                    } while (theWord != null);
                } catch (IOException ioe) {
                    System.err.println("Extension Dictionary loading exception.");
                    ioe.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 加载用户扩展的停止词词典
     */
    private void loadStopWordDict() {
        // 建立一个主词典实例
        _StopWordDict = new DictSegment((char) 0);
        // 加载扩展停止词典
        List<String> extStopWordDictFiles = cfg.getExtStopWordDictionary();
        if (extStopWordDictFiles != null) {
            InputStream is = null;
            for (String extStopWordDictName : extStopWordDictFiles) {
                log.info("加载扩展停止词典：" + extStopWordDictName);
                // 读取扩展词典文件
                is = this.getClass().getClassLoader().getResourceAsStream(extStopWordDictName);
                // 如果找不到扩展的字典，则忽略
                if (is == null) {
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            // 加载扩展停止词典数据到内存中
                            _StopWordDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                        }
                    } while (theWord != null);
                } catch (IOException ioe) {
                    System.err.println("Extension Stop word Dictionary loading exception.");
                    ioe.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 加载量词词典
     */
    private void loadQuantifierDict() {
        // 建立一个量词典实例
        _QuantifierDict = new DictSegment((char) 0);
        // 读取量词词典文件
        InputStream is =
                this.getClass().getClassLoader().getResourceAsStream(cfg.getQuantifierDictionary());
        if (is == null) {
            throw new RuntimeException("Quantifier Dictionary not found!!!");
        }
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 512);
            String theWord;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    _QuantifierDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                }
            } while (theWord != null);
        } catch (IOException ioe) {
            System.err.println("Quantifier Dictionary loading exception.");
            ioe.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载用户配置的自定义扩展词典到主词库表
     */
    public void updateUserDict() {
        // 加载扩展词典配置
        InputStream is;
        // 读取扩展词典文件
        log.info("更新加载扩展词典：" + PATH_USER_DIC);
        try {
            is = new FileInputStream(PATH_USER_DIC);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 512);
            String theWord;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    // 加载扩展词典数据到主内存词典中
                    log.info(theWord);
                    _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                }
            } while (theWord != null);
        } catch (IOException ioe) {
            System.err.println("Extension Dictionary loading exception.");
            ioe.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
